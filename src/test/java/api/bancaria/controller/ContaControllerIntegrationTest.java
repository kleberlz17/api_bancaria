package api.bancaria.controller;

import java.math.BigDecimal;
import java.time.LocalDate;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import com.fasterxml.jackson.databind.ObjectMapper;
import api.bancaria.dto.ContaDTO;
import api.bancaria.dto.NovoSaldoDTO;
import api.bancaria.dto.NovoStatusDTO;
import api.bancaria.model.Cliente;
import api.bancaria.model.Conta;
import api.bancaria.model.StatusConta;
import api.bancaria.model.TipoConta;
import api.bancaria.repository.ClienteRepository;
import api.bancaria.repository.ContaRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional //Cada teste roda numa transação isolada pra evitar conflitos.
@Rollback //Desfaz todas as alterações feitas no banco de dados ao final do teste.
class ContaControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ContaRepository contaRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	private Long idCliente;
	private Long idConta; //É necessário pro Get pq o banco está em CREATE DROP(cria e apaga)
	
	@BeforeEach
	void setUp() {
		Cliente cliente = new Cliente(
				"Jack Skellington",
				"33333333333",
				LocalDate.of(1993, 12, 24),
				"jack@email.com",
				"2195555555",
				"Halloween City");
		
		Cliente clienteSalvo = clienteRepository.save(cliente);
		idCliente = clienteSalvo.getIdCliente();
		
		Conta conta = new Conta(
				"0001",
				new BigDecimal("3000.00"),
				TipoConta.CORRENTE,
				StatusConta.ATIVA,
				cliente);		
		
		Conta contaSalva = contaRepository.save(conta);
		idConta = contaSalva.getIdConta();
	}
	
	@Test
	void testSalvarComSucesso() throws Exception {
		ContaDTO contaDto = new ContaDTO();
		contaDto.setAgencia("0001");
		contaDto.setSaldoAtual(new BigDecimal("1000.00"));
		contaDto.setTipoConta(TipoConta.POUPANCA);
		contaDto.setStatusConta(StatusConta.ATIVA);
		contaDto.setClienteId(idCliente);;
		
		mockMvc.perform(post("/contas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(contaDto)))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", containsString("/contas/")));
	}
	
	@Test
	void testObterPorIdComSucesso() throws Exception {
		mockMvc.perform(get("/contas/{idConta}", idConta))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.idConta").value(idConta))
		.andExpect(jsonPath("$.agencia").value("0001"));
	}
	
	@Test
	void testAtualizarSaldoComSucesso() throws Exception {
		NovoSaldoDTO novoSaldoDto = new NovoSaldoDTO();
		novoSaldoDto.setNovoSaldo(new BigDecimal("5000.00"));
		
		mockMvc.perform(put("/contas/{idConta}/saldo", idConta)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(novoSaldoDto)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.saldoAtual").value(5000.00));	
	}
	
	@Test
	void testAlterarStatusComSucesso() throws Exception {
		NovoStatusDTO novoStatusDto = new NovoStatusDTO();
		novoStatusDto.setStatusConta(StatusConta.BLOQUEADA);
		
		mockMvc.perform(put("/contas/{idConta}/status", idConta)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(novoStatusDto)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.statusConta").value("BLOQUEADA"));
	}
	
	@Test
	void testBuscarContasPorClienteComSucesso() throws Exception {
		mockMvc.perform(get("/contas/{idCliente}/contascliente", idCliente))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));//Verifica se a resposta JSON é uma lista(array) e se tem pelo menos 1 elemento.
	}
	
	@Test
	void testBuscarClientePorIdComSucesso() throws Exception {
		mockMvc.perform(get("/contas/{idCliente}/cliente", idCliente))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nome").value("Jack Skellington"))
				.andExpect(jsonPath("$.cpf").value("33333333333"))
				.andExpect(jsonPath("$.email").value("jack@email.com"));
	}
	
	
}
