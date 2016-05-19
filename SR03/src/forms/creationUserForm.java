package forms;

import dao.DAOException;

public class creationUserForm {

	public void validationEmail( String email ) throws Exception {
	    if ( email != null && email.trim().length() != 0 ) {
	        if ( !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {
	            throw new Exception( "Merci de saisir une adresse mail valide." );
	        }
	    } else {
	        throw new Exception( "Merci de saisir une adresse mail." );
	    }
	}
	
	public void validationMotDePasse( String motDePasse) throws Exception{
	    if (motDePasse != null && motDePasse.trim().length() != 0) {
	        if (motDePasse.trim().length() < 6) {
	            throw new Exception("Le mot de passe doit contenir au moins 6 caractères.");
	        }
	    } else {
	        throw new Exception("Merci de saisir votre mot de passe.");
	    }
	}
	
	public void validationNom( String nom ) throws Exception {
        if ( nom == null || nom.trim().length() == 0 ) {
            throw new Exception( "Merci d'entrer le nom de l'utilisateur." );
        }
    }

	public void validationPrenom( String prenom ) throws Exception {
        if ( prenom == null || prenom.trim().length() == 0  ) {
            throw new Exception( "Merci d'entrer le prenom de l'utilisateur." );
        }
    }

	public void validationTel( String tel ) throws Exception {
        if ( tel.trim().length() != 10 && (tel != null || tel != "") ) {
            throw new Exception( "Merci d'entrer un numéro de téléphone valide." );
        }
    }
	
}
