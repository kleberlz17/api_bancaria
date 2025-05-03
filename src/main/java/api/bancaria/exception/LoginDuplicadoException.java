package api.bancaria.exception;

@SuppressWarnings("serial")
public class LoginDuplicadoException extends RuntimeException {
	
	public LoginDuplicadoException(String message) {
		super(message);
	}

}
