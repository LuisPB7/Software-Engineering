package pt.tecnico.mydrive.domain;

import java.util.Set;

import org.jdom2.Document;

import pt.tecnico.mydrive.exception.FileNotFoundException;
import pt.tecnico.mydrive.exception.InvalidFileFormatException;
import pt.tecnico.mydrive.exception.ReadPermissionDeniedException;

public class Directory extends Directory_Base {
    /**
     * class Directory
     * 
     * class that implements the Directory type File on MyDrive Application
     * 
     */
	
	//private XMLExporter _xmlExporter = new XMLExporter();
    
    public Directory(){
        
    }
	
	
    
    public String readFileContent(User u)  {
    	if(get_filePermission().contains("r") || (u instanceof Root))
    		throw new InvalidFileFormatException(get_fileName());
    	else
    		throw new ReadPermissionDeniedException(get_fileName(), get_path());
        
    }
    
    
    public Directory(int id,String name,String path,String permission) {
        super();
        initFile(id, name, path, permission);
    }
    
  /*
    NAO DESCOMENTAR O COMENTARIO MAIS INTERNO
    protected void initDirectory(int id,String name,String path,String permission){
        set_fileId(id);
        set_fileName(name);
        set_lastModification(new DateTime());
        set_path(path);
        set_filePermission(permission);
    }
    */

    /**
     * toString()
     * returns a string with directory parameters
     */
    public String toString(){
        return  super.toString("Directory");
    }
    
    /**
     * listFile(String list)
     * receives a list of files and returns all file names contained on it
     * @param list
     * @return list
     * list is the file names contained on the given argument
     * 
     */
    public String listFile(String list){

    	/*if(!list.isEmpty()){
            list+="\n";
        }*/
    	Set<MyDriveFile> FileList= getDirectoryFilesSet();
    	for(MyDriveFile file: FileList){
    		if(file.equals(this)){	//When parent directory is the current 
    			continue;			//directory, aka, Home directory
    		}else{	
    			if(file instanceof Directory){
    				list += file.get_fileName()+"/";
    			}
    			else{
    				if(file instanceof Link){
    					list +="->" + ((Link) file).get_Content();
    				}
    				list += file.get_fileName();
    			}
    			list+="\n";
    		}
    	}
    	return list.substring(0, list.length() - 1);
    }

    /**
     * User getUser(String username)
     * receives a username and return the correspondent user
     * @param username
     * @return u
     * u is the username pretended to get
     */
    
    
    /**
     * eraseReferences(MyDriveFile f, User u)
     * receives a file and it's owner and delete all references to that file
     * @param f
     * @param u
     */
    public void eraseReferences(MyDriveFile f, User u) {
        Set<MyDriveFile> directoryFiles = getDirectoryFilesSet();
        if(!directoryFiles.isEmpty()){
        	eraseFilesFromDirectory(f,u);}
        	//throw new DirectoryRemovalException(f.get_path());}
            //Problema a lancar excecao
        /*
        BECAUSE WE'RE ONLY ABLE TO DELETE EMPTY DIRECTORIES, IF NEEDED USER RECURSIVE CALLS
        ON A DIFFERENT METHOD
        */
    /*  Directory father = getParent();
        Set<MyDriveFile>fatherlist = father.getDirectoryFilesSet();
        fatherlist.remove(f);*/
        setParent(null);
        super.eraseReferences(f,u);
    }
    
    public void eraseFilesFromDirectory(MyDriveFile f, User u){
    	/*REVIEW METHOD TO DELETE FILES OF A DIRECTORY RECURSIVELY*/
		
		Set<MyDriveFile> directoryFiles = getDirectoryFilesSet();
    	while(!directoryFiles.isEmpty()){
    		for(MyDriveFile file: directoryFiles){
    			if(file instanceof Directory){
    				eraseReferences(file,u);
    			}
    			file.eraseReferences(file, u);
    		}
    		
    	}
    }

    
    
    public MyDriveFile getDirectoryFile(String filename){
    	Set<MyDriveFile> FileList= getDirectoryFilesSet();
    	for(MyDriveFile f: FileList){
    		if(f.get_fileName().equals(filename)){
    			return f;
    		}
    		
    	}
		throw new FileNotFoundException(filename);
	}
    
    
    /**
     * setDirectories(Directory dir)
     * receis a directory(dir) and initializes the directory "father" and insert it on the list of MyDriveApp Directories
     * @param dir
     */
    public void setDirectories(Directory dir){
        insertIntoDir(dir);
        setCurrent(this);
        setParent(dir);
        
    }

    
    @Override
    public Document XMLExport(Document doc){
    	XMLExporter _xmlExporter = new XMLExporter();
    	return _xmlExporter.Export(this, doc);
    }
    
    public void write(String s, User u){
    	throw new InvalidFileFormatException(this.getClass().toString());
    }

}

