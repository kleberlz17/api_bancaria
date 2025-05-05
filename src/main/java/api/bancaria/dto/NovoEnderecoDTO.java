package api.bancaria.dto;

import jakarta.validation.constraints.NotBlank;

public class NovoEnderecoDTO {

	@NotBlank(message = "O endereço não pode ser vazio.")
	private String endereco;

	public NovoEnderecoDTO() {//Jackson depende dele pra criar a instancia do DTO quando converte o JSON(de fora) em um objeto.
		
	}
	
	public NovoEnderecoDTO(String endereco) {
		this.endereco = endereco;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

}
