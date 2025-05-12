package api.bancaria.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import api.bancaria.model.Usuario;

@SuppressWarnings("serial")
public class CustomAuthentication implements Authentication {
	
	private final Usuario usuario;
	
	public CustomAuthentication(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Collection<GrantedAuthority> getAuthorities() {
	//Pega as roles do Usuario, mapeia elas pro SimpleGrantedAuthority
		return this.usuario
				.getRoles()
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getNome()))
				.collect(Collectors.toList());	
	}
	
	public Usuario getUsuario() {
		return this.usuario;
	}

	@Override //Override é sobrescrito - Sobrescrevi os metodos do authentication.
	public String getName() {
		return usuario.getLogin();
	}
	
	@Override
	public Object getCredentials() {
		return null;
	}
	
	@Override
	public Object getDetails() {
		return usuario;
	}
	
	@Override
	public Object getPrincipal() {
		return usuario;
	}
	
	@Override
	public boolean isAuthenticated() {
		return true;
	}
	
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		
	}
	
	
	
	

}
