package com.ohh_really.ringdingdong.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return charSequence.toString().equals(s);
            }
        };
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .headers().frameOptions().disable()
            .and()
                .authorizeRequests()
                .antMatchers("/user/login", "/user/policyAgree").permitAll()
                .antMatchers("/fcm/**").hasAnyRole("ADMIN", "SUPER_ADMIN", "DEVELOPER")
                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html","/api-docs").permitAll()
                .anyRequest().authenticated();

        return http.build();
    }
}
