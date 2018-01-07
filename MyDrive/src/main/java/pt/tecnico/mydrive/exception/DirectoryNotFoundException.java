package pt.tecnico.mydrive.exception;

public class DirectoryNotFoundException extends MyDriveException {
	private static final long serialVersionUID = 1L;

    private String _directoryName;
    private String _currentPath;
    public DirectoryNotFoundException(String name, String path) {
        _directoryName = name;
        _currentPath=path;
    }

    public String getDirectoryName() {
        return _directoryName;
    }
    
    public String getCurrentPath() {
        return _currentPath;
    }

    @Override
    public String getMessage() {
        return  ""+_directoryName + ".No such directory can be found from path: "
        		+_currentPath;
    }
}
