package org.dramatic.project;

import java.util.List;

import org.dramatic.project.updaters.CollisionForce;
import org.dramatic.project.updaters.CollisionForceSEQ;
import org.dramatic.project.updaters.EntityUpdater;

public abstract class AbstractMain {
	
	protected int gnumBodies;
	protected int numSteps;
	protected float far = 0.5f;
	protected int numThreads = 1;
	protected Engine engine;
	protected boolean collision = true;
	protected String[] args;
	
	public AbstractMain(String[] args)
	{
		this.args = args;
		if(args.length < 2)
		{
			System.out.println("Usage: [gnumBodies] [numSteps] (far=0.5) (numThreads=1 || 2) (collision=true)");
			return;
		}
		
		/*
		for(String arg : args)
			System.out.println(arg);
		*/
		
		this.gnumBodies = Integer.parseInt(args[0]);
		this.numSteps = Integer.parseInt(args[1]);
		if(args.length > 2)
			this.far = Float.parseFloat(args[2]);
		if(args.length > 3)
			this.numThreads = Integer.parseInt(args[3]);
		if(args.length > 4)
			this.collision = Boolean.parseBoolean(args[4]);
		
		init();
		
		List<Entity> entities = EntityFactory.getEntities(gnumBodies);		
		engine = new Engine(entities);	
			
		engine.addUpdater(getUpdater());
		
		if(collision) {
			if(numThreads > 1)
				engine.addUpdater(new CollisionForce(numThreads));
			else
				engine.addUpdater(new CollisionForceSEQ());		
		}
		
		System.out.println("gnumBodies: " + gnumBodies);
		System.out.println("numSteps: " + numSteps);
		System.out.println("far: " + far);
		System.out.println("numThreads: " + numThreads);
		System.out.println("collision: " + collision);
		System.out.println("Updaters:");
		for(EntityUpdater updater : engine.getUpdaters())
			System.out.println(updater.getClass().getName());
		
		System.out.println();
		System.out.println("Now running.");
		long start_time = System.nanoTime();
		engine.run(numSteps);
		long end_time = System.nanoTime();
		
		System.out.println(end_time - start_time + " ns");
	}
	
	
	public abstract void init();
	public abstract EntityUpdater getUpdater();
	
}
