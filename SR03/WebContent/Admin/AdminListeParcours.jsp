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
    
    	<%@ include file="/WEB-INF/NavBar.jsp" %>

        <div class="container">
        <fieldset>
        <h5 class="header col s12 light">Liste des Parcours de ${ stagiaire.prenom } ${ stagiaire.nom }</h5>
        
        <div id="corps">
        <c:choose>
            <%-- Si aucun questionnaire n'existe, affichage d'un message par défaut. --%>
            <c:when test="${ empty L_Parcours }">
                <p class="erreur">Aucun questionnaire n'a été fait pour le moment.</p>
            </c:when>
            <%-- Sinon, affichage du tableau. --%>
            <c:otherwise>
            <table class = "centered">
                <thead><tr>
                    <th>Questionnaire</th>
                    <th>Score</th>
                    <th>Durée (ms)</th>                
                </tr></thead>
                <%-- Parcours de la Map des Questionnaires dans la requete, et utilisation de l'objet varStatus. --%>
                <tbody>
                <c:forEach items="${ L_Parcours }" var="mapParcours" varStatus="boucle">
              	
                <tr>
                    <%-- Affichage des propriétés du bean PArcours_Visu, qui est stocké en tant que valeur de l'entrée courante de la map --%>
                    <td><c:out value="${ mapParcours.questionnaire.sujet }"/></td>
                    <td><c:out value="${ mapParcours.score }"/></td>
                    <td><c:out value="${ mapParcours.duree }"/></td>
                </tr>
                </c:forEach>
                </tbody>
              </table>
            </c:otherwise>
        </c:choose>
        
        <c:if test="${sessionScope.page > 1}">
	   		<a href="<c:url value="/Admin/AdminListeParcours"><c:param name="page" value="${ sessionScope.page - 1 }" /></c:url>">Précédent</a>
        </c:if>
        <c:if test="${sessionScope.page < sessionScope.pageMax }">
      	 <a href="<c:url value="/Admin/AdminListeParcours"><c:param name="page" value="${ sessionScope.page + 1 }" /></c:url>">Suivant</a>
      	</c:if>
        
        
        </div>
        </fieldset>
        </div>
        
        <%@ include file="/WEB-INF/footer.jsp" %>
        
    </body>
</html>