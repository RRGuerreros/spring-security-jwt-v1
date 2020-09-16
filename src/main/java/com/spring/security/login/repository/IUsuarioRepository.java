package com.spring.security.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.security.login.entity.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {

	Usuario findByUsername( String username );
	
	
	
	
}
