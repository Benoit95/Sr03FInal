package dao;

import java.util.List;

import beans.Questionnaire;

public interface QuestionnaireDAO {

    Questionnaire trouver_ByID( long id ) throws DAOException;
    Questionnaire trouver_BySujet( String sujet) throws DAOException;

	List<Questionnaire> lister() throws DAOException;
	
    void creer( Questionnaire questionnaire ) throws DAOException;
    void modifier( Questionnaire questionnaire ) throws DAOException;
	void supprimer(Questionnaire questionnaire) throws DAOException;
	
}
