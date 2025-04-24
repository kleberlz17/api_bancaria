package api.bancaria.exception;

@SuppressWarnings("serial")
public class CpfDuplicadoException extends RuntimeException {

	public CpfDuplicadoException(String message) {
		super(message);
	}

}
