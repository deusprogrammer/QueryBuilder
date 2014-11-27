package com.trinary.db.entity.test;

import com.trinary.db.*;

public class Application {
	public static void main(String[] args) throws Exception {
		User user = new User("mmain", "password");
		
		Insert<User> insert = new Insert<User>(user);
		
		Select<User> select = new Select<User>(User.class);
		
		Update<User> update = new Update<User>(user);
		
		Delete<User> delete = (Delete<User>)new Delete<User>(user);
		
		insert.execute();
		select.execute();
		select
			.addFilter(new SimpleFilter("username", "=", "mmain"))
			.execute();
		select
			.exclude("password")
			.execute();
		select
			.include("username")
			.execute();
		update.execute();
		update
			.set("username", "fartknocker")
			.set("password", "notpassword")
			.addFilter(new SimpleFilter("username", "=", "mmain"))
			.addFilter(
				new OrFilter()
					.addFilter(new SimpleFilter("password", "=", "password"))
					.addFilter(new SimpleFilter("daysUp", ">", "1")))
			.execute();
		delete.execute();
	}
}