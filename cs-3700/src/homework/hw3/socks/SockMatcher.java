package homework.hw3.socks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import homework.hw3.socks.Sock.color;


public class SockMatcher{
	CyclicBarrier barrier = new CyclicBarrier(4);
	List<Sock> socks = new ArrayList<Sock>(200);
	Random rand = new Random();
	int totalSocks = 0;
	
	public void engage(){
		int numberOfThreads = 5;
		SockFactoryThread[] threads = new SockFactoryThread[numberOfThreads -1];
		threads[0] = new SockFactoryThread(color.red, rand, socks);
		threads[1] = new SockFactoryThread(color.green, rand, socks);
		threads[2] = new SockFactoryThread(color.blue, rand, socks);
		threads[3] = new SockFactoryThread(color.orange, rand, socks);
		
		for (SockFactoryThread sockFactoryThread : threads) {
			sockFactoryThread.start();
		}
		for (SockFactoryThread sockFactoryThread : threads) {
			try {
				sockFactoryThread.join();
			} catch (InterruptedException e) {
				throw new RuntimeException("A sock factory thread interrupted.");
			}
		}
		System.out.println("Total Socks: " + totalSocks);
		match();
	}
	
	private void match()
	{
		Objects.requireNonNull(socks, "List of socks is null.");
		List<Sock> socksToWash = new ArrayList<Sock>();
		int pairCount = 0;
		for (int i = 0; i < socks.size(); i++) {
			Sock s1 = socks.get(i);
			for (int j = 0; j < socks.size(); j++) {
				Sock s2 = socks.get(j);
				if(s1.equals(s2)){continue;} // skip if these are the same sock
				else if(s1.getMyColor().compareTo(s2.getMyColor()) == 0){ // if these socks match
					if(!socksToWash.contains(s1) && !socksToWash.contains(s2)){
						pairCount += 1;
						socksToWash.add(s1);
						socksToWash.add(s2);
						break;
					}else if(socksToWash.contains(s1)){
						break;
					}
				}
			}
		}
		System.out.println("Matcher: Sending "+pairCount+" pairs of socks to washer. Total Socks Sent: " + pairCount * 2);
		System.out.println("Socks leftover: " + (totalSocks - pairCount*2));
		new WasherThread(socksToWash, socks).run();
	}
	
	class WasherThread extends Thread {
		List<Sock> socksToWash;
		List<Sock> socks;
		WasherThread(List<Sock> socksToWash, List<Sock> socks){
			this.socks = socks;
			this.socksToWash = socksToWash;
		}
		@Override
		public void run() {
			int count = 0;
			for (Sock sock : socksToWash) {
				socks.remove(sock);
				count++;
			}
			System.out.println("Washer: Destroyed " + count+ " Socks");
		}
		
	}

	class SockFactoryThread extends Thread {
		color sockColor;
		Random rand;
		List<Sock> socks;
		
		SockFactoryThread(color c, Random rand, List<Sock> socks){
			this.sockColor = c;
			this.rand = rand;
			this.socks = socks;
		}

		@Override
		public synchronized void run(){
			int numberOfSocks = rand.nextInt(100) + 1;
			int counter = 0;
			for (int i = 0; i < numberOfSocks; i++) {
				socks.add(new Sock(sockColor));
				counter++;
			}
			
			try {
				barrier.await();
				totalSocks += counter;
			} catch (InterruptedException | BrokenBarrierException e) {
				e.printStackTrace();
			}
			System.out.println(sockColor.name() +" Sock Factory: " + counter + " Socks Produced. " + counter % 2 + " singleton sock.");
		}
	}
	
	public static void main(String[] args) {
		SockMatcher matcher = new SockMatcher();
		matcher.engage();

	}
}


