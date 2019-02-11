package com.axmor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.h2.tools.Server;

import com.zaxxer.hikari.HikariDataSource;
/** 
 * Класс содержит методы для соединения и работы с базой данных IssueTracker (H2).
 * 
 * @autor Venediktov V.S.
 * @version 1.0
*/
public class DataBase {

	/** Хранит список задач, полученных из БД IssueTracker*/
	public static ArrayList<Issue> issuesList = new ArrayList<Issue>();// список задач

	/** 
     * Метод для соединения с БД IssueTracker (H2, server mode) через HikariCP (connection pool)
     * 
     * @return возвращает connection
     */
	public static Connection getConnection() throws SQLException {
		Server server = Server.createTcpServer().start();// start the TCP Server
		HikariDataSource ds = new HikariDataSource();
		ds.setJdbcUrl("jdbc:h2:tcp:localhost/./IssueTracker");
		ds.setUsername("sa");
		ds.setPassword("");
		return ds.getConnection();
	}
	/** 
     * Метод для аутентификации пользователя
     * 
     * @param login - логин
     * @param password - пароль
     * @return имя пользователя userName
     */
	public static String userAuthentication(String login, String password) throws SQLException {

		String userName = "";

		Connection c = getConnection();
		String sql = "SELECT USERNAME FROM USERS WHERE LOGIN = ? AND PASSWORD = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, login);
		ps.setString(2, password);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			userName = rs.getString(1);
		}
		c.close();
		return userName;
	}
	/** 
     * Метод для получения из БД списка всех задач
     * 
     * @return список ArrayList
     */
	public static ArrayList<Issue> getIssuesList() throws SQLException {

		Connection c = getConnection();
		String sql = "SELECT * FROM ISSUES";
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		issuesList.clear();
		while (rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			String author = rs.getString("author");
			String description = rs.getString("description");
			String status = rs.getString("status");
			String startDate = rs.getString("startDate");
			String comment = rs.getString("comment");
			//добавляем задачи из БД в список
			issuesList.add(new Issue(id, name, author, description, status, startDate, comment));
		}
		c.close();
		return issuesList;
	}
	/** 
     * Метод для получения из БД задачи по заданному ID
     * 
     * @param id - ID задачи
     * @return объект класса Issue (задача)
     */
	public static Issue getIssueByID(int id) throws SQLException {

		Issue issue = null;
		for (int i = 0; i < issuesList.size(); i++) {
			if (issuesList.get(i).getId() == id) {
				issue = issuesList.get(i);
				break;
			}
		}
		return issue;
	}
	/** 
     * Метод для получения названия задачи по заданному ID
     * 
     * @param id - ID задачи
     * @return String name - название задачи
     */
	public static String getNameByID(int id) throws SQLException {

		return getIssueByID(id).getName();
	}
	/** 
     * Метод для получения статуса задачи по заданному ID
     * 
     * @param id - ID задачи
     * @return String status - статус задачи
     */
	public static String getStatusByID(int id) throws SQLException {

		return getIssueByID(id).getStatus();
	}
	/** 
     * Метод для получения комментария к задаче по заданному ID
     *
     * @param id - ID задачи
     * @return String comment - комментарий к задаче
     */
	public static String getCommentByID(int id) throws SQLException {

		return getIssueByID(id).getComment();
	}
	/** 
     * Метод для изменения названия задачи по заданному ID
     * 
     * @param id - ID задачи
     * @param newName - новое название задачи
     * @return true - название изменено, false - название не изменено
     */
	public static boolean changeIssueName(int id, String newName) throws SQLException {

		Connection c = getConnection();
		String sql = "UPDATE ISSUES SET NAME = ? WHERE id = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, newName);
		ps.setInt(2, id);
		if (ps.executeUpdate() > 0) {
			c.close();
			return true;
		} else {
			c.close();
			return false;
		}
	}
	/** 
     * Метод для изменения статуса задачи по заданному ID
     * 
     * @param id - ID задачи
     * @param newStatus - новый статус задачи
     * @return true - статус изменен, false - статус не изменен
     */
	public static boolean changeIssueStatus(int id, String newStatus) throws SQLException {

		Connection c = getConnection();
		String sql = "UPDATE ISSUES SET STATUS = ? WHERE id = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, newStatus);
		ps.setInt(2, id);
		if (ps.executeUpdate() > 0) {
			c.close();
			return true;
		} else {
			c.close();
			return false;
		}
	}
	/** 
     * Метод для изменения комментария к задаче по заданному ID
     * 
     * @param id - ID задачи
     * @param newComment - новый комментарий к задаче
     * @return true - комментарий добавлен, false - комментарий не добавлен
     */
	public static boolean changeIssueComment(int id, String newComment) throws SQLException {

		Connection c = getConnection();
		String sql = "UPDATE ISSUES SET COMMENT = ? WHERE id = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, newComment);
		ps.setInt(2, id);
		if (ps.executeUpdate() > 0) {
			c.close();
			return true;
		} else {
			c.close();
			return false;
		}
	}
	/** 
     * Метод для создания новой задачи
     * 
     * @param name - название
     * @param author - автор
     * @param description - описание задачи
     * @return true - задача создана, false - задача не создана
     */
	public static boolean createNewIssue(String name, String author, String description) throws SQLException {

		Connection c = getConnection();
		String sql = "INSERT INTO ISSUES (NAME, AUTHOR, DESCRIPTION, STATUS, STARTDATE, COMMENT) VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement ps = c.prepareStatement(sql);

		ps.setString(1, name);
		ps.setString(2, author);
		ps.setString(3, description);
		ps.setString(4, "Новая");
		ps.setString(5, (new SimpleDateFormat("dd.MM.yy HH:mm")).format(new Date()));
		ps.setString(6, "");

		if (ps.executeUpdate() > 0) {
			c.close();
			return true;
		} else {
			c.close();
			return false;
		}
	}
	/** 
     * Метод для удаления задачи по заданному ID
     * 
     * @param id - ID задачи
     * @return true - задача удалена, false - задача не удалена
     */
	public static boolean deleteIssue(int id) throws SQLException {

		Connection c = getConnection();

		String sql = "DELETE FROM ISSUES WHERE ID = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, id);

		if (ps.executeUpdate() > 0) {
			c.close();
			return true;
		} else {
			c.close();
			return false;
		}
	}
	
}
