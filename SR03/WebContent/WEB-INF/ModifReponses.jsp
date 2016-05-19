<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Modification de reponses</title>
		  <link href="<c:url value="/inc/materialize.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
		  <link href="<c:url value="/inc/style.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
    </head>
    <body>
    
    	<%@ include file="NavBar.jsp" %>
    
        <%@ include file="MenuAdmin.jsp" %>
        <div class="container">
        
        <fieldset>
            
        <h5 class="header col s12 light">Modification d'une Reponse de la Question n° ${ empty QuestionID ? param.IdQuestion : QuestionID  }</h5>
        	<div class="row">
            <form method="post" action="ModifReponses">
                <div class="row center">
                    <input type="hidden" id="IdQuestion" name="IdQuestion" value="<c:out value="${ empty QuestionID ? param.IdQuestion : QuestionID  }"/>" size="30" maxlength="60" />

                    <input type="hidden" id="IdM" name="IdM" value="<c:out value="${ empty rep_to_modif.id ? param.IdM : rep_to_modif.id  }"/>" size="30" maxlength="60" />
                    <span class="erreur">${erreurs['IdM']}</span>
					<br />
                   	<div class="input-field col m6 s12 offset-m1">
                   		<input type="text" id="TextM" name="TextM" value="<c:out value="${ empty rep_to_modif.text ? param.TextM : rep_to_modif.text }"/>" size="30" maxlength="60" /><label class="active" for="TextM">Intitulé</label>
                   	</div>
					<div class="col m6 s12 offset-m1">
                  			<span class="erreur">${erreurs['TextM']}</span>
                   	</div>

					<div class="input-field col m6 s12 offset-m1">
	                    <label class="active" for="ordre">Ordre</label>
	                    <input type="text" id="ordreM" name="ordreM" value="<c:out value="${ empty rep_to_modif.ordre ? param.ordreM : rep_to_modif.ordre  }"/>" size="30" maxlength="60" />
                    </div>
	                <div class="input-field col m6 s12 offset-m1">
	                    <span class="erreur">${erreurs['ordreM']}</span>
					</div>
					
					
                    <div class="input-field col m6 s12 offset-m1">
                    <input type="radio" id="estValideM1" name="estValideM" value="non" size="30" maxlength="60" ${ rep_to_modif.estValide == 'non' ? "checked ='checked'" : "checked ='checked'"  }/> <label class="active" for="estValideM1">Mauvaise réponse</label>
                    <input type="radio" id="estValideM2" name="estValideM" value="oui" size="30" maxlength="60" ${ rep_to_modif.estValide == 'oui' ? "checked ='checked'" : ''  }/> <label class="active" for="estValideM2">Bonne réponse</label>
					</div>
					
       				<div class="input-field col m6 s12 offset-m1">
	                    <input type="radio" id="statut1" name="statutM" value="actif" size="30" maxlength="60" ${ param.statut == 'actif' ? "checked ='checked'" : "checked ='checked'"  }/><label class="active" for="statut1">Actif</label>
	                    <input type="radio" id="statut2" name="statutM" value="inactif" size="30" maxlength="60" ${ param.statut == 'inactif' ? "checked ='checked'" : ''  }/><label class="active" for="statut2">Inactif</label> <br>
					</div>
				</div>

                <input class="col m5 s12 waves-effect waves-light btn-large offset-m1" type="submit" value="Modifier"  /> 
                
            </form>         
            
            </div>

            </fieldset>
            <p class="erreur">${resultat}</p>
            </div> 
		
			<%@ include file="footer.jsp" %>  
        
    </body>
</html>