package dao;

import static dao.DAOUtilitaire.fermeturesSilencieuses;
import static dao.DAOUtilitaire.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Reponse;

public class ReponseDAOImpl implements ReponseDAO {

	private static final String SQL_SELECT = "SELECT * FROM Reponse WHERE id_question = ? ORDER BY ordre";
	private static final String SQL_SELECT_REP_CORRECTE = "SELECT * FROM Reponse WHERE id_question = ? AND estValide = 'oui'";
    private static final String SQL_SELECT_PAR_ID = "SELECT * FROM Reponse WHERE id = ?";
    private static final String SQL_SELECT_PAR_text = "SELECT * FROM Reponse WHERE text = ? AND id_question = ?";
    private static final String SQL_INSERT           = "INSERT INTO Reponse (text, statut, ordre , estValide, id_question) VALUES (?,?,?,?,?)";
    private static final String SQL_DELETE_PAR_ID = "DELETE FROM Reponse WHERE id = ?";
    private static final String SQL_MODIF_PAR_ID = "UPDATE Reponse SET text = ?, statut = ? ,ordre = ? , estValide = ? WHERE id = ? AND id_question = ?";
    
    private DAOFactory          daoFactory;

    ReponseDAOImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }	
	
	@Override
	public Reponse trouver_ByID(long id) throws DAOException {
		return trouver( SQL_SELECT_PAR_ID, id);
	}

	@Override
	public Reponse trouverRepCorrecte(long id_question) throws DAOException {
		return trouver( SQL_SELECT_REP_CORRECTE, id_question);
	}	
	
	@Override
	public Reponse trouver_ByText(String text,long id_question) throws DAOException {
		return trouver( SQL_SELECT_PAR_text, text , id_question);
	}

	@Override
	public List<Reponse> lister(long id_question) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Reponse> ListeReponse = new ArrayList<Reponse>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connection, SQL_SELECT, false, id_question );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
            	ListeReponse.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }

        return ListeReponse;
	}

	@Override
	public void creer(Reponse Reponse) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, Reponse.getText(), Reponse.getStatut(),Reponse.getOrdre(), Reponse.getEstValide(), Reponse.getId_question()  );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Échec de la création de la Reponse, aucune ligne ajoutée dans la table." );
            }
            valeursAutoGenerees = preparedStatement.getGeneratedKeys();
            if ( valeursAutoGenerees.next() ) {
            	Reponse.setId( valeursAutoGenerees.getLong( 1 ) );
            } else {
                throw new DAOException( "Échec de la création de la Reponse en base, aucun ID auto-généré retourné." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
		
	}

	@Override
	public void modifier(Reponse Reponse) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_MODIF_PAR_ID, true, Reponse.getText(), Reponse.getStatut(),Reponse.getOrdre(), Reponse.getEstValide(), Reponse.getId(), Reponse.getId_question() );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Échec de la modification de la Reponse, aucune ligne modifiée de la table." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }
		
	}

	@Override
	public void supprimer(Reponse Reponse) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_PAR_ID, true, Reponse.getId() );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Échec de la suppression de la Reponse, aucune ligne supprimée de la table." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }
		
	}

    //------------------------------------------------------------//
    
    private Reponse trouver( String sql, Object... objets ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Reponse Reponse = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, sql, false, objets );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de données retournée dans le ResultSet */
            if ( resultSet.next() ) {
            	Reponse = map( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return Reponse;
    }	

   private static Reponse map( ResultSet resultSet ) throws SQLException {
	   Reponse quest = new Reponse();
	   quest.setId( resultSet.getLong( "id" ) );
	   quest.setId_question( resultSet.getLong( "id_question" ) );
	   quest.setText(resultSet.getString("text"));
	   quest.setStatut( resultSet.getString( "statut" ) );
	   quest.setEstValide(resultSet.getString("estValide"));
	   quest.setOrdre(resultSet.getInt("ordre"));
       return quest;
   }    
    
}
