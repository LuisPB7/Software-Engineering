package pt.tecnico.mydrive.domain;

import org.jdom2.Document;

import pt.tecnico.mydrive.exception.WritePermissionDeniedException;

public class Plain extends Plain_Base {
	
	//private XMLExporter _xmlExporter = new XMLExporter();
	
	public Plain(){}
    
    public Plain(int id, String name, String path, String permission, String content) {
        initFile(id, name, path, permission);
        set_Content(content);
        
    }
    
    public String toString(){
    	return 	super.toString("Plain");
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
    	if((u instanceof Nobody))
    		throw new WritePermissionDeniedException(u.get_userName(), get_fileName());
    	
    	else if((u instanceof Root) ||  u.get_userName().equals(this.getCreator().get_userName()) || get_filePermission().contains("w")){
    		set_Content(s);
    		return;
    	}
    	
    	else
    		throw new WritePermissionDeniedException(u.get_userName(), get_fileName());
    }
}
