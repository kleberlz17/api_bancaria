package api.bancaria.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "nome", length = 150, nullable = false)
	private String nomeCompleto;

	@Column(name = "cpf", nullable = false, unique = true)
	private String cpf;

	@Column(name = "data_nascimento", nullable = false)
	private LocalDate dataNascimento;

	@Column(name = "email", length = 100, nullable = false)
	private String email;

	@Column(name = "telefone", length = 20, nullable = false)
	private String telefone;

	@Column(name = "endereco", length = 200, nullable = false)
	private String endereco;

	@OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
	private List<Conta> contas;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
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

	public List<Conta> getContas() {
		return contas;
	}

	public void setContas(List<Conta> contas) {
		this.contas = contas;
	}

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", nomeCompleto=" + nomeCompleto + ", cpf=" + cpf + ", dataNascimento="
				+ dataNascimento + ", email=" + email + ", telefone=" + telefone + ", endereco=" + endereco
				+ ", contas=" + contas + "]";
	}

}
