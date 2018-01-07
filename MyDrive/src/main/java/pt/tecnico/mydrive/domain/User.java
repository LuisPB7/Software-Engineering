package pt.tecnico.mydrive.domain;

import org.jdom2.Document;

import pt.tecnico.mydrive.exception.InvalidPasswordException;

public class User extends User_Base {
	
	//private XMLExporter _xmlExporter = new XMLExporter();
	
	public User(){
		super();
	}
    
	public User(String username,int creationId) {
        this(username,creationId,"rwxd----");
	}
	
	public User(String username,int creationId, String mask) {
       this(username,username,username,creationId,mask);
    }
    public User(String username,String password,String name, int creationId, String umask){
    	super.set_userName(username);
    	super.set_password(password);
    	super.set_name(name);
    	super.set_systemId(creationId);
    	super.set_uMask(umask);
    	super.set_homeDir("/home/"+username);
    }
    
    public String toString(){
        return "USER:\n"
          +"System Id: "+get_systemId()+"\n"
          +"username: " + get_userName() +"\n"
          +"password: "+ get_password()+"\n"
          +"name: "+get_name()+"\n"
          +"mask: " + get_uMask()+"\n"
          +"homedir: " + get_homeDir();
   }
    
    public static Root getInstanceRootUser(MyDrive md){
    	if(!md.userExists("root")){
    		return new Root(); //creates user root 
    	}
    	return (Root)md.getUser("root");
    	//throws exception
    	//return null;
    }
    
    
    public static Nobody getInstanceNobodyUser(MyDrive md){
    	if(!md.userExists("Guest")){
    		return new Nobody(); //creates user root 
    	}
    	return (Nobody)md.getUser("Guest");
    	//throws exception
    	//return null;
    }
    
    
    public Document XMLExport(Document doc){
    	XMLExporter _xmlExporter = new XMLExporter();
    	return _xmlExporter.Export(this, doc);
    }
    
    @Override
    public void set_password(String pw){
    	if(pw.length()<8 && (!(get_userName().equals("root"))) && (!(get_userName().equals("Guest"))))
    		throw new InvalidPasswordException();
    	else
    		super.set_password(pw);
    	
    	
    }

}
