package pt.tecnico.mydrive.service;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.mydrive.*;
import pt.tecnico.mydrive.exception.*;
import pt.tecnico.mydrive.domain.*;

public class CreateFileService extends MyDriveService {
	
		private long _token;
		private String _filename;
		private String _type;
		private String _content;
	
	public CreateFileService(long token, String filename, String type, String content) {
		_token = token;
		_filename = filename;
        _type = type;
        _content = content;
    }
	
	public long getToken(){
		return _token;
	}
	
	public final void dispatch(){
		String permission = "rwxdr";
		
		MyDrive mydrive = MyDrive.getInstance();
		Session s = mydrive.getSessionByToken(getToken());
		User u = mydrive.UserByToken(getToken());
		Directory dir = s.getCurrentDir();
		String path = s.getCurrentDir().get_path();
		
		/*
		if(s==null){
			throw new SessionDoesNotExistException(getToken());
		}
		*/
		
		mydrive.createFile(_filename, path + "/" + _filename, _content, u, dir, _type);		
			    
	}

}