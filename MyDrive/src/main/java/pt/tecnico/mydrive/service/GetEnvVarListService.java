package pt.tecnico.mydrive.service;

import java.util.Set;

import pt.tecnico.mydrive.domain.EnvVar;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.exception.MyDriveException;
	
public class GetEnvVarListService extends MyDriveService {
	private long _token;
	private Set<EnvVar> _varList;

	public GetEnvVarListService(long token) {
		_token=token;
	}
	
	
	@Override
	public void dispatch() throws MyDriveException {

		MyDrive md = MyDrive.getInstance();
		Session s = md.getSessionByToken(_token);
		_varList = s.getVariableListSet();
		
	}
	
	public final String result(){
		String finalList=null;
		for(EnvVar v : _varList)
			finalList+= v.getName() + " = " + v.getValue();
		return finalList;
	}

}
