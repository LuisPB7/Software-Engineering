package pt.tecnico.mydrive.domain;

import org.jdom2.Document;
import pt.tecnico.mydrive.exception.InvalidLinkFormatException;
import pt.tecnico.mydrive.exception.WritePermissionDeniedException;

public class Link extends Link_Base {
	
	//private XMLExporter _xmlExporter = new XMLExporter();
	
	public Link(){}
    
	public Link(int id, String name, String path, String permission, String content) {
   	 	if(content == null){
   	 		throw new InvalidLinkFormatException(content);
   	 	}
		if(!content.contains("/")){
   	 		throw new InvalidLinkFormatException(content);
   	 	}
		initFile(id, name, path, permission);
		set_Content(content);
        
   }
	
	 public String toString(){
	    return 	super.toString("Link");
	 }

	
	 @Override
	 public Document XMLExport(Document doc){
		 XMLExporter _xmlExporter = new XMLExporter();
		 return _xmlExporter.Export(this, doc);
	 }

	/*
	 * @Override(non-Javadoc)
	 * @see pt.tecnico.mydrive.domain.Text#listFile(java.lang.String)
	 */
    /*
	public String listFile(String S) {
		// TODO Auto-generated method stub
		return null;
	}
    */
	 
	    public void write(String s,User u){
	    	if((u instanceof Nobody))
	    		throw new WritePermissionDeniedException(u.get_userName(), get_fileName());
	    	
	    	else if(get_filePermission().contains("w") || (u instanceof Root) ||  u.get_userName().equals(this.getCreator().get_userName())){
	    		if(s.contains("/")){
	    			set_Content(s);
	    			return;
	    		}
	    		throw new InvalidLinkFormatException(s);
	    	}
	    	
	    	else
	    		throw new WritePermissionDeniedException(u.get_userName(), get_fileName());
	    }
}
