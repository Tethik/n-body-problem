package org.dramatic.project.updaters;

import java.util.ArrayList;
import java.util.List;

import org.dramatic.project.Vector;
import org.dramatic.project.Entity;

public class CollisionForce extends AbstractEntityUpdater {

	public static double ELASTICITY = 1;
	
	public CollisionForce(int threadCount)
	{
		this(threadCount, 1D);
	}
	
	public CollisionForce(int threadCount, double elasticity)
	{
		super(threadCount);
		this.ELASTICITY = elasticity;
	}

	@Override
	public List<Runnable> getTasks(List<Entity> collection) {		
		
		List<Runnable> taskList = new ArrayList<Runnable>();
		int chunkSize = collection.size()/getNumThreads();
		chunkSize =  (chunkSize < 1) ? 1 : chunkSize;
		
		int start = -1* chunkSize;
		int end = 0;
		//int tmp = collection.size() - chunkSize;
		
		for(int t = 0; t < getNumThreads() && end < collection.size(); t++)
		{
			start += chunkSize;
			end += chunkSize; 
			end = (end > collection.size()) ? collection.size() : end;
			taskList.add(new CollisionTask(start, end, collection, ELASTICITY));			
		}
		return taskList;

	}
}
