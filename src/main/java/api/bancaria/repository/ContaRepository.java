package api.bancaria.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import api.bancaria.model.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long> {
	
	// Cliente = nome do atributo da entidade Conta(relacionado ao dono da conta).
	// IdCliente = nome do atributo da entidade Cliente.
	List<Conta> findByCliente_IdCliente(Long idCliente); //Procurando todas as contas de um determinado cliente.
	
	Optional<Conta> findById(Long idConta);
}
