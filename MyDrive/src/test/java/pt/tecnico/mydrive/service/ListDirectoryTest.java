package pt.tecnico.mydrive.service;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;

import pt.tecnico.mydrive.MyDriveApplication;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.SessionDoesNotExistException;
import pt.tecnico.mydrive.exception.TimedOutSessionException;
import pt.tecnico.mydrive.exception.UserDoesNotExistException;



public class ListDirectoryTest extends AbstractServiceTest {
	public long token;
	public long unexistentToken;
	
	
	@Override
	protected void populate() {
		// TODO Auto-generated method stub
		MyDrive m = MyDrive.getInstance();
		m.cleanup();
		m.createUser("UserpFnX");
		User u = m.getUser("UserpFnX");
		Directory userDir = m.getDirectory(u.get_userName(), u.get_homeDir());
		m.createUser("luispb78");
		User u1 = m.getUser("luispb78");
		Directory userDir1 = m.getDirectory(u1.get_userName(), u1.get_homeDir());
		m.createUser("UserMiguens");
		User u2 = m.getUser("UserMiguens");
		Directory userDir2 = m.getDirectory(u2.get_userName(), u2.get_homeDir());
		m.createDirectory("GUNS", "/home/UserpFnX/GUNS", u, userDir);
		m.createFile("AK47", "/home/UserpFnX/GUNS/AK47", "arma terrorista", u, m.getDirectory("/home/UserpFnX/GUNS"), "Plain");
		m.createFile("README", "/home/UserpFnX/README", "nada a dizer", u, m.getDirectory("/home/UserpFnX"), "Plain");
		token = m.createSession("UserpFnX", "UserpFnX");
		unexistentToken = token + 1;
	}
	
	//----------------------------LOGIN TESTS---------------------------------//
	
	@Test(expected = TimedOutSessionException.class)
    public void invalidLoginAfterTimeLimitForRoot(){	

    	MyDrive m = MyDrive.getInstance();

		LoginService service = new LoginService("root", "***");
		service.dispatch();
		Session s = m.getSessionByToken(service.getToken());
		DateTime dt = new DateTime();
		DateTime added = dt.plusMinutes(-11);
		s.set_lastAccess(added);
		ListDirectoryService servicetwo = new ListDirectoryService(service.getToken());
		servicetwo.dispatch();
        
		
    }

    

    @Test
    public void LoginAfterTimeLimitForGuest(){	

    	MyDrive m = MyDrive.getInstance();

		LoginService service = new LoginService("Guest", null);
		service.dispatch();
		Session s = m.getSessionByToken(service.getToken());
		DateTime dt = new DateTime();
		DateTime added = dt.plusHours(-24);
		s.set_lastAccess(added);
		ListDirectoryService servicetwo = new ListDirectoryService(service.getToken());
		servicetwo.dispatch();

		
    }
    
    
    @Test(expected = TimedOutSessionException.class)
    public void invalidLoginAfterTimeLimit(){
		MyDrive m = MyDrive.getInstance();

		LoginService service = new LoginService("UserMiguens", "UserMiguens");
		service.dispatch();
		Session s = m.getSessionByToken(service.getToken());
		DateTime dt = new DateTime();
		DateTime added = dt.plusHours(-4);
		s.set_lastAccess(added);
		ListDirectoryService servicetwo = new ListDirectoryService(service.getToken());
		servicetwo.dispatch();

    }
		
		//------------------------------------------------------------------------//
	
	@Test(expected = SessionDoesNotExistException.class)
	public void fails(){
		ListDirectoryService service = new ListDirectoryService(unexistentToken);
		service.dispatch();
		service.result();

	}

	@Test
	public void success(){
		ListDirectoryService servicetest1 = new ListDirectoryService(token);
		servicetest1.dispatch();
		String resultDir1 = servicetest1.result();
		assertEquals("LISTING DIRECTORY /home/UserpFnX:\nGUNS/\nREADME", resultDir1);		

	}

}