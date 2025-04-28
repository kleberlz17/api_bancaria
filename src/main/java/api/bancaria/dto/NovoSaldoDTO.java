package api.bancaria.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;

public class NovoSaldoDTO {
	
	@DecimalMin(value = "0.0", inclusive = true, message = "O saldo não pode ser negativo")
	private BigDecimal novoSaldo;

	public BigDecimal getNovoSaldo() {
		return novoSaldo;
	}

	public void setNovoSaldo(BigDecimal novoSaldo) {
		this.novoSaldo = novoSaldo;
	}
	
	

}
