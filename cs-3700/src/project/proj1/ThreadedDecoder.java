package project.proj1;

public class ThreadedDecoder extends HuffmanInterface {
	
	/**
	 * 
	 */
	private String outputDestination;
	private Thread jobs[];
	
	public ThreadedDecoder(String path, Integer maxTasks, Boolean isQuiet) {
		super(path, maxTasks, isQuiet);
		this.setOutputDestination(filepath + ".decompressed");
	}

	public String getOutputDestination() {
		return outputDestination;
	}

	public void setOutputDestination(String outputDestination) {
		this.outputDestination = outputDestination;
	}
	
	public void runThreads(){
		jobs = new Thread[maxTasksCount];
		for(int threadIndex = 0; threadIndex < maxTasksCount -1; threadIndex++){
			Decoder decoder;
			decoder = new Decoder(filepath, threadIndex);
			jobs[threadIndex] = new Thread(decoder);
			jobs[threadIndex].run();
		}
		
		for(int threadIndex = 0; threadIndex < maxTasksCount - 1; threadIndex++){
			try{
				jobs[threadIndex].join();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
}
