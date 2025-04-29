package api.bancaria.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import api.bancaria.dto.TransacaoDTO;
import api.bancaria.mapper.TransacaoConverter;
import api.bancaria.model.TipoTransacao;
import api.bancaria.model.Transacao;
import api.bancaria.service.TransacaoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/transacoes")
@Slf4j
public class TransacaoController {
	
	private final TransacaoService transacaoService;
	private final TransacaoConverter transacaoConverter;
	
	
	public TransacaoController(TransacaoService transacaoService, TransacaoConverter transacaoConverter) {
		this.transacaoService = transacaoService;
		this.transacaoConverter = transacaoConverter;
		
	}
	
	@PostMapping
	public ResponseEntity<Object> salvar (@RequestBody @Valid TransacaoDTO transacaoDTO) {
		log.info("Salvando dados de transações: {}", transacaoDTO.getIdTransacao());
		
		Transacao transacao = transacaoConverter.dtoParaEntidade(transacaoDTO);
		Transacao transacaoSalva = transacaoService.salvar(transacao);
		
		TransacaoDTO transacaoSalvaDTO = transacaoConverter.entidadeParaDTO(transacaoSalva);
		
		URI uri = URI.create("/transacoes/" + transacaoSalvaDTO.getIdTransacao());
		
		return ResponseEntity.created(uri).body(transacaoSalvaDTO);
	}
	
	@GetMapping("/{idTransacao}")
	public ResponseEntity<Transacao> obterPorId(@PathVariable Long idTransacao) {
		log.info("Buscando transação com o ID: {}", idTransacao);
		
		Optional<Transacao> transacao = transacaoService.obterPorId(idTransacao);
		
		if(transacao.isPresent()) {
			return ResponseEntity.ok(transacao.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/valores")
	public ResponseEntity<List<Transacao>> obterPorValorMovimentado(@RequestParam BigDecimal valorMovimentado) {
		log.info("Buscando transações por valor movimentado: {}", valorMovimentado);
		
		List<Transacao> valoresTransacoes = transacaoService.obterPorValorMovimentado(valorMovimentado);
		if(valoresTransacoes.isEmpty()) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(valoresTransacoes);
		}
	}
	
	@GetMapping("/datastransacoes")
	public ResponseEntity<List<Transacao>> obterDataTransacao(@RequestParam LocalDateTime dataTransacao) {
		log.info("Buscando datas de movimentações de transações: {}", dataTransacao);
		
		List<Transacao> datasTransacoes = transacaoService.obterDataTransacao(dataTransacao);
		if(datasTransacoes.isEmpty()) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(datasTransacoes);
		}
	}
	
	@GetMapping("/transacoesconta/{idConta}")
	public ResponseEntity<List<Transacao>> listarTransacoesConta(@PathVariable Long idConta) {
		log.info("Buscando transações de conta com ID: {}", idConta);
		
		List<Transacao> listaTransacoes = transacaoService.listarTransacoesConta(idConta);
		if(listaTransacoes.isEmpty()) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(listaTransacoes);
		}	
	}
	
	@GetMapping("/periodo")
	public ResponseEntity<List<Transacao>> listarPorPeriodo(@RequestParam LocalDateTime inicio,  @RequestParam LocalDateTime fim) {
		log.info("Buscando transações entre {} e {}", inicio, fim);
		
		List<Transacao> listaPeriodo = transacaoService.listarPorPeriodo(inicio, fim);
		if(listaPeriodo.isEmpty()) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(listaPeriodo);
		}
	}
	
	@GetMapping("/tipostransacao")
	public ResponseEntity<List<Transacao>> listarPorTipo(@RequestParam TipoTransacao tipo) {
		log.info("Buscando tipos de transações: {}", tipo);
		
		List<Transacao> tiposTransacoes = transacaoService.listarPorTipo(tipo);
		if(tiposTransacoes.isEmpty()) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(tiposTransacoes);
		}
		
	}
	
	@PostMapping("/transferir")
	public ResponseEntity<TransacaoDTO> transferir(@RequestBody @Valid TransacaoDTO transacaoDTO) {
		log.info("Iniciando transferência de conta {} para conta {} no valor de {}",
				transacaoDTO.getContaOrigemId(), transacaoDTO.getContaDestinoId(), transacaoDTO.getValorMovimentado());
		
		//Aqui chama o service diretamente com os IDs, não precisa buscar Conta aqui.
		Transacao transacao = transacaoService.realizarTransferencia(
				transacaoDTO.getContaOrigemId(),
				transacaoDTO.getContaDestinoId(),
				transacaoDTO.getValorMovimentado());
		
		TransacaoDTO respostaDTO = transacaoConverter.entidadeParaDTO(transacao);
		return ResponseEntity.ok(respostaDTO);
	}
	
	
	

}
