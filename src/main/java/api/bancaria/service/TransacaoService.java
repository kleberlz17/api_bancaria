package api.bancaria.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import api.bancaria.dto.TransacaoDTO;
import api.bancaria.exception.ContaNaoEncontradaException;
import api.bancaria.exception.SaldoInsuficienteException;
import api.bancaria.exception.TransacaoNaoEncontradaException;
import api.bancaria.mapper.TransacaoConverter;
import api.bancaria.model.Conta;
import api.bancaria.model.TipoTransacao;
import api.bancaria.model.Transacao;
import api.bancaria.repository.ContaRepository;
import api.bancaria.repository.TransacaoRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransacaoService {

	private final TransacaoRepository transacaoRepository;
	private final ContaRepository contaRepository;
	private final TransacaoConverter transacaoConverter;

	public TransacaoService(TransacaoRepository transacaoRepository, ContaRepository contaRepository, TransacaoConverter transacaoConverter) {
		this.transacaoRepository = transacaoRepository;
		this.contaRepository = contaRepository;
		this.transacaoConverter = transacaoConverter;

	}

	public Transacao salvar(Transacao transacao) {
		log.info("Transação salva no sistema: {}", transacao);
		return transacaoRepository.save(transacao);
	}

	public Optional<TransacaoDTO> obterPorId(Long idTransacao) {
		//tem que carretar contaOrigem e contaDestino antes de retornar Transacao pq o hibernate está LAZY
		//fora da sessão ele não consegue mais acessar elas e causa  erro no Hibernate, entao esse método
		//deve garantir esse carregamento antes.
		Optional<Transacao> transacaoOpt = transacaoRepository.findById(idTransacao);
		
		if(transacaoOpt.isPresent()) {
			//Converte Transacao para TransacaoDTO
			TransacaoDTO transacaoDTO = transacaoConverter.entidadeParaDTO(transacaoOpt.get());
			log.info("ID da transação obtido: {}, Tipo de Transação: {}, Valor movimentado: {}, Data da Transação: {}, Conta de origem: {}, Conta de destino: {}",
					idTransacao, transacaoDTO.getTipoTransacao(),
					transacaoDTO.getValorMovimentado(), transacaoDTO.getDataTransacao(), 
					transacaoDTO.getContaOrigemId(), transacaoDTO.getContaDestinoId());
			
			return Optional.of(transacaoDTO);
		} else {
			return Optional.empty();
		}
	}

	public List<TransacaoDTO> obterPorValorMovimentado(BigDecimal valorMovimentado) {
		List<Transacao> transacoes = transacaoRepository.findByValorMovimentado(valorMovimentado);
		log.info("Valor buscado: {}, Transações de valor equivalente: {}", valorMovimentado, transacoes.size());
		return transacoes.stream()
				.map(transacaoConverter::entidadeParaDTO)
				.toList();
		
	}

	public List<TransacaoDTO> obterDataTransacao(LocalDate dataTransacao) {
		List<Transacao> datas = transacaoRepository.findByDataTransacaoOnly(dataTransacao);
		log.info("Data buscada: {}, Transações feitas no dia equivalente: {}", dataTransacao, datas.size());
		return datas.stream()
				.map(transacaoConverter::entidadeParaDTO)
				.collect(Collectors.toList());
	}
	

	public List<TransacaoDTO> listarTransacoesConta(Long idConta) {
		List<Transacao> lista =  transacaoRepository.findByContaOrigem_IdContaOrContaDestino_IdConta(idConta, idConta);
		if (lista.isEmpty()) {
			throw new TransacaoNaoEncontradaException("Nenhuma transação encontrada para a conta com ID: " + idConta);
		}
		log.info("ID da conta : {}, Transações feitas pela respectiva conta: {}", idConta, lista.size());
		return lista.stream()
				.map(transacaoConverter::entidadeParaDTO)
				.collect(Collectors.toList());// coleta todos os DTOS convertidos em uma lista.
	}

	public List<TransacaoDTO> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
		
		List<Transacao> lista =  transacaoRepository.findByDataTransacaoBetween(inicio, fim);
		if (lista.isEmpty()) {
			throw new TransacaoNaoEncontradaException("Nenhuma transação encontrada entre " + inicio + " e " + fim);
		}
		// Convertendo a lista de entidades transacao para DTOs novamente.
		log.info("Datas buscadas:  {} e {}, Transações feitas no período: {}", inicio, fim, lista.size());
		return lista.stream()
				.map(transacaoConverter::entidadeParaDTO)
				.collect(Collectors.toList());
	}

	public List<TransacaoDTO> listarPorTipo(TipoTransacao tipo) {
		List<Transacao> lista =  transacaoRepository.findByTipoTransacao(tipo);
		if (lista.isEmpty()) {
			throw new TransacaoNaoEncontradaException("Nenhuma transação encontrada para o tipo: " + tipo);
		}
		
		log.info("Tipo de transação buscado: {}, Transações feitas do tipo buscado: {}", tipo, lista.size());
		return lista.stream()
				.map(transacaoConverter::entidadeParaDTO)
				.collect(Collectors.toList());
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
		
		log.info("Transferência realizada da conta de ID {} para a conta de ID {} | Valor da transferência: {}", contaOrigemId, contaDestinoId, valor);
		return transacaoRepository.save(transacao);
	}

}
