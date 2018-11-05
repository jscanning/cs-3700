package homework.hw5;


import homework.hw5.support.Buffer;
import homework.hw5.support.BufferException;

public class ProdConWithIsolatedSections {

	public static void main(String[] args) {
		IsoBuffer buff = new IsoBuffer();
		try {
			buff.quickTest();
		} catch (BufferException e) {
			e.printStackTrace();
		}
	}


	static class IsoBuffer implements Buffer{
		private boolean done;
		private final Object empty, full;

		IsoBuffer(){
			full = new Object();
			empty = new Object();
			done = false;
		}

		public void setDone(){
			done = !done;
		}

		@Override
		public synchronized void put() throws InterruptedException {
				try{
					while(queue.size() == CAPACITY){
						System.out.println(Thread.currentThread().getName() + ": Buffer is full, waiting...");
						full.wait();
					}
					System.out.println(Thread.currentThread().getName() + ": Done waiting, initiating produce.");
					int item = rand.nextInt();
					boolean isAdded = queue.offer(item);
					if(isAdded){
						System.out.printf("%s: produced %d into queue %n", Thread.currentThread().getName(), item);
						System.out.println(Thread.currentThread().getName() + ": Signalling completed produce");
						empty.notifyAll();}
				}catch(InterruptedException | IllegalMonitorStateException e){
					//e.printStackTrace();
				}
		}


		@Override
		public synchronized void get() throws InterruptedException {
				try{
					while(queue.size() == 0){
						System.out.println(Thread.currentThread().getName() + ": Buffer is empty, waiting...");
						empty.wait();
					}
					System.out.println(Thread.currentThread().getName() + ": Done waiting, initiating consume.");
					Object item = queue.poll();
					if(item != null){
						System.out.printf("%s: consumed %d from queue %n", Thread.currentThread().getName(), item);
						System.out.println(Thread.currentThread().getName() + ": Signalling completed consume");
						full.notifyAll();
					}
				}catch(InterruptedException | IllegalMonitorStateException e){
					//e.printStackTrace();
				}finally{
					Thread.sleep(1000);
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
		public void continuousGet() throws InterruptedException {
			while(!done) {
				get();
			}
		}

		@Override
		public void setDone(boolean b) {
			// TODO Auto-generated method stub
			done = b;
		}

		@Override
		public void joinConsumers(Thread[] consumerThreads) {
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

