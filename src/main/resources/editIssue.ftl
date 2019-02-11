<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Edit Issue</title>
		<style type="text/css">
			TABLE {
			    text-align: center;
			    width: 25%; 
			    height: 30%;
			    background: #99ff99;
			    border: 2px solid #000;
			    border-collapse: collapse;
			    margin: auto;
			}
			TD, TH {
			    padding: 5px;
			    
			}
		</style>
		<script src="http://code.jquery.com/jquery-1.8.3.js"></script>
		<script>
			function changeIssue(){
				
				var name = $('#name').val();
				var status = $('#status').val();
				var comment = $('#comment').val();
				var dataStr = {name: name, status: status, comment: comment, id: ${issueId}};
			
				$.ajax(
					{
					type: "PUT",
					url: "/issueTracker/editIssueChange",
					data: dataStr,
					success:function(answer){
						$("div").html(answer);
						}
					}
				);
			}
		</script>
		<script>
			function deleteIssue(){
			
				$.ajax(
					{
					type: "DELETE",
					url: "/issueTracker/editIssueDelete/${issueId}",
					success:function(answer){
						$("div").html(answer);
						}
					}
				);
			}
		</script>
		
	</head>
	<body>
		<table>
			<caption style="font-size:25px">Issue Tracker: Редактирование задачи</caption>		
			<tr>
				<td>
					Название задачи: ${issueName}<br>
					Новое название задачи <input type="text" id="name">
				</td>
			</tr>	
			<tr>
				<td>
					Статус: ${issueStatus}
				</td>
			</tr>
			<tr>
				<td>			
					Дата создания: ${issueStartDate}
				</td>
			</tr>
			<tr>
				<td>
					Автор: ${issueAuthor}
				</td>
			</tr>
			<tr>
				<td>			
					Описание:<br>
					${issueDescription}
				</td>
			</tr>	
			<tr>
				<td>	
					Комментарии:<br>
					<textarea rows="7" cols="50" disabled>${issueComment}</textarea>
				</td>
			</tr>
			<tr>
				<td>		
					Добавить комментарий:<br>
					<textarea rows="5" cols="30" id="comment"></textarea>
				</td>
			</tr>			
			<tr>
				<td>		
					Изменить статус:</br>
					<select id="status">
				  		<option>Новая</option>
				  		<option>В процессе выполнения</option>
				  		<option>Выполнена</option>
					</select>	
				</td>
			</tr>		
			<tr>
				<td>		
					<button onclick="changeIssue()" style="margin-right:10px">Внести изменения</button>
					<button onclick="deleteIssue()">Удалить задачу</button>
				</td>
			</tr>
			<tr>
				<td>
					<div></div>
				</td>
			</tr>
			<tr>
				<td>
					<a href = '/issueTracker/issues'>Перейти к списку задач</a>
				</td>
			</tr>
		</table>						
	</body>
</html>