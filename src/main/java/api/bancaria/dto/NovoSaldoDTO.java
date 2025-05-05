package api.bancaria.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;

public class NovoSaldoDTO {

	@DecimalMin(value = "0.0", inclusive = true, message = "O saldo não pode ser negativo")
	private BigDecimal novoSaldo;
	
	public NovoSaldoDTO() {//Jackson depende dele pra criar a instancia do DTO quando converte o JSON(de fora) em um objeto.
		
	}
	
	public NovoSaldoDTO(BigDecimal novoSaldo) {
		this.novoSaldo = novoSaldo;
	}

	public BigDecimal getNovoSaldo() {
		return novoSaldo;
	}

	public void setNovoSaldo(BigDecimal novoSaldo) {
		this.novoSaldo = novoSaldo;
	}

}
