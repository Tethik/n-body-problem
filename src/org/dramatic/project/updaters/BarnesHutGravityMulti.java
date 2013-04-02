package org.dramatic.project.updaters;

import java.util.ArrayList;
import java.util.List;

import org.dramatic.project.Entity;
import org.dramatic.project.EntityFactory;
import org.dramatic.project.Vector;

public class BarnesHutGravityMulti extends AbstractEntityUpdater {

	private double width;
	private double granularity;
	
	public BarnesHutGravityMulti(int threadCount, double width, double granularity) {
		super(threadCount);
		
		this.width = width;
		this.granularity = granularity;		
	}

	@Override
	public List<Runnable> getTasks(List<Entity> collection) {
		Vector slices = new Vector(EntityFactory.DIMENSION);
		DimensionTree tree = new DimensionTree(slices,width,granularity);
		for(Entity e : collection)
			tree.add(e);
				
		
		List<Runnable> taskList = new ArrayList<Runnable>();
		if(getNumThreads() == 1)
		{
			BarnesHutTask task = new BarnesHutTask(tree, collection);
			taskList.add(task);
		}
		else
		{
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
				taskList.add(new BarnesHutTask(tree, collection, start, end));			
			}
			
		}
		return taskList;
	}

}
