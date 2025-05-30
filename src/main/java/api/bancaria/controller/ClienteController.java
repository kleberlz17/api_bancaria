package api.bancaria.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
		log.info("Salvando dados do cliente...");
		
		Cliente cliente = clienteConverter.dtoParaEntidade(clienteDTO);
		Cliente clienteSalvo = clienteService.salvar(cliente);
		
		URI uri = URI.create("/clientes/" + clienteSalvo.getIdCliente());
		
		log.info("Cliente salvo com sucesso. ID gerado: {}", clienteSalvo.getIdCliente());
		return ResponseEntity.created(uri).build();	
	}
	
	@GetMapping("/{idCliente}")
	public ResponseEntity<Cliente> buscarPorId(@PathVariable Long idCliente) {
		log.info("Buscando cliente com o ID {} no sistema...", idCliente);
		
		Optional<Cliente> cliente = clienteService.buscarPorId(idCliente);
		
		if (cliente.isPresent()) {
			log.info("Cliente encontrado.");
			return ResponseEntity.ok(cliente.get());
		} else {
			return ResponseEntity.notFound().build();
		}	
	}
	
	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Cliente>> buscarPorNome(@PathVariable String nome) {
		log.info("Buscando cliente com o nome {} no sistema...", nome);
		
		List<Cliente> clienteNome = clienteService.buscarPorNome(nome);
		
		if(clienteNome.isEmpty()) {
			return ResponseEntity.notFound().build();
		} else {
			
			log.info("Cliente encontrado.");
			return ResponseEntity.ok(clienteNome);
		}
	}
	
	@GetMapping("/cpf/{cpf}")
	public ResponseEntity<Cliente> buscarPorCpf(@PathVariable String cpf) {
		log.info("Buscando cliente com o CPF {} no sistema...", cpf);
		
		Optional<Cliente> cpfCliente = clienteService.buscarPorCpf(cpf);
		
		if(cpfCliente.isPresent()) {
			log.info("Cliente encontrado.");
			return ResponseEntity.ok(cpfCliente.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/dadoscliente/{nome}/{cpf}/{dataNascimento}")
	public ResponseEntity<Cliente> buscarPorNomeAndCpfAndNascimento(@PathVariable String nome, @PathVariable String cpf, @PathVariable LocalDate dataNascimento) {
		log.info("Buscando cliente no sistema: {} {] {}", nome, cpf, dataNascimento);
		
		Optional<Cliente> dadosCliente = clienteService.buscarPorNomeAndCpfAndDataNascimento(nome, cpf, dataNascimento);
		
		if(dadosCliente.isPresent()) {
			log.info("Cliente encontrado.");
			return ResponseEntity.ok(dadosCliente.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PutMapping("/{idCliente}/novoemail")
	public ResponseEntity<Cliente> alterarEmail(@PathVariable Long idCliente, @RequestBody @Valid NovoEmailDTO novoEmailDto) {
		log.info("Alterando o email do cliente com o ID {} no sistema...", idCliente);
		Cliente emailAlterado = clienteService.alterarEmail(idCliente, novoEmailDto.getEmail());
		
		log.info("Email do cliente alterado com sucesso.");
		return ResponseEntity.ok(emailAlterado);
	}
	
	@PutMapping("/{idCliente}/novotelefone")
	public ResponseEntity<Cliente> alterarTelefone(@PathVariable Long idCliente, @RequestBody @Valid NovoTelefoneDTO novoTelefoneDto) {
		log.info("Alterando o telefone do cliente com o ID {} no sistema...", idCliente);
		Cliente telefoneAlterado = clienteService.alterarTelefone(idCliente, novoTelefoneDto.getTelefone());
		
		log.info("Telefone do cliente alterado com sucesso.");
		return ResponseEntity.ok(telefoneAlterado);
	}
	
	@PutMapping("/{idCliente}/novoendereco")
	public ResponseEntity<Cliente> alterarEndereco(@PathVariable Long idCliente, @RequestBody @Valid NovoEnderecoDTO novoEnderecoDto) {
		log.info("Alterando o endereço do cliente com o ID {} no sistema...", idCliente);
		
		try {
			Cliente enderecoAlterado = clienteService.alterarEndereco(idCliente, novoEnderecoDto.getEndereco());
			return ResponseEntity.ok(enderecoAlterado);
		} catch (RuntimeException e) {
			log.error("Erro ao alterar o endereço do cliente: {}", e.getMessage());
			return ResponseEntity.notFound().build();
		}	
	}
}
