package homework.hw3.matrixMultiplication;

import java.util.Random;

import homework.hw3.matrixMultiplication.support.ParallelMatrixMultiplier;
import homework.hw3.matrixMultiplication.support.SeqMatrixMultiplier;

public class ParallelTest {
	private static int THREAD_COUNT = 1;
	
	public static void matmult(float[] A, float[] B, float[] C, int m, int n, int p){
		Matrix mA = new Matrix(m,n,A);
		Matrix mB = new Matrix(n,p,B);
		Matrix result = new ParallelMatrixMultiplier(THREAD_COUNT).multiply(mA, mB);
		for(int i = 0; i < result.getCols(); i++)
		{
			for (int j = 0; j < result.getRows(); j++) {
				C[i * result.getCols() + j] = result.get(j, i);
			}
		}
	}
	
	public static void main(String[] args) {
		Random rand = new Random();
		for(int i = 1; i <= 4; i++){
			int bound = 200*i;
			System.out.println("Matrix size: " + bound);
			int m = bound, n = bound, p = bound;
			float[] A = getRandomFloatArray(m, n, rand);
			float[] B = getRandomFloatArray(n, p, rand);
			float[] C = new float[m*p];
			for(int j = 1; j <= 8; j++){
				THREAD_COUNT = j;
				System.out.print("Threads: " + THREAD_COUNT + " -> ");
				long start = System.currentTimeMillis();
				matmult(A,B,C,m,n,p);
				long end = System.currentTimeMillis();
				System.out.println("Runtime: " + (end-start));
			}
			System.out.println();
		}
		/*
		 * for (int i = 0; i < m; i++) {
		 *	for (int j = 0; j < p; j++) {
		 *		System.out.print(C[i] + " ");
		 *	}
		 *	System.out.println();
		 *}
		 */
	}
	
	private static float[] getRandomFloatArray(int n, int m, Random rand){
		int length = n*m;
		float[] out = new float[length];
		for (int i = 0; i < out.length; i++) {
			out[i] = rand.nextFloat();
		}
		return out;
	}
	
	private static Matrix getRandomMatrix(int rows, int cols, Random rand){
		Matrix m = new Matrix(rows, cols);
		
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				m.set(i, j, rand.nextFloat());
			}
		}
		return m;
	}
	
	@SuppressWarnings("unused")
	private void test(){
		Random rand = new Random();
		Matrix m1 = getRandomMatrix(500, 500, rand);
		Matrix m2 = getRandomMatrix(500, 500, rand);

		Matrix m1b = new Matrix(m1);
		Matrix m2b = new Matrix(m2);

		long start = System.currentTimeMillis();
		Matrix result1 = new SeqMatrixMultiplier().multiply(m1, m2);
		long end = System.currentTimeMillis();

		System.out.println("Sequential: " + (end - start));

		start = System.currentTimeMillis();
		Matrix result2 = new ParallelMatrixMultiplier().multiply(m1b, m2b);
		end = System.currentTimeMillis();

		System.out.println("Parallel: " + (end - start));
		System.out.println("Same results: " + result1.equals(result2));
	}

}
