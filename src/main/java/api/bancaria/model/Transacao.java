package api.bancaria.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Transacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_transacao", nullable = false)
	private Long idTransacao;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_transacao", length = 50, nullable = false)
	private TipoTransacao tipoTransacao;

	@Column(name = "valor_movimentado", nullable = false, scale = 2)
	private BigDecimal valorMovimentado;

	@Column(name = "data_transacao", nullable = false)
	private LocalDateTime dataTransacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "conta_origem_id", nullable = false)
	private Conta contaOrigem;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "conta_destino_id", nullable = false)
	private Conta contaDestino;

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

	public Conta getContaOrigem() {
		return contaOrigem;
	}

	public void setContaOrigem(Conta contaOrigem) {
		this.contaOrigem = contaOrigem;
	}

	public Conta getContaDestino() {
		return contaDestino;
	}

	public void setContaDestino(Conta contaDestino) {
		this.contaDestino = contaDestino;
	}

	@Override
	public String toString() {
		return "Transacao [idTransacao=" + idTransacao + ", tipoTransacao=" + tipoTransacao + ", valorMovimentado="
				+ valorMovimentado + ", dataTransacao=" + dataTransacao + ", contaOrigem=" + contaOrigem
				+ ", contaDestino=" + contaDestino + "]";
	}

	
}
