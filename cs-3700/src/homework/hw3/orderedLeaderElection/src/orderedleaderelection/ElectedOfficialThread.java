/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orderedleaderelection;

import java.util.Random;

/**
 *
 * @author jscanning
 */
public class ElectedOfficialThread extends Thread {
    private final String name;
    private int rank;
    private String leader;
    private boolean hasChanges = true;

    public ElectedOfficialThread(String name, int rank) {
        this.name = name;
        this.rank = rank;
        leader = this.name;
    }
    
    public ElectedOfficialThread(String name, Random random){
        this.name = name;
        rank = random.nextInt(2147483647 + 1 +2147483647) -2147483647;
        leader = this.name;
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
    
    @Override
    public void run(){
       
       synchronized(this){
           try{
               do{
                    if(hasChanges)
                        speak();
                    wait();
               }while(!done); // idea is that some boolean controls how long this runs: boolean controlled by rank thread
           }catch(InterruptedException e){
               
           }
       }
           
    }

    private void produce() throws InterruptedException {
        
    }
    
    public static void main(String[] args){
        
    }
}
