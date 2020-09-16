package com.spring.security.login.controller;


import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.spring.security.login.entity.Usuario;
import com.spring.security.login.repository.IUsuarioRepository;


@Controller
public class PrincipalController {
	
	Logger log = LoggerFactory.getLogger(PrincipalController.class);
	
	@Autowired
	private IUsuarioRepository usuarioRepository;
	
	@GetMapping({ "/", "/login" })
	public String index( Principal principal ) {
		
		if( principal != null ) {
			log.info("Ya ah iniciado sesión anteriormente");
			return "redirect:/menu-principal";
		}
		
		return "login";
	}
	
	@GetMapping("/menu-principal")
	public String menu() {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
	    UserDetails userDetail = (UserDetails) auth.getPrincipal();
	    	        
	    Usuario usuario = usuarioRepository.findByUsername( userDetail.getUsername() );
	    
	    log.info("el usuario "+usuario.getUsername()+" ah solicitado iniciar sesion");
	    log.info("estado del usuario -> " + (usuario.getEnabled()? "Activo" :"Inactivo"));
	    
	    if( !usuario.getEnabled()) {
	    	log.info("el usuario no se encuentra activo, no pudo iniciar sesion");
	    	return "redirect:/login?error=true";
	    }
	    
	    log.info("usuario conectado con éxito");

		return "menu";
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
