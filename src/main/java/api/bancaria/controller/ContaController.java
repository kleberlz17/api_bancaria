package api.bancaria.controller;


import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.bancaria.dto.ContaDTO;
import api.bancaria.dto.NovoSaldoDTO;
import api.bancaria.dto.NovoStatusDTO;
import api.bancaria.mapper.ContaConverter;
import api.bancaria.model.Cliente;
import api.bancaria.model.Conta;

import api.bancaria.service.ContaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/contas")
@Slf4j
public class ContaController {

	private final ContaService contaService;
	private final ContaConverter contaConverter;// mapper.

	public ContaController(ContaService contaService, ContaConverter contaConverter) {
		this.contaService = contaService;
		this.contaConverter = contaConverter;
	}

	@PostMapping
	public ResponseEntity<Object> salvar(@RequestBody @Valid ContaDTO contaDTO) {
		log.info("Salvando dados da conta: {}", contaDTO.getIdConta());

		Cliente cliente = contaService.buscarClientePorId(contaDTO.getClienteId());
		Conta conta = contaConverter.dtoParaEntidade(contaDTO, cliente);
		Conta contaSalva = contaService.salvar(conta);

		URI uri = URI.create("/contas/" + contaSalva.getIdConta());

		return ResponseEntity.created(uri).build();
	}

	@GetMapping("/{idConta}")
	public ResponseEntity<Conta> obterPorId(@PathVariable Long idConta) {
		log.info("Buscando conta com o ID: {}", idConta);

		Optional<Conta> conta = contaService.obterPorId(idConta);

		if (conta.isPresent()) {
			return ResponseEntity.ok(conta.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/{idConta}/saldo")
	public ResponseEntity<Conta> atualizarSaldo(@PathVariable Long idConta, @RequestBody @Valid NovoSaldoDTO novoSaldoDto) {
		log.info("Atualizando o saldo da conta com o ID: {}", idConta);

		try {
			Conta contaAtualizada = contaService.atualizarSaldo(idConta, novoSaldoDto.getNovoSaldo());
			return ResponseEntity.ok(contaAtualizada);
		} catch (RuntimeException e) {
			log.error("Erro ao atualizar o saldo: {}", e.getMessage());
			return ResponseEntity.notFound().build();
		}
		
	}
	
	@PutMapping("/{idConta}/status")
	public ResponseEntity<Conta> alterarStatus(@PathVariable Long idConta, @RequestBody @Valid NovoStatusDTO novoStatusDto) {
		log.info("Alterando o status da conta com o ID: {}", idConta);
		
		try {
			Conta statusAtualizado = contaService.alterarStatus(idConta, novoStatusDto.getStatusConta());
			return ResponseEntity.ok(statusAtualizado);
		} catch (RuntimeException e) {
			log.error("Erro ao alterar o status da conta: {}", e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/{idConta}/contascliente")
	public ResponseEntity<List<Conta>> buscarPorCliente(@PathVariable Long idCliente) {
		log.info("Buscando contas pelo ID do Cliente {}", idCliente);
		 
		List<Conta> contas = contaService.buscarPorCliente(idCliente);
		if (contas.isEmpty()) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(contas);
		}
		
	}
	
	@GetMapping("/{idConta}/clientedaconta")
	public ResponseEntity<Cliente> buscarClientePorIdConta(@PathVariable Long idConta) {
		log.info("Buscando cliente pela conta com ID: {}", idConta);
		
		try {
			Cliente clienteBuscado = contaService.buscarClientePorIdConta(idConta);
			return ResponseEntity.ok(clienteBuscado);
		} catch (RuntimeException e) {
			log.error("Erro ao buscar cliente: {}", e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/{idCliente}/cliente")
	public ResponseEntity<Cliente> buscarClientePorId(@PathVariable Long idCliente) {
		log.info("Buscando cliente pelo ID; {}", idCliente);
		
		try {
			Cliente cliente = contaService.buscarClientePorId(idCliente);
			return ResponseEntity.ok(cliente);
		} catch (RuntimeException e ) {
			log.error("Erro ao buscar o cliente: {}", e.getMessage());
			return ResponseEntity.notFound().build();
		}		
	}

}
