package briscola.view;

import java.awt.*;
import java.util.Observable;

import javax.swing.*;

public class PannelloMenu extends Pannello{
	
	JLabel titolo;
	JButton profilo;
	JButton gioca;
	
	
	public PannelloMenu() {
		
		titolo= new JLabel("JBriscola");
		profilo= creaBottoneStilizzato("Profilo");
		gioca= creaBottoneStilizzato("Gioca");
		
		setLayout(new GridBagLayout());
		titolo.setFont(new Font("Arial", Font.BOLD, 40));
		
		GridBagConstraints gbc= new GridBagConstraints();
		
		// TITOLO
		gbc.gridx= 0;
		gbc.gridy= 0;
		gbc.weighty= 1;
		gbc.anchor= GridBagConstraints.NORTH;
		gbc.insets= new Insets(30,0,0,0);
		
		add(titolo, gbc);
		
		// BOTTONE GIOCA
		gbc.gridx= 0;
		gbc.gridy= 1;
		gbc.weighty= 0;
		gbc.anchor= GridBagConstraints.CENTER;
		gbc.insets= new Insets(10,0,10,0);
		
		add(profilo, gbc);
		
		
		// BOTTONE PROFILO
		gbc.gridx= 0;
		gbc.gridy= 2;
		gbc.weighty= 1;
		gbc.anchor= GridBagConstraints.NORTH;
		gbc.insets= new Insets(10,0,10,0);
		
		
		add(gioca, gbc);
		
		
	}


	public JLabel getTitolo() {
		return titolo;
	}


	public JButton getProfilo() {
		return profilo;
	}


	public JButton getGioca() {
		return gioca;
	}


	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
	
	

}
