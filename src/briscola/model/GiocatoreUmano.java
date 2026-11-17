package briscola.model;

/**
 * Rappresenta un giocatore umano all'interno del gioco.
 * Estende {@link Giocatore} delegando la selezione delle carte
 * e le azioni di gioco all'interazione con l'utente.
 */
public class GiocatoreUmano extends Giocatore{
	
	/**
	 * Costruisce un nuovo giocatore umano con il nickname specificato.
	 * 
	 * @param nickname il nome identificativo del giocatore
	 */
	public GiocatoreUmano(String nickname) {
		super(nickname);

	}
	

}
