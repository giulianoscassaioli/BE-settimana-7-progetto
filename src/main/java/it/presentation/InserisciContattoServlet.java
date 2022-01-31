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

import it.business.RubricaEJB;
import it.data.Contatto;
import it.data.NumTelefono;
import it.exception.ContattoEsistenteException;
import it.exception.NumeriUgualiException;
import it.exception.NumeroEsistenteException;

/**
 * Servlet implementation class InserisciContattoServlet
 */
@WebServlet("/inseriscicontatto")
public class InserisciContattoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InserisciContattoServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	@EJB
	RubricaEJB re;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		doPost(request,response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String nome = request.getParameter("nome");
		String cognome = request.getParameter("cognome");
		String email = request.getParameter("email");

		String campo1 = request.getParameter("numero1");
		String campo2 = request.getParameter("numero2");

		String numero1 = null;
		String numero2 = null;

		NumTelefono num1 = new NumTelefono();
		NumTelefono num2 = new NumTelefono();

		if (controlloCampo(campo1)) {
			numero1 = campo1;
			num1.setNumTelefono(numero1);
		} else {
			PrintWriter out = response.getWriter();
			out.println("il campo deve contenere un numero di telefono valido!");
			return;
		}

		if (controlloCampo(campo2)) {
			numero2 = campo2;
			num2.setNumTelefono(numero2);

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
		
		if(numero2.equals("")) {
			
			Contatto c = new Contatto();

			c.setCognome(cognome);
			c.setEmail(email);
			c.setNome(nome);

			num1.setContatto(c);
			

			ArrayList<NumTelefono> numeri = new ArrayList<>();
			numeri.add(num1);

			c.setNumTelefoni(numeri);

			try {
				re.inserisciContatto(c);
				PrintWriter out = response.getWriter();
				out.println("contatto inserito correttamente!");
			} catch (ContattoEsistenteException e) {
				PrintWriter out = response.getWriter();
				out.println("contatto gia esistente!");
			} catch (NumeroEsistenteException e) {
				PrintWriter out = response.getWriter();
				out.println("il/i mumero/i di telefono inseriti son gia assegnati ad un altro utente!");
			}
		}

		else {

			Contatto c = new Contatto();

			c.setCognome(cognome);
			c.setEmail(email);
			c.setNome(nome);

			num1.setContatto(c);
			num2.setContatto(c);

			ArrayList<NumTelefono> numeri = new ArrayList<>();
			numeri.add(num2);
			numeri.add(num1);

			c.setNumTelefoni(numeri);

			try {
				re.inserisciContatto(c);
				PrintWriter out = response.getWriter();
				out.println("contatto inserito correttamente!");
			} catch (ContattoEsistenteException e) {
				PrintWriter out = response.getWriter();
				out.println("contatto gia esistente!");
			} catch (NumeroEsistenteException e) {
				PrintWriter out = response.getWriter();
				out.println("il/i mumero/i di telefono inseriti son gia assegnati ad un altro utente!");
			}
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
