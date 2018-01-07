package pt.tecnico.mydrive.exception;

public class DeleteNobodyException extends MyDriveException {
	
	private static final long serialVersionUID = 1L;
	private String _userName;
    public DeleteNobodyException(String username) {
        _userName = username;
    }

    public String getUserName() {
        return _userName;
    }
    
    @Override
    public String getMessage() {
        return  "Cannot delete "+ getUserName();
    }

}
