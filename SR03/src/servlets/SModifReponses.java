package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Reponse;
import beans.Utilisateur;
import dao.DAOException;
import dao.DAOFactory;
import dao.ReponseDAO;
import forms.ReponseForm;

public class SModifReponses extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String AFFICHAGE          = "/WEB-INF/ModifReponses.jsp";
	public static final String AFFICHAGEGestion   = "/WEB-INF/GestionReponses.jsp";
	public static final String ATT_SESSION_USER = "sessionUtilisateur";
	public static final String ACCESSREFUSED = "/RefuseAccess.jsp";
	public static final String CONF_DAO_FACTORY = "daofactory";

	public static final String CHAMP_IDQuestion_A = "IdQuestion";
	public static final String CHAMP_ID_M  = "IdM";
	public static final String CHAMP_Text_M  = "TextM";
	public static final String CHAMP_STATUT_M  = "statutM";
	public static final String CHAMP_ESTVALIDE_M  = "estValideM";
	public static final String CHAMP_ordreM  = "ordreM";

	public static final String PARAM_Question= "QuestionID";
	public static final String PARAM_QUEST_TO_MODIF   = "ReponseID_to_modif";

	public static final String ATT_Question= "QuestionID";
	public static final String ATT_LIST_Reponses = "L_Reponses";
	public static final String ATT_QUEST_TO_MODIF   = "rep_to_modif";
	public static final String ATT_ERREURS  = "erreurs";
	public static final String ATT_RESULTAT = "resultat";

	private ReponseDAO     ReponseDAO;
	List<Reponse> L_Reponses;
	ReponseForm questForm = new ReponseForm();

	public void init() throws ServletException {
		/* Récupération d'une instance de notre DAO Utilisateur */
		this.ReponseDAO = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getReponseDao();
	}

	public SModifReponses() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Récupération de la session*/
		HttpSession session = request.getSession();
		Utilisateur user_co = (Utilisateur) session.getAttribute( ATT_SESSION_USER );

		// Si l'utilisateur connecté est bien un admin
		if (user_co != null && user_co.getAdmin() == true){

			/* Récupération des paramètres (si modification demandé) */
			String QuestIDToModifS = getValeurParametre( request, PARAM_QUEST_TO_MODIF);
			String QuestionIDS = getValeurParametre( request, PARAM_Question);
			int QuestionID = Integer.parseInt(QuestionIDS);

			// Si paramètre de modification reçu :
			if (QuestIDToModifS != null && QuestionIDS != null){
				int QuestIDToModif = Integer.parseInt(QuestIDToModifS);
				Reponse quest_to_modif = new Reponse();
				quest_to_modif = ReponseDAO.trouver_ByID(QuestIDToModif);

				if (quest_to_modif != null){
					try{
						/* Alors on envoie les parametres au formulaire*/
						request.setAttribute( ATT_QUEST_TO_MODIF, quest_to_modif );
					} catch ( DAOException e ) {
						e.printStackTrace();
					}
				}
			}

			request.setAttribute( ATT_Question, QuestionID );

			/* Affichage de la page du formulaire de modification */
			this.getServletContext().getRequestDispatcher( AFFICHAGE ).forward( request, response );
		}else
			this.getServletContext().getRequestDispatcher( ACCESSREFUSED ).forward( request, response );
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resultat;
		Map<String, String> erreurs = new HashMap<String, String>();
		String Text = request.getParameter( CHAMP_Text_M );
		String statut = request.getParameter( CHAMP_STATUT_M );
		String id = request.getParameter( CHAMP_ID_M );
		String IdQuestion = request.getParameter( CHAMP_IDQuestion_A );
		String ordreM = request.getParameter( CHAMP_ordreM);
		String estValide = request.getParameter( CHAMP_ESTVALIDE_M );

		/* Si formulaire de modification envoyé */

		/* Validation du champ Text. */
		try {
			questForm.validationText(Text);
			// A voir si on garde ... QuestDejaUsed(Text, Integer.parseInt(IdQuestion));
			ReponseCorrecteDejaUsed(Integer.parseInt(IdQuestion), estValide);
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

			Reponse quest_to_modif = ReponseDAO.trouver_ByID(Integer.parseInt(id));
			quest_to_modif.setText(Text);
			quest_to_modif.setStatut(statut);
			quest_to_modif.setEstValide(estValide);
			quest_to_modif.setOrdre(Integer.parseInt(ordreM));

			ReponseDAO.modifier(quest_to_modif);

		} else {
			resultat = "Échec de modification.";
		}

		/* Stockage du résultat et des messages d'erreur dans l'objet request */

		/* on récupère la liste des utilisateurs */
		L_Reponses = ReponseDAO.lister(Integer.parseInt(IdQuestion));

		/* on la passe en attribut à la requete */
		request.setAttribute( ATT_LIST_Reponses, L_Reponses );

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
		if(ReponseDAO.trouver_ByID(id) == null)
			throw new DAOException( "Aucun Reponse correspondant à cet ID" );
	}

	// Déclenche une erreur si Text deja utilisé
	private void QuestDejaUsed( String Text, long idQuestion ) throws DAOException{
		if(ReponseDAO.trouver_ByText(Text, idQuestion) != null)
			throw new DAOException( "Ce Text est déjà utilisé" );
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
