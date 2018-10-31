package homework.hw5;


import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.*;

import homework.hw5.support.Buffer;
import homework.hw5.support.Consumer;
import homework.hw5.support.Producer;

public class ProdConWithLocks {
	
	static class LockBuffer implements Buffer {
		private final Random rand = new Random();
		private final int CAPACITY = 10;
		private final Queue<Integer> queue = new LinkedList<>();
		
		private final Lock bufLock = new ReentrantLock();
		private final Condition bufferNotFull = bufLock.newCondition();
		private final Condition bufferNotEmpty = bufLock.newCondition();
		private boolean done = false;
		
		public void setDone() {
			done = !done;
		}
		
		public void put() throws InterruptedException {
			bufLock.lock();
			try {
				while(queue.size() == CAPACITY) {
					System.out.println(Thread.currentThread().getName() + ": Buffer is full, waiting...");
					bufferNotEmpty.await();
				}
				
				int item = rand.nextInt();
				boolean isAdded = queue.offer(item);
				if(isAdded) {
					System.out.printf("%s: added %d into queue %n", Thread.currentThread().getName(), item);
					System.out.println(Thread.currentThread().getName() + ": Signalling that buffer isn't empty");
					bufferNotFull.signalAll();
				}
			}finally {
				bufLock.unlock();
			}
		}
		
		public void get() throws InterruptedException {
			bufLock.lock();
			try {
				while(queue.size() == 0) {
					System.out.println(Thread.currentThread().getName() + ": Buffer is empty, waiting...");
					bufferNotFull.await();
				}
				Object item = queue.poll();
				if(item != null) {
					System.out.printf("%s: consumed %d from queue %n", Thread.currentThread().getName(), item);
					System.out.println(Thread.currentThread().getName() + ": Signalling that buffer might not be full");
					bufferNotEmpty.signalAll();
				}
			}finally {
				bufLock.unlock();
				Thread.sleep(1000);
			}
		}
		
		@Override
		public void continuousGet() throws InterruptedException{
			while(!done) {
				get();
			}
		}

		@Override
		public void continuousPut(int amount) throws InterruptedException {
			while(amount > 0) {
					put();
					amount -= 1;
			}
		}
	}
	
	public static void test(int producers, int consumers) {
		Buffer sharedBuff = new LockBuffer();
		
		Producer p = new Producer(sharedBuff);
		Consumer c = new Consumer(sharedBuff);
		
		Thread[] producerThreads = new Thread[producers];
		Thread[] consumerThreads = new Thread[consumers]; //(c, "CONSUMER");
		
		for (int i = 0; i < producerThreads.length; i++) {
			producerThreads[i] = new Thread(p, String.format("PRODUCER-%d", i));
			producerThreads[i].start();
		}
		
		for(int j = 0; j < consumerThreads.length; j++){
			consumerThreads[j] = new Thread(c, String.format("CONSUMER-%d", j));
			consumerThreads[j].start();
		}
		
		try{
			for(int i = 0; i < producerThreads.length; i++){
				producerThreads[i].join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			((LockBuffer) sharedBuff).setDone();
		}
	}
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		test(2, 5); // 37033 milliseconds
		long endTime = System.currentTimeMillis();
		long p2c5time = endTime - startTime;
		System.out.println("2 Producers and 5 Consumers run time = " + p2c5time + " milliseconds");
		
		startTime = System.currentTimeMillis();
		test(5, 2);
		endTime = System.currentTimeMillis();
		long p5c2time = endTime - startTime;
		System.out.println("5 Producers and 2 Consumers run time = " + p5c2time + " milliseconds");
		System.out.println("2 Producers and 5 Consumers run time = " + p2c5time + " milliseconds");
		System.out.println("Total time: " + (p5c2time + p2c5time));
		System.out.println("Time difference: " + (p5c2time - p2c5time));
		
	}
	
}
