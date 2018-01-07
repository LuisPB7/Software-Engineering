package pt.tecnico.mydrive.service;

import static org.junit.Assert.assertEquals;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.FileNotFoundException;
import pt.tecnico.mydrive.exception.SessionDoesNotExistException;
import pt.tecnico.mydrive.exception.TimedOutSessionException;
import pt.tecnico.mydrive.exception.CircularLinkException;


public class ExecuteFileTest extends AbstractServiceTest {

	@Override
	protected void populate(){
		MyDrive m = MyDrive.getInstance();
		m.cleanup();
		
		m.createUser("UserSilvogo");
		m.createUser("UserpFnX");
		User u = m.getUser("UserSilvogo");
		Directory userDir = m.getDirectory(u.get_userName(), u.get_homeDir());
		
		m.createDirectory("DIR", "/home/UserSilvogo/DIR", u);
		m.createFile("MIC", "/home/UserSilvogo/MIC", "/home/UserSilvogo/SAX", u, m.getDirectory("/home/UserSilvogo"), "Link");
		m.createFile("GUITAR", "/home/UserSilvogo/GUITAR", "/home/UserSilvogo/SAX", u, m.getDirectory("/home/UserSilvogo"), "Link");
		m.createFile("DRUMS", "/home/UserSilvogo/DRUMS", "/home/UserSilvogo/BASS", u, m.getDirectory("/home/UserSilvogo"), "Link");
		m.createFile("BASS", "/home/UserSilvogo/BASS", "presentation.Hello.greet", u, m.getDirectory("/home/UserSilvogo"), "App");
		m.createFile("PIANO", "/home/UserSilvogo/PIANO", "presentation.Hello", u, m.getDirectory("/home/UserSilvogo"), "App");
		m.createFile("FAILS", "/home/UserSilvogo/FAILS", "presentation.Cenas.greet", u, m.getDirectory("/home/UserSilvogo"), "App");
		m.createFile("SAX", "/home/UserSilvogo/SAX", "/home/UserSilvogo/BASS paulo ritto", u, m.getDirectory("/home/UserSilvogo"), "Plain");
		m.createFile("LOL", "/home/UserSilvogo/LOL", "/home/UserSilvogo/./SAX", u, m.getDirectory("/home/UserSilvogo"), "Link");
		m.createFile("NOOB", "/home/UserSilvogo/NOOB", "/home/UserSilvogo/../UserSilvogo/SAX", u, m.getDirectory("/home/UserSilvogo"), "Link");	
		m.createFile("CLARINET", "/home/UserSilvogo/CLARINET", "/home/UserSilvogo/.././UserSilvogo/BASS", u, m.getDirectory("/home/UserSilvogo"), "Link");
		m.createFile("PIFARO", "/home/UserSilvogo/PIFARO", "./.././UserSilvogo/BASS", u, m.getDirectory("/home/UserSilvogo"), "Link");	
		m.createFile("SHREK", "/home/UserSilvogo/SHREK", "../UserSilvogo/SAX", u, m.getDirectory("/home/UserSilvogo"), "Link");
		m.createFile("TRUMP", "/home/UserSilvogo/TRUMP", "./.././UserSilvogo/SAX", u, m.getDirectory("/home/UserSilvogo"), "Link");
		m.createFile("RIPSD", "/home/UserSilvogo/RIPSD", "./.././UserSilvogo/RIPCOMPILADORES", u, m.getDirectory("/home/UserSilvogo"), "Link");
		m.createFile("RIPCOMPILADORES", "/home/UserSilvogo/RIPCOMPILADORES", "./.././UserSilvogo/RIPLEIC", u, m.getDirectory("/home/UserSilvogo"), "Link");
		m.createFile("RIPLEIC", "/home/UserSilvogo/RIPLEIC", "./.././UserSilvogo/RIPSD", u, m.getDirectory("/home/UserSilvogo"), "Link");




	}

	
	//----------------------------LOGIN TESTS---------------------------------//
	
	
	
