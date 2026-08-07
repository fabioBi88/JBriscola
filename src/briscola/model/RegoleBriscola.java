package briscola.model;

import java.util.Comparator;
import java.util.List;

public class RegoleBriscola {
	
	
	public static Carta determinaVincente(List<Carta> carteGiocate, Carta briscola) {
		
		if(carteGiocate==null || carteGiocate.isEmpty()) throw new IllegalArgumentException("Il tavolo non puo' essere vuoto");
		
		// ricerca tramite Stream<T>, se presente, della carta con seme di Briscola con il valore piu alto
		Carta vincente= carteGiocate.stream().filter(c->c.getSeme()==briscola.getSeme()).max((c1,c2)-> Integer.compare(c1.getForza(), c2.getForza())).orElse(null);
		
		if(vincente==null) {
			
			Seme mano= carteGiocate.get(0).getSeme();
			
			return carteGiocate.stream().filter(c->c.getSeme()==mano).max((c1,c2)-> Integer.compare(c1.getForza(), c2.getForza())).orElse(carteGiocate.get(0));
			
		}
		
		return vincente;
		
	}

}
