package com.mehediFifo.CRM.security;

import com.mehediFifo.CRM.security.jwt.AuthEntryPointJwt;
import com.mehediFifo.CRM.security.jwt.AuthTokenFilter;
import com.mehediFifo.CRM.security.services.UserDetailsServiceImpl;
import com.mehediFifo.CRM.security.services.UserDetailsServiceImplAgent;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {
    @Value("${spring.console.path}")
    private String h2ConsolePath;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserDetailsServiceImplAgent userDetailsServiceAgent;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Super Admin
        authProvider.setPasswordEncoder(passwordEncoder()); // Use BCrypt for super admin
        return authProvider;
    }

    @Bean
    public DaoAuthenticationProvider agentAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsServiceAgent); // Agent
        authProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance()); // Plain text for agents if required
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder.authenticationProvider(authenticationProvider());
        authManagerBuilder.authenticationProvider(agentAuthenticationProvider());
        return authManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Use NoOpPasswordEncoder for agents if plain text is required
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth
                        .requestMatchers("/api/auth/signInAgent").permitAll()
                        .requestMatchers("/cc/campaigns").permitAll() // Allow unauthenticated access to /cc/campaigns
                        .requestMatchers("/agent/**").authenticated()
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
                );

        // Fix H2 database console issue
//        http.headers().frameOptions().sameOrigin();

        // Add JWT token filter
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

