package org.dramatic.project;

import java.io.OutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.dramatic.project.updaters.BarnesHutGravity;
import org.dramatic.project.updaters.BarnesHutGravityMulti;
import org.dramatic.project.updaters.CollisionForce;
import org.dramatic.project.updaters.EntityUpdater;
import org.dramatic.project.updaters.GravitationalForce;
import org.dramatic.project.updaters.GravitationalForceSEQ;

public class ResultGenerator {
	
	private Engine engine;
	
	public ResultGenerator(Engine engine)
	{
		this.engine = engine; 
	}
	
	public void printToStream(OutputStream stream, int steps, int repeats)
	{
		for(int i = 0; i < repeats; i++)
		{
			long start_time = System.currentTimeMillis();
			engine.run(steps);
			long end_time = System.currentTimeMillis();
			System.out.println(end_time - start_time + " ms");
		}
	}
	
	private static List<Entity> entities = null;
	
	private static Engine createDefaultEngine(boolean BARNESHUT, EntityUpdater up, int threads, int num_entities)
	{
		entities = EntityFactory.getEntities(num_entities);
		
		if(BARNESHUT)
			for(Entity e: entities)
				e.initForceList(1);
		else
			for(Entity e: entities)
				e.initForceList(num_entities);
		
		List<EntityUpdater> ups = new ArrayList<EntityUpdater>();
		//ups.add(new CollisionForce(8));
		ups.add(up);
		
		return new Engine(entities,ups);		
	}

	public static void main(String[] args)
	{		
		List<EntityUpdater> ups = new ArrayList<EntityUpdater>();
		List<String> names = new ArrayList<String>();
		
		int steps = 500;
		int repeats = 15;
		if(args.length > 1)
		{
			steps = Integer.parseInt(args[0]);
			repeats = Integer.parseInt(args[1]);
		}
		
		System.out.println("Running tests with " + steps + " steps and " + repeats + " repeats");		
		
		 
		// Fuzzing to get java vm to start properly.
		int b;
		for(int i = 1; i < Integer.MAX_VALUE; i++)
		{
			for(int y = 0; y < Integer.MAX_VALUE; y++)
			{
				b = y / i;
			}
		}
		
		ups.add(new GravitationalForceSEQ());
		names.add("GravitationalForce Sequential");
		for(int i = 1; i < 4; i++)
		{
			int num_threads = 1 << i;
			ups.add(new GravitationalForce(num_threads));
			names.add("GravitationalForce (" + num_threads + ")");
		}

		ups.add(new BarnesHutGravity(10000000000D, 0.5));
		names.add("Barnes-Hut Sequential");
		for(int i = 1; i < 4; i++)
		{
			int num_threads = 1 << i;
			ups.add(new BarnesHutGravityMulti(num_threads, 10000000000D, 0.5));
			names.add("Barnes-Hut (" + num_threads + ")");
		}	
		
		
		int num_collision_threads = 8;
		int num_entities[] = {120, 180, 240};		
		// Create engines and run.
		for(int i = 0; i < ups.size(); i++)
		{
			for(int num : num_entities)
			{
				boolean barnes = ups.get(i).getClass().getName().toLowerCase().indexOf("barnes") > 1;
				Engine engine = createDefaultEngine(barnes, ups.get(i), num_collision_threads, num);
				ResultGenerator gen = new ResultGenerator(engine);
				System.out.println(names.get(i) + " " + num + ": ");
				gen.printToStream(System.out, steps, repeats);
			}
		}
		
	}
}
