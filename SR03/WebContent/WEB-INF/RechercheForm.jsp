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
    
		<div class="container">
		<fieldset>
		    <h5 class="header col s12 light">Recherche</h5>
            <form method="post" action="Recherche">
                <div class= "row center">
	                <div class="input-field col m6 s12">
	                	<input type="text" id="valeur" name="valeur" />
	                </div>
	
	                <div class="col m6 s12">
		                <input class="col m6 s12 waves-effect waves-light btn-large" type="submit" value="Rechercher"  />
	                </div>
                </div>
                   
               
            </form>
        </fieldset>
        </div>
        <br><br>

    </body>
</html>