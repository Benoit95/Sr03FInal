package dao;

import java.util.List;

import beans.Reponse;

public interface ReponseDAO {

    Reponse trouver_ByID(long id) throws DAOException;
    Reponse trouverRepCorrecte(long iDQuestion);
    Reponse trouver_ByText( String text, long id_Reponse) throws DAOException;

	List<Reponse> lister(long id_Question) throws DAOException;
	
    void creer( Reponse Reponse ) throws DAOException;
    void modifier( Reponse Reponse ) throws DAOException;
	void supprimer(Reponse Reponse) throws DAOException;
	
}
