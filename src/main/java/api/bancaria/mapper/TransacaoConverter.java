package api.bancaria.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import api.bancaria.dto.TransacaoDTO;
import api.bancaria.model.Conta;
import api.bancaria.model.Transacao;

@Component
public class TransacaoConverter { 
	//entidadeParaDTO já tem a fonte de dados completa e pode transferir diretamente para o novo objeto,
	//dtoParaEntity é construido um novo objeto a partir de uma fonte
	//de dados separada, exigindo a leitura(get) e atribuição(set)de cada valor individualmente.
	
	public Transacao dtoParaEntidade(TransacaoDTO transacaoDTO) {
		if (Objects.isNull(transacaoDTO)) {
			return null;
		}
		
		Transacao transacao = new Transacao();
		transacao.setIdTransacao(transacaoDTO.getIdTransacao());
		transacao.setTipoTransacao(transacaoDTO.getTipoTransacao());
		transacao.setValorMovimentado(transacaoDTO.getValorMovimentado());
		transacao.setDataTransacao(transacaoDTO.getDataTransacao());
		
		if (transacaoDTO.getContaOrigemId() != null) { //Necessário por referenciar outra entidade.(Conta)
			Conta contaOrigem = new Conta();	//Criar objeto da Conta e atribuir o ID dele.
			contaOrigem.setIdConta(transacaoDTO.getContaOrigemId());
			transacao.setContaOrigem(contaOrigem);
		}
		
		if(transacaoDTO.getContaDestinoId() != null) { // Necessário por referenciar outra entidade(Conta)
			Conta contaDestino = new Conta();   //Criar objeto da Conta e atribuir o ID dele.
			contaDestino.setIdConta(transacaoDTO.getContaDestinoId());
			transacao.setContaDestino(contaDestino);
		}
		
		return transacao;
	}
	
	public TransacaoDTO entidadeParaDTO(Transacao transacao) {
		if (Objects.isNull(transacao)) {
			return null;
		}
		
		return new TransacaoDTO(
				transacao.getIdTransacao(),
				transacao.getTipoTransacao(),
				transacao.getValorMovimentado(),
				transacao.getDataTransacao(),
				Objects.nonNull(transacao.getContaOrigem()) ? transacao.getContaOrigem().getIdConta() : null,
				Objects.nonNull(transacao.getContaDestino()) ? transacao.getContaDestino().getIdConta(): null
				);
	}
}
