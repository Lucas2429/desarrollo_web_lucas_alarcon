package com.tarea4.tarea4.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.tarea4.tarea4.models.User;
import com.tarea4.tarea4.repositories.UserRepository;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Esto viene por defecto, pero nosotros lo desactivamos por simplicidad
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/register", "/css/**", "/svg/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN") // sin "ROLE_" en hasRole
                .requestMatchers("/user/**").hasRole("USER") 
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .successHandler(successHandler())
                .permitAll()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .invalidSessionUrl("/login")
                .sessionFixation(sessionFix -> sessionFix.migrateSession())
                .maximumSessions(1)
                    .expiredUrl("/login")
                    .sessionRegistry(sessionRegistry())
            )
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/forbidden")
            );

        return http.build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    public AuthenticationSuccessHandler successHandler(){
        return ((_, response, _) -> {
            response.sendRedirect("/");
        });
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }

            return new org.springframework.security.core.userdetails.User(
                user.getUsername(), 
                user.getPassword(), 
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().toString()))
            );
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

