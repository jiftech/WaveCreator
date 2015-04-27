package waveProcess;
import javax.sound.sampled.AudioFormat;


public class Wave {
	private double[] s;
	private AudioFormat format;

	Wave(double[] soundData, AudioFormat format){
		this.s = soundData;
		this.format = format;
	}

	double[] getSoundData(){
		return s;
	}

	AudioFormat getFormat(){
		return format;
	}

	int getLength(){
		return s.length;
	}

	void updateSoundData(double[] newSoundData){
		this.s = newSoundData;
	}
}