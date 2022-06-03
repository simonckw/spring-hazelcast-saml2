package com.sckw.spring.hazelcast.saml2.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.saml2.Saml2LoginConfigurer;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.security.saml2.provider.service.authentication.Saml2Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
class Saml2LoginSettings implements Customizer<Saml2LoginConfigurer<HttpSecurity>> {

  @Override
  public void customize(Saml2LoginConfigurer<HttpSecurity> saml2LoginConfigurer) {

    saml2LoginConfigurer.successHandler(new SavedRequestAwareAuthenticationSuccessHandler() {

      @Override
      public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
          Authentication auth) throws IOException, ServletException {

        setTargetUrl();
        setSessionAttribute(auth, request);
        super.onAuthenticationSuccess(request, response, assignAuthorities(auth, request));
      }

      private void setTargetUrl() {
        // Below are default values
        setDefaultTargetUrl("/");
        // setTargetUrlParameter("spring-security-redirect");
        setAlwaysUseDefaultTargetUrl(false);
      }
    });
  }

  private void setSessionAttribute(Authentication auth, HttpServletRequest request) {
    Saml2AuthenticatedPrincipal principal = (Saml2AuthenticatedPrincipal) auth.getPrincipal();
    request.getSession().setAttribute("role", principal.getFirstAttribute("role"));
  }

  private Authentication assignAuthorities(Authentication auth, HttpServletRequest request) {

    @SuppressWarnings("unchecked")
    Collection<SimpleGrantedAuthority> oldAuthorities =
        (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication()
            .getAuthorities();

    Saml2AuthenticatedPrincipal principal = (Saml2AuthenticatedPrincipal) auth.getPrincipal();

    if (principal.getAttribute("role").contains("admin")) {
      List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
      updatedAuthorities.addAll(oldAuthorities);

      updatedAuthorities.add(new SimpleGrantedAuthority("admin"));

      Saml2Authentication updatedAuth =
          new Saml2Authentication(
              (AuthenticatedPrincipal) SecurityContextHolder.getContext().getAuthentication()
                  .getPrincipal(),
              ((Saml2Authentication) auth).getSaml2Response(), updatedAuthorities);
      SecurityContextHolder.getContext().setAuthentication(updatedAuth);

      return updatedAuth;
    } else {
      return auth;
    }
  }
}
