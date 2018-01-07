package pt.tecnico.mydrive.exception;

public class InvalidFileFormatException extends MyDriveException {
	private static final long serialVersionUID = 1L;
	private String _type;
	
	public InvalidFileFormatException(String type){
		_type=type;
	}
	
	public String getType(){
		return _type;
	}
	
	
	public String getMessage(){
		return "Invalid file format: "+ _type;
	}
}