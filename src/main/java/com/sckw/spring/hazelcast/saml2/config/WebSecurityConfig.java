package com.sckw.spring.hazelcast.saml2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

/**
 * WebSecurityConfig.
 */
@Configuration
public class WebSecurityConfig {

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (customizer) -> customizer //

        // Permit health check without any authentication
        .ignoring().antMatchers("/actuator/**");
  }

}
