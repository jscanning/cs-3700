package homework.hw2.Lecture3;

public class HelloRunnable implements Runnable { 
	public void run() { 
		System.out.println("Hello from a thread!"); 
	} 
	public static void main(String args[]) { 
		(new Thread(new HelloRunnable())).start(); 
	} 
} 



