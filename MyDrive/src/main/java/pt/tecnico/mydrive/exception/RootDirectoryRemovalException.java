package pt.tecnico.mydrive.exception;

public class RootDirectoryRemovalException extends MyDriveException {
	private static final long serialVersionUID = 1L;

    private String _userName;
    private String _path;
    public RootDirectoryRemovalException(String username, String path) {
        _userName = username;
        _path=path;
    }

    public String getUserName() {
        return _userName;
    }
    
    public String getpath() {
        return _path;
    }

    @Override
    public String getMessage() {
        return  "ROOT DIRECTORY CANNOT BE ERASED!";
    }
}
