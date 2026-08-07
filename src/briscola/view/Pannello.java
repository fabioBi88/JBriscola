package briscola.view;

import java.awt.*;
import java.util.Observer;

import javax.swing.*;

@SuppressWarnings("deprecation")
public abstract class Pannello extends JPanel implements Observer {
	
	public enum TipoPannello {
		MENU, PROFILO, GIOCO
	}
	
	
	protected JButton creaBottoneStilizzato(String testo) {
        JButton b = new JButton(testo);
        
        b.setFont(new Font("Arial", Font.PLAIN, 18));
        b.setPreferredSize(new Dimension(180, 45));
        b.setFocusPainted(false);
        b.setBackground(Color.WHITE);
        
        return b;
    }
	
	protected JButton creaBottoneCarta(String testo) {
		JButton b= new JButton(testo);
		
		b.setFont(new Font("Arial", Font.PLAIN, 18));
		b.setPreferredSize(new Dimension(70, 100));
        b.setFocusPainted(false);
        b.setBackground(Color.WHITE);
        
        return b;
	}
	
	
	@Override
	protected void paintComponent(Graphics g) { // Sfondo linee oblique verdi
		super.paintComponent(g);
		int d = 5; g.setColor(Color.decode("#b2f2bb"));
		for (int x = 0; x <= getWidth() + getHeight(); x += d)
		g.drawLine(x, 0, 0, x);
	}
	
	
	
}
