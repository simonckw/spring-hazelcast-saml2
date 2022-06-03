package com.sckw.spring.hazelcast.saml2.config;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.FlushMode;
import org.springframework.session.SaveMode;
import org.springframework.session.config.SessionRepositoryCustomizer;
import org.springframework.session.hazelcast.Hazelcast4IndexedSessionRepository;
import org.springframework.session.hazelcast.config.annotation.SpringSessionHazelcastInstance;
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

/**
 * SessionConfiguration.
 */
@Configuration
@EnableHazelcastHttpSession
class SessionConfig extends AbstractHttpSessionApplicationInitializer {

  @Bean
  public SessionRepositoryCustomizer<Hazelcast4IndexedSessionRepository> customize() {
    return (sessionRepository) -> {
      sessionRepository.setFlushMode(FlushMode.IMMEDIATE);
      sessionRepository.setSaveMode(SaveMode.ALWAYS);
      sessionRepository.setSessionMapName("spring-session-map-name");
      sessionRepository.setDefaultMaxInactiveInterval(900);
    };
  }

  @Bean
  @SpringSessionHazelcastInstance
  public HazelcastInstance hazelcastInstance() {
    return Hazelcast.newHazelcastInstance(new Config().setClusterName("spring-session-cluster"));
  }


}
