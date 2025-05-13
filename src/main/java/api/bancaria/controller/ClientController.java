package api.bancaria.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import api.bancaria.model.Client;
import api.bancaria.service.ClientService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("clients")
@Slf4j
public class ClientController {
	
	private final ClientService clientService;
	
	public ClientController(ClientService clientService) {
		this.clientService = clientService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasRole('GERENTE')")
	public void salvar(@RequestBody Client client) {
		log.info("Registrando novo Client: {} com scope: {} ", client.getClientId(), client.getScope());
		clientService.salvar(client);
	}
}
