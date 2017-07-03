/**
 * 
 */
package com.docsolr.dto;

import com.docsolr.entity.Account;
import com.docsolr.entity.Users;

/**
 * @author Rajkiran Dewara
 *
 */
public class CurrentUserVO {

	private Users user;
	private Account account;
	
	public Long getAccountId() {
		return account.getId();
	}
	
	public Account getAccount() {
		return account;
	}
	
	public void setAccount(Account account) {
		this.account = account;
	}
	
	public Users getUser() {
		return user;
	}
	
	public void setUser(Users user) {
		this.user = user;
	}
}
