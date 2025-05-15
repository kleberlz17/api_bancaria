package api.bancaria.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import api.bancaria.model.Client;
import api.bancaria.service.ClientService;

@Component
public class ClientDataLoader implements CommandLineRunner {

	private final ClientService clientService;
	
	public ClientDataLoader(ClientService clientService) {
		this.clientService = clientService;
	}

	@Override
	public void run(String... args) throws Exception {
		if (clientService.obterPorClientID("meu-client") == null) {
			Client novoClient = new Client();
			novoClient.setClientId("meu-client");
			novoClient.setClientSecret("MinhaSenhaSecreta");
			novoClient.setRedirectUri("http://localhost:8080/login/oauth2/code/meu-client");
			novoClient.setScope("read write"); // read write = le, cria e edita
			// outros ex: delete = pode excluir / admin = acesso mais completo.
			// são scopes padrões de uso comum.
			
			clientService.salvar(novoClient);
			
			System.out.println("Client criado: meu-client");
		}
		
	}
	
	
	
}
