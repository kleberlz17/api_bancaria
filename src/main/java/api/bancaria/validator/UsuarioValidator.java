package api.bancaria.validator;

import java.util.Optional;

import org.springframework.stereotype.Component;

import api.bancaria.exception.EmailDuplicadoException;
import api.bancaria.exception.LoginDuplicadoException;
import api.bancaria.model.Usuario;
import api.bancaria.repository.UsuarioRepository;

@Component
public class UsuarioValidator {
	
	private UsuarioRepository usuarioRepository;
	
	public UsuarioValidator(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}
	
	public void validarUsuarioLogin(Usuario usuario) {
		if(existeUsuarioLoginCadastrado(usuario)) {
			throw new LoginDuplicadoException("Login escolhido já está sendo utilizado");
		}
	}
	
	public boolean existeUsuarioLoginCadastrado(Usuario usuario) {
		Optional<Usuario> existente = usuarioRepository.findByLogin(usuario.getLogin());
		return existente.isPresent() && !existente.get().getIdUser().equals(usuario.getIdUser());//Aqui login existe mas o ID não é o mesmo do atual, então é outra pessoa tentando cadastrar.
	}
	
	public void validarEmail(Usuario usuario) {
		if(existeEmailCadastrado(usuario)) {
			throw new EmailDuplicadoException("Email já cadastrado no sistema");
		}
	}
	
	public boolean existeEmailCadastrado(Usuario usuario) {
		Optional<Usuario> existente = usuarioRepository.findByEmail(usuario.getEmail());
		return existente.isPresent() && !existente.get().getIdUser().equals(usuario.getIdUser());
	}

}
