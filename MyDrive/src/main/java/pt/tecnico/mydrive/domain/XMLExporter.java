package pt.tecnico.mydrive.domain;

import org.jdom2.*;

public class XMLExporter {
	
	public XMLExporter(){ }
	
	public Document Export(User user, Document doc){
		Element userElement = new Element("user");
        Element username = new Element("username");
        Element password = new Element("password");
        Element name = new Element("name");
        Element homedir = new Element("homedir");
        Element mask = new Element("mask");
        userElement.setAttribute("id", Integer.toString(user.get_systemId()));
        username.addContent(user.get_userName());
        password.addContent(user.get_password());
        name.addContent(user.get_name());
        homedir.addContent(user.get_homeDir());
        mask.addContent(user.get_uMask());
        userElement.addContent(username);
        userElement.addContent(password);
        userElement.addContent(name);
        userElement.addContent(homedir);
        userElement.addContent(mask);
        doc.getRootElement().addContent(userElement);
        return doc;
	}
	
	public Document Export(Directory dir, Document doc){
		Element dirElement = new Element("dir");
        Element name = new Element("name");
        Element owner = new Element("owner");
        Element permissions = new Element("permissions");
        Element path = new Element("path");
        Element lastModified = new Element("lastmodified");
        dirElement.setAttribute("id", Integer.toString(dir.get_fileId()));
        name.addContent(dir.get_fileName());
        owner.addContent(dir.getCreator().get_userName());
        permissions.addContent(dir.get_filePermission());
        path.addContent(dir.get_path());
        lastModified.addContent(dir.get_lastModification().toString());
        dirElement.addContent(name);
        dirElement.addContent(owner);
        dirElement.addContent(permissions);
        dirElement.addContent(path);
        dirElement.addContent(lastModified);
        doc.getRootElement().addContent(dirElement);
        return doc;
	}
	
	public Document Export(Link linkFile, Document doc){
		Element linkElement = new Element("linkfile");
        Element name = new Element("name");
        Element owner = new Element("owner");
        Element permissions = new Element("permissions");
        Element path = new Element("path");
        Element lastModified = new Element("lastmodified");
        Element content = new Element("data");
        linkElement.setAttribute("id", Integer.toString(linkFile.get_fileId()));
        name.addContent(linkFile.get_fileName());
        owner.addContent(linkFile.getCreator().get_userName());
        permissions.addContent(linkFile.get_filePermission());
        path.addContent(linkFile.get_path());
        lastModified.addContent(linkFile.get_lastModification().toString());
        content.addContent(linkFile.get_Content());
        linkElement.addContent(name);
        linkElement.addContent(owner);
        linkElement.addContent(permissions);
        linkElement.addContent(path);
        linkElement.addContent(lastModified);
        linkElement.addContent(content);
        doc.getRootElement().addContent(linkElement);
        return doc;
	}
	
	public Document Export(App appFile, Document doc){
		Element appElement = new Element("appfile");
        Element name = new Element("name");
        Element owner = new Element("owner");
        Element permissions = new Element("permissions");
        Element path = new Element("path");
        Element lastModified = new Element("lastmodified");
        Element content = new Element("data");
        appElement.setAttribute("id", Integer.toString(appFile.get_fileId()));
        name.addContent(appFile.get_fileName());
        owner.addContent(appFile.getCreator().get_userName());
        permissions.addContent(appFile.get_filePermission());
        path.addContent(appFile.get_path());
        lastModified.addContent(appFile.get_lastModification().toString());
        content.addContent(appFile.get_Content());
        appElement.addContent(name);
        appElement.addContent(owner);
        appElement.addContent(permissions);
        appElement.addContent(path);
        appElement.addContent(lastModified);
        appElement.addContent(content);
        doc.getRootElement().addContent(appElement);
        return doc;
	}
	
	public Document Export(Plain plainFile, Document doc){
		Element plainElement = new Element("plainfile");
        Element name = new Element("name");
        Element owner = new Element("owner");
        Element permissions = new Element("permissions");
        Element path = new Element("path");
        Element lastModified = new Element("lastmodified");
        Element content = new Element("data");
        plainElement.setAttribute("id", Integer.toString(plainFile.get_fileId()));
        name.addContent(plainFile.get_fileName());
        owner.addContent(plainFile.getCreator().get_userName());
        permissions.addContent(plainFile.get_filePermission());
        path.addContent(plainFile.get_path());
        lastModified.addContent(plainFile.get_lastModification().toString());
        content.addContent(plainFile.get_Content());
        plainElement.addContent(name);
        plainElement.addContent(owner);
        plainElement.addContent(permissions);
        plainElement.addContent(path);
        plainElement.addContent(lastModified);
        plainElement.addContent(content);
        doc.getRootElement().addContent(plainElement);
        return doc;
	}
	
	public Document Export(MyDrive mydrive, Document doc){
		for(User u: mydrive.getUserListSet()){
			doc = u.XMLExport(doc);
		}
		for(MyDriveFile f: mydrive.getFileListSet()){
			if(f instanceof Directory){
				doc = f.XMLExport(doc);
			}
		}
		for(MyDriveFile f: mydrive.getFileListSet()){
			if(!(f instanceof Directory)){
				doc = f.XMLExport(doc);
			}
		}
		return doc;
	}
}
