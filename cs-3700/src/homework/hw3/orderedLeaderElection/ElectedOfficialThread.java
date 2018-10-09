/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.hw3.orderedLeaderElection;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author jscanning
 */
public class ElectedOfficialThread extends Thread implements Comparable<ElectedOfficialThread> {
    rankThread rankthread;
	CyclicBarrier barrier;
    
	private final String name;
    private int rank;
    private String leader;
    
    private boolean hasChanges = true;
    private boolean done = false;

    public ElectedOfficialThread(String name, int rank, rankThread r, CyclicBarrier b) {
        this.name = name;
        this.rank = rank;
        leader = name;
        rankthread = r;
        barrier = b;
    }
    
    public ElectedOfficialThread(String name, Random random, rankThread r, CyclicBarrier b){
        this.name = name;
        rank = random.nextInt();
        leader = name;
        rankthread = r;
        barrier = b;
    }
    
    public int getRank(){
        return this.rank;
    }
    
    public void setRank(int newRank){
        change();
        rank = newRank;
    }
    
    public String getMyName(){
        return name;
    }
    
    public String getLeader(){
        return leader;
    }
    
    public void setLeader(String newLeader){
        change();
        this.leader = newLeader;
    }
    
    private void change(){
        hasChanges = true;
    }
    
    public void speak(){
        System.out.println(getMyName()+" Rank: "+ getRank() +" says: I think the leader is: " + getLeader());
        hasChanges = false;
    }
    
    public void done() {
    	done = true;
    }
    
    @Override
    public void run(){
    	synchronized(this){
    		try {
    			rankthread.interrupt();
    			do{
    				if(hasChanges) {
    					speak();
    					barrier.await();
    				}
    				wait();
    			}while(!done);
    			// idea is that some boolean controls how long this runs: boolean controlled by rank thread
    		}catch(InterruptedException | BrokenBarrierException e){
    			e.printStackTrace();
    		} 
    		
    		if(hasChanges) {
				speak();
			}
    		
    		rankthread.done();
    		rankthread.notify();
    	}

    }

	@Override
	public int compareTo(ElectedOfficialThread o) {
		if(o == null) {
			throw new NullPointerException("null object in ElectedOfficialThread compareTo method");
		}
		
		int result;
		if(getRank() < o.getRank())
			result = -1;
		else if(getRank() == o.getRank())
			result = 0;
		else
			result = 1;
		
		return result;
	}
	
	 /*
    public static void main(String[] args){
    	final int DEFAULT_N = 4;
    	ElectedOfficialThread leader;
        String trueLeaderName;
        int numberOfOfficials = DEFAULT_N;
        ElectedOfficialThread[] threads = new ElectedOfficialThread[numberOfOfficials];
    }
    */
}
