package com.sckw.spring.hazelcast.saml2.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.session.web.http.SessionRepositoryFilter;

/**
 * SecurityConfig.
 */
@RequiredArgsConstructor
@Configuration
public class HttpSecurityConfig {

  private final @NonNull Saml2LoginSettings saml2LoginSettings;
  private final @NonNull SessionRepositoryFilter sessionRepositoryFilter;
  // private final @NonNull SessionRepository sessionRepository;

  @Autowired
  private FindByIndexNameSessionRepository sessionRepository;



  /**
   * filterChain.
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http //

        .addFilterBefore(sessionRepositoryFilter, SecurityContextPersistenceFilter.class)

        // .csrf().disable() // default: enabled
        // .userDetailsService(userDetailsService)

        // Requires saml2 with admin role
        .authorizeHttpRequests((authz) -> authz.antMatchers("/secured/admin/**") //
            .hasAuthority("admin")) //

        // Requires saml2 for other requests
        .authorizeHttpRequests((authz) -> authz.anyRequest() //
            .authenticated()) //

        .saml2Login(saml2LoginSettings) //

        .sessionManagement((sessionManagement) -> sessionManagement //

            .enableSessionUrlRewriting(false) // default: false

            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // default: if_required

            .sessionConcurrency(customizer -> customizer //
                .maximumSessions(1) // default: no limit
                // .sessionRegistry(sessionRegistry()) //
                .maxSessionsPreventsLogin(false)) // default: false

            .sessionFixation(customizer -> customizer //
                .migrateSession() // default: migrateSession
            )

        );



    return http.build();
  }

  @Bean
  SpringSessionBackedSessionRegistry sessionRegistry() {
    return new SpringSessionBackedSessionRegistry<>(sessionRepository);
  }

}
