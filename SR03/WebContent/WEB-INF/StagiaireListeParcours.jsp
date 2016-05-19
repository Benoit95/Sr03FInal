<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Liste des parcours</title>
		  <link href="<c:url value="/inc/materialize.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
		  <link href="<c:url value="/inc/style.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
    </head>
    <body>
    
    	<%@ include file="NavBar.jsp" %>

        <div class="container">
        <fieldset>
        <h5 class="header col s12 light">Liste des Parcours</h5>
        
        <div id="corps">
        <c:choose>
            <%-- Si aucun questionnaire n'existe, affichage d'un message par défaut. --%>
            <c:when test="${ empty L_questionnaires }">
                <p class="erreur">Aucun questionnaire n'a été fait pour le moment.</p>
            </c:when>
            <%-- Sinon, affichage du tableau. --%>
            <c:otherwise>
            <table class = "centered">
                <thead><tr>
                    <th>ID</th>
                    <th>Sujet</th>
                    <th>Statut</th>
                    <th class="action">Visualiser le questionnaire</th>                 
                </tr></thead>
                <%-- Parcours de la Map des Questionnaires dans la requete, et utilisation de l'objet varStatus. --%>
                <tbody>
                <c:forEach items="${ L_questionnaires }" var="mapQuestionnaires" varStatus="boucle">
              	
                <tr>
                    <%-- Affichage des propriétés du bean Questionnaire, qui est stocké en tant que valeur de l'entrée courante de la map --%>
                    <td><c:out value="${ mapQuestionnaires.id }"/></td>
                    <td><c:out value="${ mapQuestionnaires.sujet }"/></td>
                    <td><c:out value="${ mapQuestionnaires.statut }"/></td>
                    <%-- Lien vers les servlets de gestions--%>
                    <td class="action">
                        <a href="<c:url value="StagiaireVisualiseQuestionnaire"><c:param name="IDquestionnaire" value="${ mapQuestionnaires.id }" /><c:param name="page" value="1" /></c:url>">Visualiser</a>
                    </td>
                </tr>
                </c:forEach>
                </tbody>
              </table>
            </c:otherwise>
        </c:choose>
        
        <c:if test="${sessionScope.page > 1}">
	   		<a href="<c:url value="StagiaireListeParcours"><c:param name="page" value="${ sessionScope.page - 1 }" /></c:url>">Précédent</a>
        </c:if>
        <c:if test="${sessionScope.page < sessionScope.pageMax }">
      	 <a href="<c:url value="StagiaireListeParcours"><c:param name="page" value="${ sessionScope.page + 1 }" /></c:url>">Suivant</a>
      	</c:if>
        
        
        </div>
        </fieldset>
        </div>
        
        <%@ include file="footer.jsp" %>
        
    </body>
</html>