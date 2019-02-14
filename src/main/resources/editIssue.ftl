<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Edit Issue</title>
		<style type="text/css">
			TABLE {
			    font-family: Verdana;
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
			function formValid(){
			
				var name = $('#newName').val();
				var comment = $('#newComment').val();
				var notMatched = "Неверный формат введенных данных!\nМожно использовать следующие символы: буквы, цифры, точка, запятая, ?, !, №, скобки (), пробел, нижнее подчеркивание (_), дефис";
				var pattern = /^[A-ZА-яa-zа-я0-9№\(\),?! _\.-]*$/;
				
				if (!(pattern.test(name)&&pattern.test(comment))) {
					alert(notMatched);
					return;
				}
				changeIssue();
			}
		</script>
		<script>
			function changeIssue(){
				
				var name = $('#newName').val();
				var status = $('#newStatus').val();
				var comment = $('#newComment').val();
				var dataStr = {name: name, status: status, comment: comment, id: ${issue.id}};
			
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
					url: "/issueTracker/editIssueDelete/${issue.id}",
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
				<td>Название задачи: ${issue.name}</td>
			</tr>	
			<tr>
				<td>Статус: ${issue.status}</td>	
			</tr>
			<tr>
				<td>Дата создания: ${issue.startDate}</td>			
			</tr>
			<tr>
				<td>Автор: ${issue.author}</td>
			</tr>
			<tr>
				<td>Описание:<br>${issue.description}</td>			
			</tr>	
			<tr>
				<td>	
					<table border = 2px>
						<caption style="font-size:16px">Комментарии:</caption>
						<tr>
							<th>№</th>
							<th>Комментарий</th>
							<th>Автор</th>
							<th>Статус задачи</th>
							<th>Дата</th>
						</tr>
						<#list issue.comments as comment>
						<tr>
							<td>${comment?counter}</td>
							<td>${comment.text}</td>
							<td>${comment.comAuthor}</td>
							<td>${comment.issueStatus}</td>
							<td>${comment.comDate}</td>
						</tr>
						</#list>
					</table>	
				</td>
			</tr>
			<tr>
				<td>
					<table>
						<caption style="font-size:18px">Добавить комментарий</caption>
						<tr>
							<td>Новое название задачи <input type="text" id="newName"></td>
						</tr>
						<tr>
							<td>Добавить комментарий:<br><textarea rows="5" cols="50" id="newComment"></textarea></td>
						</tr>
						<tr>		
							<td>Изменить статус:</br>
								<select id="newStatus">
							  		<option>Новая</option>
							  		<option>Выполняется</option>
							  		<option>Выполнена</option>
								</select>	
							</td>
						</tr>	
					</table>	
				</td>
			</tr>
			<tr>
				<td>		
					<button onclick="formValid()" style="margin-right:10px">Внести изменения</button>
					<button onclick="deleteIssue()">Удалить задачу</button>
				</td>
			</tr>
			<tr>
				<td><div></div></td>
			</tr>
			<tr>
				<td><a href = '/issueTracker/issues'>Перейти к списку задач</a></td>
			</tr>
		</table>						
	</body>
</html>