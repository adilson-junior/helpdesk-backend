package br.com.facilitysoft.helpdesk.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {
	//Essa anotação pega as variáveis definidas no arquivo application.properties
	@Value("${jwt.expiration}")
	private Long expiration;
	//Essa anotação pega as variáveis definidas no arquivo application.properties
	@Value("${jwt.secret}")
	private String secret;
	
	//Metodo para gerar o token para o usuário
	public String genereteToken(String email) {
		return Jwts.builder()
				.setSubject(email)//Seta o email do usuario
				.setExpiration(new Date(System.currentTimeMillis() + expiration))//Define o tempo de expiração do token
				.signWith(SignatureAlgorithm.HS512, secret.getBytes())//Define a regra do algoritmo de criação do token
				.compact();
	}

	public boolean tokenValido(String token) {
		Claims claims = getClaims(token);
		if( claims != null) {
			String username = claims.getSubject();
			Date expirationDate = claims.getExpiration();
			Date now = new Date(System.currentTimeMillis());
			if(username != null && expirationDate != null && now.before(expirationDate)) {
				return true;
			}				
		}
		return false;
	}
	
	private Claims getClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			return null;
		}
	}

	public String getUsername(String token) {
		Claims claims = getClaims(token);
		if(claims != null) {
			return claims.getSubject();
		}
		return null;
	}

}
