package briscola.controller;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import briscola.model.*;
import briscola.view.AudioManager;
import briscola.view.FinestraGiocoJBriscola;
import briscola.view.Pannello;

@SuppressWarnings("deprecation")
public class JBriscola {

    private Partita model;
    private FinestraGiocoJBriscola view;
    
    private Timer timerPulizia;
    private Timer timerBot;

    public JBriscola(Partita model, FinestraGiocoJBriscola view) {
        this.model = model;
        this.view = view;
        
        this.model.addObserver(view.getPannelloGioco());
        this.model.addObserver(view.getPannelloMenu());

     // --- AZIONE BOTTONE "GIOCA" NEL MENU ---
        view.getPannelloMenu().getGioca().addActionListener(e->{
        	
        	if(this.model.getGiocatori().isEmpty()) {
        		this.model.preparaPartitaASquadre(new GiocatoreUmano("Giocatore"));
        	}
        	
        	this.model.inizializzaPartita();
        	view.mostraSchermata(Pannello.TipoPannello.GIOCO.name());
        	
        	gestisciAvanzamentoTurno();
        	
        });
        
     // --- AZIONE BOTTONE "MENU" NEL PANNELLO GIOCO ---
        view.getPannelloGioco().getTornaAlMenu().addActionListener(e -> {
        	
        	// Mostra l'allert di conferma uscita
        	int scelta= JOptionPane.showConfirmDialog(
        			view.getPannelloGioco(), 
        			"Attenzione: tornando al menu la partita attuale verrà resettata!\nSei sicuro di voler uscire?",
        	        "Conferma Uscita",
        	        JOptionPane.YES_NO_OPTION,
        	        JOptionPane.WARNING_MESSAGE
        			);
        	// Se si pricedi al reset e torna al menu
        	if(scelta==JOptionPane.YES_NO_OPTION) {
        		
        		// Ferma eventuali timer in corso!
                if (timerPulizia != null && timerPulizia.isRunning()) timerPulizia.stop();
                if (timerBot != null && timerBot.isRunning()) timerBot.stop();
                
                // Resetta il Model e torna al Menu
                this.model.resetPartita();
                this.view.mostraSchermata(Pannello.TipoPannello.MENU.name());
            }
        });
        		
    
        
     // --- 2. AZIONI BOTTONI CARTE (SUD) ---
        // Colleghiamo ciascuno dei 3 bottoni delle carte
        for (int i = 0; i < 3; i++) {
            final int indiceCarta = i; // Variabile final per la lambda
            this.view.getPannelloGioco().addCartaClickListener(indiceCarta, e -> {
                // Esegue la giocata dell'umano solo se è il suo turno
                if (this.model.isTurnoUmano() && indiceCarta < this.model.getManoUmano().size()) {
                	
                	AudioManager.getInstance().play("/audio/placeCard.wav");
                    
                	this.model.giocaCarta(model.getGiocatori().get(0), model.getManoUmano().get(indiceCarta));
                    //AudioManager.getInstance().play("/audio/placeCard.wav");
                    gestisciAvanzamentoTurno();
                }
            });
        }
        
        
    }
    
    public void gestisciAvanzamentoTurno() {
        // CASO 1: Tutte e 4 le carte sono sul tavolo (Mano completata)
        if (this.model.getTavolo().size() == 4) {
            // Pausa di 2 secondi per far vedere le carte sul tavolo prima di pulire
            timerPulizia = new Timer(1500, e -> {
            	if (this.model.getGiocatori().isEmpty()) return; // non parte se la partita è stata "svuotata" per evitare crash
            	
            	AudioManager.getInstance().play("/audio/sweepTable.wav");
                
            	this.model.risolviMano(); // Pulisce il tavolo, assegna punti e cambia stato in FINE_MANO
                
                // Se dopo la pulizia tocca a un bot iniziare la mano successiva, avvia il bot
            	if (this.model.isFinita()) {
                    // Aspetta 600ms prima di mostrare il Pop-Up!
                    // Questo evita che JOptionPane blocchi il thread prima di sentire l'audio e vedere il tavolo pulito.
                    Timer timerFinePartita = new Timer(600, ev -> {
                        view.getPannelloGioco().mostraPopupFinePartita(); 
                     // Resetta il Model e torna al Menu
                        this.model.resetPartita();
                        this.view.mostraSchermata(Pannello.TipoPannello.MENU.name());
                    });
                    timerFinePartita.setRepeats(false);
                    timerFinePartita.start();
                } 
                else if (!this.model.isTurnoUmano()) {
                    // Se la partita continua e tocca a un bot, prosegui
                    gestisciAvanzamentoTurno();
                }
            });
            timerPulizia.setRepeats(false);
            timerPulizia.start();
        } 
        // CASO 2: La mano non è finita e tocca a un Bot
        else if (!this.model.isTurnoUmano() && !this.model.isFinita()) {
            // Pausa di 1 secondo prima che il bot giochi
            timerBot = new Timer(1000, e -> {
            	if (this.model.getGiocatori().isEmpty()) return;
                GiocatoreIA botCorrente = (GiocatoreIA) this.model.getGiocatoreCorrente();
                Carta cartaBot = botCorrente.selezionaCartaDaGiocare(); // Il bot sceglie la carta
                
                AudioManager.getInstance().play("/audio/placeCard.wav");
                this.model.giocaCarta(botCorrente, cartaBot); // Gioca la carta (imposta CARTA_GIOCATA)
                //AudioManager.getInstance().play("/audio/placeCard.wav");
                
                gestisciAvanzamentoTurno(); // Richiama se stesso per verificare il prossimo passo
            });
            timerBot.setRepeats(false);
            timerBot.start();
        }
        // CASO 3: Tocca all'Umano
        // Non serve nessun Timer, il programma attende il click dell'utente sui bottoni delle proprie carte
    }
    
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Instanziamo il Model
            Partita model = new Partita();

            // 2. Instanziamo la View (il Frame)
            FinestraGiocoJBriscola view = new FinestraGiocoJBriscola();

            // 3. Instanziamo il Controller che collega tutto
            new JBriscola(model, view);

            // 4. Mostriamo la finestra
            view.setVisible(true);
        });
        
    }
}