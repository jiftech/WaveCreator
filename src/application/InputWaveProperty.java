/**
 * InputWaveProperty is an aggregation of wave property input with screen.
 */
package application;

public class InputWaveProperty {
	private String waveType;
	private double baseFreq;
	private int    tone;
	private double length;
	private double amp;
	private int    stepNum;
	private String FCNoiseFreq;



	InputWaveProperty(String waveType, double baseFreq, int tone, double length,
			double amp, int stepNum, String fCNoiseFreq) {
		this.waveType = waveType;
		this.baseFreq = baseFreq;
		this.tone = tone;
		this.length = length;
		this.amp = amp;
		this.stepNum = stepNum;
		FCNoiseFreq = fCNoiseFreq;
	}



	public String getWaveType() {
		return waveType;
	}



	public double getBaseFreq() {
		return baseFreq;
	}



	public int getTone() {
		return tone;
	}



	public double getLength() {
		return length;
	}



	public double getAmp() {
		return amp;
	}



	public int getStepNum() {
		return stepNum;
	}



	public String getFCNoiseFreq() {
		return FCNoiseFreq;
	}


}
