package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Question;
import beans.Questionnaire;
import beans.Reponse;
import beans.Utilisateur;
import dao.DAOFactory;
import dao.QuestionDAO;
import dao.QuestionnaireDAO;
import dao.ReponseDAO;
import dao.UtilisateurDao;
import forms.QuestionnaireForm;

public class SRecherche extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String AFFICHAGE_USER = "/WEB-INF/GestionUsers.jsp";
	public static final String AFFICHAGE_QUESTIONNAIRE = "/WEB-INF/GestionQuestionnaires.jsp";
	public static final String AFFICHAGE_QUESTION = "/WEB-INF/GestionQuestions.jsp";
	public static final String AFFICHAGE_REPONSE = "/WEB-INF/GestionReponses.jsp";

	public static final String ATT_SESSION_USER = "sessionUtilisateur";
	public static final String ACCESSREFUSED = "/RefuseAccess.jsp";
	public static final String CONF_DAO_FACTORY = "daofactory";

	public static final String R_TYPE = "type";
	public static final String R_ID = "ID";
	public static final String CHAMP_VALEUR  = "valeur";

	public static final String ATT_LISTUSERS = "users";
	public static final String ATT_LIST_Questionnaires = "L_questionnaires";
	public static final String ATT_LIST_Questions = "L_Questions";
	public static final String ATT_LIST_Reponses = "L_Reponses";

	public static final String ATT_ERREURS  = "erreurs";
	public static final String ATT_RESULTAT = "resultat";

	public static final String PAGE = "page";
	public static final String PAGEMAX = "pageMax";
	public static final String ATT_SESSION_NB_QUESTIONNAIRE_PAGE = "sessionQuestionnairePage";


	private QuestionnaireDAO     questionnaireDAO;
	private QuestionDAO     questionDAO;
	private ReponseDAO     reponseDAO;
	private UtilisateurDao userDAO;
	QuestionnaireForm questForm = new QuestionnaireForm();

	public void init() throws ServletException {
		/* Récupération des DAO */
		this.questionnaireDAO = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getQuestionnaireDao();
		this.questionDAO = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getQuestionDao();
		this.reponseDAO = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getReponseDao();
		this.userDAO = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUtilisateurDao();
	}

	public SRecherche() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String valeur = request.getParameter( CHAMP_VALEUR );

		/* Récupération de la session*/
		HttpSession session = request.getSession();

		String type = (String) session.getAttribute( R_TYPE );	// Récupération du type (questionnaire/question/reponse)
		int ID = (int) session.getAttribute( R_ID ); // Récupération de ID nécessaire(questionnaire/question)

		int page = (int) session.getAttribute(PAGE);
		int nb_quest_affich = (int) session.getAttribute(ATT_SESSION_NB_QUESTIONNAIRE_PAGE);

		// Si on recherche un questionnaire
		if (type.contains("questionnaire")){
			if (questionnaireDAO.trouver_BySujet(valeur) != null){
				List<Questionnaire> LquestionnaireToAffiche = new ArrayList<Questionnaire>();
				LquestionnaireToAffiche.add(questionnaireDAO.trouver_BySujet(valeur));

				/* on la passe en attribut à la requete */
				request.setAttribute( ATT_LIST_Questionnaires, LquestionnaireToAffiche );
				session.setAttribute( PAGEMAX,  Math.ceil((double)LquestionnaireToAffiche.size() / (double)nb_quest_affich));

			}else
				session.setAttribute( PAGEMAX, 1);
			/* Actualisation de la page */
			if(valeur == null || valeur == "")
				response.sendRedirect("GestionQuestionnaires?page=1");
			else
				this.getServletContext().getRequestDispatcher( AFFICHAGE_QUESTIONNAIRE ).forward( request, response );

			// Si on recherche une question
		}else if(type.contains("question")){
			if ((questionDAO.trouver_ByText(valeur, ID) != null)){
				List<Question> LQuestionToAffiche = new ArrayList<Question>();
				LQuestionToAffiche.add(questionDAO.trouver_ByText(valeur, ID));

				/* on la passe en attribut à la requete */
				request.setAttribute( ATT_LIST_Questions, LQuestionToAffiche );
				session.setAttribute( PAGEMAX,  Math.ceil((double)LQuestionToAffiche.size() / (double)nb_quest_affich));

			}else
				session.setAttribute( PAGEMAX, 1);
			
			/* Actualisation de la page */
			if(valeur == null || valeur == "")
				response.sendRedirect("GestionQuestions?page=1&QuestionnaireID="+ID);
			else
				this.getServletContext().getRequestDispatcher( AFFICHAGE_QUESTION ).forward( request, response );

			// Si on recherche une réponse
		}else if (type.contains("reponse")){
			if ((reponseDAO.trouver_ByText(valeur, ID) != null)){
				List<Reponse> LreponseToAffiche = new ArrayList<Reponse>();
				LreponseToAffiche.add(reponseDAO.trouver_ByText(valeur, ID));

				/* on la passe en attribut à la requete */
				request.setAttribute( ATT_LIST_Reponses, LreponseToAffiche );
				session.setAttribute( PAGEMAX,  Math.ceil((double)LreponseToAffiche.size() / (double)nb_quest_affich));

			}else
				session.setAttribute( PAGEMAX, 1);
			
			/* Actualisation de la page */
			if(valeur == null || valeur == "")
				response.sendRedirect("GestionReponses?page=1&QuestionID="+ID);
			else
				this.getServletContext().getRequestDispatcher( AFFICHAGE_REPONSE ).forward( request, response );
		}else if (type.contains("user")){
			if (userDAO.trouver_byMail(valeur) != null){
				List<Utilisateur> LuserToAffiche = new ArrayList<Utilisateur>();
				LuserToAffiche.add(userDAO.trouver_byMail(valeur));

				/* on la passe en attribut à la requete */
				request.setAttribute( ATT_LISTUSERS, LuserToAffiche );
				session.setAttribute( PAGEMAX,  Math.ceil((double)LuserToAffiche.size() / (double)nb_quest_affich));

			}else
				session.setAttribute( PAGEMAX, 1);
			
			/* Actualisation de la page */
			if(valeur == null || valeur == "")
				response.sendRedirect("GestionUser?page=1");
			else
				this.getServletContext().getRequestDispatcher( AFFICHAGE_USER ).forward( request, response );
		}
	}
}
