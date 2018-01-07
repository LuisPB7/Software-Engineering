package pt.tecnico.mydrive.exception;


public class SessionDoesNotExistException extends MyDriveException {
	private static final long serialVersionUID = 1L;

    private long _token;

    public SessionDoesNotExistException(long token) {
        _token = token;
    }

    public long getToken() {
        return _token;
    }

    @Override
    public String getMessage() {
        return "Session with token = " + _token + " does not exist";
    }
}