package pt.tecnico.mydrive.service;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;

import pt.tecnico.mydrive.domain.EnvVar;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.exception.DuplicatedDirectoryException;
import pt.tecnico.mydrive.exception.InvalidContentFormatException;
import pt.tecnico.mydrive.exception.InvalidLinkFormatException;
import pt.tecnico.mydrive.exception.SessionDoesNotExistException;
import pt.tecnico.mydrive.exception.TimedOutSessionException;
import pt.tecnico.mydrive.exception.WritePermissionDeniedException;
import pt.tecnico.mydrive.exception.UnknownVariableException;



public class AddEnvironmentalVariableTest extends AbstractServiceTest {
	private long token;
	private long token2;
	private String varname;
	private String varname2;
	private String value;
	private String value2;	
	private EnvVar var;
	private User u;
	private User u2;
	private MyDrive m;
	
	@Override
	protected void populate() {
		
		m = MyDrive.getInstance();
		m.cleanup();		
		m.createUser("sofiaTOSTAS");
		m.createUser("usermiguens");
		u = m.getUser("sofiaTOSTAS");
		u2 = m.getUser("usermiguens");
		varname = "inteiro";
		varname2 = "real";
		value = "527";
		value2 = "fr4";
		
		
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
			AddEnvironmentalVariableService servicetwo = new AddEnvironmentalVariableService(service.getToken(), varname, value);
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
			AddEnvironmentalVariableService servicetwo = new AddEnvironmentalVariableService(service.getToken(), varname, value);
			servicetwo.dispatch();

			
	    }
	    
	    
	    @Test(expected = TimedOutSessionException.class)
	    public void invalidLoginAfterTimeLimit(){
			MyDrive m = MyDrive.getInstance();

			LoginService service = new LoginService("sofiaTOSTAS", "sofiaTOSTAS");
			service.dispatch();
			Session s = m.getSessionByToken(service.getToken());
			DateTime dt = new DateTime();
			DateTime added = dt.plusHours(-4);
			s.set_lastAccess(added);
			AddEnvironmentalVariableService servicetwo = new AddEnvironmentalVariableService(service.getToken(), varname, value);
			servicetwo.dispatch();

			
	    }
	
	//------------------------------------------------------------------------//

	@Test
	public void ListVarTestTwoVars() {
		token = m.createSession(u.get_userName(), u.get_password());
		AddEnvironmentalVariableService Env = new AddEnvironmentalVariableService(token, varname, value);
		Env.dispatch();
		EnvVar variable = Env.getVar();
		AddEnvironmentalVariableService Envtwo = new AddEnvironmentalVariableService(token, varname2, value2);
		Envtwo.dispatch();
		EnvVar variabletwo = Envtwo.getVar();
		assertEquals(variabletwo.list() + "\n" + variable.list() , Envtwo.getResult());
	}
	

	
	@Test
	public void ListVarTesttwo() {
		token = m.createSession(u.get_userName(), u.get_password());
		AddEnvironmentalVariableService Env = new AddEnvironmentalVariableService(token, varname, value);
		Env.dispatch();
		EnvVar variable = Env.getVar();
		AddEnvironmentalVariableService Envtwo = new AddEnvironmentalVariableService(token, varname);		
		Envtwo.dispatch();
		assertEquals(variable.getValue(), Envtwo.getResult());
	}
	
	
	@Test
	public void ListVarTest() {
		token = m.createSession(u.get_userName(), u.get_password());
		AddEnvironmentalVariableService Env = new AddEnvironmentalVariableService(token, varname, value);
		Env.dispatch();
		EnvVar variable = Env.getVar();
		AddEnvironmentalVariableService Envtwo = new AddEnvironmentalVariableService(token);		
		Envtwo.dispatch();
		assertEquals(variable.getName() + "=" + variable.getValue(), Envtwo.getResult());
	}
	
	@Test
	public void successListVarTest() {
		token = m.createSession(u.get_userName(), u.get_password());
		AddEnvironmentalVariableService Env = new AddEnvironmentalVariableService(token, varname, value);
		Env.dispatch();
		EnvVar variable = Env.getVar();
		
		//assertEquals(variable.list(), Env.getResult());
	}
	
	
	@Test
	public void successEmptyListVarTest() {
		token = m.createSession(u.get_userName(), u.get_password());
		AddEnvironmentalVariableService Env = new AddEnvironmentalVariableService(token, null, null);
		Env.dispatch();
		EnvVar variable = Env.getVar();
		
//		if(variable!=null)
//			System.out.println(variable.list());
		assertEquals("", Env.getResult());
	}
	
	
	@Test
	public void successVarValsTest() {
		token = m.createSession(u.get_userName(), u.get_password());
		AddEnvironmentalVariableService Env = new AddEnvironmentalVariableService(token, varname, value);
		Env.dispatch();
		EnvVar variable = Env.getVar();
		
		assertEquals(variable.getName(), varname);
		assertEquals(variable.getValue(), value);
	}
		
