<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		  <link href="<c:url value="/inc/materialize.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
		  <link href="<c:url value="/inc/style.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
		<title>Visualisation de questionnaire</title>
	</head>
	<body>
		
		<%@ include file="/WEB-INF/NavBar.jsp" %>
		<div class="container">
		     
		<h3 class="header col s12 orange-text">Qestionnaire n° ${ questionnaire.id } : "${ questionnaire.sujet }"</h3>
	    <c:forEach items="${ questionnaire.LQuestions }" var="question" varStatus="boucle">
	    <div class="row">
	    <fieldset>
             <legend>${ question.text }</legend>
	    	 <form method="post" action="RepondreQuestion">

	                   <c:forEach items="${ question.LReponses }" var="reponse" varStatus="boucle">
	                   		<p>
		                    <input type="radio" id="${ reponse.id }" name="${ question.id }" value="${ reponse.id }"/>
	 						<label class="active" for="${ reponse.id }">${ reponse.text } ${ reponse.estValide == 'oui' ? "(Bonne réponse)" : ""  } </label>
	 						</p>
	 					</c:forEach>
	 					<br>
	 					<%-- <input type="submit" value="Valider"  /> --%>
	                           
	            </form>
	            </fieldset>
	            <br>
	            </div>
	   </c:forEach>
	   
		<c:if test="${page > 1}">
	   		<a href="<c:url value="Questionnaire1by1"><c:param name="IDquestionnaire" value="${ questionnaire.id }" /><c:param name="page" value="${ page - 1 }" /></c:url>">Précédent</a>
        </c:if>
        <c:if test="${page < pageMax }">
      	 <a href="<c:url value="Questionnaire1by1"><c:param name="IDquestionnaire" value="${ questionnaire.id }" /><c:param name="page" value="${ page + 1 }" /></c:url>">Suivant</a>
      	</c:if>
      	
	   </div>
	  	
	   
	   <%@ include file="/WEB-INF/footer.jsp" %>
	   
	</body>
</html>