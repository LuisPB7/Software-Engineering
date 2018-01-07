package pt.tecnico.mydrive.domain;

import java.io.BufferedReader;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.SpinnerListModel;

import antlr.collections.List;
import java.io.File;

import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.Document;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.ist.fenixframework.*;
import pt.tecnico.mydrive.exception.CircularLinkException;
import pt.tecnico.mydrive.exception.DeleteNobodyException;
import pt.tecnico.mydrive.exception.DirectoryNotFoundException;
import pt.tecnico.mydrive.exception.DuplicatedDirectoryException;
import pt.tecnico.mydrive.exception.DuplicatedUserException;
import pt.tecnico.mydrive.exception.ExecutePermissionDeniedException;
import pt.tecnico.mydrive.exception.InvalidFileNameFormatException;
import pt.tecnico.mydrive.exception.InvalidFilePathLengthException;
import pt.tecnico.mydrive.exception.InvalidUserNameFormatException;
import pt.tecnico.mydrive.exception.SessionDoesNotExistException;
import pt.tecnico.mydrive.exception.UnknownVariableException;
import pt.tecnico.mydrive.exception.UserDoesNotExistException;
import pt.tecnico.mydrive.exception.WritePermissionDeniedException;
import pt.tecnico.mydrive.exception.FileNotFoundException;
import pt.tecnico.mydrive.exception.InvalidAppFormatException;
import pt.tecnico.mydrive.exception.InvalidFileFormatException;

import org.jdom2.*;

@SuppressWarnings("unused")
public class MyDrive extends MyDrive_Base {
    
    /**
     *  class MyDrive
     * 
     *  class that implements the application manager for the MyDrive file system
     *  
     */
    static final Logger log = LogManager.getRootLogger();
    static final Map <String, MyDriveFile> map = new TreeMap<String, MyDriveFile>(); //For the extensions

    public static MyDrive getInstance() {
        MyDrive md = FenixFramework.getDomainRoot().getMydrive();
        if (md != null) {
            return md;
        }
        log.trace("new MyDrive");
        return new MyDrive();
    }
    /*
     * private MyDrive() { setRoot(FenixFramework.getDomainRoot()); }
     */

    //private XMLExporter _xmlExporter;
    //private XMLImporter _xmlImporter;

    
    public MyDrive() {
        super();
        setRoot(FenixFramework.getDomainRoot());
        initMyDrive();
    }
    /**
     * initMyDrive
     * creates the mydrive application, with the initial user, the root an his directory
     */
    protected void initMyDrive() {
        Root root;
        Nobody nobody;
       // User pauleta;
        Directory dirRoot, home, dirNobody;
        set_creationId(0);

        // CREATE HOME DIRECTORY
        home = new Directory(get_creationId(), "home", "/home", "rwxd"); // creates home directory
        home.setParent(home);                                                                  
        home.setCurrent(home);                                                                   
        incSysId();


        
        root = User.getInstanceRootUser(this); // creates user root
        addUserList(root);
        incSysId();
        //
        home.linkToCreator(root);
        home.setDirectories(home);
        addFileList(home);
        //
        dirRoot = new Directory(get_creationId(), "root", "/home/root", "rwxd"); // creates root directory
                                                                                
        dirRoot.linkToCreator(root);
        dirRoot.setDirectories(home);
        addFileList(dirRoot);
        incSysId();
        
        
        //
        nobody = User.getInstanceNobodyUser(this); // creates user nobody
        addUserList(nobody);
        incSysId();
        //
        dirNobody = new Directory(get_creationId(), "Guest", "/home/Guest", "rwxd"); // creates nobody directory
        
        dirNobody.linkToCreator(nobody);
        dirNobody.setDirectories(home);
        addFileList(dirNobody);
        incSysId();
        
        
//        pauleta = createUser("ritto", "rittoPW"); // creates user ritto
//        addUserList(pauleta);
//        incSysId();

    }

    /**
     * incSysId()
     * Increment of the system id counter
     * 
     */

    public void incSysId() {
        set_creationId(get_creationId() + 1);
    }

    /**
     * decSysId()
     * Decrement of the system id counter
     * 
     */

    private void decSysId() {
        set_creationId(get_creationId() - 1);
    }

