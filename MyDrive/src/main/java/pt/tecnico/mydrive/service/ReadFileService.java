package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.*;
import pt.tecnico.mydrive.exception.*;
import pt.tecnico.mydrive.domain.*;

public class ReadFileService extends MyDriveService {
	
	String readResult;
	private long _token;
	private String _filename;
	
	public ReadFileService(long token, String filename){
		_token = token;
		_filename = filename;
	}
	
	public long getToken(){
		return _token;
	}
	
	public String getFileName(){
		return _filename;
	}
	
	public String result(){
		return readResult;
	}
	
	public final void dispatch(){
		MyDrive mydrive = MyDrive.getInstance();
		Session session = mydrive.getSessionByToken(getToken());
		MyDriveFile file =mydrive.getFileByName(getFileName());
		//System.out.println(file+"-----------------------------------------");
		readResult = mydrive.readFileContent(file.get_path(),session.getCurrentUser());	

	}

}