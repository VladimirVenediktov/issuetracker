package com.axmor;
/** 
 * Класс объекта Issue (задача).
 * 
 * @autor Venediktov V.S.
 * @version 1.0
*/
public class Issue {
	/**
	 * ID задачи
	 */
	private int id;
	/**
	 * название задачи
	 */
	private String name;
	/**
	 * автор задачи
	 */
	private String author;
	/**
	 * описание задачи
	 */
	private String description;
	/**
	 * статус задачи
	 */
	private String status;
	/**
	 * дата создания задачи
	 */
	private String startDate;
	/**
	 * комментарии к задаче
	 */
	private String comment;
	
	/** 
     * Конструктор - создание нового объекта Issue (задача)
     */
	public Issue(int  id, String name, String author, String description, String status, String startDate, String comment) {
		this.id = id;
		this.name = name;
		this.author = author;
		this.description = description;
		this.status = status;
		this.startDate = startDate;
		this.comment = comment;
	}
	
	//геттеры
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getStatus() {
		return status;
	}
	
	public String getStartDate() {
		return startDate;
	}
	
	public String getComment() {
		return comment;
	}
	
	@Override
	public String toString() {
		return name+" - "+status;
	}
}
