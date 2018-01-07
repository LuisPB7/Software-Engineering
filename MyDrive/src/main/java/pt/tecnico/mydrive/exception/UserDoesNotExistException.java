package pt.tecnico.mydrive.exception;


public class UserDoesNotExistException extends MyDriveException {
	private static final long serialVersionUID = 1L;

    private String _userName;

    public UserDoesNotExistException(String name) {
        _userName = name;
    }

    public String getUserName() {
        return _userName;
    }

    @Override
    public String getMessage() {
        return "User " + _userName + " isn't registered";
    }
}
