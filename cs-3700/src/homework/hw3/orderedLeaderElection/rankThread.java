package homework.hw3.orderedLeaderElection;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class rankThread extends Thread {
	public ElectedOfficialThread[] officials = null;
	private static final int DEFAULT_NUMBER_OF_THREADS = 2;
	private boolean initialized = false, done = false;
	
	private int leaderIndex = 0, numOfficials;
	private Random rand;
	
	public rankThread(int threads, Random r){
		super();
		this.officials = new ElectedOfficialThread[threads];
		rand = r;
		numOfficials = threads;
	}
	
	public rankThread(ElectedOfficialThread[] threads, Random r){
		super();
		this.officials = threads;
		if(officials != null)
			initialized = true;
		rand = r;
		numOfficials = threads.length;
	}
	
	
	private int getNumOfficials() {
		
		return 0;
	}


	public rankThread() {
		super();
	}
	
	public void setThreadArray(ElectedOfficialThread[] threads) {
		officials = threads;
		if(officials != null)
			initialized = true; // assume inputted thread array is initialized
	}
	
	private void initializeThreads() {
		if(officials == null) {
			officials = new ElectedOfficialThread[DEFAULT_NUMBER_OF_THREADS];
		}
		
		int i = 0;
		for (ElectedOfficialThread eot : officials) {
			eot = new ElectedOfficialThread("Official "+i, rand, this, new CyclicBarrier(getNumOfficials()));
			i++;
		}
		initialized = true;
	}
	
	@Override
	public void run(){
		if(!initialized)
			initializeThreads();
		for (int j = 0; j < officials.length; j++) {
			officials[j].start();
		}
		int i = 1;
		while(!done) {
			if(interrupted()) {
				if(checkLeader()) {
					updateLeader(officials[leaderIndex]);
				}
				i++;
			}else if(i >= officials.length) {
				for (int j = 0; j < officials.length; j++) {
					officials[j].done();
				}
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
	public void done() {
		done = true;
	}

	private void updateLeader(ElectedOfficialThread newLeader) {
		//notifyAll();
		for (ElectedOfficialThread losers : officials) {
			losers.setLeader(newLeader.getMyName());
		}
	}

	private boolean checkLeader() {
		boolean result = false;
		int temp = 0;
		for(int i = 0; i < officials.length; i++) {
			if(officials[leaderIndex].getRank() <= officials[i].getRank()) {
				temp = i;
				result = true;
			}
		}
		leaderIndex = temp;
		return result;
	}

	
	
}
