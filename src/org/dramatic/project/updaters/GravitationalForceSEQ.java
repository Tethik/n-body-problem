package org.dramatic.project.updaters;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dramatic.project.Entity;
import org.dramatic.project.Vector;

public class GravitationalForceSEQ implements EntityUpdater
{
	public final static double GRAVITY_CONSTANT = 6.67384 * Math.pow(10, -11);
	
	public void update(List<Entity> collection) 
	{	
		int start = 0;
		for(Entity e1: collection)
		{
			
			Vector force = new Vector(e1.getAcceleration().getDimensions());
		// Contains a set of entities that have already been updated against entity e1
			for(int i = start; i  < collection.size(); i++)
			{
				Entity e2 = collection.get(i);
				if( e1.equals(e2) )
					continue;
				
				Vector gravityForce = getGravitationalForce(e1, e2);
				force = force.add(gravityForce);
			// E2 has the same yet opposite force
				e2.setForce(start, gravityForce.reverse());
			}
			if(start < collection.size()) e1.setForce(start, force);
			start++;
		}
		
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
