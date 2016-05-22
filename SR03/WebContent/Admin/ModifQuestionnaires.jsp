<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Liste des questionnaires existants</title>
		  <link href="<c:url value="/inc/materialize.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
		  <link href="<c:url value="/inc/style.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
    </head>
    <body>
    
    	<%@ include file="/WEB-INF/NavBar.jsp" %>
    
        <%@ include file="MenuAdmin.jsp" %>
        
        <div class="container">
        
        <fieldset>
        
        <h5 class="header col s12 light">Modif du questionnaire n° ${ empty quest_to_modif.id ? param.IdM : quest_to_modif.id  }</h5>
        <div class="row">
            <form method="post" action="ModifQuestionnaires">
            	<div class="row center">

                    <input type="hidden" id="IdM" name="IdM" value="<c:out value="${ empty quest_to_modif.id ? param.IdM : quest_to_modif.id  }"/>" size="30" maxlength="60" />
                    <span class="erreur">${erreurs['IdM']}</span>
					<br /> 
                
                    	<div class="input-field col m6 s12 offset-m1">
                    		<input type="text" id="sujetM" name="sujetM" value="<c:out value="${ empty quest_to_modif.sujet ? param.sujetM : quest_to_modif.sujet  }"/>" size="30" maxlength="60" /><label class="active" for="sujetM">Sujet</label>
                    	</div>
						<div class="col m6 s12 offset-m1">
                   			<span class="erreur">${erreurs['sujetM']}</span>
                    	</div>
					<br /> <br /> 
       				</div>
       				
       				<div class="input-field col m6 s12 offset-m1">
	                    <input type="radio" id="statut1" name="statutM" value="actif" size="30" maxlength="60" ${ param.statut == 'actif' ? "checked ='checked'" : "checked ='checked'"  }/><label class="active" for="statut1">Actif</label>
	                    <input type="radio" id="statut2" name="statutM" value="inactif" size="30" maxlength="60" ${ param.statut == 'inactif' ? "checked ='checked'" : ''  }/><label class="active" for="statut2">Inactif</label> <br>
					</div>
					<br /> 
					
                <br><br>
                <input class="col m5 s12 waves-effect waves-light btn-large offset-m1" type="submit" value="Modifier"  />
                
            </form>
            </div>

           </fieldset>
           <p class="erreur">${resultat}</p>
           </div>
			
			<%@ include file="/WEB-INF/footer.jsp" %>
        
    </body>
</html>