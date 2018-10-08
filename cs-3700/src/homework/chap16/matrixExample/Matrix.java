package homework.chap16.matrixExample;


public final class Matrix{
	int dimension;
	float[][] data;
	int rowDisplace, colDisplace;
	public Matrix(int d){
		dimension = d;
		rowDisplace = colDisplace = 0;
		data = new float[d][d];
	}
	
	private Matrix(float[][] matrix, int x, int y, int d){
		data = matrix;
		rowDisplace = x;
		colDisplace = y;
		dimension = d;
	}
	public float get(int row, int col){
		return data[row+rowDisplace][col+colDisplace];
	}
	public void set(int row, int col, float val){
		data[row+rowDisplace][col+colDisplace] = val;
	}
	public int getDim(){
		return dimension;
	}
	Matrix[][] split(){
		Matrix[][] result = new Matrix[2][2];
		int newDim = dimension / 2;
		result[0][0] = new Matrix(data, rowDisplace, colDisplace, newDim);
		result[0][1] = new Matrix(data, rowDisplace, colDisplace + newDim, newDim);
		result[1][0] = new Matrix(data, rowDisplace + newDim, colDisplace, newDim);
		result[1][1] = new Matrix(data, rowDisplace + newDim, colDisplace + newDim, newDim);
		return result;
	}
}
