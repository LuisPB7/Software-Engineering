package pt.tecnico.mydrive.exception;

public class InvalidAppFormatException extends InvalidContentFormatException {
	
	public InvalidAppFormatException(String content){
		 super(content);
	}
	
	public String getMessage(){
		return "App must have up to 2 occurences of character  \'.\' and no empty spaces in the content. \n No such pattern was found on content:" + super.getType();
	}
}