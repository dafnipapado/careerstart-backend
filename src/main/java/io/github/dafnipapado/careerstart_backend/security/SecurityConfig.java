package io.github.dafnipapado.careerstart_backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${allowed.origins}")
    private List<String> allowedOrigins;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
        http
                .cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/employers").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/employers/{uuid}").hasAuthority("UPDATE_EMPLOYER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/employers/{uuid}").hasAuthority("DELETE_EMPLOYER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/employers/{uuid}/view").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/employers/{uuid}").hasAuthority("VIEW_EMPLOYER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/employers/{uuid}/avatar-upload").hasAuthority("UPDATE_EMPLOYER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/employers/{uuid}/avatar").hasAuthority("VIEW_EMPLOYER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/employers").hasAuthority("VIEW_EMPLOYERS")
                        .requestMatchers(HttpMethod.GET, "/api/v1/employers/dashboard").hasAuthority("ROLE_EMPLOYER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/jobseekers").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/jobseekers/{uuid}").hasAuthority("UPDATE_JOB_SEEKER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/jobseekers/{uuid}").hasAuthority("DELETE_JOB_SEEKER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/jobseekers/{uuid}/view").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/jobseekers/{uuid}").hasAuthority("VIEW_JOB_SEEKER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/jobseekers/{uuid}/avatar", "/api/v1/jobseekers/{uuid}/cv-file").hasAuthority("UPDATE_JOB_SEEKER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/jobseekers").hasAuthority("VIEW_JOB_SEEKERS")
                        .requestMatchers(HttpMethod.GET, "/api/v1/jobseekers/dashboard").hasAuthority("ROLE_JOB_SEEKER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/job-listings").hasAuthority("INSERT_JOB_LISTING")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/job-listings/{uuid}").hasAuthority("UPDATE_JOB_LISTING")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/job-listings/{uuid}").hasAuthority("DELETE_JOB_LISTING")
                        .requestMatchers(HttpMethod.GET, "/api/v1/job-listings/{uuid}/view").hasAnyAuthority("ROLE_ADMIN", "ROLE_EMPLOYER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/job-listings/{uuid}").hasAuthority("VIEW_JOB_LISTING")
                        .requestMatchers(HttpMethod.GET, "/api/v1/job-listings").hasAuthority("VIEW_JOB_LISTINGS")
                        .requestMatchers(HttpMethod.POST, "/api/v1/cv").hasAuthority("UPLOAD_CV")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/cv/{uuid}").hasAuthority("UPDATE_CV")
                        .requestMatchers(HttpMethod.GET, "/api/v1/cv/{jobSeekerUuid}/view").hasAuthority("VIEW_JOB_SEEKER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/jobseekers/{jobListingUuid}/apply").hasAuthority("APPLY_TO_JOB_LISTING")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/jobseekers/{jobListingUuid}/withdraw").hasAuthority("WITHDRAW_FROM_JOB_LISTING")
                        .requestMatchers(HttpMethod.GET, "/api/v1/jobseekers/{jobListingUuid}/has-applied").hasAuthority("APPLY_TO_JOB_LISTING")
                        .requestMatchers(HttpMethod.GET, "/api/v1/jobseekers/{jobListingUuid}/job-seekers").hasAnyAuthority("ROLE_EMPLOYER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/fields", "/api/v1/regions").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
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

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
