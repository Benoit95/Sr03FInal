<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		  <link href="<c:url value="/inc/materialize.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
		  <link href="<c:url value="/inc/style.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
		<title>Footer</title>
	</head>
	<body>
       <!-- NavBar -->
       <nav class="light-blue lighten-1 fixed" role="navigation">
           <div class="nav-wrapper container">
               <img class="responsive-img" src="UTC_logo.png" alt="Photo_non_disponible_mini.png">
               <c:if test="${empty sessionScope.sessionUtilisateur}">
	               <ul class="right hide-on-med-and-down">
	                   <li><a href="login">Se connecter</a></li>
	               </ul>
               </c:if>
               <c:if test="${!empty sessionScope.sessionUtilisateur}">
               		<!-- Administrateur -->
               		<c:if test="${sessionScope.sessionUtilisateur.admin == true}">          
		               <ul class="right hide-on-med-and-down">
		                   <li><a href="/SR03/deconnexion">Se deconnecter</a></li>
		               </ul>
		               <ul class="right hide-on-med-and-down">
		                   <li><a href="<c:url value="/Admin/GestionUser"><c:param name="page" value="1" /></c:url>">Gérer les utilisateurs</a></li>
		               </ul>  
		               <ul class="right hide-on-med-and-down">
		                   <li><a href="<c:url value="/Admin/GestionQuestionnaires"><c:param name="page" value="1" /></c:url>">Gérer les questionnaires</a></li>
		               </ul>  
		            </c:if>
		            
		            <!-- Stagiaire -->
               		<c:if test="${sessionScope.sessionUtilisateur.admin == false}">          
		               <ul class="right hide-on-med-and-down">
		                   <li><a href="/SR03/deconnexion">Se deconnecter</a></li>
		               </ul>
		               <ul class="right hide-on-med-and-down">
		                   <li><a href="<c:url value="/Stagiaire/StagiaireListeQuestionnaire"><c:param name="page" value="1" /></c:url>">Questionnaires</a></li>
		               </ul>  
		               <ul class="right hide-on-med-and-down">
		                   <li><a href="<c:url value="/Stagiaire/StagiaireListeParcours"><c:param name="page" value="1" /></c:url>">Mes résultats</a></li>
		               </ul>  
		            </c:if>
		            
	           </c:if>               
               <ul id="nav-mobile" class="side-nav">
                   <li><a href="login">Se connecter</a></li>
               </ul>
               <a href="Accueil" data-activates="nav-mobile" class="button-collapse"><i class="material-icons">menu</i></a>
           </div>
       </nav>
       <section> <br><br><br> </section>
	</body>
</html>