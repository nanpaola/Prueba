package com.transport.api.controller;
import com.transport.api.dto.Requests.*;
import com.transport.api.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/auth") public class AuthController {
 private final AuthenticationManager auth;private final JwtService jwt;
 public AuthController(AuthenticationManager auth,JwtService jwt){this.auth=auth;this.jwt=jwt;}
 @PostMapping("/login") public Token login(@Valid @RequestBody Login r){auth.authenticate(new UsernamePasswordAuthenticationToken(r.username(),r.password()));return new Token(jwt.generate(r.username()),"Bearer");}
}
