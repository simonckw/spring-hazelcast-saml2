package com.sckw.spring.hazelcast.saml2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.session.web.http.SessionRepositoryFilter;

@Configuration
public class HttpSecurityConfig {

  @Autowired
  private SessionRepositoryFilter sessionRepositoryFilter;



  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http //

        .addFilterBefore(sessionRepositoryFilter, SecurityContextPersistenceFilter.class)

        // Requires saml2 with admin role
        .authorizeHttpRequests((authz) -> authz.antMatchers("/secured/admin/**") //
            .hasAuthority("admin")) //

        // Requires saml2 for other requests
        .authorizeHttpRequests((authz) -> authz.anyRequest() //
            .authenticated()) //

        .saml2Login();

    return http.build();
  }

}
