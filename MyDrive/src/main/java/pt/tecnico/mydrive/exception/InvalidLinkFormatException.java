package pt.tecnico.mydrive.exception;

public class InvalidLinkFormatException extends InvalidContentFormatException {
	
	public InvalidLinkFormatException(String content){
		 super(content);
	}
	
	public String getMessage(){
		return "Link must have a character  \'/\'. \n No such charater was found on content:" + super.getType();
	}
}