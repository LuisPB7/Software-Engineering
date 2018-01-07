package pt.tecnico.mydrive.presentation;
import pt.tecnico.mydrive.service.LoginService;

public class Login extends MdCommand {
	private long _token;
	
	public long getToken(){
		return _token;
	}
    public Login(Shell sh) { super(sh, "login", "login user"); }
    
    public void execute(String[] args) {
    	LoginService login;
	if (args.length < 1 || args.length > 2)
	    throw new RuntimeException("USAGE: "+name()+" name");
	if(args.length==1){
		if(args[0].equals("Guest")){
			login = new LoginService(args[0], null);
		}
		else{
			login = new LoginService(args[0], args[0]);
		}
	}
	else{
		login = new LoginService(args[0], args[1]);
	}
		login.execute();
		_token=login.getToken();
		((MdShell)shell()).changeSession(_token);		
	
    }
}
