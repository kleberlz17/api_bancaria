package api.bancaria.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import api.bancaria.dto.TransacaoDTO;
import api.bancaria.mapper.TransacaoConverter;
import api.bancaria.model.Cliente;
import api.bancaria.model.Conta;
import api.bancaria.model.StatusConta;
import api.bancaria.model.TipoConta;
import api.bancaria.model.TipoTransacao;
import api.bancaria.model.Transacao;
import api.bancaria.repository.ContaRepository;
import api.bancaria.repository.TransacaoRepository;

class TransacaoServiceTest {
	
	@Mock
	private TransacaoRepository transacaoRepository;
	
	@Mock
	private ContaRepository contaRepository;
	
	@Mock
	private TransacaoConverter transacaoConverter;
	
	@InjectMocks
	private TransacaoService transacaoService;
	
	private TransacaoDTO transacaoDTO;
	private TransacaoDTO transacaoDTO1;
	private Transacao transacao;
	private Transacao transacao1;
	private Conta conta1;
	private Conta conta2;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		Cliente cliente1 = new Cliente();
		cliente1.setIdCliente(1L);
		cliente1.setNome("Jacques");
		cliente1.setCpf("12345678900");
		cliente1.setDataNascimento(LocalDate.of(1992, 4, 30));
		cliente1.setEmail("jacques@email.com");
		cliente1.setTelefone("21999999999");
		cliente1.setEndereco("Rua Teste, 333");
		
		Cliente cliente2 = new Cliente();
		cliente2.setIdCliente(2L);
		cliente2.setNome("Berman");
		cliente2.setCpf("11111111111");
		cliente2.setDataNascimento(LocalDate.of(2000, 7, 10));
		cliente2.setEmail("berman@email.com");
		cliente2.setTelefone("21888888888");
		cliente2.setEndereco("Springfield, 46");
		
		conta1 = new Conta();
		conta1.setIdConta(1L);
		conta1.setAgencia("0001");
		conta1.setSaldoAtual(new BigDecimal("3000.00"));
		conta1.setTipoConta(TipoConta.CORRENTE);
		conta1.setStatusConta(StatusConta.ATIVA);
		conta1.setCliente(cliente1);
		
		conta2 = new Conta();
		conta2.setIdConta(2L);
		conta2.setAgencia("0001");
		conta2.setSaldoAtual(new BigDecimal("2000.00"));
		conta2.setTipoConta(TipoConta.POUPANCA);
		conta2.setStatusConta(StatusConta.ATIVA);
		conta2.setCliente(cliente2);
		
		transacao = new Transacao();
		transacao.setIdTransacao(11L);
		transacao.setTipoTransacao(TipoTransacao.TRANSFERENCIA);
		transacao.setValorMovimentado(new BigDecimal("250.00"));
		transacao.setDataTransacao(LocalDateTime.of(2025, 5, 7, 23, 30));
		transacao.setContaOrigem(conta1);
		transacao.setContaDestino(conta2);
		
		transacao1 = new Transacao();
		transacao1.setIdTransacao(12L);
		transacao1.setTipoTransacao(TipoTransacao.TRANSFERENCIA);
		transacao1.setValorMovimentado(new BigDecimal("300.00"));
		transacao1.setDataTransacao(LocalDateTime.of(2025, 5, 8, 02, 48));
		transacao1.setContaOrigem(conta1);
		transacao1.setContaDestino(conta2);
		
		transacaoDTO = new TransacaoDTO();
		transacaoDTO.setIdTransacao(11L);
		transacaoDTO.setTipoTransacao(TipoTransacao.TRANSFERENCIA);
		transacaoDTO.setValorMovimentado(new BigDecimal("250.00"));
		transacaoDTO.setDataTransacao(LocalDateTime.of(2025, 5, 7, 23, 30));
		transacaoDTO.setContaOrigemId(1L);
		transacaoDTO.setContaDestinoId(2L);
		
