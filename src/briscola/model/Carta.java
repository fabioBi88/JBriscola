package briscola.model;

import java.util.Objects;

/**
 * Classe che rappresenta una carta da gioco per la Briscola.
 */
public class Carta {
	
	private Valore valore;
	private Seme seme;
	
	/**
	 * Costruttore della classe.
	 * 
	 * @param seme l'enum che definisce il seme della carta
	 * @param valore l'enum che definisce il valore della carta
	 */
	public Carta(Seme seme, Valore valore) {
		
		this.valore=valore;
		this.seme=seme;
		
	}
	
	/**
	 * Restituisce il valore della carta.
	 * 
	 * @return l'enum che rappresenta il valore della carta
	 */
	public Valore getValore() {
		return valore;
	}
	
	/**
	 * * Restituisce il seme della carta.
	 * 
	 * @return l'enum che rappresenta il seme della carta
	 */
	public Seme getSeme() {
		return seme;
	}
	
	/**
	 * Restituisce i punti associati alla carta.
	 * 
	 * @return un intero che rappresenta il punteggio della carta
	 */
	public int getPunti() {
		return valore.getPunti();
	}
	
	/**
	 * Restituisce la forza di presa della carta.
	 * 
	 * @return la forza della carta, un intero da 1 da 10, non considera il concetto di briscola
	 */
	public int getForza() {
		return valore.getForza();
	}
	
	/**
	 * Restituisce la rappresentazione testuale della carta.
	 * 
	 * @return una stringa che rapprresenta la carta
	 */
	public String toString() {
		
		return seme.toString().toLowerCase()+valore.getCifra();
	}
	
	
	public int hashCode() {
		return Objects.hash(seme, valore);
	}
	
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Carta other = (Carta) obj;
		return seme == other.seme && valore == other.valore;
	}
	
	
}
