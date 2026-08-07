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

}
