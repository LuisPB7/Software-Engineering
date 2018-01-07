package pt.tecnico.mydrive.exception;

public class UserAccessLevelException extends MyDriveException {

	private static final long serialVersionUID = 1L;
	private String _userName;
	private String _accessLevel;
	
	public UserAccessLevelException(String user, String access){
		_userName=user;
		_accessLevel=access;
	}

	/**
	 * @return the _userName
	 */
	public String get_userName() {
		return _userName;
	}

	/**
	 * @return the _accessLevel
	 */
	public String get_accessLevel() {
		return _accessLevel;
	}

	public String getMessage(){
		return "User: "+_userName
				+" permission level doesn't allow intended action."
				+"Current Access Level: "
				+_accessLevel;
	}
}
