package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import beans.Utilisateur;


@WebServlet("/SStagiaire")
public class SStagiaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String STAGIAIRE          = "/Stagiaire/startStagiaire.jsp";
	
    public SStagiaire() {
        super();
    }
    
    Utilisateur user_co;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			/* Affichage de la page de stagiaire */
	        this.getServletContext().getRequestDispatcher( STAGIAIRE ).forward( request, response );						

	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
