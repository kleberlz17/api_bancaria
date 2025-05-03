package api.bancaria.exception;

@SuppressWarnings("serial")
public class UsuarioNaoEncontradoException extends RuntimeException {
	public UsuarioNaoEncontradoException(String mensagem) {
		super(mensagem);
	}

}
