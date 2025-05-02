package api.bancaria.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import api.bancaria.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByIdUser(String idUser);

	Optional<Usuario> findByLogin(String login);

	Optional<Usuario> findByEmail(String email);

}
