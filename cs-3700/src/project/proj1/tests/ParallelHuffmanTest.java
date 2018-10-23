package project.proj1.tests;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import project.proj1.ThreadedDecoder;
import project.proj1.ThreadedEncoder;

@SuppressWarnings("unused")
public class ParallelHuffmanTest {
	//C:\Users\Jeremy Canning\git\cs-3700\cs-3700\src\project\proj1

	static boolean isQuiet = false;
	
	public static void main(String[] args) throws IOException {
		boolean doDecode = true;		
		int simultaeneousTasksAllowed = 4;
		
		long decodingTime = 0, encodingTime = 0, startTime, endTime;
		
		Path usConst = Paths.get("src/project/proj1/USConstitution.txt");
		System.out.println(usConst.toAbsolutePath());
				
		ThreadedEncoder encoder = new ThreadedEncoder(usConst.toString(), simultaeneousTasksAllowed, isQuiet);
		ThreadedDecoder decoder = new ThreadedDecoder(usConst.toString(), simultaeneousTasksAllowed+1, isQuiet);
		
		startTime = System.currentTimeMillis();
		try {
			encoder.runThreads();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		endTime = System.currentTimeMillis();
		encodingTime = endTime - startTime;
		
		if(doDecode) {
			startTime = System.currentTimeMillis();
			decoder.runThreads();
			endTime = System.currentTimeMillis();
			decodingTime = endTime - startTime;
		}
		
		System.out.println("Encoding time: " + encodingTime + " milliseconds");
		if(doDecode) {System.out.println("Decoding time: " + decodingTime + " milliseconds");}
		System.out.println("Total Run Time (including file I/O): " + (encodingTime + decodingTime) + " milliseconds");
		//System.out.println("Initial file size: " + initFileSize + " bytes");
		
	}

}
