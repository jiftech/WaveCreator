package application;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class ToneName{
	public static final Map<Integer, String> TONE_NAME_MAP;
		static{
			HashMap<Integer, String> map = new HashMap<Integer, String>();
			map.put(0, "C");
			map.put(1, "C#");
			map.put(2, "D");
			map.put(3, "D#");
			map.put(4, "E");
			map.put(5, "F");
			map.put(6, "F#");
			map.put(7, "G");
			map.put(8, "G#");
			map.put(9, "A");
			map.put(10,"A#");
			map.put(11,"B");
			TONE_NAME_MAP = Collections.unmodifiableMap(map);
	}
}