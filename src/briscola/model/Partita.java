package briscola.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

@SuppressWarnings("deprecation")

/**
 * Gestisce la logica principale, le regole e lo stato di una partita di Briscola a squadre.
 * Estende {@link Observable} per notificare l'interfaccia utente (View) di ogni variazione dello stato di gioco.
 */
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
	
	private Profilo profilo;

	
	/**
	 * Rappresenta i possibili stati in cui può trovarsi il ciclo di vita della partita.
	 */
	public enum Stato {
		NON_INIZIATA, 
		INIZIO_PARTITA,
		INIZIO_MANO,
		IN_CORSO,
		CARTA_GIOCATA, 
		FINE_MANO,
		FINE_PARTITA;
	}
	
	/**
	 * Costruisce e inizializza una nuova istanza di partita nello stato {@link Stato#NON_INIZIATA}.
	 */
	public Partita() {
		
		mazzo= new Mazzo();
		giocatori= new ArrayList<>();
		tavolo= new ArrayList<>();
		squadre=new ArrayList<>();
		statoPartita= Stato.NON_INIZIATA;		
		profilo= new Profilo("GiocatoreUmano");
	}
	
	/**
	 * Prepara una partita a 4 giocatori creando i tre bot IA e organizzandoli in due squadre
	 * disposte alternativamente attorno al tavolo.
	 * 
	 * @param umano il {@link Giocatore} umano che partecipa alla partita
	 */
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
	
	/**
	 * Modifica lo stato corrente della partita e notifica tutti gli osservatori del cambiamento.
	 * 
	 * @param nuovoStato il nuovo {@link Stato} da impostare
	 */
	public void cambiaStato(Stato nuovoStato) {
		
		this.statoPartita= nuovoStato;
		setChanged();
		notifyObservers(); // non uso l'argomento
		
	}
	
	/**
	 * Restituisce lo stato attuale del flusso di gioco.
	 * 
	 * @return lo {@link Stato} corrente della partita
	 */
	public Stato getStatoCorrente() {
		return statoPartita;
	}
	
	/**
	 * Inizializza la partita distribuendo 3 carte a ogni giocatore, definendo la briscola
	 * e impostando lo stato iniziale.
	 */
	public void inizializzaPartita() {
		
		mazzo = new Mazzo();
	   
		int distr= 3;
		
		while(distr>0) {
		
			for (Giocatore giocatore: giocatori) {
				
				giocatore.riceviCarta(mazzo.pesca());
			}
			
			distr-=1;
		}
		
		briscola= mazzo.scegliBriscola();
		
		turno= 0;
		
		cambiaStato(Stato.INIZIO_PARTITA);
	}
	
	/**
	 * Esegue la giocata di una carta da parte di un giocatore, posizionandola sul tavolo
	 * e facendo avanzare il turno.
	 * 
	 * @param giocatore giocatore il {@link Giocatore} che intende calare la carta
	 * @param carta carta la {@link Carta} da giocare
	 * @throws IllegalStateException se il giocatore che tenta di giocare non è quello di turno
	 */
	public void giocaCarta(Giocatore giocatore, Carta carta) {
		
		if (!giocatore.equals(getGiocatoreCorrente())) {
            throw new IllegalStateException("Non è il turno di " + giocatore.getNickname());
        }

        // 1. Rimuoviamo la carta dalla mano del giocatore
        giocatore.getMano().remove(carta);

        // 2. Aggiungiamo la carta al tavolo e nel dizionario giocate
        tavolo.add(carta);
        
        giocate.put(carta, giocatore);
        
        if (tavolo.size() < 4) {
            turno = (turno + 1) % giocatori.size();
        }
        // 3. Notifichiamo la View del cambiamento
        cambiaStato(Stato.CARTA_GIOCATA);
       
	}
	
	/**
	 * Avvia una nuova mano verificando prima se la partita è giunta al termine.
	 */
	public void avviaNuovaMano() {
	    // Se il gioco è finito, passiamo allo stato finale
	    if (isFinita()) {
	        cambiaStato(Stato.FINE_PARTITA);
	        return;
	    }

	    // Se ci sono ancora carte, segnaliamo che la nuova mano sta per cominciare
	    cambiaStato(Stato.INIZIO_MANO);
	}

	
	/**
	 * Determina il vincitore della mano corrente, assegna le carte vinte al suo pozzo,
	 * gestisce la pescata di nuove carte dal mazzo e aggiorna lo stato di gioco.
	 */
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
	    	aggiornaStatisticheFinePartita();
	        cambiaStato(Stato.FINE_PARTITA);
	        return;
	    }

	    cambiaStato(Stato.FINE_MANO);
	}
		
	
	
	/**
	 * Verifica se la partita è terminata.
	 * 
	 * @return {@code true} se mazzo, tavolo e mani dei giocatori sono vuoti, {@code false} altrimenti
	 */
	public boolean isFinita() {
		
		if(giocatori == null || giocatori.isEmpty()) return false;
		
		return mazzo.isEmpty() && tavolo.isEmpty() && giocatori.get(0).getMano().isEmpty();
	
	}
	
	/**
	 * Verifica se è attualmente il turno del giocatore umano.
	 * 
	 * @return {@code true} se spetta all'umano giocare, {@code false} altrimenti
	 */
	public boolean isTurnoUmano() {
        return this.turno == 0;
    }
	
	/**
	 * Restituisce l'indice del giocatore attualmente di turno.
	 * 
	 * @return l'indice del turno corrente
	 */
	public int getTurno() {
		return turno;
	}
	
	/**
	 * Determina la squadra vincitrice della partita confrontando i punti totali accumulati.
	 * 
	 * @return la {@link Squadra} che ha ottenuto il punteggio più alto
	 */
	public Squadra getVincitoreFinale() {
			
			//vincitoreFinale= giocatori.stream().max(Comparator.comparing(Giocatore::calcolaPunteggio)).orElse(null);
			
			squadraVincente= squadre.stream().max(Comparator.comparing(Squadra::getPuntggioSquadra)).orElse(null);
			
			return squadraVincente;		
	}
		
	/**
	 * Restituisce la carta che definisce il seme di briscola per la partita corrente.
	 * 
	 * @return la {@link Carta} di briscola
	 */
	public Carta getBriscola() {

		return briscola;
	}
	
	/**
	 * Restituisce la lista di tutti i giocatori partecipanti.¨
	 * 
	 * @return la lista dei {@link Giocatore}
	 */
	public List<Giocatore> getGiocatori() {
		return giocatori;
	}
	
	/**
	 * Restituisce le squadre partecipanti alla partita.
	 * 
	 * @return la lista delle {@link Squadra}
	 */
	public List<Squadra> getSquadre(){
		
		return squadre;
		
	}
	
	/**
	 * Restituisce le carte attualmente giocate sul tavolo nella mano corrente.
	 * 
	 * @return la lista delle {@link Carta} sul tavolo
	 */
	public List<Carta> getTavolo() {
		return tavolo;
	}
	
	/**
	 * Restituisce il giocatore a cui spetta la mossa nel turno corrente.
	 * 
	 * @return il {@link Giocatore} di turno
	 */
	public Giocatore getGiocatoreCorrente() {
		
		return giocatori.get(turno);
	}
	
	/**
	 * Restituisce la mano di carte del giocatore umano (primo giocatore).
	 * 
	 * @return la lista delle {@link Carta} in mano all'utente
	 */
	public List<Carta> getManoUmano(){
		
		if(giocatori == null || giocatori.isEmpty()) return new ArrayList<>();
		
		return giocatori.get(0).getMano();
	}
	
	/**
	 * Restituisce il mazzo di carte della partita.
	 * 
	 * @return il {@link Mazzo} in uso
	 */
	public Mazzo getMazzo(){
		
		return mazzo;
	}
	
	/**
	 * Restituisce il profilo utente associato alla partita corrente.
	 * 
	 * @return il {@link Profilo} dell'utente
	 */
	public Profilo getProfilo() {
		return profilo;
	}
	
	/**
	 * Associa un nuovo profilo utente alla partita e notifica gli osservatori.
	 * 
	 * @param profilo il nuovo {@link Profilo} da associare
	 */
	public void setProfilo(Profilo profilo) {
		
		this.profilo=profilo;
		
		setChanged();
        notifyObservers();
	}
	
	/**
	 * Ripristina lo stato iniziale della partita svuotando mazzo, tavoli, squadre e collezioni di giocate.
	 */
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
	
	/**
	 * Aggiorna il bilancio di vittorie/sconfitte nel profilo dell'utente a fine partita e salva i dati su file.
	 */
	public void aggiornaStatisticheFinePartita() {
		
		if (profilo == null || squadre.isEmpty()) return;
		
		if (squadre.get(0).equals(getVincitoreFinale())) {
			
			profilo.registraVittoria();	
		} else {
		
			profilo.registraSconfitta();
		}
		
        GestoreProfilo.salva(profilo);
        
        setProfilo(profilo); // Notifica la View per sicurezza
		
	}
	
	/**
	 * Individua il compagno di squadra del giocatore specificato.
	 * 
	 * @param g il {@link Giocatore} di cui identificare il compagno
	 * @return il {@link Giocatore} compagno di squadra, oppure {@code null} se non trovato
	 */
	public Giocatore getCompagnoDi(Giocatore g) {
		
		//filtro le squadre selezionando quella che contiene il giocatore "g" di cui identificare il compagno, della squadra risultante filtro e restituisco il giocatore compagno, che non è "g"
		return squadre.stream().filter(s->s.contieneGiocatore(g)).flatMap(s->s.getGiocatori().stream()).filter(gg->!gg.equals(g)).findFirst().orElse(null);

	}
	
	/**
	 * Risale al giocatore che ha calato una specifica carta sul tavolo nella mano corrente.
	 * 
	 * @param cartaCorrente la {@link Carta} di cui si vuole conoscere l'autore della giocata
	 * @return il {@link Giocatore} che ha giocato la carta, oppure {@code null} se non presente
	 */
	public Giocatore getGiocatoreCheHaGiocato(Carta cartaCorrente) {
		
		return giocate.get(cartaCorrente);
		
	}
	
	/**
	 * Verifica se due giocatori fanno parte della medesima squadra.
	 * 
	 * @param uno il primo {@link Giocatore} da verificare
	 * @param due il secondo {@link Giocatore} da verificare
	 * @return {@code true} se i due giocatori appartengono alla stessa squadra, {@code false} altrimenti
	 */
	public boolean sonoInStessaSquadra(Giocatore uno, Giocatore due) {
	    if (uno == null || due == null) return false; // sicurezza

	    return squadre.stream()
	            .anyMatch(s -> s.contieneGiocatore(uno) && s.contieneGiocatore(due));
	}
	

}
