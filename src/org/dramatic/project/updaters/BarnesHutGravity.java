package org.dramatic.project.updaters;

import java.util.List;

import org.dramatic.project.Entity;
import org.dramatic.project.EntityFactory;
import org.dramatic.project.Vector;

public class BarnesHutGravity implements EntityUpdater {

	private double width;
	private double granularity;
	
	public BarnesHutGravity(double width, double granularity)
	{
		this.width = width;
		this.granularity = granularity;
	}
	
	private static int steps = 1;

	@Override
	public void update(List<Entity> collection) {
		Vector slices = new Vector(EntityFactory.DIMENSION);
		DimensionTree tree = new DimensionTree(slices,width,granularity);
		for(Entity e : collection)
			tree.add(e);
		
		
		for(Entity e : collection)
		{
			e.setForce(0, tree.getForce(e));
		}
		
		//System.out.println("steps: " + steps);
		//steps++;
	}

}
