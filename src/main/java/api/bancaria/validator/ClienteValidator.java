package api.bancaria.validator;

import org.springframework.stereotype.Component;

import api.bancaria.exception.CpfDuplicadoException;
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
		if (cliente.getCpf() == null) {
			return false;
		}
		
		return clienteRepository.findByCPF(cliente.getCpf()).isPresent();
	}

}
