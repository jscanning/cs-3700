package project.proj1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ThreadedEncoder extends HuffmanInterface {
	
	private int buffersPerThread;
	private Thread jobs[];
	private ArrayList<String> fileData;
	private String outputDestination;
	
	public ThreadedEncoder(String path, Integer maxTasks, Boolean isQuiet) {
		super(path, maxTasks, isQuiet);
		this.setOutputDestination(filepath);
		fileData = new ArrayList<>();
		try{
			readFile();
		}catch (IOException e){
			System.err.println("Reading file failed.");
		}
	}

	public String getOutputDestination() {
		return outputDestination;
	}

	public void setOutputDestination(String outputDestination) {
		this.outputDestination = outputDestination;
	}

	private void readFile() throws IOException{
		if(!isQuiet)
			logger.info("Started reading the file.");
		
		File sourceFile = new File(filepath);
		
		//BufferedReader reader = null;
		try(BufferedReader fileReader = new BufferedReader(new FileReader(sourceFile)))
		{
			String sCurrentLine;
			while((sCurrentLine = fileReader.readLine()) != null){
				fileData.add(sCurrentLine);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		initializeThreadParameters(fileData.size(), maxTasksCount);
		
		if(!isQuiet)
			logger.info("Finished reading the file.");
	}

	private void initializeThreadParameters(int fileLines, Integer threadsCount) {
		if(threadsCount > 1){
			buffersPerThread = (fileLines / (threadsCount - 1));
		}else{
			buffersPerThread = 0;
		}
		jobs = new Thread[threadsCount];
	}
	
	private ArrayList<String> readFilePart(int seekToBuffer, int reqBufferCount){
		ArrayList<String> result = new ArrayList<>();
		for (int i = seekToBuffer; i < seekToBuffer + reqBufferCount; i++) {
			result.add(fileData.get(i));
		}
		return result;
	}
	
	private void createThread(int seekToBuffer, int reqBufferCount, int threadIndex){
		ArrayList<String> rawData;
		rawData = readFilePart(seekToBuffer, reqBufferCount);
		Encoder encoder = new Encoder(rawData, outputDestination, threadIndex);
		jobs[threadIndex] = new Thread(encoder);
	}
	
	public void runThreads() throws InterruptedException{
		int seekToBuffer = 0;
		int reqBufferCount = buffersPerThread;
		if(!isQuiet)
			logger.info("Started reading file to compress.");
		
		for(int index = 0; index < (maxTasksCount - 1); index++){
			createThread(seekToBuffer, reqBufferCount, index);
			String logMessage = "Compressing thread " + (index) + " started";
			if(!isQuiet)
				logger.info(logMessage);
			seekToBuffer += buffersPerThread;
		}
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < (maxTasksCount - 1); i++) {
			jobs[i].start();
		}
		//File file = new File(filepath);
		int lastThreadPos = buffersPerThread * (maxTasksCount -1);
		if(fileData.size() - lastThreadPos > 0){
			createThread(lastThreadPos, fileData.size()-lastThreadPos, maxTasksCount -1);
			String logMessage = "Compressing thread " + (maxTasksCount) + " started.";
			if(!isQuiet)
				logger.info(logMessage);
			jobs[maxTasksCount - 1].start();
		}else{
			// one thread is unneeded
			maxTasksCount--;
		}
		for(int i = 0; i < maxTasksCount; i++){
			jobs[i].join();
		}
		long endTime = System.currentTimeMillis();
		long executionTime = endTime - startTime;
		logger.info("Total compression time: " + executionTime);
		fileData.clear();
	}

}
