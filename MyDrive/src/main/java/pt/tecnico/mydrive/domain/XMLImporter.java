package pt.tecnico.mydrive.domain;

import org.jdom2.Document;
import org.jdom2.Element;

import pt.tecnico.mydrive.exception.*;

public class XMLImporter {
	
	public XMLImporter(){ }
	
	public void processPath(String path){
		//System.out.println("<" + path + ">");
		MyDrive mydrive = MyDrive.getInstance();
		String[] dirNames = path.split("/");
		String currentPath = "";
		for(int i = 0; i < dirNames.length - 1; i++){
			String dirName = dirNames[i];
			if(dirName.equals("")){ continue; }
			currentPath += "/" + dirName;
			if(!(mydrive.fileExists(dirName))){
				mydrive.createDirectory(dirName, currentPath, mydrive.getUser("root"), getParentDirectory(currentPath));
			}
		}
	}
	
	public Directory getParentDirectory(String path){
		MyDrive mydrive = MyDrive.getInstance();
		String[] dirNames = path.split("/");
		String currentPath = "";
		if(dirNames.length > 2){
			for(int i = 0; i < dirNames.length - 1; i++){
				if(dirNames[i].equals("")){ continue; }
				//System.out.println(dirNames[i]);
				currentPath = currentPath + "/" + dirNames[i];
			}
			//System.out.println("RETURNING PARENT " + mydrive.getDirectory(dirNames[dirNames.length-2], currentPath));
			return mydrive.getDirectory(dirNames[dirNames.length-2], currentPath);
		}
		//System.out.println("RETURNING PARENT: " + mydrive.getDirectory(dirNames[dirNames.length-1], path));
		return mydrive.getDirectory(dirNames[dirNames.length-1], path);
	}
	
	public void ImportUser(Element userElement){
		MyDrive mydrive = MyDrive.getInstance();
		Element username = userElement.getChild("username");
		Element password = userElement.getChild("password");
		Element name = userElement.getChild("name");
		Element homeDir = userElement.getChild("homedir");
		Element mask = userElement.getChild("mask");
		if(username.getText().equals("root")){
			return;
		}
		if(mydrive.userExists(username.getText())){
			throw new DuplicatedUserException(username.getText());
		}
		User u = mydrive.createUser(username.getText());
		u.set_name(name.getText());
		u.set_password(password.getText());
		u.set_homeDir(homeDir.getText());
		u.set_uMask(mask.getText());
		u.set_systemId(Integer.parseInt(userElement.getAttributeValue("id")));
	}
	
	public void ImportDirectory(Element directoryElement){
		MyDrive mydrive = MyDrive.getInstance();
		if(!mydrive.fileExists(directoryElement.getChild("name").getText())){
			Element name = directoryElement.getChild("name");
			Element owner = directoryElement.getChild("owner");
			//Element permission = directoryElement.getChild("permissions");
			Element path = directoryElement.getChild("path");
			//Element mask = directoryElement.getChild("mask");
			User userOwner = mydrive.getUser(owner.getText());
			processPath(path.getText());
			Directory parent = getParentDirectory(path.getText());
			mydrive.createDirectory(name.getText(), path.getText(), userOwner, parent).set_fileId(Integer.parseInt(directoryElement.getAttributeValue("id")));
		}
	}
	
	public void ImportLink(Element linkFileElement){
		MyDrive mydrive = MyDrive.getInstance();
		if(!mydrive.fileExists(linkFileElement.getChild("name").getText())){
			Element name = linkFileElement.getChild("name");
			Element owner = linkFileElement.getChild("owner");
			Element permission = linkFileElement.getChild("permissions");
			Element path = linkFileElement.getChild("path");
			//Element mask = linkFileElement.getChild("mask");
			Element content = linkFileElement.getChild("data");
			User userOwner = mydrive.getUser(owner.getText());
			processPath(path.getText());
			Directory parent = getParentDirectory(path.getText());
			mydrive.createLinkFile(name.getText(), path.getText(), permission.getText(), content.getText(), userOwner, parent).set_fileId(Integer.parseInt(linkFileElement.getAttributeValue("id")));
		}
	}
	
	public void ImportApp(Element appFileElement){
		MyDrive mydrive = MyDrive.getInstance();
		if(!mydrive.fileExists(appFileElement.getChild("name").getText())){
			Element name = appFileElement.getChild("name");
			Element owner = appFileElement.getChild("owner");
			Element permission = appFileElement.getChild("permissions");
			Element path = appFileElement.getChild("path");
			//Element mask = appFileElement.getChild("mask");
			Element content = appFileElement.getChild("data");
			User userOwner = mydrive.getUser(owner.getText());
			processPath(path.getText());
			Directory parent = getParentDirectory(path.getText());
			mydrive.createAppFile(name.getText(), path.getText(), permission.getText(), content.getText(), userOwner, parent).set_fileId(Integer.parseInt(appFileElement.getAttributeValue("id")));
		}
	}
	
	public void ImportPlain(Element plainFileElement){
		MyDrive mydrive = MyDrive.getInstance();
		if(!mydrive.fileExists(plainFileElement.getChild("name").getText())){
			Element name = plainFileElement.getChild("name");
			Element owner = plainFileElement.getChild("owner");
			Element permission = plainFileElement.getChild("permissions");
			Element path = plainFileElement.getChild("path");
			//Element mask = plainFileElement.getChild("mask");
			Element content = plainFileElement.getChild("data");
			User userOwner = mydrive.getUser(owner.getText());
			processPath(path.getText());
			Directory parent = getParentDirectory(path.getText());
			mydrive.createPlainFile(name.getText(), path.getText(), permission.getText(), content.getText(), userOwner, parent).set_fileId(Integer.parseInt(plainFileElement.getAttributeValue("id")));
		}
	}
	
	public void Import(MyDrive mydrive, Document doc){
		Element myDriveElement = doc.getRootElement();
		for (Element node: myDriveElement.getChildren()) {
		    String name = node.getName();
		    if(name.equals("user")){
		    	ImportUser(node);
		    }
		    
		    if(name.equals("plainfile")){
		    	ImportPlain(node);
		    }
			if(name.equals("linkfile")){
		    	ImportLink(node);
		    }
			if(name.equals("dir")){
				ImportDirectory(node);
			}
			if(name.equals("appfile")){
				ImportApp(node);
			}
		}
	}
}
