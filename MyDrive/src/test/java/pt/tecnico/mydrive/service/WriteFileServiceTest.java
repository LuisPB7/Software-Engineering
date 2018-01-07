package pt.tecnico.mydrive.service;


import static org.junit.Assert.assertNull;

import org.joda.time.DateTime;

import static org.junit.Assert.assertEquals;


import org.junit.Test;

import pt.tecnico.mydrive.domain.App;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.MyDriveFile;
import pt.tecnico.mydrive.domain.Plain;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.Text;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.FileNotFoundException;
import pt.tecnico.mydrive.exception.InvalidAppFormatException;
import pt.tecnico.mydrive.exception.InvalidFileFormatException;
import pt.tecnico.mydrive.exception.InvalidLinkFormatException;
import pt.tecnico.mydrive.exception.SessionDoesNotExistException;
import pt.tecnico.mydrive.exception.TimedOutSessionException;
import pt.tecnico.mydrive.exception.WritePermissionDeniedException;



public class WriteFileServiceTest extends AbstractServiceTest {
	
	
	protected void populate() {
		// TODO Auto-generated method stub
		MyDrive m = MyDrive.getInstance();
		m.cleanup();
		m.createUser("UserSilvogo");
		m.createUser("UserpFnX");

		User u = m.getUser("UserSilvogo");
		User uguest = m.getUser("Guest");
		Directory userDir = m.getDirectory(u.get_userName(), u.get_homeDir());


		m.createDirectory("DIR", "/home/UserSilvogo/DIR", u);
		m.createFile("GUITAR", "/home/Guest/GUITAR", "awesome guitar", uguest, m.getDirectory("/home/Guest"), "Plain");
		m.createFile("GUITAR", "/home/UserSilvogo/GUITAR", "awesome guitar", u, m.getDirectory("/home/UserSilvogo"), "Plain");
		m.createFile("DRUMS", "/home/UserSilvogo/DRUMS", "awesome/drums", u, m.getDirectory("/home/UserSilvogo"), "Link");
		m.createFile("BASS", "/home/UserSilvogo/BASS", "awesomebass.class", u, m.getDirectory("/home/UserSilvogo"), "App");
		m.createFile("PIANO", "/home/UserSilvogo/PIANO", "awesome.piano", u, m.getDirectory("/home/UserSilvogo"), "App");
		m.createFile("SAX", "/home/UserSilvogo/SAX", "/aweso/mesax", u, m.getDirectory("/home/UserSilvogo"), "Link");	
		m.createFile("VIOLIN", "/home/UserSilvogo/VIOLIN", "awesome violin", u, m.getDirectory("/home/UserSilvogo"), "Plain");

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
		WriteFileService servicetwo = new WriteFileService(service.getToken(),"GUITAR","awesomeguitar");
		servicetwo.dispatch();
        
		
    }

    

    @Test(expected = WritePermissionDeniedException.class)
    public void LoginAfterTimeLimitForGuest(){	

    	MyDrive m = MyDrive.getInstance();

		LoginService service = new LoginService("Guest", null);
		service.dispatch();
		Session s = m.getSessionByToken(service.getToken());
		DateTime dt = new DateTime();
		DateTime added = dt.plusHours(-24);
		s.set_lastAccess(added);
		WriteFileService servicetwo = new WriteFileService(service.getToken(),"GUITAR","awesomeguitar");
		servicetwo.dispatch();

		
    }
    
    
    @Test(expected = TimedOutSessionException.class)
    public void invalidLoginAfterTimeLimit(){
		MyDrive m = MyDrive.getInstance();

		LoginService service = new LoginService("UserSilvogo", "UserSilvogo");
		service.dispatch();
		Session s = m.getSessionByToken(service.getToken());
		DateTime dt = new DateTime();
		DateTime added = dt.plusHours(-4);
		s.set_lastAccess(added);
		WriteFileService servicetwo = new WriteFileService(service.getToken(),"GUITAR","awesomeguitar");
		servicetwo.dispatch();

    }
	
	//------------------------------------------------------------------------//

	@Test
	public void successPlain() throws Exception{
		MyDrive m = MyDrive.getInstance();
		LoginService lservice = new LoginService("UserSilvogo","UserSilvogo");
		lservice.dispatch();
		long t = lservice.getToken();
		Session s = m.getSessionByToken(t);	
		WriteFileService service = new WriteFileService(t,"GUITAR","awesomeguitar");
		service.dispatch();

		//check writed data
		Directory d = s.getCurrentDir();
		Plain file = (Plain) d.getDirectoryFile("GUITAR");
		assertEquals("Invalid content", "awesomeguitar",  file.get_Content());

	}

