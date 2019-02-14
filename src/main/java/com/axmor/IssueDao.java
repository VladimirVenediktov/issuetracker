package com.axmor;

import java.util.ArrayList;

public interface IssueDao {
	
	/** получить список всех задач из БД 
	 *
	 * @return коллекция объектов issuesList
	 */
	public ArrayList<Issue> getAllIssues();
	
	/** 
     * Метод для получения из БД задачи по заданному ID
     * 
     * @param id - ID задачи
     * @return объект класса Issue (задача)
     */
	public Issue getIssueByID(int id);
	
	/** 
     * Метод для изменения задачи по заданному ID
     * 
     * @param id - ID задачи
     * @param newName - новое название задачи
     * @param newStatus - новый статус задачи
     * @return true - задача изменена, false - не изменена
     */
	public boolean changeIssue(int id, String newName, String newStatus);
	
	/** 
     * Метод для создания новой задачи
     * 
     * @param name - название
     * @param author - автор
     * @param description - описание
     * @return true - задача создана, false - задача не создана
     */
	public boolean createIssue(String name, String author, String description);
	
	/** 
     * Метод для удаления задачи по заданному ID
     * 
     * @param id - ID задачи
     * @return true - задача удалена, false - задача не удалена
     */
	public boolean deleteIssue(int id);
}
