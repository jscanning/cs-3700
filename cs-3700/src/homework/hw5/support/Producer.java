package homework.hw5.support;

public class Producer implements Runnable{

	Buffer buff;
	int productionAmount = 100;
	
	public Producer(Buffer buff){
		this.buff = buff;
	}

	@Override
	public void run() {
		try {
			buff.continuousPut(productionAmount);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
}
