package org.dramatic.project.updaters;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dramatic.project.Entity;
import org.dramatic.project.Vector;

public class GravitationalForceTask extends Task
{
	
	public final static double GRAVITY_CONSTANT = 6.67384 * Math.pow(10, -11);
	
	private int start;
	private Entity e1;
	private List<Entity> entities;
	
	public GravitationalForceTask(Entity e, int start, List<Entity> entities)
	{
		this.e1 = e;
		this.entities = entities;
		this.start = start;
	}

	public void doTask()
	{		
	// Contains a set of entities that have already been updated against entity e1
		Entity e2;
		Vector accumulatedForce = new Vector(e1.getAcceleration().getDimensions());
		for(int i = start; i < entities.size(); i++)
		{
			e2 = entities.get(i);
			if(e1.equals(e2))
				continue;
			
			Vector force = getGravitationalForce(e1, e2);
			accumulatedForce = accumulatedForce.add(force);
		// E2 has the same yet opposite force
			e2.setForce(start, force.reverse());
		}
		e1.setForce(start, accumulatedForce);
	}
	
	/**
	 * Calculates the force that entity e2 exerts on e1.
	 * 
	 * @param e1
	 * @param e2
	 * @return
	 */
	private Vector getGravitationalForce(Entity e1, Entity e2)
	{
	// F = G * Mm / r^2
		Vector pos1 = e1.getPosition();
		Vector pos2 = e2.getPosition();
		Vector R = new Vector(pos1.getDimensions());
		double len = Math.pow(pos2.subtract(pos1).length(),2);
		if(len < 1000)
			return R;
		
		Vector posR = pos2.subtract(pos1).normalize();
		double f = GRAVITY_CONSTANT*e1.getMass()*e2.getMass();
		f /= len;
		
		return posR.multiplyByScalar(f);
	}
}