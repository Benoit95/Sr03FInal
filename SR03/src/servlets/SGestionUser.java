package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Utilisateur;
import dao.DAOException;
import dao.DAOFactory;
import dao.UtilisateurDao;
import forms.creationUserForm;

public class SGestionUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String AFFICHAGE          = "/Admin/GestionUsers.jsp";
	public static final String CONF_DAO_FACTORY = "daofactory";
	public static final String ATT_LISTUSERS = "users";

	public static final String CHAMP_EMAIL  = "emailUser";
	public static final String CHAMP_PASS   = "mdpUser";
	public static final String CHAMP_NOM  = "nom";
	public static final String CHAMP_ADMIN  = "admin";
	public static final String CHAMP_PRENOM   = "prenom";
	public static final String CHAMP_SOCIETE   = "societe";
	public static final String CHAMP_TEL   = "tel";
	public static final String CHAMP_STATUT   = "statut_cpt";

	public static final String PARAM_MAIL_USER_TO_DELETE   = "mailUser_to_delete";
	public static final String PARAM_MAIL_USER_TO_MODIF   = "mailUser_to_modif";
	public static final String ATT_USER_TO_MODIF   = "user_to_modif";

	public static final String ATT_ERREURS  = "erreurs";
	public static final String ATT_RESULTAT = "resultat";
	
	public static final String R_TYPE = "type";
	public static final String PAGE = "page";
	public static final String PAGEMAX = "pageMax";
	public static final String ATT_SESSION_NB_USER_PAGE = "sessionUserPage";

	public SGestionUser() {
		super();
	}

	private UtilisateurDao     utilisateurDao;
	List<Utilisateur> users;
	creationUserForm crea = new creationUserForm();

	public void init() throws ServletException {
		/* Récupération d'une instance de notre DAO Utilisateur */
		this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUtilisateurDao();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Récupération de la session*/
		HttpSession session = request.getSession();

			String resultat ="";
			int pageI;
			int nb_user_affich = (int) session.getAttribute(ATT_SESSION_NB_USER_PAGE);
			
			/* Récupération des paramètres (si suppression/modification demandé) */
			String mailUserToDelete = getValeurParametre( request, PARAM_MAIL_USER_TO_DELETE);
			String mailUserToModif = getValeurParametre( request, PARAM_MAIL_USER_TO_MODIF);

			if (mailUserToDelete != null){
				Utilisateur user_to_delete = new Utilisateur();
				user_to_delete = utilisateurDao.trouver_byMail(mailUserToDelete);

				if (user_to_delete != null){
					try{
						/* Alors suppression du client de la BDD */
						utilisateurDao.supprimer( user_to_delete);	
					} catch ( DAOException e ) {
						e.printStackTrace();
					}
				}
				
				resultat = "Suppression réussie";
			}

			if (mailUserToModif != null){
				Utilisateur user_to_modif = new Utilisateur();
				user_to_modif = utilisateurDao.trouver_byMail(mailUserToModif);

				if (user_to_modif != null){
					try{
						/* Alors on envoie les parametres au formulaire*/
						request.setAttribute( ATT_USER_TO_MODIF, user_to_modif );
					} catch ( DAOException e ) {
						e.printStackTrace();
					}
					
					resultat = "Modification réussie";
				}
			}
			
			// On récupère la valeur de la page actuelle en session/en requete.
			if (getValeurParametre( request, PAGE) == null)
				pageI = (int) session.getAttribute(PAGE);
			else
				pageI = Integer.parseInt(getValeurParametre( request, PAGE));

			/* on récupère la liste des utilisateurs */
			users = utilisateurDao.lister();

			/* On prépare la liste des questionnaires à affichées (page) */
			List<Utilisateur> LuserToAffiche = new ArrayList<Utilisateur>();
			for (int i = 1 ; i <= nb_user_affich ; i++){
				if ((i+nb_user_affich*(pageI-1) -1) < users.size())
					LuserToAffiche.add(i-1, users.get(i+nb_user_affich*(pageI-1) -1));
			}

			/* on la passe en attribut à la requete */
			request.setAttribute( ATT_LISTUSERS, LuserToAffiche );
			session.setAttribute( R_TYPE, "user"); // att session pour la recherche
			request.setAttribute( ATT_RESULTAT, resultat );
			
			/* On passe aussi la valeur de la page et de pagemax */
			session.setAttribute(PAGE, pageI);
			session.setAttribute( PAGEMAX,  Math.ceil((double)users.size() / (double)nb_user_affich));
			;

			/* Affichage de la page de d'affichage user */
			this.getServletContext().getRequestDispatcher( AFFICHAGE ).forward( request, response );
	}

	// Lors d'un ajout/modification d'un utilisateur;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resultat;
		Map<String, String> erreurs = new HashMap<String, String>();

		/* Récupération des champs du formulaire. */
		Boolean b_admin ;
		String admin = request.getParameter( CHAMP_ADMIN );

		/* Récupération de la session*/
		HttpSession session = request.getSession();
		int page = (int) session.getAttribute(PAGE);
		int nb_user_affich = (int) session.getAttribute(ATT_SESSION_NB_USER_PAGE);
		
		if (admin.contentEquals("Administrateur") )
			b_admin = true;
		else
			b_admin = false;
		
		String email = request.getParameter( CHAMP_EMAIL );
		String motDePasse = request.getParameter( CHAMP_PASS );
		String nom = request.getParameter( CHAMP_NOM );
		String prenom = request.getParameter( CHAMP_PRENOM );
		String societe = request.getParameter( CHAMP_SOCIETE );
		String tel = request.getParameter( CHAMP_TEL );
		String statut = request.getParameter( CHAMP_STATUT );

		/* Validation du champ email. */
		try {
			crea.validationEmail( email );
		} catch ( Exception e ) {
			erreurs.put( CHAMP_EMAIL, e.getMessage() );
		}

		/* Validation du champ mot de passe. */
		try {
			crea.validationMotDePasse( motDePasse );
		} catch ( Exception e ) {
			erreurs.put( CHAMP_PASS, e.getMessage() );
		}

		/* Validation du champ nom. */
		try {
			crea.validationNom( nom );
		} catch ( Exception e ) {
			erreurs.put( CHAMP_NOM, e.getMessage() );
		}

		/* Validation du champ prenom. */
		try {
			crea.validationPrenom( prenom );
		} catch ( Exception e ) {
			erreurs.put( CHAMP_PRENOM, e.getMessage() );
		}

		/* Validation du champ tel. */
		try {
			crea.validationTel( tel );
		} catch ( Exception e ) {
			erreurs.put( CHAMP_TEL, e.getMessage() );
		}


		/* Initialisation du résultat global de la validation. */
		if ( erreurs.isEmpty() ) {
			resultat = "Succès.";

			// Si le mail n'a jamais été utilisé : création
			if (MailDejaUsed(email) != true){
				Utilisateur user_created = new Utilisateur();
				user_created.setAdmin(b_admin);
				user_created.setMail(email);
				user_created.setMdp(motDePasse);
				user_created.setNom(nom);
				user_created.setPrenom(prenom);

				if (societe != null)
					user_created.setSociete(societe);
				if (tel != null)
					user_created.setTel(tel);
				if (statut != null)
					user_created.setStatut(statut);

				utilisateurDao.creer(user_created);
				
				resultat = "Création effectuée.";
			}
			else{
				Utilisateur user_to_modif = utilisateurDao.trouver_byMail(email);
				user_to_modif.setAdmin(b_admin);
				user_to_modif.setMail(email);
				user_to_modif.setMdp(motDePasse);
				user_to_modif.setNom(nom);
				user_to_modif.setPrenom(prenom);

				if (societe != null)
					user_to_modif.setSociete(societe);
				if (tel != null)
					user_to_modif.setTel(tel);
				if (statut != null)
					user_to_modif.setStatut(statut);

				utilisateurDao.modifier(user_to_modif);
				
				resultat = "Modification effectuée.";
			}


		} else {
			resultat = "Échec de la création/modification .";
		}

		/* Stockage du résultat et des messages d'erreur dans l'objet request */

		/* on récupère la liste des utilisateurs */
		users = utilisateurDao.lister();

		/* On prépare la liste des questionnaires à affichées (page) */
		List<Utilisateur> LuserToAffiche = new ArrayList<Utilisateur>();
		for (int i = 1 ; i <= nb_user_affich ; i++){
			if ((i+nb_user_affich*(page-1) -1) < users.size())
				LuserToAffiche.add(i-1, users.get(i+nb_user_affich*(page-1) -1));
		}
		
		/* on la passe en attribut à la requete */
		request.setAttribute( ATT_LISTUSERS, LuserToAffiche );
		request.setAttribute( ATT_ERREURS, erreurs );
		request.setAttribute( ATT_RESULTAT, resultat );
		
		session.setAttribute( PAGEMAX,  Math.ceil((double)users.size() / (double)nb_user_affich));

		/* Actualisation de la page */
		this.getServletContext().getRequestDispatcher( AFFICHAGE ).forward( request, response );
	}

	// Retourne vraie si le mail est deja utilisé false sinon
	private boolean MailDejaUsed( String email ){
		if(utilisateurDao.trouver_byMail(email) != null)
			return true;
		else
			return false;
	}

	/*
	 * Méthode utilitaire qui retourne null si un paramètre est vide, et son contenu sinon.
	 */
	private static String getValeurParametre( HttpServletRequest request, String nomChamp ) {
		String valeur = request.getParameter( nomChamp );
		if ( valeur == null || valeur.trim().length() == 0 ) {
			return null;
		} else {
			return valeur;
		}
	}

}
