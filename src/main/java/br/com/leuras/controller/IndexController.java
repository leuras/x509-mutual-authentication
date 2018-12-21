package br.com.leuras.controller;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
}
