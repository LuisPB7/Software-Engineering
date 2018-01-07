package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.EnvVar;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.exception.MyDriveException;
	
public class AddEnvironmentalVariableService extends MyDriveService {
	private long _token;
	private String _name;
	private String _value;
	private EnvVar _var;
	private String _result="";
	public AddEnvironmentalVariableService(long token,String name,String value) {
		_token=token;
		_name=name;
		_value=value;
	}
	
	public AddEnvironmentalVariableService(long token) {
		_token=token;
		_name=null;
		_value=null;
	}
	
	public AddEnvironmentalVariableService(long token, String name) {
		_token=token;
		_name=name;
		_value=null;
	}
	
	public EnvVar getVar(){
		return _var;
	}
	
	public String getResult(){
		return _result;
	}
	@Override
	public void dispatch() throws MyDriveException {
		// TODO Auto-generated method stub
		MyDrive mydrive = MyDrive.getInstance();
		Session s = mydrive.getSessionByToken(_token);

		if(_name!=null&&_value!=null){
			_var=mydrive.addEnvironmentalVariableToSession(s, _name, _value);
			//_result = _var.list();
			_result+=s.listEnvVar();
		}else{
		
			if(_name!=null){
				_var=mydrive.getEnvironmentalVariableFromSession(s, _name);
				_result=_var.getValue();
			}else{
				_result=s.listEnvVar();
			}
		}
	}
}

