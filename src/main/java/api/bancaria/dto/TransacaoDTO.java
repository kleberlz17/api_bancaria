package api.bancaria.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import api.bancaria.model.TipoTransacao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class TransacaoDTO {

	@NotNull(message = "Campo obrigatório")
	@Min(1)
	private Long idTransacao;

	@NotNull(message = "Informe o tipo de transação")
	private TipoTransacao tipoTransacao;

	@NotNull(message = "Valor movimentado não pode ser nulo")
	@DecimalMin("0.01")
	private BigDecimal valorMovimentado;

	private LocalDateTime dataTransacao;

	@NotNull(message = "Informe o ID da conta de origem da transação")
	private Long contaOrigemId;

	@NotNull(message = "Informe o ID da conta de destino da transação")
	private Long contaDestinoId;

	public TransacaoDTO(Long idTransacao, TipoTransacao tipoTransacao, BigDecimal valorMovimentado,
			LocalDateTime dataTransacao, Long contaOrigemId, Long contaDestinoId) {
		this.idTransacao = idTransacao;
		this.tipoTransacao = tipoTransacao;
		this.valorMovimentado = valorMovimentado;
		this.dataTransacao = dataTransacao;
		this.contaOrigemId = contaOrigemId;
		this.contaDestinoId = contaDestinoId;

	}

	public Long getIdTransacao() {
		return idTransacao;
	}

	public void setIdTransacao(Long idTransacao) {
		this.idTransacao = idTransacao;
	}

	public TipoTransacao getTipoTransacao() {
		return tipoTransacao;
	}

	public void setTipoTransacao(TipoTransacao tipoTransacao) {
		this.tipoTransacao = tipoTransacao;
	}

	public BigDecimal getValorMovimentado() {
		return valorMovimentado;
	}

	public void setValorMovimentado(BigDecimal valorMovimentado) {
		this.valorMovimentado = valorMovimentado;
	}

	public LocalDateTime getDataTransacao() {
		return dataTransacao;
	}

	public void setDataTransacao(LocalDateTime dataTransacao) {
		this.dataTransacao = dataTransacao;
	}

	public Long getContaOrigemId() {
		return contaOrigemId;
	}

	public void setContaOrigemId(Long contaOrigemId) {
		this.contaOrigemId = contaOrigemId;
	}

	public Long getContaDestinoId() {
		return contaDestinoId;
	}

	public void setContaDestinoId(Long contaDestinoId) {
		this.contaDestinoId = contaDestinoId;
	}

}
