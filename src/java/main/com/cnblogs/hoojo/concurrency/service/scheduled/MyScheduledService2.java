package com.cnblogs.hoojo.concurrency.service.scheduled;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * <b> 固定频率任务，固定延迟任务 </b>
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
		
		// 固定延迟任务，等待0秒后立即运行，没间隔1秒运行异常，每次运行需要等待前置任务完成后才能运行
		return Scheduler.newFixedDelaySchedule(0, 1, TimeUnit.SECONDS);
	}
}
