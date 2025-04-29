package api.bancaria.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import api.bancaria.exception.ContaNaoEncontradaException;
import api.bancaria.exception.SaldoInsuficienteException;
import api.bancaria.exception.TransacaoNaoEncontradaException;
import api.bancaria.model.Conta;
import api.bancaria.model.TipoTransacao;
import api.bancaria.model.Transacao;
import api.bancaria.repository.ContaRepository;
import api.bancaria.repository.TransacaoRepository;

@Service
public class TransacaoService {

	private final TransacaoRepository transacaoRepository;
	private final ContaRepository contaRepository;

	public TransacaoService(TransacaoRepository transacaoRepository, ContaRepository contaRepository) {
		this.transacaoRepository = transacaoRepository;
		this.contaRepository = contaRepository;

	}

	public Transacao salvar(Transacao transacao) {
		return transacaoRepository.save(transacao);
	}

	public Optional<Transacao> obterPorId(Long idTransacao) {
		return transacaoRepository.findById(idTransacao);
	}

	public List<Transacao> obterPorValorMovimentado(BigDecimal valorMovimentado) {
		List<Transacao> lista = transacaoRepository.findByValorMovimentado(valorMovimentado);
		if (lista.isEmpty()) {
			throw new TransacaoNaoEncontradaException("Nenhuma transação encontrada com o valor movimentado: " + valorMovimentado);
		}
		return lista;
		
	}

	public List<Transacao> obterDataTransacao(LocalDateTime dataTransacao) {
		List<Transacao> lista = transacaoRepository.findByDataTransacao(dataTransacao);
		if (lista.isEmpty()) {
			throw new TransacaoNaoEncontradaException("Nenhuma transação encontrada na data: " + dataTransacao);
		}
		return lista;
	}
	

	public List<Transacao> listarTransacoesConta(Long idConta) {
		List<Transacao> lista =  transacaoRepository.findByContaOrigemIdOrContaDestinoId(idConta, idConta);
		if (lista.isEmpty()) {
			throw new TransacaoNaoEncontradaException("Nenhuma transação encontrada para a conta com ID: " + idConta);
		}
		return lista;
	}

	public List<Transacao> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
		List<Transacao> lista =  transacaoRepository.findByDataTransacaoBetween(inicio, fim);
		if (lista.isEmpty()) {
			throw new TransacaoNaoEncontradaException("Nenhuma transação encontrada entre " + inicio + " e " + fim);
		}
		return lista;
	}

	public List<Transacao> listarPorTipo(TipoTransacao tipo) {
		List<Transacao> lista =  transacaoRepository.findByTipoTransacao(tipo);
		if (lista.isEmpty()) {
			throw new TransacaoNaoEncontradaException("Nenhuma transação encontrada para o tipo: " + tipo);
		}
		return lista;
	}

	public Transacao realizarTransferencia(Long contaOrigemId, Long contaDestinoId, BigDecimal valor) {
		Conta origem = contaRepository.findById(contaOrigemId)
				.orElseThrow(() -> new ContaNaoEncontradaException("Conta de origem não encontrada"));
		
		Conta destino = contaRepository.findById(contaDestinoId)
				.orElseThrow(() -> new ContaNaoEncontradaException("Conta de destino não encontrada"));
		
		if (origem.getSaldoAtual().compareTo(valor) < 0) {
			throw new SaldoInsuficienteException("Saldo insuficiente na conta de origem.");
		}
		
		origem.setSaldoAtual(origem.getSaldoAtual().subtract(valor));
		destino.setSaldoAtual(destino.getSaldoAtual().add(valor));
		
		Transacao transacao = new Transacao();
		transacao.setContaOrigem(origem);
		transacao.setContaDestino(destino);
		transacao.setValorMovimentado(valor);
		transacao.setDataTransacao(LocalDateTime.now());
		transacao.setTipoTransacao(TipoTransacao.TRANSFERENCIA);
		
		return transacaoRepository.save(transacao);
	}

}
