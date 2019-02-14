package com.axmor;

/** 
 * Класс объекта Comment (комментарий).
 * 
 * @autor Venediktov V.S.
 * @version 1.0
*/
public class Comment {
	/**
	 * ID комментария
	 */
	private int id;
	/**
	 * содержание (текст) комментария
	 */
	private String text;
	/**
	 * ID соответствующей задачи
	 */
	private int issueId;
	/**
	 * автор комментария
	 */
	private String comAuthor;
	
	/**
	 * статус задачи после добавления комментария
	 */
	private IssueStatus issueStatus;
	/**
	 * дата добавления комментария
	 */
	private String comDate;
	
	/** 
     * Конструктор - создание нового объекта Issue (задача)
     */
	public Comment(int id, String text, int issueId, String comAuthor, IssueStatus issueStatus, String comDate) {
		this.id = id;
		this.text = text;
		this.issueId = issueId;
		this.comAuthor = comAuthor;
		this.issueStatus = issueStatus;
		this.comDate = comDate;
	}

	enum IssueStatus {

		Новая, 
		Выполняется, 
		Выполнена
	}
	
	//геттеры
	public int getId() {
		return id;
	}
	public String getText() {
		return text;
	}
	public int getIssueId() {
		return issueId;
	}
	public String getComAuthor() {
		return comAuthor;
	}
	public IssueStatus getIssueStatus() {
		return issueStatus;
	}
	public String getComDate() {
		return comDate;
	}
	
	@Override
	public String toString() {
		return "Comment id:"+id+" {text: "+text+", issueId: "+issueId+", comAuthor:"+comAuthor+", issueStatus: "+issueStatus+", comDate: "+comDate+"}";
	}
}

