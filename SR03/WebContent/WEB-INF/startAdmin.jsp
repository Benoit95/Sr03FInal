<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		  <link href="<c:url value="/inc/materialize.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
		  <link href="<c:url value="/inc/style.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
<title>Start Admin</title>
</head>
<body>
	<%@ include file="NavBar.jsp" %>

<div class="container">
    <div class="row center">

		<h1 class="header col s12 orange-text">Section Administrateur</h1>
		<h5 class="header col s12 light">Bienvenue ${sessionScope.sessionUtilisateur.prenom} ${ sessionScope.sessionUtilisateur.nom }</h5>
	</div>
      <section>
	      <p class="flow-text"> Etant administrateur vous pouvez:
	      </p>
		
      </section>
      
            

</div>

	<%@ include file="MenuAdmin.jsp" %>
	
	<%@ include file="footer.jsp" %>

</body>
</html>