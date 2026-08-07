package briscola.model;

import java.util.List;
import java.util.Random;

public class GiocatoreIA extends Giocatore {
	
	private Random random;

	public GiocatoreIA(String nickname) {
		super(nickname);
		this.random= new Random();
	}

	
	public Carta selezionaCartaDaGiocare() {
		
		int indiceCasuale= random.nextInt(mano.size());
		Carta cartaScelta= mano.remove(indiceCasuale);
		
		return cartaScelta;
	}
	
	

}
