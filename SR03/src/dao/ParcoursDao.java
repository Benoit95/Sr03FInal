package dao;

import java.util.List;

import beans.Parcours;

public interface ParcoursDao {

	List<Parcours> trouver_Parcours_User( long id_user, long id_questionnaire ) throws DAOException;
	Parcours trouver_Question_user( long id_user, long id_question ) throws DAOException;

	List<Parcours> lister(long id_user) throws DAOException;
	
    void creer( Parcours parcours ) throws DAOException;
    
   // void modifier( Parcours parcours ) throws DAOException;

	void supprimer(Parcours parcours) throws DAOException;

}
