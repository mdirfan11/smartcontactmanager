package com.mdtech.smartcontactmanager.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.mdtech.smartcontactmanager.entity.ContactEntity;
import com.mdtech.smartcontactmanager.entity.UserEntity;
import com.mdtech.smartcontactmanager.repository.ContactRepository;
import com.mdtech.smartcontactmanager.repository.UserRepository;

@RestController
public class SearchController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query,Principal principal) {
		System.out.println("Query : "+query);
		String username = principal.getName();
		UserEntity user = userRepository.getUserByemail(username);
		List<ContactEntity> contacts = contactRepository.findByFirstNameContainingAndUser(query, user);
		return ResponseEntity.ok(contacts);
		
	}

}
