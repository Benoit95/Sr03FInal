package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Utilisateur;
import dao.UtilisateurDao;
import forms.creationUserForm;
import dao.DAOFactory;

public class SLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String LOGIN          = "/WEB-INF/login.jsp";
    public static final String ADMIN          = "admin";
    public static final String STAGIAIRE      = "/stagiaire";
    public static final String CHAMP_EMAIL  = "emailUser";
    public static final String CHAMP_PASS   = "mdpUser";
    
    public static final String ATT_ERREURS  = "erreurs";
    public static final String ATT_RESULTAT = "resultat";
    public static final String ATT_UTILISATEUR = "utilisateur";
    
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    public static final String ATT_SESSION_NB_USER_PAGE = "sessionUserPage";
    public static final String ATT_SESSION_NB_QUESTIONNAIRE_PAGE = "sessionQuestionnairePage";
    public static final String ATT_SESSION_NB_QUESTION_PAGE = "sessionQuestionPage";
    public static final String ATT_SESSION_NB_QUESTION_PAGE_STAGIAIRE = "sessionQuestionPageStagiaire";
    public static final String ATT_SESSION_NB_REPONSE_PAGE = "sessionReponsePage";
	public static final String R_TYPE = "type";
	public static final String R_ID = "ID";
    
    public SLogin() {
        super();
    }
    
    private UtilisateurDao     utilisateurDao;
    Utilisateur connectedUser;
    creationUserForm crea = new creationUserForm();

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUtilisateurDao();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /* Affichage de la page de login */
        this.getServletContext().getRequestDispatcher( LOGIN ).forward( request, response );
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String resultat;
        Map<String, String> erreurs = new HashMap<String, String>();

        /* Récupération des champs du formulaire. */
        String email = request.getParameter( CHAMP_EMAIL );
        String motDePasse = request.getParameter( CHAMP_PASS );

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
        
        /* Validation des champs mot de passe et mail en BDD. */
        try {
            validationBDD( motDePasse , email );
        } catch ( Exception e ) {
            erreurs.put( CHAMP_EMAIL, e.getMessage() );
        }

        /* Initialisation du résultat global de la validation. */
        if ( erreurs.isEmpty() ) {
            resultat = "Succès.";
        } else {
            resultat = "Échec.";
        }
        
        /* Récupération de la session depuis la requête */
        HttpSession session = request.getSession();

        if (resultat == "Échec."){
            /* Stockage du résultat et des messages d'erreur dans l'objet request */
            request.setAttribute( ATT_ERREURS, erreurs );
            request.setAttribute( ATT_RESULTAT, resultat );
        	
        	/* Transmission de la paire d'objets request/response à notre JSP login */
        	this.getServletContext().getRequestDispatcher( LOGIN ).forward( request, response );
       
        }else{
        	// Si l'utilisateur est correct
        	session.setAttribute( ATT_SESSION_USER, connectedUser );
        	
        	session.setAttribute( ATT_SESSION_NB_USER_PAGE, 5 );
        	session.setAttribute( ATT_SESSION_NB_QUESTIONNAIRE_PAGE, 3 );
        	session.setAttribute( ATT_SESSION_NB_QUESTION_PAGE, 3 );
        	session.setAttribute( ATT_SESSION_NB_REPONSE_PAGE, 3 );
        	session.setAttribute( ATT_SESSION_NB_QUESTION_PAGE_STAGIAIRE, 1);
        	
        	session.setAttribute( R_TYPE, "questionnaire" );
        	session.setAttribute( R_ID, 0 );

        	
        	if (connectedUser.getAdmin() == true)
        		response.sendRedirect( ADMIN );
        		//this.getServletContext().getRequestDispatcher( ADMIN ).forward( request, response );
        	else
        		this.getServletContext().getRequestDispatcher( STAGIAIRE ).forward( request, response );
        }
        	
	}

	private void validationBDD( String motDePasse, String email) throws Exception{
		connectedUser = utilisateurDao.trouver_byMail( email );
		if(connectedUser == null)
			throw new Exception("identifiant (mail) inconnu dans la BDD");
		else{
			connectedUser = utilisateurDao.trouver_byMailMDp( email, motDePasse );
			if(connectedUser == null)
				throw new Exception("Votre mdp ne correspond pas à votre identifiant (mail)");
		}
	}
}
