package api.bancaria.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(SaldoInsuficienteException.class)
	public ResponseEntity<ErroResposta> tratarSaldoInsuficiente(SaldoInsuficienteException ex) {
		ErroResposta erro = new ErroResposta(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), "Saldo insuficiente");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
		
	}
	
	@ExceptionHandler(ContaNaoEncontradaException.class)
	public ResponseEntity<ErroResposta> tratarContaNaoEncontrada(ContaNaoEncontradaException ex) {
		ErroResposta erro = new ErroResposta(ex.getMessage(), HttpStatus.NOT_FOUND.value(), "Conta não encontrada");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
	}
	
	@ExceptionHandler(TransacaoNaoEncontradaException.class)
	public ResponseEntity<ErroResposta> tratarTransacaoNaoEncontrada(TransacaoNaoEncontradaException ex) {
		ErroResposta erro = new ErroResposta(ex.getMessage(), HttpStatus.NOT_FOUND.value(), "Transação não encontrada");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
	}
	
	@ExceptionHandler(NenhumaTransacaoEncontradaException.class)
	public ResponseEntity<ErroResposta> tratarNenhumaTransacaoEncontrada(NenhumaTransacaoEncontradaException ex) {
		ErroResposta erro = new ErroResposta(ex.getMessage(), HttpStatus.NOT_FOUND.value(), "Nenhuma transação encontrada");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
	}
	
	// Exceção genérica pra outras que não forem tratadas.
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErroResposta> tratarExcecaoGeral(Exception ex) {
		ErroResposta erro = new ErroResposta("Erro interno: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro inesperado no servidor");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
	}

}
