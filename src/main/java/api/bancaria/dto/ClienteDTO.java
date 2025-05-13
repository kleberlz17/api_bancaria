package api.bancaria.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ClienteDTO {


	@Min(1)
	@Max(999999)
	private Long idCliente;
	
	@NotBlank(message = "Campo Obrigatório")
	@Size(min = 3, max = 100, message = "Campo fora do tamanho padrão")
	private String nome;
	
	@Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 números")//Pattern = Padrão.
	private String cpf;
	
	@NotNull(message = "Campo Obrigatório")
	private LocalDate dataNascimento;
	
	@NotBlank(message = "Campo Obrigatório")
	@Email(message = "Email inválido")
	private String email;
	
	@Pattern(regexp = "\\d{11}", message = "Telefone deve conter exatamente 11 números")//Pattern = Padrão.
	private String telefone;
	
	@NotBlank(message = "Campo Obrigatório")
	@Size(min = 5, max = 100, message = "Campo fora do tamanho padrão" )
	private String endereco;
	
	public ClienteDTO (Long idCliente, String nome, String cpf,
			LocalDate dataNascimento, String email, String telefone,String endereco) {
		
		this.idCliente = idCliente;
		this.nome = nome;
		this.cpf = cpf;
		this.dataNascimento = dataNascimento;
		this.email = email;
		this.telefone = telefone;
		this.endereco = endereco;
	}
	
	public ClienteDTO() { // Para os testes de integração
		
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	

	
}