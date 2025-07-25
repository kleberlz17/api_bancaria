package api.bancaria.controller;



import java.net.URI;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import api.bancaria.dto.NovoEmailDTO;
import api.bancaria.dto.NovoLoginDTO;
import api.bancaria.dto.UsuarioDTO;
import api.bancaria.mapper.UsuarioConverter;
import api.bancaria.model.Usuario;
import api.bancaria.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/usuarios")
@Slf4j
public class UsuarioController {
	
	UsuarioService usuarioService;
	UsuarioConverter usuarioConverter;
	
	public UsuarioController(UsuarioService usuarioService, UsuarioConverter usuarioConverter) {
		this.usuarioService = usuarioService;
		this.usuarioConverter = usuarioConverter;
	}
	
	@PostMapping
	public ResponseEntity<Object> salvar(@RequestBody @Valid UsuarioDTO usuarioDTO) {
		log.info("Salvando os dados do usuário no sistema...");
		
		Usuario usuario = usuarioConverter.dtoParaEntidade(usuarioDTO);
		Usuario usuarioSalvo = usuarioService.salvar(usuario);
		
		URI uri = URI.create("/usuarios/" + usuarioSalvo.getIdUser());
		
		log.info("Usuário salvo com sucesso.");
		return ResponseEntity.created(uri).build();
	}
	
	@GetMapping("/{idUser}")
	public ResponseEntity<Usuario> buscarPorIdUser(@PathVariable Long idUser){
		log.info("Buscando os dados do usuário de ID {} no sistema...", idUser );
		
		Optional<Usuario> usuario = usuarioService.buscarPorIdUser(idUser);
		if(usuario.isPresent()) {
			
			log.info("Dados do usuário encontrados.");
			return ResponseEntity.ok(usuario.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/login")
	public ResponseEntity<Usuario> buscarPorLogin(@RequestParam String login) {
		log.info("Buscando usuário por login {} no sistema...", login);
		
		Optional<Usuario> usuario = usuarioService.buscarPorLogin(login);
		if(usuario.isPresent()) {
			
			log.info("Usuário encontrado");
			return ResponseEntity.ok(usuario.get());
		} else {
			return ResponseEntity.notFound().build();
		}	
	}
	
	@GetMapping("/email")
	public ResponseEntity<Usuario> buscarPorEmail(@RequestParam String email) {
		log.info("Buscando usuário pelo email {} no sistema...", email);
		
		Optional<Usuario> usuario = usuarioService.buscarPorEmail(email);
		if(usuario.isPresent()) {
			
			log.info("Usuário encontrado.");
			return ResponseEntity.ok(usuario.get());
		} else {
			return ResponseEntity.notFound().build();
		}	
	}
	
	@PutMapping("/{idUser}/novologin")
	public ResponseEntity<Usuario> atualizarLogin(@PathVariable Long idUser, @RequestBody @Valid NovoLoginDTO novoLoginDto) {
		log.info("Atualizando o login do usuário com o ID {} no sistema...", idUser);
		Usuario loginAlterado = usuarioService.atualizarLogin(idUser, novoLoginDto.getLogin());
		
		log.info("Login do usuário foi atualizado com sucesso.");
		return ResponseEntity.ok(loginAlterado);
	}

	@PutMapping("/{idUser}/novoemail")
	public ResponseEntity<Usuario> atualizarEmail(@PathVariable Long idUser, @RequestBody @Valid NovoEmailDTO novoEmailDto) {
		log.info("Atualizando o email do usuário com o ID {} no sistema...", idUser);
		Usuario emailAlterado = usuarioService.atualizarEmail(idUser, novoEmailDto.getEmail());
		
		log.info("Email do usuário foi atualizado com sucesso.");
		return ResponseEntity.ok(emailAlterado);	
	}
	
	
	
}
