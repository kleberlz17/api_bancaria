package api.bancaria.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import api.bancaria.model.TipoTransacao;
import api.bancaria.model.Transacao;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
	
	Optional<Transacao> findById(Long idTransacao);
	
	List<Transacao> findByValorMovimentado(BigDecimal valorMovimentado);
	
	@Query("SELECT t FROM Transacao t WHERE FUNCTION('DATE', t.dataTransacao) = :dataTransacao") //Remove obrigatoridade de ter que colocar Hora completa, somente a data.
	List<Transacao> findByDataTransacaoOnly(@Param("dataTransacao") LocalDate dataTransacao);
	
	List<Transacao> findByContaOrigem_IdContaOrContaDestino_IdConta(Long contaOrigemId, Long contaDestinoId);

	@Query("SELECT t FROM Transacao t WHERE FUNCTION('DATE', t.dataTransacao) BETWEEN :inicio AND :fim") //Mesma coisa aqui.
	List<Transacao> findByDataTransacaoBetween(@Param("inicio") LocalDate inicio, @Param("fim")LocalDate fim);
	
	List<Transacao> findByTipoTransacao(TipoTransacao tipoTransacao);
}
