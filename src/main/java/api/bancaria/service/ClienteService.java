package api.bancaria.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import api.bancaria.model.Cliente;
import api.bancaria.repository.ClienteRepository;
import api.bancaria.validator.ClienteValidator;

@Service
public class ClienteService {

	private final ClienteRepository clienteRepository;
	private final ClienteValidator clienteValidator;
	
	public ClienteService(ClienteRepository clienteRepository, ClienteValidator clienteValidator) {
		this.clienteRepository = clienteRepository;
		this.clienteValidator = clienteValidator;
	}
	
	public Cliente salvar(Cliente cliente) {
		clienteValidator.validarCliente(cliente);
		return clienteRepository.save(cliente);
	}
	
	public Optional<Cliente> buscarPorId(Long idCliente){
		return clienteRepository.findById(idCliente);
	}
	
	public Optional<Cliente> buscarPorNome(String nomeCompleto){
		return clienteRepository.findByNome(nomeCompleto);
	}
	
	public Optional<Cliente> buscarPorCpf(String cpf) {
		return clienteRepository.findByCPF(cpf);
	}
	
	public Optional<Cliente> buscarPorNomeAndCpfAndNascimento(String nomeCompleto, String cpf, LocalDate dataNascimento){
		return clienteRepository.findByNomeAndCpfAndNascimento(nomeCompleto, cpf, dataNascimento);
	}
	
	private Cliente atualizarCampo(Long idCliente, Consumer<Cliente> atualizador) {
		Cliente cliente = clienteRepository.findById(idCliente)
				.orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
		atualizador.accept(cliente);
		return clienteRepository.save(cliente);//Utilitário pra evitar repetição nos métodos abaixo.
	}
	
	public Cliente alterarEmail(Long idCliente, String emailNovo) {
		return atualizarCampo(idCliente, cliente -> cliente.setEmail(emailNovo));
	}
	
	public Cliente alterarTelefone(Long idCliente, String telefoneNovo) {
		return atualizarCampo(idCliente, cliente -> cliente.setTelefone(telefoneNovo));
	}
	
	public Cliente alterarEndereco(Long idCliente, String enderecoNovo) {
		Cliente cliente = clienteRepository.findById(idCliente)
				.orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
		cliente.setEndereco(enderecoNovo);
		return clienteRepository.save(cliente);//Optei por manter  a lógica cheia aqui.
	}
	

	
}
