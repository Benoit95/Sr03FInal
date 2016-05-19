package dao;

import java.util.List;

import beans.Question;

public interface QuestionDAO {

    Question trouver_ByID(long id) throws DAOException;
    Question trouver_ByText( String text, long id_questionnaire) throws DAOException;

	List<Question> lister(long id_questionnaire) throws DAOException;
	
    void creer( Question question ) throws DAOException;
    void modifier( Question question ) throws DAOException;
	void supprimer(Question question) throws DAOException;
	
}
