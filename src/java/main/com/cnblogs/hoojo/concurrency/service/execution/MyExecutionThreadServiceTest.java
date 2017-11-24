package com.cnblogs.hoojo.concurrency.service.execution;

import static org.junit.Assert.assertFalse;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;

/**
 * AbstractExecutionThreadService 异步线程任务执行器示例
 * @author hoojo
 * @createDate 2017年11月24日 上午11:19:00
 * @file MyExecutionThreadServiceTest.java
 * @package com.cnblogs.hoojo.concurrency.service.execution
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class MyExecutionThreadServiceTest {

	private static final long LONG_TIMEOUT_MILLIS = 2500;
	TimeUnit SECONDS = TimeUnit.SECONDS;
	
	private static class NoOpThreadedService extends AbstractExecutionThreadService {
		final CountDownLatch latch = new CountDownLatch(1);

		@Override
		protected void run() throws Exception {
			System.out.println("---run---start");
			latch.await();
			System.out.println("---run---end");
		}

		@Override
		protected void triggerShutdown() {
			System.out.println("triggerShutdown......");
			latch.countDown();
		}
	}
	
	@Test
	public void testListenerDoesntDeadlockOnStartAndWaitFromRunning() throws Exception {
		final NoOpThreadedService service = new NoOpThreadedService();
		
		service.addListener(new Listener() {
			@Override
			public void running() {
				service.awaitRunning();
			}
		}, MoreExecutors.directExecutor());
		
		service.startAsync().awaitRunning(LONG_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
		service.stopAsync();
	}

	@Test
	public void testListenerDoesntDeadlockOnStopAndWaitFromTerminated() throws Exception {
		final NoOpThreadedService service = new NoOpThreadedService();
		service.addListener(new Listener() {
			@Override
			public void terminated(State from) {
				service.stopAsync().awaitTerminated();
			}
		}, MoreExecutors.directExecutor());
		service.startAsync().awaitRunning();

		Thread thread = new Thread() {
			@Override
			public void run() {
				service.stopAsync().awaitTerminated();
			}
		};
		thread.start();
		thread.join(LONG_TIMEOUT_MILLIS);
		assertFalse(thread + " is deadlocked", thread.isAlive());
	}
}
