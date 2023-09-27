package com.ss.demosec.config;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter
{

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;


	protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain)
			throws ServletException, IOException
	{
		final String authHeader=request.getHeader("Authorization");
		final String jwt;
		final String userEmail;
		//check for the token is vaild or not
		if(authHeader == null || !authHeader.startsWith("Bearer "))
		{
			filterChain.doFilter(request, response);
			return;
		}
		jwt=authHeader.substring(7);
		//extract username for jwt
		userEmail=jwtService.extractUserName(jwt);
		//having usermail and not authenticated 
		if(userEmail!=null && SecurityContextHolder.getContext().getAuthentication()==null)
		{
			//then getting the userdatils from DB
			UserDetails userDetails=this.userDetailsService.loadUserByUsername(userEmail);
			//checking user is vaild or not
			if(jwtService.isVaildToken(jwt, userDetails))
			{
				//createing object and updating the autehntication
				UsernamePasswordAuthenticationToken authtoken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authtoken);
			}
		}
		filterChain.doFilter(request, response);//always pass hand to next filter to executed
		
	}
	
}
