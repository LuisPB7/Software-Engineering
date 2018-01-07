package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.exception.MyDriveException;

public class ChangeDirectoryService extends MyDriveService {
	private long token;
	private String path;
	private String previousPath;

	public ChangeDirectoryService(long token, String path){
		this.token = token;
		this.path = path;
	}

	public String currentDir(){
		return previousPath;
	}
	
	public String result(){ 
		return path;
	}

	@Override
	protected void dispatch() throws MyDriveException {
		MyDrive mydrive = MyDrive.getInstance();
		Session session = mydrive.getSessionByToken(token);
		path=MyDrive.getInstance().processPath(path, token);
		session.setCurrentDir(mydrive.getDirectory(path));
	}

}
