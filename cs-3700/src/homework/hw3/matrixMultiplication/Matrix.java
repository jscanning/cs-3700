package homework.hw3.matrixMultiplication;

import java.util.Arrays;
import java.util.Objects;

public class Matrix {
	private final int rows;
	private final int cols;
	private final float[] array;
	
	public Matrix(int rows, int cols){
		this.rows = checkRows(rows);
		this.cols = checkCols(cols);
		this.array = new float[rows * cols];
	}
	
	public Matrix(int rows, int cols, float[] array){
		this.rows = rows;
		this.cols = cols;
		this.array = array;
	}
	
	public Matrix(Matrix other){
		Objects.requireNonNull(other, "The other matrix is null.");
		this.rows = other.rows;
		this.cols = other.cols;
		this.array = other.array.clone();
	}
	
	public int getRows(){
		return rows;
	}
	
	public int getCols(){
		return cols;
	}
	
	public float get(int x, int y){
		return array[index(x,y)];
	}
	
	public void set(int x, int y, float val){
		array[index(x, y)] = val;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		String seperator = "";
		
		for(int y = 0; y < rows; ++y){
			sb.append(seperator);
			seperator = "\n";
			
			for(int x = 0; x < cols; ++x){
				sb.append(get(x, y)).append(" ");
			}
		}
		return sb.toString();
	}
	
	private void checkX(int x){
		if(x<0){
			throw new IndexOutOfBoundsException("x is negative");
		}
		
		if(x >= cols){
			throw new IndexOutOfBoundsException("x is too large for the matrix");
		}
	}
	
	private void checkY(int y){
		if(y<0){
			throw new IndexOutOfBoundsException("y is negative");
		}
		
		if(y >= rows){
			throw new IndexOutOfBoundsException("y is too large for the matrix");
		}
	}
	
	
	private int index(int x, int y){
		checkX(x);
		checkY(y);
		return y * cols + x;
	}

	private int checkCols(int col) {
		return check(col, "Number of columns is too small: "+col);
	}

	private int checkRows(int row) {
		return check(row, "Number of rows is too small: " + row);
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null){return false;}
		else if (o == this){ return true;}
		else if(!getClass().equals(o.getClass())){
			return false;
		}else{
			Matrix other = (Matrix)o;
			if(getRows() != other.getRows()) {return false;}
			if(getCols() != other.getCols()) {return false;}
			return Arrays.equals(array, other.array);
		}
	}
	
	private int check(int number, String errorMessage){
		if(number < 1){
			throw new IllegalArgumentException(errorMessage);
		}
		return number;
	}
}
