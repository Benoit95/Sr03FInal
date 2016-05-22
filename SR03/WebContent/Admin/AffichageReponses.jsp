<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Liste des Reponse existants</title>
		  <link href="<c:url value="/inc/materialize.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
		  <link href="<c:url value="/inc/style.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
    </head>
    <body>
        <%@ include file="MenuAdmin.jsp" %>
        
        <%@ include file="RechercheForm.jsp" %>
                
        <div class="container">
        <fieldset>
        
        <h5 class="header col s12 light">Reponses de la Question n° ${ empty QuestionID ? param.IdQuestion : QuestionID  }</h5>
        <div id="corps">
        <c:choose>
            <%-- Si la liste des Reponses est vide, affichage d'un message par défaut. --%>
            <c:when test="${ empty L_Reponses }">
                <p class="erreur">Aucune Reponse enregistrée.</p>
            </c:when>
            <%-- Sinon, affichage du tableau. --%>
            <c:otherwise>
            <table class = "centered">
                <thead>
                <tr>
                	<th>Question n°</th>
                    <th>Reponse n°</th>
                    <th>Text</th>
                    <th>Statut</th>
                    <th>Ordre</th>
                    <th>Est correcte?</th>
                    <th class="action">Suppression</th>
                    <th class="action">Modification</th>                  
                </tr></thead>
                <%-- Parcours de la Map des Reponses dans la requete, et utilisation de l'objet varStatus. --%>
                <tbody>
                <c:forEach items="${ L_Reponses }" var="mapReponses" varStatus="boucle">
              
                <tr>
                    <%-- Affichage des propriétés du bean Reponse, qui est stocké en tant que valeur de l'entrée courante de la map --%>
                    <td><c:out value="${ mapReponses.id_question }"/></td>
                    <td><c:out value="${ mapReponses.id }"/></td>
                    <td><c:out value="${ mapReponses.text }"/></td>
                    <td><c:out value="${ mapReponses.statut }"/></td>
                    <td><c:out value="${ mapReponses.ordre }"/></td>
                    <td><c:out value="${ mapReponses.estValide }"/></td>
                    <%-- Lien vers les servlets de gestions--%>
                    <td class="action">
                        <a href="<c:url value="/Admin/GestionReponses"><c:param name="QuestionID" value="${ mapReponses.id_question }" /><c:param name="ReponseID_to_delete" value="${ mapReponses.id }" /></c:url>">Supprimer</a>
                    </td>
                    <td class="action">
                        <a href="<c:url value="/Admin/ModifReponses"><c:param name="QuestionID" value="${ mapReponses.id_question }" /><c:param name="ReponseID_to_modif" value="${ mapReponses.id }" /></c:url>">Modifier</a>
                    </td>
                </tr>
                </c:forEach>
                </tbody>
 
            </table> 
            </c:otherwise>
        </c:choose>
        
        <c:if test="${sessionScope.page > 1}">
	   		<a href="<c:url value="/Admin/GestionReponses"><c:param name="page" value="${ sessionScope.page - 1 }" /><c:param name="QuestionID" value="${ empty QuestionID ? param.IdQuestion : QuestionID  }" /></c:url>">Précédent</a>
        </c:if>
        <c:if test="${sessionScope.page < sessionScope.pageMax }">
      	 <a href="<c:url value="/Admin/GestionReponses"><c:param name="page" value="${ sessionScope.page + 1 }" /><c:param name="QuestionID" value="${ empty QuestionID ? param.IdQuestion : QuestionID  }" /></c:url>">Suivant</a>
      	</c:if>
        
        </div>
        </fieldset>
        </div>
        
    </body>
</html>