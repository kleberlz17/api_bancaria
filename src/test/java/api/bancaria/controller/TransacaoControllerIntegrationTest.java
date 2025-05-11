package api.bancaria.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import api.bancaria.dto.TransacaoDTO;
import api.bancaria.model.Cliente;
import api.bancaria.model.Conta;
import api.bancaria.model.StatusConta;
import api.bancaria.model.TipoConta;
import api.bancaria.model.TipoTransacao;
import api.bancaria.model.Transacao;
import api.bancaria.repository.ClienteRepository;
import api.bancaria.repository.ContaRepository;
import api.bancaria.repository.TransacaoRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional //Cada teste roda numa transação isolada pra evitar conflitos.
@Rollback //Desfaz todas as alterações feitas no banco de dados ao final do teste.
class TransacaoControllerIntegrationTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private TransacaoRepository transacaoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ContaRepository contaRepository;
	
	private Long idTransacao; //É necessário pro Get pq o banco está em CREATE DROP(cria e apaga)
	@SuppressWarnings("unused")
	private Long idCliente1;
	@SuppressWarnings("unused")
	private Long idCliente2;
	private Long idContaDestino;
	private Long idContaOrigem;
	private Long idConta;
	private BigDecimal valorMovimentado;
	private LocalDate dataTransacao;
	private LocalDate inicio;
	private LocalDate fim;
	private TipoTransacao tipo;
	
	
	@BeforeEach
	void setUp() {
		Cliente cliente1 = new Cliente(
				"Jack Skellington",
				"33333333333",
				LocalDate.of(1993, 12, 24),
				"jack@email.com",
				"2195555555",
				"Halloween City");
		
		Cliente clienteSalvo = clienteRepository.save(cliente1);
		idCliente1 = clienteSalvo.getIdCliente();
		
		Cliente cliente2 = new Cliente(
				"Victor Van Dort",
				"44444444444",
				LocalDate.of(2005, 10, 21),
				"victor@email.com",
				"21966666666",
				"Halloween City");
		
		Cliente clienteSalvo2 = clienteRepository.save(cliente2);
		idCliente2 = clienteSalvo2.getIdCliente();
		
		Conta contaOrigem = new Conta(
				"0001",
				new BigDecimal("3000.00"),
				TipoConta.CORRENTE,
				StatusConta.ATIVA,
				cliente1);		
		
		Conta contaSalva = contaRepository.save(contaOrigem);
		idContaOrigem = contaSalva.getIdConta();
		
		Conta contaDestino = new Conta(
				"0001",
				new BigDecimal("4000.00"),
				TipoConta.CORRENTE,
				StatusConta.ATIVA,
				cliente2);		
		
		Conta contaSalva2 = contaRepository.save(contaDestino);
		idContaDestino = contaSalva2.getIdConta();
		
		Transacao transacao = new Transacao (
				TipoTransacao.TRANSFERENCIA,
				new BigDecimal("200.00"),
				LocalDateTime.of(2025, 5, 11, 20, 10),
				contaOrigem,
				contaDestino);
		
		Transacao transacao2 = new Transacao (
				TipoTransacao.DEPOSITO,
				new BigDecimal("300.00"),
				LocalDateTime.of(2025, 7, 12, 20, 10),
				contaOrigem,
				contaDestino);
		
		Transacao transacaoSalva = transacaoRepository.save(transacao);
		Transacao transacaoSalva2 = transacaoRepository.save(transacao2);
		
		idTransacao = transacaoSalva.getIdTransacao();
		valorMovimentado = transacaoSalva.getValorMovimentado();
		dataTransacao = transacaoSalva.getDataTransacao().toLocalDate();
		idConta = contaOrigem.getIdConta();
		
		inicio = transacaoSalva.getDataTransacao().toLocalDate();
		fim = transacaoSalva2.getDataTransacao().toLocalDate();
		tipo = transacaoSalva.getTipoTransacao();
	}
	
	@Test
	void testSalvarComSucesso() throws Exception {
		
		TransacaoDTO transacaoDto = new TransacaoDTO();
		transacaoDto.setTipoTransacao(TipoTransacao.TRANSFERENCIA);
		transacaoDto.setValorMovimentado(new BigDecimal("100.00"));
		transacaoDto.setDataTransacao(LocalDateTime.of(2025, 4, 3, 12, 30));
		transacaoDto.setContaOrigemId(idContaOrigem);
		transacaoDto.setContaDestinoId(idContaDestino);
	
		mockMvc.perform(post("/transacoes")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transacaoDto)))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", containsString("/transacoes/")));	
	}
	
	@Test
	void testObterPorIdComSucesso() throws Exception {
		mockMvc.perform(get("/transacoes/{idTransacao}", idTransacao))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.idTransacao").value(idTransacao))
		.andExpect(jsonPath("$.tipoTransacao").value("TRANSFERENCIA"))
		.andExpect(jsonPath("$.valorMovimentado").value(200.00));
	}
	
	@Test
	void testObterPorValorMovimentadoComSucesso() throws Exception {
		mockMvc.perform(get("/transacoes/valores/{valorMovimentado}", valorMovimentado))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].tipoTransacao").value("TRANSFERENCIA"))
		.andExpect(jsonPath("$[0].valorMovimentado").value(200.00));
	}
	
	@Test
	void testObterDataTransacaoComSucesso() throws Exception {
		mockMvc.perform(get("/transacoes/datas/{dataTransacao}", dataTransacao))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].tipoTransacao").value("TRANSFERENCIA"))
		.andExpect(jsonPath("$[0].dataTransacao").value("2025-05-11T20:10:00"));	
	}
	
	@Test
	void testListarTransacoesContaComSucesso() throws Exception {
		mockMvc.perform(get("/transacoes/transacoesconta/{idConta}", idConta))
		.andExpect(status().isOk());
	}
	
	@Test
	void testListarPorPeriodoComSucesso() throws Exception {
		mockMvc.perform(get("/transacoes/transacoesperiodo/{inicio}/{fim}", inicio, fim))
		.andExpect(status().isOk());
	}
	
	@Test
	void testListarPorTipoComSucesso() throws Exception {
		mockMvc.perform(get("/transacoes/tipostransacao/{tipo}", tipo ))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].tipoTransacao").value(tipo.toString()))
		.andExpect(jsonPath("$", hasSize(greaterThan(0)))); // Verifica se pelo menos uma transação foi retornada.
	}
	
	@Test
	void testTransferirComSucesso() throws Exception {
		TransacaoDTO transacaoDto1 = new TransacaoDTO();
		transacaoDto1.setTipoTransacao(TipoTransacao.TRANSFERENCIA);
		transacaoDto1.setValorMovimentado(new BigDecimal("1000.00"));
		transacaoDto1.setDataTransacao(LocalDateTime.of(2025, 10, 9, 11, 22));
		transacaoDto1.setContaOrigemId(idContaOrigem);
		transacaoDto1.setContaDestinoId(idContaDestino);
		
		mockMvc.perform(post("/transacoes/transferir")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transacaoDto1)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.valorMovimentado").value(Matchers.comparesEqualTo(1000.0)))// Garantir precisão do uso do BigDecimal.
				.andExpect(jsonPath("$.contaOrigemId").value(idContaOrigem))
				.andExpect(jsonPath("$.contaDestinoId").value(idContaDestino))
				.andExpect(jsonPath("$.tipoTransacao").value("TRANSFERENCIA"));
	}

}
