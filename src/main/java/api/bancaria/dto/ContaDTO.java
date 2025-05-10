package api.bancaria.dto;

import java.math.BigDecimal;

import api.bancaria.model.StatusConta;
import api.bancaria.model.TipoConta;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


public class ContaDTO {


	@Min(1)
	@Max(999999)
	private Long idConta;
	
	@NotBlank(message = "Agência não pode estar em branco")
	@Pattern(regexp = "\\d{4}", message = "Agência deve conter 4 numeros")
	private String agencia;
	
	
	private BigDecimal saldoAtual;
	
	@NotNull(message = "Informe o tipo da conta")
	private TipoConta tipoConta;
	
	@NotNull(message = "Informe o status da conta")
	private StatusConta statusConta;
	
	@NotNull(message = "Informe o ID do cliente")
	private Long clienteId;
	
	public ContaDTO(Long idConta, String agencia, BigDecimal saldoAtual,
			TipoConta tipoConta, StatusConta statusConta, Long clienteId) {
		
		this.idConta = idConta;
		this.agencia = agencia;
		this.saldoAtual = saldoAtual;
		this.tipoConta = tipoConta;
		this.statusConta = statusConta;
		this.clienteId = clienteId;
	}
	
	public ContaDTO() {
		
	}

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

	public Long getClienteId() {
		return clienteId;
	}

	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}

	
	
	
}
