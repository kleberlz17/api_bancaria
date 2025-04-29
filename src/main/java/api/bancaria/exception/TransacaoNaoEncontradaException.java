package api.bancaria.exception;

@SuppressWarnings("serial")
public class TransacaoNaoEncontradaException extends RuntimeException {
	public TransacaoNaoEncontradaException(String mensagem) {
		super(mensagem);
	}

}
