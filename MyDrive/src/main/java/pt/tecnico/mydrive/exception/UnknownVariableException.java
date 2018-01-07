package pt.tecnico.mydrive.exception;

import pt.tecnico.mydrive.domain.Session;

public class UnknownVariableException extends MyDriveException {

	private String _varName;
	private Session _session=null;
	
	public UnknownVariableException(String name) {
		// TODO Auto-generated constructor stub
		_varName = name;
	}
	public UnknownVariableException(Session s, String name){
		this(name);
		_session=s;
		
	}

	public String getMessage(){
		String message = "Unnable to find variable: "+_varName;
		_session.getVariableListSet().size();
		if(_session!=null){
			message +=" on Session: "+_session.get_token();
		}
		return message;
	}
	
}
