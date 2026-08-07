package briscola.model;

import java.util.*;

public class Mazzo {
	
	private List<Carta> carte;
	
	public Mazzo() {
		
		carte= new ArrayList<>();
		
		for(Seme seme: Seme.values()) {
			for (Valore valore: Valore.values()) {
				
				carte.add(new Carta(seme, valore));
				
			}
		}
		
		mescola();
		
	}
	
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
	
	// private void mescola() {
    // Collections.shuffle(this.carte);
	// }
	
	public Carta pesca() {
		
		if(!carte.isEmpty()) return carte.remove(carte.size()-1);
		
		return null;
		
	}
	
	public Carta scelgiBriscola() {
		
		if(carte.isEmpty()) return null;
		Carta ultima= carte.remove(carte.size()-1);
		carte.add(0, ultima);
		
		return ultima;
		
	}
	

	
	public boolean isEmpty() {
		
		return carte.isEmpty();
	}
	
	public int getNumeroCarte() {
		
		return carte.size();
	}
	

}
