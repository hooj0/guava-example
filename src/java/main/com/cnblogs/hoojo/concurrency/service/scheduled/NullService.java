package com.cnblogs.hoojo.concurrency.service.scheduled;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.AbstractScheduledService;

/**
 * <b>function:</b> 空服务
 * @author hoojo
 * @createDate 2017年11月15日 下午5:19:22
 * @file NullService.java
 * @package com.cnblogs.hoojo.concurrency.service.scheduled
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class NullService extends AbstractScheduledService {
	
	public volatile Scheduler configuration = Scheduler.newFixedDelaySchedule(0, 10, TimeUnit.MILLISECONDS);
	public volatile ScheduledFuture<?> future = null;

	public volatile boolean atFixedRateCalled = false;
	public volatile boolean withFixedDelayCalled = false;
	public volatile boolean scheduleCalled = false;

	public final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(10) {
		@Override
		public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
			return future = super.scheduleWithFixedDelay(command, initialDelay, delay, unit);
		}
	};
	
	@Override
	protected void runOneIteration() throws Exception {
	}

	@Override
	protected Scheduler scheduler() {
		return configuration;
	}

	@Override
	protected ScheduledExecutorService executor() {
		return executor;
	}
}
