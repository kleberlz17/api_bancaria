package api.bancaria.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import api.bancaria.exception.ClienteNaoEncontradoException;
import api.bancaria.model.Cliente;
import api.bancaria.repository.ClienteRepository;
import api.bancaria.validator.ClienteValidator;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ClienteService {

	private final ClienteRepository clienteRepository;
	private final ClienteValidator clienteValidator;
	
	public ClienteService(ClienteRepository clienteRepository, ClienteValidator clienteValidator) {
		this.clienteRepository = clienteRepository;
		this.clienteValidator = clienteValidator;
	}
	
	public Cliente salvar(Cliente cliente) {
		if(cliente.getCpf() == null || cliente.getCpf().isEmpty()) {
			throw new IllegalArgumentException("CPF não pode ser nulo ou vazio");
		}
		
		if(cliente.getEmail() == null || cliente.getEmail().isEmpty()) {
			throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
		}
		
		clienteValidator.validarCliente(cliente);
		return clienteRepository.save(cliente);
	}
	
	public Optional<Cliente> buscarPorId(Long idCliente){
		return clienteRepository.findById(idCliente);
	}
	
	public List<Cliente> buscarPorNome(String nome){
		List<Cliente> clientes = clienteRepository.findByNomeContainingIgnoreCase(nome);
		log.info("Cliente encontrados: {}", clientes.size());
		return clientes;
	}
	
	public Optional<Cliente> buscarPorCpf(String cpf) {
		return clienteRepository.findByCpfContainingIgnoreCase(cpf);
	}
	
	public Optional<Cliente> buscarPorNomeAndCpfAndDataNascimento(String nome, String cpf, LocalDate dataNascimento){
		return clienteRepository.findByNomeContainingIgnoreCaseAndCpfContainingIgnoreCaseAndDataNascimento(nome, cpf, dataNascimento);
	}
	
	private Cliente atualizarCampo(Long idCliente, Consumer<Cliente> atualizador) {
		Cliente cliente = clienteRepository.findById(idCliente)
				.orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado"));
		atualizador.accept(cliente);
		return clienteRepository.save(cliente);//Utilitário pra evitar repetição nos métodos abaixo.
	}
	
	public Cliente alterarEmail(Long idCliente, String emailNovo) {
		if (emailNovo == null || emailNovo.isEmpty()) {
			throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
		}
		
		return atualizarCampo(idCliente, cliente -> { cliente.setEmail(emailNovo);
			clienteValidator.validarEmail(cliente);
		});
	}
	
	public Cliente alterarTelefone(Long idCliente, String telefoneNovo) {
		if(telefoneNovo == null || telefoneNovo.isEmpty()) {
			throw new IllegalArgumentException("Telefone não pode ser nulo ou vazio");
		}
		
		return atualizarCampo(idCliente, cliente -> { cliente.setTelefone(telefoneNovo);
		clienteValidator.validarTelefone(cliente);
		});
	}
	
	public Cliente alterarEndereco(Long idCliente, String enderecoNovo) { //Não achei necessário uso de um validator aqui.
		Cliente cliente = clienteRepository.findById(idCliente)
				.orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado"));
		
		cliente.setEndereco(enderecoNovo);
		
		return clienteRepository.save(cliente);
		
	}
	

	
}
