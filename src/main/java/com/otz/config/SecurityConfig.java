package com.otz.config;

import com.otz.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                		 // Public endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/public/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/job/**").permitAll() // job details
                        .requestMatchers("/api/job/all").permitAll() // list all jobs
                        .requestMatchers("/api/job/search").permitAll() // search jobs

                        // Admin-only endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/job/*/approval").hasRole("ADMIN") // approve/reject jobs
                        .requestMatchers("/api/employer/profile/*/approval").hasRole("ADMIN") // approve employer profile
                        .requestMatchers("/api/job-applications/all").hasRole("ADMIN") // view all applications

                        // Employer-only endpoints
                        .requestMatchers("/api/job/{employerId}", "/api/job/{jobId}/{employerId}").hasRole("EMPLOYER")
                        .requestMatchers("/api/job-applications/job/*").hasRole("EMPLOYER") // view applications for their jobs
                        .requestMatchers("/api/employer/**").hasRole("EMPLOYER")
                        
                        // AiController endpoints for EMPLOYER
                        .requestMatchers("/api/ai/recommend-candidates/**").hasRole("EMPLOYER")
                        .requestMatchers("/api/ai/explain/**").hasRole("EMPLOYER")
                        
                        // Job Seeker endpoints
                        .requestMatchers("/api/candidate/**").hasRole("JOB_SEEKER") // profile, dashboard
                        .requestMatchers("/api/job-applications/apply").hasRole("JOB_SEEKER") // apply to jobs
                        .requestMatchers("/api/job-applications/my").hasRole("JOB_SEEKER") // view own applications
                        .requestMatchers("/api/ai/recommend-jobs/**").hasRole("JOB_SEEKER")
                        .requestMatchers("/api/employer/profile/all", "/api/employer/profile/search").hasAnyRole("ADMIN", "JOB_SEEKER")

                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
