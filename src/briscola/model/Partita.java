package briscola.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

@SuppressWarnings("deprecation")


public class Partita extends Observable {
	
	private List<Giocatore> giocatori;
	
	private List<Squadra> squadre;
	
	private Mazzo mazzo;
	
	private List<Carta> tavolo;
	
	private Carta briscola;
	
	private int turno;
	
	private Squadra squadraVincente;
	
	private HashMap<Carta, Giocatore> giocate= new HashMap<>();
	
	private Stato statoPartita;

	
	
	public enum Stato {
		NON_INIZIATA, 
		INIZIO_PARTITA,
		INIZIO_MANO,
		IN_CORSO,
		CARTA_GIOCATA, 
		FINE_MANO,
		FINE_PARTITA;
	}
	
	
	public Partita() {
		
		mazzo= new Mazzo();
		giocatori= new ArrayList<>();
		tavolo= new ArrayList<>();
		squadre=new ArrayList<>();
		statoPartita= Stato.NON_INIZIATA;		
	}
	
	public void preparaPartitaASquadre(Giocatore umano) {
	    // 1. Generiamo i 3 bot alleati e avversari
	    Giocatore compagnoBot = new GiocatoreIA("Bot-Compagno");
	    Giocatore avversarioBot1 = new GiocatoreIA("Bot-Avversario 1");
	    Giocatore avversarioBot2 = new GiocatoreIA("Bot-Avversario 2");
	    
	    // 2. Creiamo le due squadre (Umano + Compagno vs I due bot avversari)
	    Squadra sqUmana = new Squadra("Squadra Giocatore", umano, compagnoBot);
	    Squadra sqAvversaria = new Squadra("Squadra IA", avversarioBot1, avversarioBot2);
	    
	    squadre.add(sqUmana);
	    squadre.add(sqAvversaria);
	    
	    // 3. Distribuzione alternata automatica sulla sedia (Umano -> Avv1 -> Compagno -> Avv2)
	    giocatori.add(umano);
	    giocatori.add(avversarioBot1);
	    giocatori.add(compagnoBot);
	    giocatori.add(avversarioBot2);
	}
	
	
	public void cambiaStato(Stato nuovoStato) {
		
		this.statoPartita= nuovoStato;
		setChanged();
		notifyObservers(this.statoPartita);
		
	}
	
	public Stato getStatoCorrente() {
		return statoPartita;
	}
	
	public void inizializzaPartita() {
		
		mazzo = new Mazzo();
	   
		int distr= 3;
		
		while(distr>0) {
		
			for (Giocatore giocatore: giocatori) {
				
				giocatore.riceviCarta(mazzo.pesca());
			}
			
			distr-=1;
		}
		
		briscola= mazzo.scelgiBriscola();
		
		turno= 0;
		
		cambiaStato(Stato.INIZIO_PARTITA);
	}
	
	 
	public void giocaCarta(Giocatore giocatore, Carta carta) {
		
		if (!giocatore.equals(getGiocatoreCorrente())) {
            throw new IllegalStateException("Non è il turno di " + giocatore.getNickname());
        }

        // 2. Rimuoviamo la carta dalla mano del giocatore
        giocatore.getMano().remove(carta);

        // 3. Aggiungiamo la carta al tavolo
        tavolo.add(carta);
        
        giocate.put(carta, giocatore);
        
        if (tavolo.size() < 4) {
            turno = (turno + 1) % giocatori.size();
        }
        // 4. Notifichiamo la View del cambiamento
        cambiaStato(Stato.CARTA_GIOCATA);
       
	}
	
	public void avviaNuovaMano() {
	    // Se il gioco è finito, passiamo allo stato finale
	    if (isFinita()) {
	        cambiaStato(Stato.FINE_PARTITA);
	        return;
	    }

	    // Se ci sono ancora carte, segnaliamo che la nuova mano sta per cominciare
	    cambiaStato(Stato.INIZIO_MANO);
	}

	

	public void risolviMano() {
	    Carta cartaVincente = RegoleBriscola.determinaVincente(tavolo, briscola);
	    Giocatore giocatoreVincente = giocate.get(cartaVincente);

	    // Assegna le carte al pozzo del vincitore
	    giocatoreVincente.aggiungiAPozzo(new ArrayList<>(tavolo));
	    
	    // Pulizia per la mano successiva
	    tavolo.clear();
	    giocate.clear();

	    // Il vincitore sarà il primo a giocare al turno successivo
	    turno = giocatori.indexOf(giocatoreVincente);

	    // Pescata (se il mazzo non è vuoto)
	    if (!mazzo.isEmpty()) {
	        for (int i = 0; i < giocatori.size(); i++) {
	            int indicePescata = (turno + i) % giocatori.size();
	            giocatori.get(indicePescata).riceviCarta(mazzo.pesca());
	        }
	    }
	    
	    if (isFinita()) {
	        cambiaStato(Stato.FINE_PARTITA);
	        return;
	    }

	    cambiaStato(Stato.FINE_MANO);
	}
		
	
	
	
	public boolean isFinita() {
		
		if(giocatori == null || giocatori.isEmpty()) return false;
		
		return mazzo.isEmpty() && tavolo.isEmpty() && giocatori.get(0).getMano().isEmpty();
	
	}
	
	public boolean isTurnoUmano() {
        return this.turno == 0;
    }
	
	public int getTurno() {
		return turno;
	}
			
	public Squadra getVincitoreFinale() {
			
			//vincitoreFinale= giocatori.stream().max(Comparator.comparing(Giocatore::calcolaPunteggio)).orElse(null);
			
			squadraVincente= squadre.stream().max(Comparator.comparing(Squadra::getPuntggioSquadra)).orElse(null);
			
			return squadraVincente;		
	}
		

	public Carta getBriscola() {

		return briscola;
	}
	

	public List<Giocatore> getGiocatori() {
		return giocatori;
	}

	public List<Carta> getTavolo() {
		return tavolo;
	}
	
	public Giocatore getGiocatoreCorrente() {
		
		return giocatori.get(turno);
	}
	
	public List<Carta> getManoUmano(){
		
		if(giocatori == null || giocatori.isEmpty()) return new ArrayList<>();
		
		return giocatori.get(0).getMano();
	}
	
	public Mazzo getMazzo(){
		
		return mazzo;
	}
	
	public void resetPartita() {
		
		this.mazzo= new Mazzo();
	    // Svuota le mani dei giocatori
	    if (giocatori != null) {
	        for (Giocatore g : giocatori) {
	            g.getMano().clear();
	        }
	        giocatori.clear();
	    }
	    
	    // Svuota le collezioni
	    if (squadre != null) squadre.clear();
	    if (tavolo != null) tavolo.clear();
	    if (giocate != null) giocate.clear();
	    
	    // Resetta le variabili di stato
	    briscola = null;
	    turno = 0;
	    squadraVincente = null;
	    
	    // Notifica la View del reset
	    cambiaStato(Stato.NON_INIZIATA);
	}
	
	

	
	

}
