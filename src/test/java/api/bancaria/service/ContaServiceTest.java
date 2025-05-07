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
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import api.bancaria.model.Cliente;
import api.bancaria.model.Conta;
import api.bancaria.model.StatusConta;
import api.bancaria.model.TipoConta;
import api.bancaria.repository.ClienteRepository;
import api.bancaria.repository.ContaRepository;

class ContaServiceTest {
	
	@Mock
	private ContaRepository contaRepository;
	
	@Mock
	private ClienteRepository clienteRepository;
	
	@InjectMocks
	private ContaService contaService;
	
	private Cliente cliente;
	private Conta conta;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		cliente = new Cliente();
		cliente.setIdCliente(1L);
		cliente.setNome("Jacques");
		cliente.setCpf("12345678900");
		cliente.setDataNascimento(LocalDate.of(1992, 4, 30));
		cliente.setEmail("jaques@email.com");
		cliente.setTelefone("21999999999");
		cliente.setEndereco("Rua Teste, 333");	
		
		conta = new Conta();
		conta.setIdConta(1L);
		conta.setAgencia("0001");
		conta.setSaldoAtual(new BigDecimal("3000.00"));
		conta.setTipoConta(TipoConta.CORRENTE);
		conta.setStatusConta(StatusConta.ATIVA);
		conta.setCliente(cliente);
	}
	
	@Test
	void testSalvarContaComSucesso() {
		
		Conta conta = new Conta();
		conta.setIdConta(2L);
		conta.setAgencia("0001");
		conta.setSaldoAtual(new BigDecimal("5000.00"));
		conta.setTipoConta(TipoConta.SALARIO);
		conta.setStatusConta(StatusConta.ATIVA);
		conta.setCliente(cliente);
		
		when(contaRepository.save(any(Conta.class))).thenReturn(conta);
		
		Conta resultado = contaService.salvar(conta);
		
		assertNotNull(resultado);
		assertEquals(2L, resultado.getIdConta());
		assertEquals("0001", resultado.getAgencia());
		assertEquals(new BigDecimal("5000.00"), resultado.getSaldoAtual());
		assertEquals(TipoConta.SALARIO, resultado.getTipoConta());
		assertEquals(StatusConta.ATIVA, resultado.getStatusConta());
		assertEquals(cliente, resultado.getCliente());
		
		verify(contaRepository, times(1)).save(conta);
	}
	
	@Test
	void testObterPorIdComSucesso() {
		when(contaRepository.findById(2L)).thenReturn(Optional.of(conta));
		
		Optional<Conta> resultado = contaService.obterPorId(2L);
		
		assertTrue(resultado.isPresent());
		assertEquals(conta.getIdConta(), resultado.get().getIdConta());
		assertEquals(conta.getAgencia(), resultado.get().getAgencia());
		assertEquals(conta.getSaldoAtual(), resultado.get().getSaldoAtual());
		assertEquals(conta.getTipoConta(), resultado.get().getTipoConta());
		assertEquals(conta.getStatusConta(), resultado.get().getStatusConta());
		assertEquals(conta.getCliente(), resultado.get().getCliente());
		
		verify(contaRepository, times(1)).findById(2L);
	}
	
	@Test
	void testAtualizarSaldoComSucesso() {
		Long id = conta.getIdConta();
		BigDecimal novoSaldo = new BigDecimal("4500.00"); //Testar não encontrado, trocar pra null ou "".
		
		Conta contaAtualizada = new Conta();
		contaAtualizada.setIdConta(id);
		contaAtualizada.setAgencia(conta.getAgencia());
		contaAtualizada.setSaldoAtual(novoSaldo);
		contaAtualizada.setTipoConta(conta.getTipoConta());
		contaAtualizada.setStatusConta(conta.getStatusConta());
		contaAtualizada.setCliente(conta.getCliente());
		
		when(contaRepository.findById(id)).thenReturn(Optional.of(conta));
		when(contaRepository.save(any(Conta.class))).thenReturn(contaAtualizada);
		
		Conta resultado = contaService.atualizarSaldo(id, novoSaldo);
		
		assertNotNull(resultado);
		assertEquals(novoSaldo, resultado.getSaldoAtual());
		
		verify(contaRepository).findById(id);
		verify(contaRepository).save(conta);
	}
	
	@Test
	void testAtualizarStatusComSucesso() {
		Long id = conta.getIdConta();
		StatusConta novoStatus = StatusConta.INATIVA;
		
		Conta contaAtualizada = new Conta();
		contaAtualizada.setIdConta(id); // Numero inexistente de ID, throw.
		contaAtualizada.setAgencia(conta.getAgencia());
		contaAtualizada.setSaldoAtual(conta.getSaldoAtual());
		contaAtualizada.setTipoConta(conta.getTipoConta());
		contaAtualizada.setStatusConta(novoStatus); // Se for igual avisa por log.
		contaAtualizada.setCliente(conta.getCliente());
		
		when(contaRepository.findById(id)).thenReturn(Optional.of(conta));
		when(contaRepository.save(any(Conta.class))).thenReturn(contaAtualizada);
		
		Conta resultado = contaService.alterarStatus(id, novoStatus);
		
		assertNotNull(resultado);
		assertEquals(novoStatus, resultado.getStatusConta());
		
		verify(contaRepository).findById(id);
		verify(contaRepository).save(conta);
	}
	
	@Test
	void testBuscarContasClienteComSucesso() {
		List<Conta> contas = List.of(conta);
		
		when(contaRepository.findByCliente_IdCliente(1L)).thenReturn(contas); //Testar não encontrado, trocar pra null ou "".
		
		List<Conta> resultado = contaService.buscarPorCliente(1L);
		
		assertNotNull(resultado);
		assertEquals(1, resultado.size());
		assertEquals(conta, resultado.get(0)); //Comparando conta ja criado com a conta que vem do teste.(posição 0) = primeiro da lista
		
		verify(contaRepository, times(1)).findByCliente_IdCliente(1L);
	}
	
	@Test
	void testBuscarClientePorId() {
		Long idCliente = 1L;
		
		when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(cliente));
		
		Cliente resultado = contaService.buscarClientePorId(1L); //Testar não encontrado, trocar pra null ou "".
		
		assertNotNull(resultado);
		assertEquals(cliente, resultado);
		verify(clienteRepository, times(1)).findById(idCliente);
	}

}
