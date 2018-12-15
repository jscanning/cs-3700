package rockPaperScissors;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.Random;

public class pipedPlayer implements Runnable{

	private PipedReader[] readers = {new PipedReader(), new PipedReader()};
	private PipedWriter[] writers = {new PipedWriter(), new PipedWriter()};
	private int id, myScore;
	private pipedPlayer left, right;
	
	public pipedPlayer(int id){
		this.id = id;
		myScore = 0;
	}
	
	public Hand randomRps() {
		Random rnd = new Random(System.currentTimeMillis());
		int value = rnd.nextInt(3);
		switch (value) {
			case 0: return Hand.ROCK;
			case 1: return Hand.PAPER;
			case 2: return Hand.SCISSORS;
		}
		return null;
	}
	
	public void connectTo(pipedPlayer other){
		try {
			if(other.id == id + 1){
				left = other;
				readers[0].connect(other.writers[1]);
				writers[0].connect(other.readers[1]);
			}else{
				right = other;
				readers[1].connect(other.writers[0]);
				writers[1].connect(other.readers[0]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void sendMove(Hand myMove) {
		try {
			for (PipedWriter writer : writers) {
				writer.write(myMove.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Hand receiveMove(pipedPlayer other) {
		int index = other == left ? 0 : 1;
		char[] cbuf = new char[8];
		try {
			readers[index].read(cbuf);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String str = new String(cbuf);
		return Hand.valueOf(str);
	}
	
	private int evaluateMoves(Hand myMove, Hand moveA, Hand moveB){
		int pointsWon = 0;
		if(myMove.evaluate(moveA) == Result.LOSE || myMove.evaluate(moveB) == Result.LOSE)
			return pointsWon;
		if(myMove.evaluate(moveA) == Result.WIN)
			pointsWon += 1;
		if(myMove.evaluate(moveB) == Result.WIN)
			pointsWon += 1;
		return pointsWon;
	}
	
	public void playRound(){
		Hand myMove = randomRps();
		sendMove(myMove);
		myScore += evaluateMoves(myMove, receiveMove(left), receiveMove(right));
	}

	public void sendReady() {
		// TODO Auto-generated method stub

	}

	public boolean waitForReady() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		playRound();
	}

	public static void main(String[] args) {
		pipedPlayer[] players = {new pipedPlayer(0), new pipedPlayer(1), new pipedPlayer(2)};
		players[0].connectTo(players[1]);
		players[0].connectTo(players[2]);
		
		Thread[] threads = new Thread[3];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(players[i]);
		}
		for (Thread thread : threads) {
			thread.start();
		}
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.printf("Scores: Player 1: %d Player 2: %d Player 3: %d", players[0].myScore, players[1].myScore, players[2].myScore);
	}

	

}
