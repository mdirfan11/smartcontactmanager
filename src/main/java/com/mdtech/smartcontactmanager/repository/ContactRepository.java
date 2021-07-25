package com.mdtech.smartcontactmanager.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mdtech.smartcontactmanager.entity.ContactEntity;
import com.mdtech.smartcontactmanager.entity.UserEntity;

public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
	
	@Query(" select c from ContactEntity c where c.user.id =:userId")
	/*
	 * pageable interface contain two information 
	 * 1. current page 
	 * 2. No. of record per page
	 */
	public Page<ContactEntity> getContactByUserId(@Param("userId") Long userId, Pageable pageable);
	
	//@Query("select c from CustomerEntity c where firstName like ")
	public List<ContactEntity> findByFirstNameContainingAndUser(String firstName, UserEntity user);

}
