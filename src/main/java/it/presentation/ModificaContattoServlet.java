package it.presentation;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import it.business.RubricaEJB;
import it.data.Contatto;
import it.data.NumTelefono;
import it.exception.ContattoNonTrovatoException;
import it.exception.NumeroEsistenteException;

/**
 * Servlet implementation class ViewByCognomeServlet
 */
@WebServlet("/modificacontatto")
public class ModificaContattoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ModificaContattoServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	@EJB
	RubricaEJB rj;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.valueOf(request.getParameter("id"));
		String campo1 = request.getParameter("numero1");
		String campo2 = request.getParameter("numero2");

		String numero1 = null;
		String numero2 = null;

		if (controlloCampo(campo1)) {
			numero1 = campo1;
		} else {
			PrintWriter out = response.getWriter();
			out.println("il campo deve contenere un numero di telefono valido!");
			return;
		}

		if (controlloCampo(campo2)) {
			numero2 = campo2;

		} else {
			PrintWriter out = response.getWriter();
			out.println("il campo deve contenere un numero di telefono valido!");
			return;
		}

		if (numero1.equals(numero2)) {
			PrintWriter out = response.getWriter();
			out.println("non puoi inserire due numeri uguali!");

			return;
		}

		try {
			rj.modificaContattoById(id, numero1, numero2);
			PrintWriter out = response.getWriter();
			out.println("contatto con id " + id + " modificato!");
		} catch (ContattoNonTrovatoException e) {
			PrintWriter out = response.getWriter();
			out.println("il contatto che vuoi modificare non esiste!");
		} catch (NumeroEsistenteException e) {
			PrintWriter out = response.getWriter();
			out.println("il numero è gia stato assegnato ad un altro contatto!");
		}

	}

	private boolean controlloCampo(String campo) {
		int counter = 0;
		for (char c : campo.toCharArray()) {
			if (counter == 0 && c == '+') {
				continue;
			}

			if (counter != 0 && (c == '/' || c == '-')) {
				continue;
			}

			if (!Character.isDigit(c)) {
				return false;

			}
			counter++;
		}

		return true;
	}

}
