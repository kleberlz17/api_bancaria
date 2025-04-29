package api.bancaria.exception;

import java.time.LocalDateTime;

public class ErroResposta {
	private String mensagem;
	private LocalDateTime timestamp;
	private int status;
	private String descricaoErro;
	
	public ErroResposta(String mensagem, int status, String descricaoErro) {
		this.mensagem = mensagem;
		this.timestamp = LocalDateTime.now();
		this.status = status;
		this.descricaoErro = descricaoErro;
	}

	public String getMensagem() {
		return mensagem;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;

	}
	
	public int getStatus() {
		return status;
	}
	
	public String getDescricaoErro() {
		return descricaoErro;
	}
}
