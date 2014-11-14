package com.trinary.db.entity.test;

import com.trinary.db.annotation.Column;
import com.trinary.db.annotation.Table;

@Table(name="esign_status_signer")
public class User {
	@Column(name="user_nm")
	protected String username;
	
	@Column(name="password_sha256")
	protected String password;
	
	@Column(name="days_up_nr")
	protected Integer daysUp = 0;

	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getDaysUp() {
		return daysUp;
	}

	public void setDaysUp(Integer daysUp) {
		this.daysUp = daysUp;
	}
}
