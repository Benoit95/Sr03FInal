package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Parcours;
import beans.Question;
import beans.Questionnaire;
import beans.Utilisateur;
import dao.DAOFactory;
import dao.ParcoursDao;
import dao.QuestionDAO;
import dao.QuestionnaireDAO;
import dao.ReponseDAO;

public class StagiaireEffectuerQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String AFFICHAGE          = "/Stagiaire/StagiaireEffectuerQuestionnaire.jsp";
	public static final String AFFICHAGE_LISTPARCOURS          = "StagiaireListeParcours?page=1";
	
	
	public static final String CONF_DAO_FACTORY = "daofactory";
	public static final String PARAM_QUESTIONNAIRE = "QuestionnaireID";
	public static final String ATT_QUESTIONNAIRE = "questionnaire";
	public static final String PAGE = "page";
	public static final String PAGEMAX = "pageMax";
	public static final String ATT_SESSION_USER = "sessionUtilisateur";
	public static final String ATT_SESSION_NB_QUESTION_PAGE_STAGIAIRE = "sessionQuestionPageStagiaire";

	public static final String CHAMP_REPONSE = "reponse";
	public static final String ATT_QUESTIONNAIRE_USER = "questionnaireUser";
	public static final String  ATT_QUESTIONNAIRE_SESSION = "questionnaireSession";
	public static final String ATT_LIST_PARCOURS_SESSION = "ListParcoursSession";
	public static final String START_DUREE = "start_duree";

	private QuestionnaireDAO     questionnaireDAO;
	private QuestionDAO     questionDAO;
	private ReponseDAO     reponseDAO;
	private ParcoursDao parcoursDAO;
	private Questionnaire questionnaireAffiche;

	public StagiaireEffectuerQuestionnaire() {
		super();
	}

	public void init() throws ServletException {
		/* Récupération d'une instance de nos DAO utiles */
		this.questionnaireDAO = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getQuestionnaireDao();
		this.questionDAO = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getQuestionDao();
		this.reponseDAO = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getReponseDao();
		this.parcoursDAO = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getParcoursDao();
	}    

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Récupération de la session*/
		HttpSession session = request.getSession();

			int pageI = 0;
			int questionnaireID = 0;

			// Récupération du questionnaire a visualiser
			String questionnaireIDS = getValeurParametre( request, PARAM_QUESTIONNAIRE);
			String page = getValeurParametre( request, PAGE);

			if(questionnaireIDS == null ||  page == null){
				Questionnaire questionnaireS = (Questionnaire) session.getAttribute( ATT_QUESTIONNAIRE_SESSION );
				questionnaireID = (int) questionnaireS.getId();
				pageI = (int) session.getAttribute( PAGE );
			}else{
				pageI = Integer.parseInt(page);
				questionnaireID = Integer.parseInt(questionnaireIDS);
			}

			int nb_quest_affich = (int) session.getAttribute(ATT_SESSION_NB_QUESTION_PAGE_STAGIAIRE);

			questionnaireAffiche = questionnaireDAO.trouver_ByID(questionnaireID);
			Questionnaire questionnaire = questionnaireDAO.trouver_ByID(questionnaireID);

			// Récupération des questions de ce questionnaires.
			List<Question> Lquestion = new ArrayList<Question>();
			if (questionDAO.lister(questionnaireID) != null)
				Lquestion.addAll(questionDAO.lister(questionnaireID));

			List<Question> LquestionToAffiche = new ArrayList<Question>();
			for (int i = 1 ; i <= nb_quest_affich ; i++){
				if ((i*pageI -1) < Lquestion.size())
					LquestionToAffiche.add(i-1, Lquestion.get(i*pageI -1));
			}

			// Pour les questions demandées , on récupère les réponses associées.
			for (int i = 1 ; i <= nb_quest_affich ; i++){
				if ((i*pageI -1) < Lquestion.size())
					LquestionToAffiche.get(i-1).setLReponses(reponseDAO.lister(Lquestion.get(i*pageI -1).getId()));
			}

			// ....
			for (int i = 0 ; i < Lquestion.size() ; i++){
				Lquestion.get(i).setLReponses(reponseDAO.lister(Lquestion.get(i).getId()));
			}

			// On rajoute les Questions/Reponses dans le questionnaire.
			questionnaireAffiche.setLQuestions(LquestionToAffiche);
			questionnaire.setLQuestions(Lquestion);

			session.setAttribute( ATT_QUESTIONNAIRE_SESSION, questionnaire );
			request.setAttribute( ATT_QUESTIONNAIRE, questionnaireAffiche );
			session.setAttribute( PAGE, pageI );
			session.setAttribute( PAGEMAX,  Math.ceil((double)Lquestion.size() / (double)nb_quest_affich));
			session.setAttribute( START_DUREE, System.currentTimeMillis());

			/* Affichage du questionnaire (jsp) */
			this.getServletContext().getRequestDispatcher( AFFICHAGE ).forward( request, response );
	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Récupération de la session*/
		HttpSession session = request.getSession();
		int pageI = (int) session.getAttribute( PAGE );
		Double pageMax = (Double) session.getAttribute( PAGEMAX );
		Questionnaire questionnaire = (Questionnaire) session.getAttribute(ATT_QUESTIONNAIRE_SESSION);
		Utilisateur user_co = (Utilisateur) session.getAttribute( ATT_SESSION_USER );
		long start_duree = (long) session.getAttribute( START_DUREE);

		int id_reponse = 0;
		ArrayList<Parcours> L_Parcours = new ArrayList<Parcours>();

		// Si l'utilisateur a saisit une reponse
		if (request.getParameter( CHAMP_REPONSE) != null){
			//Traiter la réponse choisit par l'utilisateur (sauvegarde etc...)
			id_reponse = Integer.parseInt(request.getParameter( CHAMP_REPONSE));

			// Si c'est la premire page d'un questionnaire ... sinon ...
			if (pageI == 1){
				//
			}else{
				L_Parcours = (ArrayList<Parcours>) session.getAttribute( ATT_LIST_PARCOURS_SESSION );
			}

			// On supprime les réponses de la question en cours (questionnaire user)
			// et on ajoute seulement la réponse choisit par l'utilisateur en session...
			Parcours p = new Parcours();
			p.setId_user(user_co.getId());//User ID
			p.setId_questionnaire(questionnaire.getId()); // Questionnaire ID
			p.setId_question(questionnaire.getLQuestions().get(pageI-1).getId()); // Question ID
			p.setId_reponse(id_reponse);	//Reponse ID
			
			if (reponseDAO.trouver_ByID(id_reponse).getEstValide().contains("oui"))//score
				p.setScore(1);
			else
				p.setScore(0);
			
			p.setDuree(System.currentTimeMillis()-start_duree); //duree
		
			L_Parcours.add(p);
			System.out.println("a:"+L_Parcours.size());

			session.setAttribute(ATT_LIST_PARCOURS_SESSION, L_Parcours);
			System.out.println("b:"+((ArrayList<Parcours>)session.getAttribute(ATT_LIST_PARCOURS_SESSION)).size());

			// Si dernière question valider -> Page de résultat + Sauvegarde en BDD.
			if (pageI >= pageMax){
				
				// Si un parcours de ce questionnaire a deja été enregistrer
				if (parcoursDAO.trouver_Parcours_User(user_co.getId(), questionnaire.getId()) != null) // Si parcorus deja présent
					parcoursDAO.supprimer(p); // on le supprime
				
				// on sauvegarde le parcours en BDD.
				for (int i = 0 ; i < L_Parcours.size() ; i++){	
					parcoursDAO.creer(L_Parcours.get(i));	
				}
				
				
				// On renvoie l'utilisateur sur la page des "résultats"
				pageI = 1;
				session.setAttribute( PAGE, pageI );
				
				response.sendRedirect( AFFICHAGE_LISTPARCOURS );
				
				
			}else{
				// Sinon passer à la question suivante
				pageI ++;
				session.setAttribute( PAGE, pageI );
				doGet(request, response);       	
			}

		}

		
		else{
			//Sinon on le renvoie sur sa page actuelle..
			doGet(request, response);
		}

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
