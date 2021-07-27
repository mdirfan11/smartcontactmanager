package com.mdtech.smartcontactmanager.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mdtech.smartcontactmanager.entity.UserEntity;
import com.mdtech.smartcontactmanager.helper.HelperMessage;
import com.mdtech.smartcontactmanager.repository.UserRepository;
import com.mdtech.smartcontactmanager.service.SendMailService;

@Controller
@RequestMapping("/forgetpassword")
public class ForgetPasswordController {
	
	@Autowired
	private SendMailService sendMailService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("")
	public String VerifyEmailForm() {
		return "verifyemailform";
	}
	
	@PostMapping("/doprocess")
	public String SendOTPToEmail(HttpSession session, @RequestParam("email") String email) {
		try {
			UserEntity user = userRepository.getUserByemail(email);
			
			if (user != null) {
				Random r = new Random();
				int otp = r.nextInt(999999)+100000;
				
				System.out.println("OTP ["+otp+"] EMAIL ["+email+"]");
				
				String receiver = email;
				String subject = "SCM OTP";
				String content = "<p>Smart Contact Manager Forget password OTP = <b>"+otp+"</b></p>";
				
				sendMailService.SendMail(receiver, subject, content);
				
				session.setAttribute("myotp", otp);
				session.setAttribute("username", email);
				session.setAttribute("message", new HelperMessage("OTP Send to youe emeil successfully !!", "alert-success"));
				return "verifyotpform";
			} else {
				session.setAttribute("message", new HelperMessage("This email is not registerd", "alert-danger"));
				return "verifyemailform";
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new HelperMessage("Somthing Went Wrong", "alert-danger"));
			return "verifyemailform";
		}
	}
	
	@PostMapping("/verifyotp")
	public String VerifyOtp(@RequestParam("otp") int otp, HttpSession session) {
		String returnView = "";
		int myotp = (int) session.getAttribute("myotp");
		String username = (String) session.getAttribute("username");
		
		if (myotp == otp) {
			returnView = "updatepassword";
		} else {
			session.setAttribute("message", new HelperMessage("Invalid OTP", "alert-danger"));
			returnView = "verifyotpform";
		}
		
		return returnView;
	}
	
	@PostMapping("/doupdatepassword")
	public String DoUpdatePassword(@RequestParam("password") String password, @RequestParam("confirmPassword")String confPassword,
			HttpSession session) {
		String returnView = "";
		String username = (String) session.getAttribute("username");
		UserEntity user = userRepository.getUserByemail(username);
		
		if (password.equals(confPassword)) {
			user.setPassword(bCryptPasswordEncoder.encode(password));
			userRepository.save(user);
			System.out.println("PASSWORD UPDATED");
			session.setAttribute("message", new HelperMessage("Password Updated SUCCESSFULLY!!", "alert-success"));
			returnView = "redirect:/signin?change=Password Updated SUCCESSFULLY!!";
		} else {
			session.setAttribute("message", new HelperMessage("Password and Confirm Password should match", "alert-danger"));
			returnView = "updatepassword";
		}
		return returnView;
	}

}
