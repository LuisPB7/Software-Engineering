package pt.tecnico.mydrive.exception;

public class DeleteFileException extends MyDriveException {
	private static final long serialVersionUID = 1L;
	private String _userName;
    private String _file;
    public DeleteFileException(String username, String fileName) {
        _userName = username;
        _file=fileName;
    }

    public String getUserName() {
        return _userName;
    }
    
    public String getFileName() {
        return _file;
    }

    @Override
    public String getMessage() {
        return  "User: "+ getUserName()
        		+" cannot delete file: "+ getFileName();
    }
}
