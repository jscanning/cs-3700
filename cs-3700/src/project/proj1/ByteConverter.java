package project.proj1;

import java.util.BitSet;

public class ByteConverter {

	public static BitSet fromByteArray(byte[] bytes) {
		BitSet bits = new BitSet();
		for (int i = 0; i < bytes.length*8; i++) {
			if ((bytes[bytes.length-i/8-1]&(1<<(i%8))) > 0) {
				bits.set(i);
			}
		}
		return bits;
	}
	
	public static byte[] toByteArray(String str) {
		BitSet b = new BitSet();
		char[] charArray = str.toCharArray();
		for (int bitIndex = 0; bitIndex < charArray.length; bitIndex++) {
			//charArray.length- bitIndex-1
			if (charArray[bitIndex] == '1') {
				b.set(bitIndex);
			}
		}
		byte[] converted = toByteArray(b);
		return converted;
	}
	
	public static byte[] toByteArray(BitSet bits) {
	    byte[] bytes = new byte[bits.length()/8+1];
	    for (int i=0; i<bits.length(); i++) {
	        if (bits.get(i)) {
	            bytes[bytes.length-i/8-1] |= 1<<(i%8);
	        }
	    }
	    return bytes;
	}

}
