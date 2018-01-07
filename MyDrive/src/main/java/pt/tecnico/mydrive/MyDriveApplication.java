package pt.tecnico.mydrive;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.io.PrintStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.ist.fenixframework.*;
import java.util.*;
import pt.tecnico.mydrive.*;
import pt.tecnico.mydrive.domain.*;
import pt.tecnico.mydrive.exception.DirectoryNotFoundException;
import pt.tecnico.mydrive.exception.InvalidFileNameFormatException;
import pt.tecnico.mydrive.presentation.MdShell;
import pt.tecnico.mydrive.service.LoginService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.File;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
public class MyDriveApplication {
	static final Logger log = LogManager.getRootLogger();

	public static void main(String[] args) throws Exception {
		System.out.println("*** Welcome to the MyDrive application! ***");
		try {
			
			if(args.length==0)
				setup();
			else
				xmlScan(new File(args[0]));
			
			MdShell.main(null);
			//seeMD();
			xmlPrint();
			//    print();
		} 
		
		finally {
			FenixFramework.shutdown();
		}
	}
	
	@Atomic
	public static void init(){
		log.trace("Init: "+FenixFramework.getDomainRoot());
		// TO DO: ADD FileDelete(String path) to MyDrive
		// to implement cleanup method on MyDrive
		//MyDrive.getInstance().cleanup();
	//	MyDrive.getInstance();
	}
	
	@Atomic
	public static void setup(){
		log.trace("Setup: " + FenixFramework.getDomainRoot());
		MyDrive md = MyDrive.getInstance();
		//if (md.getUserListSet().size()>1) return;
		
//		CRIAÇAO DOS USERS		
		Root root = User.getInstanceRootUser(md);
		Nobody nobody = User.getInstanceNobodyUser(md);
		User ritto = md.createUser("ritto", "rittoPW123");
		User luis = md.createUser("luis", "luisPW123");
		User miguens = md.createUser("miguens", "miguensPW123");
		User diogo = md.createUser("diogo", "diogoPW123");
		User pestana = md.createUser("pestana", "pestanaPW123");
		User jola = md.createUser("jola", "jolaPW123");
		
		
//		CRIAÇAO DE DIRETORIAS		
		
		md.createDirectory("DIR", "/home/nobody/DIR", nobody);
		md.createDirectory("DIR", "/home/root/DIR", root);
		md.createDirectory("DIRritto", "/home/ritto/DIRritto", ritto);
		md.createDirectory("DIRluis", "/home/luis/DIRluis", luis);
		md.createDirectory("DIRjola", "/home/jola/DIRjola", jola);
		md.createDirectory("DIRmiguens", "/home/miguens/DIRmiguens", miguens);
		md.createDirectory("DIRpestana", "/home/pestana/DIRpestana", pestana);
		md.createDirectory("DIRdiogo", "/home/diogo/DIRdiogo", diogo);

		Directory dir1 = md.createDirectory("DIR1", "/home/root/DIR1", root);
		Directory dirritto1 = md.createDirectory("DIRritto1", "/home/ritto/DIRritto1", ritto);
		Directory dirluis1 = md.createDirectory("DIRluis1", "/home/luis/DIRluis1", luis);
		Directory dirjola1 = md.createDirectory("DIRjola1", "/home/jola/DIRjola1", jola);
		Directory dirmiguens1 = md.createDirectory("DIRmiguens1", "/home/miguens/DIRmiguens1", miguens);
		Directory dirpestana1 = md.createDirectory("DIRpestana1", "/home/pestana/DIRpestana1", pestana);
		Directory dirdiogo1 = md.createDirectory("DIRdiogo1", "/home/diogo/DIRdiogo1", diogo);
		
		md.createDirectory("DIR2", "/home/root/DIR/DIR2", root);
		md.createDirectory("DIRritto2", "/home/ritto/DIRritto/DIRritto2", ritto);
		md.createDirectory("DIRluis2", "/home/luis/DIRluis/DIRluis2", luis);
		md.createDirectory("DIRjola2", "/home/jola/DIRjola/DIRjola2", jola);
		md.createDirectory("DIRmiguens2", "/home/miguens/DIRmiguens/DIRmiguens2", miguens);
		md.createDirectory("DIRpestana2", "/home/pestana/DIRpestana/DIRpestana2", pestana);
		md.createDirectory("DIRdiogo2", "/home/diogo/DIRdiogo/DIRdiogo2", diogo);
		
		
//		CRIAÇAO DE FICHEIROS
		md.createFile("FILE", "/home/root/FILE", "file do root", root, md.getDirectory(root.get_homeDir()), "Plain");
		md.createFile("FILEritto", "/home/ritto/FILEritto", "/home/ritto/BASS Discussao", ritto, md.getDirectory(ritto.get_homeDir()), "Plain");
		md.createFile("FILEmiguens", "/home/miguens/FILEmiguens", "file do miguens", miguens, md.getDirectory(miguens.get_homeDir()), "Plain");
		md.createFile("FILEluis", "/home/luis/FILEluis", "file do luis", luis, md.getDirectory(luis.get_homeDir()), "Plain");
		md.createFile("FILEpestana", "/home/pestana/FILEpestana", "file do pestana", pestana, md.getDirectory(pestana.get_homeDir()), "Plain");
		md.createFile("FILEjola", "/home/jola/FILEjola", "file do jola", jola, md.getDirectory(jola.get_homeDir()), "Plain");
		md.createFile("FILEdiogo", "/home/diogo/FILEdiogo", "file do diogo", diogo, md.getDirectory(diogo.get_homeDir()), "Plain");

		md.createFile("FILE1", "/home/root/DIR/FILE1", "file1 do root", root, dir1, "Plain");
		md.createFile("FILEritto1", "/home/ritto/DIRritto1/FILEritto1", "file1 do ritto", ritto, dirritto1, "Plain");
		md.createFile("FILEmiguens1", "/home/miguens/DIRmiguens1/FILEmiguens1", "file1 do miguens", miguens, dirmiguens1, "Plain");
		md.createFile("FILEluis1", "/home/luis/DIRluis1/FILEluis1", "file1 do luis", luis, dirluis1, "Plain");
		md.createFile("FILEpestana1", "/home/pestana/DIRpestana1/FILEpestana1", "file1 do pestana", pestana, dirpestana1, "Plain");
		md.createFile("FILEjola1", "/home/jola/DIRjola1/FILEjola1", "file1 do jola", jola, dirjola1, "Plain");
		md.createFile("FILEdiogo1", "/home/diogo/DIRdiogo1/FILEdiogo1", "file1 do diogo", diogo, dirdiogo1, "Plain");

		md.createFile("FILE2", "/home/root/FILE2", "file2 do root", root, md.getDirectory(root.get_homeDir()), "Plain");
		md.createFile("FILEmiguens2", "/home/miguens/FILEmiguens2", "file2 do miguens", miguens, md.getDirectory(miguens.get_homeDir()), "Plain");
		md.createFile("FILEluis2", "/home/luis/FILEluis2", "file2 do luis", luis, md.getDirectory(luis.get_homeDir()), "Plain");
		md.createFile("FILEpestana2", "/home/pestana/FILEpestana2", "file do pestana", pestana, md.getDirectory(pestana.get_homeDir()), "Plain");
		md.createFile("FILEjola2", "/home/jola/FILEjola2", "file2 do jola", jola, md.getDirectory(jola.get_homeDir()), "Plain");
		md.createFile("FILEdiogo2", "/home/diogo/FILEdiogo2", "file2 do diogo", diogo, md.getDirectory(diogo.get_homeDir()), "Plain");
		
		md.createFile("FILEritto2", "/home/ritto/FILEritto2", "/home/ritto/BASS Henrique Fernandes", ritto, md.getDirectory(ritto.get_homeDir()), "Plain");
		md.createFile("BASS", "/home/ritto/BASS", "presentation.Hello.greet", ritto, md.getDirectory(ritto.get_homeDir()), "App");
		md.createFile("GUITAR", "/home/ritto/GUITAR", "presentation.Hello", ritto, md.getDirectory(ritto.get_homeDir()), "App");
		md.createFile("MIC", "/home/ritto/MIC", "/home/ritto/FILEritto2", ritto, md.getDirectory(ritto.get_homeDir()), "Link");

//		System.out.println("CENAS SHOULD BE HERE");
//		System.out.println(md.getUserListSet());

	}
	
		
	@Atomic
    public static void xmlPrint() throws IOException {
		log.trace("xmlPrint: " + FenixFramework.getDomainRoot());
		Document MyDriveDocument = MyDrive.getInstance().XMLExport();
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		xmlOutput.output(MyDriveDocument, new FileWriter("mydrive.xml"));
	}
	
