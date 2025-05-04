package api.bancaria.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Service;

import api.bancaria.exception.ClienteNaoEncontradoException;
import api.bancaria.exception.ContaNaoEncontradaException;
import api.bancaria.model.Cliente;
import api.bancaria.model.Conta;
import api.bancaria.model.StatusConta;
import api.bancaria.repository.ClienteRepository;
import api.bancaria.repository.ContaRepository;

@Service
public class ContaService {

	private final ContaRepository contaRepository;
	private final ClienteRepository clienteRepository;
	
	public ContaService(ContaRepository contaRepository, ClienteRepository clienteRepository) {
		this.contaRepository = contaRepository;
		this.clienteRepository = clienteRepository;
	}
	
	public Conta salvar(Conta conta) {
		return contaRepository.save(conta);
	}
	
	public Optional<Conta> obterPorId(Long idConta){
		return contaRepository.findById(idConta);
	}
	
	public Conta atualizarSaldo(Long idConta, BigDecimal novoSaldo) {
		Conta conta = contaRepository.findById(idConta)
				.orElseThrow(() -> new ContaNaoEncontradaException("Conta não encontrada"));
		conta.setSaldoAtual(novoSaldo);
		return contaRepository.save(conta);
	}
	
	public Conta alterarStatus(Long idConta, StatusConta novoStatus) {
		Conta conta = contaRepository.findById(idConta)
				.orElseThrow(() -> new ContaNaoEncontradaException("Conta não encontrada"));
		conta.setStatusConta(novoStatus);
		return contaRepository.save(conta);
	}
	
	public List<Conta> buscarPorCliente(Long idCliente) {//Buscando Contas pelo id do cliente.
		List<Conta> lista = contaRepository.findByCliente_IdCliente(idCliente);
		if(lista.isEmpty()) {
			throw new ClienteNaoEncontradoException("ID do cliente inexistente");
		}
		return lista;
	}
	
	public Cliente buscarClientePorIdConta(Long idConta) {//Buscando Cliente pelo id da conta.
		return contaRepository.findById(idConta)
				.map(Conta::getCliente)
				.orElseThrow(() -> new ContaNaoEncontradaException("Conta não encontrada"));
	}
	
	public Cliente buscarClientePorId(Long idCliente) {
		return clienteRepository.findById(idCliente)
				.orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado"));
	}
	
}
