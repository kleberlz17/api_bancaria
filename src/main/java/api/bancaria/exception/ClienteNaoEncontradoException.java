package api.bancaria.exception;

@SuppressWarnings("serial")
public class ClienteNaoEncontradoException extends RuntimeException {
	public ClienteNaoEncontradoException(String mensagem) {
		super(mensagem);
	}

}
