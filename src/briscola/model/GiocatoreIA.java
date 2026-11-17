package briscola.model;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Rappresenta un giocatore controllato dall'intelligenza artificiale (IA).
 * Estende {@link Giocatore} e implementa le regole di scelta della carta
 * da giocare in base allo stato attuale della partita.
 */
public class GiocatoreIA extends Giocatore {
	
	private Random random;

	/**
	 * Costruisce un nuovo giocatore IA con il nickname specificato.
	 * 
	 * @param nickname il nome identificativo del giocatore IA
	 */
	public GiocatoreIA(String nickname) {
		super(nickname);
		this.random= new Random();
		
	}

	/**
	 * Restituisce una carta scelta casualmente dalla mano.
	 * 
	 * Nota: metodo utilizzato principalmente come fallback standard e inizialmente come test
	 * 
	 * @return la {@link Carta} selezionata casualmente
	 */
	public Carta selezionaCartaDaGiocare() {
		
		int indiceCasuale= random.nextInt(mano.size()); // la rimozione avviene nel controller
		return mano.get(indiceCasuale);
		
	}
	
	/**
	 * Seleziona la carta da giocare in base allo stato del tavolo nella partita.
	 * 
	 * @param partita la {@link Partita} corrente usata per valutare la giocata
	 * @return la {@link Carta} scelta dall'IA
	 */
	public Carta selezionaCartaDaGiocare(Partita partita) {
	    if (partita.getTavolo().isEmpty()) {
	        return giocaPerPrimo(partita);
	    }
	    return giocaSuTavoloEsistente(partita);
	}
	
	/**
	 * Verifica se una carta è dello stesso seme della briscola.
	 * 
	 * @param carta la carta da verificare
	 * @param briscola la carta di riferimento per la briscola
	 * @return {@code true} se la carta è una briscola, {@code false} altrimenti
	 */
	private boolean isBriscola(Carta carta, Carta briscola) {
	    return carta.getSeme().equals(briscola.getSeme());
	}
	
	/**
	 * Confronta la forza di due carte indipendentemente dalla briscola.
	 * 
	 * @param mia la prima carta da confrontare
	 * @param avversario la seconda carta da confrontare
	 * @return {@code true} se la prima carta ha una forza di presa maggiore
	 */
	private boolean haForzaMaggiore(Carta mia, Carta avversario) {
	    return mia.getValore().getForza() > avversario.getValore().getForza();
	}
	
	/**
	 * Determina se la carta vincente a terra è stata giocata da un compagno di squadra.
	 * 
	 * @param cartaDominante la carta attualmente vincente sul tavolo
	 * @param partita la partita in corso
	 * @return {@code true} se la carta appartiene al compagno, {@code false} altrimenti
	 */
	private boolean isCartaDelCompagno(Carta cartaDominante, Partita partita) {
	    Giocatore proprietario = partita.getGiocatoreCheHaGiocato(cartaDominante);
	    
	    // Se la carta non è stata trovata o l'ha giocata il bot stesso, non è del compagno
	    if (proprietario == null || proprietario.equals(this)) {
	        return false;
	    }
	    
	    // Verifica se il proprietario fa parte della stessa squadra dell'IA
	    return partita.sonoInStessaSquadra(this, proprietario);
	}
	
	/**
	 * Determina la giocata migliore quando il tavolo è vuoto.
	 * 
	 * @param partita la partita in corso
	 * @return la {@link Carta} selezionata da giocare
	 */
	private Carta giocaPerPrimo(Partita partita) {
		
		List<Carta> mano= getMano();
		Carta briscola= partita.getBriscola();
		
		return mano.stream()
				//Filtra non-briscole ed esclude Carichi (Asso e Tre)
				.filter(carta->!isBriscola(carta, briscola) && carta.getPunti()<=4)
				//Prende la carta non-briscola che vale meno punti (privilegia i lisci a 0pt)
				.min(Comparator.comparing(Carta::getPunti))
				// Fallback: se hai solo briscole o solo carichi non-briscola o un mix di entrambi, gioca la carta che vale meno in assoluto
				.orElseGet(()->mano.stream()
						.min(Comparator.comparing(Carta::getPunti))
						.orElseGet(this::selezionaCartaDaGiocare));
	}
	
