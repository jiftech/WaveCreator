package waveProcess;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioFileFormatChecker {

	public static void main(String[] args) {
		AudioInputStream ais;
		File file  = new File("sine2.wav");
		try {
			ais = AudioSystem.getAudioInputStream(file);
			AudioFormat fmt = ais.getFormat();
			System.out.println(ais.getFrameLength());
			System.out.println(fmt.isBigEndian());
			System.out.println(fmt.getChannels());
			System.out.println(fmt.getEncoding());
			System.out.println(fmt.getFrameRate());
			System.out.println(fmt.getFrameSize());
			System.out.println(fmt.getSampleRate());
			System.out.println(fmt.getSampleSizeInBits());
			ais.close();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
