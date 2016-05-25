package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import beans.Questionnaire;
import dao.DAOException;
import dao.DAOFactory;
import dao.QuestionnaireDAO;
import forms.QuestionnaireForm;

public class SModifQuestionnaires extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String AFFICHAGE          = "/Admin/ModifQuestionnaires.jsp";
	public static final String AFFICHAGEGestion          = "GestionQuestionnaires";
	public static final String CONF_DAO_FACTORY = "daofactory";

	public static final String CHAMP_ID_M  = "IdM";
	public static final String CHAMP_SUJET_M  = "sujetM";
	public static final String CHAMP_STATUT_M  = "statutM";

	public static final String PARAM_QUEST_TO_DELETE   = "QuestionnaireID_to_delete";
	public static final String PARAM_QUEST_TO_MODIF   = "QuestionnaireID_to_modif";

	public static final String ATT_LIST_Questionnaires = "L_questionnaires";
	public static final String ATT_QUEST_TO_MODIF   = "quest_to_modif";

	private QuestionnaireDAO     questionnaireDAO;
	List<Questionnaire> L_questionnaires;
	QuestionnaireForm questForm = new QuestionnaireForm();

	public void init() throws ServletException {
		/* Récupération d'une instance de notre DAO Utilisateur */
		this.questionnaireDAO = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getQuestionnaireDao();
	}

	public SModifQuestionnaires() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

			/* Récupération des paramètres (si suppression/modification demandé) */
			String QuestIDToModifS = getValeurParametre( request, PARAM_QUEST_TO_MODIF);

			// Si paramètre de modification reçu :
			if (QuestIDToModifS != null){
				int QuestIDToModif = Integer.parseInt(QuestIDToModifS);
				Questionnaire quest_to_modif = new Questionnaire();
				quest_to_modif = questionnaireDAO.trouver_ByID(QuestIDToModif);

				if (quest_to_modif != null){
					try{
						/* Alors on envoie les parametres au formulaire*/
						request.setAttribute( ATT_QUEST_TO_MODIF, quest_to_modif );
					} catch ( DAOException e ) {
						e.printStackTrace();
					}
				}
			}

			/* Affichage de la page du formulaire de modification */
			this.getServletContext().getRequestDispatcher( AFFICHAGE ).forward( request, response );
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resultat;
		Map<String, String> erreurs = new HashMap<String, String>();
		String sujet;
		String statut;
		String id ="";

		/* Si formulaire de modification envoyé */
		id = request.getParameter( CHAMP_ID_M );
		sujet = request.getParameter( CHAMP_SUJET_M );
		statut = request.getParameter( CHAMP_STATUT_M );

		/* Validation du champ sujet. */
		try {
			questForm.validationSujet(sujet);
			QuestDejaUsed(sujet);
		} catch ( Exception e ) {
			erreurs.put( CHAMP_SUJET_M, e.getMessage() );
		}

		try {
			ValidationId( Integer.parseInt(id));
		} catch ( Exception e ) {
			erreurs.put( CHAMP_ID_M, e.getMessage() );
		}


		/* Initialisation du résultat global de la validation. */
		if ( erreurs.isEmpty() ) {
			resultat = "Succès de la modification.";

			Questionnaire quest_to_modif = questionnaireDAO.trouver_ByID(Integer.parseInt(id));
			quest_to_modif.setSujet(sujet);
			quest_to_modif.setStatut(statut);

			questionnaireDAO.modifier(quest_to_modif);

		} else {
			resultat = "Échec de modification.";
		}
		
		/* Actualisation de la page */
		if ( erreurs.isEmpty() )
			response.sendRedirect(AFFICHAGEGestion);
		else
			this.getServletContext().getRequestDispatcher( AFFICHAGE ).forward( request, response );
	}

	// Retourne vraie si le mail est deja utilisé false sinon
	private void ValidationId( long id ) throws DAOException{
		if(questionnaireDAO.trouver_ByID(id) == null)
			throw new DAOException( "Aucun questionnaire correspondant à cet ID" );
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
