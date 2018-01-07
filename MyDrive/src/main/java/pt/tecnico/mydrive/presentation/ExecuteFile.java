package pt.tecnico.mydrive.presentation;
import java.util.ArrayList;
import java.util.Arrays;

import pt.tecnico.mydrive.service.ExecuteFileService;


public class ExecuteFile extends MdCommand {

 
 
 public ExecuteFile(Shell sh) { super(sh, "do", "Execute the file with the path indentified with the arguments provided"
   + "USAGE do path[args]"); }

 
 @Override
 public void execute(String[] args) {
	  String path = args[0];
	  long token = ((MdShell)shell()).getSession();
	  if (args.length < 1){
	       throw new RuntimeException("USAGE: "+name()+" name");}
	  else if(args.length == 1){
	   //String[] arguments = {""};
	   new ExecuteFileService(token,path, null).execute();
	  }
	  else{
	   String[] arguments = Arrays.copyOfRange(args, 1, args.length);
	   new ExecuteFileService(token,path, arguments).execute();
	  }
  
    }


}