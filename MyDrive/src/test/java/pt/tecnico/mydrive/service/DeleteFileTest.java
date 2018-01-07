package pt.tecnico.mydrive.service;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;

import pt.tecnico.mydrive.MyDriveApplication;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.FileNotFoundException;
import pt.tecnico.mydrive.exception.SessionDoesNotExistException;
import pt.tecnico.mydrive.exception.TimedOutSessionException;
import pt.tecnico.mydrive.exception.UserDoesNotExistException;
import pt.tecnico.mydrive.exception.DeletePermissionDeniedException;




public class DeleteFileTest extends AbstractServiceTest {
	
	public long token;
	MyDrive md;
	User u1;
	User uguest;
	Directory d;

	Directory dir;
	User u2;
	
	@Override
	protected void populate(){
		MyDrive md = MyDrive.getInstance();
		md.cleanup();
		
		u1= md.createUser("jmiguens");
		md.createUser("UserpFnX");
		uguest = md.getUser("Guest");
		dir = md.createDirectory("pasta", "/home/jmiguens/pasta", md.getUser("jmiguens"));
		md.createFile("appText", "/home/jmiguens/appText", "cenas.java" , u1, md.getDirectory("/home/jmiguens"), "App");
		md.createFile("appText", "/home/Guest/appText", "cenass.java" , uguest, md.getDirectory("/home/Guest"), "App");
		md.createFile("fileLink", "/home/jmiguens/fileLink", "/cenas/pasta" , u1, md.getDirectory("/home/jmiguens"), "Link");
		md.createFile("plainFile", "/home/jmiguens/plainFile", "cenas random da vovo" , u1, md.getDirectory("/home/jmiguens"), "Plain");
		d=(Directory) md.createFile("DirectoriaXPTO", "/home/jpmiguens/DirectoriaXPTO",null, u1,md.getDirectory("/home/jmiguens"), "Directory");
		
		
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
		DeleteFileService servicetwo = new DeleteFileService(service.getToken(), "appText");
		servicetwo.dispatch();
        
		
    }

    

    @Test(expected = DeletePermissionDeniedException.class)
    public void LoginAfterTimeLimitForGuest(){	

    	MyDrive m = MyDrive.getInstance();

		LoginService service = new LoginService("Guest", null);
		service.dispatch();
		Session s = m.getSessionByToken(service.getToken());
		DateTime dt = new DateTime();
		DateTime added = dt.plusHours(-24);
		s.set_lastAccess(added);
		DeleteFileService servicetwo = new DeleteFileService(service.getToken(), "appText");
		servicetwo.dispatch();

		
    }
    
    
    @Test(expected = TimedOutSessionException.class)
    public void invalidLoginAfterTimeLimit(){
		MyDrive m = MyDrive.getInstance();

		LoginService service = new LoginService("UserpFnX", "UserpFnX");
		service.dispatch();
		Session s = m.getSessionByToken(service.getToken());
		DateTime dt = new DateTime();
		DateTime added = dt.plusHours(-4);
		s.set_lastAccess(added);
		DeleteFileService servicetwo = new DeleteFileService(service.getToken(), "appText");
		servicetwo.dispatch();

		
    }
	
	//------------------------------------------------------------------------//
	
	@Test
	public void appFileDeleteSuccessful(){
		MyDrive md = MyDrive.getInstance();

		LoginService service = new LoginService("jmiguens", "jmiguens");

		md.getFileListSet().toString();
		service.dispatch();
		DeleteFileService servicetwo = new DeleteFileService(service.getToken(), "appText");
		servicetwo.dispatch();

	}
	
	@Test
	public void linkFileDeleteSuccessful(){
		MyDrive md = MyDrive.getInstance();
		LoginService service = new LoginService("jmiguens", "jmiguens");
		service.dispatch();
		DeleteFileService servicetwo = new DeleteFileService(service.getToken(), "fileLink");
		servicetwo.dispatch();
		

	}
	
	@Test
	public void plainFileDeleteSuccessful(){
		LoginService service = new LoginService("jmiguens", "jmiguens");
		service.dispatch();
		DeleteFileService servicetwo = new DeleteFileService(service.getToken(), "plainFile");
		servicetwo.dispatch();
	}
	
	@Test
	public void directoryDeleteSuccessful(){
		MyDrive md = MyDrive.getInstance();
		LoginService service = new LoginService("jmiguens", "jmiguens");
		service.dispatch();
		DeleteFileService servicetwo = new DeleteFileService(service.getToken(), "pasta");
		servicetwo.dispatch();
	}
	
	
	@Test(expected = FileNotFoundException.class)
	public void deleteUnexistingFile(){
		LoginService service = new LoginService("jmiguens", "jmiguens");
		service.dispatch();
		DeleteFileService servicetwo = new DeleteFileService(service.getToken(), "ficheiroFantasma");
		servicetwo.dispatch();
	}
	
	
	
	
}