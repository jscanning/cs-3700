package homework.hw3.orderedLeaderElection;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class LeaderElectionTest {
		static int numberOfOfficials = 2;
	
	public static void main(String[] args) {
		Random random = new Random();
		ElectedOfficialThread[] officials = new ElectedOfficialThread[numberOfOfficials];
		rankThread ranker = new rankThread(officials, random);
		CyclicBarrier barrier = new CyclicBarrier(numberOfOfficials - 1);
		
		for (int i = 0; i < officials.length; i++) {
			officials[i] = new ElectedOfficialThread("Official "+i, random, ranker, barrier);
		}
		
		ranker.start();
		
		try {
			ranker.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
