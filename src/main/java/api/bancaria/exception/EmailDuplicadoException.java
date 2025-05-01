package api.bancaria.exception;

@SuppressWarnings("serial")
public class EmailDuplicadoException extends RuntimeException {
	
	public EmailDuplicadoException(String message) {
		super(message);
	}

}
