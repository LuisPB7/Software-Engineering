package pt.tecnico.mydrive.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.DateTime;
import org.junit.Test;

import pt.tecnico.mydrive.MyDriveApplication;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.MyDriveFile;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.*;


public class ChangeDirectoryTest extends AbstractServiceTest {
	
	private long token;

	protected void populate() {
		// TODO Auto-generated method stub
		MyDrive m = MyDrive.getInstance();
		m.cleanup();
		m.createUser("userSofia");
		m.createUser("luispb78");
		token = m.createSession("userSofia", "userSofia");
		User u = m.getUser("userSofia");
		User user = m.getUser("luispb78");
		Directory userDir = m.getDirectory(u.get_userName(), u.get_homeDir());
		String fileName="plainText";
		String filePath=getCurrentDirPath()+"/"+fileName;
		String defaultPermission = "";
		String contentTest="TEXT FILE TEST";
		String contentTest2="TEXT//FILE//TEST";
		String contentTest3="package.class.method";
		m.createFile(fileName, filePath, contentTest , u, userDir, "Plain");
		m.createFile("appText", getCurrentDirPath()+"/"+"appText",  contentTest3, u, userDir, "App");	
		m.createFile("linkText", getCurrentDirPath()+"/"+"linkText", contentTest2 , u, userDir, "Link");
		m.createFile("randomDir", getCurrentDirPath()+"/"+"randomDir", null, u, m.getDirectory(u.get_userName(), u.get_homeDir()), "Directory");
		m.createFile("luisDir", user.get_homeDir()+"/"+"luisDir", null, user, m.getDirectory(user.get_userName(), user.get_homeDir()), "Directory");
		//diretoria luisDir tem de ter permissões especiais
		//MyDriveApplication.seeMD();
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
		ChangeDirectoryService servicetwo = new ChangeDirectoryService(service.getToken(), ".");
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
		ChangeDirectoryService servicetwo = new ChangeDirectoryService(service.getToken(), ".");
		servicetwo.dispatch();

		
    }
    
    
    @Test(expected = TimedOutSessionException.class)
    public void invalidLoginAfterTimeLimit(){
		MyDrive m = MyDrive.getInstance();

		LoginService service = new LoginService("luispb78", "luispb78");
		service.dispatch();
		Session s = m.getSessionByToken(service.getToken());
		DateTime dt = new DateTime();
		DateTime added = dt.plusHours(-4);
		s.set_lastAccess(added);
		ChangeDirectoryService servicetwo = new ChangeDirectoryService(service.getToken(), ".");
		servicetwo.dispatch();

		
    }
	
	//------------------------------------------------------------------------//
	
	private String getCurrentDirPath(){
		MyDrive m = MyDrive.getInstance();
		return m.getSessionByToken(token).getCurrentDir().get_path();
	}
	
	private User getActiveUser(){
		MyDrive m = MyDrive.getInstance();
		return m.getSessionByToken(token).getCurrentUser();
	}
	
	@Test
	public void ChangeToDot(){
		MyDrive m = MyDrive.getInstance();
		ChangeDirectoryService service = new ChangeDirectoryService(token, ".");
		service.dispatch();
		assertEquals("Dot directory is the same directory", getCurrentDirPath(), service.result());
	}
	
	@Test
	public void ChangeToDotTwice(){
		MyDrive m = MyDrive.getInstance();
		ChangeDirectoryService service = new ChangeDirectoryService(token, "./.");
		service.dispatch();
		assertEquals("Dot directory is always the same directory", getCurrentDirPath(), service.result());
	}
	
	@Test
	public void ChangeToPathDot(){
		ChangeDirectoryService service = new ChangeDirectoryService(token, "/home/userSofia/.");
		service.dispatch();
		assertEquals("Dot directory is always the same directory", "/home/userSofia", service.result());
	}
	
	@Test
	public void ChangeToDotExistingDirectory(){
		String dirBefore = getCurrentDirPath();
		ChangeDirectoryService service = new ChangeDirectoryService(token, "./randomDir");
		service.dispatch();
		//System.out.println(service.result());
		//System.out.println(MyDrive.getInstance().ListDirectory(dirBefore));
		assertEquals("Dot/directory is OK?", dirBefore + "/randomDir", service.result());
		//System.out.println(MyDrive.getInstance().ListDirectory(dirBefore));
		//assertNull(null);
	}
	
