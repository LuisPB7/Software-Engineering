package pt.tecnico.mydrive.presentation;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.*;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.service.LoginService;

public class MdShell extends Shell {
	//LoginService login = new LoginService("Guest", null);
	private long _token;

	
  public static void main(String[] args) throws Exception {
	//MyDriveApplication.setup();
	MdShell sh = new MdShell();
    sh.execute();
    
  }

  public MdShell() { // add commands here
    super("MyDrive");  
    String[] arg = {"Guest"};
    Login l = new Login(this);
    l.execute(arg);
    new Key(this);
    new List(this);
    new AddEnvironmmentVariable(this);
//  new CreateFile(this);
//  new DeleteFile(this);
    new ExecuteFile(this);
    new ChangeWorkingDirectory(this);
//  new ReadFile(this);
    new Write(this);
  }
  
  public void changeSession(long token){
	  _token = token;
  }
  
  public long getSession(){
	  return _token;
  }
  
}
