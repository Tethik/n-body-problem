package org.dramatic.project.updaters;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.dramatic.project.Entity;
import org.dramatic.project.Vector;

public class GravitationalForce extends AbstractEntityUpdater 
{	
	public GravitationalForce(int numThreads)
	{
		super(numThreads);
	}
	
	@Override
	public List<Runnable> getTasks(List<Entity> collection) 
	{
		List<Runnable> taskList = new ArrayList<Runnable>();
/*
		int chunkSize = collection.size()/getNumThreads();
		chunkSize =  (chunkSize < 1) ? 1 : chunkSize;
		
		int start = -1* chunkSize;
		int end = 0;*/
		//int tmp = collection.size() - chunkSize;
		
		int taskid = 0;
		for(Entity e: collection)
		{
			//start += chunkSize;
			//end += chunkSize; 
			//end = (end > collection.size()) ? collection.size() : end;
			//e.initForceList(taskid+1);
			taskList.add(new GravitationalForceTask(e, taskid, collection));
			taskid++;
		}
		return taskList;
	}
}
