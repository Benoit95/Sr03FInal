package dao;

import static dao.DAOUtilitaire.fermeturesSilencieuses;
import static dao.DAOUtilitaire.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Parcours;
import beans.Questionnaire;

public class ParcoursDaoImpl implements ParcoursDao {

	private static final String SQL_SELECT = "SELECT * FROM Parcours WHERE id_user = ? ORDER BY id_question";
	private static final String SQL_SELECT_PAR_QUESTION= "SELECT * FROM Parcours WHERE id_user = ? AND id_question = ? ORDER BY id_question";
    private static final String SQL_SELECT_PAR_QUESTIONNAIRE = "SELECT * FROM Parcours WHERE id_user = ? AND id_questionnaire = ? ORDER BY id_question";
    private static final String SQL_INSERT           = "INSERT INTO Parcours (id_user, id_questionnaire, id_question, id_reponse,score,duree) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE= "DELETE FROM Parcours WHERE id_user = ? AND id_questionnaire = ?";
 
    private DAOFactory          daoFactory;

    ParcoursDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void creer( Parcours Parcours ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, Parcours.getId_user(), Parcours.getId_questionnaire(), Parcours.getId_question(), Parcours.getId_reponse(), Parcours.getScore(), Parcours.getDuree());
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Échec de la création de l'Parcours, aucune ligne ajoutée dans la table." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
    }

    /*
     * Méthode générique utilisée pour retourner un Parcours depuis la base
     * de données, correspondant à la requête SQL donnée prenant en paramètres
     * les objets passés en argument.
     */


    @Override
    public List<Parcours> lister(long id_user) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Parcours> parcours = new ArrayList<Parcours>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connection, SQL_SELECT, true, id_user);
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
            	parcours.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }

        return parcours;
    }
    
    @Override
    public List<Parcours> trouver_Parcours_User( long id_user, long id_questionnaire ) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Parcours> parcours = new ArrayList<Parcours>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connection, SQL_SELECT_PAR_QUESTIONNAIRE, true, id_user, id_questionnaire);
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
            	parcours.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }

        return parcours;
    }
    
    @Override
    public Parcours trouver_Question_user( long id_user, long id_question ) throws DAOException {
    	return trouver(SQL_SELECT_PAR_QUESTION, id_user, id_question);
    }

    @Override
    public void supprimer( Parcours p ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE, true, p.getId_user(), p.getId_questionnaire() );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Échec de la suppression de l'Parcours, aucune ligne supprimée de la table." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }
    }

    
    //------------------------------------------------------------//
    
    private Parcours trouver( String sql, Object... objets ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Parcours Parcours = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, sql, false, objets );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de données retournée dans le ResultSet */
            if ( resultSet.next() ) {
            	Parcours = map( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return Parcours;
    }	
    
    /*
     * Simple méthode utilitaire permettant de faire la correspondance (le
     * mapping) entre une ligne issue de la table des Parcourss (un
     * ResultSet) et un bean Parcours.
     */
    
    private static Parcours map( ResultSet resultSet ) throws SQLException {
    	Parcours Parcours = new Parcours();
        Parcours.setId_user( resultSet.getLong( "id_user" ) );
        Parcours.setId_questionnaire(resultSet.getLong("id_questionnaire"));
        Parcours.setId_question( resultSet.getLong( "id_question" ) );
        Parcours.setId_reponse( resultSet.getLong( "id_reponse" ) );
        Parcours.setScore( resultSet.getInt( "score" ) );
        Parcours.setDuree( resultSet.getLong( "duree" ) );
        return Parcours;
    }
}
