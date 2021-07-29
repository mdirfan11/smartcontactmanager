package com.mdtech.smartcontactmanager.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mdtech.smartcontactmanager.common.CommonConstant;
import com.mdtech.smartcontactmanager.entity.UserEntity;
import com.mdtech.smartcontactmanager.helper.HelperMessage;
import com.mdtech.smartcontactmanager.repository.UserRepository;

@Controller
//@RequestMapping("/signup")
public class UserRegistrationController {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("user", new UserEntity());
		return "signup";
	}
	
	@PostMapping("/doregister")
	public String registerUser(@Valid @ModelAttribute("user") UserEntity user, BindingResult bindingResult,
								@RequestParam(value="agreement", defaultValue="false") boolean agreement,
								@RequestParam("txtConfirmPassword") String cnfPassword, Model model, HttpSession session) {
		String returnView = "";
		try {
			
			if (bindingResult.hasErrors()) {
				System.out.println(bindingResult);
				returnView = "signup";
			}
			
			if(user.getPassword().equals(cnfPassword)) {
				user.setRole(CommonConstant.NORMAL_USER);
				user.setActive(CommonConstant.ACTIVE);
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				System.out.println("User after setting role and activeflag : "+user);
				userRepository.save(user);
				model.addAttribute("user", new UserEntity());
				session.setAttribute("message", new HelperMessage("User Registered Successfully!!", "alert-success"));
				returnView = "signup";
			} else {
				model.addAttribute("user", user);
				session.setAttribute("message", new HelperMessage("Password and Confirm Password must be same", "alert-danger"));
				returnView = "signup";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new HelperMessage("Somthing went wrong!!"+e.getMessage(), "alert-danger"));
			returnView = "signup";
		}
		return returnView;
	}

}
