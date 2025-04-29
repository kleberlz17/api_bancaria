package api.bancaria.exception;

@SuppressWarnings("serial")
public class SaldoInsuficienteException extends RuntimeException {
	public SaldoInsuficienteException(String mensagem) {
		super(mensagem);
	}

}
