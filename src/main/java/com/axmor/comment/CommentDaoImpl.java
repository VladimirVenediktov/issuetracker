package com.axmor.comment;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.axmor.comment.Comment.IssueStatus;
import com.axmor.connection.ConnectionPool;


public class CommentDaoImpl implements CommentDao {

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
	public CommentDaoImpl() throws IOException, SQLException {
		this.connection = ConnectionPool.getConnection();
	}
	
	/** 
	 * Метод для получения списка всех комментариев к задаче из БД
	 * 
	 * @param id - ID задачи 
	 * @return коллекция объектов comments
	 */
	@Override
	public ArrayList<Comment> getIssueComments(int issueId){
		
		final String ISSUE_ID = "issue_id";
		final String SELECT_ALL_ISSUES_COMMENTS = "SELECT * FROM COMMENT WHERE +"+ISSUE_ID+" = "+issueId;
		ArrayList<Comment> comments = new ArrayList<Comment>();// список комментариев к задаче
		
		try(PreparedStatement ps = connection.prepareStatement(SELECT_ALL_ISSUES_COMMENTS);
			ResultSet rs = ps.executeQuery()){
			
			comments.clear();
			while (rs.next()) {
				int id = rs.getInt("id");
				String text = rs.getString("text");
				String comAuthor = rs.getString("author");
				String issueStatus = rs.getString("issue_status");
				String comDate = rs.getString("date");
				IssueStatus status = IssueStatus.valueOf(issueStatus);
				//добавляем комментарий из табл. Comment в список
				comments.add(new Comment(id, text, issueId, comAuthor, status, comDate));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return comments;
	}
	
	/** 
     * Метод для добавления комментария к задаче с заданным ID
     * 
     * @param id - ID задачи
     * @param text - содержание (текст) комментария
     * @param comAuthor - автор комментария
     * @param status - измененный статус задачи
     * @param date - дата добавления комментария
     * @return true - комментарий добавлен, false - не добавлен
     */
	@Override
	public boolean addComment(int issueId, String text, String comAuthor, String status, String date){
		
		final String ISSUE_ID = "issue_id";
		final String TEXT = "text";
		final String COM_AUTHOR = "author";
		final String ISSUE_STATUS = "issue_status";
		final String DATE = "date";
		final String ADD_COMMENT = "INSERT INTO COMMENT ("+TEXT+", "+ISSUE_ID+", "+COM_AUTHOR+", "+ISSUE_STATUS+", "+DATE+") VALUES (?, ?, ?, ?, ?)";
		boolean res = false;
		
		try(PreparedStatement ps = connection.prepareStatement(ADD_COMMENT)){
			ps.setString(1, text);
			ps.setInt(2, issueId);
			ps.setString(3, comAuthor);
			ps.setString(4, status);
			ps.setString(5, date);
			res = (ps.executeUpdate() > 0)?true:false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
}
