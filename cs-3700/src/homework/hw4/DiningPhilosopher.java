package homework.hw4;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosopher extends Thread{
	Random rand = new Random();
	private int index;
	Lock leftFork;
	Lock rightFork;
	int stomach;
	boolean done = false;
	
	public DiningPhilosopher(int index, Lock leftFork, Lock rightFork, int mealTime) {
		this.index = index+1;
		this.leftFork = leftFork;
		this.rightFork = rightFork;
		this.stomach = new Integer(mealTime);
	}

	@Override
	public void run() {
		while(stomach > 0){
			getForks();
		}
		System.out.println();
		System.out.println("Philosopher "+ index +": I'm full!");
		System.out.println();
	}

	public void eat(){
		int t = rand.nextInt(10)+1;
		try {
			stomach -= t;
			System.out.println("Philosopher "+ index +": eating for " + t + " seconds. \nStill need to eat for "+ (stomach) +" seconds.");
			sleep(t);
		} catch (InterruptedException e) {
			
		}
	}
	
	public void think(){
		int t = rand.nextInt(5) + 1;
		try {
			System.out.println("Philosopher "+ index +": thinking for " + t + " seconds.");
			sleep(t);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void getForks() {
		boolean left = false, right = false;
		if((left = leftFork.tryLock()) && (right = rightFork.tryLock())){
			System.out.println("Philosopher "+ index +": acquired Left Fork.");
			//System.out.println(leftFork);
			System.out.println("Philosopher "+ index +": acquired Right Fork.");
			//System.out.println(rightFork);
			try{
				eat();
			}finally{
				leftFork.unlock();
				System.out.println("Philosopher "+ index +": returned Left Fork.");
				//System.out.println(leftFork);
				
				rightFork.unlock();
				System.out.println("Philosopher "+ index +": returned Right Fork.");
				//System.out.println(rightFork);
			}
		}else{
			if(!left)
				System.out.println("Philosopher "+ index +": failed to acquire Left Fork.");
			else{
				System.out.println("Philosopher "+ index +": acquired Left Fork.");
				leftFork.unlock();
				System.out.println("Philosopher "+ index +": returned Left Fork.");
			}
			if(!right)
				System.out.println("Philosopher "+ index +": failed to acquire Right Fork.");
			else{
				System.out.println("Philosopher "+ index +": acquired Right Fork.");
				rightFork.unlock();
				System.out.println("Philosopher "+ index +": returned Right Fork.");
			}
			
			think();
		}
		
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("How long is the meal? Enter an int value: ");
		int duration = sc.nextInt();
		sc.close();
		Lock[] forks = new Lock[5];
		Lock left, right;
		DiningPhilosopher[] philo = new DiningPhilosopher[5];
		
		for(int i = 0; i < forks.length; i++){
			forks[i] = new ReentrantLock();
		}
		
		for(int i = 0; i < philo.length; i++){
			if(i == 0){
				left = forks[forks.length -1];
				right = forks[i+1];
			}else if(i == philo.length-1){
				left = forks[i-1];
				right = forks[0];
			}
			else{
				left = forks[i-1];
				right = forks[i+1];
			}
				philo[i] = new DiningPhilosopher(i, left, right, duration);
		}
		for (DiningPhilosopher Phil : philo) {
			Phil.start();
		}
		
		for (DiningPhilosopher p : philo) {
			try {
				p.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Everyone is full!");
	}

}
