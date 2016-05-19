<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Login</title>
		  <link href="<c:url value="/inc/materialize.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
		  <link href="<c:url value="/inc/style.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
    </head>
    <body>
    
    	<%@ include file="NavBar.jsp" %>
    
		<div class="container">
		     <div class="row center">
		         <h1 class="header col s12 orange-text">Bienvenue</h1>
		         
		        <c:if test="${!empty sessionScope.sessionUtilisateur}">
                    <%-- Si l'utilisateur existe en session, alors on affiche son adresse email. --%>
                    <h5 class="header col s12 light"> Vous êtes déjà connecté(e) avec l'adresse : ${sessionScope.sessionUtilisateur.mail}</h5>
                </c:if>
                
		        <c:if test="${empty sessionScope.sessionUtilisateur}">
                    <h5 class="header col s12 light">Veuillez renseigner vos identifiants.</h5>
                </c:if>
		         
		     </div>
            <form method="post" action="login">
                <fieldset>
                    <legend>Se connecter</legend>
                    
                    <label for="emailUser">Adresse email <span class="requis">*</span></label>
                    <input type="email" id="emailUser" name="emailUser" value="<c:out value="${param.emailUser}"/>" size="30" maxlength="60" />
                    <span class="erreur">${erreurs['emailUser']}</span>
					<br /> 
					             
                    <label for="mdpUser">Mot de passe <span class="requis">*</span></label>
                    <input type="password" id="mdpUser" name="mdpUser" value="" size="30" maxlength="20" />
                    <span class="erreur">${erreurs['mdpUser']}</span>
					<br /> 
                    
                </fieldset>
                
                <br><br>
                
				<c:if test="${empty sessionScope.sessionUtilisateur}">
	                <div class="row center">
		                <input class="col m5 s12 waves-effect waves-light btn-large" type="submit" value="Connexion"  />
	                </div>
                </c:if>
            </form>
        </div>
        
        <%@ include file="footer.jsp" %>
    </body>
</html>