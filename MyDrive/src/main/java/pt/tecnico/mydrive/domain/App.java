package pt.tecnico.mydrive.domain;

import org.jdom2.Document;

import pt.tecnico.mydrive.exception.InvalidAppFormatException;
import pt.tecnico.mydrive.exception.WritePermissionDeniedException;

public class App extends App_Base {
	
	//private XMLExporter _xmlExporter = new XMLExporter();
	
	public App(){}
    
    public App(int id, String name, String path, String permission, String content) {
    	AppContentValidator(content); 
    	initFile(id, name, path, permission);
         set_Content(content);
         
    }

    public String toString(){
    	return 	super.toString("App");
    }

    
    @Override
    public Document XMLExport(Document doc){
    	XMLExporter _xmlExporter = new XMLExporter();
    	return _xmlExporter.Export(this, doc);
    }
    
    /*
	@Override
	public String listFile(String S) {
		// TODO Auto-generated method stub
		return null;
	}
 */  
    
    public void write(String s,User u){
    	AppContentValidator(s);
    	if((u instanceof Nobody))
    		throw new WritePermissionDeniedException(u.get_userName(), get_fileName());
    	
    	else if(get_filePermission().contains("w")  || (u instanceof Root) ||(u.get_userName().equals(getCreator().get_userName()))){
    		if(s.contains(".")){
    			set_Content(s);
    			return;
    		}

    		throw new InvalidAppFormatException(s);
    	}
    	else
    		throw new WritePermissionDeniedException(u.get_userName(), get_fileName());
    }
    
    public void AppContentValidator(String content){
    	if(!(content == null||content.contains(" "))){
    		String app[]=content.split("\\.");
    		if(app.length<=3){
    			for(int i=0; i<app.length;i++){
    				if(app[i].equals("")){
    					throw new InvalidAppFormatException(content);
    				}
    			}
    			return;
    		}
    			
    	}
    	throw new InvalidAppFormatException(content);
    }
}