		@Test(expected = TimedOutSessionException.class)
	    public void invalidLoginAfterTimeLimitForRoot(){	

	    	MyDrive m = MyDrive.getInstance();
			String[] args = {"Joao","Miguens"};


			LoginService service = new LoginService("root", "***");
			service.dispatch();
			Session s = m.getSessionByToken(service.getToken());
			DateTime dt = new DateTime();
			DateTime added = dt.plusMinutes(-11);
			s.set_lastAccess(added);
			ExecuteFileService servicetwo = new ExecuteFileService(service.getToken(), "/home/UserSilvogo/BASS", args);
			servicetwo.dispatch();
	        
			
	    }

	    

	    @Test
	    public void LoginAfterTimeLimitForGuest(){	

	    	MyDrive m = MyDrive.getInstance();
			String[] args = {"Joao","Miguens"};


			LoginService service = new LoginService("Guest", null);
			service.dispatch();
			Session s = m.getSessionByToken(service.getToken());
			DateTime dt = new DateTime();
			DateTime added = dt.plusHours(-24);
			s.set_lastAccess(added);
			ExecuteFileService servicetwo = new ExecuteFileService(service.getToken(), "/home/UserSilvogo/BASS", args);
			servicetwo.dispatch();

			
	    }
	    
	    
	    @Test(expected = TimedOutSessionException.class)
	    public void invalidLoginAfterTimeLimit(){
			MyDrive m = MyDrive.getInstance();
			String[] args = {"Joao","Miguens"};

			LoginService service = new LoginService("UserSilvogo", "UserSilvogo");
			service.dispatch();
			Session s = m.getSessionByToken(service.getToken());
			DateTime dt = new DateTime();
			DateTime added = dt.plusHours(-4);
			s.set_lastAccess(added);
			ExecuteFileService servicetwo = new ExecuteFileService(service.getToken(), "/home/UserSilvogo/BASS", args);
			servicetwo.dispatch();

	    }
	    
		//------------------------------------------------------------------------//
	    
	
	
