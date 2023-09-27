package com.ss.demosec.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController 
{
	@GetMapping
	public ResponseEntity<String> demoController()
	{
		return ResponseEntity.ok( "From secured Endpoint");
	}
}
