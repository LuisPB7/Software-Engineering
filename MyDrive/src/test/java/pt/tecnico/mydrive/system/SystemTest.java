package pt.tecnico.mydrive.system;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.mydrive.service.AbstractServiceTest ;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.presentation.*;

public class SystemTest extends AbstractServiceTest {

    private MdShell sh;

    protected void populate() {
    	
        sh = new MdShell();
        MyDrive m = MyDrive.getInstance();
		m.cleanup();
		User ritto = m.createUser("ritto", "rittoPW123");
		User miguens = m.createUser("miguens", "miguensPW123");
		m.createDirectory("DIRritto", "/home/ritto/DIRritto", ritto);
		m.createFile("FILEritto", "/home/ritto/DIRritto/FILEritto", "presentation.Hello.greet", ritto, m.getDirectory(ritto.get_homeDir()), "App");
		
		
    
    }

    @Test
    public void success() {
        new Login(sh).execute(new String[] {"ritto", "rittoPW123"} );
        new Login(sh).execute(new String[] { "root", "***" } );
        new Login(sh).execute(new String[] { "miguens", "miguensPW123" } );
        new Key(sh).execute(new String[] { "ritto" } );
        new List(sh).execute(new String[] {} );
        new AddEnvironmmentVariable(sh).execute(new String[] {"VarUm", "valorUm"});
        new AddEnvironmmentVariable(sh).execute(new String[] {"VarUm"});
        new AddEnvironmmentVariable(sh).execute(new String[] {});
        new ChangeWorkingDirectory(sh).execute(new String[] {"/home/ritto/DIRritto"} );
        new ExecuteFile(sh).execute(new String[] {"/home/ritto/DIRritto/FILEritto"});
        new Write(sh).execute(new String[] {"/home/ritto/DIRritto/FILEritto", "presentation.Hello.bye"});
        new ExecuteFile(sh).execute(new String[] {"/home/ritto/DIRritto/FILEritto", "Paulo", "Ritto"});
        
        
    }
}