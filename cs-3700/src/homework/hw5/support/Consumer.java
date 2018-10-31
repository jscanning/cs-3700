package homework.hw5.support;


public class Consumer implements Runnable {

	Buffer buff;
	
	public Consumer(Buffer buff){
		this.buff = buff;
	}

	@Override
	public void run() {
		try {
			buff.continuousGet();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
}

