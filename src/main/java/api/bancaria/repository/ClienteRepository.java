package api.bancaria.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import api.bancaria.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	

	List<Cliente> findByNomeContainingIgnoreCase(String nome); // Busca por parte do nome

	Optional<Cliente> findByCpfContainingIgnoreCase(String cpf);

	Optional<Cliente> findByNomeContainingIgnoreCaseAndCpfContainingIgnoreCaseAndDataNascimento(String nome, String cpf, LocalDate dataNascimento);
	
	Optional<Cliente> findByEmail(String email);
	
	Optional<Cliente> findByTelefone(String telefone);
}
