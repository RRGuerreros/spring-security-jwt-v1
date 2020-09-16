package com.spring.security.login.config.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.security.login.config.service.JWTService;
import com.spring.security.login.config.service.JWTServiceImpl;
import com.spring.security.login.entity.Usuario;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	private JWTService jwtService;
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		
		// con esto podemos cambiar la ruta por defecto de nuestra mapeo al login.
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login","POST")); 
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, NullPointerException {

		String username = obtainUsername(request);
		String password = obtainPassword(request);

		if( username != null && password != null ) {
			logger.info("username desde request parameter (form-data): " + username );
			logger.info("password desde request parameter (form-data): " + password );
		} else {
			Usuario user = null;
			
			try {
				user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
				
				username = user.getUsername();
				password = user.getPassword();
				
				logger.info("username desde request InputStream (raw): " + username );
				logger.info("password desde request InputStream (raw): " + password );
				
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		username = username.trim();
		
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
		
		
		
		
		return authenticationManager.authenticate(authToken);
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		String token = jwtService.create(authResult);
		
		// importante que el nombre tiene que ser Authorization y tambien el prefijo debe ser 'Bearer '
		response.addHeader(JWTServiceImpl.HEADER_STRING, JWTServiceImpl.TOKEN_PREFIX+ token);
		
		Map<String, Object> body = new HashMap<String, Object>();
		
		body.put("token", token);
		
		body.put("user", authResult.getPrincipal() );
		
		body.put("mensaje", String.format("Hola %s, has iniciado sesión con éxito", authResult.getName()));
		
		response.getWriter().write( new ObjectMapper().writeValueAsString(body) );
		response.setStatus(200);
		response.setContentType("application/json");

	}
	
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
	

		Map<String, Object> body = new HashMap<String, Object>();
		
		body.put("mensaje", "Error de autenticación: username o password incorrecto!");
		body.put("error", failed.getMessage());
		
		response.getWriter().write( new ObjectMapper().writeValueAsString(body) );
		response.setStatus(401);
		response.setContentType("application/json");
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
