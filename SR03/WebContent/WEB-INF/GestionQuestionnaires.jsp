<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Gestion questionnaire</title>
		  <link href="<c:url value="/inc/materialize.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
		  <link href="<c:url value="/inc/style.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
    </head>
    <body>
    	
    	<%@ include file="NavBar.jsp" %>
    
        <%@ include file="AffichageQuestionnaires.jsp" %>
        
        <br><br>
        <div class="container">
        
        <fieldset>
        
        <h5 class="header col s12 light">Ajout d'un questionnaire</h5>
        <div class="row">
            <form method="post" action="GestionQuestionnaires">
                    <div class="row center">
                    	<div class="input-field col m6 s12 offset-m1">
                    		<input type="text" id="sujetA" name="sujetA" value="<c:out value="${param.sujetA}"/>" size="30" maxlength="60" /><label class="active" for="sujetA">Sujet</label>
                    	</div>
						<div class="col m6 s12 offset-m1">
                   			<span class="erreur">${erreurs['sujetA']}</span>
                    	</div>
					<br /> <br /> 
       				</div>
       				
       				<div class="input-field col m6 s12 offset-m1">
	                    <input type="radio" id="statut1" name="statutA" value="actif" size="30" maxlength="60" ${ param.statut == 'actif' ? "checked ='checked'" : "checked ='checked'"  }/><label class="active" for="statut1">Actif</label>
	                    <input type="radio" id="statut2" name="statutA" value="inactif" size="30" maxlength="60" ${ param.statut == 'inactif' ? "checked ='checked'" : ''  }/><label class="active" for="statut2">Inactif</label> <br>
					</div>
					<br /> 
					
                <br><br>
                <input class="col m5 s12 waves-effect waves-light btn-large offset-m1" type="submit" value="Ajouter"  />
                
            </form>
            </div>

           </fieldset>
           <p class="erreur">${resultat}</p>
           </div>
            
             <%@ include file="footer.jsp" %>
        
    </body>
</html>