package api.bancaria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NovoTelefoneDTO {

	@NotBlank(message = "O campo de telefone é obrigatório.")
	@Size(min = 10, max = 15, message = "O telefone deve ter entre 10 e 15 caracteres.")
	private String telefone;

	public NovoTelefoneDTO(String telefone) {
		this.telefone = telefone;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

}
