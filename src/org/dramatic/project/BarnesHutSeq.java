package org.dramatic.project;

import org.dramatic.project.EntityFactory.forcelist_type;
import org.dramatic.project.updaters.BarnesHutGravity;
import org.dramatic.project.updaters.EntityUpdater;

public class BarnesHutSeq extends AbstractMain {
	
	public BarnesHutSeq(String[] args) {
		super(args);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		BarnesHutSeq s = new BarnesHutSeq(args);
	}

	@Override
	public EntityUpdater getUpdater() {
		BarnesHutGravity updater = new BarnesHutGravity(EntityFactory.POS_MAGNITUDE_FACTOR * 2, far);
		return updater;
	}
	
	@Override
	public void init() {
		EntityFactory.FORCELIST_TYPE = forcelist_type.SINGLE;		
	}


}
