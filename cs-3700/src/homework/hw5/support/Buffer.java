package homework.hw5.support;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public interface Buffer {
	final Random rand = new Random();
	final int CAPACITY = 10;
	final Queue<Integer> queue = new LinkedList<>();
	
	public default boolean isEmpty(){ return (queue.size() == 0); }
	public void put() throws InterruptedException;
	public void get() throws InterruptedException;
	public void continuousPut(int amount) throws InterruptedException;
	public void continuousGet() throws InterruptedException;
	public void setDone(boolean b);
	
	public default void test(int producers, int consumers) throws BufferException {
		this.setDone(false);
		Producer p = new Producer(this);
		Consumer c = new Consumer(this);
		
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
			System.out.println("Producers done!");
			while(!(this.isEmpty())){Thread.sleep(0);}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			if(this.isEmpty()){ 
				this.setDone(true);
			joinConsumers(consumerThreads);
			System.out.println("Consumers done!");
			}else throw new BufferException("Buffer not emptied by consumers");
		}
	}
	
	public void joinConsumers(Thread[] t);
	
	public default void quickTest() throws BufferException{
		long startTime = System.currentTimeMillis();
		this.test(2, 5);
		long endTime = System.currentTimeMillis();
		long p2c5time = endTime - startTime;
		System.out.println("2 Producers and 5 Consumers run time = " + p2c5time + " milliseconds");

		startTime = System.currentTimeMillis();
		this.test(5, 2);
		endTime = System.currentTimeMillis();
		long p5c2time = endTime - startTime;
		System.out.println("5 Producers and 2 Consumers run time = " + p5c2time + " milliseconds");
		System.out.println("2 Producers and 5 Consumers run time = " + p2c5time + " milliseconds");
		System.out.println("Total time: " + (p5c2time + p2c5time));
		System.out.println("Time difference: " + Math.abs(p5c2time - p2c5time));
	}


	
}
