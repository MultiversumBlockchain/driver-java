package io.multiversum.db.executor.core.contracts;

import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.util.encoders.Hex;

public class Decoder {

	private static final int PADDING = 64;
	
	public static List<List<String>> decodeArrayOfArrayOfStrings(String data) {
		List<List<String>> result = new ArrayList<List<String>>();
		
		// Remove 0x prefix if present
		if (data.startsWith("0x")) {
			data = data.substring(2);
		}
		
		// Ignore first 32 bytes
		int offset = PADDING;

		// Decode array length
		final int arrayLength = Integer.decode("0x" + data.substring(offset, offset + PADDING));
		offset += PADDING;

		// Find and decode sub arrays offsets
		int subArrayOffset = offset;
		int[] subArrayOffsets = new int[arrayLength];
		for (int i = 0; i < arrayLength; i++) {
			subArrayOffsets[i] = Integer.decode("0x" + data.substring(subArrayOffset, subArrayOffset + PADDING)) * 2;
			
			subArrayOffset += PADDING;
		}
		
		for (int i = 0; i < arrayLength; i++) {
			// Calculate sub array offset
			int currentOffset = offset + subArrayOffsets[i];
			
			// Add sub array decoded values to result
			result.add(decodeArrayOfStrings(data.substring(currentOffset)));
		}

		return result;
	}
	
	public static List<String> decodeArrayOfStrings(String data) {
		List<String> result = new ArrayList<String>();
		
		// Remove 0x prefix if present
		if (data.startsWith("0x")) {
			data = data.substring(2);
		}
		
		int offset = 0;
		
		// Decode array length
		final int arrayLength = Integer.decode("0x" + data.substring(offset, offset + PADDING));
		offset += PADDING;
		
		// Find and decode value offsets
		int valuesOffset = offset;
		int[] valuesOffsets = new int[arrayLength];
		for (int i = 0; i < arrayLength; i++) {
			valuesOffsets[i] = Integer.decode("0x" + data.substring(valuesOffset, valuesOffset + PADDING)) * 2;
			
			valuesOffset += PADDING;
		}
		
		for (int i = 0; i < arrayLength; i++) {
			// Calculate current row offset
			int currentOffset = offset + valuesOffsets[i];
			// Decode current row byte size
			int valueSize = Integer.decode("0x" + data.substring(currentOffset, currentOffset + PADDING));
			
			currentOffset += PADDING;
			
			// Result hex string
			String value = "";
			// Get the number of rows to parse
			int valueRows = (int) Math.ceil(valueSize / 32.0d);
			for (int j = 0; j < valueRows; j++) {
				// Append parsed values to the result string
				value += data.substring(currentOffset, currentOffset + PADDING);
				
				currentOffset += PADDING;
			}
			
			// Decode string and append to result array
			result.add(new String(Hex.decode(value)).trim());
		}
		
		return result;
	}
	
}
