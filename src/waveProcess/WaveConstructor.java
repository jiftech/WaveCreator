package waveProcess;

import javax.sound.sampled.AudioFormat;

import application.InputWaveProperty;

public class WaveConstructor {
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
			if(FCNoiseFreq.equals("’·ŽüŠú"))
				noiseType = FCNoiseType.LONG_FREQ;
			else if(FCNoiseFreq.equals("’ZŽüŠú"))
				noiseType = FCNoiseType.SHORT_FREQ;
			else{
				noiseType = null;
				System.out.println("hoge");
			}
			wave = new Wave(WaveGenerator.FCNoise(amp, freq * FCNOISE_FREQ_MULTIPLYER, SAMPLING_RATE, length, noiseType),
				    	new AudioFormat((float)SAMPLING_RATE, SAMPLING_BITS, 1, true, false));
			break;
		default:
			wave = null;
			System.out.println("fuga");
			break;
		}
		return wave;
	}
}
