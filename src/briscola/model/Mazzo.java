package briscola.model;

import java.util.*;

/**
 * Rappresenta il mazzo di carte del gioco della Briscola.
 * Gestisce la creazione, il mescolamento e l'estrazione delle carte.
 */
public class Mazzo {
	
	private List<Carta> carte;
	
	/**
	 * Costruisce e inizializza un nuovo mazzo di carte completo,
	 * generando tutte le combinazioni possibili di {@link Seme} e {@link Valore},
	 * per poi mescolarle.
	 */
	public Mazzo() {
		
		carte= new ArrayList<>();
		
		for(Seme seme: Seme.values()) {
			for (Valore valore: Valore.values()) {
				
				carte.add(new Carta(seme, valore));
			}
		}
		mescola();
	}
	
	/**
	 * Mescola casualmente le carte attualmente presenti nel mazzo.
	 */
	private void mescola() {
		
		Random r= new Random();
		
		List<Carta> disponibili= new ArrayList<>(carte);
		
		List<Carta> mescolate= new ArrayList<>(carte.size());
		
		while(!disponibili.isEmpty()) {
			
			int indiceCasuale= r.nextInt(disponibili.size());
			
			mescolate.add(disponibili.remove(indiceCasuale));
			
		}
		
		carte= mescolate;
		
	}
	
	/**
	 * Estrae una carta dalla cima del mazzo.
	 * 
	 * @return la {@link Carta} pescata, oppure {@code null} se il mazzo è vuoto
	 */
	public Carta pesca() {
		
		if(!carte.isEmpty()) return carte.remove(carte.size()-1);
		
		return null;
		
	}
	
	/**
	 * Estrae l'ultima carta del mazzo per impostarla come briscola e la riposiziona
	 * in fondo al mazzo (all'indice 0).
	 * 
	 * @return la {@link Carta} scelta come briscola, oppure {@code null} se il mazzo è vuoto
	 */
	public Carta scegliBriscola() {
		
		if(carte.isEmpty()) return null;
		Carta ultima= carte.remove(carte.size()-1);
		carte.add(0, ultima);
		
		return ultima;
		
	}
	

	/**
	 * Verifica se il mazzo è privo di carte.
	 * 
	 * @return {@code true} se non ci sono più carte nel mazzo, {@code false} altrimenti
	 */
	public boolean isEmpty() {
		
		return carte.isEmpty();
	}
	
	/**
	 * Restituisce il numero di carte attualmente rimanenti nel mazzo.
	 * 
	 * @return il numero di carte ancora presenti
	 */
	public int getNumeroCarte() {
		
		return carte.size();
	}
	

}
