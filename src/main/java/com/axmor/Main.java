package com.axmor;

import static spark.Spark.*;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import freemarker.template.Configuration;
import freemarker.template.Template;
/** 
 * Класс Main обеспечивает запуск и работу веб-приложения Issue Tracker
 * с применением микрофреймворка Spark (http://sparkjava.com),
 * содержит Routes и Filters.
 * 
 * @autor Venediktov V.S.
 * @version 1.0
*/
public class Main {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		port(80);
		staticFiles.location("/public");
		
		/**
	     * аутентификация пользователя
	     */
		get("/auth", (request, response) -> {
			
			String login = request.queryParams("log");
			String password = request.queryParams("pass");
			String userName;
			String answer;
			
			userName = DataBase.userAuthentication(login, password);
			if (userName.equals("")) {
				answer = "Введен неверный логин или пароль";
			}
			else {
				answer = "Вход выполнен<br><a href = '/issueTracker/issues'>Перейти к списку задач</a>";//ответ после аутентификации
			}
			response.cookie("userName", userName);//создадим cookie с именем пользователя
			return answer;
		});
		
		/**
		 * проверка авторизации перед каждым запросом вида "/issueTracker/*"
		 */
		before("/issueTracker/*",(request, response) -> {
			
			if (request.cookie("userName").equals("")) {
				halt(401, "Вы не авторизованы<br><a href = '/auth.html'>Войти в систему</a>");
			}
		});
		
		/**
		 * показать список задач
		 */
		get("/issueTracker/issues", (request, response) -> {

			Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
			configuration.setClassForTemplateLoading(Main.class, "/");
			configuration.setDefaultEncoding("UTF-8");
			StringWriter writer;
			Template template = configuration.getTemplate("issuesList.ftl");
			writer = new StringWriter();

			Map<String, ArrayList<Issue>> map = new HashMap<String, ArrayList<Issue>>();
			map.put("issuesList", DataBase.getIssuesList());
			
			template.process(map, writer);

			return writer;
		});
		
		/**
		 * редактирование задачи
		 */
		get("/issueTracker/editIssue/:id", (request, response) -> {
			
			Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
			configuration.setClassForTemplateLoading(Main.class, "/");
			configuration.setDefaultEncoding("UTF-8");
			StringWriter writer;
			Template template = configuration.getTemplate("editIssue.ftl");
			writer = new StringWriter();
			
			Map<String, String> map = new HashMap<String, String>();
			
			int id = Integer.parseInt(request.params(":id"));

			Issue issue = DataBase.getIssueByID(id);
			map.put("issueId", Integer.toString(id));
			map.put("issueName", issue.getName());
			map.put("issueAuthor", issue.getAuthor());
			map.put("issueDescription", issue.getDescription());
			map.put("issueStatus", issue.getStatus());
			map.put("issueStartDate", issue.getStartDate());
			map.put("issueComment", issue.getComment());
			
			template.process(map, writer);

			return writer;
		});
		
		/**
		 * внести изменения в задачу - изменить название, изменить статус, добавить комментарий
		 */
		put("/issueTracker/editIssueChange", (request, response) -> {
			
			/**
			 * id изменяемой задачи
			 */
			int id = Integer.parseInt(request.queryParams("id"));
			
			/**
			 * текущие значения полей редактируемой задачи
			 */
			String oldName = DataBase.getNameByID(id);
			String oldStatus = DataBase.getStatusByID(id);
			String oldComment = DataBase.getCommentByID(id);
			
			/**
			 * новые значения полей редактируемой задачи
			 */
			String newName = request.queryParams("name");//название
			String newComment = request.queryParams("comment");//комментарий
			String newStatus = request.queryParams("status");//статус
			
			String comment = "";//общий итоговый комментарий
			
			String answer = "в задачу '"+oldName+"' не внесено изменений";
			
			//добавим новый комментарий (если он непустой) к текущему
			if (!newComment.equals("")&&(newComment != null)) {
				comment+=newComment+"//";
			}
			//изменим название, если оно отличается от текущего (и поле не пустое)
			if (!newName.equals(oldName)&&(!newName.equals(""))&&(newName != null)) {
				DataBase.changeIssueName(id, newName);
				comment+= " Название ->'"+newName+"',";
			}
			//изменим статус, если он отличается от текущего
			if (!newStatus.equals(oldStatus)) {
				DataBase.changeIssueStatus(id, newStatus);
				comment+= " Cтатус '"+newStatus+"',";
			}
			//запишем дату изменения и автора
			comment+= " Дата изм. "+(new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date()))+" "+request.cookie("userName");
			comment = oldComment+"\n"+comment;
			
			if (DataBase.changeIssueComment(id, comment)) {
				answer = "в задачу '"+oldName+"' внесены изменения";
			}
			return answer;
		});
		
		/**
		 * создать новую задачу
		 */
		post("/issueTracker/createIssue", (request, response) -> {
			
			/**
			 * поля новой задачи
			 */
			String name = request.queryParams("name");// название
			String author = request.queryParams("author");// автор
			String description = request.queryParams("description");//описание
			
			String answer = "Не удалось создать новую задачу";
			
			//если поле название осталось незаполненным - указываем 'Новая задача'
			if (name.equals("")||(name == null)) {
				name = "Новая задача";
			}
			//если поле автор осталось незаполненным - указываем текущего пользователя (из cookie)
			if (author.equals("")||(author == null)) {
				author = request.cookie("userName");
			}
			
			if (DataBase.createNewIssue(name, author, description)) {
				answer = "Новая задача cоздана";
			}
			return answer;
		});
		
		/**
		 * удалить задачу
		 */
		delete("/issueTracker/editIssueDelete/:id", (request, response) -> {

			int id = Integer.parseInt(request.params(":id"));
			String answer = "Не удалось удалить задачу";
			
			if (DataBase.deleteIssue(id)) {
				answer = "Задача удалена";
			}
			return answer;
		});

	}
}
