<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Liste des utilisateurs existants</title>
		  <link href="<c:url value="/inc/materialize.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
		  <link href="<c:url value="/inc/style.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
    </head>
    <body>
    
    	<%@ include file="/WEB-INF/NavBar.jsp" %>
    
        <%@ include file="MenuAdmin.jsp" %>
        
        <%@ include file="RechercheForm.jsp" %>
        
        <div class="container">
        <fieldset>
        <h5 class="header col s12 light">Liste des utilisateurs</h5>
        <div id="corps">
        <c:choose>
            <%-- Si aucun utilisateur n'existe en session, affichage d'un message par défaut. --%>
            <c:when test="${ empty users }">
                <p class="erreur">Aucun utilisateur enregistré.</p>
            </c:when>
            <%-- Sinon, affichage du tableau. --%>
            <c:otherwise>
            <table class = "centered">
                <thead><tr>
                    <th>Nom</th>
                    <th>Prénom</th>
                    <th>Société</th>
                    <th>Téléphone</th>
                    <th>Email</th>
                    <th>Admin</th>
                    <th>Date de création</th>
                    <th>Statut du compte</th>
                    <th class="action">Suppression</th>
                    <th class="action">Modification</th>
                    <th class="action">Parcours</th>                    
                </tr></thead>
                <%-- Parcours de la Map des Users dans la requete, et utilisation de l'objet varStatus. --%>
                <tbody>
                <c:forEach items="${ users }" var="mapUsers" varStatus="boucle">
              
                <tr>
                    <%-- Affichage des propriétés du bean User, qui est stocké en tant que valeur de l'entrée courante de la map --%>
                    <td><c:out value="${ mapUsers.nom }"/></td>
                    <td><c:out value="${ mapUsers.prenom }"/></td>
                    <td><c:out value="${ mapUsers.societe }"/></td>
                    <td><c:out value="${ mapUsers.tel }"/></td>
                    <td><c:out value="${ mapUsers.mail }"/></td>
                    <td><c:out value="${ mapUsers.admin }"/></td>
                    <td><c:out value="${ mapUsers.date_crea }"/></td>
                    <td><c:out value="${ mapUsers.statut }"/></td>
                    <%-- Lien vers la servlet de suppression, avec passage du nom du User en paramètre grâce à la balise <c:param/>. --%>
                    <td class="action">
                        <a href="<c:url value="GestionUser"><c:param name="mailUser_to_delete" value="${ mapUsers.mail }" /></c:url>">Supprimer</a>
                    </td>
                    <td class="action">
                        <a href="<c:url value="GestionUser"><c:param name="mailUser_to_modif" value="${ mapUsers.mail }" /></c:url>">Modifier</a>
                    </td>
                    <td class="action">
				        <c:if test="${mapUsers.admin  == false}">
					   		<a href="<c:url value="/Admin/AdminListeParcours"><c:param name="StagiaireID" value="${ mapUsers.id }" /><c:param name="page" value="1" /></c:url>">Visualiser</a>
				        </c:if>
                    </td>
                    
                </tr>
                </c:forEach>
                </tbody>
            </table>
            </c:otherwise>
        </c:choose>

        <c:if test="${sessionScope.page > 1}">
	   		<a href="<c:url value="GestionUser"><c:param name="page" value="${ sessionScope.page - 1 }" /></c:url>">Précédent</a>
        </c:if>
        <c:if test="${sessionScope.page < sessionScope.pageMax }">
      	 <a href="<c:url value="GestionUser"><c:param name="page" value="${ sessionScope.page + 1 }" /></c:url>">Suivant</a>
      	</c:if>
        
        
        </div>
        </fieldset>
        </div>
        
        <br><br>
        <div class="container">
        
        <fieldset>
        
        <h5 class="header col s12 light">Ajout/Modification d'un utilisateur</h5>
        <div class="row">
        <br>
        
            <form method="post" action="GestionUser">
                    <div class="row">
                    
                    	<div class="input-field col m6 s12 offset-m1">
		                    <input type="radio" id="admin1" name="admin" value="Administrateur" size="30" maxlength="60" ${ user_to_modif.admin == true ? "checked ='checked'" : "checked ='checked'"  } /><label class="active" for="admin1">Administrateur</label>
		                    <input type="radio" id="admin2" name="admin" value="Stagiaire" size="30" maxlength="60" ${ user_to_modif.admin == false ? "checked ='checked'" : ''  }/> <label class="active" for="admin2">Stagiaire</label>
                   		 </div>
                    
                    	<div class="input-field col m6 s12 offset-m1">
		                    <label for="emailUser" class="active">Adresse email <span class="requis">*</span></label>
		                    <input type="email" id="emailUser" name="emailUser" value="<c:out value="${ empty user_to_modif.mail ? param.emailUser : user_to_modif.mail  }"/>" size="30" maxlength="60" />
                    	</div>
                    	<div class="input-field col m6 s12 offset-m1"> 
                    		<span class="erreur">${erreurs['emailUser']}</span>
						</div>
					 
						<div class="input-field col m6 s12 offset-m1">           
		                    <label for="mdpUser" class="active">Mot de passe <span class="requis">*</span></label>
		                    <input type="password" id="mdpUser" name="mdpUser" value="<c:out value="${ empty user_to_modif.mdp ? param.mdpUser : user_to_modif.mdp  }"/>" size="30" maxlength="20" />
	                    </div>
	                    <div class="input-field col m6 s12 offset-m1">
	                    	<span class="erreur">${erreurs['mdpUser']}</span>
	                    </div>

						<div class="input-field col m6 s12 offset-m1">
		                    <label for="nom" class="active">Nom<span class="requis">*</span></label>
		                    <input type="text" id="nom" name="nom" value="<c:out value="${ empty user_to_modif.nom ? param.nom : user_to_modif.nom  }"/>" size="30" maxlength="20" />
	                    </div>
	                    <div class="input-field col m6 s12 offset-m1">
	                    	<span class="erreur">${erreurs['nom']}</span>
						</div> 
						
						<div class="input-field col m6 s12 offset-m1">
		                    <label for="prenom" class="active">Prénom <span class="requis">*</span></label>
		                    <input type="text" id="prenom" name="prenom" value="<c:out value="${ empty user_to_modif.prenom ? param.prenom : user_to_modif.prenom  }"/>" size="30" maxlength="20" />
	                    </div>
	                    <div class="input-field col m6 s12 offset-m1">
	                    	<span class="erreur">${erreurs['prenom']}</span>
						</div>
						
	                    <div class="input-field col m6 s12 offset-m1">
		                    <label for="societe" class="active">Société</label>
		                    <input type="text" id="societe" name="societe" value="<c:out value="${ empty user_to_modif.societe ? param.societe : user_to_modif.societe  }"/>" size="30" maxlength="20" />
						</div> 
						
						<div class="input-field col m6 s12 offset-m1">
		                    <label for="tel" class="active">Téléphone</label>
		                    <input type="text" id="tel" name="tel" value="<c:out value="${ empty user_to_modif.tel ? param.tel : user_to_modif.tel  }"/>" size="30" maxlength="20" />
	                    </div>
	                    <div class="input-field col m6 s12 offset-m1">
	                    	<span class="erreur">${erreurs['tel']}</span>
						</div> 
						
						<div class="input-field col m6 s12 offset-m1">
	                   	 	<input type="radio" id="statut_cpt1" name="statut_cpt" value="actif" size="30" maxlength="60" ${ user_to_modif.statut == 'actif' ? "checked ='checked'" : "checked ='checked'"  }/><label class="active" for="statut_cpt1">Actif</label>
	                    	<input type="radio" id="statut_cpt2" name="statut_cpt" value="inactif" size="30" maxlength="60" ${ user_to_modif.statut == 'inactif' ? "checked ='checked'" : ''  }/><label class="active" for="statut_cpt2">Inactif</label>
						</div>

	                <br><br>
	                <input class="col m5 s12 waves-effect waves-light btn-large offset-m1" type="submit" value="Ajouter/Modifier"  />
	                </div>
            	</form>
            </div>

           </fieldset>
           <p class="erreur">${resultat}</p>
           </div>
            <%@ include file="/WEB-INF/footer.jsp" %>
        
    </body>
</html>