package pt.tecnico.mydrive.presentation;

import pt.tecnico.mydrive.service.ChangeDirectoryService;

public class ChangeWorkingDirectory extends MdCommand {
/*
	public ChangeWorkingDirectory(Shell sh, String n) {
		super(sh, n);
		// TODO Auto-generated constructor stub
	}
*/
	public ChangeWorkingDirectory(Shell sh) {
		super(sh, "cwd", "Change Workind Directory");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(String[] args) {
		// TODO Auto-generated method stub
    	if (args.length != 1)
    		throw new RuntimeException("USAGE: "+name()+" path");
    	
    	long token = ((MdShell) shell()).getSession();
    	
    	ChangeDirectoryService changeDir = new ChangeDirectoryService(token, args[0]);
    	
    	changeDir.execute();
//    	String previous = changeDir.currentDir();
//    	System.out.println("\n\nPrevious Directory: "+previous+"\n\nCurrent Directory: " +changeDir.result());
	}
	
}