	/**
	 * Determina la giocata quando sul tavolo ci sono già altre carte.
	 * 
	 * @param partita la partita in corso
	 * @return la {@link Carta} selezionata da giocare
	 */
	private Carta giocaSuTavoloEsistente(Partita partita) {
	    List<Carta> mano = getMano();
	    Carta briscola = partita.getBriscola();
	    
	    // 1. Identifichiamo la carta attualmente vincente a terra
	    Carta cartaDominante = RegoleBriscola.determinaVincente(partita.getTavolo(), briscola);
	    
	    // 2. Calcoliamo i punti totali attualmente in palio sul tavolo
	    int puntiSulTavolo = partita.getTavolo().stream().mapToInt(Carta::getPunti).sum();
	    
	    // CASO A: Sta vincendo il compagno 
	    if (isCartaDelCompagno(cartaDominante, partita)) {
	        return mano.stream()
	                // Aggiungi punti o un Carico ma non del seme di Briscola 
	                .filter(c -> !isBriscola(c, briscola))
	                .max(Comparator.comparing(Carta::getPunti))
	                // Aggiungi la briscola più bassa se in mano hai solo briscole
	                .orElseGet(() -> mano.stream()
	                        .min(Comparator.comparing(Carta::getPunti))
	                        .orElseGet(this::selezionaCartaDaGiocare));
	    }
	    
	    // CASO B: Vince l'avversario con una BRISCOLA e ci sono punti in palio
	    if (!isCartaDelCompagno(cartaDominante, partita) 
	            && puntiSulTavolo > 0 
	            && isBriscola(cartaDominante, briscola)) {

	        return mano.stream()
	            // Deve essere UNA BRISCOLA con forza maggiore
	            .filter(c -> isBriscola(c, briscola))
	            .filter(c -> haForzaMaggiore(c, cartaDominante))
	            .min(Comparator.comparing(Carta::getPunti)) // Briscola più piccola sufficiente a vincere
	            .orElseGet(() -> mano.stream()
	                // Se non può vincere, scarta prima un liscio non-briscola
	                .filter(c -> !isBriscola(c, briscola))
	                .min(Comparator.comparing(Carta::getPunti))
	                // Se ha SOLO briscole in mano, gioca la briscola più bassa
	                .orElseGet(() -> mano.stream()
	                    .min(Comparator.comparing(Carta::getPunti))
	                    .orElseGet(this::selezionaCartaDaGiocare)
	                )
	            );
	    }
	    
	    // CASO C: Vince l'avversario con una carta NORMALE e ci sono punti in palio
	    if(!isCartaDelCompagno(cartaDominante, partita)
	    		&& puntiSulTavolo>0
	    		&& !isBriscola(cartaDominante, briscola)) {
	    	
	    		return mano.stream()
	    				// 1. Prova prima a superare l'avversario sullo STESSO SEME con forza maggiore
	    				.filter(c-> c.getSeme().equals(cartaDominante.getSeme()) && haForzaMaggiore(c, cartaDominante))
	    				.min(Comparator.comparing(Carta::getPunti))
	    				// 2. Se non ha carte dello stesso seme e con forza maggiore per vincere la mano, taglia con la BRISCOLA più bassa
	    				.orElseGet(()->mano.stream()
	    						.filter(c-> isBriscola(c, briscola))
	    						.min(Comparator.comparing(Carta::getPunti))
	    						// 3. Fallback estremo se per qualsiasi motivo i filtri precedenti falliscono
	    						.orElseGet(this::selezionaCartaDaGiocare)); // orElseGet evita di eseguire calcoli inutili o chiamate a funzioni quando lo Stream ha già trovato la risposta valida al primo tentativo.
	    	
	    }
	    // CASO D: Fallback predefinito (es. l'avversario vince con carta NORMALE o ci sono 0 punti)
	    // scarta la carta non-briscola più bassa
	    return mano.stream()
	            .filter(c -> !isBriscola(c, briscola))
	            .min(Comparator.comparing(Carta::getPunti))
	            .orElseGet(() -> mano.stream()
	                    .min(Comparator.comparing(Carta::getPunti))
	                    .orElseGet(this::selezionaCartaDaGiocare));
	}
	

}
	
		
	
	
	
	
	
