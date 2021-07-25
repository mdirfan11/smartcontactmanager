package com.mdtech.smartcontactmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mdtech.smartcontactmanager.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	@Query(" select u from UserEntity u where u.email =:email ")
	public UserEntity getUserByemail(@Param("email") String email);
	
}
