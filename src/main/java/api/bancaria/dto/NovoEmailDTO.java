package api.bancaria.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class NovoEmailDTO {

	@NotBlank(message = "O campo de email é obrigatório.")
	@Email(message = "O email fornecido é inválido.")
	private String email;
	
	public NovoEmailDTO() {//Jackson depende dele pra criar a instancia do DTO quando converte o JSON(de fora) em um objeto.
		
	}

	public NovoEmailDTO(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
