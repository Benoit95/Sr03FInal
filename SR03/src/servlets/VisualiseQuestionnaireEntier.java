package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Question;
import beans.Questionnaire;
import dao.DAOException;
import dao.DAOFactory;
import dao.QuestionDAO;
import dao.QuestionnaireDAO;
import dao.ReponseDAO;

public class VisualiseQuestionnaireEntier extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String AFFICHAGE          = "/WEB-INF/VisualiseQuestionnaireEntier.jsp";
	public static final String CONF_DAO_FACTORY = "daofactory";
	public static final String PARAM_QUESTIONNAIRE = "IDquestionnaire";
	public static final String ATT_QUESTIONNAIRE = "questionnaire";
	
	
	private QuestionnaireDAO     questionnaireDAO;
	private QuestionDAO     questionDAO;
	private ReponseDAO     reponseDAO;
	private Questionnaire questionnaire;	
	
    public VisualiseQuestionnaireEntier() {
        super();
    }

	public void init() throws ServletException {
		/* Récupération d'une instance de nos DAO utiles */
		this.questionnaireDAO = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getQuestionnaireDao();
		this.questionDAO = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getQuestionDao();
		this.reponseDAO = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getReponseDao();
	}    
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Récupération du questionnaire a visualiser
		String questionnaireIDS = getValeurParametre( request, PARAM_QUESTIONNAIRE);
		if (questionnaireIDS != null){
			int questionnaireID = Integer.parseInt(questionnaireIDS);
			questionnaire = questionnaireDAO.trouver_ByID(questionnaireID);
			
			// Récupération des questions de ce questionnaires.
			List<Question> Lquestion = new ArrayList<Question>();
			if (questionDAO.lister(questionnaireID) != null)
				Lquestion.addAll(questionDAO.lister(questionnaireID));
				
			
			// Pour chacune des questions , on récupère les réponses associées.
			for (int i = 0 ; i < Lquestion.size() ; i++){
				Lquestion.get(i).setLReponses(reponseDAO.lister(Lquestion.get(i).getId()));
			}
			
			// On rajoute les Questions/Reponse dans le questionnaire.
			questionnaire.setLQuestions(Lquestion);
			request.setAttribute( ATT_QUESTIONNAIRE, questionnaire );			
		}

		
		/* Affichage du questionnaire (jsp) */
		this.getServletContext().getRequestDispatcher( AFFICHAGE ).forward( request, response );	
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
