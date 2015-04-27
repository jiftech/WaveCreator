package waveProcess;

import java.util.Random;
/**
 * WaveGenerator provides several methods that generate a PCM wave data.
 * @author Takumi Fujiwara
 *
 */
public class WaveGenerator {
	private static int reg = 0x8000;
	/**
	 * Generate sine wave.
	 * @param a Amplitude
	 * @param f Frequency of wave
	 * @param fs Sampling rate
	 * @param l Length of wave (second)
	 * @return PCM wave data
	 */
	public static double[] Sine(double a, double f, double fs, double l){
		double[] s = new double[(int)(fs * l)];

		for(int n = 0; n < s.length; n++){
			s[n] = a * Math.sin(2 * Math.PI * f * n / fs);
		}
		return s;
	}

	/**
	 * Generate PSG sawtooth wave.
	 * @param a Amplitude
	 * @param f Frequency of wave
	 * @param fs Sampling rate
	 * @param l Length of wave (second)
	 * @return PCM wave data
	 */
	public static double[] PSGSaw(double a, double f, double fs, double l){
		double[] s = new double[(int)(fs * l)];
		double t = fs / f;

		for(int n = 0; n < s.length; n++){
			s[n] = 2.0 * a *((double)n/t - Math.floor((double)n/t + 0.5));
		}
		return s;
	}

	public static double[] pseudoSaw(double a, double f, double fs, double l, int step){
		double[] s = new double[(int)(fs * l)];
		double t = fs / f;

		for(int n = 0; n < s.length; n++){
			double p = n % t;
			int stepPhase = (int)(p * (2*step + 1) / t) + 1;
			s[n] = a * ((double)((step + 1) - stepPhase) / step);
		}
		return s;
	}

	/**
	 * Generate PSG square wave.
	 * @param a Amplitude
	 * @param f Frequency of wave
	 * @param fs Sampling rate
	 * @param l Length of wave (second)
	 * @return PCM wave data
	 */
	public static double[] PSGSquare(double a, double f, double fs, double l){
		double[] s = new double[(int)(fs * l)];
		double t = fs / f;

		for(int n = 0; n < s.length; n++){
			if (((double)n/t - Math.floor((double)n/t)) < 0.5){
				s[n] = a * 1.0;
			}
			else{
				s[n] = a * -1.0;
			}
		}
		return s;
	}

	/**
	 * Generate PSG Triangle wave.
	 * @param a Amplitude
	 * @param f Frequency of wave
	 * @param fs Sampling rate
	 * @param l Length of wave (second)
	 * @return PCM wave data
	 */
	public static double[] PSGTriangle(double a, double f, double fs, double l){
		double[] s = new double[(int)(fs * l)];
		double t = fs / f;

		for(int n = 0; n < s.length; n++){
			double p = n % t;
			if (p < t/2){
				s[n] = a * (-1.0 + 4.0*p / t);
			}
			else{
				s[n] = a * (3.0 - 4.0*p / t);
			}
		}
		return s;
	}

	/**
	 * Generate Pseudo Triangle wave.
	 */
	public static double[] pseudoTriangle(double a, double f, double fs, double l, int step){
		double[] s = new double[(int)(fs * l)];
		double t = fs / f;

		for(int n = 0; n < s.length; n++){
			double p = n % t;
			int stepPhase = (int)(p * 4 * step / t) + 1;
			if (stepPhase <= 2*step){
				s[n] = a * ((double)(-step + stepPhase) / step);
			}
			else{
				s[n] = a * ((double)(3*step - stepPhase) / step);
			}
		}
		return s;
	}

	/**
	 * Generate White noise.
	 * @param a Amplitude
	 * @param fs Sampling rate
	 * @param l Length of wave (second)
	 * @return PCM wave data
	 */
	public static double[] WhiteNoise(double a, double fs, double l){
		double[] s = new double[(int)(fs * l)];
		Random rand = new Random(System.currentTimeMillis());

		for(int n = 0; n < s.length; n++){
			s[n] = a * (rand.nextDouble() * 2 - 1);
		}

		return s;
	}



	public static double[] FCNoise(double a, double f, double fs, double l, FCNoiseType type) {
		double[] s = new double[(int)(fs * l)];
		int t = (int)(fs / f);
		int cnt = 0;
		double output = 0.0;

		for(int n = 0; n < s.length; n++){
			if(cnt >= t){
				cnt = 0;
				reg >>= 1;
				reg |= ((reg ^ (reg >> type.getValue())) & 1) << 15;
				output = reg & 1;
			}
			s[n] = a *(output * 2 - 1);
			cnt++;
		}
		return s;
	}
}
