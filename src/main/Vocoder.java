package main;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.Mixer;

public class Vocoder {
	
	static byte[] audioBuffer; // contains the current chunk of data to be shifted

	public static void main(String[] args) {
		audioBuffer = new byte[16];
		TargetDataLine dataLine = null;
		
		SourceDataLine outputLine = null;
		Mixer.Info mixerInfo = null;
		Mixer mixer = null;
		
		// generates the input format and tries to get the dataLine for default audio device
		AudioFormat af = new AudioFormat(2000.0f, 16, 1, true, true);
		// grabs DataLine Info for our output line
		DataLine.Info outputInfo = new DataLine.Info(SourceDataLine.class, af);
		
		// gets information about our audio devices
		Mixer.Info[] mixInfoList = AudioSystem.getMixerInfo();
		
		mixerInfo = mixInfoList[1]; // currently grabs the mixer for headphones for Zaruba's computer

		try {
			dataLine = AudioSystem.getTargetDataLine(af);
			
		} catch (LineUnavailableException e) {
			System.err.println("bad input device");
			System.exit(1);
		}
		
		
		
		while(true) {
		    output(dataLine, outputLine, mixer);
		}
	}
	
	public static void output(TargetDataLine input, SourceDataLine output, Mixer mixer) {
	    input.read(audioBuffer, 0, 16);
	    output.write(audioBuffer, 0, 16);
	}
}
