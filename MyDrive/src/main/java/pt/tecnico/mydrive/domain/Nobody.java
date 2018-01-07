package pt.tecnico.mydrive.domain;

import pt.tecnico.mydrive.exception.InvalidPasswordException;

public class Nobody extends Nobody_Base {
    
    public Nobody() {
        set_userName("Guest");
    	//set_password(null);
    	set_name("Guest");
    	set_uMask("rwxdr-x-");
    	set_homeDir("/home/Guest");
    	set_systemId(3);

    }
    
    public void set_password(String pass) { throw new InvalidPasswordException(); }
}
