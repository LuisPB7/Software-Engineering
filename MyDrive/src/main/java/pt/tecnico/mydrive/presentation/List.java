package pt.tecnico.mydrive.presentation;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.service.ChangeDirectoryService;
import pt.tecnico.mydrive.service.ListDirectoryService;

public class List extends MdCommand {

	public List(Shell sh) {
		super(sh, "ls", "list directory in a given path");
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public void execute(String[] args) {
		if (args.length < 0)
		    throw new RuntimeException("USAGE: "+name()+"  [<username>]"); 
		if (args.length > 0){
			ListDirectoryService listService= new ListDirectoryService(((MdShell)shell()).getSession(), args[0]);
			listService.execute();
			System.out.println(listService.result());
		}
		else{
			ListDirectoryService service= new ListDirectoryService(((MdShell)shell()).getSession());
			service.execute();
			System.out.println(service.result());
		}
	}

}