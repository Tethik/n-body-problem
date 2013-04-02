package org.dramatic.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityFactory 
{
	
	private static Random rand = new Random();
	
	
	public static double MASS_MAGNITUDE_FACTOR = 1000000000000000D;
	
	public static long POS_MAGNITUDE_FACTOR = 10000;
	public static long VELOCITY_MAGNITUDE_FACTOR = 0;
	public static double RADIUS = 100;
	
	public enum forcelist_type {
		ALL,
		SINGLE
	}
	
	public static forcelist_type FORCELIST_TYPE = forcelist_type.SINGLE;
	
	public static int DIMENSION = 2;
	
	
	public static List<Entity> getEntities(int num)
	{
		List<Entity> entityList = new ArrayList<Entity>();
		for(int i = 0; i < num; i++)
		{
			entityList.add(getEntity(num));
		}
		return entityList;
	}
	
	private static Entity getEntity(int number_of_entities)
	{
		//return new Entity(randomNumber(MASS_MAGNITUDE_FACTOR), randomVector(POS_MAGNITUDE_FACTOR), randomVector(VELOCITY_MAGNITUDE_FACTOR) );
		Entity e = new Entity(MASS_MAGNITUDE_FACTOR, randomVector(POS_MAGNITUDE_FACTOR), randomVector(VELOCITY_MAGNITUDE_FACTOR) );
		e.setRadius(RADIUS);		
		if(FORCELIST_TYPE == forcelist_type.ALL)
		{
			e.initForceList(number_of_entities);
		} else {
			e.initForceList(1);
		}
		return e; 
	}
	
	private static double randomNumber(double value)
	{
		return rand.nextDouble()*value;
	}
	
	private static Vector randomVector(double value)
	{		
		
		double[] vals = new double[DIMENSION];
		
	// Maybe they are negative?
		for(int i = 0; i < DIMENSION; i++)
		{
			vals[i] = randomNumber(value);
			int n = rand.nextInt(2);
			if(n != 0)
				vals[i] *= -1;
		}
		Vector v = new Vector(DIMENSION);
		v.setVars(vals);
		return v;
	}
	
	public static void changeRandomSeed(long seed)
	{
		rand.setSeed(seed);
	}
	
	
}
