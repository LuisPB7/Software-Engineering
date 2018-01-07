package pt.tecnico.mydrive.exception;

public class DomainAcessDeniedException extends MyDriveException {

	
	public DomainAcessDeniedException(){
		
	}


	public String getMessage(){
		return " Domain access denied.";
	}

}
