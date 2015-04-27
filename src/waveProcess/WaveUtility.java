package waveProcess;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import application.InputWaveProperty;

public class WaveUtility {
	private static final double SAMPLING_RATE = 44100.0;
	private static final int    SAMPLING_BITS = 16;
	private static final double FCNOISE_FREQ_MULTIPLYER = 1789772.5 / SAMPLING_RATE;

	public static Wave construct(InputWaveProperty property){
		Wave   wave;

		String waveType    = property.getWaveType();
		double baseFreq    = property.getBaseFreq();
		int    tone        = property.getTone();
		double length      = property.getLength();
		double amp         = property.getAmp();
		int    step        = property.getStepNum();
		String FCNoiseFreq = property.getFCNoiseFreq();

		double  freq = baseFreq * Math.pow(2.0, tone / 12.0);
		FCNoiseType noiseType;

		switch (waveType) {
		case "sine":
			wave = new Wave(WaveGenerator.Sine(amp, freq, SAMPLING_RATE, length),
						    new AudioFormat((float)SAMPLING_RATE, SAMPLING_BITS, 1, true, false));
			break;
		case "square":
			wave = new Wave(WaveGenerator.PSGSquare(amp, freq, SAMPLING_RATE, length),
				    		new AudioFormat((float)SAMPLING_RATE, SAMPLING_BITS, 1, true, false));
			break;
		case "triangle":
			wave = new Wave(WaveGenerator.PSGTriangle(amp, freq, SAMPLING_RATE, length),
				    		new AudioFormat((float)SAMPLING_RATE, SAMPLING_BITS, 1, true, false));
			break;
		case "pTriangle":
			wave = new Wave(WaveGenerator.pseudoTriangle(amp, freq, SAMPLING_RATE, length, step),
							new AudioFormat((float)SAMPLING_RATE, SAMPLING_BITS, 1, true, false));
			break;
		case "saw":
			wave = new Wave(WaveGenerator.PSGSaw(amp, freq, SAMPLING_RATE, length),
				    		new AudioFormat((float)SAMPLING_RATE, SAMPLING_BITS, 1, true, false));
			break;
		case "pSaw":
			wave = new Wave(WaveGenerator.pseudoSaw(amp, freq, SAMPLING_RATE, length, step),
				    		new AudioFormat((float)SAMPLING_RATE, SAMPLING_BITS, 1, true, false));
			break;
		case "whiteNoise":
			wave = new Wave(WaveGenerator.WhiteNoise(amp, SAMPLING_RATE, length),
				    		new AudioFormat((float)SAMPLING_RATE, SAMPLING_BITS, 1, true, false));
			break;
		case "FCNoise":
			if(FCNoiseFreq.equals("長周期"))
				noiseType = FCNoiseType.LONG_FREQ;
			else if(FCNoiseFreq.equals("短周期"))
				noiseType = FCNoiseType.SHORT_FREQ;
			else{
				noiseType = null;
			}
			wave = new Wave(WaveGenerator.FCNoise(amp, freq * FCNOISE_FREQ_MULTIPLYER, SAMPLING_RATE, length, noiseType),
				    	new AudioFormat((float)SAMPLING_RATE, SAMPLING_BITS, 1, true, false));
			break;
		default:
			wave = null;
			break;
		}
		return wave;
	}

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

	public static void fileWrite(Wave wave, File file){
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
