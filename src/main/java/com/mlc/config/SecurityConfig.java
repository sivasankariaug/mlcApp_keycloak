package com.mlc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

import java.util.Collection;
import java.util.Map;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new CrosFilterConfig(), ChannelProcessingFilter.class)
                .authorizeHttpRequests(registry -> registry
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2Configurer -> oauth2Configurer.jwt(
                        jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwt -> {
                            Map<String, Collection<String>> realmAccess = jwt.getClaim("realm_access");
                            Collection<String> roles = realmAccess.get("roles");
                            var grantedAuthorities = roles.stream()
                                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                    .toList();
                            return new JwtAuthenticationToken(jwt, grantedAuthorities);
                        })));
        return http.build();
    }

}
