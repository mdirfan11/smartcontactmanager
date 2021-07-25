package com.mdtech.smartcontactmanager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="tbl_contact")
public class ContactEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="cnt_id")
	private Long id;
	
	@NotNull
	@NotBlank
	@Size(min=2, max=50, message="{user.firstname.invalid}")
	@Column(name="first_name", length=50, nullable=false)
	private String firstName;
	
	@NotNull
	@NotBlank
	@Size(min=2, max=50, message="{user.lastname.invalid}")
	@Column(name="last_name", length=50, nullable=false)
	private String lastName;
	
	@NotNull
	@NotBlank
	@Pattern(regexp="[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}", message="{user.email.invalid}")
	@Column(name="email", length=100, nullable=false, unique=true)
	private String email;
	
	@Column(name="work_dtl", length=100)
	private String workDetail;
	
	@NotNull
	@NotBlank
	@Pattern(regexp="(^$|[0-9]{10})", message="{contact.no.invalid}")
	@Column(name="contact_no", nullable=false, length=10, unique=true)
	private String contactNo;
	
	@Column(name="image_url")
	private String imageUrl;
	//private String imageUrl;
	@Column(name="description", length=5000)
	private String description;
	
	@ManyToOne()
	@JsonIgnore
	private UserEntity user;
	
	public ContactEntity() {}

	public ContactEntity(Long id, String firstName, String lastName, String email, String workDetail, String contactNo,
			String imageUrl, String description, UserEntity user) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.workDetail = workDetail;
		this.contactNo = contactNo;
		this.imageUrl = imageUrl;
		this.description = description;
		this.user = user;
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

	public String getWorkDetail() {
		return workDetail;
	}

	public void setWorkDetail(String workDetail) {
		this.workDetail = workDetail;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "ContactEntity [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", workDetail=" + workDetail + ", contactNo=" + contactNo + ", imageUrl=" + imageUrl
				+ ", description=" + description + ", user=" + user + "]";
	}
	
	

}
