package briscola.view;

import java.awt.CardLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import briscola.model.*;

import javax.imageio.ImageIO;
import javax.swing.*;


@SuppressWarnings("deprecation")
public class FinestraGiocoJBriscola extends JFrame{
	
	private static String titolo = "Gioco JBriscola"; // nome della finestra
	private static String pathIcona = ""; // path dell'icona della finestra

	private JPanel pannelloContenitore;
	private PannelloMenu pannelloMenu;
	private PannelloProfilo pannelloProfilo;
	private PannelloGioco pannelloGioco;
	
	public FinestraGiocoJBriscola() {
		
		super(titolo);
		
		pannelloMenu= new PannelloMenu();
		pannelloProfilo= new PannelloProfilo();
		pannelloGioco= new PannelloGioco();
		
		pannelloContenitore = new JPanel(new CardLayout()) {
			{
				
				add(pannelloMenu, Pannello.TipoPannello.MENU.name());
				add(pannelloProfilo, Pannello.TipoPannello.PROFILO.name());
				add(pannelloGioco, Pannello.TipoPannello.GIOCO.name());
				
				
			}
		};
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		try {
			setIconImage(ImageIO.read(new File(pathIcona)));
		} catch (IOException e) {
			// viene usata l'icona di default della finestra
		}

		add(pannelloContenitore);

		/*
		 * le dimensioni della finestra sono settate al minimo necessario per mostrare
		 * interamente gli elementi da essa contenuti
		 */
		setSize(900,800);
		setLocationRelativeTo(null);
		setVisible(true);
		
	}
	
	
	public void mostraSchermata(String nome) {
        ((CardLayout) pannelloContenitore.getLayout()).show(pannelloContenitore, nome);
    }
	


	public PannelloMenu getPannelloMenu() {
		return pannelloMenu;
	}


	public PannelloProfilo getPannelloProfilo() {
		return pannelloProfilo;
	}


	public PannelloGioco getPannelloGioco() {
		return pannelloGioco;
	}
	

}
