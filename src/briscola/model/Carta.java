package briscola.model;

public class Carta {
	
	private Valore valore;
	private Seme seme;
	
	public Carta(Seme seme, Valore valore) {
		
		this.valore=valore;
		this.seme=seme;
		
	}

	public Valore getValore() {
		return valore;
	}

	public Seme getSeme() {
		return seme;
	}
	
	public int getPunti() {
		return valore.getPunti();
	}
	
	public int getForza() {
		return valore.getForza();
	}
	
	public String toString() {
		
		return seme.toString().toLowerCase()+valore.getCifra();
	}

}
