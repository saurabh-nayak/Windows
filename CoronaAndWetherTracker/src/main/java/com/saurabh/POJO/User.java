package com.saurabh.POJO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.ws.rs.DefaultValue;

import org.hibernate.validator.constraints.ISBN;

import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
@Table(name = "user", schema = "spring")
public class User {
	@Id
	private int id;
	
	@Column(name="userName")
	private String userName;
	
	/*Filtering of Object serialization*/
	@JsonIgnore
	
	@Column(name="pass")
	private String pass;
	@Column(name="roles")
	@DefaultValue(value="ROLE_USER")
	private String roles="ROLE_USER";
	@Column(name="status")
	@DefaultValue(value="0")
	private int status=0;
	
public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", roles=" + roles + "]";
	}
       
}
