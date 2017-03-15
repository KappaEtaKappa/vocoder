package main;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Vocoder {
	
	static byte[] audioBuffer; // contains the current chunk of data to be shifted

	public static void main(String[] args) {
		audioBuffer = new byte[256];
		
		// generates the input format and tries to get the dataLine for default audio device
		AudioFormat af = new AudioFormat(2000.0f, 16, 1, true, true);
		
		try {
			TargetDataLine dataLine = AudioSystem.getTargetDataLine(af);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
		
	}
}
