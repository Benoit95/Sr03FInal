<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Gestion Reponses</title>
		  <link href="<c:url value="/inc/materialize.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
		  <link href="<c:url value="/inc/style.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
    </head>
    <body>
    	
    	<%@ include file="NavBar.jsp" %>
    
        <%@ include file="AffichageReponses.jsp" %>
        <br><br>
        <div class="container">
        
        <fieldset>
                
        <h5 class="header col s12 light">Ajout d'une réponse</h5>
        <div class="row">
        
            <form method="post" action="GestionReponses">
                <div class="row center">

					<input type="hidden" id="IdQuestion" name="IdQuestion" value="<c:out value="${ empty QuestionID ? param.IdQuestion : QuestionID  }"/>" size="30" maxlength="60" />
					<div class="input-field col m6 s12 offset-m1">
	                    <label class="active" for="TextA">Intitulé</label>
	                    <input type="text" id="TextA" name="TextA" value="<c:out value="${param.TextA}"/>" size="30" maxlength="60" />
                  	</div>
                  	<div class="input-field col m6 s12 offset-m1">
                   		<span class="erreur">${erreurs['TextA']}</span>
					</div>
					
					<div class="input-field col m6 s12 offset-m1">
	                    <label class="active" for="ordre">Ordre</label>
	                    <input type="text" id="ordre" name="ordre" value="<c:out value="${param.ordre}"/>" size="30" maxlength="60" />
                    </div>
	                <div class="input-field col m6 s12 offset-m1">
	                    <span class="erreur">${erreurs['ordre']}</span>
					</div>
 
                    <div class="input-field col m6 s12 offset-m1">
	                    <input type="radio" id="estValide1" name="estValide" value="non" size="30" maxlength="60" ${ param.estValide == 'non' ? "checked ='checked'" : "checked ='checked'"  }/> <label class="active" for="estValide1">Mauvaise réponse</label>
	                    <input type="radio" id="estValide2" name="estValide" value="oui" size="30" maxlength="60" ${ param.estValide == 'oui' ? "checked ='checked'" : ''  }/><label class="active" for="estValide2">Bonne réponse</label>
					</div> 
					
       				<div class="input-field col m6 s12 offset-m1">
	                    <input type="radio" id="statut1" name="statutA" value="actif" size="30" maxlength="60" ${ param.statut == 'actif' ? "checked ='checked'" : "checked ='checked'"  }/><label class="active" for="statut1">Actif</label>
	                    <input type="radio" id="statut2" name="statutA" value="inactif" size="30" maxlength="60" ${ param.statut == 'inactif' ? "checked ='checked'" : ''  }/><label class="active" for="statut2">Inactif</label> <br>
					</div>
				</div>

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