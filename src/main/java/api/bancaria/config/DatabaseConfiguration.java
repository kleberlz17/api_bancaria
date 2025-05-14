package api.bancaria.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class DatabaseConfiguration {
	
	@Value("${DB_URL}")
	private String url;
	
	@Value("${DB_USERNAME}")
	private String username;
	
	@Value("${DB_PASSWORD}")
	private String password;
	
//	@Bean  // Este método é apenas para consulta futura.
    // Não possui @Bean para evitar conflitos com a configuração automática do Spring Boot.
	public DataSource hikariDataSource() {
		
		log.info("Iniciando conexão com o banco na URL: {}", url);
		
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(url);
		config.setUsername(username);
		config.setPassword(password);
		
		config.setMaximumPoolSize(10);// maximo de conexões liberadas
		config.setMinimumIdle(1);// tamanho inicial do pool
		config.setPoolName("bancaria-db-pool");
		config.setMaxLifetime(600000);// 600 mil ms (10 minutos)
		config.setConnectionTimeout(100000);// timeout para conseguir uma conexão
		config.setConnectionTestQuery("select 1");// query de teste
		
		return new HikariDataSource(config);	
	}
}
