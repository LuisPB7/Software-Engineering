package pt.tecnico.mydrive.service;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;

import pt.tecnico.mydrive.domain.App;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.Link;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.MyDriveFile;
import pt.tecnico.mydrive.domain.Plain;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.DuplicatedDirectoryException;
import pt.tecnico.mydrive.exception.InvalidContentFormatException;
import pt.tecnico.mydrive.exception.InvalidLinkFormatException;
import pt.tecnico.mydrive.exception.SessionDoesNotExistException;
import pt.tecnico.mydrive.exception.TimedOutSessionException;
import pt.tecnico.mydrive.exception.WritePermissionDeniedException;


public class CreateFileTest extends AbstractServiceTest {
	private long token;
	private String fileName="plainText";
	private String filePath;
	private String defaultPermission = "";
	private String contentTest="TEXT FILE TEST";
	private String contentTestApp="package.class.method";
	private User u;
	Directory userDir;
	
	
	protected void populate() {
		// TODO Auto-generated method stub
		MyDrive m = MyDrive.getInstance();
		m.cleanup();		
		m.createUser("sofiaTOSTAS");
		u = m.getUser("sofiaTOSTAS");
		m.createUser("UserpFnX");
		userDir = m.getDirectory(u.get_userName(), u.get_homeDir());
		filePath=u.get_homeDir()+"/"+fileName;
		token=m.createSession(u.get_userName(), u.get_password());
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
		CreateFileService servicetwo = new CreateFileService(service.getToken(), fileName,"Plain", null);
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
		CreateFileService servicetwo = new CreateFileService(service.getToken(), fileName,"Plain", null);
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
		CreateFileService servicetwo = new CreateFileService(service.getToken(), fileName,"Plain", null);
		servicetwo.dispatch();

		
    }
		
		//------------------------------------------------------------------------//
	
	
	//TESTING PLAIN FILE CREATION::
	@Test
	public void createEmptyPlainFileSuccessfully(){
		CreateFileService service = new CreateFileService(token,fileName,"Plain",null);
		service.dispatch();
		MyDrive md=MyDrive.getInstance();
		Plain testResult=(Plain)md.getFileByName(fileName);
		assertNotNull(testResult);
		
		String fileNameToTest=testResult.get_fileName();
		String filePathToTest=testResult.get_path();
		String fileContentToTest=testResult.get_Content();
		
		assertTrue(fileNameToTest.equals(fileName));
		assertTrue(filePathToTest.equals(filePath));
		
		assertNull(fileContentToTest);
		
	}

	@Test
	public void createPlainFileSuccessfullyWithContent(){
		CreateFileService service = new CreateFileService(token,fileName,"Plain",contentTest);
		service.dispatch();
		MyDrive md=MyDrive.getInstance();
		Plain testResult=(Plain)md.getFileByName(fileName);
		assertNotNull(testResult);
		
		String fileNameToTest=testResult.get_fileName();
		String filePathToTest=testResult.get_path();
		String fileContentToTest=testResult.get_Content();
		
		assertTrue(fileNameToTest.equals(fileName));
		assertTrue(filePathToTest.equals(filePath));
		
		assertTrue(fileContentToTest.equals(contentTest));
	}

	//TESTING APP FILE CREATION::
	@Test(expected = InvalidContentFormatException.class)
	public void createEmptyAppFileInsuccessfully(){
		CreateFileService service = new CreateFileService(token,fileName,"App",null);
		service.dispatch();
//		MyDrive md=MyDrive.getInstance();
//		App testResult=(App)md.getFileByName(fileName);
//		assertNotNull(testResult);
//		
//		String fileNameToTest=testResult.get_fileName();
//		String filePathToTest=testResult.get_path();
//		String fileContentToTest=testResult.get_Content();
//		
//		assertTrue(fileNameToTest.equals(fileName));
//		assertTrue(filePathToTest.equals(filePath));
//		
//		assertNull(fileContentToTest);
//		
	}
	@Test(expected = InvalidContentFormatException.class)
	public void createAppFileInsuccessfullyDueToEmptyPositions(){
		CreateFileService service = new CreateFileService(token,fileName,"App","package..method");
		service.dispatch();
//		MyDrive md=MyDrive.getInstance();
//		App testResult=(App)md.getFileByName(fileName);
//		assertNotNull(testResult);
//		
//		String fileNameToTest=testResult.get_fileName();
//		String filePathToTest=testResult.get_path();
//		String fileContentToTest=testResult.get_Content();
//		
//		assertTrue(fileNameToTest.equals(fileName));
//		assertTrue(filePathToTest.equals(filePath));
//		
//		assertNull(fileContentToTest);
//		
	}
	@Test(expected = InvalidContentFormatException.class)
	public void createAppFileInsuccessfullyDueToBadContent(){
		CreateFileService service = new CreateFileService(token,fileName,"App","package.class.method.variable");
		service.dispatch();
//		MyDrive md=MyDrive.getInstance();
//		App testResult=(App)md.getFileByName(fileName);
//		assertNotNull(testResult);
//		
//		String fileNameToTest=testResult.get_fileName();
//		String filePathToTest=testResult.get_path();
//		String fileContentToTest=testResult.get_Content();
//		
//		assertTrue(fileNameToTest.equals(fileName));
//		assertTrue(filePathToTest.equals(filePath));
//		
//		assertNull(fileContentToTest);
//		
	}
	@Test
	public void createAppFileSuccessfullyWithContent(){
		CreateFileService service = new CreateFileService(token,fileName,"App",contentTestApp);
		service.dispatch();
		MyDrive md=MyDrive.getInstance();
		App testResult=(App)md.getFileByName(fileName);
		assertNotNull(testResult);
		
		String fileNameToTest=testResult.get_fileName();
		String filePathToTest=testResult.get_path();
		String fileContentToTest=testResult.get_Content();
		System.out.println(fileContentToTest);
		
		assertTrue(fileNameToTest.equals(fileName));
		assertTrue(filePathToTest.equals(filePath));
		
		assertEquals(contentTestApp,fileContentToTest);
	}
	
