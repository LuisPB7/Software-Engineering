package pt.tecnico.mydrive.service;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.FileNotFoundException;
import pt.tecnico.mydrive.exception.InvalidPasswordException;
import pt.tecnico.mydrive.exception.UserDoesNotExistException;
import java.util.Random;


public class LoginService extends MyDriveService{
	private String _username;
	private String _password;
	private long _token;
	
	public LoginService(String username, String password) {
		_username = username;
		_password = password;
	}
	
	
	public long getToken(){
		return _token;
	}
	
	public void removeUnvalidSessions() {
		MyDrive md = MyDrive.getInstance();
		for ( Session s : md.getSessionsSet() ) {
			if(s.getCurrentUser().get_userName().equals("root")){
				if( s.get_lastAccess().plusMinutes(10).isBeforeNow()) {
					md.removeSessions(s);
				}
			}
			else if(s.getCurrentUser().get_userName().equals("Guest")){
				continue;
			}
			else if( s.get_lastAccess().plusHours(2).isBeforeNow()) {
				md.removeSessions(s);
			}
		}
	}

	public final void dispatch() throws UserDoesNotExistException, InvalidPasswordException {
		removeUnvalidSessions();
		User u = MyDrive.getInstance().getUser(_username);
		if(u.get_userName().equals("Guest")){
			_token = MyDrive.getInstance().createSession(_username, null);
			return;
		}
		
		if(_password.equals(u.get_password())){
			_token = MyDrive.getInstance().createSession(_username, _password);
		}
		else{
			throw new InvalidPasswordException();
		}
	}
}
