package dao;

import static dao.DAOUtilitaire.fermeturesSilencieuses;
import static dao.DAOUtilitaire.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Utilisateur;

public class UtilisateurDaoImpl implements UtilisateurDao {

	private static final String SQL_SELECT = "SELECT * FROM Utilisateur";
	private static final String SQL_SELECT_PAR_ID = "SELECT * FROM Utilisateur WHERE id = ?";
    private static final String SQL_SELECT_PAR_EMAIL = "SELECT * FROM Utilisateur WHERE email = ?";
    private static final String SQL_SELECT_PAR_EMAIL_ET_MDP = "SELECT * FROM Utilisateur WHERE email = ? AND mot_de_passe = ?";
    private static final String SQL_INSERT           = "INSERT INTO Utilisateur (admin, email, mot_de_passe, nom, prenom, societe, tel, statut_cpt, date_creation) VALUES (?,?, ?, ?, ?, ?, ?, ?, NOW())";
    private static final String SQL_DELETE_PAR_MAIL = "DELETE FROM Utilisateur WHERE email = ?";
    private static final String SQL_MODIF_PAR_MAIL = "UPDATE Utilisateur SET admin = ?, email = ?, mot_de_passe = ?, nom = ?, prenom = ? , societe = ?, tel = ?, statut_cpt = ? WHERE email = ?";
    
    private DAOFactory          daoFactory;

    UtilisateurDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    @Override
    public Utilisateur trouver_byMail( String email ) throws DAOException {
        return trouver( SQL_SELECT_PAR_EMAIL, email );
    }
    
	@Override
	public Utilisateur trouver_byMailMDp(String email, String mdp) throws DAOException {
		return trouver( SQL_SELECT_PAR_EMAIL_ET_MDP, email, mdp );
	}
	
	@Override
	public Utilisateur trouver_byID(long id) throws DAOException {
		return trouver( SQL_SELECT_PAR_ID, id );
	}

    @Override
    public void creer( Utilisateur utilisateur ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, utilisateur.getAdmin() , utilisateur.getMail(), utilisateur.getMdp(), utilisateur.getNom(), utilisateur.getPrenom(),utilisateur.getSociete(),utilisateur.getTel(),utilisateur.getStatut() );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Échec de la création de l'utilisateur, aucune ligne ajoutée dans la table." );
            }
            valeursAutoGenerees = preparedStatement.getGeneratedKeys();
            if ( valeursAutoGenerees.next() ) {
                utilisateur.setId( valeursAutoGenerees.getLong( 1 ) );
            } else {
                throw new DAOException( "Échec de la création de l'utilisateur en base, aucun ID auto-généré retourné." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
    }

    @Override
    public void modifier( Utilisateur utilisateur ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_MODIF_PAR_MAIL, true, utilisateur.getAdmin(), utilisateur.getMail(), utilisateur.getMdp(), utilisateur.getNom(), utilisateur.getPrenom(),utilisateur.getSociete(),utilisateur.getTel(),utilisateur.getStatut(), utilisateur.getMail() );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Échec de la modification de l'utilisateur, aucune ligne modifiée de la table." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }
    }
    /*
     * Méthode générique utilisée pour retourner un utilisateur depuis la base
     * de données, correspondant à la requête SQL donnée prenant en paramètres
     * les objets passés en argument.
     */


    @Override
    public List<Utilisateur> lister() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Utilisateur> users = new ArrayList<Utilisateur>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement( SQL_SELECT );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                users.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }

        return users;
    }

    @Override
    public void supprimer( Utilisateur user ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_PAR_MAIL, true, user.getMail() );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Échec de la suppression de l'utilisateur, aucune ligne supprimée de la table." );
            } else {
                user.setMail( null );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }
    }

    
    //------------------------------------------------------------//
    
    private Utilisateur trouver( String sql, Object... objets ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Utilisateur utilisateur = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            /*
             * Préparation de la requête avec les objets passés en arguments
             * (ici, uniquement une adresse email) et exécution.
             */
            preparedStatement = initialisationRequetePreparee( connexion, sql, false, objets );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de données retournée dans le ResultSet */
            if ( resultSet.next() ) {

                utilisateur = map( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return utilisateur;
    }
    
    /*
     * Simple méthode utilitaire permettant de faire la correspondance (le
     * mapping) entre une ligne issue de la table des utilisateurs (un
     * ResultSet) et un bean Utilisateur.
     */
    private static Utilisateur map( ResultSet resultSet ) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId( resultSet.getLong( "id" ) );
        utilisateur.setAdmin(resultSet.getBoolean("admin"));
        utilisateur.setMail( resultSet.getString( "email" ) );
        utilisateur.setMdp( resultSet.getString( "mot_de_passe" ) );
        utilisateur.setNom( resultSet.getString( "nom" ) );
        utilisateur.setPrenom( resultSet.getString( "prenom" ) );
        utilisateur.setSociete( resultSet.getString( "societe" ) );
        utilisateur.setTel( resultSet.getString( "tel" ) );
        utilisateur.setDate_crea( resultSet.getTimestamp( "date_creation" ) );
        utilisateur.setStatut( resultSet.getString( "statut_cpt" ) );
        return utilisateur;
    }


}
