package pt.tecnico.mydrive.exception;

public class DuplicatedUserException extends MyDriveException {

	private static final long serialVersionUID = 1L;

    private String _existingUserName;

    public DuplicatedUserException(String name) {
        _existingUserName = name;
    }

    public String getExistingName() {
        return _existingUserName;

    }

    @Override
    public String getMessage() {
        return "This username " + _existingUserName + " is already registered";
    }
	
}
