package br.com.leuras.controller;

import java.security.Principal;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {
	
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@RequestMapping(value = {"/"}, method = RequestMethod.GET)
	public String index(Model model, Principal principal) {
		
		final UserDetails user = (UserDetails) ((Authentication) principal).getPrincipal();
		model.addAttribute("username", user.getUsername());
		
		return "index";
	}
	
	@RequestMapping(value = {"/logout"}, method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		SecurityContextHolder.clearContext();
		
		final HttpSession session = request.getSession(false);
		
		if (session != null) {
			session.invalidate();
		}
		
		for (final Cookie cookie : request.getCookies()) {
			cookie.setMaxAge(0);
		}
		
		return "logout";
	}
}
