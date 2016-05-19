package dao;

import java.util.List;

import beans.Utilisateur;

public interface UtilisateurDao {

    Utilisateur trouver_byMail( String email ) throws DAOException;
    
    Utilisateur trouver_byMailMDp( String email, String mdp ) throws DAOException;

	List<Utilisateur> lister() throws DAOException;
	
    void creer( Utilisateur utilisateur ) throws DAOException;
    
    void modifier( Utilisateur utilisateur ) throws DAOException;

	void supprimer(Utilisateur user) throws DAOException;

}
