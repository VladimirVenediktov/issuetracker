<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Issue List</title>
		<style type="text/css">
			TABLE {
				text-align: center;
				width: 50%;
				height: 10%;
				background: #99ff99;
				border: 2px solid #000;
				border-collapse: collapse;
				margin: auto;
			}
		
			TD, TH {
				padding: 5px;
				border: 1px solid #000;
			}
		</style>
	</head>
	<body>
    	<table>
			<caption style="font-size:25px">Issues List</caption>
			<tr>
				<th>№</th>
				<th>name</th>
				<th>status</th>
				<th>start date</th>
				<th>comment</th>
			</tr>
			<#list issuesList as Issue>
			<tr>
				<td>${Issue?counter}</td>
				<td><a href="/issueTracker/editIssue/${Issue.id}">${Issue.name}</a></td>
				<td>${Issue.status}</td>
				<td>${Issue.startDate}</td>
				<td><textarea rows="7" cols="70" disabled>${Issue.comment}</textarea></td>
			</tr>
			</#list>
		</table>
		<p align="center"><a href="/createIssue.html" title="Добавить новую задачу">Новая задача</a></p>
	</body>
</html>