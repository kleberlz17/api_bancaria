package api.bancaria.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import api.bancaria.model.Usuario;
import api.bancaria.service.UsuarioService;

public class CustomUserDetailsService implements UserDetailsService {
	
	private final UsuarioService usuarioService;
	
	public CustomUserDetailsService (UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		Usuario usuario = usuarioService.buscarPorLogin(login).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com login:" + login));
		
		return User.builder()
				.username(usuario.getLogin())
				.password(usuario.getSenha())
				.roles(usuario.getRoles().toArray(new String[usuario.getRoles().size()]))
				.build();	
	}
	
	

}
