package pt.tecnico.mydrive.exception;

public class DirectoryRemovalException extends MyDriveException {
	private static final long serialVersionUID = 1L;

    private String _path;
    public DirectoryRemovalException(String path) {
        _path=path;
    }
    
    public String getpath() {
        return _path;
    }

    @Override
    public String getMessage() {
        return  "Could not remove directory from given path: "+ getpath();
    }
}
