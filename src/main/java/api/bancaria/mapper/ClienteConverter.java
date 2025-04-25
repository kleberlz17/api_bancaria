package api.bancaria.mapper;

import org.springframework.stereotype.Component;

import api.bancaria.dto.ClienteDTO;
import api.bancaria.model.Cliente;

@Component
public class ClienteConverter {
	
	//entidadeParaDTO já tem a fonte de dados completa e pode transferir diretamente para o novo objeto,
	//dtoParaEntity é construido um novo objeto a partir de uma fonte
	//de dados separada, exigindo a leitura(get) e atribuição(set)de cada valor individualmente.
	public static Cliente dtoParaEntidade(ClienteDTO dto) {
		Cliente cliente = new Cliente();
		cliente.setIdCliente(dto.getIdCliente());
		cliente.setNomeCompleto(dto.getNomeCompleto());
		cliente.setCpf(dto.getCpf());
		cliente.setDataNascimento(dto.getDataNascimento());
		cliente.setEmail(dto.getEmail());
		cliente.setTelefone(dto.getTelefone());
		cliente.setEndereco(dto.getEndereco());
		return cliente;
	}
	
	public static ClienteDTO entidadeParaDto(Cliente entidade) {
		return new ClienteDTO(
				entidade.getIdCliente(),
				entidade.getNomeCompleto(),
				entidade.getCpf(),
				entidade.getDataNascimento(),
				entidade.getEmail(),
				entidade.getTelefone(),
				entidade.getEndereco()
				);
				
	}

}
