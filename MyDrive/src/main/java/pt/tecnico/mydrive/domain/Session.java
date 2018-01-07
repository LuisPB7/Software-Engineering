package pt.tecnico.mydrive.domain;

import org.joda.time.DateTime;

import pt.tecnico.mydrive.exception.DomainAcessDeniedException;
import pt.tecnico.mydrive.exception.TimedOutSessionException;

public class Session extends Session_Base {

    public Session(User user) {
        super();
        DateTime access = new DateTime();
        set_lastAccess(access);
        setCurrentUser(user);
    }
    
    public void resetAccess(){
    	DateTime access = new DateTime();
    	set_lastAccess(access);
    }
    
    public void timedOut(long token){
    	throw new TimedOutSessionException(token);
    }
    
    public String listEnvVar(){
    	String s="";
    	for(EnvVar v : getVariableListSet()){
    		if(s.equals("")){
    			s=v.list();
    			continue;
    		}
    		s+="\n"+v.list();}
    	
    	return s;
    }
    
//    @Override
//    public void set_token(long t){
//    	throw new DomainAcessDeniedException();
//    	
//    }
}
