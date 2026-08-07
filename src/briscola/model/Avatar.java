package briscola.model;

public enum Avatar {
	
	MARIO(1),
	CAVALLO(2),
	GIRL(3),
	PACMAN(4),
	GUN(5);
	
	private final int indice;
	
	Avatar(int indice){
		this.indice=indice;
	}
	
	public int getIndice() {
		return indice;
	}
	
	
	
}
