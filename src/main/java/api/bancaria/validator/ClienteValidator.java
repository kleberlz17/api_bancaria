package api.bancaria.validator;

import java.util.Optional;
import org.springframework.stereotype.Component;

import api.bancaria.exception.CpfDuplicadoException;
import api.bancaria.exception.EmailDuplicadoException;
import api.bancaria.exception.TelefoneDuplicadoException;
import api.bancaria.model.Cliente;
import api.bancaria.repository.ClienteRepository;

@Component
public class ClienteValidator {

	private ClienteRepository clienteRepository;

	public ClienteValidator(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}

	public void validarCliente(Cliente cliente) {
		if (existeClienteCadastrado(cliente)) {
			throw new CpfDuplicadoException("CPF existe no banco, cliente já cadastrado.");
		}
	}

	private boolean existeClienteCadastrado(Cliente cliente) {
		//Verificar no banco se existe algum cliente com o mesmo CPF.
		Optional<Cliente> existente = clienteRepository.findByCpf(cliente.getCpf());
		return existente.isPresent() && !existente.get().getIdCliente().equals(cliente.getIdCliente());
	}

	public void validarEmail(Cliente cliente) {
		if (existeEmailCadastrado(cliente)) {
			throw new EmailDuplicadoException("Email já cadastrado no banco");
		}
	}

	private boolean existeEmailCadastrado(Cliente cliente) {
		Optional<Cliente> existente = clienteRepository.findByEmail(cliente.getEmail());
		// Se o email ja está cadastrado, verifica se é do mesmo cliente que está
		// tentando alterar
		// Se for, entao é o cliente tentando salvar o mesmo e-mail que ja é dele, não é
		// duplicado.
		// Se for outro cliente com o mesmo email, bloqueia.
		return existente.isPresent() && !existente.get().getIdCliente().equals(cliente.getIdCliente());
	}

	public void validarTelefone(Cliente cliente) {
		if (existeTelefoneCadastrado(cliente)) {
			throw new TelefoneDuplicadoException("Telefone já cadastrado no banco");
		}
	}

	private boolean existeTelefoneCadastrado(Cliente cliente) {
		Optional<Cliente> existente = clienteRepository.findByTelefone(cliente.getTelefone());
		return existente.isPresent() && !existente.get().getIdCliente().equals(cliente.getIdCliente());
	}
}
