package waveProcess;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class WaveFileWriter {
	public static void write(Wave wave, File file){
		AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(WaveFinisher.finish(wave, true)),
													wave.getFormat(),
													wave.getLength());
		try {
			AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
