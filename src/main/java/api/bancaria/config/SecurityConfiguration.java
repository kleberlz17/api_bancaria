package api.bancaria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import api.bancaria.security.JwtCustomAuthenticationFilter;

@Configuration
@EnableWebSecurity // Habilitar config de segurança em nível global.
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true) // Habilitar segurança baseada em métodos(preauthorize, secured)
public class SecurityConfiguration {
	
	@Bean
	public SecurityFilterChain securityFilterChain(
			HttpSecurity http,
			JwtCustomAuthenticationFilter jwtCustomAuthenticationFilter) throws Exception {
		return http
				.csrf(AbstractHttpConfigurer::disable) // csrf desabilitado pq não é aplicação WEB
				.authorizeHttpRequests(authorize -> {
					authorize.requestMatchers("/login/**").permitAll();
					authorize.requestMatchers(HttpMethod.POST, "/usuarios/**").permitAll();
					authorize.anyRequest().authenticated();
				})
				.oauth2ResourceServer(oauth2 -> oauth2
						.jwt(jwt -> jwt
								.jwtAuthenticationConverter(jwtAuthenticationConverter())
						)
				)
				.addFilterAfter(jwtCustomAuthenticationFilter, BearerTokenAuthenticationFilter.class)
				.build();		
	}
	
	@Bean
	public GrantedAuthorityDefaults grantedAuthorityDefaults() { // REMOVE OBRIGATORIEDADE DE PREFIXO ROLE_
		return new GrantedAuthorityDefaults("");
	}
	
	@Bean // Evita a adição do prefixo ROLE_ as authorities que são
		 // permissões atribuidas ao usuário que está fazendo a requisição, e essas autoridades vêm do JWT TOKEN.
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		var authoritiesConverter = new JwtGrantedAuthoritiesConverter();
		authoritiesConverter.setAuthorityPrefix("");
		
		var converter = new JwtAuthenticationConverter();
		converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
		
		return converter;
		
	}

}
