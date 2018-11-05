package homework.hw5;


import java.util.concurrent.locks.*;

import homework.hw5.support.Buffer;
import homework.hw5.support.BufferException;

public class ProdConWithLocks {

	public static void main(String[] args) {
		LockBuffer lb = new LockBuffer();
		try {
			lb.quickTest();
		} catch (BufferException e) {
			e.printStackTrace();
		}
	}
	
	static class LockBuffer implements Buffer {
		private final Lock bufLock = new ReentrantLock();
		private final Condition bufferNotFull = bufLock.newCondition();
		private final Condition bufferNotEmpty = bufLock.newCondition();
		private boolean done = false;

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
				while(queue.size() == 0 && !done) {
					System.out.println(Thread.currentThread().getName() + ": Buffer is empty, waiting...");
					//if(done) return;
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

		@Override
		public void setDone(boolean b) {
			done = b;
		}

		@Override
		public void joinConsumers(Thread[] consumerThreads) {
			bufLock.lock();
			bufferNotFull.signalAll();
			bufLock.unlock();
			for(int i = 0; i < consumerThreads.length; i++){
				try {
					consumerThreads[i].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
}