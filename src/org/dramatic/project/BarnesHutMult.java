package org.dramatic.project;

import org.dramatic.project.EntityFactory.forcelist_type;
import org.dramatic.project.updaters.BarnesHutGravityMulti;
import org.dramatic.project.updaters.EntityUpdater;

public class BarnesHutMult extends AbstractMain {

	public BarnesHutMult(String[] args) {
		super(args);
	}

	
	public static void main(String[] args)
	{
		BarnesHutMult hut = new BarnesHutMult(args);
		
	}


	@Override
	public EntityUpdater getUpdater() {
		BarnesHutGravityMulti updater = new BarnesHutGravityMulti(numThreads, EntityFactory.POS_MAGNITUDE_FACTOR * 2, far);		
		return updater;
	}


	@Override
	public void init() {
		EntityFactory.FORCELIST_TYPE = forcelist_type.SINGLE;
		numThreads = (numThreads > 1) ? numThreads : 2;
	}

}
