package main;

import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;

import java.io.ByteArrayOutputStream;
import java.util.Scanner;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.Mixer.Info;

public class Reciever {
	private int line; 
	private AudioFormat format; 
	private Info[] lines;
	private TargetDataLine inputLine;
	private DataLine.Info inInfo;
	private int bufferSize;
	private ByteArrayOutputStream byteArrayOutputStream;
	
	public static void main(String[] args){
		
		
		Reciever a = new Reciever(8000f);
		a.recieve();
	}
	
	/**
	 * prints out all possible audio inputs
	 */
	public static void printInputData(){
		Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
		try{
			for (Mixer.Info info : mixerInfos) {
				Mixer m = AudioSystem.getMixer(info);
				Line.Info[] lineInfos = m.getSourceLineInfo();
				for (Line.Info lineInfo : lineInfos) {
					System.out.println(info.getName() + "---" + lineInfo);
					Line line = m.getLine(lineInfo);
					System.out.println("\t-----" + line);
				}
				lineInfos = m.getTargetLineInfo();
				for (Line.Info lineInfo : lineInfos) {
					System.out.println(m + "---" + lineInfo);
					Line line = m.getLine(lineInfo);
					System.out.println("\t-----" + line);
				}
			}
		}catch(Exception e){ System.err.println("ERR");}
	}
	
	/**
	 * 
	 * @param inputBR takes input bit rate of the headphones
	 */
	public Reciever(float inputSR){
		format = new AudioFormat(inputSR, 16, 1, true, true);
		lines = AudioSystem.getMixerInfo();
		inInfo = new DataLine.Info(TargetDataLine.class, format);
		bufferSize = (int) (format.getSampleSizeInBits() * format.getFrameRate());
	}
	
	
	
	/**
	 * opens up a stream to read from the audio source
	 */
	public void recieve(){
		try{
			DataLine.Info dataLineInfo =
	                new DataLine.Info(
	                  TargetDataLine.class,
	                   format);
			inputLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
			inputLine.open(format);
			inputLine.start();
			Scanner scnr = new Scanner(System.in);
			System.out.println("Hit Enter to stop capture");
			Thread captureThread = new Thread(){
				byte tempBuffer[] = new byte [10000];
				public void run(){
					byteArrayOutputStream = new ByteArrayOutputStream();
					try{
						//capture audio till enter is typed
						while(!scnr.hasNext()){
							int cnt = inputLine.read(tempBuffer, 0, tempBuffer.length);
							if(cnt > 0)
								byteArrayOutputStream.write(tempBuffer, 0, cnt);
						}
					}catch(Exception e){
						System.out.println(e);
						System.exit(0);
					}
				}
				
				@Override
				public void start(){
					run();
					super.start();
				}
			};
			captureThread.start();
		}catch(Exception e){
			System.out.println(e);
			System.exit(0);
		}
		
	}
}
