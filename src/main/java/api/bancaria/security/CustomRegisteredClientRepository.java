package api.bancaria.security;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

import api.bancaria.service.ClientService;

@Component
public class CustomRegisteredClientRepository implements RegisteredClientRepository {
	
	private final ClientService clientService;
	private final TokenSettings tokenSettings;
	private final ClientSettings clientSettings;
	
	public CustomRegisteredClientRepository(ClientService clientService, TokenSettings tokenSettings, ClientSettings clientSettings) {
		this.clientService = clientService;
		this.tokenSettings = tokenSettings;
		this.clientSettings = clientSettings;
	}

	@Override
	public void save(RegisteredClient registeredClient) {
	}

	@Override
	public RegisteredClient findById(String id) {
		return null;
	}

	@Override
	public RegisteredClient findByClientId(String clientId) {
		var client = clientService.obterPorClientID(clientId);
		
		if(client == null) {
			return null;
		}
		
		RegisteredClient.Builder builder = RegisteredClient
			    .withId(client.getId().toString())
			    .clientId(client.getClientId())
			    .clientSecret(client.getClientSecret())
			    .redirectUri(client.getRedirectUri())
			    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
			    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			    .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
			    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
			    .tokenSettings(tokenSettings)
			    .clientSettings(clientSettings);

			// adiciona escopos individualmente:
			if (client.getScope() != null && !client.getScope().isBlank()) {
			    String[] scopes = client.getScope().split("\\s+");
			    for (String scope : scopes) {
			        builder.scope(scope.trim());
			    }
			}

			return builder.build();

	}

}
