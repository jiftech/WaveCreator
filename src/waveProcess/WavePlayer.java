package waveProcess;

import java.io.ByteArrayInputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class WavePlayer {
	public static void play(Wave wave){
		try{
			AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(WaveFinisher.finish(wave, true)),
													wave.getFormat(),
													wave.getLength());

			DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat());
			Clip clip = (Clip)AudioSystem.getLine(info);
			clip.open(ais);
			clip.start();

		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
