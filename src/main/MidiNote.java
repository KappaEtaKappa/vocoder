//https://www.java-forums.org/new-java/70056-help-java-real-time-midi.html

/**
 * 
 */
package main;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

/**
 * @author Matt
 *	produces a midi c note, for reference only
 */
public class MidiNote {
	public MidiNote() {
		try{
			listen();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void listen() throws MidiUnavailableException, InvalidMidiDataException{

		ShortMessage myMsg = new ShortMessage();
		// Start playing the note Middle C (60),
		// moderately loud (velocity = 93).
		long timeStamp = -1;
		Synthesizer synth = MidiSystem.getSynthesizer();
		synth.open();
		Receiver rcvr = synth.getReceiver();
		myMsg.setMessage(ShortMessage.NOTE_ON, 60, 93);
		rcvr.send(myMsg, timeStamp);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		}
		myMsg.setMessage(ShortMessage.NOTE_OFF, 60, 0);
		rcvr.send(myMsg, timeStamp);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}

	}
}
