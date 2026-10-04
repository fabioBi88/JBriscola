package briscola.model;

import java.io.*;

public class GestoreProfilo {

    private static final String FILE_SALVATAGGIO = "profilo.dat";

    public static void salva(Profilo profilo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_SALVATAGGIO))) {
            oos.writeObject(profilo);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio del profilo: " + e.getMessage());
        }
    }

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
