package com.social.socialnetwork.config;

import com.social.socialnetwork.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private static final String[] WHITE_LIST_URLS = {
            "/api/v1/auth/register-email",
            "/api/v1/auth/verifyRegistration",
            "/api/v1/auth/resetPassword",
            "/api/v1/auth/authenticate",
            "/api/v1/auth/resetPasswordRequest",
            "/api/v1/auth/resetPassword",
            "/api/v1/auth/resendOTP",
            "/api/v1/auth/register-phone",
            "/api/v1/auth/verifyRegistration-phone",
            "/api/v1/auth/authenticate-by-phone",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeHttpRequests().requestMatchers(WHITE_LIST_URLS).permitAll();
        http.cors().and().authorizeHttpRequests()
                .requestMatchers("/api/v1/admin/**").hasAnyAuthority(Role.ADMIN.toString())
                .requestMatchers(HttpMethod.POST,"/api/v1/**").hasAnyAuthority(Role.USER.toString(), Role.ADMIN.toString())
                .requestMatchers(HttpMethod.PUT,"/api/v1/**").hasAnyAuthority(Role.USER.toString(), Role.ADMIN.toString())
                .requestMatchers(HttpMethod.DELETE,"/api/v1/**").hasAnyAuthority(Role.USER.toString(), Role.ADMIN.toString())
                .requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}
