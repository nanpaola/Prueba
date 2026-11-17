package com.transport.api.security;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
@Component public class JwtFilter extends OncePerRequestFilter {
 private final JwtService jwt;private final UserDetailsService users;
 public JwtFilter(JwtService jwt,UserDetailsService users){this.jwt=jwt;this.users=users;}
 @Override protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException{String auth=req.getHeader("Authorization");if(auth!=null&&auth.startsWith("Bearer ")&&SecurityContextHolder.getContext().getAuthentication()==null){try{String username=jwt.subject(auth.substring(7));var user=users.loadUserByUsername(username);SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities()));}catch(JwtException|org.springframework.security.core.userdetails.UsernameNotFoundException ex){SecurityContextHolder.clearContext();}}chain.doFilter(req,res);}
}