	//TESTING LINK FILE CREATION::
	@Test(expected = InvalidLinkFormatException.class)
	public void createEmptyLinkFileUnSuccessfully(){
		CreateFileService service = new CreateFileService(token,fileName,"Link",null);
		service.dispatch();
		MyDrive md=MyDrive.getInstance();
		Link testResult=(Link)md.getFileByName(fileName);
		/*
		String fileNameToTest=testResult.get_fileName();
		String filePathToTest=testResult.get_path();
		String fileContentToTest=testResult.get_Content();
		
		assertTrue(fileNameToTest.equals(fileName));
		assertTrue(filePathToTest.equals(filePath));
		
		assertNull(fileContentToTest);*/
	}
	
	@Test(expected = InvalidContentFormatException.class)
	public void createLinkFileUnSuccessfullyWithContent(){
		CreateFileService service = new CreateFileService(token,fileName,"Link",contentTest);
		service.dispatch();
		MyDrive md=MyDrive.getInstance();
		Link testResult=(Link)md.getFileByName(fileName);
		
		/*
		String fileNameToTest=testResult.get_fileName();
		String filePathToTest=testResult.get_path();
		String fileContentToTest=testResult.get_Content();
		
		assertTrue(fileNameToTest.equals(fileName));
		assertTrue(filePathToTest.equals(filePath));
		
		assertTrue(fileContentToTest.equals(contentTest));*/
	}
	@Test
	public void createLinkFileSuccessfullyWithContent(){
		contentTest="/home/root";
		CreateFileService service = new CreateFileService(token,fileName,"Link",contentTest);
		service.dispatch();
		MyDrive md=MyDrive.getInstance();
		Link testResult=(Link)md.getFileByName(fileName);
		
		String fileNameToTest=testResult.get_fileName();
		String filePathToTest=testResult.get_path();
		String fileContentToTest=testResult.get_Content();
		
		assertTrue(fileNameToTest.equals(fileName));
		assertTrue(filePathToTest.equals(filePath));
		
		assertTrue(fileContentToTest.equals(contentTest));
		
	}
	//TESTING DIRECTORY FILE CREATION::
	
	@Test
	public void createDirectorySuccessfully(){
		fileName="ExampleDir";
		CreateFileService service = new CreateFileService(token,fileName,"Directory",null);
		service.dispatch();
		MyDrive md=MyDrive.getInstance();
		Session currentS = md.getSessionByToken(token);
		
		String currentDirPath = currentS.getCurrentDir().get_path()+"/"+fileName;
		Directory d = md.getDirectory(currentDirPath);
		
		assertNotNull(d);
		
		assertTrue(d.get_fileName().equals(fileName));
		String nameOnDirectory=d.getCreator().get_name();
		String nameOfSessionUser=currentS.getCurrentUser().get_name();
		
		assertEquals(nameOfSessionUser,nameOnDirectory);
	}
	
	@Test(expected = DuplicatedDirectoryException.class)
	public void failCreatingDuplicatedDirectory(){
		fileName="ExampleDir";
		CreateFileService service = new CreateFileService(token,fileName,"Directory",null);
		service.dispatch();
		service=new CreateFileService(token,fileName,"Directory",null);
		service.dispatch();

	}

	@Test(expected = WritePermissionDeniedException.class)
	public void FailToCreateFileOnDirectory(){
		User u=MyDrive.getInstance().createUser("Dummy");
		Directory d =MyDrive.getInstance().getDirectory(u.get_homeDir());
		d.set_filePermission("--x-");
		long token = MyDrive.getInstance().createSession("Dummy", "Dummy");
		CreateFileService service =new CreateFileService(token,"This","plain",null);
		service.dispatch();
	}
}
