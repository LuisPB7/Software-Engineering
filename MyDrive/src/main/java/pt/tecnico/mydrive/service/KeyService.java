package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.exception.MyDriveException;
import pt.tecnico.mydrive.presentation.MdShell;

public class KeyService extends MyDriveService {
	
	private long _token;
	private long _newToken;
	private String _username;
	private String _result;
	
	public KeyService(long token, String username){
		_username = username;
		_token = token;
	}
	

	@Override
	public void dispatch() throws MyDriveException {
		// TODO Auto-generated method stub
		MyDrive md = MyDrive.getInstance();
		if(_username == null){
			_result = "Token: " + _token + " from " + md.getSessionByToken(_token).getCurrentUser().get_userName();
		}
		else{
			for(Session s: md.getSessionsSet()){
				if(s.getCurrentUser().get_userName().equals(_username)){
					_newToken = s.get_token();
					_result = "New active token: " + s.get_token();
					return;
				}
			}
		}
		
	}
	
	public String result(){
		return _result;
	}
	
	public long getNewToken(){
		return _newToken;
	}

}
