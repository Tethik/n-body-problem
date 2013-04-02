package org.dramatic.project;

import org.dramatic.project.EntityFactory.forcelist_type;
import org.dramatic.project.updaters.EntityUpdater;
import org.dramatic.project.updaters.GravitationalForceSEQ;

public class GravitationalForceSeq extends AbstractMain {

	public GravitationalForceSeq(String[] args) {
		super(args);
	}
	
	@Override
	public void init() {
		EntityFactory.FORCELIST_TYPE = forcelist_type.ALL;	
	}

	@Override
	public EntityUpdater getUpdater() {
		GravitationalForceSEQ updater = new GravitationalForceSEQ();
		return updater;
	}
	
	public static void main(String args[])
	{
		GravitationalForceSeq s = new GravitationalForceSeq(args);
	}

}
