
package it.business;
//aggiunto commento chiarificatore
import java.util.ArrayList;
import java.util.List;

import it.data.Contatto;
import it.data.NumTelefono;
import it.exception.ContattoEsistenteException;
import it.exception.ContattoNonTrovatoException;
import it.exception.NumeroEsistenteException;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

/**
 * Session Bean implementation class RubricaEJB
 */
@Stateless
@LocalBean
public class RubricaEJB implements RubricaEJBLocal {

	private static final String SELECT_NUMERI_E_CONTATTO = "SELECT c.nome,c.cognome,c.email,n.numTelefono FROM Contatto c JOIN c.numTelefoni n";

	@PersistenceContext(unitName = "rubricaPS")
	EntityManager em;

	public RubricaEJB() {
		// TODO Auto-generated constructor stub
	}

	// il metodo inserisci il contatto solo se l'utente non esiste gia nel db
	public void inserisciContatto(Contatto c) throws ContattoEsistenteException, NumeroEsistenteException {

		if (getName(c.getNome()) && getSurname(c.getCognome())) {
			throw new ContattoEsistenteException(
					"il contatto: " + c.getNome() + " " + c.getCognome() + " è gia esistente nel db!");
		}

		else if (controlloNumero(c.getNumTelefoni())) {

			throw new NumeroEsistenteException(
					"il/i mumero/i di telefono inseriti son gia assegnati ad un altro utente!");
		}

		else {
			em.persist(c);
			em.flush();

		}

	}

	//metodo per visualizzare i contatti con i propri numeri associati
	@SuppressWarnings("unchecked")
	public List<Object[]> getContattiNumeri() {

		Query q = em.createQuery(SELECT_NUMERI_E_CONTATTO);

		return q.getResultList();

	}

	//metodo per trovare il contatto tramite congnome
	//se il contatto ricercato non esiste lancio un eccezione
	public List<Object[]> getContattoBySurname(String cognome) throws ContattoNonTrovatoException {
		Query q = em.createQuery(
				"SELECT c.nome,c.cognome,c.email,n.numTelefono FROM Contatto c JOIN c.numTelefoni n WHERE UPPER(c.cognome) LIKE UPPER (:cognome)");

		q.setParameter("cognome", "%" + cognome + "%");
		@SuppressWarnings("unchecked")
		List<Object[]> listacontatti = q.getResultList();
		if(listacontatti.isEmpty()) {
			throw new ContattoNonTrovatoException("il contatto non è stato trovato!");
		}

		return listacontatti;
	}

	//metodo per trovare il contatto tramite numero
	//se il numero ricercato non esiste lancio un eccezione
	public List<Object[]> getContattoByNumber(String numero) throws ContattoNonTrovatoException {
		Query q = em.createQuery(
				"SELECT c.nome,c.cognome,c.email,n.numTelefono FROM Contatto c JOIN c.numTelefoni n WHERE n.numTelefono = :numero");

		q.setParameter("numero", numero);
		@SuppressWarnings("unchecked")
		List<Object[]> listacontatti = q.getResultList();
		if(listacontatti.isEmpty()) {
			throw new ContattoNonTrovatoException("il contatto non è stato trovato!");
		}
		return listacontatti;
	}

