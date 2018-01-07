package pt.tecnico.mydrive.presentation;

import java.security.GeneralSecurityException;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.mydrive.*;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.exception.*;
import pt.tecnico.mydrive.service.MyDriveService;
import pt.tecnico.mydrive.service.WriteFileService;
import pt.tecnico.mydrive.domain.*;

public class Write extends MdCommand {
	
	public Write(Shell sh) { super(sh, "update", "Write");}
	
	
	public void execute(String[] args){
		
		WriteFileService service = null;
		
		if(args.length == 2){
			long s = ((MdShell)shell()).getSession();
			service = new WriteFileService(((MdShell)shell()).getSession(), args[0], args[1]);
		}
				
		else
			throw new RuntimeException("USAGE: " + name() + "name");
		
		service.execute();
		

			
	}
}
