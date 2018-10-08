package homework.hw3.matrixMultiplication.support;

import java.util.Objects;

import homework.hw3.matrixMultiplication.Matrix;
import homework.hw3.matrixMultiplication.MatrixMultiplier;

public final class SeqMatrixMultiplier implements MatrixMultiplier {

	@Override
	public Matrix multiply(Matrix left, Matrix right) {
		Objects.requireNonNull(left, "Left matrix is null");
		Objects.requireNonNull(right, "Right matrix is null");
		checkDim(left, right);
		
		Matrix result = new Matrix(left.getRows(), right.getCols());
		
		for(int y = 0; y < left.getRows(); ++y){
			for(int x = 0; x < right.getCols(); ++x){
				float sum = 0.0f;
				
				for(int k = 0; k < left.getCols(); ++k){
					sum += left.get(k, y) * right.get(x, k);
				}
				result.set(x, y, sum);
			}
		}
		return result;
	}

	private void checkDim(Matrix left, Matrix right) {
		if(left.getCols() != right.getRows()){
			throw new IllegalArgumentException("Trying to multiply non-compatible matrices. "
					+ "Columns of left matrix: " + left.getCols() 
					+ ". Rows of right matrix: " + right.getRows());
		}
	}

}