    /**
     * User getUser(String username)
     * receives a username and return the correspondent user
     * @param username
     * @return u
     * u is the username pretended to get
     */
    public User getUser(String username) {
        for (User u : getUserListSet()) {
            if (u.get_userName().equals(username)) {
                return u;
            }
        }
        throw new UserDoesNotExistException(username);
    }

    /**
     * getDirectory(String dirName, String path)
     * receives a directory name and his location and return the correspondent directory
     * @param dirName
     * @param path
     * @return dir
     * dir is the directory pretended to get
     */
    public Directory getDirectory(String dirName, String path) {

        for (MyDriveFile dir : getFileListSet()) {
            if ((dir instanceof Directory)
            	&&((Directory) dir).get_path().equals(path)) {
            	if(dir.get_filePermission().charAt(2)=='x')
            		return (Directory) dir;
            	else{
            		throw new ExecutePermissionDeniedException("", dir.get_fileName());
            	}
            }
        }

        throw new DirectoryNotFoundException(dirName, path);
    }
    
    public Directory getDirectory(String path) throws DirectoryNotFoundException{
    	if(!path.contains("/"))
    		throw new DirectoryNotFoundException("UNKNOWN FROM PATH", path);
    	String[] parsedPath=path.split("/");
    	int namePos = parsedPath.length - 1;
    	return getDirectory(parsedPath[namePos],path);
    }
    

    /**
     * getDirectory(String dirName, String path)
     * receives a directory name and his location and return the correspondent directory
     * @param dirName
     * @param path
     * @return dir
     * dir is the directory pretended to get
     */
    public Directory getDirectory(String dirName, String path, Directory currentDir) {

        for (MyDriveFile dir : currentDir.getDirectoryFilesSet()) {
            if ((dir instanceof Directory)
            	&&((Directory) dir).get_path().equals(path)){
            	if(dir.get_filePermission().charAt(2)=='x'){
                return (Directory) dir;
            	}else{
            		throw new ExecutePermissionDeniedException("", dir.get_fileName());
            	}
            }
        }

        throw new DirectoryNotFoundException(dirName, path);
    }


