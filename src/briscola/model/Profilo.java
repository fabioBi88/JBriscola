package briscola.model;

import java.io.Serializable;

public class Profilo implements Serializable {
	
	// Identificatore per la serializzazione
    private static final long serialVersionUID = 1L;
	
	private String nickname;
	private int partiteVinte;
	private int partitePerse;
	private String avatarPath;
	
	public Profilo(String nickname, String avatarPath) {
		
		this.nickname=nickname;
		this.setAvatarPath(avatarPath);
	}
	
	public Profilo(String nickname) {
		this(nickname,"/images/avatars/default.png");
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public void setNickname(String nickName) {
		this.nickname=nickName;
	}

	public int getPartiteVinte() {
		return partiteVinte;
	}

	public int getPartitePerse() {
		return partitePerse;
	}
	
	public int getPartiteGiocate() {
		return partiteVinte+partitePerse;
	}
	
	public String getPercentualeVittorie() {
	    int giocate = getPartiteGiocate();
	    
	    // 1. Protezione contro il crash per divisione per zero
	    if (giocate == 0) {
	        return "0%";
	    }
	    
	    // 2. Cast a (double) per ottenere la divisione decimale corretta
	    double percentuale = ((double) getPartiteVinte() / giocate) * 100;
	    
	    // 3. Formattazione con un decimale
	    return String.format("%.1f%%", percentuale);
	}
	
	public void registraVittoria() {
		partiteVinte++;
	}
	
	public void registraSconfitta() {
		partitePerse++;
	}

	public String getAvatarPath() {
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}
	
}
