package io.github.dafnipapado.careerstart_backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/employers").hasAuthority("INSERT_EMPLOYER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/employers/{uuid}").hasAuthority("UPDATE_EMPLOYER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/employers/{uuid}").hasAuthority("DELETE_EMPLOYER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/employers/{uuid}/view").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/employers/{uuid}").hasAuthority("VIEW_EMPLOYER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/employers/{uuid}/avatar").hasAuthority("UPDATE_EMPLOYER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/employers").hasAuthority("VIEW_EMPLOYERS")
                        .requestMatchers(HttpMethod.POST, "/api/v1/jobseekers").hasAuthority("INSERT_JOB_SEEKER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/jobseekers/{uuid}").hasAuthority("UPDATE_JOB_SEEKER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/jobseekers/{uuid}").hasAuthority("DELETE_JOB_SEEKER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/jobseekers/{uuid}/view").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/jobseekers/{uuid}").hasAuthority("VIEW_JOB_SEEKER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/jobseekers/{uuid}/avatar", "/api/v1/jobseekers/{uuid}/cv-file").hasAuthority("UPDATE_JOB_SEEKER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/jobseekers").hasAuthority("VIEW_JOB_SEEKERS")
                        .requestMatchers(HttpMethod.POST, "/api/v1/job-listings").hasAuthority("INSERT_JOB_LISTING")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/job-listings/{uuid}").hasAuthority("UPDATE_JOB_LISTING")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/job-listings/{uuid}").hasAuthority("DELETE_JOB_LISTING")
                        .requestMatchers(HttpMethod.GET, "/api/v1/job-listings/{uuid}/view").hasAnyAuthority("ROLE_ADMIN", "ROLE_EMPLOYER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/job-listings/{uuid}").hasAuthority("VIEW_JOB_LISTING")
                        .requestMatchers(HttpMethod.GET, "/api/v1/job-listings").hasAuthority("VIEW_JOB_LISTINGS")
                        .anyRequest().authenticated()
                )
                .anonymous(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler))
        ;
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
}
