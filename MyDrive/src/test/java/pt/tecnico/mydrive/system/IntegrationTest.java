package pt.tecnico.mydrive.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.junit.runner.RunWith;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import pt.tecnico.mydrive.service.AbstractServiceTest;
import pt.tecnico.mydrive.service.AddEnvironmentalVariableService;
import pt.tecnico.mydrive.service.ChangeDirectoryService;
import pt.tecnico.mydrive.service.ExecuteFileService;
import pt.tecnico.mydrive.service.GetEnvVarService;
import pt.tecnico.mydrive.service.KeyService;
import pt.tecnico.mydrive.service.ListDirectoryService;
import pt.tecnico.mydrive.service.LoginService;
import pt.tecnico.mydrive.service.ResolveEnvLinkService;
import pt.ist.fenixframework.DomainRoot;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.User;

@RunWith(JMockit.class)
public class IntegrationTest extends AbstractServiceTest {

   // private static final List<String> names = new ArrayList<String>();
    private static String u1 = "ritto", u2 = "miguens", u3 = "root";
    private static String p1 = "pFnXPW123", p2 = "miguensPW123", p3 = "***";
    private static String var1="variavel1";
    private static String val1="DIRritto";
    private static String pathMiguens="/home/ritto/DIRmiguens";
    private static long token;



    
    //MAYBE HERE???????????? private static final String importFile = "other.xml";
    //private static final int phone2 = 345678900, phone5 = 935667654;

    protected void populate() { // populate mockup
    	
    	MyDrive md = MyDrive.getInstance();
    	md.cleanup();
    	User ritto = md.createUser(u1, p1);
    	User miguens = md.createUser(u2, p2);
    	User sofia = md.createUser("sofia", "qwerty1234");
    	md.createDirectory("DIRritto", "/home/ritto/DIRritto", ritto);
		md.createDirectory("DIRmiguens", "/home/miguens/DIRmiguens", miguens);
		md.createFile("FILEritto", "/home/ritto/DIRritto/FILEritto", "presentation.Hello.greet", ritto, md.getDirectory("/home/ritto/DIRritto"), "App");
		md.createFile("AppText", "/home/sofia/rato", "presentation.Hello.greet", sofia, md.getDirectory("/home/sofia"), "App");
		md.createFile("FILE2doritto", "/home/ritto/DIRritto/FILE2doritto", "presentation.Hello.greet", ritto, md.getDirectory("/home/ritto/DIRritto"), "Plain");
		//md.createFile("FILEmiguens", "/home/miguens/DIRmiguens/FILEmiguens", "file do miguens", miguens, md.getDirectory(miguens.get_homeDir()), "Plain");
		

    }

