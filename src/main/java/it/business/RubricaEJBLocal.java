package it.business;

import java.util.List;

import it.data.Contatto;
import it.exception.ContattoEsistenteException;
import it.exception.ContattoNonTrovatoException;
import it.exception.NumeroEsistenteException;
import jakarta.ejb.Local;

@Local
public interface RubricaEJBLocal {

	public void inserisciContatto(Contatto c) throws ContattoEsistenteException, NumeroEsistenteException;
	public List<Object[]> getContattiNumeri() ;
	public List<Object[]>  getContattoBySurname(String cognome) throws ContattoNonTrovatoException;
	public List<Object[]>  getContattoByNumber(String numero) throws ContattoNonTrovatoException;
	public void modificaContattoById(int id,String numero1,String numero2) throws ContattoNonTrovatoException, NumeroEsistenteException;
	public void deleteContatto(int id) throws ContattoNonTrovatoException;
	
	
}
