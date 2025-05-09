package api.bancaria.controller;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import api.bancaria.dto.ClienteDTO;
import api.bancaria.dto.NovoEmailDTO;
import api.bancaria.dto.NovoEnderecoDTO;
import api.bancaria.dto.NovoTelefoneDTO;
import api.bancaria.model.Cliente;
import api.bancaria.repository.ClienteRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional //Cada teste roda numa transação isolada pra evitar conflitos.
@Rollback //Desfaz todas as alterações feitas no banco de dados ao final do teste.
class ClienteControllerIntegrationTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;// pra converter objetos em JSON
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	private Long idCliente; //Todos aqui são necessários pro Get pq o banco está em CREATE DROP(cria e apaga)
	
	@BeforeEach
	void setUp() {
		Cliente cliente = new Cliente(
				"Arno Dorian",
				"66666666666",
				LocalDate.of(1999, 4, 28),
				"arnodorian@email.com",
				"21977777777",
				"Rua Testar, 77");
		Cliente clienteSalvo = clienteRepository.save(cliente);	
		idCliente = clienteSalvo.getIdCliente(); //Necessário pro Get pq o banco está em CREATE DROP(cria e apaga)
	}
	
	@Test
	void testSalvarClienteComSucesso() throws Exception {
		ClienteDTO clienteDTO = new ClienteDTO();
		clienteDTO.setNome("Juliana");
		clienteDTO.setCpf("55555555555");
		clienteDTO.setDataNascimento(LocalDate.of(2000, 3, 20));
		clienteDTO.setEmail("juliana@email.com");
		clienteDTO.setTelefone("21988888888");
		clienteDTO.setEndereco("Rua Teste de Integração, 365");
		
		mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/clientes/")));	
	}
	
	@Test
	void testBuscarPorIdComSucesso() throws Exception {
		mockMvc.perform(get("/clientes/{id}", idCliente))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nome").value("Arno Dorian"))
        .andExpect(jsonPath("$.cpf").value("66666666666"));			
	}
	
	@Test
	void testBuscarPorNomeComSucesso() throws Exception {
		
		String nome = "Arno Dorian";
		
		mockMvc.perform(get("/clientes/nome/{nome}", nome))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].email").value("arnodorian@email.com"))//Lidando com List, devo buscar o primeiro(indice 0) já que só terá 1 mesmo gerado na hora. Depende da necessidade do teste
		.andExpect(jsonPath("$[0].cpf").value("66666666666"));
	}
	
	@Test
	void testBuscarPorCpfComSucesso() throws Exception {
		
		String cpf = "66666666666";
		
		mockMvc.perform(get("/clientes/cpf/{cpf}", cpf))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.nome").value("Arno Dorian"))
		.andExpect(jsonPath("$.email").value("arnodorian@email.com"));
	}
	
	@Test
	void testBuscarPorNomeAndCpfAndNascimentoComSucesso() throws Exception {
		
		String nome = "Arno Dorian";
		String cpf = "66666666666";
		LocalDate dataNascimento = LocalDate.of(1999, 4, 28);
		
		mockMvc.perform(get("/clientes/dadoscliente/{nome}/{cpf}/{dataNascimento}", nome, cpf, dataNascimento))
		.andExpect(status().isOk());
	}
	
	@Test
	void testAlterarEmailComSucesso() throws Exception {
		NovoEmailDTO novoEmailDto = new NovoEmailDTO();
		novoEmailDto.setEmail("dorian@email.com");
		
		mockMvc.perform(put("/clientes/{idCliente}/novoemail", idCliente)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(novoEmailDto)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.email").value("dorian@email.com"));
	}
	
	@Test
	void testAlterarTelefoneComSucesso() throws Exception {
		NovoTelefoneDTO novoTelefoneDto = new NovoTelefoneDTO();
		novoTelefoneDto.setTelefone("21933333333");
		
		mockMvc.perform(put("/clientes/{idCliente}/novotelefone", idCliente)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(novoTelefoneDto)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.telefone").value("21933333333"));
	}
	
	@Test
	void testAlterarEnderecoComSucesso() throws Exception {
		NovoEnderecoDTO novoEnderecoDto = new NovoEnderecoDTO();
		novoEnderecoDto.setEndereco("Rua Já Testado");
		
		mockMvc.perform(put("/clientes/{idCliente}/novoendereco", idCliente)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(novoEnderecoDto)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.endereco").value("Rua Já Testado"));
	}

	

}
