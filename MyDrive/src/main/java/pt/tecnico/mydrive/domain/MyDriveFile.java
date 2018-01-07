package pt.tecnico.mydrive.domain;
import java.util.Set;

import org.jdom2.Document;
import org.joda.time.DateTime;

import pt.tecnico.mydrive.exception.DeletePermissionDeniedException;
import pt.tecnico.mydrive.exception.InvalidContentFormatException;
import pt.tecnico.mydrive.exception.InvalidFileFormatException;

public abstract class MyDriveFile extends MyDriveFile_Base {
    
	
	
	 /**  
	 * class MyDriveFile
	 * 
	 * MyDriveFile is the class that implements MyDrive system abstract concept of file.
	 * Has the following arguments needed for it's creation: 
	 * int id - unique numeric system identification
	 * String name - file name
	 * String path - path is the String describing the hierarchy of Directory objects plus 
	 * the file name of the current MyDriveFile. 
	 * string permission - permission is a String that contains a mask which describes a 
	 * mask containing the access to the file
	 * 
	 * @see pt.tecnico.mydrive.domain.Text#initFile(int id,String name,String path,String permission)
	 */
    public MyDriveFile(int id, String name, String path, String permission) {
       super();
       initFile(id, name, path, permission);
    }
    
    public abstract String readFileContent(User u);
    
    /**
     * MyDriveFile()
     */
    public MyDriveFile(){
    	super();
    }
    
    
    
    /**
     * toString(String typeOfFile)
     * 
     * @override
     */
    public String toString(String typeOfFile){
    	//User creator= getCreator();
    	return 	"Type: "+typeOfFile+"\n"
    			+"Creator: " + getCreator().get_userName() + "\n"
    			+"IDNum: "+get_fileId()+"\n"
    			+"Name: "+get_fileName()+"\n"
    			+"Last Modification: "+get_lastModification()+"\n"
    			+"Path: "+get_path()+"\n"
    			+"Permission: "+get_filePermission()
    			+"\nDirectory Path: "+ getDirectory().get_path();
    	
    	
    }
    
    
    
    /**
     * initFile(int id, String name, String path, String permission)
     * 
     * Receive id, name, path, permission as arguments and sets them as the attributes of the file.
     * When the File is creates it's also set a date as attribute.
     * @param id
     * id is the system identifier of the file.
     * @param name
     * name is the name of the file.
     * @param path
     * path is the String describing the hierarchy of Directory objects plus the file name of the current MyDriveFile. 
     * @param permission
     * permission is a String that contains a mask which describes a mask containing the access to the file
     */
    protected void initFile(int id,String name,String path,String permission){
    	set_fileId(id);
    	set_fileName(name);
    	set_lastModification(new DateTime());
    	set_path(path);
    	set_filePermission(permission);
    }    
    
    
    
    /**
     * deteteFromSystem()
     * 
     * This function will erase all the references to the file from the creator (User), applying method.
     * @see pt.tecnico.mydrive.domain.Text#eraseReferences(MyDriveFile f, User u)
     */
    public void deleteFromSystem(){
    	User user = getCreator();
    	eraseReferences(this,user);
    }
    
    
    
    /**
     * eraseReferences(MydriveFile f, User u)
     * 
     * Receive MyDriveFile and a user as arguments, it will remove the f file from the list of files
     * from the user that created it, it will also remove the f file from the directory and set the
     * directory as null.
     * @param f
     * f is the file to be removed.
     * @param u
     * u is the User that has the reference to the files. 
 f    */
    public void eraseReferences(MyDriveFile f, User u){
    	//if(u instanceof Nobody){throw new DeletePermissionDeniedException(u.get_name(), f.get_fileName());}
    	Set<MyDriveFile> fileList = u.getFileSet();
    	fileList.remove(f);
    	Directory dir = getDirectory();
    	
    	Set<MyDriveFile> dirList = dir.getDirectoryFilesSet();
    	dirList.remove(f);
    	setDirectory(null);
    }
    
     public boolean isLink(String content){
    	if(content.charAt(0)=='/')
    		return true;
    	else
    		return false;
    }
    
   public boolean isApp(String content){
	   String pattern = "^[a-zA-Z]+.class$";
	   String pattern2 = "^[a-zA-Z]+.java$";
	
	   if(content.matches(pattern) || content.matches(pattern2))
		   return true;
    	
   	   else
   		   return false;
   }
   
	public void writeFile(MyDriveFile f, User u, String content){
		
		//if(u instanceof Nobody){throw new WritePermissionDeniedException(u.get_name(), f.get_fileName());}
		
		if(f instanceof Link){
			
			
			if(isLink(content)){
				((Link) f).set_Content(content);
			}
			else
				throw new InvalidContentFormatException(content);
		}
		
		
		else if(f instanceof Plain){
		
			((Plain) f).set_Content(content);
		}
	
		
		else if(f instanceof App){
		
			if(isApp(content)){
				((App) f).set_Content(content);
			}
			else
				throw new InvalidContentFormatException(content);
		}
		
		
		else
			throw new InvalidFileFormatException(f.getClass().toString());
	}
    
   /**
    * String listFile(String s)
    * 
    * Receive a string as an argument.
    * 
    * @param s
    * s is string to be return.
    * @return
    * Returns the string containing the list of the file.
    */    
   public abstract String listFile(String s);
   
   
   
   /**
    * insertIntoDir(Directory dir)
    * 
    * Receive as argument a directory that contains a file, it will set the directory attribute as a dir
    * and will add the file it self to the directory list of files.
    * @param dir
    * Directory containing the file.
    */
   public void insertIntoDir(Directory dir){		
		setDirectory(dir);
		dir.addDirectoryFiles(this);
   }
   

   
   /**
    * linkToCretator(User u)
    * 
    * Receive as argument a user as creator of the file, it will set the creator attribute as the user 
    * and will add the file it self to the user list of files.
    *  
    * @param u
    * u is the creator of the file.
    */
   public void linkToCreator(User u){
	   setCreator(u);
	   u.addFile(this);
	   
   }
      
   public Document XMLExport(Document doc){ 
	   return null;
   }
   
   
   public abstract void write(String s,User u);
}
