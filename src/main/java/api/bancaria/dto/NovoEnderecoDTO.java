package api.bancaria.dto;

import jakarta.validation.constraints.NotBlank;


public class NovoEnderecoDTO {

	@NotBlank(message = "O endereço não pode ser vazio.")
	private String endereco;

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	
}
