package com.saurabh.Enum;

public enum ResponseConstant {
	MANDATORY_PARAMETERS_MISSING("mandatory parameters missing", -201),
	PASSWORD_MINIMUM("password should have minimum 4 characters", -202),
	SUCCESSFULL_SIGNUP("Successfully signed up", -203), ERROR_SIGNUP("error occured during signup", -204),
	BAD_CREDENTIAS("BAD CREDENTIALS", -205), SUCCESS_AUTHENTICATION("SUCCESS", -206),
	PASSWORD_CHANGED_SUCCESSFULL("password successfully changed please login again", -207),
	ERROR_CHANGE_PASSWORD("change password encountered a problem", -208),
	MANDATORY_MISSING_OR_PASSWORD_ARE_EQUAL("mandatory parameters missing or old password and new password are equal",
			-209),
	REQUEST_MAPPING_NOT_FOUND("REQUEST MAPPING NOT FOUND", -210),
	USER_STATUS_NOT_ACTIVE("USER STATUS NOT SET TO ACTIVE OR ADMIN NOT APPROAVED YOU TILL", -211),
	NO_UNAPPROVED_USER("NO USER LEFT TO APPROVE", -212),
	PLEASE_ENTER_LOCATION("Enter location into location field", -213),
	MANDATORY_PARAMETERS_OK("Mandatory Parameters are ok", 100),
	GET_MULTIPLE_API_PARAM_TO_VALIDATE("Multiple API Param found to validate ", -214),
	OLD_PASSWORD_AND_NEW_PASSWORD_ARE_EQUAL("Old and New Passwords are equal", -215),
    DATA_UPDATED("Data updated successfully ",200),
    PROBLEM_OCCURED_DURING_UPDATE_DATA("Problem occurred during update data",-217),
    ;
	String errorMessage;
	int errorCode;

	private ResponseConstant(String errorMessage, int errorCode) {
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}
}
