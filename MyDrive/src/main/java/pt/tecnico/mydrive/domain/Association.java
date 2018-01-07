package pt.tecnico.mydrive.domain;

public class Association extends Association_Base {
    
    public Association() {
        super();
    }
    
    public Association(User u, MyDriveFile f, String extension){
    	setExtension(extension);
    	setUser(u);
    	setFile(f);
    }
    
    public void addAssociationToUser(User u){
    	u.addAssociation(this);
    }
}
