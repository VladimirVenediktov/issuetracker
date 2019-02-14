package com.axmor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.axmor.Issue.IssueStatus;

public class IssueDaoImpl implements IssueDao {

	/**
	 * Соединение с БД (инициализируется в конструкторе)
	 */
	private Connection connection;
	
	/** 
	 * Конструктор (получает соединение с БД)
	 * 
	 * @throws SQLException 
	 * @throws IOException
	 */
	public IssueDaoImpl() throws IOException, SQLException {
		this.connection = ConnectionPool.getConnection();
	}
	
	/** 
	 * Метод для полученя списка всех задач из БД 
	 * 
	 * @return коллекция объектов issuesList
	 */
	@Override
	public ArrayList<Issue> getAllIssues() {
		
		final String SELECT_ALL_ISSUES = "SELECT * FROM ISSUE";
		ArrayList<Issue> issuesList = new ArrayList<Issue>();// список задач
		
		try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL_ISSUES); ResultSet rs = ps.executeQuery()){
			issuesList.clear();
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String author = rs.getString("author");
				String description = rs.getString("description");
				String strStatus = rs.getString("status");
				String startDate = rs.getString("startDate");
				//получение элемента enum по строковому представлению его имени
				IssueStatus status = IssueStatus.valueOf(strStatus);
				//добавляем задачи из БД в список (поле comments - коллекция объектов comment)
				issuesList.add(new Issue(id, name, author, description, status, startDate, new CommentDaoImpl().getIssueComments(id)));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return issuesList;
	}
	
	/** 
     * Метод для получения из БД задачи по заданному ID
     * 
     * @param id - ID задачи
     * @return объект класса Issue (задача)
     */
	@Override
	public Issue getIssueByID(int id) {
		
		Issue issue = null;
		
		try (PreparedStatement ps = createPreparedStatement(id); ResultSet rs = ps.executeQuery()){
			if (rs.next()) {
				String name = rs.getString(2);
				String author = rs.getString(3);
				String description = rs.getString(4);
				String strStatus = rs.getString(5);
				String startDate = rs.getString(6);
				// получение элемента enum по строковому представлению его имени
				IssueStatus status = IssueStatus.valueOf(strStatus);
				issue = new Issue(id, name, author, description, status, startDate,
						new CommentDaoImpl().getIssueComments(id));
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return issue;
	}
	
	/** 
     * Метод для создания объекта PreparedStatement,
     * используемого в getIssueByID(int id) в try-with-resources
     * 
     * @param id - ID задачи
     * @return объект PreparedStatement
	 * @throws SQLException
     */
	private PreparedStatement createPreparedStatement(int id) throws SQLException {
		
		final String ID = "id";
		final String SELECT_ISSUE_BY_ID = "SELECT * FROM ISSUE WHERE " + ID + " = ?";
		
		PreparedStatement ps = connection.prepareStatement(SELECT_ISSUE_BY_ID);
		ps.setInt(1, id);
		return ps;
	}
	
	/** 
     * Метод для изменения задачи по заданному ID
     * 
     * @param id - ID задачи
     * @param newName - новое название задачи
     * @param newStatus - новый статус задачи
     * @return true - задача изменена, false - не изменена
     */
	@Override
	public boolean changeIssue(int id, String newName, String newStatus) {
		
		final String NAME = "name";
		final String STATUS = "status";
		final String ID = "id";
		final String UPDATE_ISSUE = "UPDATE ISSUE SET "+NAME+" = ?, "+STATUS+" = ? WHERE "+ID+" = ?";
		boolean res = false;
		
		try(PreparedStatement ps = connection.prepareStatement(UPDATE_ISSUE)){
			ps.setString(1, newName);
			ps.setString(2, newStatus);
			ps.setInt(3, id);
			res = (ps.executeUpdate() > 0)?true:false;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	/** 
     * Метод для создания новой задачи
     * 
     * @param name - название
     * @param author - автор
     * @param description - описание
     * @return true - задача создана, false - задача не создана
     */
	@Override
	public boolean createIssue(String name, String author, String description) {
		
		final String NAME = "name";
		final String AUTHOR = "author";
		final String DESCRIPTION = "description";
		final String STATUS = "status";
		final String STARTDATE = "startdate";
		final String CREATE_ISSUE = "INSERT INTO ISSUE ("+NAME+", "+AUTHOR+", "+DESCRIPTION+", "+STATUS+", "+STARTDATE+") VALUES (?, ?, ?, ?, ?)";
		boolean res = false;
		
		try(PreparedStatement ps = connection.prepareStatement(CREATE_ISSUE)){
			ps.setString(1, name);
			ps.setString(2, author);
			ps.setString(3, description);
			ps.setString(4, "Новая");
			ps.setString(5, (new SimpleDateFormat("dd.MM.yy HH:mm")).format(new Date()));
			res = (ps.executeUpdate() > 0)?true:false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	/** 
     * Метод для удаления задачи по заданному ID
     * 
     * @param id - ID задачи
     * @return true - задача удалена, false - задача не удалена
     */
	public boolean deleteIssue(int id) {
		
		final String ID = "id";
		final String DELETE_ISSUE = "DELETE FROM ISSUE WHERE "+ID+" = ?"; 
		boolean res = false;
		
		try (PreparedStatement ps = connection.prepareStatement(DELETE_ISSUE)){
			ps.setInt(1, id);
			res = (ps.executeUpdate() > 0)?true:false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
}
