package api.bancaria.mapper;

import org.springframework.stereotype.Component;

import api.bancaria.dto.UsuarioDTO;
import api.bancaria.model.Usuario;

@Component
public class UsuarioConverter {

	
	public Usuario dtoParaEntidade(UsuarioDTO dto) {
		Usuario usuario = new Usuario();
		usuario.setIdUser(dto.getIdUser());
		usuario.setLogin(dto.getLogin());
		usuario.setEmail(dto.getEmail());
		usuario.setSenha(dto.getSenha());
		usuario.setRoles(dto.getRoles());
		return usuario;
	}
	
	public UsuarioDTO entidadeParaDto(Usuario entidade) {
		return new UsuarioDTO(
				entidade.getIdUser(),
				entidade.getLogin(),
				entidade.getEmail(),
				entidade.getSenha(),
				entidade.getRoles()
				);
				
	}
}
