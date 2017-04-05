package main;

import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
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
	public Reciever(float inputBR){
		format = new AudioFormat(inputBR, 16, 1, true, true);
		lines = AudioSystem.getMixerInfo();
		inInfo = new DataLine.Info(TargetDataLine.class, format);
		bufferSize = (int) (format.getSampleSizeInBits() * format.getFrameRate());
	}
	
	/**
	 * opens up a stream to read from the audio source
	 */
	public void recieve(){
		try{
		      inputLine = (TargetDataLine)AudioSystem.getMixer(lines[line]).getLine(inInfo);
		      inputLine.open(format, bufferSize);
		      inputLine.start(); 

		      byte[] buffer = new byte[bufferSize];

		      System.out.println("Listening on line " +line+", " + lines[line].getName() + "...");

		      while(true){
		        inputLine.read(buffer,0,buffer.length);
		        System.out.println(buffer);
		        
		      }
		    }catch (LineUnavailableException e){
		      System.out.println("Line " + line + " is unavailable.");
		      e.printStackTrace();
		      System.exit(1);
		    }
	}
}
