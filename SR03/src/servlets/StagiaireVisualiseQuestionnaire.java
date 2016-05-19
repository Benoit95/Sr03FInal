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
import beans.Reponse;
import beans.Utilisateur;
import dao.DAOException;
import dao.DAOFactory;
import dao.ParcoursDao;
import dao.QuestionDAO;
import dao.QuestionnaireDAO;
import dao.ReponseDAO;

public class StagiaireVisualiseQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String AFFICHAGE          = "/WEB-INF/StagiaireVisualiseQuestionnaire.jsp";
	public static final String ACCESSREFUSED = "/RefuseAccess.jsp";
	public static final String CONF_DAO_FACTORY = "daofactory";
	public static final String PARAM_QUESTIONNAIRE = "IDquestionnaire";
	
	public static final String ATT_QUESTIONNAIRE = "questionnaire";
	public static final String ATT_SESSION_USER = "sessionUtilisateur";
	public static final String ATT_SESSION_NB_QUESTION_PAGE = "sessionQuestionPage";
	
	public static final String ATT_SCORE = "score";
	public static final String ATT_DUREE = "duree";
	public static final String ATT_NB_QUESTIONS = "nb_questions";

	private QuestionnaireDAO     questionnaireDAO;
	private QuestionDAO     questionDAO;
	private ReponseDAO     reponseDAO;
	private Questionnaire questionnaire;
	private ParcoursDao parcoursDAO;

	public StagiaireVisualiseQuestionnaire() {
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
		Utilisateur user_co = (Utilisateur) session.getAttribute( ATT_SESSION_USER );
		
		// Si l'utilisateur connecté est bien un stagiaire
		if (user_co != null && user_co.getAdmin() == false){

			// Récupération du questionnaire a visualiser
			String questionnaireIDS = getValeurParametre( request, PARAM_QUESTIONNAIRE);

			if (questionnaireIDS != null){
				int questionnaireID = Integer.parseInt(questionnaireIDS);
				questionnaire = questionnaireDAO.trouver_ByID(questionnaireID);
				
				// Récupération des questions de ce questionnaires.
				List<Question> Lquestion = new ArrayList<Question>();
				if (questionDAO.lister(questionnaireID) != null)
					Lquestion.addAll(questionDAO.lister(questionnaireID));
				
				// Pour chacune des questions , on récupère la réponse donnée par l'utilisateur.
				for (int i = 0 ; i < Lquestion.size() ; i++){
					List<Reponse> Lreponse = new ArrayList<Reponse>();
					Parcours parcours = parcoursDAO.trouver_Question_user(user_co.getId(), Lquestion.get(i).getId());
					Lreponse.add(reponseDAO.trouver_ByID(parcours.getId_reponse()));
					System.out.println(parcours.getId_reponse());
					Lquestion.get(i).setLReponses(Lreponse);
				}
					

				// On rajoute les Questions/Reponses dans le questionnaire.
				questionnaire.setLQuestions(Lquestion);
				
				// On calcul le score du questionnaire et sa durée..
				List<Parcours> L_Parcours = parcoursDAO.trouver_Parcours_User(user_co.getId(), questionnaireID);
				int score = 0;
				long duree = 0;
				
				for (int i=0 ; i< L_Parcours.size() ; i++){
					score += L_Parcours.get(i).getScore();
					duree += L_Parcours.get(i).getDuree();
				}
				
				request.setAttribute( ATT_QUESTIONNAIRE, questionnaire );
				request.setAttribute( ATT_SCORE, score );
				request.setAttribute( ATT_NB_QUESTIONS, Lquestion.size() );
				request.setAttribute( ATT_DUREE, duree );
			}

			/* Affichage du questionnaire (jsp) */
			this.getServletContext().getRequestDispatcher( AFFICHAGE ).forward( request, response );
		}else
			this.getServletContext().getRequestDispatcher( ACCESSREFUSED ).forward( request, response );
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
