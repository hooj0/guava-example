package com.cnblogs.hoojo.concurrency.service.scheduled;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.google.common.util.concurrent.AbstractScheduledService;

/**
 * <b>function:</b> ScheduleService
 * 
 * @author hoojo
 * @createDate 2017年11月16日 下午5:56:57
 * @file ScheduleServiceTest.java
 * @package com.cnblogs.hoojo.concurrency.service.scheduled
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ScheduleServiceTest {

	// These constants are arbitrary and just used to make sure that the correct method is called
	// with the correct parameters.
	private static final int initialDelay = 10;
	private static final int delay = 20;
	private static final TimeUnit unit = TimeUnit.MILLISECONDS;

	// Unique runnable object used for comparison.
	final Runnable testRunnable = new Runnable() {
		@Override
		public void run() {
		}
	};
	boolean called = false;
	
	private void assertSingleCallWithCorrectParameters(Runnable command, long initialDelay, long delay, TimeUnit unit) {
		System.out.println("called:" + called); // only called once.
		called = true;
		
		System.out.println("initialDelay:" + initialDelay);
		System.out.println("delay:" + delay);
		System.out.println("unit:" + unit);
		System.out.println("command:" + command);
	}
	
	@Test
	public void testFixedRateSchedule() {
		
		AbstractScheduledService service = new AbstractScheduledService() {
			@Override
			protected void runOneIteration() throws Exception {
				System.out.println("runOneIteration......");
			}

			@Override
			protected ScheduledExecutorService executor() {
				System.out.println("executor......");
				
				return new ScheduledThreadPoolExecutor(1) {
					@Override
					public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
						assertSingleCallWithCorrectParameters(command, initialDelay, delay, unit);
						return null;
					}
				};
			}
			
			@Override
			protected Scheduler scheduler() {
				System.out.println("scheduler......");
				return Scheduler.newFixedRateSchedule(initialDelay, delay, unit);
			}
		};

		service.startAsync().awaitRunning();
		System.out.println("called:" + called);
	}
	
	private static class TestFailingCustomScheduledService extends AbstractScheduledService {
		final AtomicInteger numIterations = new AtomicInteger(0);
		final CyclicBarrier firstBarrier = new CyclicBarrier(2);
		final CyclicBarrier secondBarrier = new CyclicBarrier(2);

		@Override
		protected void runOneIteration() throws Exception {
			System.out.println("runOneIteration……");
			
			numIterations.incrementAndGet();
			System.out.println("first-awit:" + firstBarrier.await());
			System.out.println("second-awit:" + secondBarrier.await());
		}

		@Override
		protected ScheduledExecutorService executor() {
			System.out.println("executor……");
			// use a bunch of threads so that weird overlapping schedules are more likely to happen.
			return Executors.newScheduledThreadPool(10);
		}

		@Override
		protected Scheduler scheduler() {
			System.out.println("scheduler……");
			return new CustomScheduler() {
				@Override
				protected Schedule getNextSchedule() throws Exception {
					System.out.println("getNextSchedule……");
					
					if (numIterations.get() > 2) {
						throw new IllegalStateException("Failed");
					}
					return new Schedule(delay, unit);
				}
			};
		}
	}
	
	@Test
	public void testCustomSchedulerFailure() throws Exception {
		TestFailingCustomScheduledService service = new TestFailingCustomScheduledService();
		service.startAsync().awaitRunning();
		
		for (int i = 1; i < 4; i++) {
			System.out.println("first-awit:" + service.firstBarrier.await());
			System.out.println(service.numIterations.get());
			System.out.println("second-awit:" + service.secondBarrier.await());
		}
		Thread.sleep(1000);
		
		try {
			service.stopAsync().awaitTerminated(100, TimeUnit.SECONDS);
		} catch (IllegalStateException e) {
			System.out.println(service.state());
		}
	}
	
	private static class TestFailingCustomScheduledService2 extends AbstractScheduledService {
		final AtomicInteger numIterations = new AtomicInteger(0);

		@Override
		protected void runOneIteration() throws Exception {
			System.out.println("runOneIteration……");
			
			System.out.println(numIterations.incrementAndGet());
		}

		@Override
		protected ScheduledExecutorService executor() {
			System.out.println("executor……");
			// use a bunch of threads so that weird overlapping schedules are more likely to happen.
			return Executors.newScheduledThreadPool(10);
		}

		@Override
		protected Scheduler scheduler() {
			System.out.println("scheduler……");
			return new CustomScheduler() {
				@Override
				protected Schedule getNextSchedule() throws Exception {
					System.out.println("getNextSchedule……");
					
					/**
					 * 每 20 毫秒运行1次
					 */
					return new Schedule(delay, unit);
				}
			};
		}
	}
	
	@Test
	public void testCustomSchedulerFailure2() throws Exception {
		TestFailingCustomScheduledService2 service = new TestFailingCustomScheduledService2();
		service.startAsync().awaitRunning();
		
		Thread.sleep(40);
		try {
			service.stopAsync().awaitTerminated(100, TimeUnit.SECONDS);
		} catch (IllegalStateException e) {
			System.out.println(service.state());
		}
	}
	
	private static class TestAbstractScheduledCustomService extends AbstractScheduledService {
		final AtomicInteger numIterations = new AtomicInteger(0);
		final CyclicBarrier firstBarrier = new CyclicBarrier(2);
		final CyclicBarrier secondBarrier = new CyclicBarrier(2);
		volatile boolean useBarriers = true;

		@Override
		protected void runOneIteration() throws Exception {
			System.out.println("runOneIteration……");
			
			numIterations.incrementAndGet();
			if (useBarriers) {
				System.out.println("first-await:" + firstBarrier.await());
				System.out.println("second-await:" + secondBarrier.await());
			}
		}

		@Override
		protected ScheduledExecutorService executor() {
			System.out.println("executor……");
			// use a bunch of threads so that weird overlapping schedules are more likely to happen.
			return Executors.newScheduledThreadPool(10);
		}

		@Override
		protected void startUp() throws Exception {
			System.out.println("startUp……");
		}

		@Override
		protected void shutDown() throws Exception {
			System.out.println("shutDown……");
		}

		@Override
		protected Scheduler scheduler() {
			System.out.println("scheduler……");
			
			return new CustomScheduler() {
				@Override
				protected Schedule getNextSchedule() throws Exception {
					System.out.println("getNextSchedule……");
					
					return new Schedule(delay, unit);
				}
			};
		}
	}
	
	@Test
	public void testCustomSchedulerServiceStop() throws Exception {
		TestAbstractScheduledCustomService service = new TestAbstractScheduledCustomService();
		service.startAsync().awaitRunning();
		
		System.out.println("first-await:" + service.firstBarrier.await());
		System.out.println("first-get:" + service.numIterations.get());
		
		service.stopAsync();
		System.out.println("second-await:" + service.secondBarrier.await());
		service.awaitTerminated();
		
		// Sleep for a while just to ensure that our task wasn't called again.
		Thread.sleep(unit.toMillis(3 * delay));
		System.out.println("last-get:" + service.numIterations.get());
	}

	@Test
	public void testBig() throws Exception {
		TestAbstractScheduledCustomService service = new TestAbstractScheduledCustomService() {
			@Override
			protected Scheduler scheduler() {
				return new AbstractScheduledService.CustomScheduler() {
					@Override
					protected Schedule getNextSchedule() throws Exception {
						// Explicitly yield to increase the probability of a pathological scheduling.
						Thread.yield();
						return new Schedule(0, TimeUnit.SECONDS);
					}
				};
			}
		};
		
		service.useBarriers = false;
		service.startAsync().awaitRunning();
		Thread.sleep(50);
		
		service.useBarriers = true;
		System.out.println("--first-await: " + service.firstBarrier.await());
		System.out.println("--get:" + service.numIterations.get());
		
		service.stopAsync();
		System.out.println("--second-await: " + service.secondBarrier.await());
		service.awaitTerminated();
		System.out.println("--get:" + service.numIterations.get());
	}
}
