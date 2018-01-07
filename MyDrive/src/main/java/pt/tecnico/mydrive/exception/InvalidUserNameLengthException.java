package pt.tecnico.mydrive.exception;

/**
 * 
 *
 */
public class InvalidUserNameLengthException extends MyDriveException {

	private static final long serialVersionUID = 1L;
	private String _userName;
	
	public InvalidUserNameLengthException(String name){
		_userName=name;
	}
	
	public String getUserName(){
		return _userName;
	}
	
	
	public String getMessage(){
		return "Invalid username format: "
				+ _userName
				+ ". username must contain more than 2 character.";
	}
}
