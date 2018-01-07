package pt.tecnico.mydrive.exception;

public class InvalidPasswordException extends MyDriveException {

	private static final long serialVersionUID = 1L;

    public InvalidPasswordException() {
    }
/*
    public String getExistingName() {
        return _existingUserName;

    }
*/
    @Override
    public String getMessage() {
        return "Password doesn't match current registry.";
    }
	
	
}
