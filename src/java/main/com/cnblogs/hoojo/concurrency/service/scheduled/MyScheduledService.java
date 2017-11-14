package com.cnblogs.hoojo.concurrency.service.scheduled;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.util.concurrent.AbstractScheduledService;

/**
 * 固定频率任务，固定延迟任务
 * fixedRate: 固定频率任务，
 * 		任务两次执行时间间隔是任务的开始点，固定在某个点运行。
 * 		是以固定频率来执行线程任务，固定频率的含义就是可能设定的固定时间不足以完成线程任务，
 * 		但是它不管，达到设定的延迟时间了就要执行下一次了。
 * fixedDelay: 固定延迟任务，
 * 		是前次任务的结束与下次任务的开始，也就是说下一次任务必须等待上一次运行结束后再运行，固定间隔时间。
 * 		从字面意义上可以理解为就是以固定延迟（时间）来执行线程任务，它实际上是不管线程任务的执行时间的，
 * 		每次都要把任务执行完成后再延迟固定时间后再执行下一次。
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
	// 1 秒后运行任务，每500毫秒运行一次，每次运行需要等待前置任务完成后才能运行
	public volatile Scheduler configuration = Scheduler.newFixedDelaySchedule(1000, 500, TimeUnit.MILLISECONDS);
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
