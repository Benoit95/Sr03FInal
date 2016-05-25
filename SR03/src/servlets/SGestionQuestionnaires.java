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

import beans.Questionnaire;
import dao.DAOException;
import dao.DAOFactory;
import dao.QuestionnaireDAO;
import forms.QuestionnaireForm;

public class SGestionQuestionnaires extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String AFFICHAGE          = "/Admin/GestionQuestionnaires.jsp";
	public static final String CONF_DAO_FACTORY = "daofactory";

	public static final String CHAMP_SUJET_A = "sujetA";
	public static final String CHAMP_STATUT_A  = "statutA";

	public static final String PARAM_QUEST_TO_DELETE   = "QuestionnaireID_to_delete";
	public static final String PARAM_QUEST_TO_MODIF   = "QuestionnaireID_to_modif";

	public static final String ATT_LIST_Questionnaires = "L_questionnaires";
	public static final String ATT_ERREURS  = "erreurs";
	public static final String ATT_RESULTAT = "resultat";
	
	public static final String R_TYPE = "type";
	public static final String PAGE = "page";
	public static final String PAGEMAX = "pageMax";
	public static final String ATT_SESSION_NB_QUESTIONNAIRE_PAGE = "sessionQuestionnairePage";
	
	
	private QuestionnaireDAO     questionnaireDAO;
	List<Questionnaire> L_questionnaires;
	QuestionnaireForm questForm = new QuestionnaireForm();

	public void init() throws ServletException {
		/* Récupération d'une instance de notre DAO Utilisateur */
		this.questionnaireDAO = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getQuestionnaireDao();
	}

	public SGestionQuestionnaires() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Récupération de la session*/
		HttpSession session = request.getSession();

			String resultat ="";
			int pageI;
			int nb_quest_affich = (int) session.getAttribute(ATT_SESSION_NB_QUESTIONNAIRE_PAGE);

			/* Récupération des paramètres (si suppression/modification demandé) */
			String QuestIDToDeleteS = getValeurParametre( request, PARAM_QUEST_TO_DELETE);

			if (getValeurParametre( request, PAGE) == null)
				// On récupère la valeur de la page actuelle en session.
				pageI = (int) session.getAttribute(PAGE);
			else
				// On récupère la valeur de la page actuelle dans la requete
				pageI = Integer.parseInt(getValeurParametre( request, PAGE));
			
			// Si paramètre de suppression reçu :
			if (QuestIDToDeleteS != null){
				
				// On trouve le questionnaire a supprimer avec son ID
				int QuestIDToDelete = Integer.parseInt(QuestIDToDeleteS);
				Questionnaire quest_to_delete = new Questionnaire();
				quest_to_delete = questionnaireDAO.trouver_ByID(QuestIDToDelete);

				// Si on a trouvé le questionnaire
				if (quest_to_delete != null){
					try{
						/* Alors suppression du questionnaire de la BDD */
						questionnaireDAO.supprimer( quest_to_delete);	
					} catch ( DAOException e ) {
						resultat = e.getMessage();
					}
				}
				
				resultat = "Suppression réussie";
				
			}

			/* on récupère la liste des questionnaires */
			L_questionnaires = questionnaireDAO.lister();
			
			/* On prépare la liste des questionnaires à affichées (page) */
			List<Questionnaire> LquestionnaireToAffiche = new ArrayList<Questionnaire>();
			for (int i = 1 ; i <= nb_quest_affich ; i++){
				if ((i+nb_quest_affich*(pageI-1) -1) < L_questionnaires.size())
					LquestionnaireToAffiche.add(i-1, L_questionnaires.get(i+nb_quest_affich*(pageI-1) -1));
			}

			/* on la passe en attribut à la requete */
			request.setAttribute( ATT_LIST_Questionnaires, LquestionnaireToAffiche );
			session.setAttribute( R_TYPE, "questionnaire"); // att session pour la recherche
			request.setAttribute( ATT_RESULTAT, resultat );
			
			/* On passe aussi la valeur de la page et de pagemax */
			session.setAttribute(PAGE, pageI);
			session.setAttribute( PAGEMAX,  Math.ceil((double)L_questionnaires.size() / (double)nb_quest_affich));

			/* Affichage de la page de d'affichage questionnaire */
			this.getServletContext().getRequestDispatcher( AFFICHAGE ).forward( request, response );
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resultat;
		Map<String, String> erreurs = new HashMap<String, String>();
		String sujet = request.getParameter( CHAMP_SUJET_A );
		String statut = request.getParameter( CHAMP_STATUT_A );

		/* Récupération de la session*/
		HttpSession session = request.getSession();
		int page = (int) session.getAttribute(PAGE);
		int nb_quest_affich = (int) session.getAttribute(ATT_SESSION_NB_QUESTIONNAIRE_PAGE);
		
		/* Validation du champ sujet. */
		try {
			questForm.validationSujet(sujet);
			QuestDejaUsed(sujet);
		} catch ( Exception e ) {
			erreurs.put( CHAMP_SUJET_A, e.getMessage() );
		}

		/* Initialisation du résultat global de la validation. */
		if ( erreurs.isEmpty() ) {
			resultat = "Succès de l'ajout.";

			Questionnaire quest_to_create = new Questionnaire();
			quest_to_create.setSujet(sujet);
			quest_to_create.setStatut(statut);

			questionnaireDAO.creer(quest_to_create);				

		} else {
			resultat = "Échec de l'ajout.";
		}

		/* on récupère la liste des questionnaires */
		L_questionnaires = questionnaireDAO.lister();
		
		List<Questionnaire> LquestionnaireToAffiche = new ArrayList<Questionnaire>();
		for (int i = 1 ; i <= nb_quest_affich ; i++){
			if ((i+nb_quest_affich*(page-1) -1) < L_questionnaires.size())
				LquestionnaireToAffiche.add(i-1, L_questionnaires.get(i+nb_quest_affich*(page-1) -1));
		}

		/* on la passe en attribut à la requete */
		request.setAttribute( ATT_LIST_Questionnaires, LquestionnaireToAffiche );
		session.setAttribute( PAGEMAX,  Math.ceil((double)L_questionnaires.size() / (double)nb_quest_affich));

		request.setAttribute( ATT_ERREURS, erreurs );
		request.setAttribute( ATT_RESULTAT, resultat );

		/* Actualisation de la page */
		this.getServletContext().getRequestDispatcher( AFFICHAGE ).forward( request, response );
	}

	// Déclenche une erreur si sujet deja utilisé
	private void QuestDejaUsed( String sujet ) throws DAOException{
		if(questionnaireDAO.trouver_BySujet(sujet) != null)
			throw new DAOException( "Ce sujet est déjà utilisé" );
	}

	// Méthode utilitaire qui retourne null si un paramètre est vide, et son contenu sinon.
	private static String getValeurParametre( HttpServletRequest request, String nomChamp ) {
		String valeur = request.getParameter( nomChamp );
		if ( valeur == null || valeur.trim().length() == 0 ) {
			return null;
		} else {
			return valeur;
		}
	}
}
