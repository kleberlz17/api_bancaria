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
	
	@ExceptionHandler(ClienteNaoEncontradoException.class)
	public ResponseEntity<ErroResposta> tratarClienteNaoEncontrada(ClienteNaoEncontradoException ex) {
		ErroResposta erro = new ErroResposta(ex.getMessage(), HttpStatus.NOT_FOUND.value(), "Nenhum cliente encontrado");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
	}
	
	@ExceptionHandler(EmailDuplicadoException.class)
	public ResponseEntity<ErroResposta> handleEmailDuplicado(EmailDuplicadoException ex) {
		ErroResposta erro = new ErroResposta(ex.getMessage(), HttpStatus.CONFLICT.value(), "Email já cadastrado");
		return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
	}
	
	@ExceptionHandler(TelefoneDuplicadoException.class)
	public ResponseEntity<ErroResposta> handleTelefoneDuplicado(TelefoneDuplicadoException ex) {
		ErroResposta erro = new ErroResposta(ex.getMessage(), HttpStatus.CONFLICT.value(), "Telefone já cadastrado");
		return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
	}
	
	@ExceptionHandler(CpfDuplicadoException.class)
	public ResponseEntity<ErroResposta> handleCpfDuplicado(CpfDuplicadoException ex) {
		ErroResposta erro = new ErroResposta(ex.getMessage(), HttpStatus.CONFLICT.value(), "CPF já cadastrado");
		return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
	}
	
	@ExceptionHandler(LoginDuplicadoException.class)
	public ResponseEntity<ErroResposta> handleLoginDuplicado(LoginDuplicadoException ex) {
		ErroResposta erro = new ErroResposta(ex.getMessage(), HttpStatus.CONFLICT.value(), "Login já cadastrado");
		return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
	}
	
	@ExceptionHandler(UsuarioNaoEncontradoException.class)
	public ResponseEntity<ErroResposta> tratarUsuarioNaoEncontrado(UsuarioNaoEncontradoException ex) {
		ErroResposta erro = new ErroResposta(ex.getMessage(), HttpStatus.NOT_FOUND.value(), "Nenhum usuário encontrado");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
		
	}

}
