package pt.tecnico.mydrive.domain;

public class Root extends Root_Base {
    
    public Root() {
    	set_userName("root");
    	set_password("***");
    	set_name("root");
    	set_systemId(1);
    	set_uMask("rwxdr-x-");
    	set_homeDir("/home/root");
    }
    
}
