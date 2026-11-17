package briscola.model;

// Definisce il nome, i punti e la forza di ogni carta
public enum Valore {
	
	DUE(0,1,"02"),
	QUATTRO(0,2,"04"),
	CINQUE(0,3,"05"),
	SEI(0,4,"06"),
	SETTE(0,5,"07"),
	FANTE(2,6,"08"),
	CAVALLO(3,7,"09"),
	RE(4,8,"10"),
	TRE(10,9,"03"),
	ASSO(11,10,"01");
	
	private final int punti;
	private final int forza;
	private final String inCifre;
	
	Valore(int punti, int forza, String inCifre){
		this.punti=punti;
		this.forza=forza;
		this.inCifre= inCifre;
	}

	public int getPunti() {
		return punti;
	}

	public int getForza() {
		return forza;
	}
	
	public String getCifra() {
		return inCifre;
	}
		

}
