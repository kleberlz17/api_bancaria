package api.bancaria.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import api.bancaria.model.Client;
import api.bancaria.repository.ClientRepository;

@Service
public class ClientService {
	
	private final ClientRepository clientRepository;
	private final PasswordEncoder passwordEncoder;
	
	public ClientService(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
		this.clientRepository = clientRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public Client salvar(Client client) {
		var senhaCriptografada = passwordEncoder.encode(client.getClientSecret());
		client.setClientSecret(senhaCriptografada);
		return clientRepository.save(client);
	}
	
	public Client obterPorClientID(String clientId) {
		return clientRepository.findByClientId(clientId);
	}

}
