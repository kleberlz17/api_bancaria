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
		log.info("Cliente salvo: {}", cliente);
		return clienteRepository.save(cliente);
	}
	
	public Optional<Cliente> buscarPorId(Long idCliente){
		log.info("Cliente encontrado com o ID: {}, Dados da conta: {}", idCliente);
		return clienteRepository.findById(idCliente);
	}
	
	public List<Cliente> buscarPorNome(String nome){
		List<Cliente> clientes = clienteRepository.findByNomeContainingIgnoreCase(nome);
		log.info("Clientes encontrados: {}", clientes.size());
		return clientes;
	}
	
	public Optional<Cliente> buscarPorCpf(String cpf) {
		log.info("Cliente encontrado com o CPF: {}", cpf);
		return clienteRepository.findByCpfContainingIgnoreCase(cpf);
	}
	
	public Optional<Cliente> buscarPorNomeAndCpfAndDataNascimento(String nome, String cpf, LocalDate dataNascimento){
		log.info("Cliente encontrado com Nome: {}, CPF: {}, Data de Nascimento: {}", nome, cpf, dataNascimento);
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
		
		log.info("Email alterado para: {}", emailNovo);
		return atualizarCampo(idCliente, cliente -> { cliente.setEmail(emailNovo);
			clienteValidator.validarEmail(cliente);
		});
	}
	
	public Cliente alterarTelefone(Long idCliente, String telefoneNovo) {
		if(telefoneNovo == null || telefoneNovo.isEmpty()) {
			throw new IllegalArgumentException("Telefone não pode ser nulo ou vazio");
		}
		
		log.info("Telefone alterado para: {}", telefoneNovo);
		return atualizarCampo(idCliente, cliente -> { cliente.setTelefone(telefoneNovo);
		clienteValidator.validarTelefone(cliente);
		});
	}
	
	public Cliente alterarEndereco(Long idCliente, String enderecoNovo) { //Não achei necessário uso de um validator aqui.
		Cliente cliente = clienteRepository.findById(idCliente)
				.orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado"));
		
		cliente.setEndereco(enderecoNovo);
		log.info("Endereço alterado para: {}", enderecoNovo);
		return clienteRepository.save(cliente);
	}
	

	
}
