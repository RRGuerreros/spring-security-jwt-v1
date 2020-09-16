package com.spring.security.login.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.security.login.entity.Rol;
import com.spring.security.login.entity.Usuario;
import com.spring.security.login.repository.IUsuarioRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
	
	@Autowired
	private IUsuarioRepository usuarioRepository;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	@Transactional( readOnly = true )
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuario appUser = usuarioRepository.findByUsername( username );
		
		if( appUser == null ) {
			
			log.error("No existe el usuario '"+username+"' en el sistema!");
			
			throw new UsernameNotFoundException("No existe el usuario "+username+" en el sistema!");
		}
		
		//Mapear nuestra lista de Authority con la de spring security 
	    List<GrantedAuthority> authorities = new ArrayList<>();
    
	    for ( Rol rol: appUser.getRoles() ) {
	        // ROLE_USER, ROLE_ADMIN,..
	    	authorities.add(new SimpleGrantedAuthority( rol.getAuthority() ));
	    }
	    
	    if( authorities.isEmpty()) {
			
			log.error("Error login: el usuario " + username + " no tiene roles asignados");
			
			throw new UsernameNotFoundException("Error login: el usuario " + username + " no tiene roles asignados!");
		}
		
        return new User(appUser.getUsername(), appUser.getPassword(), appUser.getEnabled(), true, true, true, authorities);
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
