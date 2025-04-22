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

	

}
