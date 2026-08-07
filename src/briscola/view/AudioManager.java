package briscola.view;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioManager {

	private static AudioManager instance;

	public static AudioManager getInstance() {
		if (instance == null)
			instance = new AudioManager();
		return instance;
	}

	private AudioManager() {

	}

	public void play(String filename) {
	    try {
	        // Carica la risorsa dal Classpath
	        InputStream rawStream = getClass().getResourceAsStream(filename); // variazione per fare in modo che venga cercato nella cartella src
	        
	        // CORREZIONE CRASH: Se il file non esiste, interrompi il metodo ed evita il crash!
	        if (rawStream == null) {
	            System.err.println("ERRORE AUDIO: File non trovato al percorso -> " + filename);
	            return;
	        }

	        InputStream in = new BufferedInputStream(rawStream);
	        AudioInputStream audioIn = AudioSystem.getAudioInputStream(in);
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioIn);
	        clip.start();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}

