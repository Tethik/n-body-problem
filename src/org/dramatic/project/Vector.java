package org.dramatic.project;

public class Vector 
{	
// Variables that are "cached", only recalculated when vector is changed.
	private double length = 0;
	private Vector reverse = null;
	private Vector normalVector = null;
	
	private int dimensions;
	private double vars[];
	
	/**
	 * Creates a vector instance of dimension 'd'. Vector is initially 
	 * filled with 0's.
	 * @param d
	 */
	public Vector(int d)
	{
		if(d < 1)
			throw new IllegalArgumentException("VectorConstructor: Faulty Dimensions number! Can't be less than one");
		this.dimensions = d;
		vars = new double[d];
	}
	
// BEGIN make cacheable
	
	/**
	 * Return the length of this vector.
	 * @return length
	 */
	public double length()
	{
		// Just be sure that it is really 0
		if(length == 0) calcLength();
		return length;
	}
	
	/**
	 * Calculate the length of this vector and set it
	 * to the global field 'length'.
	 */
	private void calcLength()
	{
		double tmp = 0;
		for(int i = 0; i < dimensions; i++)
		{
			tmp += Math.pow(vars[i], 2);
		}
		tmp = Math.sqrt(tmp);
		
		length = tmp;
	}
	
	/**
	 * Return the normal vector of this vector.
	 * @return normalVector
	 */
	public Vector normalize()
	{
		if(normalVector == null) calcNormalVector();
		return normalVector;
	}
	
	/**
	 * Calculate a normal vector of this vector and set it to the
	 * global field 'normalVector'.
	 */
	private void calcNormalVector()
	{
		if(normalVector == null) normalVector = new Vector(dimensions);
		double length = length();
		for(int i = 0; i< dimensions; i++)
		{
			normalVector.vars[i] = vars[i]/length;
		}
	}
	
	/**
	 * Return the reverse vector.
	 * @return reverseVector
	 */
	public Vector reverse()
	{
		if(reverse == null) calcReverse();
		return reverse;
	}
	
	/**
	 * Calculate a reverse vector of this vector and set it to the
	 * global field 'reverse'.
	 */
	private void calcReverse()
	{
		if(reverse == null) reverse = new Vector(dimensions);
		for(int i = 0; i < dimensions; i++)
		{
			reverse.vars[i] = -1*vars[i];
		}
	}

// END cacheable
	
	
// Getters
	public double getVar(int pos)
	{
		if(pos >= vars.length || pos < 0)
			throw new IllegalArgumentException("getVar(int): Attempted to get variable from outside given vector dimensions");
		return vars[pos];	
	}
	
	public int getDimensions()
	{
		return dimensions;
	}
		
// Methods that change the state of the Vector

	/**
	 * Re-evaluates the length of this vector, the reverse vector and the normal vector.
	 */
	private void updateCacheVariables()
	{
		calcLength();
		calcReverse();
		calcNormalVector();
	}
	
	/**
	 * Clears vectors values by setting them to 0.
	 */
	public void clear()
	{
		for(int i = 0; i < dimensions; i++)
		{
			vars[i] = 0;
		}
		
		updateCacheVariables();
	}
	
	/**
	 * Sets vals as the new array of values. 
	 * @param vals
	 */
	public void setVars(double[] vals)
	{
		if(this.vars.length != vals.length) throw new IllegalArgumentException("Vector-setPositions(): Different sizes of arrays. " + this.vars.length + " vs " + vals.length);
		this.vars = vals; 
		
		updateCacheVariables();
	}
	
	/**
	 * Sets a value var at the vectors value index 'index'
	 * @param index
	 * @param var
	 */
	public void setVar(int index, double var)
	{
		if(index < 0 && index >= vars.length) throw new IllegalArgumentException("Vector-setVar(): Crazy index value " + index + " too large compared to size: " + vars.length);
		this.vars[index] = var;
		
		updateCacheVariables();
	}
	
	
// Methods that do not change the state of vector
	
	/**
	 * Returns a new Vector, which is the result of this vector
	 * added with vector v2.
	 * @param v2
	 * @return resultVector
	 */
	public Vector add(Vector v2)
	{
		if(v2.dimensions != dimensions)
			throw new IllegalArgumentException("Dimensions mismatch!");
		
		Vector result = new Vector(dimensions);
		double[] resultVars = new double[dimensions];
		for(int i = 0; i < dimensions; i++)
			resultVars[i] = this.vars[i] + v2.vars[i];
		result.setVars(resultVars);
		return result;
	}

	/**
	 * Returns a new Vector, which is the result of this vector
	 * subtracted with vector v2.
	 * @param v2
	 * @return resultVector
	 */
	public Vector subtract(Vector v2)
	{
		if(v2.dimensions != dimensions)
			throw new IllegalArgumentException("Dimensions mismatch!");
		
		Vector result = new Vector(dimensions);
		double[] resultVars = new double[dimensions];
		for(int i = 0; i < dimensions; i++)
			resultVars[i] = this.vars[i] - v2.vars[i];
		result.setVars(resultVars);
		return result;
	}
	
	/**
	 * Returns a new Vector, which has the values of
	 * this vector multiplied by the scalar. 
	 * @param scalar
	 * @return resultVector
	 */
	public Vector multiplyByScalar(double scalar)
	{
		Vector result = new Vector(dimensions);
		double[] resultVars = new double[dimensions];
		for(int i = 0; i < dimensions; i++)
		{
			resultVars[i] = scalar*vars[i];
		}
		result.setVars(resultVars);
		return result;
	}
	
	/**
	 * Returns a new Vector, which has the values of
	 * this vector divided by the scalar. 
	 * @param scalar
	 * @return resultVector
	 */
	public Vector divideByScalar(double scalar)
	{
		Vector result = new Vector(dimensions);
		double[] resultVars = new double[dimensions];
		for(int i = 0; i < dimensions; i++)
		{
			resultVars[i] = vars[i]/scalar;
		}
		result.setVars(resultVars);
		return result;
	}
	
	/**
	 * Calculates the dot-product between this and the v2 vector
	 * @param v2
	 * @return dotProduct
	 */
	public int dotProduct(Vector v2)
	{
		int sum = 0;
		for(int i = 0; i < dimensions; i++)
		{
			sum += vars[i]*v2.vars[i];
		}
		return sum;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("( ");
		for(int i = 0; i < dimensions; i++)
		{
			if(i != dimensions-1) sb.append(vars[i]).append(", ");
			else sb.append(vars[i]);
		}
		sb.append(")");
		return sb.toString();
	}
}

