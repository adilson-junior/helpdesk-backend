 package br.com.facilitysoft.helpdesk.config.security;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.facilitysoft.helpdesk.util.dto.CredenciasDTO;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	//Essa classe faz a interface de autenticação
	private AuthenticationManager authenticationManager;
	private JWTUtil  jwtUtil;
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			//Converte os dados da requisição em um objeto CredenciasDTO
			CredenciasDTO creds = new ObjectMapper().readValue(request.getInputStream(), CredenciasDTO.class);
			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getSenha(),new ArrayList<>());
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			return authentication;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		//Dando tudo certo esse metodo retorna os tados para o frontend.
		String username = ((UserSS) authResult.getPrincipal()).getUsername();
		String token = jwtUtil.genereteToken(username);
		response.setHeader("access-control-expose-headers", "Authorization");
		response.setHeader("Authorization", "Bearer " + token);
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		response.setStatus(401);
		response.setContentType("application/json");
		response.getWriter().append(json());//Chama o Json com o erro para o usuário.
	}

	private CharSequence json() {
		LocalDate date = LocalDate.now();
		return "{"
		+ "\"timestamp\": " + date + ", " 
		+ "\"status\": 401, "
		+ "\"error\": \"Não autorizado\", "
		+ "\"message\": \"Email ou senha inválidos\", "
		+ "\"path\": \"/login\"}";
	}
}
