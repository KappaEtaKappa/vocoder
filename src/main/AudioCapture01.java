//http://www.developer.com/java/other/article.php/1572251/Java-Sound-Getting-Started-Part-1-Playback.htm#Whats Next
//http://www.developer.com/java/other/article.php/1579071/Java-Sound-Getting-Started-Part-2-Capture-Using-Specified-Mixer.htm
//http://www.dickbaldwin.com/tocadv.htm

package main;
/*File AudioCapture01.java
This program demonstrates the capture
and subsequent playback of audio data.

A GUI appears on the screen containing
the following buttons:
Capture
Stop
Playback

Input data from a microphone is
captured and saved in a
ByteArrayOutputStream object when the
user clicks the Capture button.

Data capture stops when the user clicks
the Stop button.

Playback begins when the user clicks
the Playback button.

Tested using SDK 1.4.0 under Win2000
**************************************/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*;

public class AudioCapture01 extends JFrame {

	boolean stopCapture = false;
	ByteArrayOutputStream byteOutStream;
	AudioFormat audioFormat;
	TargetDataLine tDL;
	AudioInputStream audioInputStream;
	SourceDataLine sDL;

	public static void main(String args[]) {
		new AudioCapture01().findCompatableMixers();
		
		
	}// end main

	public AudioCapture01() {// constructor
		final JButton captureBtn = new JButton("Capture");
		final JButton stopBtn = new JButton("Stop");
		final JButton playBtn = new JButton("Playback");

		captureBtn.setEnabled(true);
		stopBtn.setEnabled(false);
		playBtn.setEnabled(false);

		// Register anonymous listeners
		captureBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				captureBtn.setEnabled(false);
				stopBtn.setEnabled(true);
				playBtn.setEnabled(false);
				// Capture input data from the
				// microphone until the Stop
				// button is clicked.
				captureAudio();
			}// end actionPerformed
		}// end ActionListener
		);// end addActionListener()
		getContentPane().add(captureBtn);

		stopBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				captureBtn.setEnabled(true);
				stopBtn.setEnabled(false);
				playBtn.setEnabled(true);
				// Terminate the capturing of
				// input data from the
				// microphone.
				stopCapture = true;
			}// end actionPerformed
		}// end ActionListener
		);// end addActionListener()
		getContentPane().add(stopBtn);

		playBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Play back all of the data
				// that was saved during
				// capture.
				playAudio();
			}// end actionPerformed
		}// end ActionListener
		);// end addActionListener()
		getContentPane().add(playBtn);

		getContentPane().setLayout(new FlowLayout());
		setTitle("Capture/Playback Demo");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(250, 70);
		setVisible(true);
	}// end constructor
	
	//print out mixers on this computer
	public void printMixers(){
		Mixer.Info[] mixers = AudioSystem.getMixerInfo();
		int i = 0;
		for(Mixer.Info e : mixers){
			System.out.println(i + " " + e);
			++i;
		}
	}
	
	public void findCompatableMixers(){
		Mixer.Info[] mixers = AudioSystem.getMixerInfo();
		Mixer mxr;
		for(Mixer.Info e : mixers){
			try{
				mxr = AudioSystem.getMixer(e);
				System.out.println(e + " is compatable");
			}catch(Exception exc){
				System.out.println(e + " Not Compatable");
			}
		}
	}

	// This method captures audio input
	// from a microphone and saves it in
	// a ByteArrayOutputStream object.
	private void captureAudio() {
		try {
			audioFormat = getAudioFormat();
			DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
			sDL = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			sDL.open(audioFormat);
			sDL.start();
			// Get everything set up for
			// capture
			
			dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
			tDL = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
			tDL.open(audioFormat);
			tDL.start();

			// Create a thread to capture the
			// microphone data and start it
			// running. It will run until
			// the Stop button is clicked.
			Runnable capture = new Runnable() {
				//the lower the amount of bytes in the buffer, the lower the delay, min is 2
				byte tempInBuffer[] = new byte[2];
				byte tempOutBuffer[] = new byte[16];
				//TODO MAKE PLAYBACK SIMULTANEIOUS god i suck at spelling
				//
				public void run() {
					byteOutStream = new ByteArrayOutputStream();
					stopCapture = false;
					try {// Loop until stopCapture is set
							// by another thread that
							// services the Stop button.
						while (!stopCapture) {
							// Read data from the internal
							// buffer of the data line.
							int cur = tDL.read(tempInBuffer, 0, tempInBuffer.length);
							
							if (cur > 0) {
								// Save data in output stream
								// object.
								//byteOutStream.write(tempInBuffer, 0, cur);
								sDL.write(tempInBuffer, 0, cur); //was just added
							} // end if
							
							//TODO clear byeOutStream
						} // end while
						sDL.drain();
						sDL.close();
						byteOutStream.close();
					} catch (Exception e) {
						e.printStackTrace();
						System.exit(0);
					} // end catch
				}// end run
			};
			new Thread(capture).start();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.exit(0);
		}
	}

	// This method plays back the audio
	// data that has been saved in the
	// ByteArrayOutputStream
	private void playAudio() {
		try {
			// Get everything set up for
			// playback.
			// Get the previously-saved data
			// into a byte array object.
			byte audioData[] = byteOutStream.toByteArray();
			// Get an input stream on the
			// byte array containing the data
			InputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
			AudioFormat audioFormat = getAudioFormat();
			audioInputStream = new AudioInputStream(byteArrayInputStream, audioFormat,
					audioData.length / audioFormat.getFrameSize());
			DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
			sDL = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			sDL.open(audioFormat);
			sDL.start();

			// Create a thread to play back
			// the data and start it
			// running. It will run until
			// all the data has been played
			// back.
			Runnable playThread = new Runnable() {
				byte tempBuffer[] = new byte[10000];

				public void run() {
					try {
						int cnt;
						// Keep looping until the input
						// read method returns -1 for
						// empty stream.
						while ((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
							if (cnt > 0) {
								// Write data to the internal
								// buffer of the data line
								// where it will be delivered
								// to the speaker.
								sDL.write(tempBuffer, 0, cnt);
							} // end if
						} // end while
							// Block and wait for internal
							// buffer of the data line to
							// empty.
						sDL.drain();
						sDL.close();
					} catch (Exception e) {
						System.out.println(e);
						System.exit(0);
					} // end catch
				}// end run
			};
			new Thread(playThread).start();
		} catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		} // end catch
	}// end playAudio

	// This method creates and returns an
	// AudioFormat object for a given set
	// of format parameters. If these
	// parameters don't work well for
	// you, try some of the other
	// allowable parameter values, which
	// are shown in comments following
	// the declarations.
	private AudioFormat getAudioFormat() {
		float sampleRate = 8000.0F;
		// 8000,11025,16000,22050,44100
		int sampleSizeInBits = 16;
		// 8,16
		int channels = 1;
		// 1,2
		boolean signed = true;
		// true,false
		boolean bigEndian = false;
		// true,false
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}

}// end outer class