    /**
     * userExists(String username)
     * receives a username and check if it does already(return true) exists in the my drive application or not(return false)
     * @param username
     * @return boolean
     */
    public boolean userExists(String username) {
        for (User u : getUserListSet()) {
            if (u.get_userName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * createUser(String username)
     * receives a username and creates a user with the parameter username(if posible)
     * @param username
     * @return u
     * u is the user created
     */
    public User createUser(String username) {
        Directory d;
        Directory home = getDirectory("home", "/home");
        String pattern = "^[a-zA-Z0-9]+$";
        if (!(username.matches(pattern))) {
            throw new InvalidUserNameFormatException(username);
        }
        if (!(username.length()>2)) {
            throw new InvalidUserNameFormatException(username);
        }
        if (!userExists(username)) {
            User u = new User(username, get_creationId());
            addUserList(u);
            incSysId();
            //////
            addFileList(createDirectory(u.get_userName(), u.get_homeDir(), u, home));

            return u;

        }
        throw new DuplicatedUserException(username);
    }
    
  //public Nobody createNobody (String username){}
    

    /**
     * User createUser(String username, String password)
     * receives an username, and a passwor, verifies the username in order
     * to check if it is allowed according with the systems conditions
     * and if it valid creates a new user
     * @param username
     * @param password
     * @return u
     */
    public User createUser(String username, String password) {
        Directory d;
        Directory home = getDirectory("home", "/home");
        String pattern = "^[a-zA-Z0-9]+$";
        if (!(username.matches(pattern))) {
            throw new InvalidUserNameFormatException(username);
        }
        if (!(username.length()>2)) {
            throw new InvalidUserNameFormatException(username);
        }
        if (!userExists(username)) {
            User u = new User(username, get_creationId());
            u.set_password(password);
            addUserList(u);
            incSysId();
            //////
            addFileList(createDirectory(u.get_userName(), u.get_homeDir(), u, home));

            return u;

        }
        throw new DuplicatedUserException(username);
    }


    /**
     * createDirectory(String directoryName, String directoryPath, User creator, Directory parent)
     * Creates a directory with the given parameters
     * @param directoryName
     * @param directoryPath
     * @param creator
     * @param parent
     * @return d
     * d is the directory created
     */
    public Directory createDirectory(String directoryName, String directoryPath, User creator, Directory parent) {
        Directory d;
        
        	d = new Directory(get_creationId(), directoryName, directoryPath, generateFilePermission(creator));
        	//System.out.println(d);
            d.linkToCreator(creator);
            d.setDirectories(parent);
            addFileList(d);
            incSysId();
            return d;
    }
    
    /**
     * createDirectory(String directoryName, String directoryPath, User creator, Directory parent)
     * receives a name, path and a creator and creates the 
     * respective Directory with the given parameters
     * @param directoryName
     * @param directoryPath
     * @param creator
     * @return
     */
    public Directory createDirectory(String directoryName, String directoryPath, User creator) {
    	// Set<MyDriveFile> listDir=getFileListSet();

    	Directory 		d=null;
    	Directory		parentDir=null;
    	String[] 		splitedPath=directoryPath.split("/");
    	String			currentDirName;
    	String			currentDirPath=new String();
    	int				index=1;

    	checkPathLenght(directoryPath);
    	try{
    		d=getDirectory(directoryName, directoryPath);
    		throw new DuplicatedDirectoryException(directoryName, directoryPath);
    	}
    	catch(DirectoryNotFoundException e){
    		do{

    			currentDirName=splitedPath[index];
    			currentDirPath+="/"+currentDirName;

    			try{
    				parentDir = getDirectory(currentDirName, currentDirPath);
    			}catch(DirectoryNotFoundException dirEx){
    				d=createDirectory(currentDirName, currentDirPath, creator,parentDir);
    				parentDir=d;
    			}
    			index+=1;
    		}while(index<splitedPath.length);

    		return d;
    	}
    }
    
    /**
     * Link createPlainFile(String name, String path, String permission, String content, User creator, Directory parent)
     * Create a new Plain file wit the given parameters
     * @param name
     * @param path
     * @param permission
     * @param content
     * @param creator
     * @param parent
     * @return p
     * p is the plain file created
     */
    public Plain createPlainFile(String name, String path, String permission, String content, User creator, Directory parent) {
        Plain p;
        p = new Plain(get_creationId(), name, path, permission, content);
            p.linkToCreator(creator);
            p.insertIntoDir(parent);
            map.put("PlainExtension", p);
            addFileList(p);
            incSysId();
            return p;
    }
    
    /**
     * Link createLinkFile(String name, String path, String permission, String content, User creator, Directory parent)
     * Create a new Link file wit the given parameters
     * @param name
     * @param path
     * @param permission
     * @param content
     * @param creator
     * @param parent
     * @return p
     * p is the Link file created
     */
    public Link createLinkFile(String name, String path, String permission, String content, User creator, Directory parent) {
        Link p;
        p = new Link(get_creationId(), name, path, permission, content);
            p.linkToCreator(creator);
            p.insertIntoDir(parent);
            map.put("LinkExtension", p);
            addFileList(p);
            incSysId();
            return p;
    }
    
    
    /**
     * createAppFile(String name, String path, String permission, String content, User creator, Directory parent)
     * Create a new App file wit the given parameters
     * @param name
     * @param path
     * @param permission
     * @param content
     * @param creator
     * @param parent
     * @return p
     * p is the App file created
     */
    public App createAppFile(String name, String path, String permission, String content, User creator, Directory parent) {
        App p;
        p = new App(get_creationId(), name, path, permission, content);
            p.linkToCreator(creator);
            p.insertIntoDir(parent);
            map.put("AppExtension", p);
            addFileList(p);
            incSysId();
            return p;
    }
    /**
     * createFile(String name, String path, String permission, String content, User creator, Directory parent, String type)
     * Creates a new file with the given parameters
     * @param name
     * @param path
     * @param permission
     * @param content
     * @param creator
     * @param parent
     * @param type
     * @return MyDriveFile
     */
    public MyDriveFile createFile(String name, String path, String content, User creator, Directory parent, String type) {
        String pattern = "^[a-zA-Z0-9_]+$";
    	if (!(name.matches(pattern))) {
            throw new InvalidFileNameFormatException(name);
        }
    	if(!(parent.get_filePermission().charAt(1)=='w'))
    		throw new WritePermissionDeniedException(creator.get_userName(), parent.get_fileName());
    	checkPathLenght(path);
    	
    	if(!((type.equals("Directory"))||
            (type.equals("Plain"))||
            (type.equals("App"))||
            (type.equals("Link")))){
            throw new InvalidFileFormatException(type);
        }
        switch(type){
        case("Directory"):
            return createDirectory(name, path, creator);
        case("Plain"):
            return createPlainFile(name, path, generateFilePermission(creator), content, creator, parent);
        case("App"):
//        	AppContentValidator(content);
            return createAppFile(name, path, generateFilePermission(creator), content, creator, parent);
        case("Link"):
            return createLinkFile(name, path, generateFilePermission(creator), content, creator, parent);
        default:
            return null;
        }
    }
    
//    public void AppContentValidator(String content){
//    	if(!(content == null||content.contains(" "))){
//    		String app[]=content.split("\\.");
//    		if(app.length<=3){
//    			for(int i=0; i<app.length;i++){
//    				if(app[i].equals("")){
//    					throw new InvalidAppFormatException(content);
//    				}
//    			}
//    			return;
//    		}
//    			
//    	}
//    	throw new InvalidAppFormatException(content);
//    }
    
    /**
     * MyDriveFile getFileByName(String filename)
     * receives a filename and returns the file 
     * correspondent to that name, if it exists 
     * @param filename
     * @return f
     * @throws FileNotFoundException
     */
    public MyDriveFile getFileByName(String filename){
    	for (MyDriveFile f: getFileListSet()) {
    		if(f.get_fileName()==filename){
    			return f;
    		}
    	}
    	throw new FileNotFoundException(filename);
    }
    
    
    public MyDriveFile getFileByPath(String path){
    	for (MyDriveFile f: getFileListSet()) {
    		if(f.get_path().equals(path)){
    			return f;
    		}
    	}
    	throw new FileNotFoundException(path);
    }
    
    
    
    /**
     * checkPathLenght(String fullPath)
     * receives a path and verifies if it doesn't
     * exceed the maximum length allowed
     * @param fullPath
     * @throws InvalidFilePathLengthException
     */
    private void checkPathLenght(String fullPath){
    	if(fullPath.length()>1024){
    		throw new InvalidFilePathLengthException(fullPath.length());
    	}
    }
    

/*
    public Text createTextFile(String name, String path, String permission, String content){
        Text file = new Plain(get_creationId(), name, path, permission, content);
        incSysId();
        return file;
    }
    
    */
    /**
     * addFile(MyDriveFile file)
     * receives a file and add it to the MyDriveFileList, if successful(returns true), in other case(returns false) 
     * @param file
     * @return boolean
     */
    boolean addFile(MyDriveFile file) {
        Set<MyDriveFile> fileList = getFileListSet();
        fileList.add(file);
        return fileList.contains(file);
    }

    /**
     * ListDirectory(String path)
     * Receives a path and returns all directories in it
     * @param path
     * @return list
     * list is the list of all directories in the given path
     */
    public String ListDirectory(String path) {
        String list="LISTING DIRECTORY " +path +":\n";
        Set<MyDriveFile> fileList = getFileListSet();
        for (MyDriveFile f : fileList) {
            if (f instanceof Directory) {
                if (f.get_path().equals(path)) {
                    return f.listFile(list);
                    //return list;
                }
            }
        }
        return "Theres no directory\n";
    }

    /*String listFileContent(String path) throws FileNotFoundException, IOException {
        String content = "";
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                content += line;
            }
        }
        return content;
    }
     */
    
    /**
     * boolean fileDelete(String path)
     * receives a path and deletes the correspondent file, if successful(returns true), other case(returns false)
     * @param path
     * @return boolean
     * @throws FileNotFoundException
     */
    public boolean fileDelete(String path) throws FileNotFoundException {
    	
    	
        /*
         * String[] hierarchy =path.split("/"); String currentFileName;
         * Set<MyDriveFile> listOfFiles= getFileListSet(); int hierarchyIndex=0;
         * 
         * 
         * for(MyDriveFile f : listOfFiles){ currentFileName = f.get_fileName();
         * 
         * if(hierarchy[hierarchyIndex].equals(currentFileName)){
         * if(hierarchyIndex == hierarchy.length -1){ //WE'VE REACHED THE FILE
         * TO ERASE FROM THE SYSTEM f.deleteFromSystem(); removeFileList(f);
         * return true; }else{ //System.err.println(f); if(f instanceof
         * Directory){ hierarchyIndex+=1; listOfFiles= ((Directory)
         * f).getDirectoryFilesSet(); }else{ throw new
         * FileNotFoundException(path); } } } } return false;
         */
        for (MyDriveFile f : getFileListSet()) {
            String currentPath = f.get_path();
            if (currentPath.equals(path)) {
                removeFileList(f);
                f.deleteFromSystem();
                
                return true;
            }
        }
        throw new FileNotFoundException(path);
    }
    
    
    /**
     * deleteUser(User userdeleter, String username)
     * 
     * receives an userdeleter and an username of an user and deletes that user only 
     * if the userdeleter is root, and assigns every file from the deleted user to root
     * @param userdeleter
     * @param username
     */
    public void deleteUser(User userdeleter, String username){
		if(userdeleter instanceof Root){
			if(getUser(username) instanceof Nobody){
				throw new DeleteNobodyException(username);
			}
			for(User u: getUserListSet()){
				if(u.get_userName().equals(username)){
					removeUserList(u);
					Set<MyDriveFile> fileList = u.getFileSet();
					for(MyDriveFile f : fileList){
						u.removeFile(f);
						f.setCreator(null);
						f.linkToCreator(userdeleter);
					}
				}
			}
		}
	
	}

    
    /**
     * boolean fileExists(String filename)
     * receives a filename and check if it already exists on MyDrive or not
     * @param filename
     * @return boolean
     */
    public boolean fileExists(String filename){
		for(MyDriveFile f: getFileListSet()){
			if(f.get_fileName().equals(filename)){
				return true;
			}
		}
		return false;
	}
    
    
    public void directoryDelete(Directory directory){
        if(directory.getDirectoryFilesSet().isEmpty()){
         fileDelete(directory.get_path());
         return;
        }
        for (MyDriveFile f : directory.getDirectoryFilesSet()){
         if(f instanceof Directory){
          if(((Directory) f).getDirectoryFilesSet().isEmpty()){
              fileDelete(f.get_path());
          }
          else{
           directoryDelete(((Directory) f));
          }
         }
         else{
             fileDelete(f.get_path());
         }
        }
        
       }
    
    
    /*
     * Directory getDirectory(User u, String name,String path,String
     * permission){ //TO DO return new Directory(get_creationId(), name,
     * path+name, permission); }
     */

    /**
     * readFileContent(String path)
     * returns the content of the file of the given path
     * @param path
     * @return text
     * @throws FileNotFoundException
     */
    public String readFileContent(String path, User u) throws FileNotFoundException  {
        /*
         * String[] hierarchy =path.split("/"); String currentFileName;
         * Set<MyDriveFile> listOfFiles= getFileListSet(); String text = null;
         * int hierarchyIndex=0;
         * 
         * for(MyDriveFile f : listOfFiles) { currentFileName =
         * f.get_fileName();
         * 
         * if(hierarchy[hierarchyIndex].equals(currentFileName)){
         * if(hierarchyIndex == hierarchy.length - 1){ //WE'VE REACHED THE FILE
         * TO PRINT IT text = ((Text)f).get_Content(); System.out.println(
         * "The File content is:\n" + text); return text; }else{
         * 
         * if(f instanceof Directory){ hierarchyIndex += 1; Directory d =
         * ((Directory) f); listOfFiles = d.getDirectoryFilesSet(); }else{
         * //throw new FileNotFoundException(path); } } } } return null;
         */
        String text = null;
        for (MyDriveFile f : getFileListSet()) {
            String currentFileName = f.get_path();
            if (currentFileName.equals(path)) {
                if (f instanceof Text) {
                    text = ((Text) f).readFileContent(u);
                    return text;
                }else{
                	throw new InvalidFileFormatException(f.get_fileName());
                }
            }
        }
        throw new FileNotFoundException(path);
    }

    /*
     * String ListFile(MyDriveFile file){ //TO DO return null; }
     */
    
    
    
    /**
     * readFileContent(String path)
     * receives an XML Document and creates the respective objects/elements 
     * @param doc
     */
     public void XMLImport(Document doc) {
    	 XMLImporter _xmlImporter = new XMLImporter();
    	 _xmlImporter.Import(this, doc);
     }
     
     
     
     /**
     * XMLExport()
     * get an instance of the system  and exports it to the XML type
     * @return _xmlExporter
     * @throws IOException 
      */
     public Document XMLExport() throws IOException {
    	 XMLExporter _xmlExporter = new XMLExporter();
    	 return _xmlExporter.Export(this, new Document(new Element("mydrive")));
     }
     

     
     
     
     /**
      * getSessionByToken(long token)
      * receives a token and return the correspondent session associated to that token
      * @param token
      * @return s
      * @throws 
      */
     public Session getSessionByToken(long token) throws SessionDoesNotExistException{
    	 for(Session s : getSessionsSet()){


    		 if(s.get_token() == token){
    			 if(s.getCurrentUser() instanceof Root){
    				 if(s.get_lastAccess().plusMinutes(10).isBeforeNow()) {
    					 s.timedOut(token);
    				 }
    			 }
    			 else if(s.getCurrentUser() instanceof Nobody){
    				     				 
    			 }
    			 else if(s.get_lastAccess().plusHours(2).isBeforeNow()) {
    				 s.timedOut(token);
    			 }
    			 return s;
    		 }
    	 }
         throw new SessionDoesNotExistException(token);
     }
     
     /**
      * UserByToken(long token)
      * receives a token and return the correspondent owner of the session associated to that token
      * @param token
      * @return
      */
     public User UserByToken(long token){
    	Session s = getSessionByToken(token);
    	return s.getCurrentUser();
     }
     
     /**
      * boolean uniqueToken(long token)
      * receives a token and check if it is unique or not
      * @param token
      * @return boolean
      */
     public boolean uniqueToken(long token){
    	 for (Session s : getSessionsSet()) {
             if (s.get_token() == token) {
                 return false;
             }
         }
    	 return true;
     }
     
     /**
      * receives an username and his password and 
      * creates a new session assigning to it a new token
      * and adding it to the list sessions
      * @param username
      * @param password
      * @return token
      */
     public long createSession(String username, String password){
    	 User u = getUser(username);
    	 Session s = new Session(u);
    	 s.setCurrentDir(getDirectory(u.get_homeDir()));
		 long token = new BigInteger(64, new Random()).longValue();
    	 while(!uniqueToken(token)){
    		 token = new BigInteger(64, new Random()).longValue();
    	 }
    	 s.set_token(token);
    	 addSessions(s);
    	 return s.get_token();
     }
     
     
     /*
      * ReadAction = 0
      * WriteAction = 1
      * ExecuteAction = 2
      * DeleteAction = 3
      */
     /**
      * boolean grantPermission(String userPermission, String filePermission, int actionNumber [0=READ;1=WRITE;2=EXECUTE;3=DELETE])
      * receives an user and file permission and the respective action to do
      * and decides if the user as permissions to do the respective activity
      * @param userPermission
      * @param filePermission
      * @param actionNumber
      * @return boolean
      */
     public boolean grantPermission(String userPermission, String filePermission, int actionNumber){
    	// System.out.println("User");
    	 if(userPermission.equals("rwxdr-x-")){
    		 return true;
    	 }
    	     	 
    	 else if(userPermission.charAt(actionNumber)==(filePermission.charAt(actionNumber)))
    		 return true;
    	 else
    		 return false;
    	     	  
     }
     public String generateUmaskRoot(){
    	 return generateUmask(true, true, true, true, true );}
     public String generateUmaskNormalUser(){
    	 return generateUmask(true, true, true, true, false);
     }
     public String generateUmask(boolean read, boolean write, boolean execute, boolean delete, boolean root ){
    	 String permission = new String();
    	 
    	 if (read){
    		 permission += "r";
    	 }else{
    		 permission += "-";
    	 }
    	 
    	 if (write){
    		 permission += "w";
    	 }else{
    		 permission += "-";
    	 }
    	 
    	 if (execute){
    		 permission += "x";
    	 }else{
    		 permission += "-";
    	 }
    	 
    	 if (delete){
    		 permission += "d";
    	 }else{
    		 permission += "-";
    	 }
    	 
    	 if (root){
    		 permission += "r-x-";
    	 }else{
    		 permission += "----";
    	 }
    	 return permission; 
     }
     
     public String generateFilePermission(User user){
    	 String permission = user.get_uMask();
    	 return permission.substring(0,4);
     }
     /*
     public void giveReadPermission(User u){
    	 String userPermission = u.get_uMask();
    	 userPermission.charAt(0)='r';
    	 
    	 u.set_uMask(changedUserPermission);
    	 
    	 
     }
     */
     
     
     /**
      * void cleanup()
      * erase datasabe unecessary content to prepare it to be tested correctly
      */
     public void cleanup(){
    	 Directory d;
    	 User u;
    	 MyDrive md = getInstance();
    	 
    	 for(MyDriveFile file: getFileListSet()){
    		 if(file.get_fileId()>4)
    			 removeFileList(file);
//    		 if(file.get_fileId()==0 || file.get_fileId()==2 || file.get_fileId()==4){
//    			d = ((Directory) file);
//    			for(MyDriveFile dirFile: d.getDirectoryFilesSet())
//    				if(!(dirFile.get_fileId()==0 || dirFile.get_fileId()==2 || file.get_fileId()==4))
//    				d.removeDirectoryFiles(dirFile);
//    		 }		
    	 }
    	 
    	 for(User user: getUserListSet()){
    		    		 
//    		 for(MyDriveFile file: user.getFileSet()){
//    			 if(!(file.get_fileId()==0 || file.get_fileId()==2 || file.get_fileId()==4))
//     				removeFileList(file);
//    		 }
    	
    		 if(!((user instanceof Root)|| (user instanceof Nobody)))
    			 removeUserList(user);	 
    	 }
    	 md.set_creationId(5);
    	 
    	 //System.out.println(md.getFileListSet());
    	 }
     
     public EnvVar addEnvironmentalVariableToSession(Session s, String varName, String varValue){
    	 for(EnvVar v : s.getVariableListSet()){
    		 if(v.getName().equals(varName)){
    			 v.setValue(varValue);
    			 return v;
    		 }
    	 }
    	 EnvVar var=new EnvVar(varName,varValue);
    	 s.addVariableList(var);
    	 return var;
     }
     
     public EnvVar getEnvironmentalVariableFromSession(Session s, String name){
    	 for(EnvVar v: s.getVariableListSet()){
    		 if(v.getName().equals(name)){
    			 return v;
    		 }
    	 }
    	 throw new UnknownVariableException(s,name);
     }
     
     public String processFatherDir(String currentPath, long token){
    	 String[] dirs;
    	 String pathToReturn = "";
    	 int limit;
    	 if(currentPath.trim().equals("")){ //path starts with ..
    		 Session s = getSessionByToken(token);
    		 dirs = s.getCurrentDir().get_path().split("/");
    	 }
    	 else{ dirs = currentPath.split("/"); }
    	 for(int i = 0; i < dirs.length-1; i++){
    		 if(dirs[i].trim().equals("")) {continue;}
    		 pathToReturn += "/" + dirs[i];
    	 }
    	 return pathToReturn;
     }
     
     public String processPath(String path, long token){
    	 if(!path.startsWith("/home") && !path.startsWith(".")){ return processPath("./" + path, token); }
    	 String effectivePath = "";
    	 String[] dirs = path.split("/");
    	 Session s = getSessionByToken(token);
    	 if(dirs[0].equals(".")){
    		 effectivePath = s.getCurrentDir().get_path();
    	 }
    	 for(int i = 0; i < dirs.length; i++){
    		 String dir = dirs[i];
    		 if(dir.trim().equals("")){ continue; } //path starts with /
    		 if(dir.equals(".")){ continue; }
    		 if(dir.equals("..")){
    			 effectivePath = processFatherDir(effectivePath, token);
    		 }
    		 else{ effectivePath += "/"+ dir; }
    	 }
    	 return effectivePath;
     }
     
     
     public static Object run(String name, String[] args) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
         Class<?> cls;
         Method meth;
         try { // name is a class: call main()
           cls = Class.forName("pt.tecnico.mydrive."+name);
           meth = cls.getMethod("main", String[].class);
         } catch (ClassNotFoundException cnfe) { // name is a method
           int pos;
           if ((pos = name.lastIndexOf('.')) < 0) throw cnfe;
           cls = Class.forName("pt.tecnico.mydrive."+name.substring(0, pos));
           meth = cls.getMethod(name.substring(pos+1), String[].class);
         }
         return meth.invoke(null, (Object)args); // static method (ignore return)
       }
       
       
       public Object executePlain(Plain file, String[] args) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
	        String[] words = file.get_Content().split(" ");
	        App app = (App)getFileByPath(words[0]);
	        String[] arguments = Arrays.copyOfRange(words, 1, words.length);
	        if(args == null) { return executeApp(app, arguments); }
	        else{ return executeApp(app, args); }
       }
       
       
       public Object executeApp(App file, String[] args) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
        return run(file.get_Content(), args);
       }
       
       
       public Object executeFile(User user, String path, String[] args, long token, ArrayList<String> linkHistory) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
    	   MyDriveFile file = getFileByPath(path);
    	   if(!(file.get_filePermission().contains("x") || (user instanceof Root))){
    		   throw new ExecutePermissionDeniedException(file.get_fileName(), user.get_userName());
    	   }
    	   if(file instanceof App){
    		  return executeApp((App)file, args);
    	   }
    	   if(file instanceof Plain){
    		   return executePlain((Plain)file, args);
    	   }
    	   if(file instanceof Link){
    		   String effectivePath = processPath(((Link) file).get_Content(), token);
    		   if(linkHistory.contains(effectivePath)) { 
    			   throw new CircularLinkException((Link)file); 
    		   }
    		   linkHistory.add(effectivePath);
    		   return executeFile(user, processPath(((Link) file).get_Content(), token), args, token, linkHistory);
    	   }
    	   else{
    		   throw new InvalidFileFormatException("Directory");
    	   }
       }
     