		transacaoDTO1 = new TransacaoDTO();
		transacaoDTO1.setIdTransacao(12L);
		transacaoDTO1.setTipoTransacao(TipoTransacao.TRANSFERENCIA);
		transacaoDTO1.setValorMovimentado(new BigDecimal("300.00"));
		transacaoDTO1.setDataTransacao(LocalDateTime.of(2025, 5, 8, 02, 48));
		transacaoDTO1.setContaOrigemId(1L);
		transacaoDTO1.setContaDestinoId(2L);
	}
	
	@Test
	void testSalvarTransacaoComSucesso() {
		
		Cliente clienteNovo = new Cliente();
		clienteNovo.setIdCliente(3L);
		clienteNovo.setNome("Webster");
		clienteNovo.setCpf("22222222222");
		clienteNovo.setDataNascimento(LocalDate.of(1998, 3, 1));
		clienteNovo.setEmail("webster@email.com");
		clienteNovo.setTelefone("21777777777");
		clienteNovo.setEndereco("Rua das Ruas, 11");
		
		Cliente clienteNovo2 = new Cliente();
		clienteNovo2.setIdCliente(4L);
		clienteNovo2.setNome("Travis");
		clienteNovo2.setCpf("33333333333");
		clienteNovo2.setDataNascimento(LocalDate.of(2003, 1, 1));
		clienteNovo2.setEmail("travis@email.com");
		clienteNovo2.setTelefone("21666666666");
		clienteNovo2.setEndereco("Houston, 44");
		
		Conta contaOrigem1 = new Conta();
		contaOrigem1.setIdConta(1L);
		contaOrigem1.setAgencia("0001");
		contaOrigem1.setSaldoAtual(new BigDecimal("3000.00"));
		contaOrigem1.setTipoConta(TipoConta.CORRENTE);
		contaOrigem1.setStatusConta(StatusConta.ATIVA);
		contaOrigem1.setCliente(clienteNovo);
		
		Conta contaDestino2 = new Conta();
		contaDestino2.setIdConta(2L);
		contaDestino2.setAgencia("0001");
		contaDestino2.setSaldoAtual(new BigDecimal("2000.00"));
		contaDestino2.setTipoConta(TipoConta.POUPANCA);
		contaDestino2.setStatusConta(StatusConta.ATIVA);
		contaDestino2.setCliente(clienteNovo2);
		
		Transacao novaTransacao = new Transacao();
		novaTransacao.setIdTransacao(12L);
		novaTransacao.setTipoTransacao(TipoTransacao.DEPOSITO);
		novaTransacao.setValorMovimentado(new BigDecimal("500.00"));
		novaTransacao.setDataTransacao(LocalDateTime.of(2025, 05, 07, 23, 30));
		novaTransacao.setContaOrigem(contaOrigem1);
		novaTransacao.setContaDestino(contaDestino2);
		
		when(transacaoRepository.save(any(Transacao.class))).thenReturn(novaTransacao);
		Transacao resultado = transacaoService.salvar(novaTransacao);
		
		assertNotNull(resultado);
		assertEquals(12L, resultado.getIdTransacao());
		assertEquals(TipoTransacao.DEPOSITO, resultado.getTipoTransacao());
		assertEquals(new BigDecimal("500.00"), resultado.getValorMovimentado());
		assertEquals(contaOrigem1, resultado.getContaOrigem());
		assertEquals(contaDestino2, resultado.getContaDestino());
		
		verify(transacaoRepository, times(1)).save(novaTransacao);
	}
	
	@Test
	void testObterPorIdComSucesso() {
		
		when(transacaoRepository.findById(11L)).thenReturn(Optional.of(transacao));
		when(transacaoConverter.entidadeParaDTO(transacao)).thenReturn(transacaoDTO);
		
		Optional<TransacaoDTO> resultado = transacaoService.obterPorId(11L);
		
		assertTrue(resultado.isPresent());
		assertEquals(transacaoDTO.getIdTransacao(), resultado.get().getIdTransacao());
		assertEquals(transacaoDTO.getTipoTransacao(), resultado.get().getTipoTransacao());
		assertEquals(transacaoDTO.getValorMovimentado(), resultado.get().getValorMovimentado());
		assertEquals(transacaoDTO.getDataTransacao(), resultado.get().getDataTransacao());
		assertEquals(transacaoDTO.getContaOrigemId(), resultado.get().getContaOrigemId());
		assertEquals(transacaoDTO.getContaDestinoId(), resultado.get().getContaDestinoId());
		
		verify(transacaoRepository, times(1)).findById(11L);
		verify(transacaoConverter, times(1)).entidadeParaDTO(transacao);
	}
	
	@Test
	void testObterPorValorMovimentadoComSucesso() {
		List<Transacao> transacoes = List.of(transacao);
		
		when(transacaoRepository.findByValorMovimentado(new BigDecimal("250.00"))).thenReturn(transacoes);
		when(transacaoConverter.entidadeParaDTO(transacao)).thenReturn(transacaoDTO);
		
		List<TransacaoDTO> resultado = transacaoService.obterPorValorMovimentado(new BigDecimal("250.00"));
		
		assertNotNull(resultado);
		assertEquals(1, resultado.size()); //Confere se a lista possui 1 apenas, já que realmente só tem 1.
		assertEquals(transacaoDTO.getIdTransacao(), resultado.get(0).getIdTransacao());
		assertEquals(transacaoDTO.getTipoTransacao(), resultado.get(0).getTipoTransacao());
		assertEquals(transacaoDTO.getValorMovimentado(), resultado.get(0).getValorMovimentado());
		
		verify(transacaoRepository, times(1)).findByValorMovimentado(new BigDecimal("250.00"));
		verify(transacaoConverter, times(1)).entidadeParaDTO(transacao);
	}
	
	@Test
	void testObterPorDataTransacaoComSucesso() {
		List<Transacao> transacoes = List.of(transacao);
		
		when(transacaoRepository.findByDataTransacaoOnly(LocalDate.of(2025, 5, 8))).thenReturn(transacoes);
		when(transacaoConverter.entidadeParaDTO(transacao)).thenReturn(transacaoDTO);
		
		List<TransacaoDTO> resultado = transacaoService.obterDataTransacao(LocalDate.of(2025, 5, 8));
		
		assertNotNull(resultado);
		assertEquals(1, resultado.size()); //Confere se a lista possui 1 apenas, já que realmente só tem 1.
		assertEquals(transacaoDTO.getIdTransacao(), resultado.get(0).getIdTransacao());
		assertEquals(transacaoDTO.getTipoTransacao(), resultado.get(0).getTipoTransacao());
		assertEquals(transacaoDTO.getDataTransacao(), resultado.get(0).getDataTransacao());
		
		verify(transacaoRepository, times(1)).findByDataTransacaoOnly(LocalDate.of(2025, 5, 8));
		verify(transacaoConverter, times(1)).entidadeParaDTO(transacao);
	}
	
	@Test
	void testListarTransacoesContaComSucesso() {
		List<Transacao> transacoes = List.of(transacao);
		
		when(transacaoRepository.findByContaOrigem_IdContaOrContaDestino_IdConta(1L, 1L)).thenReturn(transacoes);
		when(transacaoConverter.entidadeParaDTO(transacao)).thenReturn(transacaoDTO);
		
		List<TransacaoDTO> resultado = transacaoService.listarTransacoesConta(1L);
		
		assertNotNull(resultado);
		assertEquals(1, resultado.size()); //Confere se a lista possui 1 apenas, já que realmente só tem 1.
		assertEquals(transacaoDTO.getTipoTransacao(), resultado.get(0).getTipoTransacao());
		assertEquals(transacaoDTO.getValorMovimentado(), resultado.get(0).getValorMovimentado());
		assertEquals(transacaoDTO.getDataTransacao(), resultado.get(0).getDataTransacao());
		
		verify(transacaoRepository, times(1)).findByContaOrigem_IdContaOrContaDestino_IdConta(1L, 1L);
		verify(transacaoConverter, times(1)).entidadeParaDTO(transacao);
	}
	
	@Test
	void testListarPorPeriodoComSucesso() {
		List<Transacao> transacoes = List.of(transacao, transacao1);
		
		when(transacaoRepository.findByDataTransacaoBetween(LocalDate.of(2025, 5, 7), LocalDate.of(2025, 5, 8))).thenReturn(transacoes);
		when(transacaoConverter.entidadeParaDTO(transacao)).thenReturn(transacaoDTO);
		when(transacaoConverter.entidadeParaDTO(transacao1)).thenReturn(transacaoDTO1);
		
		List<TransacaoDTO> resultado = transacaoService.listarPorPeriodo(LocalDate.of(2025, 5, 7), LocalDate.of(2025, 5, 8));
		
		assertNotNull(resultado);
		assertEquals(2, resultado.size()); //Confere se a lista possui 2, agora ela terá 2.
		assertEquals(transacaoDTO.getIdTransacao(), resultado.get(0).getIdTransacao());
		assertEquals(transacaoDTO1.getIdTransacao(), resultado.get(1).getIdTransacao());
		
		assertEquals(transacaoDTO.getTipoTransacao(), resultado.get(0).getTipoTransacao());
		assertEquals(transacaoDTO1.getTipoTransacao(), resultado.get(1).getTipoTransacao());
		
		assertEquals(transacaoDTO.getValorMovimentado(), resultado.get(0).getValorMovimentado());
		assertEquals(transacaoDTO1.getValorMovimentado(), resultado.get(1).getValorMovimentado());
		
		verify(transacaoRepository, times(1)).findByDataTransacaoBetween(LocalDate.of(2025, 5, 7), LocalDate.of(2025, 5, 8));
		verify(transacaoConverter, times(1)).entidadeParaDTO(transacao);
		verify(transacaoConverter, times(1)).entidadeParaDTO(transacao1);
	}
	
	@Test
	void testListarPorTipoComSucesso() {
		List<Transacao> transacoes = List.of(transacao, transacao1);
		
		when(transacaoRepository.findByTipoTransacao(TipoTransacao.TRANSFERENCIA)).thenReturn(transacoes);
		when(transacaoConverter.entidadeParaDTO(transacao)).thenReturn(transacaoDTO);
		when(transacaoConverter.entidadeParaDTO(transacao1)).thenReturn(transacaoDTO1);
		
		List<TransacaoDTO> resultado = transacaoService.listarPorTipo(TipoTransacao.TRANSFERENCIA);
		
		assertNotNull(resultado);
		assertEquals(2, resultado.size()); //Confere se a lista possui 2, agora ela terá 2.
		assertEquals(transacaoDTO.getIdTransacao(), resultado.get(0).getIdTransacao());
		assertEquals(transacaoDTO1.getIdTransacao(), resultado.get(1).getIdTransacao());
		
		assertEquals(transacaoDTO.getTipoTransacao(), resultado.get(0).getTipoTransacao());
		assertEquals(transacaoDTO1.getTipoTransacao(), resultado.get(1).getTipoTransacao());
		
		verify(transacaoRepository, times(1)).findByTipoTransacao(TipoTransacao.TRANSFERENCIA);
		verify(transacaoConverter, times(1)).entidadeParaDTO(transacao);
		verify(transacaoConverter, times(1)).entidadeParaDTO(transacao1);
	}
	
	@Test
	void testRealizarTransferenciaComSucesso() {
		BigDecimal valor = new BigDecimal("750.00");
		Long contaOrigemId = 1L;
		Long contaDestinoId = 2L;
		
		conta1.setSaldoAtual(new BigDecimal("3000.00"));
		conta2.setSaldoAtual(new BigDecimal("2000.00"));
		
		when(contaRepository.findById(contaOrigemId)).thenReturn(Optional.of(conta1));
		when(contaRepository.findById(contaDestinoId)).thenReturn(Optional.of(conta2));
		
		Transacao transacao = new Transacao();
		transacao.setIdTransacao(10L);
		transacao.setContaOrigem(conta1);
		transacao.setContaDestino(conta2);
		transacao.setValorMovimentado(valor);
		transacao.setDataTransacao(LocalDateTime.now());
		transacao.setTipoTransacao(TipoTransacao.TRANSFERENCIA);
		
		when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacao);
		
		Transacao resultado = transacaoService.realizarTransferencia(contaOrigemId, contaDestinoId, valor);
		
		assertNotNull(resultado);
		assertEquals(valor, resultado.getValorMovimentado());
		assertEquals(TipoTransacao.TRANSFERENCIA, resultado.getTipoTransacao());
		assertEquals(conta1, resultado.getContaOrigem());
		assertEquals(conta2, resultado.getContaDestino());
		
		assertEquals(new BigDecimal("2250.00"), conta1.getSaldoAtual());
		assertEquals(new BigDecimal("2750.00"), conta2.getSaldoAtual());
		
		verify(contaRepository, times(1)).findById(contaOrigemId);
		verify(contaRepository, times(1)).findById(contaDestinoId);
		verify(transacaoRepository, times(1)).save(any(Transacao.class));
		


	}
}