    @Test
    public void success() throws Exception {

    	MyDrive md = MyDrive.getInstance();
    	int nr_entries=0;
    	String directories;
    	LoginService service2 = new LoginService(u1, p1);
    	LoginService service1 = new LoginService(u2, p2);
    	LoginService service3 = new LoginService(u3, p3);
    	service1.execute();
    	service2.execute();
    	service3.execute();
    	long token1 = (service1.getToken()); 
    	long token2 = (service2.getToken());
    	long token3 = (service3.getToken());
    	KeyService ks = new KeyService(token1, u1);

    	ListDirectoryService ld = new ListDirectoryService(token2, "/home/ritto/DIRritto");
    	ld.execute();
    	directories = ld.result();
    	System.out.println("CENAS= \n\n\n"+directories);
    	
    	for(int i=0; i<directories.length(); i++)             //for that checks how many "\n" there are
    		if(directories.charAt(i)=='\n')                   //for each "\n" there is one entry
    			nr_entries++;
    	
    	assertEquals(2, nr_entries);                          //FILEritto, FILE2doritto

    	
    	//FALTA METER MOCKS PARA AS OPERAÇOES A REALIZAR COM VARIOS LINKS
    	//FALTA METER MOCKS PARA AS EXTENCOES
    	
    	
    	AddEnvironmentalVariableService ad1 = new AddEnvironmentalVariableService(token2, var1, val1);
    	AddEnvironmentalVariableService ad2 = new AddEnvironmentalVariableService(token2, var1);
    	AddEnvironmentalVariableService ad3 = new AddEnvironmentalVariableService(token2);
    	ad1.execute();
    	ad2.execute();
    	ad3.execute();
    	String result = ad2.getResult();
    	int nr_vars=0;
    	nr_vars = md.getSessionByToken(token2).getVariableListSet().size();
    	assertEquals(result, val1);                //value1
    	assertEquals(1, nr_vars);                  //only var1
    	String newPath="/home/ritto/$" + var1;
    	
    	System.out.println("NEWPATH = "+newPath);
//    	ChangeDirectoryService cd = new ChangeDirectoryService(token2, newPath);
//    	
//    	System.out.println("PATH: \n\n\nS"+md.getSessionByToken(token2).getCurrentDir().toString());
//    	assertEquals("/home/ritto/DIRritto", md.getSessionByToken(token2).getCurrentDir().get_path());
//
//    	
//    	ExecuteFileService ef = new ExecuteFileService(token2, "/home/ritto/DIRritto/FILEritto", null);
//    	ef.execute();
//    	String res= (String) ef.result();
//    	assertEquals("Hello", res);
//    	String[] args = {"Paulo", "Ritto"};
//    	ExecuteFileService ef2 = new ExecuteFileService(token2, "/home/ritto/DIRritto/FILEritto", args);
    	
    	
    	
    	/*--------------------------------------------------------------*/
    	
//    	public class EnvLinksTest extends AbstractServiceTest{
//    
//    		@Test
//    		public void testLinkWithEnvVarToUserHome(){
//    			new mockUp<EnvLinksevice>(){
//    				@Mock
//    				String result(){
//    					return rootHomePath;
//    				}	
//    			
//    				@Mock
//    				void execute(){
//    				
//    				}
//    		};
//    		
//    		EnvLinkService serv == new EnvLinkService(testLink);
//    	}
//    	
//    	public class EnvLinksTest extends AbstractServiceTest{
//    		new mockUp<EnvLinksevice>(){
//    			@Mock
//    			String result(){
//    				throw new ;
//    			}
//    			
//    			@Mock
//    			void execute(){
//    				
//    			}
//    		};
//    		EnvLinkService serv == new EnvLinkService(testLink);
//    	}
//
//    	
//    	
//    	
//    	ClassLoader loader = getClass().getClassLoader();
//	File file = new File(loader.getResource(importFile).getFile());
//	Document doc = (Document)new SAXBuilder().build(file);
//	new ImportPhoneBookService(doc).execute();
//
//	ListPeopleService lp = new ListPeopleService();
//	lp.execute();
//	System.out.print("Persons:");
//	for (String name: lp.result())
//	    System.out.print("\t" + name);
//	System.out.println(".");
//	assertEquals(lp.result().size(), 6); // Tiago, António, Miguel, Xana, Sofia
//
//        new RemoveContactService(p1, p2).execute();
//	lc = new ListPersonPhoneBook(p1);
//	lc.execute();
//	assertEquals(lc.result().size(), 1);
//
//        new RemovePersonService(p1).execute();
//	lp = new ListPeopleService();
//	lp.execute();
//	assertEquals(lp.result().size(), 5); // António, Miguel, Xana, Sofia
//
//	ExportPhoneBookService exp = new ExportPhoneBookService();
//	exp.execute();
//	// exported the same number of persons!
//	assertEquals(doc.getRootElement().getChildren().size(),
//		     exp.result().getRootElement().getChildren().size());
//
//        new MockUp<FindPersonsWithContactService>() {
//	  @Mock
//	  List<String> result() { return names; }
//	};
//        FindPersonsWithContactService s = new FindPersonsWithContactService(p3);
//        s.execute();
//        assertEquals(s.result().size(), 2);
//
//        new MockUp<PhoneBook>() {
//	  @Mock
//          int getPhoneNumberByContact(String p2) { return phone2; }
//        };
//        FindPhoneNumberService pn = new FindPhoneNumberService(p2);
//        pn.execute();
//        assertEquals(pn.result(), phone2);
    }
	
	@Test
	public void EnvLinkSuccess(){
		
		String content = "$ABC/project";
		String abecedario = "ABC";
		String cena = "/home/abc/project";
		
		new MockUp<ResolveEnvLinkService>() {
			@Mock
			String getResult() { 
				return "/home/abc/project"; 
			}
		};
		
		LoginService service2 = new LoginService("root", "***");
		service2.dispatch();
		token = service2.getToken();
		AddEnvironmentalVariableService variable = new AddEnvironmentalVariableService(token, abecedario, "home/abc");
		variable.dispatch();
		
		String cena2 = MyDrive.getInstance().parseEnvVar(MyDrive.getInstance().getSessionByToken(token), content);
		//GetEnvVarService service = GetEnvVarService(content.split("/"));
		//content.split("/")
		assertEquals(cena, cena2);
/*
		
		
		ResolveEnvLinkService service = new ResolveEnvLinkService(content);
		service.dispatch();

		
		parseEnvVar value = new parseEnvVar(token, content);
		System.out.println("================================================================================");
		value.dispatch();
		System.out.println("********************************************************************************");

		assertEquals(abecedario, value.result());
		System.out.println("################################################################################");
*/
		
		
		//ChangeDirectoryService path = new ChangeDirectoryService(token, "/home/test/notfound");
		//path.dispatch();
		
	}
		
	@Test
	public void ExecuteFileTest2() throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		String[] args = {"coisas"};

		new MockUp<ExecuteFileService>() {
			@Mock
			String getFileByAssociation(String extention){
				return "/home/sofia/rato";
			}
		};
				
		LoginService service2 = new LoginService("sofia", "qwerty1234");
		service2.dispatch();
		token = service2.getToken();
		
		ExecuteFileService service = new ExecuteFileService(token, "/home/sofia/rato", args);
		service.dispatch();
		
		assertEquals("Hello coisas", service.result());
	}
}