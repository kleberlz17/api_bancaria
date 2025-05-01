package api.bancaria.exception;

@SuppressWarnings("serial")
public class TelefoneDuplicadoException extends RuntimeException {
	
	public TelefoneDuplicadoException (String message) {
		super(message);
	}

}