	@Atomic
    public static void xmlScan(File file) {
        log.trace("xmlScan: " + FenixFramework.getDomainRoot());
		MyDrive mydrive = MyDrive.getInstance();
		SAXBuilder builder = new SAXBuilder();
		try {
		    Document document = (Document)builder.build(file);
		    mydrive.XMLImport(document);
		} catch (JDOMException | IOException e) {
		    e.printStackTrace();
		}
    }
	
	@Atomic
	public static void seeMD(){
		MyDrive mydrive = MyDrive.getInstance();
		System.out.println("-------------- DIRECTORIES -------------");
		for(MyDriveFile f: mydrive.getFileListSet()){
			if(f instanceof Directory){
				System.out.println(f.get_fileName());
			}
		}
		System.out.println("--------------- FILES ------------------");
		for(MyDriveFile f: mydrive.getFileListSet()){
			if(!(f instanceof Directory)){
				System.out.println(f.get_fileName());
			}
		}
		System.out.println("--------------- USERS --------------");
		for(User u: mydrive.getUserListSet()){
			System.out.println(u.get_userName());
		}
		System.out.println("--------------- LOGIN --------------");
		mydrive.createUser("pFnX");
		mydrive.createUser("miguens");	
		for(User u: mydrive.getUserListSet()){
			System.out.println(u.get_userName());
		}
		System.out.println(mydrive.createSession("pFnX", "ehehehe"));
	}
		
}