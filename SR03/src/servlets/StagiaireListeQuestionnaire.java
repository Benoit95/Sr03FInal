package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Questionnaire;
import dao.DAOFactory;
import dao.QuestionnaireDAO;
import forms.QuestionnaireForm;

public class StagiaireListeQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String AFFICHAGE          = "/Stagiaire/StagiaireListeQuestionnaire.jsp";
	public static final String EFFECTUER_QUEST          = "/Stagiaire/StagiaireEffectuerQuestionnaire.jsp";
	public static final String ATT_SESSION_USER = "sessionUtilisateur";
	public static final String CONF_DAO_FACTORY = "daofactory";

	public static final String PARAM_QUESTIONNAIRE_ID   = "QuestionnaireID";

	public static final String ATT_LIST_Questionnaires = "L_questionnaires";
	public static final String ATT_ERREURS  = "erreurs";
	public static final String ATT_RESULTAT = "resultat";
	
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

	public StagiaireListeQuestionnaire() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Récupération de la session*/
		HttpSession session = request.getSession();

			int pageI;
			int nb_quest_affich = (int) session.getAttribute(ATT_SESSION_NB_QUESTIONNAIRE_PAGE);

			/* Récupération des paramètres (si le stagiaire veut effectuer le questionnaire ) */
			String QuestID = getValeurParametre( request, PARAM_QUESTIONNAIRE_ID);

			// Si paramètre de "effectuer" reçu :
			if (QuestID != null){
				
				// On trouve le questionnaire a effecter avec son ID
				int QuestIDToDelete = Integer.parseInt(QuestID);
				Questionnaire quest_to_delete = new Questionnaire();
				quest_to_delete = questionnaireDAO.trouver_ByID(QuestIDToDelete);

				// Si on a trouvé le questionnaire
				if (quest_to_delete != null){
					// On lance le servlet s'occupant d'effectuer le questionnaire
					this.getServletContext().getRequestDispatcher( EFFECTUER_QUEST ).forward( request, response );
				}
				// On récupère la valeur de la page actuelle en session.
				pageI = (int) session.getAttribute(PAGE);
				
				
			}else{
				// On récupère la valeur de la page actuelle dans la requete
				pageI = Integer.parseInt(getValeurParametre( request, PAGE));
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
			
			/* On passe aussi la valeur de la page et de pagemax */
			session.setAttribute(PAGE, pageI);
			session.setAttribute( PAGEMAX,  Math.ceil((double)L_questionnaires.size() / (double)nb_quest_affich));

			/* Affichage de la page de d'affichage questionnaire */
			this.getServletContext().getRequestDispatcher( AFFICHAGE ).forward( request, response );
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
