package api.bancaria.mapper;

import org.springframework.stereotype.Component;

import api.bancaria.dto.ContaDTO;
import api.bancaria.model.Cliente;
import api.bancaria.model.Conta;

@Component
public class ContaConverter {
	//entidadeParaDTO já tem a fonte de dados completa e pode transferir diretamente para o novo objeto,
	//dtoParaEntity é construido um novo objeto a partir de uma fonte
	//de dados separada, exigindo a leitura(get) e atribuição(set)de cada valor individualmente.
	
	public Conta dtoParaEntidade(ContaDTO dto, Cliente cliente) {
		Conta conta = new Conta();
		conta.setIdConta(dto.getIdConta());
		conta.setAgencia(dto.getAgencia());
		conta.setSaldoAtual(dto.getSaldoAtual());
		conta.setTipoConta(dto.getTipoConta());
		conta.setStatusConta(dto.getStatusConta());
		conta.setCliente(cliente); // Associa o cliente encontrado a conta.
		return conta;
	}
	
	public ContaDTO entidadeParaDto(Conta entidade) {
		return new ContaDTO(
				entidade.getIdConta(),
				entidade.getAgencia(),
				entidade.getSaldoAtual(),
				entidade.getTipoConta(),
				entidade.getStatusConta(),
				entidade.getCliente().getIdCliente() //Aqui pega o ID do cliente Associado.
				);
	}

}
