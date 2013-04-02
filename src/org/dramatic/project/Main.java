package org.dramatic.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.dramatic.project.EntityFactory.forcelist_type;
import org.dramatic.project.updaters.BarnesHutGravity;
import org.dramatic.project.updaters.BarnesHutGravityMulti;
import org.dramatic.project.updaters.CollisionForce;
import org.dramatic.project.updaters.CollisionForceSEQ;
import org.dramatic.project.updaters.EntityUpdater;
import org.dramatic.project.updaters.GravitationalForce;
import org.dramatic.project.updaters.GravitationalForceSEQ;
import org.dramatic.project.visualiser.Visualiser2D;

public class Main extends AbstractMain {
	
	private boolean barnes;
	private int width;
	private int height;
	private Visualiser2D vis;
	
	public Main(String[] args) {		
		super(args);
	}
	
	
	@Override
	public void init() {
		
		width = 640;
		height = 480;
		barnes = false;
		
		if(args.length > 6 && args[5] != null && args[6] != null)
		{
			System.out.println("Sanity check!");
			width = Integer.parseInt(args[5]);
			height = Integer.parseInt(args[6]);
		}
		
		if(args.length > 7)
			barnes = Boolean.parseBoolean(args[7]);
		
		System.out.println("Width: " + width);
		System.out.println("Height: " + height);
		
		
		
		if(barnes)
			EntityFactory.FORCELIST_TYPE = forcelist_type.SINGLE;
		else
			EntityFactory.FORCELIST_TYPE = forcelist_type.ALL;
	}

	@Override
	public EntityUpdater getUpdater() {		
		vis = new Visualiser2D(width,height,true);
		engine.addListener(vis);
		
		if(!barnes)		
			if(numThreads > 1)
				return new GravitationalForce(numThreads);
			else
				return new GravitationalForceSEQ();
		
		if(numThreads > 1)
			return new BarnesHutGravityMulti(numThreads, EntityFactory.POS_MAGNITUDE_FACTOR * 2, far);
		else
			return new BarnesHutGravity(EntityFactory.POS_MAGNITUDE_FACTOR * 2, far);
	}
	
	public static void main(String[] args) 
	{	
		Main main = new Main(args);
		main.waitForInput();		
	}
	
	
	public void waitForInput()
	{
		if(vis != null)
		vis.waitForInput();
	}


}
