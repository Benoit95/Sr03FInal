package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String ADMIN          = "/Admin/startAdmin.jsp";
	
    public SAdmin() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			/* Affichage de la page de admin */
	        this.getServletContext().getRequestDispatcher( ADMIN ).forward( request, response );					
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
