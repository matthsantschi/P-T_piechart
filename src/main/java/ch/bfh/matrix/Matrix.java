/*
	* Project and Training 2: Pie Chart - Computer Science, Berner Fachhochschule
	*/
package ch.bfh.matrix;


/**
 * Represents a two-dimensional matrix of double-values. Objects are immutable
 * and methods implementing matrix operations always return new matrix objects.
 */
public class Matrix {

	// expected precision in floating point calculations
	public static final double EPSILON = 1e-10;

	// the matrix values in lines and columns
	protected double[][] values;

	/**
	 * Creates a matrix with values given in a two-dimensional array. First
	 * dimension represents lines, second the columns.
	 *
	 * @param values a non-empty and rectangular two-dimensional array
	 */
	public Matrix(final double[][] values) throws IllegalArgumentException {
		if (values == null) {
			throw new IllegalArgumentException("values are null");
		}
		// check if each row has same amount of digits
		int digitsInFirstRow = values[0].length;
		for (double[] ds : values) {
			if (ds.length != digitsInFirstRow) {
				throw new IllegalArgumentException("Digits in rows are not equal");
			}
			if (ds.length == 0) {
				throw new IllegalArgumentException("Please provide some Matrix Input");
			}
		}
		this.values = new double[values.length][values[0].length];
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[0].length; j++) {
				this.values[i][j] = values[i][j];
			}	
		}
	}

	/**
	 * @return the number of lines in this matrix
	 */
	public int getNbOfLines() {
		return this.values.length;
	}

	/**
	 * @return the number of columns in this matrix
	 */
	public int getNbOfColumns() {
		return this.values[0].length;
	}

	/**
	 * Returns the value at the given position in the matrix.
	 *
	 * @param line indicates the index for the line
	 * @param col  indicates the index for the column
	 * @return the value at the indicated position
	 */
	public double get(final int line, final int col) {
		return this.values[line][col];
	}

	/**
	 * Calculates the transpose of this matrix.
	 *
	 * @return the transpose of this matrix
	 */
	public Matrix transpose() {
		double[][] transposedValues = new double[this.getNbOfColumns()][this.getNbOfLines()];
		for (int i = 0; i < this.getNbOfLines(); i++) {
			for (int j = 0; j < this.getNbOfColumns(); j++) {
				transposedValues[j][i] = this.values[i][j];
			}
		}
		return new Matrix(transposedValues);
	}

	/**
	 * Calculates the product of this matrix with the given scalar value.
	 *
	 * @param scalar the scalar value to multiply with
	 * @return the scalar product
	 */
	public Matrix multiply(final double scalar) {
		double[][] matrixScalar = new double[this.getNbOfLines()][this.getNbOfColumns()];
		for (int i = 0; i < this.getNbOfLines(); i++) {
			for (int j = 0; j < this.getNbOfColumns(); j++) {
				matrixScalar[i][j] =  values[i][j] * scalar;
			}
		}
		return new Matrix(matrixScalar);
	}

	/**
	 * Calculates the product of this matrix with another matrix.
	 *
	 * @param other the other matrix to multiply with
	 * @return the matrix product
	 */
	public Matrix multiply(final Matrix other) {
		if (this.getNbOfColumns() != other.getNbOfLines()) {
			throw new IllegalArgumentException("Dimension mismatch");
		}
		if (this.equals(other) && this.getNbOfLines() != this.getNbOfColumns()) {
			throw new IllegalArgumentException("Matrix is not cubic!");
		}
		// the result matrix inherits the smaller dimension of both others
		int lines = this.getNbOfLines();
		int columns = ((this.getNbOfColumns() < other.getNbOfColumns()) ? this.getNbOfColumns()
				: other.getNbOfColumns());
		double[][] multiplyValues = new double[lines][columns];
		for (int i = 0; i < lines; i++) {
			for (int j = 0; j < columns; j++) {
				multiplyValues[i][j] = this.getProductRowCol(i, j, other);
			}
		}

		return new Matrix(multiplyValues);
	}

	private double getProductRowCol(int lineNr, int colNr, final Matrix other) {
		double product = 0.0;
		int maxSizeRowCol = (this.getNbOfColumns() > other.getNbOfLines()) ? this.getNbOfColumns()
				: other.getNbOfLines();
		for (int i = 0; i < maxSizeRowCol; i++) {
			if (i >= this.getNbOfColumns() || i >= other.getNbOfLines()) {
				continue;
			}
			product += this.values[lineNr][i] * other.get(i, colNr);
		}
		return product;
	}

	/**
	 * Calculates the sum of this matrix with another matrix.
	 *
	 * @param other the other matrix to add with
	 * @return the matrix sum
	 */
	public Matrix add(final Matrix other) {
		// we can only add matrix to matrix if dimensions are the same
		if (this.getNbOfLines() != other.getNbOfLines()) {
			throw new UnsupportedOperationException();
		}
		double[][] valuesSum = new double[this.getNbOfLines()][this.getNbOfColumns()];
		for (int i = 0; i < this.getNbOfLines(); i++) {
			for (int j = 0; j < this.getNbOfColumns(); j++) {
				valuesSum[i][j] = this.values[i][j] + other.get(i, j);
			}
		}
		return new Matrix(valuesSum);
	}

	@Override
	public int hashCode() {
		int hash = 0;
		for (int i = 0; i < this.getNbOfLines(); i++) {
			for (int j = 0; j < this.getNbOfColumns(); j++) {
				hash += Double.hashCode(Math.round(values[i][j]));
			}
		}
		return hash;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Matrix)) {
			return false;
		}
		// typecast obj so we can use the Matrix methodes, this is save because we
		// checked in advance, that obj is instanceof Matrix
		Matrix matrix = (Matrix) obj;
		if (this.getNbOfColumns() != matrix.getNbOfColumns() || this.getNbOfLines() != matrix.getNbOfLines()) {
			return false;
		}

		for (int i = 0; i < this.getNbOfLines(); i++) {
			for (int j = 0; j < this.getNbOfColumns(); j++) {
				if (Math.abs(matrix.get(i, j) - this.values[i][j]) > Matrix.EPSILON) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.getNbOfLines(); i++) {
			sb.append("| ");
			for (int j = 0; j < this.getNbOfColumns(); j++) {
				sb.append(Double.toString(this.values[i][j]));
				sb.append(":");
			}
			sb.append(" | \n");
		}
		return sb.toString();
	}
}
