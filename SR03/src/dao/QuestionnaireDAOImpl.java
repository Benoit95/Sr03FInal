package dao;

import static dao.DAOUtilitaire.fermeturesSilencieuses;
import static dao.DAOUtilitaire.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Questionnaire;

public class QuestionnaireDAOImpl implements QuestionnaireDAO {

	private static final String SQL_SELECT = "SELECT * FROM questionnaire";
    private static final String SQL_SELECT_PAR_ID = "SELECT * FROM questionnaire WHERE id = ?";
    private static final String SQL_SELECT_PAR_SUJET = "SELECT * FROM questionnaire WHERE sujet = ?";
    private static final String SQL_INSERT           = "INSERT INTO questionnaire (sujet, statut) VALUES (?,?)";
    private static final String SQL_DELETE_PAR_ID = "DELETE FROM questionnaire WHERE id = ?";
    private static final String SQL_MODIF_PAR_ID = "UPDATE questionnaire SET sujet = ?, statut = ? WHERE id = ?";
    
    private DAOFactory          daoFactory;

    QuestionnaireDAOImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }	
	
	@Override
	public Questionnaire trouver_ByID(long id) throws DAOException {
		return trouver( SQL_SELECT_PAR_ID, id );
	}

	@Override
	public Questionnaire trouver_BySujet(String sujet) throws DAOException {
		return trouver( SQL_SELECT_PAR_SUJET, sujet );
	}

	@Override
	public List<Questionnaire> lister() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Questionnaire> q = new ArrayList<Questionnaire>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement( SQL_SELECT );
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
	public void creer(Questionnaire questionnaire) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, questionnaire.getSujet(), questionnaire.getStatut() );
            System.out.println(preparedStatement);
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Échec de la création du questionnaire, aucune ligne ajoutée dans la table." );
            }
            valeursAutoGenerees = preparedStatement.getGeneratedKeys();
            if ( valeursAutoGenerees.next() ) {
            	questionnaire.setId( valeursAutoGenerees.getLong( 1 ) );
            } else {
                throw new DAOException( "Échec de la création du questionnaire en base, aucun ID auto-généré retourné." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
		
	}

	@Override
	public void modifier(Questionnaire questionnaire) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_MODIF_PAR_ID, true, questionnaire.getSujet(), questionnaire.getStatut(), questionnaire.getId() );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Échec de la modification du questionnaire, aucune ligne modifiée de la table." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }
		
	}

	@Override
	public void supprimer(Questionnaire questionnaire) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_PAR_ID, true, questionnaire.getId() );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Échec de la suppression du questionnaire, aucune ligne supprimée de la table." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }
		
	}

    //------------------------------------------------------------//
    
    private Questionnaire trouver( String sql, Object... objets ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Questionnaire questionnaire = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, sql, false, objets );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de données retournée dans le ResultSet */
            if ( resultSet.next() ) {
            	questionnaire = map( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return questionnaire;
    }	

   private static Questionnaire map( ResultSet resultSet ) throws SQLException {
	   Questionnaire q = new Questionnaire();
       q.setId( resultSet.getLong( "id" ) );
       q.setSujet(resultSet.getString("sujet"));
       q.setStatut( resultSet.getString( "statut" ) );
       return q;
   }    
    
}
