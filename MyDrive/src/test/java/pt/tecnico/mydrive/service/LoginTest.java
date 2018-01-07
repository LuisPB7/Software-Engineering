package pt.tecnico.mydrive.service;

import static org.junit.Assert.*;

import org.joda.time.DateTime;

//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import pt.tecnico.mydrive.domain.*;

import pt.tecnico.mydrive.exception.InvalidPasswordException;
import pt.tecnico.mydrive.exception.SessionDoesNotExistException;
import pt.tecnico.mydrive.exception.TimedOutSessionException;
import pt.tecnico.mydrive.exception.UserDoesNotExistException;
import pt.tecnico.mydrive.service.LoginService;
import pt.tecnico.mydrive.service.AbstractServiceTest;


public class LoginTest extends AbstractServiceTest {
	

	public static final String nullUser = null;
	public static final String randomPassword = "1pAsSwOrD12"; 
	public static final String nullPassword = null;
	public static  long token;
	
	
	@Override
    protected void populate() {
		
		MyDrive m = MyDrive.getInstance();
		m.cleanup();
//		System.out.println("/n /n ------------------------------------");
//		System.out.println(m.getUserListSet());
		User pFnX = m.createUser("pFnX", "pFpass123");
		User jpmm = m.createUser("jmiguens", "miguenspass341");
		
    }

	
	
    @Test
    public void successfulLogin(){
    	
		LoginService service = new LoginService("pFnX", "pFpass123");
		MyDrive m = MyDrive.getInstance();
        service.dispatch();
        System.out.println("USER: " + m.getSessionByToken(service.getToken()).getCurrentUser().toString());
		
    }
    
    
    
    @Test(expected = SessionDoesNotExistException.class)
    public void invalidLoginAfterTimeLimitForRoot(){	

    	MyDrive m = MyDrive.getInstance();

		LoginService service = new LoginService("root", "***");
		service.dispatch();
		Session s = m.getSessionByToken(service.getToken());
		DateTime dt = new DateTime();
		DateTime added = dt.plusMinutes(-11);
		s.set_lastAccess(added);
		LoginService servicetwo = new LoginService("pFnX", "pFpass123");
		servicetwo.dispatch();
        Session s2 = m.getSessionByToken(service.getToken());

        service.dispatch();
		
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
		LoginService servicetwo = new LoginService("pFnX", "pFpass123");
		servicetwo.dispatch();
        Session s2 = m.getSessionByToken(service.getToken());

        service.dispatch();
		
    }
    
    @Test
    public void createSession(){	

    	MyDrive m = MyDrive.getInstance();
		LoginService service = new LoginService("Guest", null);
		service.dispatch();
		Session s = m.getSessionByToken(service.getToken());
		assertNotNull(s);		
    }


    @Test
    public void successfulLoginTwoSessions(){
		MyDrive m = MyDrive.getInstance();

		LoginService service = new LoginService("pFnX", "pFpass123");
		LoginService servicetwo = new LoginService("pFnX", "pFpass123");
        service.dispatch();
		servicetwo.dispatch();
		
		assertEquals(m.UserByToken(service.getToken()), m.UserByToken(servicetwo.getToken()));

		
    }
    
    @Test(expected = SessionDoesNotExistException.class)
    public void invalidLoginAfterTimeLimit(){
		MyDrive m = MyDrive.getInstance();

		LoginService service = new LoginService("jmiguens", "miguenspass341");
		service.dispatch();
		Session s = m.getSessionByToken(service.getToken());
		DateTime dt = new DateTime();
		DateTime added = dt.plusHours(-4);
		s.set_lastAccess(added);
		LoginService servicetwo = new LoginService("pFnX", "pFpass123");
		servicetwo.dispatch();
        Session s2 = m.getSessionByToken(service.getToken());

		
    }
	
    
	
    @Test(expected = UserDoesNotExistException.class)
    public void loginWithUnknownUser() throws Exception {
    	LoginService service = new LoginService("ZeManel", "pass1234");
		service.dispatch();
	
    }
	
	 @Test(expected = InvalidPasswordException.class)
    public void loginWithKnownUserWithWrongPassword() throws Exception {
		 LoginService service = new LoginService("jmiguens", "pFpass1234");
		service.dispatch();
		
	}
		 
	
	@Test(expected = NullPointerException.class)
    public void loginWithNullPassword() throws Exception {
		LoginService service = new LoginService("jmiguens", nullPassword);
		service.dispatch();
		
    }
	
	@Test(expected = InvalidPasswordException.class)
    public void loginWithEmptyPassword() throws Exception {
		LoginService service = new LoginService("pFnX", "");
		service.dispatch();
	}
	
	@Test(expected = InvalidPasswordException.class)
    public void loginWithAnotherUserPassword() throws Exception {
		LoginService service = new LoginService("pFnX", "miguenspass341");
		service.dispatch();
	}
	
	@Test(expected = UserDoesNotExistException.class)
    public void loginWithUnknownUserWithKnownPassword() throws Exception {
		LoginService service = new LoginService("MariaAMELIA", "miguensPass341");
		service.dispatch();
	}
	
	@Test(expected = UserDoesNotExistException.class)
    public void loginWithNullUsername() throws Exception {
		LoginService service = new LoginService(nullUser, randomPassword);
		service.dispatch();
	}
	
	
	@Test(expected = UserDoesNotExistException.class)
    public void loginWithEmptyUsername() throws Exception {
		LoginService service = new LoginService("", randomPassword);
		service.dispatch();
    }
	
	@Test(expected = UserDoesNotExistException.class)
    public void loginWithUsernameAndPasswordNull()throws Exception {
		LoginService service = new LoginService(nullUser, nullPassword);
		service.dispatch();
    }
	
	@Test(expected = UserDoesNotExistException.class)
    public void loginWithEmptyUsernameAndPassword()throws Exception {
		LoginService service = new LoginService("", "");
		service.dispatch();
		}
		
}