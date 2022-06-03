package com.sckw.spring.hazelcast.saml2.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * SessionController.
 */
@RequiredArgsConstructor
@Controller
@Slf4j
public class SessionController {

  /**
   * index.
   */
  @RequestMapping("/")
  public String index() {
    return "home";
  }

  /**
   * admin.
   */
  @RequestMapping("/secured/admin")
  public String admin() {
    return "admin";
  }

  /**
   * hello.
   */
  @RequestMapping("/secured/attribute")
  public String hello(@AuthenticationPrincipal Saml2AuthenticatedPrincipal principal,
      HttpServletRequest request, Model model) {
    model.addAttribute("spEntityId", principal.getRelyingPartyRegistrationId());
    log.info("SessionIndex {}", principal.getSessionIndexes());
    model.addAttribute("sessionIndexes", principal.getSessionIndexes());
    model.addAttribute("name", principal.getName());
    model.addAttribute("attributes", principal.getAttributes());
    model.addAttribute("role", principal.getAttribute("role"));

    HttpSession session = request.getSession(false);
    if (session != null) {
      model.addAttribute("sessionId", session.getId());
      model.addAttribute("sessionRole", session.getAttribute("role"));
    }

    SecurityContext context = SecurityContextHolder.getContext();
    Authentication authentication = context.getAuthentication();

    model.addAttribute("context.name", authentication.getName());
    model.addAttribute("context.name", authentication.isAuthenticated());
    model.addAttribute("context.authorities", authentication.getAuthorities());

    WebAuthenticationDetails webAuthenticationDetails =
        (WebAuthenticationDetails) authentication.getDetails();
    authentication.getCredentials();


    return "attribute";
  }

}
