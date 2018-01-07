package pt.tecnico.mydrive.presentation;

import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.service.KeyService;

public class Key extends MdCommand {

	public Key(Shell sh) {
		super(sh, "token", "exchange between users");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(String[] args) {
		
		
		if (args.length < 0)
		    throw new RuntimeException("USAGE: "+name()+"  [<username>]"); 
		if (args.length > 0){
			System.out.println(args[0]);
			KeyService service = new KeyService(((MdShell)shell()).getSession(), args[0]);
			service.execute();
			((MdShell)shell()).changeSession(service.getNewToken());
			System.out.println(service.result());
		}
		else{
			KeyService service = new KeyService(((MdShell)shell()).getSession(), null);
			service.execute();
			System.out.println(service.result());
		}

	}
}
