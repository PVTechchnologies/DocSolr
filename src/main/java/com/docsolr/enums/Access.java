package com.docsolr.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Rajkiran
 *
 */
public enum Access {

    READ(new HashSet<String>(Arrays.asList("getChallenges"))),
    CREATE(new HashSet<String>(Arrays.asList("saveEntity","saveUpdateEntity","saveTheChallengeObject"))),
    UPDATE(new HashSet<String>(Arrays.asList("updateEntity"))), 
    DELETE(new HashSet<String>(Arrays.asList("deleteEntity"))),
	SYSTEMADMINISTRATOR("System Administrator"),
	STANDARDUSER("Standard User");
	public String value;
	
	public Set<String> methodSet;
	
	public static Map<String, String> childEntityMap = new HashMap<String,String>(){
		{
			put("ChallengeParticipantFile", "ChallengeDto");
			put("GroupMemeber", "Group");
		}
	};
	
	public static Map<String, String> globalDefalutAccessEntityMap = new HashMap<String,String>(){
		{
			put("UserEntity", "UserEntity");
			put("PaymentgatewayUserInfo","PaymentgatewayUserInfo");
			put("PaymentgatewayUserTransaction","PaymentgatewayUserTransaction");
		}
	};
	
	public static Map<String, String> requestUrlMap = new HashMap<String,String>(){
		{
			put("/restaurant/grouprequest", "usergroup");
			put("/restaurant/menuCategories","resort");
			put("/videos","video");
		}
	};

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private Access(String value) {
		this.value = value;
	}
	
	private Access(Set<String> methodSet) {
		this.methodSet = methodSet;
	}
	
	public static Access getByValue(String value)
	{
		Access access=null;
		for(Access acc:Access.values())
		{
			if(acc.getValue().equals(value))
			{
				access=acc;
				break;
			}
			
		}
		return access;
	}


	public Set<String> getMethodSet() {
		return methodSet;
	}

	public void setMethodSet(Set<String> methodSet) {
		this.methodSet = methodSet;
	}
}
