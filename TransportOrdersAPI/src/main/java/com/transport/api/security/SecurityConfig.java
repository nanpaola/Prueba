package com.transport.api.security;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration public class SecurityConfig {
 @Bean PasswordEncoder encoder(){return new BCryptPasswordEncoder();}
 @Bean UserDetailsService users(@Value("${app.auth.username}") String username,@Value("${app.auth.password}") String password,PasswordEncoder encoder){return new InMemoryUserDetailsManager(User.withUsername(username).password(encoder.encode(password)).roles("ADMIN").build());}
 @Bean AuthenticationManager auth(AuthenticationConfiguration c)throws Exception{return c.getAuthenticationManager();}
 @Bean SecurityFilterChain filterChain(HttpSecurity http,JwtFilter jwt)throws Exception{return http.csrf(c->c.disable()).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(a->a.requestMatchers("/api/auth/**","/swagger-ui/**","/v3/api-docs/**").permitAll().anyRequest().authenticated()).exceptionHandling(e->e.authenticationEntryPoint((req,res,ex)->res.sendError(401,"Unauthorized")).accessDeniedHandler((req,res,ex)->res.sendError(403,"Forbidden"))).addFilterBefore(jwt,UsernamePasswordAuthenticationFilter.class).build();}
}
