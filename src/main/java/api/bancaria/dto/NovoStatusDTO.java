package api.bancaria.dto;

import api.bancaria.model.StatusConta;
import jakarta.validation.constraints.NotNull;

public class NovoStatusDTO {

	@NotNull(message = "O status não pode ser nulo")
	private StatusConta statusConta;

	public NovoStatusDTO(StatusConta statusConta) {
		this.statusConta = statusConta;
	}

	public StatusConta getStatusConta() {
		return statusConta;
	}

	public void setStatusConta(StatusConta statusConta) {
		this.statusConta = statusConta;
	}

}
