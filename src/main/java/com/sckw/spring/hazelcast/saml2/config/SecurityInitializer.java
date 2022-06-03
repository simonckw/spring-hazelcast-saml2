package com.sckw.spring.hazelcast.saml2.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * SecurityInitializer.
 */
class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {
  public SecurityInitializer() {
    super(HttpSecurityConfig.class, SessionConfig.class);
  }
}
