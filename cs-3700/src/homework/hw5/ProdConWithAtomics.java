package homework.hw5;

import homework.hw5.support.Buffer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.*;

public class ProdConWithAtomics {
	static class AtomicBuffer implements Buffer {
		AtomicInteger head;
		AtomicInteger tail;
		private final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
		AtomicBoolean done = new AtomicBoolean(false);
		@Override
		public void put() throws InterruptedException {
			// TODO Auto-generated method stub
			int head = this.head.get();
			int tail = this.tail.get();
			int newTail = (tail + 1) % (CAPACITY);
			int item = rand.nextInt();
			if(newTail == head){
				//full
			}else{
				queue.offer(item);
				this.tail.set(newTail);
			}
		}

		@Override
		public void get() throws InterruptedException {
			// TODO Auto-generated method stub
			int tail = this.tail.get();
			int head = this.head.get();
			
		}

		@Override
		public void continuousPut(int amount) throws InterruptedException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void continuousGet() throws InterruptedException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setDone(boolean b) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void joinConsumers(Thread[] t) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
