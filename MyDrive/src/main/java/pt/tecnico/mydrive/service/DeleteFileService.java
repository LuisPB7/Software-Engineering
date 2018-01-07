package pt.tecnico.mydrive.service;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.mydrive.*;
import pt.tecnico.mydrive.exception.*;
import pt.tecnico.mydrive.domain.*;

public class DeleteFileService extends MyDriveService {
	
	
	private long _token;
	private String _filename;

	
	public DeleteFileService(long token, String filename) {
        _token = token;
        _filename = filename;
    }
	
	public long getToken(){
		return _token;
	}
	
	public String getFilename(){
		return _filename;
	}
	
	
	public final void dispatch() throws UserDoesNotExistException, FileNotFoundException{
		
		MyDrive mydrive = getMyDrive();
		
		Session s = mydrive.getSessionByToken(getToken());
		
		User u  = s.getCurrentUser();
		
		Directory d = s.getCurrentDir();
		MyDriveFile file = d.getDirectoryFile(getFilename());
	    
		
		if((u instanceof Nobody))
    		throw new DeletePermissionDeniedException(u.get_userName(), file.get_fileName());
    	
    	else if(file.get_filePermission().contains("d") || (u instanceof Root) ||  u.get_userName().equals(file.getCreator().get_userName())){
    		if(file instanceof Directory){
				mydrive.directoryDelete((Directory)file);
			}
			else{
				mydrive.fileDelete(file.get_path());
			}
    	}
    	
    	else
    		throw new DeletePermissionDeniedException(u.get_userName(), file.get_fileName());
		
		
			//System.out.println("PATH DA DIRECTORIA: " + file.get_path() + "\n" + file.get_fileName());
		//}
	   // 
		//	else{
	    //	throw new DeletePermissionDeniedException(u.get_userName(), file.toString());
	   // }
		
	}
}