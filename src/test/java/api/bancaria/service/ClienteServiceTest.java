package api.bancaria.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import api.bancaria.model.Cliente;
import api.bancaria.repository.ClienteRepository;
import api.bancaria.validator.ClienteValidator;

class ClienteServiceTest {

	@Mock
	private ClienteRepository clienteRepository;

	@Mock
	private ClienteValidator clienteValidator;

	@InjectMocks
	private ClienteService clienteService;
	
	private Cliente cliente;
	
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
	}
	
	@Test
	void testSalvarClienteComSucesso() {
		
		Cliente cliente = new Cliente();
		cliente.setNome("Berman");
		cliente.setCpf("77777777777");//Testar null ou vazio, só tirar.
		cliente.setEmail("berman@email.com");//Testar null ou vazio, só tirar.
		cliente.setTelefone("21977777777");
		cliente.setEndereco("Rua dos Testadores, 777");
		
		// Simulando comportamento do clienteRepository.
		when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
		
		// Chamando o método salvar.
		Cliente resultado = clienteService.salvar(cliente);
		
		// Verificações.
		assertNotNull(resultado);
		assertEquals(cliente.getCpf(), resultado.getCpf());
		assertEquals(cliente.getEmail(), resultado.getEmail());
		assertEquals(cliente.getNome(), resultado.getNome());
		verify(clienteRepository).save(cliente);
		
		verify(clienteValidator).validarCliente(cliente);
	}
	
	@Test
	void testBuscarPorId() {
		when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
		
		Optional<Cliente> resultado = clienteService.buscarPorId(1L);
		
		assertTrue(resultado.isPresent());
		assertEquals(cliente.getIdCliente(), resultado.get().getIdCliente());
		verify(clienteRepository, times(1)).findById(1L);	
	}

	@Test
	void testBuscarPorNome() {
		List<Cliente> clientes = List.of(cliente);
		
		when(clienteRepository.findByNomeContainingIgnoreCase("jac")).thenReturn(clientes);
		
		List<Cliente> resultado = clienteService.buscarPorNome("jac");
		
		assertNotNull(resultado);
		assertEquals(1, resultado.size());//Espero que a lista tenha 1 elemento.
		assertEquals(cliente.getNome(), resultado.get(0).getNome());//Comparando meu cliente ja criado com o cliente que vem do teste.(posição 0) = primeiro da lista
		verify(clienteRepository, times(1)).findByNomeContainingIgnoreCase("jac");		
	}
	
	@Test
	void testBuscarPorCpf() {
		when(clienteRepository.findByCpfContainingIgnoreCase("12345678900")).thenReturn(Optional.of(cliente));
		
		Optional<Cliente> resultado = clienteService.buscarPorCpf("12345678900");
		
		assertTrue(resultado.isPresent());
		assertEquals(cliente.getCpf(), resultado.get().getCpf());
		verify(clienteRepository, times(1)).findByCpfContainingIgnoreCase("12345678900");
	}

	@Test
	void testbuscarPorNomeAndCpfAndDataNascimento() {
		String nome = "jacques";
		String cpf = "12345678900";
		LocalDate dataNascimento = LocalDate.of(1992, 4, 30);
		
		when(clienteRepository.findByNomeContainingIgnoreCaseAndCpfContainingIgnoreCaseAndDataNascimento(nome, cpf, dataNascimento))
			.thenReturn(Optional.of(cliente));
		
		Optional<Cliente> resultado = clienteService.buscarPorNomeAndCpfAndDataNascimento(nome, cpf, dataNascimento);
		
		assertTrue(resultado.isPresent());
		verify(clienteRepository, times(1)).findByNomeContainingIgnoreCaseAndCpfContainingIgnoreCaseAndDataNascimento(nome, cpf, dataNascimento);
	}
	
	@Test
	void testAlterarEmailComSucesso() {
		
		Long id = cliente.getIdCliente();
		String emailNovo = "emailnovo@email.com";//Testar vazio ou nulo é só colocar "" ou null.
		
		when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));
		when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
		
		Cliente resultado = clienteService.alterarEmail(id, emailNovo);
		
		assertNotNull(resultado);
		assertEquals(emailNovo, resultado.getEmail());
		verify(clienteValidator).validarEmail(cliente);
		verify(clienteRepository).save(cliente);
	}
	
	@Test
	void testAlterarTelefoneComSucesso() {
		Long id = cliente.getIdCliente();
		String telefoneNovo = "21888888888"; //Testar vazio ou nulo é só colocar "" ou null.
		
		when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));
		when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
		
		Cliente resultado = clienteService.alterarTelefone(id, telefoneNovo);
		
		assertNotNull(resultado);
		assertEquals(telefoneNovo, resultado.getTelefone());
		verify(clienteValidator).validarTelefone(cliente);
		verify(clienteRepository).save(cliente);
	}
	
	@Test
	void testAlterarEnderecoComSucesso() {
		Long id = cliente.getIdCliente();
		String enderecoNovo = "Rua TestadoraTests, 777";
		
		when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));
		when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
		
		Cliente resultado = clienteService.alterarEndereco(id, enderecoNovo);
		
		assertNotNull(resultado);
		assertEquals(enderecoNovo, resultado.getEndereco());
		verify(clienteRepository).save(cliente);
		
	}
	
	


}
