package pt.tecnico.mydrive.presentation;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.mydrive.*;
import pt.tecnico.mydrive.exception.*;
import pt.tecnico.mydrive.service.MyDriveService;

import pt.tecnico.mydrive.service.AddEnvironmentalVariableService;
import pt.tecnico.mydrive.service.GetEnvVarListService;
import pt.tecnico.mydrive.service.GetEnvVarService;
import pt.tecnico.mydrive.domain.*;

public class AddEnvironmmentVariable extends MdCommand {
	public AddEnvironmmentVariable(Shell sh) { super(sh, "env", "Environment");}
	
	
	public void execute(String[] args){
		AddEnvironmentalVariableService service = null;

		
		if (args.length < 0||args.length>3)
			throw new RuntimeException("USAGE: " + name() + " [name [value]]");
		
		else if(args.length == 0){
			service = new AddEnvironmentalVariableService(((MdShell)shell()).getSession());
		}
		
		else if(args.length == 1){
			service = new AddEnvironmentalVariableService(((MdShell)shell()).getSession(), args[0]);
		}
		
		else if(args.length == 2){
			service = new AddEnvironmentalVariableService(((MdShell)shell()).getSession(),args[0], args[1]);
		}
		
		service.execute();
		System.out.println(service.getResult());

			
	}
}
