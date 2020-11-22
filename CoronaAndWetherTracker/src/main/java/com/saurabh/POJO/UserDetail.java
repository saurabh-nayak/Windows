package com.saurabh.POJO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "userDetail" ,schema = "spring")
public class UserDetail {
	@Id
	int id;
	
	/*Validation of field*/
	@Size(min = 5,message = "should have minimum 5 characters")
	
	@Column(name = "name")
	String name;
	@Column(name="address")
	String address;
	@Column(name="mobile")
	String mobile;
	
	@Column(name ="lastLogin")
	String lastLogin;
	
	@Column(name="lastModified")
	String lastModified;


	public String getLastLogin() {
		return lastLogin;
	}


	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}


	public String getLastModified() {
		return lastModified;
	}


	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	

}
