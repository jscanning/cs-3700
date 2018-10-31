package homework.hw5.support;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public interface Buffer {
	final Random rand = new Random();
	final int CAPACITY = 10;
	final Queue<Integer> queue = new LinkedList<>();
	
	public void put() throws InterruptedException;
	
	public void get() throws InterruptedException;

	public void continuousPut(int amount) throws InterruptedException;

	public void continuousGet() throws InterruptedException;
	
	
}
