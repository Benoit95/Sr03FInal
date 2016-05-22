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

import beans.Question;
import beans.Questionnaire;
import dao.DAOFactory;
import dao.QuestionDAO;
import dao.QuestionnaireDAO;
import dao.ReponseDAO;

public class Questionnaire1by1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String AFFICHAGE          = "/Admin/Questionnaire1by1.jsp";
	public static final String CONF_DAO_FACTORY = "daofactory";
	public static final String PARAM_QUESTIONNAIRE = "IDquestionnaire";
	public static final String ATT_QUESTIONNAIRE = "questionnaire";
	public static final String PAGE = "page";
	public static final String PAGEMAX = "pageMax";
	public static final String ATT_SESSION_NB_QUESTION_PAGE = "sessionQuestionPage";

	private QuestionnaireDAO     questionnaireDAO;
	private QuestionDAO     questionDAO;
	private ReponseDAO     reponseDAO;
	private Questionnaire questionnaire;

	public Questionnaire1by1() {
		super();
	}

	public void init() throws ServletException {
		/* Récupération d'une instance de nos DAO utiles */
		this.questionnaireDAO = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getQuestionnaireDao();
		this.questionDAO = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getQuestionDao();
		this.reponseDAO = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getReponseDao();
	}    

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Récupération de la session*/
		HttpSession session = request.getSession();

			// Récupération du questionnaire a visualiser
			String questionnaireIDS = getValeurParametre( request, PARAM_QUESTIONNAIRE);
			String page = getValeurParametre( request, PAGE);
			int pageI = Integer.parseInt(page);

			int nb_quest_affich = (int) session.getAttribute(ATT_SESSION_NB_QUESTION_PAGE);

			if (questionnaireIDS != null){
				int questionnaireID = Integer.parseInt(questionnaireIDS);
				questionnaire = questionnaireDAO.trouver_ByID(questionnaireID);

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

				// On rajoute les Questions/Reponses dans le questionnaire.
				questionnaire.setLQuestions(LquestionToAffiche);
				request.setAttribute( ATT_QUESTIONNAIRE, questionnaire );
				request.setAttribute( PAGE, pageI );
				request.setAttribute( PAGEMAX,  Math.ceil((double)Lquestion.size() / (double)nb_quest_affich));
			}

			/* Affichage du questionnaire (jsp) */
			this.getServletContext().getRequestDispatcher( AFFICHAGE ).forward( request, response );
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
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