	@Test
	public void ChangeToFatherDirectory(){
		ChangeDirectoryService service = new ChangeDirectoryService(token, "/home/userSofia/randomDir");
		service.dispatch();
		ChangeDirectoryService service1 = new ChangeDirectoryService(token, "/home/userSofia");
		service1.dispatch();
		assertEquals("Directory/father is OK?", "/home/userSofia", service1.result());
	}
	
	@Test(expected = DirectoryNotFoundException.class)
	public void RandomDirectoryDoesNotExist(){
		ChangeDirectoryService service = new ChangeDirectoryService(token, "/home/userSofia/DiretoriaInexistente");
		service.dispatch();
	}
	
	@Test(expected = DirectoryNotFoundException.class)
	public void DotAndDirectoryDoesNotExist(){
		ChangeDirectoryService service = new ChangeDirectoryService(token, "./DiretoriaInexistente");
		service.dispatch();
	}
	
	@Test(expected = DirectoryNotFoundException.class)
	public void BadlyWrittenDirectory(){
		ChangeDirectoryService service = new ChangeDirectoryService(token, "diretoriamalescrita");
		service.dispatch();
		//MyDrive.getInstance().getDirectory("diretoriamalescrita");
	}
	
	@Test(expected = DirectoryNotFoundException.class)
	public void ChangeDirectoryToPlainFile(){
		ChangeDirectoryService service = new ChangeDirectoryService(token, "/home/userSofia/plainText");
		service.dispatch();
	}
	
	@Test(expected = DirectoryNotFoundException.class)
	public void ChangeDirectoryToLinkFile(){
		ChangeDirectoryService service = new ChangeDirectoryService(token, "/home/userSofia/linkText");
		service.dispatch();
	}
	
	@Test(expected = DirectoryNotFoundException.class)
	public void ChangeDirectoryToAppFile(){
		ChangeDirectoryService service = new ChangeDirectoryService(token, "/home/userSofia/appText");
		service.dispatch();
	}
	
	@Test(expected = FileNotFoundException.class)
	public void DeleteThenChange(){
		ChangeDirectoryService service = new ChangeDirectoryService(token, "/home/userSofia/randomDir");
		service.dispatch();
		DeleteFileService service1 = new DeleteFileService(token, "randomDir");
		service1.dispatch();
		ChangeDirectoryService service2 = new ChangeDirectoryService(token, "/home/userSofia/randomDir");
		service2.dispatch();
	}
//	
//	@Test(expected = ReadPermissionDeniedException.class)
//	public void ChangeDirectoryToHome(){
//		ChangeDirectoryService service = new ChangeDirectoryService(token, "/home");
//		service.dispatch();
//	}
//	
//	@Test(expected = ReadPermissionDeniedException.class)
//	public void ChangeDirectoryToAnotherUserHome(){
//		ChangeDirectoryService service = new ChangeDirectoryService(token, "/home/luispb78");
//		service.dispatch();
//	}
//	
//	@Test(expected = ReadPermissionDeniedException.class)
//	public void ChangeDirectoryToAnotherUserDirectory(){
//		ChangeDirectoryService service = new ChangeDirectoryService(token, "/home/luispb78/luisDir");
//		service.dispatch();
//	}
	
//	@Test(expected = ReadPermissionDeniedException.class)
//	public void KeepChangingToParent(){
//		ChangeDirectoryService service = new ChangeDirectoryService(token, "/home/userSofia/randomDir");
//		service.dispatch();
//		ChangeDirectoryService service1 = new ChangeDirectoryService(token, "/home/userSofia");
//		service1.dispatch();
//		ChangeDirectoryService service2 = new ChangeDirectoryService(token, "/home");
//		service2.dispatch();
//	}
//	
//	@Test(expected = ReadPermissionDeniedException.class)
//	public void KeepChangingToParentDotDotTwice(){
//		ChangeDirectoryService service = new ChangeDirectoryService(token, "/home/userSofia/randomDir");
//		service.dispatch();
//		ChangeDirectoryService service1 = new ChangeDirectoryService(token, "..");
//		service1.dispatch();
//		ChangeDirectoryService service2 = new ChangeDirectoryService(token, "..");
//		service2.dispatch();
//	}
	
