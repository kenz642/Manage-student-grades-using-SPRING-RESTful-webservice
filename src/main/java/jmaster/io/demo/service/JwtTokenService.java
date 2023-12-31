package jmaster.io.demo.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtTokenService {
	
	@Value("${jwt.secret:123}")
	private String secretKey;
	private long validity = 5; // 5 phút
	
	public String createToken(String username) {
		Claims claims = Jwts.claims().setSubject(username);
		Date now = new Date();
		Date exp = new Date(now.getTime() + validity*60*1000);
		
		return Jwts.builder().setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(exp)
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();
	}
	
	public boolean isValidToken(String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			//do nothing
		}
		return false;
	}
	
	public String getUsername(String token) {
		try {
			return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
