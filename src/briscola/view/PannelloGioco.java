package briscola.view;

import briscola.model.*;
import briscola.model.Partita.Stato;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.border.Border;

public class PannelloGioco extends Pannello {

	// Bottoni per le carte dell'umano (Sud)
    private JButton[] bottoniCarteMano;
    private JLabel labelPreseNoi;
    
    // Label per le carte coperte dei BOT
    private JLabel[] labelCarteOvest; // Avversario 1 (Giocatore 1)
    private JLabel[] labelCarteNord;  // Compagno (Giocatore 2)
    private JLabel[] labelCarteEst;   // Avversario 2 (Giocatore 3)
    private JLabel labelPreseAvversari;
    
    // Label per le 4 carte giocate al centro del tavolo
    private JLabel[] labelCarteTavolo; // le carte sul tavolo
    private JLabel labelBriscola; // indica indica la briscola
    private JLabel labelStatoGioco; // indica il turno attuale e vincitore finale?
    private JButton tornaAlMenu;
    private String messaggioVittoria;
    

    // Immagine standard per il retro delle carte giocate coperte
    private final ImageIcon iconaRetro = caricaIcona("retro", 70, 110);
    private final ImageIcon iconaRetroRuotata= caricaIcona("retro_ruotata", 110,70);

    public PannelloGioco() {
       
        setLayout(new BorderLayout());
        
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        inizializzaInterfaccia();
        
    }

