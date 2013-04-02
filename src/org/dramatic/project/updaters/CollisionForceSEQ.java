package org.dramatic.project.updaters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dramatic.project.Vector;
import org.dramatic.project.Entity;

public class CollisionForceSEQ implements EntityUpdater {

	public static double ELASTICITY = 1;

	public CollisionForceSEQ()
	{
		this(1D);
	}
	
	public CollisionForceSEQ(double elasticity)
	{
		this.ELASTICITY = elasticity;
	}

	@Override
	public void update(List<Entity> collection) 
	{
		Map<Integer, Set<Integer>> updatedEntities = new HashMap<Integer, Set<Integer>>();
		Set<Integer> skipList = null;
		
		for(Entity e1: collection)
		{
		// Contains a set of entities that have already been updated against entity e1
			skipList = updatedEntities.get(e1.getId());
			
			for(Entity e2: collection)
			{
				if(e1.equals(e2) || (skipList != null && skipList.contains(e2.getId())) )
					continue;
			
				Vector sub = e1.getPosition().subtract(e2.getPosition());
				double dist = sub.length();
				
				if(dist > e1.getRadius() + e2.getRadius())
					continue;
				
				sub = sub.normalize().multiplyByScalar(e1.getRadius()+e2.getRadius()-dist);					
				e1.setPosition(e1.getPosition().add(sub));
				
				Vector vel = e1.getVelocity();
				e1.setVelocity(e2.getVelocity().multiplyByScalar(ELASTICITY));
				e2.setVelocity(vel.multiplyByScalar(ELASTICITY));
				
				// Mark for future that e2 is already updated by e1
				if(updatedEntities.get(e2.getId()) == null)
				{
					updatedEntities.put(e2.getId(), new HashSet<Integer>());
				}
				updatedEntities.get(e2.getId()).add(e1.getId());

			}
		}
	}
}
