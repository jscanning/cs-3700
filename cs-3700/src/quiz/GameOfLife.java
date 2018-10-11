package quiz;

import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GameOfLife {
/**
 * Rules:
 * The universe of the Game of Life is an infinite, two-dimensional orthogonal grid of square cells, 
 * each of which is in one of two possible states, alive or dead, (or populated and unpopulated, respectively). 
 * Every cell interacts with its eight neighbours, which are the cells that are horizontally, vertically, or diagonally adjacent.
 * At each step in time, the following transitions occur:
 *
 * 1. Any live cell with fewer than two live neighbors dies, as if by underpopulation.
 * 2. Any live cell with two or three live neighbors lives on to the next generation.
 * 3. Any live cell with more than three live neighbors dies, as if by overpopulation.
 * 4. Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.
 * 
 * The initial pattern constitutes the seed of the system. The first generation is created by 
 * applying the above rules simultaneously to every cell in the seed; births and deaths occur 
 * simultaneously, and the discrete moment at which this happens is sometimes called a tick. 
 * Each generation is a pure function of the preceding one. The rules continue to be applied 
 * repeatedly to create further generations.
 * 
 * @param args
 */
	Random rand = new Random();
	public cellThread[][] grid;
	CyclicBarrier barrier = new CyclicBarrier(5);
	private boolean workLeft = true;
	
	public void initializeGrid(int x, int y) {
		if((x * y) > 16) {
			throw new InvalidParameterException("Grid size too high! Thread grid should be less than 10 cells.");
		}
		else {
			grid = new cellThread[x][y];
			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[i].length; j++) {
					grid[i][j] = new cellThread(i, j, rand.nextBoolean());
				}
			}
		}
		barrier = new CyclicBarrier(x * y + 1);
	}
	
	public void printGrid(){
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				System.out.print(grid[i][j].numeric()+" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	class cellThread extends Thread{
		private int xPos;
		private int yPos;
		private boolean alive;
		private boolean nextGen;
		@SuppressWarnings("unused")
		private int cycle = 0;
		private int livingNeighbors;
		
		public cellThread(int x, int y, boolean alive) {
			xPos = x;
			yPos = y;
			this.alive = alive;
		}
		
		public int numeric() {
			if(alive)
				return 1;
			else
				return 0;
		}

		public void goToNextGen() {
			setAlive(nextGen);
		}

		public boolean isLiving() {
			return alive;
		}

		public void setAlive(boolean alive) {
			this.alive = alive;
		}
		
		public void run() {
			Queue<cellThread> cellQ = new LinkedList<>();
			int left, right, up, down;
			while(workLeft){
				if(xPos > 0)
					left = xPos - 1;
				else
					left = grid.length - 1;

				if(yPos > 0)
					up = yPos - 1;
				else
					up = grid[xPos].length - 1;

				if(xPos < grid.length - 1)
					right = xPos + 1;
				else
					right = 0;

				if(yPos < grid[xPos].length - 1)
					down = yPos + 1;
				else
					down = 0;

				cellQ.add(grid[left][up]);
				cellQ.add(grid[xPos][up]);
				cellQ.add(grid[right][up]);
				cellQ.add(grid[right][yPos]);
				cellQ.add(grid[right][down]);
				cellQ.add(grid[xPos][down]);
				cellQ.add(grid[left][down]);
				cellQ.add(grid[left][yPos]);

				try {
					nextGen = determineLife(isLiving(),cellQ);
					barrier.await();
					goToNextGen();

				} catch (InterruptedException | BrokenBarrierException e) {
					e.printStackTrace();
				}
				cycle += 1;
			}
			cellQ.clear();
			
		}

		private boolean determineLife(boolean alive, Queue<cellThread> cellQ) {
			boolean result = false;
			this.livingNeighbors = 0;
			if(!alive){ // is dead
				for (cellThread cellThread : cellQ) {
					if(cellThread.isLiving())
						livingNeighbors += 1;
				}
				if(livingNeighbors == 3) {result = true;} // come to life
				else {result = false;} // stay dead
			}else{ // is alive
				for (cellThread cellThread : cellQ) {
					if(cellThread.isLiving())
						livingNeighbors += 1;
				}
				if(livingNeighbors < 2 || livingNeighbors > 3)
					result = false; // die
				else
					result = true; // live on
			}
			
			return result;
		}
	}
	
	public static void main(String[] args) {
		GameOfLife life = new GameOfLife();
		life.initializeGrid(4, 4);
		life.play(3*3);
	}

	private void play(int generations) {
		Scanner sc = new Scanner(System.in);
		for (cellThread[] cellThreads : grid) {
			for (cellThread cellThread : cellThreads) {
				cellThread.start();
			}
		}
		try{
			for(int i = 0; i < generations; i++){
				printGrid();
				System.out.println("Generation "+i);
				System.out.println("Waiting for return key...");
				//sc.nextLine();
				try {
					barrier.await();
				} catch (InterruptedException | BrokenBarrierException e) {
					e.printStackTrace();
				}
			}
			barrier.await();
			workLeft = false;
			//barrier.await(500, TimeUnit.MILLISECONDS);
		} catch (InterruptedException | BrokenBarrierException /*| TimeoutException*/ e1) {
			e1.printStackTrace();
		}
		
	}

}
