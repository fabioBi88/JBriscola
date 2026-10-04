package briscola.view;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

import briscola.model.Partita;
import briscola.model.Profilo;

@SuppressWarnings("deprecation")
public class PannelloProfilo extends JPanel implements Observer {

    private JTextField txtNickname;
    private JLabel lblAvatarPreview;
    private JButton btnAvatarPrec;
    private JButton btnAvatarSucc;

    private JLabel lblPartiteVinte;
    private JLabel lblPartitePerse;
    private JLabel lblPartiteGiocate;
    private JLabel lblPercentualeVittorie;

    private JButton btnSalva;
    private JButton btnReset;
    private JButton btnTornaMenu;

    private final String[] avatarDisponibili = {
        "/images/avatars/avatar1.png",
        "/images/avatars/avatar2.png",
        "/images/avatars/avatar3.png",
        "/images/avatars/default.png",
        
    };
    private int indiceAvatarCorrente = 0;

    public PannelloProfilo() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTitolo = new JLabel("PROFILO GIOCATORE", SwingConstants.CENTER);
        lblTitolo.setFont(new Font("Arial", Font.BOLD, 22));
        add(lblTitolo, BorderLayout.NORTH);

        JPanel pannelloCentrale = new JPanel(new GridLayout(1, 2, 20, 0));
        pannelloCentrale.add(creaSezioneModifica());
        pannelloCentrale.add(creaSezioneStatistiche());
        add(pannelloCentrale, BorderLayout.CENTER);

        JPanel pannelloSotto = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnSalva = new JButton("Salva Modifiche");
        btnReset = new JButton("Resetta Profilo");
        btnTornaMenu = new JButton("Torna al Menu");
        
        pannelloSotto.add(btnSalva);
        pannelloSotto.add(btnReset);
        pannelloSotto.add(btnTornaMenu);
        add(pannelloSotto, BorderLayout.SOUTH);

        btnAvatarPrec.addActionListener(e -> cambiaAvatar(-1));
        btnAvatarSucc.addActionListener(e -> cambiaAvatar(1));
    }

    private JPanel creaSezioneModifica() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Dati Personali"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nickname:"), gbc);

        txtNickname = new JTextField(12);
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(txtNickname, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Avatar:"), gbc);

        JPanel avatarBox = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        btnAvatarPrec = new JButton("<");
        btnAvatarSucc = new JButton(">");
        lblAvatarPreview = new JLabel();
        lblAvatarPreview.setPreferredSize(new Dimension(70, 70));
        lblAvatarPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        avatarBox.add(btnAvatarPrec);
        avatarBox.add(lblAvatarPreview);
        avatarBox.add(btnAvatarSucc);

        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(avatarBox, gbc);

        aggiornaAnteprimaAvatar();
        return panel;
    }

    private JPanel creaSezioneStatistiche() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 15));
        panel.setBorder(BorderFactory.createTitledBorder("Statistiche"));

        panel.add(new JLabel("Partite Vinte:"));
        lblPartiteVinte = new JLabel("0");
        lblPartiteVinte.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblPartiteVinte);

        panel.add(new JLabel("Partite Perse:"));
        lblPartitePerse = new JLabel("0");
        lblPartitePerse.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblPartitePerse);
        
        panel.add(new JLabel("Partite Giocate:"));
        lblPartiteGiocate = new JLabel("0");
        lblPartiteGiocate.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblPartiteGiocate);

        panel.add(new JLabel("% Vittorie:"));
        lblPercentualeVittorie = new JLabel("0%");
        lblPercentualeVittorie.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblPercentualeVittorie);

        return panel;
    }

    private void cambiaAvatar(int direzione) {
        indiceAvatarCorrente += direzione;
        if (indiceAvatarCorrente < 0) {
            indiceAvatarCorrente = avatarDisponibili.length - 1;
        } else if (indiceAvatarCorrente >= avatarDisponibili.length) {
            indiceAvatarCorrente = 0;
        }
        aggiornaAnteprimaAvatar();
    }

    private void aggiornaAnteprimaAvatar() {
        String path = avatarDisponibili[indiceAvatarCorrente];
        java.net.URL imgURL = getClass().getResource(path);

        if (imgURL != null) {
            ImageIcon icona = new ImageIcon(imgURL);
            Image img = icona.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            lblAvatarPreview.setIcon(new ImageIcon(img));
            lblAvatarPreview.setText(""); // Pulisce eventuale testo
        } else {
            lblAvatarPreview.setIcon(null);
            lblAvatarPreview.setText("Img " + (indiceAvatarCorrente + 1));
            System.err.println("Immagine non trovata nel classpath: " + path);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Partita) {
            Partita partita = (Partita) o;
            if (partita.getProfilo() != null) {
                caricaDatiProfilo(partita.getProfilo());
            }
        }
    }
    
 // Metodo per attivare/disattivare l'editing di nickname e avatar
    public void impostaCampiModificabili(boolean abilitati) {
        txtNickname.setEnabled(abilitati);
        btnAvatarPrec.setEnabled(abilitati);
        btnAvatarSucc.setEnabled(abilitati);
    }

    public void caricaDatiProfilo(Profilo profilo) {
        if (profilo == null) return;
        
     // 1. Carica i dati effettivi del Model
        txtNickname.setText(profilo.getNickname());
        lblPartiteVinte.setText(String.valueOf(profilo.getPartiteVinte()));
        lblPartitePerse.setText(String.valueOf(profilo.getPartitePerse()));
        lblPartiteGiocate.setText(String.valueOf(profilo.getPartiteGiocate()));
        lblPercentualeVittorie.setText(profilo.getPercentualeVittorie());

        for (int i = 0; i < avatarDisponibili.length; i++) {
            if (avatarDisponibili[i].equals(profilo.getAvatarPath())) {
                indiceAvatarCorrente = i;
                break;
            }
        }
        aggiornaAnteprimaAvatar();
        
     // 2. Se il profilo è già impostato/salvato, blocchiamo i campi
        // I campi rimangono modificabili solo se il profilo è quello di default ("Giocatore") o dopo un reset
        boolean eProfiloNuovo = profilo.getNickname().equalsIgnoreCase("Giocatore") && profilo.getPartiteGiocate() == 0;
        impostaCampiModificabili(eProfiloNuovo);
    }

    public String getNicknameInserito() { return txtNickname.getText().trim(); }
    public String getAvatarSelezionato() { return avatarDisponibili[indiceAvatarCorrente]; }
    public JButton getBtnSalva() { return btnSalva; }
    public JButton getBtnReset() { return btnReset; }
    public JButton getBtnTornaMenu() { return btnTornaMenu; }
}