package api.bancaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import api.bancaria.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
	
	Client findByClientId(String clientId);

}
