package com.formacionbdi.springboot.app.oauth.security.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.formacionbdi.springboot.app.commons.usuarios.models.entity.Usuario;
import com.formacionbdi.springboot.app.oauth.services.UsuarioService;

import brave.Tracer;
import feign.FeignException;

@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher {

	private Logger log = LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private Tracer tracer;
	
	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {
		if(authentication.getName().equalsIgnoreCase("frontendapp")){
		    return; // si es igual a frontendapp se salen del método!
		}
		
		UserDetails user = (UserDetails)authentication.getPrincipal();
		String mensaje = "Success login: " + user.getUsername();
		System.out.println(mensaje);
		log.info(mensaje);
		
		Usuario usuario = this.usuarioService.findByUsername(user.getUsername());
		if (usuario.getIntentos() != null && usuario.getIntentos() > 0) {
			usuario.setIntentos(0);
			this.usuarioService.update(usuario, usuario.getId());
		}
	}

	@Override
	public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
		String mensaje = "Error en el login: " + exception.getMessage();
		log.error(mensaje);
		System.out.println(mensaje);
		
		try {
			StringBuilder errors = new StringBuilder();
			errors.append(mensaje);
			
			Usuario usuario = this.usuarioService.findByUsername(authentication.getName());
			if (usuario.getIntentos() == null) {
				usuario.setIntentos(0);
			}
			
			log.info(String.format("Intentos actual es de: ", usuario.getIntentos()));
			usuario.setIntentos(usuario.getIntentos()+1);
			log.info(String.format("Intentos después es de: ", usuario.getIntentos()));
			
			errors.append(String.format(" - Intentos del login: ", usuario.getIntentos()));
			
			if (usuario.getIntentos() >= 3) {
				String errorMaxIntentos = String.format("El usuário %s des-hábilitado por máximos intentos...", usuario.getUsername()); 
				log.error(errorMaxIntentos);
				errors.append(" - " + errorMaxIntentos);
				usuario.setEnabled(false);
			}			
			
			tracer.currentSpan().tag("error.mensaje", errors.toString());
			
			this.usuarioService.update(usuario, usuario.getId());
		} catch(FeignException e) {
			log.error(String.format("El usuário %s no existe en el sistema", authentication.getName()));			
		}
	}
}