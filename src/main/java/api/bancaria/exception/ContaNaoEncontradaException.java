package api.bancaria.exception;

@SuppressWarnings("serial")
public class ContaNaoEncontradaException extends RuntimeException{
	public ContaNaoEncontradaException(String mensagem) {
		super(mensagem);
	}

}
