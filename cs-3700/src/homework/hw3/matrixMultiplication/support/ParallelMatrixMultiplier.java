package homework.hw3.matrixMultiplication.support;

import java.util.Objects;

import homework.hw3.matrixMultiplication.Matrix;
import homework.hw3.matrixMultiplication.MatrixMultiplier;

public class ParallelMatrixMultiplier implements MatrixMultiplier {
	
	private static final int MIN_WORKLOAD = 10_000;
	private static int NUMBER_OF_THREADS = -1;
	
	public ParallelMatrixMultiplier() {}
	
	public ParallelMatrixMultiplier(int exactThreadNumber){
		NUMBER_OF_THREADS = exactThreadNumber;
	}
	
	@Override
	public Matrix multiply(Matrix left, Matrix right) {
		Objects.requireNonNull(left, "Left matrix is null");
		Objects.requireNonNull(right, "Right matrix is null");
		checkDim(left, right);
		
		int numberOfThreads;
		if(NUMBER_OF_THREADS == -1){
			int workload = left.getRows() * right.getCols() * right.getRows();
			numberOfThreads = Runtime.getRuntime().availableProcessors();
			numberOfThreads = Math.min(numberOfThreads, workload / MIN_WORKLOAD);
			numberOfThreads = Math.min(numberOfThreads, left.getRows());
			numberOfThreads = Math.max(numberOfThreads, 1);
		}else
			numberOfThreads = NUMBER_OF_THREADS;
		
		if(numberOfThreads == 1){
			return new SeqMatrixMultiplier().multiply(left, right);
		}
		
		Matrix result = new Matrix(left.getRows(), right.getCols());
		MultiplierThread[] threads = new MultiplierThread[numberOfThreads - 1];
		int basicRowsPerThreads = left.getRows() / numberOfThreads;
		int startRow = 0;
		
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new MultiplierThread(left, right, result, startRow, basicRowsPerThreads);
			threads[i].start();
			startRow += basicRowsPerThreads;
		}
		
		new MultiplierThread(left, right, result, startRow, 
				basicRowsPerThreads + left.getRows() % basicRowsPerThreads).run();
		
		for(MultiplierThread thread : threads){
			try{
				thread.join();
			} catch (InterruptedException ex){
				throw new RuntimeException("A thread interrupted.", ex);
			}
		}
		
		return result;
	}
	
	private static final class MultiplierThread extends Thread{
		private final Matrix left;
		private final Matrix right;
		private final Matrix result;
		private final int startRow;
		private final int rows;
		
		public MultiplierThread(Matrix left, Matrix right, 
				Matrix result, int startRow, int rows){
			this.left = left;
			this.right = right;
			this.result = result;
			this.startRow = startRow;
			this.rows = rows;
		}
		
		@Override
		public void run(){
			for(int y = startRow; y < startRow + rows; y++){
				for(int x = 0; x < right.getCols(); x++){
					float sum = 0.0f;
					
					for(int k = 0; k < left.getCols(); k++){
						sum += left.get(k, y) * right.get(x, k);
					}
					
					result.set(x, y, sum);
				}
			}
		}
	}

	private void checkDim(Matrix left, Matrix right) {
		if(left.getCols() != right.getRows()){
			throw new IllegalArgumentException("Trying to multiply non-compatible matrices. "
					+ "Columns of left matrix: " + left.getCols() 
					+ ". Rows of right matrix: " + right.getRows());
		}
	}
}