    private void inizializzaInterfaccia() {
        // --- NORD CONTENITORE ---
        JPanel areaNord = creaPannelloTrasparente(new GridLayout(2, 1));
        this.add(areaNord, BorderLayout.NORTH);
        
        // Menu Nord
        JPanel panelMenuNord = creaPannelloTrasparente(new FlowLayout(FlowLayout.RIGHT));
        tornaAlMenu = creaBottoneStilizzato("MENU");
        panelMenuNord.add(tornaAlMenu);
        areaNord.add(panelMenuNord);
        
        // Nord: Compagno (Carte coperte)
        JPanel panelNord = creaPannelloTrasparente(new FlowLayout());
        panelNord.setPreferredSize(new Dimension(0, 120)); // Altezza fissa per il Nord
        labelCarteNord = new JLabel[3];
        for (int i = 0; i < 3; i++) {
            labelCarteNord[i] = new JLabel(iconaRetro);
            panelNord.add(labelCarteNord[i]);
        }
        areaNord.add(panelNord);

        // --- OVEST : Avversari (Carte coperte) ---
        JPanel panelOvest = creaPannelloTrasparente(new BorderLayout());
        panelOvest.setPreferredSize(new Dimension(120, 0)); // Larghezza fissa per gli avversari Ovest ed Est (80px per contenere le carte da 70px)
        JPanel panelCarteOvest = creaPannelloTrasparente(new GridLayout(3,1,0,5));
        labelCarteOvest = new JLabel[3];
    
        for (int i = 0; i < 3; i++) {
            labelCarteOvest[i] = new JLabel(iconaRetroRuotata);
            panelCarteOvest.add(labelCarteOvest[i]);
        }
        panelOvest.add(panelCarteOvest, BorderLayout.NORTH);
        this.add(panelOvest, BorderLayout.WEST);
        
        
     // --- EST: Avversario 2 + Mazzetto Prese Avversari ---
        JPanel panelEst = creaPannelloTrasparente(new BorderLayout());
        panelEst.setPreferredSize(new Dimension(120, 0));

        // Contenitore centrale per le carte Ovest/Est che centra il GridLayout
        JPanel panelCarteEstWrapper = creaPannelloTrasparente(new GridBagLayout());
        JPanel panelCarteEst = creaPannelloTrasparente(new GridLayout(3, 1, 0, 5)); // 5px di gap verticale

        labelCarteEst = new JLabel[3];
        for (int i = 0; i < 3; i++) {
            labelCarteEst[i] = new JLabel(iconaRetroRuotata);
            panelCarteEst.add(labelCarteEst[i]);
        }
        panelCarteEstWrapper.add(panelCarteEst);

        panelEst.add(panelCarteEstWrapper, BorderLayout.NORTH);

        // Mazzetto Prese Avversari in basso
        labelPreseAvversari = this.creaLabelMazzettoPrese();
        panelEst.add(labelPreseAvversari, BorderLayout.SOUTH);

        this.add(panelEst, BorderLayout.EAST);
        


        // --- CENTRO: Tavolo ---
        JPanel panelCentro = creaPannelloTrasparente(new BorderLayout());
        Border bordoEsterno = BorderFactory.createEmptyBorder(80, 100, 80, 100);
        Border bordoInterno = BorderFactory.createTitledBorder("Tavolo");
        panelCentro.setBorder(BorderFactory.createCompoundBorder(bordoEsterno, bordoInterno));
        
        JPanel panelBriscola = creaPannelloTrasparente(new FlowLayout());
        labelBriscola = new JLabel();
        labelStatoGioco = new JLabel();
        panelBriscola.add(labelStatoGioco);
        panelBriscola.add(labelBriscola);
        panelCentro.add(panelBriscola, BorderLayout.WEST);

        JPanel panelTavoloCarte = creaPannelloTrasparente(new GridLayout(1, 4));
        labelCarteTavolo = new JLabel[4];
        for (int i = 0; i < 4; i++) {
            labelCarteTavolo[i] = new JLabel();
            labelCarteTavolo[i].setHorizontalAlignment(SwingConstants.CENTER);
            panelTavoloCarte.add(labelCarteTavolo[i]);
        }
        
        panelCentro.add(panelTavoloCarte, BorderLayout.CENTER);
        this.add(panelCentro, BorderLayout.CENTER);

        // --- SUD: Carte Giocatore Umano ---
        JPanel panelSud = creaPannelloTrasparente(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelSud.setPreferredSize(new Dimension(0, 130)); // Altezza fissa per il Sud
        
        // Contenitore per le 3 carte in mano
        JPanel panelManoUmano = creaPannelloTrasparente(new FlowLayout());
        bottoniCarteMano = new JButton[3];
        for (int i = 0; i < 3; i++) {
            bottoniCarteMano[i] = new JButton();
            bottoniCarteMano[i].setPreferredSize(new Dimension(80, 120));
            bottoniCarteMano[i].setOpaque(false);
            bottoniCarteMano[i].setContentAreaFilled(false);
            bottoniCarteMano[i].setBorderPainted(false);
            bottoniCarteMano[i].setFocusPainted(false);
            panelManoUmano.add(bottoniCarteMano[i]);
        }
        panelSud.add(panelManoUmano);
        
        // Mazzetto Prese "Noi" (posizionato a fianco delle tue carte)
        labelPreseNoi = creaLabelMazzettoPrese();
        panelSud.add(labelPreseNoi);
  
        this.add(panelSud, BorderLayout.SOUTH);
    }
    
    public void mostraPopupFinePartita() {
    	
    	JOptionPane.showMessageDialog(
                this, 
                messaggioVittoria, 
                "Fine Partita", 
                JOptionPane.INFORMATION_MESSAGE
         );
    	
    	
    }

    // =========================================================================
    // OBSERVER UPDATE: Riceve gli oggetti Carta dal Model e imposta le icone
    // =========================================================================
    @Override
    public void update(Observable o, Object arg) {
        // Cast del primo parametro "o" a Partita
        if (o instanceof Partita) {
            Partita partita = (Partita) o;

            // 1. Controlla l'Enum Stato
            if (partita.getStatoCorrente() == Stato.FINE_PARTITA) {
            	//labelStatoGioco.setText("<html><center>PARTITA FINITA!<br>HA VINTO LA SQUADRA: " + partita.getVincitoreFinale() + "</center></html>");
            	messaggioVittoria= "PARTITA FINITA! HA VINTO LA SQUADRA: " + partita.getVincitoreFinale();
            	//labelStatoGioco.setText(messaggioVittoria);
                disabilitaTuttiIBottoni();
                
             } else { 
            	// Costruiamo la stringa in HTML per la Label
            	    String testoStato = "<html>Stato: " + partita.getStatoCorrente();
            	    
            	    // Se la briscola esiste, andiamo a capo con <br>
            	    if (partita.getBriscola() != null) {
            	        testoStato += "<br>Briscola: " + partita.getBriscola().getSeme();
            	    }
            	    
            	    testoStato += "</html>"; // Chiudiamo il tag HTML
            	    
            	    labelStatoGioco.setText(testoStato);
             }


         // 1. Briscola
            Carta briscola = partita.getBriscola();
            if (briscola != null && !partita.getMazzo().isEmpty()) {
                labelBriscola.setIcon(caricaIcona(briscola.toString(), 70, 110));
            } else {
            	 labelBriscola.setIcon(null);
            }

            // 2. Mano Umano (Sud)
            List<Carta> manoUmano = partita.getManoUmano();
            for (int i = 0; i < 3; i++) {
                if (manoUmano != null && i < manoUmano.size() && manoUmano.get(i) != null) {
                    bottoniCarteMano[i].setIcon(caricaIcona(manoUmano.get(i).toString(), 80, 120));
                    bottoniCarteMano[i].setVisible(true);
                    bottoniCarteMano[i].setEnabled(partita.isTurnoUmano());
                } else {
                    bottoniCarteMano[i].setIcon(null);
                    bottoniCarteMano[i].setVisible(false);
                }
            }

            // 3. Carte coperte dei BOT (Ovest, Nord, Est)
            if (partita.getGiocatori().size() >= 4) {
                int carteOvest = partita.getGiocatori().get(1).getMano().size();
                int carteNord  = partita.getGiocatori().get(2).getMano().size();
                int carteEst   = partita.getGiocatori().get(3).getMano().size();

                for (int i = 0; i < 3; i++) {
                    labelCarteOvest[i].setVisible(i < carteOvest);
                    labelCarteNord[i].setVisible(i < carteNord);
                    labelCarteEst[i].setVisible(i < carteEst);
                }
            }

            // 4. Carte sul Tavolo
            List<Carta> sulTavolo = partita.getTavolo();
            for (int i = 0; i < 4; i++) {
                if (sulTavolo != null && i < sulTavolo.size() && sulTavolo.get(i) != null) {
                    labelCarteTavolo[i].setIcon(caricaIcona(sulTavolo.get(i).toString(), 70, 110));
                } else {
                    labelCarteTavolo[i].setIcon(null);
                }
            }
            
         // 6. Aggiornamento Punteggi e Mazzetti
            List<Squadra> squadre = partita.getSquadre();
            if (squadre != null && squadre.size() >= 2) {
                aggiornaMazzettoPrese(labelPreseNoi, squadre.get(0));
                aggiornaMazzettoPrese(labelPreseAvversari, squadre.get(1));
            }
        }

        repaint();
        revalidate();
    }

    private void disabilitaTuttiIBottoni() {
        for (JButton b : bottoniCarteMano) {
            b.setEnabled(false);
        }
    }

    private ImageIcon caricaIcona(String nome, int larghezza, int altezza) {
    	String path= "/images/"+nome+".png";
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image img = icon.getImage().getScaledInstance(larghezza, altezza, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } else {
            // controllo per vedere l'eventuale errore in console!
            System.err.println("ERRORE: Impossibile trovare l'immagine al percorso: " + path);
            return null;
        }
    }
    
    
    public void addCartaClickListener(int indiceCarta, ActionListener listener) { // verificare se metterlo nel controller
        if (indiceCarta >= 0 && indiceCarta < bottoniCarteMano.length) {
            // Attacca il listener al bottone interno corrispondente (0, 1 o 2)
            bottoniCarteMano[indiceCarta].addActionListener(listener);
        }
    }

    
    private JPanel creaPannelloTrasparente(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setOpaque(false); 
        return panel;
    }
    
    private JLabel creaLabelMazzettoPrese() {
        JLabel label = new JLabel("Punti: 0", iconaRetro, SwingConstants.CENTER);
        label.setVerticalTextPosition(SwingConstants.BOTTOM);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setVisible(false); // Inizialmente nascosto
        return label;
    }
    

    private void aggiornaMazzettoPrese(JLabel label, Squadra squadra) {
        if (squadra != null && (squadra.getPuntggioSquadra() > 0 || squadra.haFattoPrese())) {
            label.setText("Punti: " + squadra.getPuntggioSquadra());
            label.setVisible(true);
        } else {
            label.setVisible(false);
        }
    }

	public JButton getTornaAlMenu() {
		return tornaAlMenu;
	}
    
    
    
}