	@Test
	public void successApp() throws Exception{
		MyDrive m = MyDrive.getInstance();
		LoginService lservice = new LoginService("UserSilvogo","UserSilvogo");
		lservice.dispatch();
		long t = lservice.getToken();
		Session s = m.getSessionByToken(t);
		WriteFileService service = new WriteFileService(t,"BASS","awesomebass.class");
		service.dispatch();

		//check writed data
		Directory d = s.getCurrentDir();
		App file = (App) d.getDirectoryFile("BASS");
		assertEquals("Invalid content", "awesomebass.class",  file.get_Content());

	}

	@Test
	public void successLink() throws Exception{
		MyDrive m = MyDrive.getInstance();
		LoginService lservice = new LoginService("UserSilvogo","UserSilvogo");
		lservice.dispatch();
		long t = lservice.getToken();
		Session s = m.getSessionByToken(t);

		WriteFileService service = new WriteFileService(t,"DRUMS","/awesome/drums");
		service.dispatch();

		//check writed data
		Directory d = s.getCurrentDir();
		Text file = (Text) d.getDirectoryFile("DRUMS");
		assertEquals("Invalid content", "/awesome/drums",  file.get_Content());

	}

	
	@Test(expected = WritePermissionDeniedException.class)
	 public void WriteWithNoReadPermission(){
		MyDrive m = MyDrive.getInstance();
		LoginService lservice = new LoginService("UserSilvogo","UserSilvogo");
		lservice.dispatch();
		MyDrive.getInstance().createUser("miguens123");  
		MyDriveFile f1 = m.createFile("VIOLIN", "/home/miguens123/VIOLIN", "awesome violin", m.getUser("UserSilvogo"), m.getDirectory("/home/miguens123"), "Plain");
		f1.set_filePermission("----");
		LoginService lservice1 = new LoginService("miguens123","miguens123");
		lservice1.dispatch();
		long t = lservice1.getToken();
		WriteFileService service = new WriteFileService(t, "VIOLIN", "/awesome/violin");
		service.dispatch();
}



//	@Test(expected = WritePermissionDeniedException.class)
//			public void WriteWithNoPermission(){
//				LoginService lservice = new LoginService("UserSilvogo","UserSilvogo");
//				lservice.dispatch();
//				long t = lservice.getToken();
//				MyDrive m = MyDrive.getInstance();
//			
//				WriteFileService service = new WriteFileService(t, "PIANO", "piano.class");
//				service.dispatch();
//			}


	//tentar escrever num ficheiro nao existente
	@Test(expected = FileNotFoundException.class)
	public void WriteonNonExistingFile(){
		LoginService lservice = new LoginService("UserSilvogo","UserSilvogo");
		lservice.dispatch();
		long t = lservice.getToken();
		WriteFileService service = new WriteFileService(t, "Vocals", "awesome vocals");
		service.dispatch();
	}

	@Test(expected = InvalidAppFormatException.class)
	public void invalidAppContentFormat(){
		//meter path no app
		//MyDrive.getInstance().getUser("UserSilvogo").set_uMask(MyDrive.getInstance().generateUmask(true, false, true, false, false));
		LoginService lservice = new LoginService("UserSilvogo","UserSilvogo");
		lservice.dispatch();
		long t = lservice.getToken();
		WriteFileService service = new WriteFileService(t, "BASS", "/home/path");
		service.dispatch();
	}


	@Test(expected = InvalidLinkFormatException.class)
	public void invalidLinkContentFormat(){
		//meter path no app
		LoginService lservice = new LoginService("UserSilvogo","UserSilvogo");
		lservice.dispatch();
		long t = lservice.getToken();
		WriteFileService service = new WriteFileService(t, "DRUMS", "package.class.method");
		service.dispatch();
	}

	//Tentar escrever na diretoria
	@Test(expected = InvalidFileFormatException.class)
	public void invalidDirectoryFormat(){
		//meter path no app
		MyDrive md = MyDrive.getInstance();
		LoginService lservice = new LoginService("UserSilvogo","UserSilvogo");
		lservice.dispatch();
		long t = lservice.getToken();
		Session s = md.getSessionByToken(t);
		WriteFileService service = new WriteFileService(t, "DIR", "Hello45345");
		service.dispatch();
	}

//	@Test(expected = WritePermissionDeniedException.class)
//	public void WriteWithPermissionDeniedBut(){
//		MyDrive md = MyDrive.getInstance();
//		LoginService lservice = new LoginService("UserSilvogo", "UserSilvogo");
//		lservice.dispatch();
//		long t = lservice.getToken();
//		MyDriveFile guitar = md.getFileByName("GUITAR");
//		guitar.set_filePermission("----");
//		WriteFileService service = new WriteFileService(t, "GUITAR", "Can't write here.....");
//		service.dispatch();
//	}

}

