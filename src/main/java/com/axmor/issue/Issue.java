package com.axmor.issue;

import java.util.ArrayList;

import com.axmor.comment.Comment;

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
	private IssueStatus status;
	/**
	 * дата создания задачи
	 */
	private String startDate;
	/**
	 * комментарии к задаче
	 */
	private ArrayList<Comment> comments;
	
	/** 
     * Конструктор - создание нового объекта Issue (задача)
     */
	public Issue(int  id, String name, String author, String description, IssueStatus status, String startDate, ArrayList<Comment> comments) {
		this.id = id;
		this.name = name;
		this.author = author;
		this.description = description;
		this.status = status;
		this.startDate = startDate;
		this.comments = comments;
	}
	
	public enum IssueStatus{
		
		Новая,
		Выполняется,
		Выполнена
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
	
	public IssueStatus getStatus() {
		return status;
	}
	
	public String getStartDate() {
		return startDate;
	}
	
	public ArrayList<Comment> getComments() {
		return comments;
	}
	
	@Override
	public String toString() {
		return "Issue id:"+id+" {name: "+name+", auhor: "+author+", description: "+description+", status: "+status+", startDate: "+startDate+"}";
	}
}
