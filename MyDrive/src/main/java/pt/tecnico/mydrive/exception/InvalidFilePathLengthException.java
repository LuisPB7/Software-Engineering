package pt.tecnico.mydrive.exception;

public class InvalidFilePathLengthException extends MyDriveException {
	private static final long serialVersionUID = 1L;
	private int _length;
	
	public InvalidFilePathLengthException(int length){
		_length=length;
	}
	
	public int getLength(){
		return _length;
	}
	
	
	public String getMessage(){
		return "Path to the file musnt have more than 1024 characters, current File Length: "+ _length;
	}
}