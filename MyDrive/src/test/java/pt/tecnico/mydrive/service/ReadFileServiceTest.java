package pt.tecnico.mydrive.service;


import static org.junit.Assert.assertNull;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;


import org.jdom2.Document;
import org.jdom2.Element;
import org.joda.time.DateTime;
import org.junit.Test;

import pt.tecnico.mydrive.MyDriveApplication;
import pt.tecnico.mydrive.domain.App;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.MyDriveFile;
import pt.tecnico.mydrive.domain.Plain;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.Text;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.FileNotFoundException;
import pt.tecnico.mydrive.exception.InvalidContentFormatException;
import pt.tecnico.mydrive.exception.InvalidFileFormatException;
import pt.tecnico.mydrive.exception.ReadPermissionDeniedException;
import pt.tecnico.mydrive.exception.SessionDoesNotExistException;
import pt.tecnico.mydrive.exception.TimedOutSessionException;
import pt.tecnico.mydrive.exception.WritePermissionDeniedException;



	public class ReadFileServiceTest extends AbstractServiceTest {
		@Override
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
			m.createFile("GUITAR", "/home/UserSilvogo/GUITAR", "awesomeguitar", u, m.getDirectory("/home/UserSilvogo"), "Plain");
			m.createFile("GUITAR", "/home/Guest/GUITAR", "awesomeguitar", uguest, m.getDirectory("/home/Guest"), "Plain");
			m.createFile("DRUMS", "/home/UserSilvogo/DRUMS", "awesome/drums", u, m.getDirectory("/home/UserSilvogo"), "Link");
			m.createFile("BASS", "/home/UserSilvogo/BASS", "awesomebass.class", u, m.getDirectory("/home/UserSilvogo"), "App");
			m.createFile("PIANO", "/home/UserSilvogo/PIANO", "awesome.piano", u, m.getDirectory("/home/UserSilvogo"), "App");
			m.createFile("SAX", "/home/UserSilvogo/SAX", "/aweso/mesax", u, m.getDirectory("/home/UserSilvogo"), "Link");	
			m.createFile("VIOLIN", "/home/UserSilvogo/VIOLIN", "awesome guitar", u, m.getDirectory("/home/UserSilvogo"), "Plain");

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
			ReadFileService servicetwo = new ReadFileService(service.getToken(),"GUITAR");
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
			ReadFileService servicetwo = new ReadFileService(service.getToken(),"GUITAR");
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
			ReadFileService servicetwo = new ReadFileService(service.getToken(),"GUITAR");
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
			ReadFileService service = new ReadFileService(t,"GUITAR");
			service.dispatch();
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
			ReadFileService service = new ReadFileService(t,"BASS");
			service.dispatch();
			
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
			
			ReadFileService service = new ReadFileService(t,"DRUMS");
			service.dispatch();
			
			Directory d = s.getCurrentDir();
			Text file = (Text) d.getDirectoryFile("DRUMS");
			assertEquals("Invalid content", "awesome/drums", file.get_Content());
			
		}
		
		/*
		@Test(expected = ReadPermissionDeniedException.class)
		public void ReadWithNoReadPermission(){
			LoginService lservice = new LoginService("UserSilvogo","UserSilvogo");
			lservice.dispatch();
			long t = lservice.getToken();
			ReadFileService service = new ReadFileService(t, "VIOLIN", "/awesome sax");
			service.dispatch();
			
		}
		
		*/
		
		//tentar escrever num ficheiro nao existente
		@Test(expected = FileNotFoundException.class)
		public void ReadNonExistingFile(){
			LoginService lservice = new LoginService("UserSilvogo","UserSilvogo");
			lservice.dispatch();
			long t = lservice.getToken();
			ReadFileService service = new ReadFileService(t, "Vocals");
			service.dispatch();
		}
		
		/*
		@Test(expected = InvalidFileFormatException.class)
		public void ReadDirectory(){
			LoginService lservice = new LoginService("UserSilvogo","UserSilvogo");
			lservice.dispatch();
			long t = lservice.getToken();
			ReadFileService service = new ReadFileService(t, "DIR");
			service.dispatch();
		}
		*/
		
		
}


