package pt.tecnico.mydrive.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.DirectoryNotFoundException;
import pt.tecnico.mydrive.exception.MyDriveException;
import pt.tecnico.mydrive.exception.UserDoesNotExistException;


public abstract class MyDriveService {
    protected static final Logger log = LogManager.getRootLogger();

    @Atomic
    public final void execute() throws MyDriveException {
        dispatch();
    }

    static MyDrive getMyDrive() {
        return MyDrive.getInstance();
    }

    static User getPerson(String userName) throws UserDoesNotExistException {
        User u = getMyDrive().getUser(userName);

        if (u == null)
            throw new UserDoesNotExistException(userName);

        return u;
    }

    static Directory getDirectory(String path) throws DirectoryNotFoundException{
    	Directory d = getMyDrive().getDirectory(path);
    	return d;
    }
    
    protected abstract void dispatch() throws MyDriveException;
}
