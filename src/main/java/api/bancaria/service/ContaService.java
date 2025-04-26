package api.bancaria.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Service;

import api.bancaria.model.Cliente;
import api.bancaria.model.Conta;
import api.bancaria.model.StatusConta;
import api.bancaria.repository.ContaRepository;

@Service
public class ContaService {

	private final ContaRepository contaRepository;
	
	public ContaService(ContaRepository contaRepository) {
		this.contaRepository = contaRepository;
	}
	
	public Conta salvar(Conta conta) {
		return contaRepository.save(conta);
	}
	
	public Optional<Conta> obterPorId(Long idConta){
		return contaRepository.findById(idConta);
	}
	
	public Conta atualizarSaldo(Long idConta, BigDecimal novoSaldo) {
		Conta conta = contaRepository.findById(idConta)
				.orElseThrow(() -> new RuntimeException("Conta não encontrada"));
		conta.setSaldoAtual(novoSaldo);
		return contaRepository.save(conta);
	}
	
	public Conta alterarStatus(Long idConta, StatusConta novoStatus) {
		Conta conta = contaRepository.findById(idConta)
				.orElseThrow(() -> new RuntimeException("Conta não encontrada"));
		conta.setStatusConta(novoStatus);
		return contaRepository.save(conta);
	}
	
	public List<Conta> buscarPorCliente(Long idCliente) {//Buscando Contas pelo id do cliente.
		return contaRepository.findByClienteId(idCliente);
	}
	
	public Cliente buscarClientePorIdConta(Long idConta) {//Buscando Cliente pelo id da conta.
		return contaRepository.findById(idConta)
				.map(Conta::getCliente)
				.orElseThrow(() -> new RuntimeException("Conta não encontrada"));
	}
	
}
