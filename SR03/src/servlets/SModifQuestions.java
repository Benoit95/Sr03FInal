package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import beans.Question;
import dao.DAOException;
import dao.DAOFactory;
import dao.QuestionDAO;
import forms.QuestionForm;

public class SModifQuestions extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String AFFICHAGE          = "/Admin/ModifQuestions.jsp";
	public static final String AFFICHAGEGestion   = "/Admin/GestionQuestions.jsp";
	public static final String CONF_DAO_FACTORY = "daofactory";

	public static final String CHAMP_IDQUESTIONNAIRE_A = "IdQuestionnaire";
	public static final String CHAMP_ID_M  = "IdM";
	public static final String CHAMP_Text_M  = "TextM";
	public static final String CHAMP_STATUT_M  = "statutM";
	public static final String CHAMP_ordreM  = "ordreM";

	public static final String PARAM_QUESTIONNAIRE= "QuestionnaireID";
	public static final String PARAM_QUEST_TO_MODIF   = "QuestionID_to_modif";

	public static final String ATT_QUESTIONNAIRE= "QuestionnaireID";
	public static final String ATT_LIST_Questions = "L_Questions";
	public static final String ATT_QUEST_TO_MODIF   = "quest_to_modif";
	public static final String ATT_ERREURS  = "erreurs";
	public static final String ATT_RESULTAT = "resultat";

	private QuestionDAO     QuestionDAO;
	List<Question> L_Questions;
	QuestionForm questForm = new QuestionForm();

	public void init() throws ServletException {
		/* Récupération d'une instance de notre DAO Utilisateur */
		this.QuestionDAO = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getQuestionDao();
	}

	public SModifQuestions() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

			/* Récupération des paramètres (si modification demandé) */
			String QuestIDToModifS = getValeurParametre( request, PARAM_QUEST_TO_MODIF);
			String QuestionnaireIDS = getValeurParametre( request, PARAM_QUESTIONNAIRE);
			int QuestionnaireID = Integer.parseInt(QuestionnaireIDS);

			// Si paramètre de modification reçu :
			if (QuestIDToModifS != null && QuestionnaireIDS != null){
				int QuestIDToModif = Integer.parseInt(QuestIDToModifS);
				Question quest_to_modif = new Question();
				quest_to_modif = QuestionDAO.trouver_ByID(QuestIDToModif);

				if (quest_to_modif != null){
					try{
						/* Alors on envoie les parametres au formulaire*/
						request.setAttribute( ATT_QUEST_TO_MODIF, quest_to_modif );
					} catch ( DAOException e ) {
						e.printStackTrace();
					}
				}
			}

			request.setAttribute( ATT_QUESTIONNAIRE, QuestionnaireID );

			/* Affichage de la page du formulaire de modification */
			this.getServletContext().getRequestDispatcher( AFFICHAGE ).forward( request, response );	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resultat;
		Map<String, String> erreurs = new HashMap<String, String>();
		String Text = request.getParameter( CHAMP_Text_M );
		String statut = request.getParameter( CHAMP_STATUT_M );
		String id = request.getParameter( CHAMP_ID_M );;
		String IdQuestionnaire = request.getParameter( CHAMP_IDQUESTIONNAIRE_A );
		String ordreM = request.getParameter( CHAMP_ordreM);

		/* Si formulaire de modification envoyé */

		/* Validation du champ Text. */
		try {
			questForm.validationText(Text);
			// A voir si on garde ... QuestDejaUsed(Text, Integer.parseInt(IdQuestionnaire));
		} catch ( Exception e ) {
			erreurs.put( CHAMP_Text_M, e.getMessage() );
		}

		try {
			ValidationId( Integer.parseInt(id));
		} catch ( Exception e ) {
			erreurs.put( CHAMP_ID_M, e.getMessage() );
		}

		/* Validation du champ ordreM. */
		try {
			questForm.validationOrdre(ordreM);
		} catch ( Exception e ) {
			erreurs.put( CHAMP_ordreM, e.getMessage() );
		}

		/* Initialisation du résultat global de la validation. */
		if ( erreurs.isEmpty() ) {
			resultat = "Succès de la modification.";

			Question quest_to_modif = QuestionDAO.trouver_ByID(Integer.parseInt(id));
			quest_to_modif.setText(Text);
			quest_to_modif.setStatut(statut);
			quest_to_modif.setOrdre(Integer.parseInt(ordreM));

			QuestionDAO.modifier(quest_to_modif);

		} else {
			resultat = "Échec de modification.";
		}

		/* Stockage du résultat et des messages d'erreur dans l'objet request */

		/* on récupère la liste des utilisateurs */
		L_Questions = QuestionDAO.lister(Integer.parseInt(IdQuestionnaire));

		/* on la passe en attribut à la requete */
		request.setAttribute( ATT_LIST_Questions, L_Questions );

		request.setAttribute( ATT_ERREURS, erreurs );
		request.setAttribute( ATT_RESULTAT, resultat );

		/* Actualisation de la page */
		if ( erreurs.isEmpty() )
			this.getServletContext().getRequestDispatcher( AFFICHAGEGestion ).forward( request, response );
		else
			this.getServletContext().getRequestDispatcher( AFFICHAGE ).forward( request, response );
	}

	// Retourne vraie si le mail est deja utilisé false sinon
	private void ValidationId( long id ) throws DAOException{
		if(QuestionDAO.trouver_ByID(id) == null)
			throw new DAOException( "Aucun Question correspondant à cet ID" );
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