	// il metodo modifica i numeri di un contatto solo se il contatto è realmente
	// esistente sul db
	//gestisco tre casistiche possibili
	public void modificaContattoById(int id, String numero1, String numero2)
			throws ContattoNonTrovatoException, NumeroEsistenteException {

		Contatto cont = em.find(Contatto.class, id);
		NumTelefono num1 = new NumTelefono();
		NumTelefono num2 = new NumTelefono();
		List<NumTelefono> numeri = new ArrayList<>();
		List<NumTelefono> numeri2 = new ArrayList<>();

		//se il contatto non esiste lancio un eccezione
		if (cont == null) {

			throw new ContattoNonTrovatoException("il contatto che vuoi modificare non esiste!");
		}

		numeri2 = getTelefoni(cont);

		//casistica in qui l'utente inserisce solo un numero nella form
		if (numero2.equals("")) {
			num1.setNumTelefono(numero1);
			num1.setContatto(cont);
			numeri.add(num1);

		
			if (controlloNumero(numeri)) {

				throw new NumeroEsistenteException("il numero è gia stato assegnato ad un altro contatto!");
			}

			updateTelefono(numero1, numeri2.get(0).getNumTelefono());
			

		}
		
		//casistica in cui il contatto da modificare ha solo un numero gia assegnato ma l' utente ne manda
		//due da modificare tramite la form
		else if(cont.getNumTelefoni().size()==1 && !numero1.equals("") && !numero2.equals("")) {
			
			num1.setNumTelefono(numero1);
			num2.setNumTelefono(numero2);
			num1.setContatto(cont);
			num2.setContatto(cont);
			numeri.add(num2);
			numeri.add(num1);

			if (controlloNumero(numeri)) {

				throw new NumeroEsistenteException("il numero è gia stato assegnato ad un altro contatto!");
			}

			deleteNumero(cont.getNumTelefoni().get(0).getNumTelefono());

			cont.setNumTelefoni(numeri);

			em.merge(cont);
			em.flush();

			
		}

		//caso in qui il contatto da modificare abbia due numeri gia assegnati è l'utente li voglia modificare
		//tutti e due
		else {

			num1.setNumTelefono(numero1);
			num2.setNumTelefono(numero2);
			num1.setContatto(cont);
			num2.setContatto(cont);
			numeri.add(num2);
			numeri.add(num1);

			if (controlloNumero(numeri)) {

				throw new NumeroEsistenteException("il numero è gia stato assegnato ad un altro contatto!");
			}

			deleteNumeri(id);

			cont.setNumTelefoni(numeri);

			em.merge(cont);
			em.flush();
		}

	}

	//metodo per cancellare un contatto
	//se il contatto non esiste lancio un eccezzione
	public void deleteContatto(int id) throws ContattoNonTrovatoException {

		Contatto cont = em.find(Contatto.class, id);

		if (cont != null) {
			em.remove(em.find(Contatto.class, id));
			em.flush();

		}

		else {

			throw new ContattoNonTrovatoException("non è stato possibile eliminare il contatto perche non esiste!");

		}
	}

	
	// metodi ausiliari

	public boolean getSurname(String cognome) {
		boolean success = true;
		Query q = em.createQuery("SELECT c FROM Contatto c WHERE UPPER (c.cognome) like UPPER (:cognome)");

		q.setParameter("cognome", cognome);
		@SuppressWarnings("unchecked")
		List<Contatto> controvati = q.getResultList();
		if (controvati.isEmpty()) {
			success = false;
		}

		return success;

	}

	public boolean getName(String nome) {
		boolean success = true;
		Query q = em.createQuery("SELECT c FROM Contatto c WHERE UPPER(c.nome) like UPPER(:nome)");

		q.setParameter("nome", nome);
		@SuppressWarnings("unchecked")
		List<Contatto> controvati = q.getResultList();
		if (controvati.isEmpty()) {
			success = false;
		}

		return success;

	}

	public void deleteNumeri(int id) {

		Contatto cont = em.find(Contatto.class, id);

		List<NumTelefono> numeridaeliminare = cont.getNumTelefoni();

		for (NumTelefono numTelefono : numeridaeliminare) {

			deleteNumero(numTelefono.getNumTelefono());

		}

	}

	public List<NumTelefono> getTelefoni(Contatto cont) {

		Query q = em.createNativeQuery("SELECT * FROM numtelefono WHERE id = " + cont.getId() + ";", NumTelefono.class);
		@SuppressWarnings("unchecked")
		List<NumTelefono> numeri = q.getResultList();

		return numeri;

	}

	public void deleteNumero(String numero) {
		em.remove(em.find(NumTelefono.class, numero));

	}

	public boolean controlloNumero(List<NumTelefono> n) {
		boolean success = true;
		Query q = em
				.createQuery("SELECT n from NumTelefono n WHERE n.numTelefono= :numero1 or n.numTelefono= :numero2");
		if (n.size() != 1) {
			q.setParameter("numero1", n.get(0).getNumTelefono());
			q.setParameter("numero2", n.get(1).getNumTelefono());
		} else {
			q.setParameter("numero1", n.get(0).getNumTelefono());
			q.setParameter("numero2", "0");
		}
		@SuppressWarnings("unchecked")
		List<NumTelefono> numtrovati = q.getResultList();
		if (numtrovati.isEmpty()) {
			success = false;
		}

		return success;

	}

	public void updateTelefono(String newnum, String numero) {
		Query q = em.createNativeQuery("update numtelefono set numtelefono = ? " + "where numtelefono = ? ");
		q.setParameter("1", newnum);
		q.setParameter("2", numero);

		int righeAggiornate = q.executeUpdate();
        em.flush();
		System.out.println("Numero righe aggiornate " + righeAggiornate);

	}

}
