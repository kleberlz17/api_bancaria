package api.bancaria.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import api.bancaria.model.TipoTransacao;
import api.bancaria.model.Transacao;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
	
	Optional<Transacao> findById(Long idTransacao);
	
	List<Transacao> findByValorMovimentado(BigDecimal valorMovimentado);
	
	List<Transacao> findByDataTransacao(LocalDateTime dataTransacao);
	
	List<Transacao> findByContaOrigemIdOrContaDestinoId(Long contaOrigemId, Long contaDestinoId);
	
	List<Transacao> findByDataTransacaoBetween(LocalDateTime transacaoInicio, LocalDateTime transacaoFim);
	
	List<Transacao> findByTipoTransacao(TipoTransacao tipoTransacao);
}
