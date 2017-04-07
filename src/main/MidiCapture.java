/**
 * 
 */
package main;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;
import javax.sound.midi.Transmitter;

/**
 * @author Matt
 *
 */
public class MidiCapture {
	private Transmitter transmitter;
	private Receiver receiver;
	private MidiDevice midiIn;

	public static void main(String[] args) {
		MidiCapture a = new MidiCapture();

	}

	// no sequencers used since we in real time
	public MidiCapture() {
		try {
			if ((midiIn = getKeyBoardDevice()) == null) {
				System.err.println("Keyboard not found");
			}
			// Open a connection to your input device
			midiIn.open();
			//TODO get the midiIn to send a midi message
			//http://stackoverflow.com/questions/6937760/java-getting-input-from-midi-keyboard
			// Get the transmitter class from  input device
			transmitter = midiIn.getTransmitter();

			Synthesizer synth = MidiSystem.getSynthesizer();
			Receiver synthReciever = synth.getReceiver();

			// Bind the transmitter to the receiver so the receiver gets input
			// from the transmitter
			transmitter.setReceiver(synthReciever);
			
			MidiChannel channels[] = synth.getChannels();
			//channels
			//synthReciever.send(transmitter., -1);
			midiIn.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private MidiDevice getKeyBoardDevice() throws MidiUnavailableException {
		for (MidiDevice.Info e : MidiSystem.getMidiDeviceInfo()) {
			if (e.toString().equals("Oxygen 49")) {
				return MidiSystem.getMidiDevice(e);
			}

		}
		return null;

	}
}
