package api.bancaria.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import api.bancaria.dto.ClienteDTO;
import api.bancaria.dto.NovoEmailDTO;
import api.bancaria.dto.NovoEnderecoDTO;
import api.bancaria.dto.NovoTelefoneDTO;
import api.bancaria.mapper.ClienteConverter;
import api.bancaria.model.Cliente;
import api.bancaria.service.ClienteService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/clientes")
@Slf4j
public class ClienteController {
	
	private final ClienteService clienteService;
	private final ClienteConverter clienteConverter; // mapper
	
	public ClienteController (ClienteService clienteService, ClienteConverter clienteConverter) {
		this.clienteService = clienteService;
		this.clienteConverter = clienteConverter;
	}
	
	@PostMapping
	public ResponseEntity<Object> salvar (@RequestBody @Valid ClienteDTO clienteDTO) {
		log.info("Salvando dados do cliente: {}", clienteDTO.getIdCliente());
		
		Cliente cliente = clienteConverter.dtoParaEntidade(clienteDTO);
		Cliente clienteSalvo = clienteService.salvar(cliente);
		
		URI uri = URI.create("/clientes/" + clienteSalvo.getIdCliente());
		
		return ResponseEntity.created(uri).build();	
	}
	
	@GetMapping("/{idCliente}")
	public ResponseEntity<Cliente> buscarPorId(@PathVariable Long idCliente) {
		log.info("Buscando cliente com o ID: {}", idCliente);
		
		Optional<Cliente> cliente = clienteService.buscarPorId(idCliente);
		
		if (cliente.isPresent()) {
			return ResponseEntity.ok(cliente.get());
		} else {
			return ResponseEntity.notFound().build();
		}	
	}
	
	@GetMapping("/nome")
	public ResponseEntity<Cliente> buscarPorNome(@RequestParam String nome) {
		log.info("Buscando cliente com o nome: {}", nome);
		
		Optional<Cliente> clienteNome = clienteService.buscarPorNome(nome);
		
		if(clienteNome.isPresent()) {
			return ResponseEntity.ok(clienteNome.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/cpf")
	public ResponseEntity<Cliente> buscarPorCpf(@RequestParam String cpf) {
		log.info("Buscando cliente com o CPF: {}", cpf);
		
		Optional<Cliente> cpfCliente = clienteService.buscarPorCpf(cpf);
		
		if(cpfCliente.isPresent()) {
			return ResponseEntity.ok(cpfCliente.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/dadoscliente")
	public ResponseEntity<Cliente> buscarPorNomeAndCpfAndNascimento(@RequestParam String nome, @RequestParam String cpf, @RequestParam LocalDate dataNascimento) {
		log.info("Buscando cliente: {} {] {}", nome, cpf, dataNascimento);
		
		Optional<Cliente> dadosCliente = clienteService.buscarPorNomeAndCpfAndDataNascimento(nome, cpf, dataNascimento);
		
		if(dadosCliente.isPresent()) {
			return ResponseEntity.ok(dadosCliente.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PutMapping("/{idCliente}/novoemail")
	public ResponseEntity<Cliente> alterarEmail(@PathVariable Long idCliente, @RequestBody @Valid NovoEmailDTO novoEmailDto) {
		log.info("Alterando o email do cliente com o ID: {}", idCliente);
		Cliente emailAlterado = clienteService.alterarEmail(idCliente, novoEmailDto.getEmail());
		return ResponseEntity.ok(emailAlterado);
	}
	
	@PutMapping("/{idCliente}/novotelefone")
	public ResponseEntity<Cliente> alterarTelefone(@PathVariable Long idCliente, @RequestBody @Valid NovoTelefoneDTO novoTelefoneDto) {
		log.info("Alterando o telefone do cliente com o ID: {}", idCliente);
		Cliente telefoneAlterado = clienteService.alterarTelefone(idCliente, novoTelefoneDto.getTelefone());
		return ResponseEntity.ok(telefoneAlterado);
	}
	
	@PutMapping("/{idCliente}/novoendereco")
	public ResponseEntity<Cliente> alterarEndereco(@PathVariable Long idCliente, @RequestBody @Valid NovoEnderecoDTO novoEnderecoDto) {
		log.info("Alterando o endereço do cliente com o ID: {}", idCliente);
		
		try {
			Cliente enderecoAlterado = clienteService.alterarEndereco(idCliente, novoEnderecoDto.getEndereco());
			return ResponseEntity.ok(enderecoAlterado);
		} catch (RuntimeException e) {
			log.error("Erro ao alterar o endereço do cliente: {}", e.getMessage());
			return ResponseEntity.notFound().build();
		}	
	}
}
