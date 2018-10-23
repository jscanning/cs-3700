package project.proj1;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class Encoder implements Runnable{
	final static Logger logger = Logger.getLogger(Encoder.class.getName());
	private Tree huffmanTree;
	private ArrayList<String> fileData;
	private String outputDestination;
	private Integer partIndex;
	private static volatile long freqTableConstructionTime = 0;
	
	public Encoder(ArrayList<String> sourceData, String path, Integer partIndex){
		this.fileData = sourceData;
		this.huffmanTree = new Tree();
		this.outputDestination = path;
		this.setPartIndex(partIndex);
	}
	
	public String encode(){
		logger.info(Thread.currentThread().getName() + " started generating tree data.");
		
		long startTime = System.currentTimeMillis();
		long nanoStartTime = System.nanoTime();
		HashMap<Character, Integer> freqMap = buildFrequencyMap(fileData);
		huffmanTree.buildTree(freqMap);
		long nanoEndTime = System.nanoTime();
		long endTime = System.currentTimeMillis();
		
		logger.info(Thread.currentThread().getName() + " generated tree in " + (endTime - startTime) 
				+ " milliseconds OR " + (nanoEndTime - nanoStartTime) + " nanoseconds.");
		
		String compressed = compressData();
		fileData.clear();
		return compressed;
	}

	public Tree getTree(){
		return huffmanTree;
	}
	
	private HashMap<Character, Integer> addFreqFromBuffer(char[] buffer, HashMap<Character, Integer> map){
		for (char c : buffer) {
			if(!map.containsKey(c)){
				map.put(c, 1);
			}else{
				int x = map.get(c);
				map.put(c, x+1);
			}
		}
		return map;
	}
	
	private HashMap<Character, Integer> buildFrequencyMap(ArrayList<String> data) {
		HashMap<Character, Integer> freqMap = new HashMap<>();
		for(String buffer : data){
			freqMap = addFreqFromBuffer(buffer.toCharArray(), freqMap);
		}
		return freqMap;
	}
	
	private String compressData(){
		logger.info(Thread.currentThread().getName() + " started compressing.");
		long startTime = System.currentTimeMillis();
		long nanoStartTime = System.nanoTime();
		StringBuilder compressed = new StringBuilder();
		for(String str : fileData){
			compressed.append(compressString(str));
		}
		long nanoEndTime = System.nanoTime();
		long endTime = System.currentTimeMillis();
		logger.info(Thread.currentThread().getName() + " finished compressing in " + (endTime - startTime) 
				+ " milliseconds OR " + (nanoEndTime - nanoStartTime) + " nanoseconds.");
		return compressed.toString();
	}
	
	private String compressString(String str) {
		StringBuilder encodedString = new StringBuilder();
		for(char ch : str.toCharArray()){
			encodedString.append(huffmanTree.getCharCode(ch));
		}
		return encodedString.toString();
	}

	@Override
	public void run() {
		String encodedResult = encode();
		logger.info(Thread.currentThread().getName() + " will flush");
		flushContentsToFile(encodedResult);
	}
	
	private void flushContentsToFile(String encodedResult) {
		Path treeOutputPath = Paths.get(String.format(outputDestination + HuffmanInterface.treeFileExtension, this.getPartIndex()));
		Path compressedOutputPath = Paths.get(String.format(outputDestination + HuffmanInterface.compressedFileExtension, this.getPartIndex()));
		long fileSize = 0;
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(treeOutputPath.toString())));
			out.println(generateFileTree()); // output result
			out.close();

			FileOutputStream fos = new FileOutputStream(new File(compressedOutputPath.toString()));
			ByteArrayOutputStream binaryOutputStream = new ByteArrayOutputStream();
			byte[] fileData = ByteConverter.toByteArray(encodedResult);
			binaryOutputStream.write(fileData);
			// Put data
			binaryOutputStream.writeTo(fos);
			fos.close();
			fileSize = Files.size(compressedOutputPath);
		} catch (IOException e) {
			System.err.println("Thread failed to flush to file");
		}
		logger.info(Thread.currentThread().getName() + " finished flushing to destination:" 
				+ compressedOutputPath.toString() + " (compressed to: " + fileSize + " bytes)");
	}
	
	private String generateFileTree(){
		StringBuilder result = new StringBuilder();
		String tree = getTree().toString();
		result.append(tree);
		result.append('\n');
		return result.toString();
	}
	
	public static long getFreqTableConstructionTime(){
		return freqTableConstructionTime;
	}

	public Integer getPartIndex() {
		return partIndex;
	}

	public void setPartIndex(Integer partIndex) {
		this.partIndex = partIndex;
	}
	
}
