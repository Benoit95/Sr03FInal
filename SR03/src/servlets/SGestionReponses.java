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

import beans.Reponse;
import dao.DAOException;
import dao.DAOFactory;
import dao.ReponseDAO;
import forms.ReponseForm;


public class SGestionReponses extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String AFFICHAGE          = "/Admin/GestionReponses.jsp";
	public static final String CONF_DAO_FACTORY = "daofactory";

	public static final String CHAMP_IDQuestion_A = "IdQuestion";
	public static final String CHAMP_Text_A = "TextA";
	public static final String CHAMP_STATUT_A  = "statutA";
	public static final String CHAMP_ESTVALIDE_A  = "estValide";
	public static final String CHAMP_ORDRE  = "ordre";

	public static final String PARAM_Question= "QuestionID";
	public static final String PARAM_QUEST_TO_DELETE   = "ReponseID_to_delete";
	public static final String PARAM_QUEST_TO_MODIF   = "ReponseID_to_modif";

	public static final String ATT_Question= "QuestionID";
	public static final String ATT_LIST_Reponses = "L_Reponses";
	public static final String ATT_ERREURS  = "erreurs";
	public static final String ATT_RESULTAT = "resultat";
	
	public static final String R_TYPE = "type";
	public static final String R_ID = "ID";
	public static final String PAGE = "page";
	public static final String PAGEMAX = "pageMax";
	public static final String ATT_SESSION_NB_QUESTION_PAGE = "sessionQuestionPage";

	private ReponseDAO     ReponseDAO;
	List<Reponse> L_Reponses;
	ReponseForm questForm = new ReponseForm();

	public void init() throws ServletException {
		/* Récupération d'une instance de notre DAO Utilisateur */
		this.ReponseDAO = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getReponseDao();
	}

	public SGestionReponses() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Récupération de la session*/
		HttpSession session = request.getSession();

			String resultat ="";
			int pageI;
			int nb_quest_affich = (int) session.getAttribute(ATT_SESSION_NB_QUESTION_PAGE);

			/* Récupération des paramètres (si suppression d'une Reponse demandée) */
			String QuestIDToDeleteS = getValeurParametre( request, PARAM_QUEST_TO_DELETE);
			int QuestionID = Integer.parseInt(getValeurParametre( request, PARAM_Question));
			
			if (getValeurParametre( request, PAGE) == null)
				// On récupère la valeur de la page actuelle en session.
				pageI = (int) session.getAttribute(PAGE);
			else
				// On récupère la valeur de la page actuelle dans la requete
				pageI = Integer.parseInt(getValeurParametre( request, PAGE));

			// Si paramètre de suppression reçu :
			if (QuestIDToDeleteS != null){
				int QuestIDToDelete = Integer.parseInt(QuestIDToDeleteS);
				Reponse quest_to_delete = new Reponse();
				quest_to_delete = ReponseDAO.trouver_ByID(QuestIDToDelete);

				if (quest_to_delete != null){
					try{
						/* Alors suppression du client de la BDD */
						ReponseDAO.supprimer( quest_to_delete);	
					} catch ( DAOException e ) {
						resultat = e.getMessage();
					}
				}
				
				resultat = "Suppression de la Reponse effectuée avec succès";
			}

			/* on récupère la liste des Reponses */
			L_Reponses = ReponseDAO.lister(QuestionID);

			/* On prépare la liste des reponses à affichées (page) */
			List<Reponse> LReponseToAffiche = new ArrayList<Reponse>();
			for (int i = 1 ; i <= nb_quest_affich ; i++){
				if ((i+nb_quest_affich*(pageI-1) -1) < L_Reponses.size())
					LReponseToAffiche.add(i-1, L_Reponses.get(i+nb_quest_affich*(pageI-1) -1));
			}
			
			/* on la passe en attribut à la requete */
			request.setAttribute( ATT_Question, QuestionID );
			request.setAttribute( ATT_LIST_Reponses, LReponseToAffiche );
			request.setAttribute( ATT_RESULTAT, resultat );

			/* On passe aussi la valeur de la page et de pagemax */
			session.setAttribute( R_TYPE, "reponse"); // att session pour la recherche
			session.setAttribute( R_ID, QuestionID); // att session pour la recherche
			session.setAttribute(PAGE, pageI);
			session.setAttribute( PAGEMAX,  Math.ceil((double)L_Reponses.size() / (double)nb_quest_affich));
			
			/* Affichage de la page de d'affichage Reponse */
			this.getServletContext().getRequestDispatcher( AFFICHAGE ).forward( request, response );
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resultat;
		Map<String, String> erreurs = new HashMap<String, String>();
		String Text = request.getParameter( CHAMP_Text_A );
		String statut = request.getParameter( CHAMP_STATUT_A );
		String estValide = request.getParameter( CHAMP_ESTVALIDE_A );
		String IdQuestion = request.getParameter( CHAMP_IDQuestion_A );
		String ordre = request.getParameter( CHAMP_ORDRE);

		/* Récupération de la session*/
		HttpSession session = request.getSession();
		int page = (int) session.getAttribute(PAGE);
		int nb_quest_affich = (int) session.getAttribute(ATT_SESSION_NB_QUESTION_PAGE);

		/* Validation du champ Text. */
		try {
			questForm.validationText(Text);
			QuestDejaUsed(Text, Integer.parseInt(IdQuestion));
			ReponseCorrecteDejaUsed(Integer.parseInt(IdQuestion), estValide);
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

			Reponse quest_to_create = new Reponse();
			quest_to_create.setText(Text);
			quest_to_create.setId_question(Integer.parseInt(IdQuestion));
			quest_to_create.setStatut(statut);
			quest_to_create.setEstValide(estValide);
			quest_to_create.setOrdre(Integer.parseInt(ordre));

			ReponseDAO.creer(quest_to_create);				

		} else {
			resultat = "Échec de l'ajout.";
		}

		/* on récupère la liste des Reponses */
		L_Reponses = ReponseDAO.lister(Integer.parseInt(IdQuestion));

		/* On prépare la liste des reponses à affichées (page) */
		List<Reponse> LReponseToAffiche = new ArrayList<Reponse>();
		for (int i = 1 ; i <= nb_quest_affich ; i++){
			if ((i+nb_quest_affich*(page-1) -1) < L_Reponses.size())
				LReponseToAffiche.add(i-1, L_Reponses.get(i+nb_quest_affich*(page-1) -1));
		}
		
		/* on la passe en attribut à la requete */
		request.setAttribute( ATT_LIST_Reponses, LReponseToAffiche );
		request.setAttribute( ATT_Question, Integer.parseInt(IdQuestion) );
		request.setAttribute( ATT_ERREURS, erreurs );
		request.setAttribute( ATT_RESULTAT, resultat );

		session.setAttribute( PAGEMAX,  Math.ceil((double)L_Reponses.size() / (double)nb_quest_affich));
		
		/* Actualisation de la page */
		this.getServletContext().getRequestDispatcher( AFFICHAGE ).forward( request, response );
	}

	// Déclenche une erreur si Text deja utilisé
	private void QuestDejaUsed( String Text , long IDQuestion ) throws DAOException{
		if(ReponseDAO.trouver_ByText(Text, IDQuestion) != null)
			throw new DAOException( "Ce Text de réponse est déjà utilisé pour cette question." );
	}

	// Déclenche une erreur si une réponse valide est deja définie pour la question
	private void ReponseCorrecteDejaUsed( long IDQuestion , String estValide ) throws DAOException{
		if(estValide.contentEquals("oui"))
			if(ReponseDAO.trouverRepCorrecte(IDQuestion) != null)
				throw new DAOException( "Une réponse correcte a déjà été donnée pour cette question." );
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
