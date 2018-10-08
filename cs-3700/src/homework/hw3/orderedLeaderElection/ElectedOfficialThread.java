/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.hw3.orderedLeaderElection;

import java.util.Random;

/**
 *
 * @author jscanning
 */
public class ElectedOfficialThread extends Thread {
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
        leader = this.name;
        rankthread = r;
        barrier = b;
    }
    
    public ElectedOfficialThread(String name, Random random, rankThread r){
        this.name = name;
        rank = random.nextInt(2147483647 + 1 +2147483647) -2147483647;
        leader = this.name;
        rankthread = r;
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
    			do{
    				if(hasChanges) {
    					speak();
    					barrier.await();
    				}
    				wait();
    			}while(!done); // idea is that some boolean controls how long this runs: boolean controlled by rank thread
    		}catch(InterruptedException e){
    			e.printStackTrace();
    		}
    	}

    }

    private void produce() throws InterruptedException {
        
    }
    
    public static void main(String[] args){
    	final int DEFAULT_N = 4;
    	ElectedOfficialThread leader;
        String trueLeaderName;
        int numberOfOfficials = DEFAULT_N;
        ElectedOfficialThread[] threads = new ElectedOfficialThread[numberOfOfficials];
        
        
    }
}
