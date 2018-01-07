package pt.tecnico.mydrive.service;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.FileNotFoundException;
import pt.tecnico.mydrive.exception.InvalidPasswordException;
import pt.tecnico.mydrive.exception.UserDoesNotExistException;
import java.util.Random;


public class ResolveEnvLinkService extends MyDriveService{
	private String result;

	public ResolveEnvLinkService(String path) {
		this.result = path;
	}

	public void dispatch() {
		// TODO quarta entrega muahahahah
	}
	
	public String getResult(){
		return result;
	}
}