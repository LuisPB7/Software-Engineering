package pt.tecnico.mydrive.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.MyDriveFile;
import pt.tecnico.mydrive.domain.Nobody;
import pt.tecnico.mydrive.domain.Root;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.DeletePermissionDeniedException;
import pt.tecnico.mydrive.exception.ExecutePermissionDeniedException;
import pt.tecnico.mydrive.exception.MyDriveException;

public class ExecuteFileService extends MyDriveService{

	private long _token;
	private String _path;
	private String[] _args;
	private Object result;
	private String path2;

	public ExecuteFileService(long token, String path, String[] arguments){
		_token = token;
		_path = path;
		_args = arguments;
	}

	@Override
	public void dispatch() throws MyDriveException {
		MyDrive mydrive = MyDrive.getInstance();
		User u = mydrive.UserByToken(_token);

		_path=MyDrive.getInstance().processPath(_path, _token);		
		MyDriveFile file = mydrive.getFileByPath(_path);

		if(file.get_filePermission().contains("x") || (u instanceof Root) ||  u.get_userName().equals(file.getCreator().get_userName())){
			try {
				result = mydrive.executeFile(u, _path, _args, _token, new ArrayList<String>());
			} catch (ClassNotFoundException | SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e){
				e.printStackTrace();}
		}

		else
			throw new ExecutePermissionDeniedException(u.get_userName(), file.get_fileName());


	}
	
	public String getFileByAssociation(String extention){
		return path2;
	}

	public final Object result(){
		return result;
	}

}