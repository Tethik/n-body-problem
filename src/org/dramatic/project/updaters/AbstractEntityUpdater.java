package org.dramatic.project.updaters;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import org.dramatic.project.Entity;

public abstract class AbstractEntityUpdater implements EntityUpdater 
{
	private int numThreads  = -1;
	protected ExecutorService executor = null;
	
	public AbstractEntityUpdater(int threadCount)
	{
		if(threadCount > 1)
		{
			executor = new ForkJoinPool(threadCount);
		}
		else 
			throw new IllegalArgumentException("AnstractEntityUpdater: Must be greater than 1");			
		this.numThreads = threadCount;
	}
	
	@Override
	public void update(List<Entity> collection)
	{
		List<Runnable> tasks = getTasks(collection);
		
		CountDownLatch done = null;

		done = new CountDownLatch(tasks.size());
		Task.initDoneCountDownLatch(done);
		for(Runnable task: tasks)
			executor.execute(task);
		try {
			done.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public abstract List<Runnable> getTasks(List<Entity> collection);

	/**
	 * Returns the numThreads in the threadpool.
	 * Returns -1 if the threadpool is not initialized.
	 * @return
	 */
	public int getNumThreads() 
	{
		return numThreads;
	}
	
}
