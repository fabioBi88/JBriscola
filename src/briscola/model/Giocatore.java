package briscola.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Rappresenta il giocatore in forma astratta e ne implementa i metodi generici comuni
 */
public abstract class Giocatore {
	
	private String nickname;
	protected List<Carta> mano;
	private List<Carta> pozzo;
	
	/**
	 * Costruttore della classe
	 * 
	 * @param nickname la stringa che rappresenta il nickname del giocatore
	 */
	public Giocatore(String nickname) {
		
		this.nickname=nickname;
		mano= new ArrayList<>();
		pozzo= new ArrayList<>();
		
	}
	
	/**
	 * Restituisce il nickname 
	 * 
	 * @return una stringa che rappresenta il nickname del giocatore
	 */
	public String getNickname() {
		return nickname;
	}
	
	/**
	 * Restituisce la mano del giocatore
	 * 
	 * @return una lista di {@link Carta} che rappresenta le carte in mano del giocatore
	 */
	public List<Carta> getMano() {
		return this.mano; 
	}
	
	/**
	 * Permette di aggiungere una carta alla mano
	 * 
	 * @param c la {@link Carta} che deve essere aggiunta alla mano
	 */
	public void riceviCarta(Carta c) {
		
		if(mano.size()<3 && c!=null) {
			
			mano.add(c);
		}
		
	}
	/**
	 * Permette l'aggiunta di una lista di {@link Carta} al pozzo del giocatore
	 * 
	 * @param carteVinte la lista di {@link Carta} che rappresenta le carte vinte e aggiunte al pozzo
	 */
	public void aggiungiAPozzo(List<Carta> carteVinte) {
		
		pozzo.addAll(carteVinte);
				
	}
	/**
	 * Restituisce il totale dei punti del pozzo del giocatore
	 * 
	 * @return un intero che è il risultato della somma dei punti di ogni carta del pozzo del giocatore
	 */
	public int calcolaPunteggio() {
		
		return pozzo.stream().mapToInt(c->c.getPunti()).sum();
		
	}
	
	
	/**
	 * Restitituisce il pozzo del giocatore
	 * 
	 * @return una lista di {@link Carta} che rappresenta la lista di carte prese dal giocatore
	 */
	public List<Carta> getPozzo() {
		return pozzo;
	}
	
	@Override
	public String toString() {
		return nickname;
	}


	@Override
	public int hashCode() {
		return Objects.hash(nickname);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Giocatore other = (Giocatore) obj;
		return Objects.equals(nickname, other.nickname);
	}
	
	
	
	

}
