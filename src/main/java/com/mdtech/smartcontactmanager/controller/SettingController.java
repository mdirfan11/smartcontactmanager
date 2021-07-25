package com.mdtech.smartcontactmanager.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mdtech.smartcontactmanager.entity.UserEntity;
import com.mdtech.smartcontactmanager.helper.HelperMessage;
import com.mdtech.smartcontactmanager.repository.UserRepository;

@Controller
@RequestMapping("/setting")
public class SettingController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder; 
	
	/* handler to show change password form */
	@GetMapping("")
	public String ShowChangePasswordForm(Model model) {
		model.addAttribute("titel", "Change Password");
		return "normal/changepassword";
	}
	
	@PostMapping("/processpasswordchange")
	public String ProcessPasswordChange(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,
			Principal principal, HttpSession session) {
		String username = principal.getName();
		UserEntity currentUser = userRepository.getUserByemail(username);
		
		System.out.println("OLD PASSWOED : "+oldPassword+" NEW PASSWORD : "+newPassword);
		if (bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
			currentUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
			
			userRepository.save(currentUser);
			session.setAttribute("message", new HelperMessage("Password updated Successfully!!", "alert-success"));
		} else {
			session.setAttribute("message", new HelperMessage("Somthing went wrong!!", "alert-danger"));
		}
		return "normal/changepassword";
	}
	
	@ModelAttribute
	public void commonData(Model model, Principal principal) {
		String username = principal.getName();
		UserEntity user = userRepository.getUserByemail(username);
		
		model.addAttribute("user", user);
		
	}

}
