package project.proj1.tests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import project.proj1.ThreadedDecoder;
import project.proj1.ThreadedEncoder;

public class ParallelHuffmanTest {
	//C:\Users\Jeremy Canning\git\cs-3700\cs-3700\src\project\proj1

	static boolean isQuiet = false;
	
	public static void main(String[] args) throws IOException {
		System.out.println("Do you want to decode as well as encode? Y/N");
		Scanner kb = new Scanner(System.in);
		char boolInput = kb.nextLine().charAt(0);
		while (boolInput != 'Y' && boolInput != 'y' && boolInput != 'n' && boolInput != 'N') {
			System.out.println("Invalid input. Choose (Y)es or (N)o.");
			boolInput=kb.nextLine().charAt(0);
		}
		boolean doDecode;
		if(boolInput == 'y' || boolInput == 'Y')
			doDecode = true;
		else
			doDecode = false;
		System.out.println("Enter desired maximum number of threads to use: (between 1 and 10)");
			int numInput = kb.nextInt();
		if(Integer.max(numInput, 10) == numInput) {
			numInput = 10;
			System.out.println("Input over maximum: Defaulting to 10");
		}
		else if(Integer.min(numInput, 1) == numInput) {
			numInput = 1;
			System.out.println("Input over minimum: Defaulting to 1");
		}
		kb.close();
		
		int simultaeneousTasksAllowed = numInput;
		long decodingTime = 0, encodingTime = 0, startTime, endTime, initFileSize;
		
		Path usConst = Paths.get("src/project/proj1/USConstitution.txt");
		initFileSize = Files.size(usConst);
		System.out.println(usConst.toAbsolutePath());
				
		ThreadedEncoder encoder = new ThreadedEncoder(usConst.toString(), simultaeneousTasksAllowed, isQuiet);
		ThreadedDecoder decoder = new ThreadedDecoder(usConst.toString(), simultaeneousTasksAllowed, isQuiet);
		
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
		System.out.println("Initial file size: " + initFileSize + " bytes");
		
	}

}