	@Test
	public void doAppFileSucessful() throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		MyDrive md = MyDrive.getInstance();
		String[] args = {"Joao","Miguens"};
		LoginService service = new LoginService("UserSilvogo", "UserSilvogo");
		service.dispatch();
		ExecuteFileService servicetwo = new ExecuteFileService(service.getToken(), "/home/UserSilvogo/BASS", args);
		servicetwo.dispatch();
		assertEquals("ExecutaAppFile", "Hello Joao", servicetwo.result());
	}
	
	@Test
	public void doAppFileSucessfulNoMethod() throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Class<?> cls;
		Method meth;
		cls = Class.forName("pt.tecnico.mydrive.presentation.Hello");
        meth = cls.getMethod("main", String[].class);
		MyDrive md = MyDrive.getInstance();
		String[] args = {"Joao","Miguens"};
		LoginService service = new LoginService("UserSilvogo", "UserSilvogo");
		service.dispatch();
		ExecuteFileService servicetwo = new ExecuteFileService(service.getToken(), "/home/UserSilvogo/PIANO", args);
		servicetwo.dispatch();
		assertEquals("ExecutaAppFile", meth.invoke(null, (Object)args), servicetwo.result());
	}

	@Test
	public void doPlainFileSucessfulWithArgs(){
		MyDrive md = MyDrive.getInstance();
		String[] args = {"Antonio","Taveira"};
		LoginService service = new LoginService("UserSilvogo", "UserSilvogo");
		service.dispatch();
		ExecuteFileService servicetwo = new ExecuteFileService(service.getToken(), "/home/UserSilvogo/SAX", args);
		servicetwo.dispatch();
		assertEquals("ExecutaPlainFile", "Hello Antonio", servicetwo.result());
	}
	
	@Test
	public void doPlainFileSucessfulWithoutArgs(){
		MyDrive md = MyDrive.getInstance();
		String[] args = null;
		LoginService service = new LoginService("UserSilvogo", "UserSilvogo");
		service.dispatch();
		ExecuteFileService servicetwo = new ExecuteFileService(service.getToken(), "/home/UserSilvogo/SAX", args);
		servicetwo.dispatch();
		assertEquals("ExecutaPlainFile", "Hello paulo", servicetwo.result());
	}
	
	@Test
	public void doLinkFileToAppSucessful(){
		MyDrive md = MyDrive.getInstance();
		String[] args = {"Diogo","Silva"};
		LoginService service = new LoginService("UserSilvogo", "UserSilvogo");
		service.dispatch();
		ExecuteFileService servicetwo = new ExecuteFileService(service.getToken(), "/home/UserSilvogo/DRUMS", args);
		servicetwo.dispatch();
		assertEquals("ExecutaLinkFile", "Hello Diogo", servicetwo.result());
	}
	
	@Test
	public void doLinkFileToPlainSucessfulWithArgs(){
		MyDrive md = MyDrive.getInstance();
		String[] args = {"Luis","Borges"};
		LoginService service = new LoginService("UserSilvogo", "UserSilvogo");
		service.dispatch();
		ExecuteFileService servicetwo = new ExecuteFileService(service.getToken(), "/home/UserSilvogo/GUITAR", args);
		servicetwo.dispatch();
		assertEquals("ExecutaLinkFile", "Hello Luis", servicetwo.result());
	}
	
	@Test
	public void doLinkFileToPlainSucessfulWithoutArgs(){
		MyDrive md = MyDrive.getInstance();
		String[] args = null;
		LoginService service = new LoginService("UserSilvogo", "UserSilvogo");
		service.dispatch();
		ExecuteFileService servicetwo = new ExecuteFileService(service.getToken(), "/home/UserSilvogo/GUITAR", args);
		servicetwo.dispatch();
		assertEquals("ExecutaLinkFile", "Hello paulo", servicetwo.result());
	}
	
	@Test
	public void doLinkFileToLinkSucessfulWithArgs(){
		MyDrive md = MyDrive.getInstance();
		String[] args = {"Pedro"};
		LoginService service = new LoginService("UserSilvogo", "UserSilvogo");
		service.dispatch();
		ExecuteFileService servicetwo = new ExecuteFileService(service.getToken(), "/home/UserSilvogo/MIC", args);
		servicetwo.dispatch();
		assertEquals("ExecutaLinkFile", "Hello Pedro", servicetwo.result());
	}
	
	@Test
	public void doLinkFileToLinkSucessfulWithoutArgs(){
		MyDrive md = MyDrive.getInstance();
		String[] args = null;
		LoginService service = new LoginService("UserSilvogo", "UserSilvogo");
		service.dispatch();
		ExecuteFileService servicetwo = new ExecuteFileService(service.getToken(), "/home/UserSilvogo/MIC", args);
		servicetwo.dispatch();
		assertEquals("ExecutaLinkFile", "Hello paulo", servicetwo.result());
	}
	
	@Test
	public void doLinkFileToPlainSucessfulWithFatherDirectory(){
		MyDrive md = MyDrive.getInstance();
		String[] args = null;
		LoginService service = new LoginService("UserSilvogo", "UserSilvogo");
		service.dispatch();
		ExecuteFileService servicetwo = new ExecuteFileService(service.getToken(), "/home/UserSilvogo/NOOB", args);
		servicetwo.dispatch();
		assertEquals("ExecutaLinkFile", "Hello paulo", servicetwo.result());
	}
	
	@Test
	public void doLinkFileToPlainSucessfulWithDotDirectory(){
		MyDrive md = MyDrive.getInstance();
		String[] args = null;
		LoginService service = new LoginService("UserSilvogo", "UserSilvogo");
		service.dispatch();
		ExecuteFileService servicetwo = new ExecuteFileService(service.getToken(), "/home/UserSilvogo/LOL", args);
		servicetwo.dispatch();
		assertEquals("ExecutaLinkFile", "Hello paulo", servicetwo.result());
	}
	
	@Test
	public void doLinkFileToAppSuccessfulWithDots(){
		MyDrive md = MyDrive.getInstance();
		String[] args = null;
		LoginService service = new LoginService("UserSilvogo", "UserSilvogo");
		service.dispatch();
		ExecuteFileService servicetwo = new ExecuteFileService(service.getToken(), "/home/UserSilvogo/CLARINET", args);
		servicetwo.dispatch();
		assertEquals("ExecutaLinkFile", "Hello", servicetwo.result());
	}
	
	@Test
	public void doLinkFileToAppSuccessfulWithParentAndDot(){
		MyDrive md = MyDrive.getInstance();
		String[] args = null;
		LoginService service = new LoginService("UserSilvogo", "UserSilvogo");
		service.dispatch();
		ExecuteFileService servicetwo = new ExecuteFileService(service.getToken(), "/home/UserSilvogo/CLARINET", args);
		servicetwo.dispatch();
		assertEquals("ExecutaLinkFile", "Hello", servicetwo.result());
	}
	
	@Test
	public void doLinkFileSuccessfulStartingWithParent(){
		MyDrive md = MyDrive.getInstance();
		String[] args = null;
		LoginService service = new LoginService("UserSilvogo", "UserSilvogo");
		service.dispatch();
		ExecuteFileService servicetwo = new ExecuteFileService(service.getToken(), "/home/UserSilvogo/SHREK", args);
		servicetwo.dispatch();
		assertEquals("ExecutaLinkFile", "Hello paulo", servicetwo.result());
	}
	
	@Test
	public void doLinkFileSuccessfulStartingWithDot(){
		MyDrive md = MyDrive.getInstance();
		String[] args = null;
		LoginService service = new LoginService("UserSilvogo", "UserSilvogo");
		service.dispatch();
		ExecuteFileService servicetwo = new ExecuteFileService(service.getToken(), "/home/UserSilvogo/PIFARO", args);
		servicetwo.dispatch();
		assertEquals("ExecutaLinkFile", "Hello", servicetwo.result());
	}
	
	@Test
	public void doLinkFileSuccessfulWithDotsAndArgument(){
		MyDrive md = MyDrive.getInstance();
		String[] args = {"Luis", "Borges"};
		LoginService service = new LoginService("UserSilvogo", "UserSilvogo");
		service.dispatch();
		ExecuteFileService servicetwo = new ExecuteFileService(service.getToken(), "/home/UserSilvogo/PIFARO", args);
		servicetwo.dispatch();
		assertEquals("ExecutaLinkFile", "Hello Luis", servicetwo.result());
	}
	
	@Test
	public void doLinkToPlainFileSuccessfulWithDotsAndArgument(){
		MyDrive md = MyDrive.getInstance();
		String[] args = {"Luis", "Borges"};
		LoginService service = new LoginService("UserSilvogo", "UserSilvogo");
		service.dispatch();
		ExecuteFileService servicetwo = new ExecuteFileService(service.getToken(), "/home/UserSilvogo/TRUMP", args);
		servicetwo.dispatch();
		assertEquals("ExecutaLinkFile", "Hello Luis", servicetwo.result());
	}
	
	@Test(expected = CircularLinkException.class)
	public void doCircularLink(){
		MyDrive md = MyDrive.getInstance();
		String[] args = {"Luis", "Borges"};
		LoginService service = new LoginService("UserSilvogo", "UserSilvogo");
		service.dispatch();
		ExecuteFileService servicetwo = new ExecuteFileService(service.getToken(), "/home/UserSilvogo/RIPSD", args);
		servicetwo.dispatch();
	}
	
	/*execute a file with a non existing path*/
	@Test(expected = FileNotFoundException.class)
	public void doUnexistingFile(){
		String [] args = null;
		LoginService service = new LoginService("UserSilvogo", "UserSilvogo");
		service.dispatch();
		ExecuteFileService servicetwo = new ExecuteFileService(service.getToken(), "/home/UserSilvogo/NaoExiste", args);
		servicetwo.dispatch();
	}
	
}