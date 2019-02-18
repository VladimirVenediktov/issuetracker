package com.axmor.user;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.axmor.connection.ConnectionPool;

public class UserDaoImpl implements UserDao {
	
	/**
	 * Соединение с БД (инициализируется в конструкторе)
	 */
	private Connection connection;
	
	public UserDaoImpl() throws IOException, SQLException {
		this.connection = ConnectionPool.getConnection();
	}
	
	/** 
     * Метод для аутентификации пользователя
     * 
     * @param login - логин
     * @param password - пароль
     * @return имя пользователя userName
     */
	@Override
	public String userAuthentication(String login, String password) {
		
		final String NAME_USER = "name";
		final String LOGIN = "login";
		final String PASSWORD = "password";
		final String FIND_USERNAME_BY_LOGIN_AND_PASSWORD = "SELECT "+NAME_USER+" FROM USER WHERE "+LOGIN+" = ? AND "+PASSWORD+" = ?";
		String userName = null;
		
		try (PreparedStatement ps = connection.prepareStatement(FIND_USERNAME_BY_LOGIN_AND_PASSWORD)){
			
			ps.setString(1, login);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
				
			if (rs.next()) {
				userName = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userName;
	}

}
