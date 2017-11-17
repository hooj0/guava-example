package com.cnblogs.hoojo.concurrency.service.scheduled;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.util.concurrent.AbstractScheduledService;

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
public class MyScheduledService extends AbstractScheduledService {

	public CyclicBarrier runFirstBarrier = new CyclicBarrier(2);
	public CyclicBarrier runSecondBarrier = new CyclicBarrier(2);

	public volatile boolean startUpCalled = false;
	public volatile boolean shutDownCalled = false;

	public AtomicInteger numberOfTimesRunCalled = new AtomicInteger(0);
	public AtomicInteger numberOfTimesExecutorCalled = new AtomicInteger(0);
	public AtomicInteger numberOfTimesSchedulerCalled = new AtomicInteger(0);

	public volatile Exception runException = null;
	public volatile Exception startUpException = null;
	public volatile Exception shutDownException = null;

	public volatile ScheduledFuture<?> future = null;
	public volatile Scheduler configuration = Scheduler.newFixedDelaySchedule(0, 10, TimeUnit.MILLISECONDS);
	final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(10) {
		@Override
		public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
			return future = super.scheduleWithFixedDelay(command, initialDelay, delay, unit);
		}
	};

	@Override
	protected void runOneIteration() throws Exception {
		System.out.println("MyScheduledService.runOneIteration...");
		System.out.println("startUpCalled: " + startUpCalled + ", shutDownCalled: " + shutDownCalled + ", States: " + state());
		
		numberOfTimesRunCalled.incrementAndGet();
		
		runFirstBarrier.await();
		runSecondBarrier.await();
		
		if (runException != null) {
			throw runException;
		}
	}

	@Override
	protected void startUp() throws Exception {
		System.out.println("MyScheduledService.startUp...");
		System.out.println("startUpCalled: " + startUpCalled + ", shutDownCalled: " + shutDownCalled + ", States: " + state());
		
		startUpCalled = true;
		if (startUpException != null) {
			throw startUpException;
		}
	}

	@Override
	protected void shutDown() throws Exception {
		System.out.println("MyScheduledService.shutDown...");
		System.out.println("startUpCalled: " + startUpCalled + ", shutDownCalled: " + shutDownCalled + ", States: " + state());
		
		shutDownCalled = true;
		if (shutDownException != null) {
			throw shutDownException;
		}
	}

	@Override
	protected ScheduledExecutorService executor() {
		System.out.println("MyScheduledService.executor...");
		
		numberOfTimesExecutorCalled.incrementAndGet();
		return executor;
	}

	@Override
	protected Scheduler scheduler() {
		System.out.println("MyScheduledService.scheduler...");
		System.out.println("startUpCalled: " + startUpCalled + ", shutDownCalled: " + shutDownCalled + ", States: " + state());

		numberOfTimesSchedulerCalled.incrementAndGet();
		return configuration;
	}
}
