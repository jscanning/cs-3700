package project.proj1.tests;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import project.proj1.Encoder;
import project.proj1.HuffmanInterface;

public class ParallelHuffmanTest {
	//C:\Users\Jeremy Canning\git\cs-3700\cs-3700\src\project\proj1

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
		
		Path compressedOutput = Paths.get(String.format(out.toString()+ HuffmanInterface.treeFileExtension, encoder.getPartIndex()));
		
		long resultFileSize = Files.size(compressedOutput);
		long decrease = initfileSize - resultFileSize;
		long percentDecrease = (long)((double)decrease / initfileSize * 100);
		
		System.out.println("Total Run Time (including file I/O): " + (endTime - startTime) + " milliseconds");
		System.out.println("Initial file size: " + initfileSize + " bytes");
		System.out.println("File size after Huffman coding: " + resultFileSize + " bytes");
		System.out.println(percentDecrease + "% Decrease");
	}

}
