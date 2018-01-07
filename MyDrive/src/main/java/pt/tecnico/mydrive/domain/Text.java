package pt.tecnico.mydrive.domain;

import pt.tecnico.mydrive.exception.ReadPermissionDeniedException;
public abstract class Text extends Text_Base {
    /**
     * class Text
     * 
     * class that implements the type Text files of the MyDrive Application
     */
    public Text() {
     
    }
    
    
    /**
     * toString(String type)
     * receives a file and returns it content
     * @param type
     * @return s
     * s is the string with the content of a text file
     */
    public String toString(String type){
    	String s=super.toString(type);
    	s+= "\nContent: "+ get_Content();
    	return s;
    }
	

	 public String readFileContent(User u)  {
		 System.out.println("PERMISSION: \n \n \n" +  get_filePermission());
	    	if((u instanceof Root) || u.get_userName().equals(this.getCreator().get_userName()) || (u.get_uMask().contains("r")) || (u instanceof Nobody))
	    		return get_Content();
	    	else
	    		throw new ReadPermissionDeniedException(get_fileName(), get_path());
	        
	 }
    
    
    
    /**
     * String listFile(String s)
     * method that lists a Text type file of MyDrive system 
     * @param s
     * @return s
     */
	public String listFile(String s) {
		// TODO Auto-generated method stub
		if(!s.isEmpty()){
			s+="\t";
		}
		return s+=get_fileName(); 
	}
    
	
	
	
	/*public void insertIntoDir(Directory dir){		
			setDirectory(dir);
			dir.addDirectoryFiles(this);
			
			
	}*/
	
	
}
