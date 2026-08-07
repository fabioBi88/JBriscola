package briscola.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Giocatore {
	
	private String nickname;
	protected List<Carta> mano;
	private List<Carta> pozzo;
	
	public Giocatore(String nickname) {
		
		this.nickname=nickname;
		mano= new ArrayList<>();
		pozzo= new ArrayList<>();
		
	}

	public String getNickname() {
		return nickname;
	}

	public List<Carta> getMano() {
		return this.mano; 
	}

	public void riceviCarta(Carta c) {
		
		if(mano.size()<3 && c!=null) {
			
			mano.add(c);
		}
		
	}
	
	public void aggiungiAPozzo(List<Carta> carteVinte) {
		
		pozzo.addAll(carteVinte);
				
	}
	
	public int calcolaPunteggio() {
		
		return pozzo.stream().mapToInt(c->c.getPunti()).sum();
		
	}
	
	/**
	 * metodo astratto implementato in maniera differente da utente umano e utente IA
	 * @param tavolo le carte sul tavolo
	 * @param semeBriscola il seme della Briscola
	 * @return la carta da giocare
	 */
	
	@Override
	public String toString() {
		return nickname;
	}

	public List<Carta> getPozzo() {
		return pozzo;
	}
	
	

}
