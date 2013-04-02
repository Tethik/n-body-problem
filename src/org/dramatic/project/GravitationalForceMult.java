package org.dramatic.project;

import org.dramatic.project.EntityFactory.forcelist_type;
import org.dramatic.project.updaters.EntityUpdater;
import org.dramatic.project.updaters.GravitationalForce;

public class GravitationalForceMult extends AbstractMain {

	public GravitationalForceMult(String[] args) {
		super(args);
	}
	
	@Override
	public void init() {
		EntityFactory.FORCELIST_TYPE = forcelist_type.ALL;		
		numThreads = (numThreads > 1) ? numThreads : 2;
	}


	@Override
	public EntityUpdater getUpdater() {
		GravitationalForce updater = new GravitationalForce(numThreads);
		return updater;
	}
	
	public static void main(String args[])
	{
		GravitationalForceMult m = new GravitationalForceMult(args);
	}

	
}
