package project.proj1.tests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import project.proj1.Decoder;
import project.proj1.Encoder;
import project.proj1.HuffmanInterface;

public class SequentialHuffmanTest {

	public static void main(String[] args) throws IOException {
		Path usConst = Paths.get("src/project/proj1/USConstitution.txt");
		long initfileSize = Files.size(usConst);
		System.out.println(usConst.toAbsolutePath());
		
		List<String> lines = null;
		try {
			lines = Files.readAllLines(usConst);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(lines == null){
			System.err.println("Lines == null");
		}
		ArrayList<String> src = (ArrayList<String>) lines;
		
		Path out = Paths.get("src/project/proj1/output");
		
		System.out.println(out.toAbsolutePath());
		
		Encoder encoder = new Encoder(src, out.toString(), 0);
		
		long startTime = System.currentTimeMillis();
		encoder.run();
		long endTime = System.currentTimeMillis();
		long encodingTime = endTime - startTime;
		
		Path compressedOutput = Paths.get(String.format(out.toString()+ HuffmanInterface.compressedFileExtension, encoder.getPartIndex()));
	
		Decoder decoder = new Decoder(out.toString(), 0);
		
		startTime = System.currentTimeMillis();
		decoder.run();
		endTime = System.currentTimeMillis();
		long decodingTime = endTime - startTime;
		
		long resultFileSize = Files.size(compressedOutput);
		long decrease = initfileSize - resultFileSize;
		double percentDecrease = ((double)decrease / initfileSize * 100);
		
		System.out.println("Total Encoding time: " + encodingTime + " milliseconds");
		System.out.println("Total Decoding time: " + decodingTime + " milliseconds");
		System.out.println("Total Run Time (including file I/O): " + (encodingTime + decodingTime) + " milliseconds");
		System.out.println("Initial file size: " + initfileSize + " bytes");
		System.out.println("Post-Huffman size: " + resultFileSize + " bytes");
		System.out.println((int)percentDecrease + "% Decrease");
	}

}
