package dao;

import static dao.DAOUtilitaire.fermeturesSilencieuses;
import static dao.DAOUtilitaire.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Question;

public class QuestionDAOImpl implements QuestionDAO {

	private static final String SQL_SELECT = "SELECT * FROM Question WHERE id_questionnaire = ? ORDER BY ordre";
    private static final String SQL_SELECT_PAR_ID = "SELECT * FROM Question WHERE id = ?";
    private static final String SQL_SELECT_PAR_text = "SELECT * FROM Question WHERE text = ? AND id_questionnaire = ?";
    private static final String SQL_INSERT           = "INSERT INTO Question (text, statut, ordre, id_questionnaire) VALUES (?,?,?,?)";
    private static final String SQL_DELETE_PAR_ID = "DELETE FROM Question WHERE id = ?";
    private static final String SQL_MODIF_PAR_ID = "UPDATE Question SET text = ?, statut = ?, ordre = ? WHERE id = ? AND id_questionnaire = ?";
    
    private DAOFactory          daoFactory;

    QuestionDAOImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }	
	
	@Override
	public Question trouver_ByID(long id) throws DAOException {
		return trouver( SQL_SELECT_PAR_ID, id);
	}

	@Override
	public Question trouver_ByText(String text,long id_questionnaire) throws DAOException {
		return trouver( SQL_SELECT_PAR_text, text , id_questionnaire);
	}

	@Override
	public List<Question> lister(long id_questionnaire) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Question> q = new ArrayList<Question>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connection, SQL_SELECT, false, id_questionnaire );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                q.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }

        return q;
	}

	@Override
	public void creer(Question Question) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, Question.getText(), Question.getStatut(), Question.getOrdre(), Question.getId_questionnaire()  );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Échec de la création du Question, aucune ligne ajoutée dans la table." );
            }
            valeursAutoGenerees = preparedStatement.getGeneratedKeys();
            if ( valeursAutoGenerees.next() ) {
            	Question.setId( valeursAutoGenerees.getLong( 1 ) );
            } else {
                throw new DAOException( "Échec de la création du Question en base, aucun ID auto-généré retourné." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
		
	}

	@Override
	public void modifier(Question Question) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_MODIF_PAR_ID, true, Question.getText(), Question.getStatut(),Question.getOrdre(), Question.getId(), Question.getId_questionnaire() );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Échec de la modification du Question, aucune ligne modifiée de la table." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }
		
	}

	@Override
	public void supprimer(Question Question) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_PAR_ID, true, Question.getId() );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Échec de la suppression du Question, aucune ligne supprimée de la table." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }
		
	}

    //------------------------------------------------------------//
    
    private Question trouver( String sql, Object... objets ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Question Question = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, sql, false, objets );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de données retournée dans le ResultSet */
            if ( resultSet.next() ) {
            	Question = map( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return Question;
    }	

   private static Question map( ResultSet resultSet ) throws SQLException {
	   Question quest = new Question();
	   quest.setId( resultSet.getLong( "id" ) );
	   quest.setId_questionnaire( resultSet.getLong( "id_questionnaire" ) );
	   quest.setText(resultSet.getString("text"));
	   quest.setStatut( resultSet.getString( "statut" ) );
	   quest.setOrdre( resultSet.getInt( "ordre" ) );
       return quest;
   }    
    
}
