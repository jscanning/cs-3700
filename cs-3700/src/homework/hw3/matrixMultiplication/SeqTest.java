package homework.hw3.matrixMultiplication;

import homework.hw3.matrixMultiplication.support.SeqMatrixMultiplier;

public class SeqTest {
	public static void main(String[] args) {
		Matrix A = new Matrix(2, 2);
		A.set(0, 0, 1.0f);
		A.set(1, 0, 2.0f);
		A.set(0, 1, 3.0f);
		A.set(1, 1, 4.0f);
		
		Matrix B = new Matrix(A);
		
		System.out.println(A);
		System.out.println();
		System.out.println(B);
		
		MatrixMultiplier multiplier = new SeqMatrixMultiplier();
		
		Matrix C = multiplier.multiply(A, B);
		System.out.println();
		System.out.println(C);
		
		Matrix D = new Matrix(1, 2);
		D.set(0, 0, 0.1f);
		D.set(1, 0, 0.2f);
		System.out.println();
		System.out.println(D);
	}
}
