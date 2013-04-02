package org.dramatic.project;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.dramatic.project.updaters.EntityUpdater;

public class Engine {
	
	private List<Entity> collection;
	private List<EntityUpdater> updaters;
	private List<EngineOutputListener> listeners = new ArrayList<EngineOutputListener>();
	
	public Engine(List<Entity> collection)
	{
		this(collection, new ArrayList<EntityUpdater>());
	}
	
	public Engine(List<Entity> collection, List<EntityUpdater> updaters) 
	{
		this.collection = collection;
		this.updaters = updaters;
	}
	
	public void addListener(EngineOutputListener listener)
	{
		listeners.add(listener);
	}
	
	public void addUpdater(EntityUpdater updater)
	{
		updaters.add(updater);
	}
	
	public void run(int steps)
	{
		for(int i = 0; i < steps; i++)
		{
			for(EntityUpdater updater : updaters)
			{
				updater.update(collection);
			}
			
			for(Entity e : collection)
			{
				e.Update();
			}
			
			for(EngineOutputListener listen : listeners)
				listen.handleOutput(collection);
		}
	}
	
	public List<EntityUpdater> getUpdaters()
	{
		return updaters;
	}
	
	public void stop() 
	{
		// Hammertime!
	}
}
