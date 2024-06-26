package com.example.petparadisebe.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SercurityConfig {
    //phân quyền đường dẫn
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/products/**").permitAll()
                .requestMatchers("/api/v1/categories/**").permitAll()
                .requestMatchers("/api/v1/users/**").permitAll()
                .requestMatchers("/api/v1/carts/**").permitAll()
                .requestMatchers("/api/v1/vnpay/**").permitAll()
                .requestMatchers("/api/v1/promotions/**").permitAll()
                .requestMatchers("/api/v1/orders/**").permitAll()
                .requestMatchers("/api/v1/transactions/**").permitAll()
//                .requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")
//                .requestMatchers("/user/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
//                .requestMatchers("/demo").hasAnyAuthority("ROLE_USER")
                .and()
                .csrf().disable()
                .cors(Customizer.withDefaults())
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
