package pt.tecnico.mydrive.exception;

import pt.tecnico.mydrive.domain.Link;

public class CircularLinkException extends MyDriveException {
	private static final long serialVersionUID = 1L;

    private String _fileName;
    public CircularLinkException(Link file) {
        _fileName = file.get_fileName();
    }

    public String getFileName(){
    	return _fileName;
    }

    @Override
    public String getMessage() {
        return  "" + _fileName + " creates a loop in link processing";
    }
}