	@Test
	public void ChangeToDotDot(){
		ChangeDirectoryService service = new ChangeDirectoryService(token, "/home/userSofia/randomDir");
		service.dispatch();
		ChangeDirectoryService service1 = new ChangeDirectoryService(token, "..");
		service1.dispatch();
		assertEquals("Dot-dot directory is always its parent", "/home/userSofia", service1.result());
	}
	
	@Test
	public void ChangeToDotDotDot(){
		ChangeDirectoryService service = new ChangeDirectoryService(token, "/home/userSofia/randomDir");
		service.dispatch();
		ChangeDirectoryService service1 = new ChangeDirectoryService(token, "..");
		service1.dispatch();
		ChangeDirectoryService service2 = new ChangeDirectoryService(token, ".");
		service2.dispatch();
		assertEquals("Dot-dot directory is always its parent", "/home/userSofia", service2.result());
	}
	
	@Test
	public void ChangeToDotDotSlashDot(){
		ChangeDirectoryService service = new ChangeDirectoryService(token, "/home/userSofia/randomDir");
		service.dispatch();
		//assertEquals("PATHS EQUALS:::::","/home/userSofia/randomDir",MyDrive.getInstance().getSessionByToken(token).getCurrentDir().get_path());
		ChangeDirectoryService service1 = new ChangeDirectoryService(token, "../.");
		service1.dispatch();
		assertEquals("Dot-dot directory is always its parent", "/home/userSofia", service1.result());
	}

	@Test
	public void ChangeToDotSlashDotDot(){
		ChangeDirectoryService service = new ChangeDirectoryService(token, "/home/userSofia/randomDir");
		service.dispatch();
		ChangeDirectoryService service1 = new ChangeDirectoryService(token, "./..");
		service1.dispatch();
		assertEquals("Dot-dot directory is always its parent", "/home/userSofia", service1.result());
	}
//	
//	@Test(expected = ReadPermissionDeniedException.class)
//	public void KeepChangingToParentDotDot(){
//		ChangeDirectoryService service = new ChangeDirectoryService(token, "/home/userSofia/randomDir");
//		service.dispatch();
//		ChangeDirectoryService service1 = new ChangeDirectoryService(token, "../..");
//		service1.dispatch();
//	}
	
	@Test
	public void ChangeToDotDotDirectory(){
		ChangeDirectoryService service = new ChangeDirectoryService(token, "/home/userSofia/randomDir");
		service.dispatch();
		ChangeDirectoryService service1 = new ChangeDirectoryService(token, "../randomDir");
		service1.dispatch();
		assertEquals("Dot-dot directory is always its parent", "/home/userSofia/randomDir", service1.result());
	}
	
	@Test
	public void ChangeToPathDotDot(){
		ChangeDirectoryService service = new ChangeDirectoryService(token, "/home/userSofia/randomDir");
		service.dispatch();
		ChangeDirectoryService service1 = new ChangeDirectoryService(token, "/home/userSofia/randomDir/..");
		service1.dispatch();
		assertEquals("Dot-dot directory is always its parent", "/home/userSofia", service1.result());
	}
	
	@Test(expected = SessionDoesNotExistException.class)
	public void ChangeDirWithSessionNull(){
		MyDrive m = MyDrive.getInstance();
		ChangeDirectoryService service = new ChangeDirectoryService(12345, "/home/userSofia/randomDir");
		service.dispatch();
		
	}
	
	@Test(expected = ExecutePermissionDeniedException.class)
	public void ChangeDirectoryDenied(){
		MyDrive m = MyDrive.getInstance();
		Directory dir = m.getDirectory("/home/luispb78");
		dir.set_filePermission("----");
		ChangeDirectoryService service = new ChangeDirectoryService(token, "/home/luispb78");
		service.dispatch();
		
	}
	/*
	@Test
	public void EnvLinkSuccess(){
		
		String content = "$abecedario/project";
		String abecedario = "/home/abc";
		String cena = "/home/abc/project";
		
		new MockUp<ResolveEnvLinkService>() {
			@Mock
			String getResult() { 
				return "/home/abc/project"; 
			}
		};
		
		ResolveEnvLinkService service = new ResolveEnvLinkService(content);
		service.dispatch();
		
		GetEnvVarService value = new GetEnvVarService(token, content);
		String v = value.result();
		assertEquals(v, abecedario);
		
		ChangeDirectoryService path = new ChangeDirectoryService(token, "/home/test/notfound");
		path.dispatch();
		
	}*/
	
}