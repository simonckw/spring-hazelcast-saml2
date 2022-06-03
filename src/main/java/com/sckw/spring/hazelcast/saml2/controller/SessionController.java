package com.sckw.spring.hazelcast.saml2.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SessionController {

  @RequestMapping("/")
  public String index() {
    return "home";
  }

  @RequestMapping("/secured/admin")
  public String admin() {
    return "admin";
  }

  @RequestMapping("/secured/attribute")
  public String hello(@AuthenticationPrincipal Saml2AuthenticatedPrincipal principal,
      HttpServletRequest request, Model model) {
    model.addAttribute("spEntityId", principal.getRelyingPartyRegistrationId());
    model.addAttribute("sessionIndexes", principal.getSessionIndexes());
    model.addAttribute("name", principal.getName());
    model.addAttribute("attributes", principal.getAttributes());
    model.addAttribute("role", principal.getAttribute("role"));

    return "attribute";
  }

}
