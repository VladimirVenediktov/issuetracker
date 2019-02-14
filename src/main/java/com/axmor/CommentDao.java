package com.axmor;

import java.util.ArrayList;

public interface CommentDao {
	
	/** 
	 * Метод для получения списка всех комментариев к задаче из БД
	 * 
	 * @param id - ID задачи 
	 * @return коллекция объектов comments
	 */
	public ArrayList<Comment> getIssueComments(int id);

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
	boolean addComment(int issueId, String text, String comAuthor, String status, String date);
}