//       public String envVarParser(Session session, String toParse){
//    	   String[] splitedString = toParse.split("/");
//    	   String s="";
//    	   EnvVar variable= null;
//    	   String varName=null;
//    	   int varLocation=0;
//    	   
//    	   //////////////////////////////////////////
//    	   /// Finding and extracting variable name
//    	   ///			within path
//    	   /////////////////////////////////////////
//    	   for(int i=0; i<splitedString.length; i ++){
//    		   if(splitedString[i].startsWith("$")){
//    			   varName=splitedString[i].split("$")[0];
//    			   varLocation=i;
//    			   break;
//    		   }
//    	   }
//    	   
//    	   if(varName!=null)
//    		   return toParse;
//    	   
//    	   //////////////////////////////////////////
//    	   /// Checking the existance of corresponding
//    	   /// 				EnvVar
//    	   //////////////////////////////////////////
//    	   for(EnvVar v : session.getVariableListSet()){
//    		   if(v.getName().equals(varName)){
//    			   variable=v;
//    			   break;
//    		   }
//    	   }
//    	   ///////////////////////////////////////////
//    	   ///	Replaces variable with content
//    	   //////////////////////////////////////////
//    	   splitedString[varLocation]=variable.getValue();
//
//   		
//   		if(splitedString.length>1){
//   		
//   			if(!splitedString[0].isEmpty())
//   				s=splitedString[0];
//   			
//   			for(int i = 1; i<splitedString.length;i++){
//   				
//   				s+="/"+splitedString[i];
//   			}
//   		}
//   		else s= splitedString[0];
//   		
//   		return s;
//       }
     
       public String parseEnvVar(Session s, String toParse){
    	   String[] splitedVar = toParse.split("/");
    	   String returnString="";
    	   
    	   if(splitedVar.length==1)
    		   return EnvVarReplace(s, splitedVar[0]);
    	   
    	   for(int i=0; i<splitedVar.length; i++){
    		   if(!splitedVar[i].equals(""))
    			   returnString+="/"+EnvVarReplace(s, splitedVar[i]);
    	   }
    	   return returnString;
       }
     
       private String EnvVarReplace(Session s, String var){
    	   String variableName;
    	   EnvVar variable=null;
    	   if(!var.startsWith("$"))
    		   return var;
    	   
    	   variableName=var.substring(1);
    	   
    	   for(EnvVar v : s.getVariableListSet()){
    		   if(v.getName().equals(variableName)){
    			   variable=v;
    			   break;
    		   }
    	   }
    	   if(variable!=null)
    		   return variable.getValue();
    	   else return var;
       }
}