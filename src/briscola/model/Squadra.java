package briscola.model;

import java.util.ArrayList;
import java.util.List;

public class Squadra {
	
	private String nome;
	private Giocatore giocatoreUno;
	private Giocatore giocatoreDue;
	private List<Giocatore> giocatori;
	
	public Squadra(String nome, Giocatore giocatoreUno, Giocatore giocatoreDue) {
		
		this.nome=nome;
		this.giocatoreUno= giocatoreUno;
		this.giocatoreDue= giocatoreDue;
		this.giocatori= new ArrayList<>(List.of(giocatoreUno, giocatoreDue));
	}
	
	public String toString() {
		
		return nome+ " - "+ giocatoreUno + " - "+giocatoreDue; 
	}
	
	public int getPuntggioSquadra() {
		
		return giocatoreUno.calcolaPunteggio()+giocatoreDue.calcolaPunteggio();
		
	}
	
	public boolean contieneGiocatore(Giocatore g) {
		
		return g.equals(giocatoreUno) || g.equals(giocatoreDue);
	}
	
	public List<Giocatore> getGiocatori(){
		
		return giocatori;
	}
	
	/**
	 * metodo per verificare se la squadra ha fatto una presa, permette alla view di visualizzare il pozzo della squadra anche se il totale della presa è zero
	 * @return un boolean che indica le prese della squadra
	 */

	public boolean haFattoPrese() {
	    for (Giocatore g : giocatori) { 
	        if (g.getPozzo() != null && !g.getPozzo().isEmpty()) { 
	            return true; // Almeno un giocatore ha preso delle carte
	        }
	    }
	    return false;
	}
	

}
