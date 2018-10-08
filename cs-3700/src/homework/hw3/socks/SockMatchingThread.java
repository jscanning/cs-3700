package homework.hw3.socks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


public class SockMatchingThread extends Thread{
	CyclicBarrier barrier = new CyclicBarrier(5);
	
	public void run(List<Sock> socks) throws InterruptedException, BrokenBarrierException
	{
		barrier.await();
		List<Sock> temp = socks.subList(0, socks.size());
		Sock current, comp;
		for (Iterator<Sock> iter = temp.listIterator(); iter.hasNext();){
			current = (Sock) iter.next();
			for (Iterator<Sock> jter = temp.listIterator(); jter.hasNext();) {
				comp = (Sock) jter.next();
				if(current.equals(comp)){continue;} // skip if these are the same sock
				else if(current.getMyColor().compareTo(comp.getMyColor()) == 0){ // if these socks match
					new Thread(new Washer(current, comp, socks));
					jter.remove();
				}
			}
		}
	}
	
	class Washer implements Runnable {
		Sock s1;
		Sock s2;
		List<Sock> sockList;
		Washer(Sock in1, Sock in2, List<Sock> socks){
			s1 = in1;
			s2 = in2;
			sockList = socks;
		}
		@Override
		public void run() {
			sockList.remove(s1);
			sockList.remove(s2);
		}
		
	}

	class SockFactory implements Runnable {
		Sock.color sockColor;
		Random rnd;
		List<Sock> socks;
		
		SockFactory(Sock.color sColor, List<Sock> sock, Random rand)
		{
			sockColor = sColor;
			rnd = rand;
			socks = sock;
		}
		
		private synchronized void produceSock(){
			socks.add(new Sock(sockColor));
		}
		
		@Override
		public void run() {
			int numSocks = rnd.nextInt(10) + 1;
			while(numSocks > 0)
			{
				produceSock();
				numSocks -= 1;
			}
			try {
				barrier.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public static void main(String[] args) {
		SockMatchingThread matcher = new SockMatchingThread();
		
		List<Sock> socks = new LinkedList<Sock>();
		Random rnd = new Random();
		
		Thread red = new Thread(matcher.new SockFactory(Sock.color.red, socks, rnd));
		Thread green = new Thread(matcher.new SockFactory(Sock.color.green, socks, rnd));
		Thread blue = new Thread(matcher.new SockFactory(Sock.color.blue, socks, rnd));
		Thread orange = new Thread(matcher.new SockFactory(Sock.color.orange, socks, rnd));
		
		red.start();
		green.start();
		blue.start();
		orange.start();
		try {
			matcher.run(socks);
		} catch (InterruptedException | BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
