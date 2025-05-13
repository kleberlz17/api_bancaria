package api.bancaria.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import api.bancaria.model.Usuario;
import api.bancaria.service.UsuarioService;

@Component
public class SecurityService {
	
	@SuppressWarnings("unused")
	private final UsuarioService usuarioService;
	
	public SecurityService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	public Usuario obterUsuarioLogado() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication instanceof CustomAuthentication customAuth) {
			return customAuth.getUsuario();
		}
		
		return null;
	}

}
