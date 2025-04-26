package api.bancaria.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import api.bancaria.model.Conta;
import api.bancaria.model.TipoTransacao;
import api.bancaria.model.Transacao;
import api.bancaria.repository.TransacaoRepository;

@Service
public class TransacaoService {

	private final TransacaoRepository transacaoRepository;

	public TransacaoService(TransacaoRepository transacaoRepository) {
		this.transacaoRepository = transacaoRepository;

	}

	public Transacao salvar(Transacao transacao) {
		return transacaoRepository.save(transacao);
	}

	public Optional<Transacao> obterPorId(Long idTransacao) {
		return transacaoRepository.findById(idTransacao);
	}

	public List<Transacao> obterPorValorMovimentado(BigDecimal valorMovimentado) {
		return transacaoRepository.findByValorMovimentado(valorMovimentado);
	}

	public List<Transacao> obterDataTransacao(LocalDateTime dataTransacao) {
		return transacaoRepository.findByDataTransacao(dataTransacao);
	}

	public List<Transacao> listarTransacoesConta(Long idConta) {
		return transacaoRepository.findByContaOrigemIdOrContaDestinoId(idConta, idConta);
	}

	public List<Transacao> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
		return transacaoRepository.findByDataTransacaoBetween(inicio, fim);
	}

	public List<Transacao> listarPorTipo(TipoTransacao tipo) {
		return transacaoRepository.findByTipoTransacao(tipo);
	}

	public Transacao realizarTransferencia(Conta origem, Conta destino, BigDecimal valor) {
		if (origem.getSaldoAtual().compareTo(valor) < 0) {
			throw new RuntimeException("Saldo insuficiente");
		}
		// Logica de tirar ou por do saldo atual;
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
