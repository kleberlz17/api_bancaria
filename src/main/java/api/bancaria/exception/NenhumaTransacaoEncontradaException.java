package api.bancaria.exception;

@SuppressWarnings("serial")
public class NenhumaTransacaoEncontradaException extends RuntimeException {
	public NenhumaTransacaoEncontradaException(String mensagem) {
		super(mensagem);
	}

}
