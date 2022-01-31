package it.presentation;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import it.business.RubricaEJB;

/**
 * Servlet implementation class ViewAllServlet
 */
@WebServlet("/viewall")
public class ViewAllServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewAllServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    @EJB
    RubricaEJB rj;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List <Object[]> contattinumeri = rj.getContattiNumeri();
		
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
	}

	
}
