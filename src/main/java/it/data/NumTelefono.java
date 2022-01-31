package it.data;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="numtelefono")
public class NumTelefono implements Serializable{

	
	private static final long serialVersionUID = 1L;

	private String numTelefono;

	private Contatto contatto;
	
	@Id
	@Column(name="numtelefono")
	public String getNumTelefono() {
		return numTelefono;
	}

	public void setNumTelefono(String numTelefono) {
		this.numTelefono = numTelefono;
	}

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id")
	public Contatto getContatto() {
		return contatto;
	}

	public void setContatto(Contatto contatto) {
		this.contatto = contatto;
	}

	@Override
	public int hashCode() {
		return Objects.hash(numTelefono);
	}

	@Override
	public boolean equals(Object obj) {
		NumTelefono other = (NumTelefono) obj;
		if (this.getNumTelefono() != other.getNumTelefono()) {
			return false;
		}
	 return true;
		
	}
	
	
	
	
	
}
