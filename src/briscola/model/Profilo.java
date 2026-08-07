package briscola.model;

public class Profilo {
	
	private final String nickname;
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

	public int getPartiteVinte() {
		return partiteVinte;
	}

	public int getPartitePerse() {
		return partitePerse;
	}
	
	public int getPartiteGiocate() {
		return partiteVinte+partitePerse;
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
