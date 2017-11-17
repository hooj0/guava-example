package com.cnblogs.hoojo.concurrency.service.scheduled;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * <b>function:</b> 周期性任务，定时任务
 * 
 * @author hoojo
 * @createDate 2017年11月14日 上午11:44:50
 * @file MyScheduledService.java
 * @package com.cnblogs.hoojo.concurrency.service
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class MyScheduledService2 extends AbstractScheduledService {

	public final CountDownLatch terminationLatch = new CountDownLatch(1);
	public volatile ScheduledExecutorService executorService;

	public Exception startException;
	@Override
	protected void startUp() throws Exception {
		System.out.println("start...");
		
		if (startException != null) {
			throw startException;
		}
	}

	@Override
	protected void runOneIteration() throws Exception {
		System.out.println("runOneIteration...");
	}

	@Override
	public ScheduledExecutorService executor() {
		if (executorService == null) {
			executorService = super.executor();
			// Add a listener that will be executed after the listener that shuts down the executor.
			addListener(new Listener() {
				@Override
				public void terminated(State from) {
					System.out.println("terminated...");
					
					if (startException == null)
						terminationLatch.countDown();
				}

				@Override
				public void failed(State from, Throwable failure) {
					System.out.println("failed..." + failure.getMessage());
					
					if (startException != null)
						terminationLatch.countDown();
				}
			}, MoreExecutors.directExecutor());
		}
		return executorService;
	}

	@Override
	protected Scheduler scheduler() {
		System.out.println("scheduler...");
		
		return Scheduler.newFixedDelaySchedule(0, 1, TimeUnit.MILLISECONDS);
	}
}
