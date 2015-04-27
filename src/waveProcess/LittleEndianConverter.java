package waveProcess;

public class LittleEndianConverter {
	public static byte[] fromShort(short data){
		byte[] byteData = new byte[2];

		byteData[0] = (byte)( data & 0x00ff);
		byteData[1] = (byte)((data & 0xff00) >> 8);

		return byteData;
	}

	public static byte[] fromShortArray(short[] data){
		byte[] byteData = new byte[data.length*2];

		for(int i = 0; i < data.length; i++){
			byteData[i*2]     = (byte)( data[i] & 0x00ff);
			byteData[i*2 + 1] = (byte)((data[i] & 0xff00) >> 8);
		}
		return byteData;
	}

	public static byte[] fromInt(int data){
		byte[] byteData = new byte[4];

		byteData[0] = (byte)( data & 0x000000ff);
		byteData[1] = (byte)((data & 0x0000ff00) >> 8);
		byteData[2] = (byte)((data & 0x00ff0000) >> 16);
		byteData[3] = (byte)((data & 0xff000000) >> 24);

		return byteData;
	}
}
