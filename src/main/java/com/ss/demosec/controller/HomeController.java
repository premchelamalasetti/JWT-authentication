package com.ss.demosec.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ss.demosec.service.AuthenticationService;

@RestController
@RequestMapping("/api/u1")
public class HomeController
{
	private final AuthenticationService authenticationService;
	
	
	
	public HomeController(AuthenticationService authenticationService) {
		super();
		this.authenticationService = authenticationService;
	}

	@PostMapping("/register")// /api/u1/reigster
	public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request)
	{
		return ResponseEntity.ok(authenticationService.register(request));
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request)
	{
		return ResponseEntity.ok(authenticationService.authenticate(request));
	}
}
