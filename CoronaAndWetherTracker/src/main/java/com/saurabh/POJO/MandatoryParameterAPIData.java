package com.saurabh.POJO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "MandatoryParameterAPIData" , schema = "spring")
public class MandatoryParameterAPIData
{
	@Id
	@Column(name = "APIId")
    int APIId;
	
	@Column(name = "APIName")
     String APIName;
	
	@Column(name = "mandatoryParameters")
     String mandatoryParameters;
	
public int getAPIId() {
	return APIId;
}
public void setAPIId(int aPIId) {
	APIId = aPIId;
}
public String getAPIName() {
	return APIName;
}
public void setAPIName(String aPIName) {
	APIName = aPIName;
}
public String getMandatoryParameters() {
	return mandatoryParameters;
}
public void setMandatoryParameters(String mandatoryParameters) {
	this.mandatoryParameters = mandatoryParameters;
}
  
}
