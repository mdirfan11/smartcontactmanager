package com.mdtech.smartcontactmanager.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name="tbl_user")
public class UserEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="user_id")
	private Long id;
	
	@Size(min=2, max=50, message="{user.firstname.invalid}")
	@Column(name="first_name", length=50, nullable=false)
	private String firstName;
	
	@Size(min=3, max=50, message="{user.lastname.invalid}")
	@Column(name="last_name", length=50, nullable=false)
	private String lastName;
	
    @Pattern(regexp="[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}", message= "{user.email.invalid}")
	@Column(name="email", length=100, nullable=false, unique=true)
	private String email;
    
	@Column(name="about", length=500)
	private String about;
	
	@NotNull(message="{user.password.invalid}")
	@Column(name="password", nullable=false)
	private String password;
	
	@Column(name="active")
	private boolean active;
	
	@Column(name="role", length=15)
	private String role;
	
	@Column(name="image_url")
	private String imageUrl;
	
	@AssertTrue(message="You must agree terms and condition before submitting")
	@Column(name="agreement")
	private boolean agreement;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="user")
	private List<ContactEntity> contactList = new ArrayList<>();
	
	public UserEntity() {}
	
	

	public UserEntity(Long id, String firstName,
			String lastName,
			String email,
			String about, String password, boolean active,
			String role, String imageUrl,
			boolean agreement,
			List<ContactEntity> contactList) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.about = about;
		this.password = password;
		this.active = active;
		this.role = role;
		this.imageUrl = imageUrl;
		this.agreement = agreement;
		this.contactList = contactList;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public boolean isAgreement() {
		return agreement;
	}

	public void setAgreement(boolean agreement) {
		this.agreement = agreement;
	}

	public List<ContactEntity> getContactList() {
		return contactList;
	}

	public void setContactList(List<ContactEntity> contactList) {
		this.contactList = contactList;
	}

	@Override
	public String toString() {
		return "UserEntity [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", about=" + about + ", password=" + password + ", active=" + active + ", role=" + role
				+ ", imageUrl=" + imageUrl + ", agreement=" + agreement + ", contactList=" + contactList + "]";
	}
	
	
}
