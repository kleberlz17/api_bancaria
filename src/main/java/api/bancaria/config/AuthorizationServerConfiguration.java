package api.bancaria.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import api.bancaria.security.CustomAuthentication;

@Configuration
@EnableWebSecurity
public class AuthorizationServerConfiguration {
	
	@Bean
	@Order(1) // Configuração pra habilitar o Authorization Server
	public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
		
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
				.oidc(Customizer.withDefaults());
        
        http.oauth2ResourceServer(oauth2Rs -> oauth2Rs.jwt(Customizer.withDefaults()));
        
        http.formLogin(configurer -> configurer.loginPage("/login"));
        
        return http.build();	
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}
	
	@Bean
	public TokenSettings tokenSettings() {
		return TokenSettings.builder()
				.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)// access_token: token utilizado nas requisições
				.accessTokenTimeToLive(Duration.ofMinutes(60))// access_token: token utilizado nas requisições
				
				.refreshTokenTimeToLive(Duration.ofMinutes(90))// refresh_token: token para renovar o access_token
				.build();
	}
	
	@Bean
	public ClientSettings clientSettings() {
		return ClientSettings.builder()
				.requireAuthorizationConsent(false)
				.build();	
	}
	
	@Bean
	public JWKSource<SecurityContext> jwkSource() throws Exception { // JSON Web Key - autenticação e autorização
		RSAKey rsaKey = gerarChaveRSA();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return new ImmutableJWKSet<>(jwkSet);	
	}
	
	private RSAKey gerarChaveRSA() throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048); //2048 bits.
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		
		RSAPublicKey chavePublica = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey chavePrivada = (RSAPrivateKey) keyPair.getPrivate();
		
		return new RSAKey
				.Builder(chavePublica)
				.privateKey(chavePrivada)
				.keyID(UUID.randomUUID().toString())
				.build();
	}
	
	@Bean
	public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}
	
	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder()
				.tokenEndpoint("/oauth2/token") // obter token
				.tokenIntrospectionEndpoint("/oauth2/introspect") // para consultar status do token
				.tokenRevocationEndpoint("/oauth2/revoke") // revogar
				.authorizationEndpoint("/oauth2/authorize") // authorization endpoint
				.oidcUserInfoEndpoint("/oauth2/userinfo") // informações do usuario OPEN ID CONNECT
				.jwkSetEndpoint("/oauth2/jwks") // obter a chave publica para verificar a assinatura do token
				.oidcLogoutEndpoint("/oauth2/logout") // logout
				.build();
	}
	
	@Bean
	public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
		return context -> {
			var principal = context.getPrincipal();
			
			if(principal instanceof CustomAuthentication authentication) {  // Vê se é uma Custom Authentication(Minha)
				OAuth2TokenType tipoToken = context.getTokenType();
				
				if(OAuth2TokenType.ACCESS_TOKEN.equals(tipoToken)) { // Vê se é um Access Token
					Collection<GrantedAuthority> authorities = authentication.getAuthorities();
					List<String> authoritiesList = 
							authorities.stream().map(GrantedAuthority::getAuthority).toList();// Convertendo para String pra incluir no token JWT
					context.getClaims() // Reivindica(claim) as authorities pra dentro do contexto.
					.claim("authorities", authoritiesList)
					.claim("email", authentication.getUsuario().getEmail());
				}
			}
		};
	}
}
