package it.presentation;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import it.business.RubricaEJB;
import it.exception.ContattoNonTrovatoException;

/**
 * Servlet implementation class ViewByCognomeServlet
 */
@WebServlet("/viewbycognome")
public class ViewByCognomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewByCognomeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpS	ervletRequest request, HttpServletResponse response)
	 */
    
    @EJB
    RubricaEJB rj;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       String cognome= request.getParameter("cognome");
		
		List<Object[]> contattinumeri;
		try {
			contattinumeri = rj.getContattoBySurname(cognome);
			response.getWriter()
			.append("<h1>")
			.append("LISTA DEI CONTATTI E DEI NUMERI TROVATI")
			.append("</h1>")
			.append("<div>")
			;
			
			
			for(Object[] o: contattinumeri) {
			
				response.getWriter()
				.append("<div>")
				.append("Nome : ").append((String) o[0])
				.append(" Cognome : ").append((String) o[1])
				.append(" Email : ").append((String)o[2])
				.append(" Numero: ").append((String) o[3])
				.append("</div>")
				;
			}
			
		} catch (ContattoNonTrovatoException e) {
			PrintWriter out = response.getWriter();
			out.println("contatto inesistente!");
		}
		
		
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
