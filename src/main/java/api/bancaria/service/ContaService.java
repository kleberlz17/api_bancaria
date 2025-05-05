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
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
		
		if(novoSaldo == null) {
			throw new IllegalArgumentException("O saldo não pode ser nulo.");
		}
		
		Conta conta = contaRepository.findById(idConta)
				.orElseThrow(() -> new ContaNaoEncontradaException("Conta não encontrada"));
		
		if(conta.getSaldoAtual().equals(novoSaldo)) {
			log.info("Saldo já está atualizado para: {}", novoSaldo);
			return conta;
		}
		
		conta.setSaldoAtual(novoSaldo);
		
		Conta contaAtualizada = contaRepository.save(conta);
		log.info("Conta com ID {} atualizada com novo saldo: {}", idConta, contaAtualizada.getSaldoAtual());
		
		
		
		
		return contaAtualizada;
	}
	
	public Conta alterarStatus(Long idConta, StatusConta novoStatus) {
		Conta conta = contaRepository.findById(idConta)
				.orElseThrow(() -> new ContaNaoEncontradaException("Conta não encontrada"));
		
		if(conta.getStatusConta().equals(novoStatus)) {
			log.info("O Status já está atualiazdo para: {}", novoStatus);
			return conta;
		}
		
		conta.setStatusConta(novoStatus);
		
		Conta contaAtualizada = contaRepository.save(conta);
		log.info("Conta com o ID {} atualizada com o novo status: {}", idConta, contaAtualizada.getStatusConta());
		
		return contaAtualizada;
	}
	
	public List<Conta> buscarPorCliente(Long idCliente) {//Buscando Contas pelo id do cliente.
		List<Conta> lista = contaRepository.findByCliente_IdCliente(idCliente);
		if(lista.isEmpty()) {
			throw new ClienteNaoEncontradoException("ID do cliente inexistente");
		}
		return lista;
	}
	
	public Cliente buscarClientePorId(Long idCliente) {
		return clienteRepository.findById(idCliente)
				.orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado"));
	}
	
}
