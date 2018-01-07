package pt.tecnico.mydrive.exception;

public class ReadPermissionDeniedException extends MyDriveException {

	private static final long serialVersionUID = 1L;
	private String _userName;
	private String _fileName;
	
	public ReadPermissionDeniedException(String user, String file){
		_userName=user;
		_fileName=file;
	}

	/**
	 * @return the _userName
	 */
	public String get_userName() {
		return _userName;
	}

	/**
	 * @return the _fileName
	 */
	public String get_fileName() {
		return _fileName;
	}

	public String getMessage(){
		return get_fileName() + "permission level does not grant User: "
				+get_userName() + " Read level access.";
	}

}
