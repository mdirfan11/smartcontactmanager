package com.mdtech.smartcontactmanager.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mdtech.smartcontactmanager.common.CommonConstant;
import com.mdtech.smartcontactmanager.entity.ContactEntity;
import com.mdtech.smartcontactmanager.entity.UserEntity;
import com.mdtech.smartcontactmanager.helper.HelperMessage;
import com.mdtech.smartcontactmanager.repository.ContactRepository;
import com.mdtech.smartcontactmanager.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@GetMapping("/home")
	public String userInded(Model model) {
		model.addAttribute("titel", "User Home");
		return "/normal/userdashbord";
	}
	
	@GetMapping("/addcontact")
	public String AddContact(Model model) {
		model.addAttribute("titel", "Add Contact");
		model.addAttribute("contact", new ContactEntity());
		return "normal/addcontact";
	}
	
	@SuppressWarnings("finally")
	@PostMapping("/processcontact")
	public String ProcessContact(@Valid @ModelAttribute("contact") ContactEntity contact,BindingResult bindingResult,
			Model model, Principal principal, HttpSession session, @RequestParam("profileImage") MultipartFile multipartFile
			,@RequestParam("callFor") String callFor, @RequestParam("oldImageName") String oldImageName) {
		
		try {
			System.out.println("Process contact callfor : "+callFor);
			if (bindingResult.hasErrors()) {
				System.out.println("bindingResult = "+bindingResult);
			} else {
				String username = principal.getName();
				UserEntity user = userRepository.getUserByemail(username);
				//Processing and uploading Profile image
				if(multipartFile.isEmpty()) {
					if (callFor.equals("addContact")) {
						contact.setImageUrl(CommonConstant.DEFAULT_MALE_PROFILE_ICON_NAME);
						System.out.println("Default image is set");
					} else if (callFor.equals("updateContact")) {
						contact.setImageUrl(contact.getImageUrl());
						System.out.println("Old image is set");
					}
				} else {
					//Upload the file to folder and update in contact
					contact.setImageUrl(multipartFile.getOriginalFilename());
					
					File file = new ClassPathResource("static/images").getFile();
					System.out.println("File = "+file);
					if (callFor.equals("updateContact")) { 
						Path path = Paths.get(file.getAbsolutePath()+File.separator+oldImageName);
						System.out.println("Image PAth : "+path);
						Files.delete(path);
						System.out.println("Old image is deleted");
					}
					Path path = Paths.get(file.getAbsolutePath()+File.separator+multipartFile.getOriginalFilename());
					Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					System.out.println("Image is uploaded ");
				}
				contact.setUser(user);
				
				user.getContactList().add(contact);
				userRepository.save(user);
				
				if (callFor.equals("addContact")) {
					model.addAttribute("titel", "Add Contact");
					model.addAttribute("contact", new ContactEntity());
					session.setAttribute("message", new HelperMessage("Contact Added Successfully !!", "alert-success"));
				} else if (callFor.equals("updateContact")) {
					ContactEntity savedContact = contactRepository.findById(contact.getId()).get();
					model.addAttribute("contact", savedContact);
					model.addAttribute("titel", "Update Contact");
					session.setAttribute("message", new HelperMessage("Contact Update Successfully !!", "alert-success"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			String message = e.getMessage();
			model.addAttribute("contact", contact);
			session.setAttribute("message", new HelperMessage(message,"alert-danger"));
		} finally {
			if (callFor.equals("addContact"))
				return "normal/addcontact";
			else 
				return "normal/updatecontact";
		}		
	}
	
	//handler for view contects
	@GetMapping("/viewcontact/{page}")
	public String  ViewContacts(@PathVariable("page") int page, Model model, Principal principal) {
		String userName = principal.getName();
		
		UserEntity user = userRepository.getUserByemail(userName);
		//List<ContactEntity> contactList = user.getContactList();
		
		Pageable pageable = PageRequest.of(page, CommonConstant.NO_OF_RECORD_IN_ONE_PAGE);
		
		Page<ContactEntity> contactList = contactRepository.getContactByUserId(user.getId(), pageable);
		
		//System.out.println("contacts = "+contactList);
		
		model.addAttribute("titel", "View Contacts");
		model.addAttribute("contacts", contactList);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contactList.getTotalPages());
		return "normal/viewcontacts";
	}
	
	//Handler for contact detail
	@SuppressWarnings("finally")
	@GetMapping("/{contactId}/contact")
	public String ContactDetail(@PathVariable("contactId") Long contactId, Model model, Principal principal) {
		try {
			String userName = principal.getName();
			UserEntity user = userRepository.getUserByemail(userName);
			Optional<ContactEntity> contactDetail = contactRepository.findById(contactId);
			ContactEntity contact = contactDetail.get();
			if (contact != null && user.getId() == contact.getUser().getId())
				model.addAttribute("contact", contact);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			model.addAttribute("titel", "Contact Detail");
			return "normal/contactdetail";
		}	
	}
	
	//Handler to delete contact with id
	@SuppressWarnings("finally")
	@GetMapping("/deletecontact/{cid}")
	public String DeleteContactFromDetail(@PathVariable("cid") Long contId, Model model, Principal principal) {
		try {
			int currentPage = 0;
			String userName = principal.getName();		
			UserEntity user = userRepository.getUserByemail(userName);
			
			Optional<ContactEntity> optionalContact = contactRepository.findById(contId);
			ContactEntity contact = optionalContact.get();
			
			if (contact.getUser().getId() == user.getId()) {
				contactRepository.deleteById(contId);
				//below condition is for deleting user profile image except for default images
				if (!contact.getImageUrl().equals(CommonConstant.DEFAULT_MALE_PROFILE_ICON_NAME)) {
					File file = new ClassPathResource("static/images").getFile();
					System.out.println("File = "+file);
					Path path = Paths.get(file.getAbsolutePath()+File.separator+contact.getImageUrl());
					System.out.println("Image PAth : "+path);
					Files.delete(path);
					System.out.println("Image is deleted");
				}
				Pageable pageable = PageRequest.of(currentPage, CommonConstant.NO_OF_RECORD_IN_ONE_PAGE);
				
				Page<ContactEntity> contactList = contactRepository.getContactByUserId(user.getId(), pageable);
				model.addAttribute("contacts", contactList);
				model.addAttribute("currentPage", currentPage);
				model.addAttribute("totalPages", contactList.getTotalPages());
			} else {
				
			}
			
		} catch (Exception e) {
			
		} finally {
			model.addAttribute("titel", "View Contacts");
			return "normal/viewcontacts";
		}
	}
	
	//Handler to delete contact with id
		@SuppressWarnings("finally")
		@GetMapping("/deletecontact/{page}/{cid}")
		public String DeleteContactFromView(@PathVariable("cid") Long contId, @PathVariable("page") int page, Model model, Principal principal) {
			try {
				String userName = principal.getName();		
				UserEntity user = userRepository.getUserByemail(userName);
				
				Optional<ContactEntity> optionalContact = contactRepository.findById(contId);
				ContactEntity contact = optionalContact.get();
				
				if (contact.getUser().getId() == user.getId()) {
					contactRepository.deleteById(contId);
					//below condition is for deleting user profile image except for default images
					if (!contact.getImageUrl().equals(CommonConstant.DEFAULT_MALE_PROFILE_ICON_NAME)) {
						File file = new ClassPathResource("static/images").getFile();
						System.out.println("File = "+file);
						Path path = Paths.get(file.getAbsolutePath()+File.separator+contact.getImageUrl());
						System.out.println("Image PAth : "+path);
						Files.delete(path);
						System.out.println("Image is deleted");
					}
					Pageable pageable = PageRequest.of(page, CommonConstant.NO_OF_RECORD_IN_ONE_PAGE);
					
					Page<ContactEntity> contactList = contactRepository.getContactByUserId(user.getId(), pageable);
					/*
					 * if (page == contactList.getTotalPages()) page--;
					 */
					model.addAttribute("contacts", contactList);
					model.addAttribute("currentPage", page);
					model.addAttribute("totalPages", contactList.getTotalPages());
				} else {
					
				}
				
			} catch (Exception e) {
				
			} finally {
				model.addAttribute("titel", "View Contacts");
				return "normal/viewcontacts";
			}
		}
		
	//handler for showing update contact form
	@SuppressWarnings("finally")
	@GetMapping("/{cid}/updatecontact")
	public String UpdateContact(@PathVariable("cid") Long cid, Model model) {
		try {
			Optional<ContactEntity> optionalContact = contactRepository.findById(cid);
			ContactEntity contact = optionalContact.get();
			model.addAttribute("contact", contact);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			model.addAttribute("titel", "Update Contact");
			return "normal/updatecontact";
		}
		
	}
	
	//handler for my profile
	@GetMapping("/myprofile")
	public String MyProfilePage(Model model, Principal principal) {
		String userName = principal.getName();
		UserEntity user = userRepository.getUserByemail(userName);
		
		model.addAttribute("user", user);
		model.addAttribute("titel", "My Profile");
		return "normal/myprofilepage";
	}
	
	
	@ModelAttribute
	public void commonData(Model model, Principal principal) {
		String username = principal.getName();
		UserEntity user = userRepository.getUserByemail(username);
		
		String userFirstName = user.getFirstName();
		String userLastName = user.getLastName();
		
		System.out.println("user : "+userFirstName+" "+userLastName);
		
		model.addAttribute("user", user);
	}

}
