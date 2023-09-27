package com.ss.demosec.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService 
{
	private static final String SECERET_KEY="ihuwiu";
	
	//extracting username and password
	public String extractUserName(String token)
	{
		return extractClaim(token, Claims::getSubject);
	}
	
	public String generateToken(Map<String,Object> extraClaims,UserDetails userDetails)
	{
			return Jwts
					.builder()
					.setClaims(extraClaims)
					.setSubject(userDetails.getUsername())
					.setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
					.signWith(getSignKey(), SignatureAlgorithm.HS256)
					.compact();	
	}
	
	public String generateToken(UserDetails userDetails)
	{
		return generateToken(new HashMap<>(),userDetails);
	}
	//vaildate taoken
	public boolean isVaildToken(String token,UserDetails userDetails)
	{
		final String username=extractUserName(token);
		return (username.equals(userDetails.getUsername()))&& !isTokenExpired(token);
	}
	
	
	private boolean isTokenExpired(String token)
	{	
		return ((Date) extractExpiration(token)).before(new Date());
	}

	private Object extractExpiration(String token)
	{
		return extractClaim(token, Claims::getExpiration);
	}

	//extract all claims
	private Claims extractAllClaims(String token)
	{
		return Jwts.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	//extracting sinlge claim
	public <T> T extractClaim(String token,Function<Claims, T> claimsResolver)
	{
		final Claims claims=extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	private Key getSignKey() 
	{
		byte[] keyBytes=Decoders.BASE64.decode(SECERET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
