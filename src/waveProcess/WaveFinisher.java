package waveProcess;


/**
 * WaveFinisher finishes("SHIAGERU") Wave when it is played, or written to file.
 * @author jiftech
 *
 */
public class WaveFinisher {
	public static byte[] finish(Wave w, boolean clickNoiseCancel){
		// if "clickNoiseCancel" is true, apply clickNoiseCanceler to Wave.
		if (clickNoiseCancel)
			clickNoiseCanceler(w, 0.05);

		// if sampling-bit is 16
		// first, convert double data into short data.
		// next, convert short array to byte array(little endian).
		if (w.getFormat().getSampleSizeInBits() == 16){
			int length = w.getLength();
			double[] s = w.getSoundData();
			short[] shortSound = new short[length];
			for(int n = 0; n < length; n++){
				shortSound[n] = (short)(s[n] * 32767);
			}
			return shortArrayTolittleEndianByteArray(shortSound);
		}
		// other sampling-bits are not supported yet...
		else{
			return null;
		}
	}

	private static void clickNoiseCanceler(Wave w, double fadeLength){
		float fs = w.getFormat().getSampleRate();
		int length = w.getSoundData().length;
		int fadeFrame = (int)(fs * fadeLength);
		double[] fade = new double[length];
		double[] newSoundData = new double[length];

		for(int n = 0; n < length; n++){
			if(n < fadeFrame){
				fade[n] = (1.0 / (fs * 0.05)) * n;
			}
			else if(n > length - fadeFrame){
				fade[n] = 1.0 - (1.0 / (fs * 0.05)) * (n - length + fs * 0.05);
			}
			else{
				fade[n] = 1.0;
			}
		}
		for(int n = 0; n < length; n++){
			newSoundData[n] = w.getSoundData()[n] * fade[n];
		}
		w.updateSoundData(newSoundData);
	}

	private static byte[] shortArrayTolittleEndianByteArray(short[] data){
		byte[] byteData = new byte[data.length*2];

		for(int i = 0; i < data.length; i++){
			byteData[i*2]     = (byte)( data[i] & 0x00ff);
			byteData[i*2 + 1] = (byte)((data[i] & 0xff00) >> 8);
		}
		return byteData;
	}
}