	@Test
	public void VariableRedefinitionTest(){
		token = m.createSession(u.get_userName(), u.get_password());
		
		AddEnvironmentalVariableService Env = new AddEnvironmentalVariableService(token, varname, value);
		Env.dispatch();
		int size1 = m.getSessionByToken(token).getVariableListSet().size();

		
		Env = new AddEnvironmentalVariableService(token, varname, value2);
		Env.dispatch();
		int size2 = m.getSessionByToken(token).getVariableListSet().size();

		assertTrue((size1 - size2) == 0);
		
	}
	
	@Test
	public void MultipleVariableListTest(){
		token = m.createSession(u.get_userName(), u.get_password());
		
		AddEnvironmentalVariableService Env = new AddEnvironmentalVariableService(token, varname, value);
		Env.dispatch();
		int size1 = m.getSessionByToken(token).getVariableListSet().size();

		
		Env = new AddEnvironmentalVariableService(token, varname2, value2);
		Env.dispatch();
		int size2 = m.getSessionByToken(token).getVariableListSet().size();

		assertTrue((size2 - size1) == 1);
		
	}
	
	
	
	@Test
	public void SecondEnvironmentalVarTest(){
		token = m.createSession(u.get_userName(), u.get_password());
		
		AddEnvironmentalVariableService Env = new AddEnvironmentalVariableService(token, varname, value);
		Env.dispatch();
		
		Env = new AddEnvironmentalVariableService(token, varname, value2);
		Env.dispatch();

		EnvVar v = m.getEnvironmentalVariableFromSession(m.getSessionByToken(token), varname);

		assertEquals(v.getName(), varname);
		assertNotEquals(v.getValue(), value);
		assertEquals(v.getValue(), value2);
		
		
	}
	
	
	
	@Test
	public void DiferentSessionsSameVarNameTest(){
		token = m.createSession(u.get_userName(), u.get_password());
		AddEnvironmentalVariableService Env = new AddEnvironmentalVariableService(token, varname, value);
		Env.dispatch();
		
		token2 = m.createSession(u2.get_userName(), u2.get_password());
		AddEnvironmentalVariableService Env2 = new AddEnvironmentalVariableService(token2, varname, value2);
		Env2.dispatch();
		
		EnvVar variable = Env.getVar();
		EnvVar variable2 = Env2.getVar();
		
		assertEquals(variable2.getName(), variable.getName());
		assertNotEquals(variable2.getValue(), variable.getValue());
		
	}
	
	@Test(expected=UnknownVariableException.class)
	public void DiferentSessionsGetVariableNoteExistingTest(){
		token = m.createSession(u.get_userName(), u.get_password());
		AddEnvironmentalVariableService Env = new AddEnvironmentalVariableService(token, varname, value);
		Env.dispatch();
		assertNotNull(Env.getVar());
		
		token2 = m.createSession(u2.get_userName(), u2.get_password());
		
		EnvVar v = m.getEnvironmentalVariableFromSession(m.getSessionByToken(token2), varname);
				
	}
	
//	@Test
//	public void AddEnvVarWithVarsInPath(){
//		token = m.createSession(u.get_userName(), u.get_password());
//		AddEnvironmentalVariableService Env = new AddEnvironmentalVariableService(token, varname, value);
//		Env.dispatch();
//		EnvVar var = Env.getVar();
//		String varNameWithPath = "/home/root/$" + var.getName();
//		System.out.println(m.parseEnvVar(m.getSessionByToken(token), varNameWithPath));
//		System.out.println("----------------");
//		 varNameWithPath = "$"+var.getName()+"/home/root/$" + var.getName();
//		System.out.println(m.parseEnvVar(m.getSessionByToken(token), varNameWithPath));
//		System.out.println("----------------");
//		 varNameWithPath = "$" + var.getName();
//		System.out.println(m.parseEnvVar(m.getSessionByToken(token), varNameWithPath));
//		System.out.println("----------------");
//		 varNameWithPath = "/home/root/$" + var.getName()+"/path/$"+var.getName();
//		System.out.println(m.parseEnvVar(m.getSessionByToken(token), varNameWithPath));
//		System.out.println("----------------");
//		
//	}
	
}