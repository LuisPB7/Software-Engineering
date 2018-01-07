package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.*;
import pt.tecnico.mydrive.exception.*;
import pt.tecnico.mydrive.domain.*;

public class ListDirectoryService extends MyDriveService {
	
	private long token;
	private String path;
	private String resultDirectory;
	
	public ListDirectoryService(long token){
		this.token = token;
		this.path = null;
	}
	
	public ListDirectoryService(long token, String path){
		this.token = token;
		this.path = path;
	}
	
	public final void dispatch(){
		MyDrive mydrive = MyDrive.getInstance();
		Session session = mydrive.getSessionByToken(token);
		if(path == null){
			resultDirectory = mydrive.ListDirectory(session.getCurrentDir().get_path());
		}
		else{
			resultDirectory = mydrive.ListDirectory(path);
		}
	}
	
	public final String result(){
		return resultDirectory;
	}
}