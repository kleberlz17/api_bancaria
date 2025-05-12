package api.bancaria.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import api.bancaria.model.Role;
import api.bancaria.model.Usuario;
import api.bancaria.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginSocialSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	private static final String SENHA_PADRAO = "123";
	private final UsuarioService usuarioService;
	
	public LoginSocialSuccessHandler(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		
		OAuth2AuthenticationToken auth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
		OAuth2User oAuth2User = auth2AuthenticationToken.getPrincipal();
		
		String email = oAuth2User.getAttribute("email");
		Usuario usuario = usuarioService.buscarPorEmail(email).orElseGet(() -> cadastrarUsuarioNaBase(email));
		
		authentication = new CustomAuthentication(usuario);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
	private Usuario cadastrarUsuarioNaBase(String email) {
		Usuario usuario;
		usuario = new Usuario();
		usuario.setEmail(email);
		usuario.setLogin(ObterLoginApartirEmail(email));
		usuario.setSenha(SENHA_PADRAO);
		
		Role operadorRole = new Role();
		operadorRole.setNome("OPERADOR");
		
		usuario.setRoles(List.of(operadorRole));
		usuarioService.salvar(usuario);	
		return usuario;
}
	
	private String ObterLoginApartirEmail(String email) {
		return email.substring(0, email.indexOf('@'));//Login gerado antes do @ do email, ex kleberluiz@email.com - será kleberluiz
	}
	
}
