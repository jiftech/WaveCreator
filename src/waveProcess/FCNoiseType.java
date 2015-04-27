/**
 * Define FCNoiseType. Associated value is used when generating.
 */
package waveProcess;

public enum FCNoiseType {
	LONG_FREQ(1), SHORT_FREQ(6);

	private int value;

	private FCNoiseType (int n){
		this.value = n;
	}

	public int getValue(){
		return this.value;
	}
}
