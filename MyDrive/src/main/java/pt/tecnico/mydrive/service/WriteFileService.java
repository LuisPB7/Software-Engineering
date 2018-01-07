package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.MyDriveFile;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.FileNotFoundException;
import pt.tecnico.mydrive.exception.UserDoesNotExistException;
import pt.tecnico.mydrive.exception.WritePermissionDeniedException;

public class WriteFileService extends MyDriveService {
	
	
	private long _token;
	private String _filename;
	private String _content;

	
	public WriteFileService(long token, String filename, String content) {
        _token = token;
        _filename = filename;
		_content = content;
    }
	
	public long getToken(){
		return _token;
	}
	
	public String getFilename(){
		return _filename;
	}
	

	
	@SuppressWarnings("null")
	public final void dispatch() throws UserDoesNotExistException, FileNotFoundException{

		MyDriveFile file;
		MyDrive mydrive = MyDrive.getInstance();

		
//		User root = mydrive.getUser("root");
//		String perm = mydrive.getUser("root").get_uMask();
//		mydrive.createFile("FILE", "/home/root/FILE", "file do root", root, mydrive.getDirectory(root.get_homeDir()), "Plain");
//		System.out.println("FILE A APARECER " + mydrive.getFileByName("FILE").toString());
//		
		
		
		Session s = mydrive.getSessionByToken(getToken());
		User u  = s.getCurrentUser();
		
		if(getFilename().contains("/"))
			file = mydrive.getFileByPath(getFilename());
		
		else{
			Directory d = s.getCurrentDir();
			file = d.getDirectoryFile(getFilename());
		}
		
	   file.write(_content, u);
	   
		//System.out.println("FILE novo " + mydrive.getFileByName("FILE").toString());

	}
}