package api.bancaria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NovoLoginDTO {

	@NotBlank(message = "O campo de login é obrigatório.")
	@Size(min = 6, max = 15, message = "O novo login de usuário deve ter entre 6 e 15 caracteres")
	private String login;
	
	public NovoLoginDTO() {//Jackson depende dele pra criar a instancia do DTO quando converte o JSON(de fora) em um objeto.
		
	}
	
	public NovoLoginDTO(String login) {
		this.login = login;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

}
