package api.bancaria.dto;

import java.util.List;

import api.bancaria.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UsuarioDTO {

	@NotNull(message = "Campo obrigatório")
	@Min(1)
	@Max(999999)
	private Long idUser;

	@NotBlank(message = "Campo obrigatório")
	private String login;

	@NotBlank(message = "Campo obrigatório")
	@Email(message = "Email inválido")
	private String email;

	@NotBlank(message = "Campo obrigatório")
	private String senha;

	@NotBlank(message = "Campo obrigatório")
	private List<Role> roles;

	public UsuarioDTO(Long idUser, String login, String email, String senha, List<Role> roles) {
		this.idUser = idUser;
		this.login = login;
		this.email = email;
		this.senha = senha;
		this.roles = roles;
	}

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}
