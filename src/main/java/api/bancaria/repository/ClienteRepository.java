package api.bancaria.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import api.bancaria.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	

	Optional<Cliente> findByNome(String nome);

	Optional<Cliente> findByCpf(String cpf);

	Optional<Cliente> findByNomeAndCpfAndDataNascimento(String nome, String cpf, LocalDate dataNascimento);
	
	Optional<Cliente> findByEmail(String email);
	
	Optional<Cliente> findByTelefone(String telefone);
}
