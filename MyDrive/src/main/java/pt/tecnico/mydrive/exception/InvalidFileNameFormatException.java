package pt.tecnico.mydrive.exception;

public class InvalidFileNameFormatException extends MyDriveException {
	private static final long serialVersionUID = 1L;
	private String _fileName;
	
	public InvalidFileNameFormatException(String name){
		_fileName=name;
	}
	
	public String getUserName(){
		return _fileName;
	}
	
	
	public String getMessage(){
		return "Invalid file name format: "
				+ _fileName
				+ ". file name cannot contain the special characters.";
	}
}
