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

import beans.Question;
import dao.DAOException;
import dao.DAOFactory;
import dao.QuestionDAO;
import forms.QuestionForm;


public class SGestionQuestions extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String AFFICHAGE          = "/Admin/GestionQuestions.jsp";
	public static final String CONF_DAO_FACTORY = "daofactory";

	public static final String CHAMP_IDQUESTIONNAIRE_A = "IdQuestionnaire";
	public static final String CHAMP_Text_A = "TextA";
	public static final String CHAMP_STATUT_A  = "statutA";
	public static final String CHAMP_ORDRE  = "ordre";

	public static final String PARAM_QUESTIONNAIRE= "QuestionnaireID";
	public static final String PARAM_QUEST_TO_DELETE   = "QuestionID_to_delete";
	public static final String PARAM_QUEST_TO_MODIF   = "QuestionID_to_modif";
	
	public static final String ATT_QUESTIONNAIRE= "QuestionnaireID";
	public static final String ATT_LIST_Questions = "L_Questions";
	public static final String ATT_ERREURS  = "erreurs";
	public static final String ATT_RESULTAT = "resultat";
	
	public static final String R_TYPE = "type";
	public static final String R_ID = "ID";
	public static final String PAGE = "page";
	public static final String PAGEMAX = "pageMax";
	public static final String ATT_SESSION_NB_QUESTION_PAGE = "sessionQuestionPage";

	private QuestionDAO     QuestionDAO;
	List<Question> L_Questions;
	QuestionForm questForm = new QuestionForm();

	public void init() throws ServletException {
		/* Récupération d'une instance de notre DAO Utilisateur */
		this.QuestionDAO = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getQuestionDao();
	}

	public SGestionQuestions() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Récupération de la session*/
		HttpSession session = request.getSession();

			String resultat ="";
			int pageI;
			int nb_quest_affich = (int) session.getAttribute(ATT_SESSION_NB_QUESTION_PAGE);

			/* Récupération des paramètres (si suppression d'une question demandée) */
			String QuestIDToDeleteS = getValeurParametre( request, PARAM_QUEST_TO_DELETE);
			int QuestionnaireID = Integer.parseInt(getValeurParametre( request, PARAM_QUESTIONNAIRE));

			if (getValeurParametre( request, PAGE) == null)
				// On récupère la valeur de la page actuelle en session.
				pageI = (int) session.getAttribute(PAGE);
			else
				// On récupère la valeur de la page actuelle dans la requete
				pageI = Integer.parseInt(getValeurParametre( request, PAGE));
			
			// Si paramètre de suppression reçu :
			if (QuestIDToDeleteS != null){
				int QuestIDToDelete = Integer.parseInt(QuestIDToDeleteS);
				Question quest_to_delete = new Question();
				quest_to_delete = QuestionDAO.trouver_ByID(QuestIDToDelete);

				if (quest_to_delete != null){
					try{
						/* Alors suppression du client de la BDD */
						QuestionDAO.supprimer( quest_to_delete);	
					} catch ( DAOException e ) {
						resultat = e.getMessage();
					}
				}

				
				resultat = "Suppression de la question effectuée avec succès";
			}

			/* on récupère la liste des questions */
			L_Questions = QuestionDAO.lister(QuestionnaireID);

			/* On prépare la liste des questions à affichés (page) */
			List<Question> LQuestionToAffiche = new ArrayList<Question>();
			for (int i = 1 ; i <= nb_quest_affich ; i++){
				if ((i+nb_quest_affich*(pageI-1) -1) < L_Questions.size())
					LQuestionToAffiche.add(i-1, L_Questions.get(i+nb_quest_affich*(pageI-1) -1));
			}
			
			/* on la passe en attribut à la requete */
			request.setAttribute( ATT_QUESTIONNAIRE, QuestionnaireID );
			request.setAttribute( ATT_LIST_Questions, LQuestionToAffiche );
			request.setAttribute( ATT_RESULTAT, resultat );
			
			/* On passe aussi la valeur de la page et de pagemax */
			session.setAttribute( R_TYPE, "question"); // att session pour la recherche
			session.setAttribute( R_ID, QuestionnaireID); // att session pour la recherche
			session.setAttribute(PAGE, pageI);
			session.setAttribute( PAGEMAX,  Math.ceil((double)L_Questions.size() / (double)nb_quest_affich));

			/* Affichage de la page de d'affichage question */
			this.getServletContext().getRequestDispatcher( AFFICHAGE ).forward( request, response );

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resultat;
		Map<String, String> erreurs = new HashMap<String, String>();
		String Text = request.getParameter( CHAMP_Text_A );
		String statut = request.getParameter( CHAMP_STATUT_A );
		String IdQuestionnaire = request.getParameter( CHAMP_IDQUESTIONNAIRE_A );
		String ordre = request.getParameter( CHAMP_ORDRE);

		/* Récupération de la session*/
		HttpSession session = request.getSession();
		int page = (int) session.getAttribute(PAGE);
		int nb_quest_affich = (int) session.getAttribute(ATT_SESSION_NB_QUESTION_PAGE);
		
		/* Validation du champ Text. */
		try {
			questForm.validationText(Text);
			// A voir si on garde ... QuestDejaUsed(Text, Integer.parseInt(IdQuestionnaire));
		} catch ( Exception e ) {
			erreurs.put( CHAMP_Text_A, e.getMessage() );
		}

		/* Validation du champ Ordre. */
		try {
			questForm.validationOrdre(ordre);
		} catch ( Exception e ) {
			erreurs.put( CHAMP_ORDRE, e.getMessage() );
		}

		/* Initialisation du résultat global de la validation. */
		if ( erreurs.isEmpty() ) {
			resultat = "Succès de l'ajout.";

			Question quest_to_create = new Question();
			quest_to_create.setText(Text);
			quest_to_create.setId_questionnaire(Integer.parseInt(IdQuestionnaire));
			quest_to_create.setStatut(statut);
			quest_to_create.setOrdre(Integer.parseInt(ordre));

			QuestionDAO.creer(quest_to_create);				

		} else {
			resultat = "Échec de l'ajout.";
		}

		/* on récupère la liste des questions */
		L_Questions = QuestionDAO.lister(Integer.parseInt(IdQuestionnaire));

		/* On prépare la liste des questions à affichés (page) */
		List<Question> LQuestionToAffiche = new ArrayList<Question>();
		for (int i = 1 ; i <= nb_quest_affich ; i++){
			if ((i+nb_quest_affich*(page-1) -1) < L_Questions.size())
				LQuestionToAffiche.add(i-1, L_Questions.get(i+nb_quest_affich*(page-1) -1));
		}

		/* on la passe en attribut à la requete */
		request.setAttribute( ATT_LIST_Questions, LQuestionToAffiche );
		request.setAttribute( ATT_QUESTIONNAIRE, Integer.parseInt(IdQuestionnaire) );
		
		request.setAttribute( ATT_ERREURS, erreurs );
		request.setAttribute( ATT_RESULTAT, resultat );
		
		session.setAttribute( PAGEMAX,  Math.ceil((double)L_Questions.size() / (double)nb_quest_affich));

		/* Actualisation de la page */
		this.getServletContext().getRequestDispatcher( AFFICHAGE ).forward( request, response );
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
