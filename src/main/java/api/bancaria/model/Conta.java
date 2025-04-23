package api.bancaria.model;

import java.math.BigDecimal;

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
public class Conta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_conta", nullable = false)
	private Long idConta;

	@Column(name = "agencia", nullable = false)
	private String agencia; // String pq costuma ter zeros na esquerda ou padroes (0001-9).

	@Column(name = "saldo_atual", nullable = false, scale = 2)
	private BigDecimal saldoAtual;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_conta", nullable = false)
	private TipoConta tipoConta;

	@Enumerated(EnumType.STRING)
	@Column(name = "status_conta", nullable = false)
	private StatusConta statusConta;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dono_conta", nullable = false)
	private Cliente cliente;

	public Long getIdConta() {
		return idConta;
	}

	public void setIdConta(Long idConta) {
		this.idConta = idConta;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public BigDecimal getSaldoAtual() {
		return saldoAtual;
	}

	public void setSaldoAtual(BigDecimal saldoAtual) {
		this.saldoAtual = saldoAtual;
	}

	public TipoConta getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(TipoConta tipoConta) {
		this.tipoConta = tipoConta;
	}

	public StatusConta getStatusConta() {
		return statusConta;
	}

	public void setStatusConta(StatusConta statusConta) {
		this.statusConta = statusConta;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Override
	public String toString() {
		return "Conta [idConta=" + idConta + ", agencia=" + agencia + ", saldoAtual=" + saldoAtual + ", tipoConta="
				+ tipoConta + ", statusConta=" + statusConta + ", cliente=" + cliente + "]";
	}

	

}