package briscola.model;

import java.io.*;

/**
 * Gestisce la salvataggio e il caricamento del profilo utente su file tramite serializzazione.
 */
public class GestoreProfilo {
	
	/**
	 * Nome del file utilizzato per la persistenza del profilo.
	 */
    private static final String FILE_SALVATAGGIO = "profilo.dat";

    /**
     * Salva il profilo specificato nel file di salvataggio
     * 
     * @param profilo il profilo da salvare su file
     */
    public static void salva(Profilo profilo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_SALVATAGGIO))) {
            oos.writeObject(profilo);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio del profilo: " + e.getMessage());
        }
    }
    /**
     * Carica il profilo memorizzato nel file di salvataggio.
     * 
     * @return l'oggetto {@link Profilo} letto da file, oppure {@code null} se il file
     * 		   non esiste o si verifica un errore durante la lettura
     */
    public static Profilo carica() {
        File file = new File(FILE_SALVATAGGIO);
        if (!file.exists()) return null; // Nessun salvataggio trovato

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Profilo) ois.readObject();
        } catch (Exception e) {
            System.err.println("Errore durante il caricamento del profilo: " + e.getMessage());
            return null;
        }
    }
}
