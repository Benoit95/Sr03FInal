<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		  <link href="<c:url value="/inc/materialize.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
		  <link href="<c:url value="/inc/style.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
<title>Start Stagiaire</title>
</head>
<body>

	<%@ include file="/WEB-INF/NavBar.jsp" %>

<div class="container">
    <div class="row center">

		<h1 class="header col s12 orange-text">Section Stagiaire</h1>
		<h5 class="header col s12 light">Bienvenue ${sessionScope.sessionUtilisateur.prenom} ${ sessionScope.sessionUtilisateur.nom }</h5>
	</div>
      <section>
	      <p class="flow-text"> Etant stagiaire vous pouvez (en utilisant la barre de navigation) :<br> <br> 
	      - Effectuer différents questionnaires.<br> 
	      - Voir vos anciens résultats (parcours).
	      </p>
		
      </section>

</div>
        <%@ include file="/WEB-INF/footer.jsp" %>
</body>
</html>