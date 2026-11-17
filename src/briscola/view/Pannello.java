package briscola.view;

import java.awt.*;
import java.util.Observer;

import javax.swing.*;

@SuppressWarnings("deprecation")
public abstract class Pannello extends JPanel implements Observer { // integrare questa classe per renderla la "fabbrica" dei pannelli per mantenere stesso stile
	
	public enum TipoPannello {
		MENU, PROFILO, GIOCO
	}
	
	private Image backgroundImage;
	
	public Pannello(){
		
		setImage("/images/sfondo.png");
		
	}
	
	public void setImage(String percorsoImage) {
		
		try {
			ImageIcon image=  new ImageIcon(getClass().getResource(percorsoImage));
			this.backgroundImage= image.getImage();
			repaint();
		} catch (Exception e) {
			System.err.println("Impossibile caricare l'immagine di sfondo: " + percorsoImage);
			this.backgroundImage = null;
		}
		
		
		
	}

	protected JButton creaBottoneStilizzato(String testo) {
        JButton b = new JButton(testo);
        
        b.setFont(new Font("Arial", Font.PLAIN, 18));
        b.setPreferredSize(new Dimension(180, 45));
        b.setFocusPainted(false);
        b.setBackground(Color.WHITE);
        
        return b;
    }
	
	
	@Override
	protected void paintComponent(Graphics g) { 
		super.paintComponent(g);
		
		if (backgroundImage != null) {
			// Disegna l'immagine ridimensionandola dinamicamente in base alla dimensione del pannello
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
		}
		
		// Sfondo linee oblique verdi
		//int d = 5; g.setColor(Color.decode("#b2f2bb"));
		//for (int x = 0; x <= getWidth() + getHeight(); x += d)
		//g.drawLine(x, 0, 0, x);
		
	}
	
	
	
}
