package api.bancaria.service;

import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import api.bancaria.exception.UsuarioNaoEncontradoException;
import api.bancaria.model.Usuario;
import api.bancaria.repository.UsuarioRepository;
import api.bancaria.validator.UsuarioValidator;

@Service
public class UsuarioService {

	UsuarioValidator usuarioValidator;
	UsuarioRepository usuarioRepository;

	public UsuarioService(UsuarioValidator usuarioValidator, UsuarioRepository usuarioRepository) {
		this.usuarioValidator = usuarioValidator;
		this.usuarioRepository = usuarioRepository;
	}

	public Usuario salvar(Usuario usuario) {
		if (usuario.getLogin() == null || usuario.getLogin().isEmpty()) {
			throw new IllegalArgumentException("O login não pode ser nulo ou vazio");
		}

		if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
			throw new IllegalArgumentException("O email não pode ser nulo ou vazio");
		}

		usuarioValidator.validarUsuarioLogin(usuario);
		return usuarioRepository.save(usuario);
	}

	public Optional<Usuario> buscarPorIdUser(Long idUser) {
		return usuarioRepository.findByIdUser(idUser);
	}

	public Optional<Usuario> buscarPorLogin(String login) {
		return usuarioRepository.findByLogin(login);
	}

	public Optional<Usuario> buscarPorEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

	private Usuario atualizarCampo(Long idUser, Consumer<Usuario> atualizador) {
		Usuario usuario = usuarioRepository.findByIdUser(idUser)
				.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não encontrado"));
		atualizador.accept(usuario);
		return usuarioRepository.save(usuario);
	}

	public Usuario atualizarLogin(Long idUser, String loginNovo) {
		if (loginNovo == null || loginNovo.isEmpty()) {
			throw new IllegalArgumentException("Login não pode ser nulo ou vazio");
		}

		return atualizarCampo(idUser, user -> {
			user.setLogin(loginNovo);
			usuarioValidator.validarUsuarioLogin(user);
		});
	}
	
	public Usuario atualizarEmail(Long idUser, String emailNovo) {
		if (emailNovo == null || emailNovo.isEmpty()) {
			throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
		}
		
		return atualizarCampo(idUser, user -> {
			user.setEmail(emailNovo);
			usuarioValidator.validarEmail(user);
		});
	}
}
