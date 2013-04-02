package org.dramatic.project.updaters;

import java.util.List;


import org.dramatic.project.Entity;
import org.dramatic.project.Vector;

public class BarnesHutTask extends Task {
	
	private DimensionTree tree;
	private List<Entity> entities;
	private int start;
	private int end;
	
	public BarnesHutTask(DimensionTree tree, List<Entity> entities)
	{
		this(tree, entities, 0, entities.size());
	}
	
	public BarnesHutTask(DimensionTree tree, List<Entity> entities, int start, int end)
	{
		this.tree = tree;
		this.entities = entities;
		this.start = start;
		this.end = end;
	}
	
	@Override
	public void doTask() {
		for(int i = start; i < end; i++)  
		{
			Vector force = tree.getForce(entities.get(i));
			Entity e = entities.get(i);
			e.setForce(0, force);
		}
	}

}
