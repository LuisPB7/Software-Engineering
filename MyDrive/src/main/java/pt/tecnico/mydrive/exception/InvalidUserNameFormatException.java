package pt.tecnico.mydrive.exception;

/**
 * 
 *
 */
public class InvalidUserNameFormatException extends MyDriveException {

	private static final long serialVersionUID = 1L;
	private String _userName;
	
	public InvalidUserNameFormatException(String name){
		_userName=name;
	}
	
	public String getUserName(){
		return _userName;
	}
	
	
	public String getMessage(){
		return "Invalid username format: "
				+ _userName
				+ ". username must not be empty nor be"
				+ " composed of symbols other than letters or numbers.";
	}
}
