package api.bancaria.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import api.bancaria.model.Usuario;
import api.bancaria.service.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtCustomAuthenticationFilter extends OncePerRequestFilter {
	
	private final UsuarioService usuarioService;
	
	public JwtCustomAuthenticationFilter(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
			
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(deveConverter(authentication)) {
			String login = authentication.getName();
			Optional<Usuario> usuarioOptional = usuarioService.buscarPorLogin(login);
			
			usuarioOptional.ifPresent(usuario -> {
				Authentication newAuthentication = new CustomAuthentication(usuario);
				SecurityContextHolder.getContext().setAuthentication(newAuthentication);	
		});
	}
		
		filterChain.doFilter(request, response);
	}
	
	private boolean deveConverter(Authentication authentication) {
		return authentication != null && authentication instanceof JwtAuthenticationToken;
	}


}
