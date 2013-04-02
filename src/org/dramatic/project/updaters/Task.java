package org.dramatic.project.updaters;

import java.util.concurrent.CountDownLatch;

public abstract class Task implements Runnable
{
	protected static CountDownLatch done;
	
	public static void initDoneCountDownLatch(CountDownLatch latch)
	{
		done = latch;
	}
	
	public void run()
	{
		doTask();
		if(done != null)done.countDown();
	}
	
	public abstract void doTask();
	
}
