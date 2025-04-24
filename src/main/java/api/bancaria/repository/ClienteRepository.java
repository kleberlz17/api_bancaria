package api.bancaria.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import api.bancaria.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	Optional<Cliente> findByNome(String nomeCompleto);

	Optional<Cliente> findByCPF(String cpf);

	Optional<Cliente> findByNomeAndCpfAndNascimento(String nomeCompleto, String cpf, LocalDate dataNascimento);
}
