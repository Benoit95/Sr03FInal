<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Liste des questions existants</title>
		  <link href="<c:url value="/inc/materialize.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
		  <link href="<c:url value="/inc/style.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
    </head>
    <body>
        <%@ include file="MenuAdmin.jsp" %>
        
        <%@ include file="RechercheForm.jsp" %>
        
        <div class="container">
        <fieldset>
        
        
        <h5 class="header col s12 light">Questions du Questionnaire n° ${ empty QuestionnaireID ? param.IdQuestionnaire : QuestionnaireID  }</h5>
        <div id="corps">
        <c:choose>
            <%-- Si la liste des questions est vide, affichage d'un message par défaut. --%>
            <c:when test="${ empty L_Questions }">
                <p class="erreur">Aucune Question enregistrée.</p>
            </c:when>
            <%-- Sinon, affichage du tableau. --%>
            <c:otherwise>
            <table class = "centered">
                <thead><tr>
                	<th>Questionnaire n°</th>
                    <th>Question n°</th>
                    <th>Text</th>
                    <th>Statut</th>
                    <th>Ordre</th>
                    <th class="action">Réponses</th>
                    <th class="action">Suppression</th>
                    <th class="action">Modification</th>                  
                </tr></thead>
                <%-- Parcours de la Map des Questions dans la requete, et utilisation de l'objet varStatus. --%>
                <tbody>
                <c:forEach items="${ L_Questions }" var="mapQuestions" varStatus="boucle">
              
                <tr>
                    <%-- Affichage des propriétés du bean Question, qui est stocké en tant que valeur de l'entrée courante de la map --%>
                    <td><c:out value="${ mapQuestions.id_questionnaire }"/></td>
                    <td><c:out value="${ mapQuestions.id }"/></td>
                    <td><c:out value="${ mapQuestions.text }"/></td>
                    <td><c:out value="${ mapQuestions.statut }"/></td>
                    <td><c:out value="${ mapQuestions.ordre }"/></td>
                    <%-- Lien vers les servlets de gestions--%>
                    <td class="action">
                        <a href="<c:url value="/Admin/GestionReponses"><c:param name="page" value="1" /><c:param name="QuestionID" value="${ mapQuestions.id }" /></c:url>">Gérer</a>
                    </td>
                    <td class="action">
                        <a href="<c:url value="/Admin/GestionQuestions"><c:param name="QuestionnaireID" value="${ mapQuestions.id_questionnaire }" /><c:param name="QuestionID_to_delete" value="${ mapQuestions.id }" /></c:url>">Supprimer</a>
                    </td>
                    <td class="action">
                        <a href="<c:url value="/Admin/ModifQuestions"><c:param name="QuestionnaireID" value="${ mapQuestions.id_questionnaire }" /><c:param name="QuestionID_to_modif" value="${ mapQuestions.id }" /></c:url>">Modifier</a>
                    </td>
                </tr>
                </c:forEach>
                </tbody>
 
            </table> 
            </c:otherwise>
        </c:choose>
        
        <c:if test="${sessionScope.page > 1}">
	   		<a href="<c:url value="/Admin/GestionQuestions"><c:param name="page" value="${ sessionScope.page - 1 }" /><c:param name="QuestionnaireID" value="${ empty QuestionnaireID ? param.IdQuestionnaire : QuestionnaireID  }" /></c:url>">Précédent</a>
        </c:if>
        <c:if test="${sessionScope.page < sessionScope.pageMax }">
      	 <a href="<c:url value="/Admin/GestionQuestions"><c:param name="page" value="${ sessionScope.page + 1 }" /><c:param name="QuestionnaireID" value="${ empty QuestionnaireID ? param.IdQuestionnaire : QuestionnaireID  }" /></c:url>">Suivant</a>
      	</c:if>
        
        </div>
        </fieldset>
        
        </div>
        
    </body>
</html>