package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.EnvVar;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.exception.MyDriveException;
	
public class GetEnvVarService extends MyDriveService {
	private long _token;
	private String _envVarList;
	private String _name;
	private EnvVar _var;

	public GetEnvVarService(long token, String name) {
		_token=token;
		_name=name;
	}
	
	public String getName(){
		return _name;
	}
	
	@Override
	public void dispatch() throws MyDriveException {
		// TODO Auto-generated method stub
		MyDrive md = MyDrive.getInstance();

		Session s = md.getSessionByToken(_token);
		_var = md.getEnvironmentalVariableFromSession(s, getName());
		
	}
	
	public final String result(){
		return _var.getValue();
	}

}
