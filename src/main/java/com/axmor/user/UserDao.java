package com.axmor.user;

public interface UserDao {
	
	/** 
	 * Метод применяется для аутентификации пользователя,
	 * возвращает имя пользователя userName
	 * 
	 * @return имя пользователя userName 
	 */
	public String userAuthentication(String login, String password);
}
