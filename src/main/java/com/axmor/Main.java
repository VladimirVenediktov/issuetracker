package com.axmor;

import static spark.Spark.*;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.axmor.comment.CommentDaoImpl;
import com.axmor.issue.Issue;
import com.axmor.issue.IssueDaoImpl;
import com.axmor.issue.Issue.IssueStatus;
import com.axmor.user.UserDaoImpl;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;

/** 
 * Класс Main обеспечивает запуск и работу веб-приложения Issue Tracker
 * с применением микрофреймворка Spark (http://sparkjava.com),
 * содержит Routes и Filters, метод конфигурации шаблона Freemarker. 
 * 
 * @autor Venediktov V.S.
 * @version 1.0
*/
public class Main {
	
	private static final String AUTH = "/auth";
	private static final String ISSUE_TRACKER = "/issueTracker/*";
	private static final String ISSUE_TRACKER_ISSUES = "/issueTracker/issues";
	private static final String ISSUE_TRACKER_EDIT_ISSUE = "/issueTracker/editIssue/:id";
	private static final String ISSUE_TRACKER_EDIT_ISSUE_CHANGE = "/issueTracker/editIssueChange";
	private static final String ISSUE_TRACKER_CREATE_ISSUE = "/issueTracker/createIssue";
	private static final String ISSUE_TRACKER_DELETE_ISSUE = "/issueTracker/editIssueDelete/:id";
	
	/**
	 * Конфигурация шаблона Freemarker
	 * 
	 * @param templateFile - название файла шаблона
	 * */
	private static Template freeMarkerTemplate (String templateFile) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
		configuration.setClassForTemplateLoading(Main.class, "/");
		configuration.setDefaultEncoding("UTF-8");
		Template template = configuration.getTemplate(templateFile);
		return template;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		port(80);
		staticFiles.location("/public");
		
		/**
	     * аутентификация пользователя (тестовые логин и пароль: user)
	     */
		get(AUTH, (request, response) -> {
			
			String login = request.queryParams("log");
			String password = request.queryParams("pass");
			String userName;
			String answer;
			String NON_AUTHENTICATED = "Введен неверный логин или пароль";
			//String AUTHENTICATED = "Вход выполнен<br><a href = '/issueTracker/issues'>Перейти к списку задач</a>";
			String AUTHENTICATED = "Вход выполнен";
			
			userName = new UserDaoImpl().userAuthentication(login, password);
			//ответ после аутентификации
			if (userName == null) {
				answer = NON_AUTHENTICATED;
			}
			else {
				answer = AUTHENTICATED;
			}
			response.cookie("userName", userName);//создадим cookie с именем пользователя
			return answer;
		});
		
		/**
		 * проверка авторизации перед каждым запросом вида ISSUE_TRACKER
		 */
		before(ISSUE_TRACKER,(request, response) -> {
			
			String NON_AUTHORIZATED = "<p style=\"text-align: center\">Вы не авторизованы<br><a href = '/auth.html'>Войти в систему</a></p>";
			
			if (request.cookie("userName").isEmpty()) {
				halt(401, NON_AUTHORIZATED);
			}
		});
		
		/**
		 * представление списка задач
		 */
		get(ISSUE_TRACKER_ISSUES, (request, response) -> {

			StringWriter writer = new StringWriter();
			Map<String, ArrayList<Issue>> map = new HashMap<String, ArrayList<Issue>>();
			map.put("issuesList", new IssueDaoImpl().getAllIssues());
			freeMarkerTemplate("issuesList.ftl").process(map, writer);
			return writer;
		});
		
		/**
		 * редактирование выбранной задачи
		 */
		get(ISSUE_TRACKER_EDIT_ISSUE, (request, response) -> {
			
			StringWriter writer = new StringWriter();
			Map<String, Issue> map = new HashMap<String, Issue>();
			int id = Integer.parseInt(request.params(":id"));
			Issue issue = new IssueDaoImpl().getIssueByID(id);
			map.put("issue", issue);
			freeMarkerTemplate("editIssue.ftl").process(map, writer);
			return writer;
		});
		
		/**
		 * изменения содержания задачи - изменить название, изменить статус, добавить комментарий
		 */
		put(ISSUE_TRACKER_EDIT_ISSUE_CHANGE, (request, response) -> {
			
			final String CHANGED  = "В текущую задачу внесены изменения";
			final String NOT_CHANGED = "Новый комментарий не добавлен, в текущую задачу не внесено изменений";
			boolean changeIssue = false;
			/**
			 * id изменяемой задачи
			 */
			int id = Integer.parseInt(request.queryParams("id"));
			
			Issue issue = new IssueDaoImpl().getIssueByID(id);//задача с указанным ID
			/**
			 * текущие название редактируемой задачи
			 */
			String oldName = issue.getName();
			IssueStatus oldStatus = issue.getStatus();
			/**
			 * новые значения полей редактируемой задачи
			 */
			String newName = (request.queryParams("name").isEmpty())?oldName:request.queryParams("name");//новое название либо старое, если новое не введено
			String newComment = request.queryParams("comment");//новый комментарий
			String newStatus = request.queryParams("status");//новый статус

			if (!(newStatus.equals(oldStatus.toString())&&newName.equals(oldName))) {
				changeIssue = new IssueDaoImpl().changeIssue(id, newName, newStatus);
			}
			boolean addComment = (newComment.isEmpty())?false:
					new CommentDaoImpl().addComment(id, newComment, request.cookie("userName"), newStatus, (new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date())));
			String answer = ((changeIssue||addComment)==true)?CHANGED:NOT_CHANGED;
			return answer;
		});
		
		/**
		 * создание новой задачи
		 */
		post(ISSUE_TRACKER_CREATE_ISSUE, (request, response) -> {
			
			final String NOT_CREATED = "Не удалось создать новую задачу";
			final String CREATED = "Новая задача cоздана";
			/**
			 * поля новой задачи
			 */
			String name = request.queryParams("name");// название
			String author = (request.queryParams("author").isEmpty())?request.cookie("userName"):request.queryParams("author");// автор (если поле не заполнено - пользователь из cookie)
			String description = request.queryParams("description");//описание
			
			boolean createIssue = new IssueDaoImpl().createIssue(name, author, description);
			String answer = (createIssue==true)?CREATED:NOT_CREATED;	
			return answer;
		});
		
		/**
		 * удаление задачи
		 */
		delete(ISSUE_TRACKER_DELETE_ISSUE, (request, response) -> {

			final String NOT_DELETED = "Не удалось удалить задачу";
			final String DELETED = "Задача удалена";
			int id = Integer.parseInt(request.params(":id"));
			
			boolean deleteIssue = new IssueDaoImpl().deleteIssue(id);
			String answer = (deleteIssue==true)?DELETED:NOT_DELETED;
			return answer;
		});

	}
}
