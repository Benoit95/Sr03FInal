<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
				  <link href="<c:url value="/inc/materialize.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
				  <link href="<c:url value="/inc/style.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
		<title></title>
	</head>
	
	<body>
	<br><br>
		<aside>
			<div class="container">
				    
			      <div class="collection ">
			    	<a class="collection-item" href="<c:url value="GestionUser"><c:param name="page" value="1" /></c:url>">Gérer les utilisateurs</a>
			        <a class="collection-item" href="<c:url value="GestionQuestionnaires"><c:param name="page" value="1" /></c:url>">Gérer les questionnaires</a>
			      </div>
		
				<br><br><br>
			</div>
		</aside>
	</body>
</html>