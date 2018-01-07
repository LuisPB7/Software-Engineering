package pt.tecnico.mydrive.exception;

public class InvalidContentFormatException extends MyDriveException {
	private static final long serialVersionUID = 1L;
	private String _content;
	
	public InvalidContentFormatException(String content){
		_content=content;
	}
	
	public String getType(){
		return _content;
	}
	
	
	public String getMessage(){
		return "Invalid content format: "+ _content;
	}
}