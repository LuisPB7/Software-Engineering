package pt.tecnico.mydrive.exception;

public class FileNotFoundException extends MyDriveException {
	private static final long serialVersionUID = 1L;

    private String _path;
    
    public FileNotFoundException(){
    	
    }
    
    public FileNotFoundException(String path) {
        _path=path;
    }

    public String getCurrentPath() {
        return _path;
    }

    @Override
    public String getMessage() {
        return "No file could be reach from path: "
        		+_path;
    }

}
