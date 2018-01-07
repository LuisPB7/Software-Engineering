package pt.tecnico.mydrive.exception;

public class TimedOutSessionException extends MyDriveException {
	private static final long serialVersionUID = 1L;

    private long _token;

    public TimedOutSessionException(long token) {
        _token = token;
    }

    public long getToken() {
        return _token;
    }

    @Override
    public String getMessage() {
        return "Session with token = " + _token + " has timed out";
    }
}
