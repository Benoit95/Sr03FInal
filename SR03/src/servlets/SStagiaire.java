package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Utilisateur;


@WebServlet("/SStagiaire")
public class SStagiaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String ATT_SESSION_USER = "sessionUtilisateur";
	public static final String STAGIAIRE          = "/WEB-INF/startStagiaire.jsp";
	public static final String ACCESSREFUSED = "/RefuseAccess.jsp";
	
    public SStagiaire() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    Utilisateur user_co;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /* Récupération de la session depuis la requête */
        HttpSession session = request.getSession();
        user_co = (Utilisateur) session.getAttribute( ATT_SESSION_USER );
        
		if (user_co != null && user_co.getAdmin() == false){
			/* Affichage de la page de stagiaire */
	        this.getServletContext().getRequestDispatcher( STAGIAIRE ).forward( request, response );			
		}else
			this.getServletContext().getRequestDispatcher( ACCESSREFUSED ).forward( request, response );			

	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
