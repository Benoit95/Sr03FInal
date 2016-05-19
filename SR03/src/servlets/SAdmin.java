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

public class SAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String ATT_SESSION_USER = "sessionUtilisateur";
	public static final String ADMIN          = "/WEB-INF/startAdmin.jsp";
	public static final String ACCESSREFUSED = "/RefuseAccess.jsp";
	
       
    public SAdmin() {
        super();
    }
    Utilisateur user_co;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /* Récupération de la session depuis la requête */
        HttpSession session = request.getSession();
        user_co = (Utilisateur) session.getAttribute( ATT_SESSION_USER );
        
		if (user_co != null && user_co.getAdmin() == true){
			/* Affichage de la page de admin */
	        this.getServletContext().getRequestDispatcher( ADMIN ).forward( request, response );			
		}else
			this.getServletContext().getRequestDispatcher( ACCESSREFUSED ).forward( request, response );			
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
