package pt.tecnico.mydrive.exception;

public class DuplicatedDirectoryException extends MyDriveException {

	private static final long serialVersionUID = 1L;
	
    private String _existingName;
    private String _currentPath;

    public DuplicatedDirectoryException(String name, String path) {
        _existingName = name;
        _currentPath = path;
    }

    public String getExistingName() {
        return _existingName;

    }
    
    public String getCurrentDirectory() {
        return _currentPath;

    }

    @Override
    public String getMessage() {
        return "Cannot create " + _existingName 
        		+ ". a directory with the same name already exists in the current location, "
        		+_currentPath+".";
    }
	
}
