/*
 * Copyright (C) 2011 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cnblogs.hoojo.concurrency.service.scheduled;

import static com.google.common.util.concurrent.AbstractScheduledService.Scheduler.newFixedDelaySchedule;
import static com.google.common.util.concurrent.MoreExecutors.directExecutor;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.cnblogs.hoojo.concurrency.testing.TestingExecutors;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.common.util.concurrent.AbstractScheduledService.Scheduler;
import com.google.common.util.concurrent.AbstractService;
import com.google.common.util.concurrent.Atomics;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;

import junit.framework.TestCase;

/**
 * <b>function:</b> 固定频率任务，固定延迟任务
 * fixedRate: 固定频率任务，
 * 		任务两次执行时间间隔是任务的开始点，固定在某个点运行。
 * 		是以固定频率来执行线程任务，固定频率的含义就是可能设定的固定时间不足以完成线程任务，
 * 		但是它不管，达到设定的延迟时间了就要执行下一次了。
 * fixedDelay: 固定延迟任务，
 * 		是前次任务的结束与下次任务的开始，也就是说下一次任务必须等待上一次运行结束后再运行，固定间隔时间。
 * 		从字面意义上可以理解为就是以固定延迟（时间）来执行线程任务，它实际上是不管线程任务的执行时间的，
 * 		每次都要把任务执行完成后再延迟固定时间后再执行下一次。
 */
public class AbstractScheduledServiceTest extends TestCase {


	volatile boolean atFixedRateCalled = false;
	volatile boolean withFixedDelayCalled = false;
	volatile boolean scheduleCalled = false;

	volatile ScheduledFuture<?> future = null;
	/** 固定延时任务，在上一次运行完成后，间隔时间抵达后运行。 */
	volatile Scheduler configuration = newFixedDelaySchedule(0, 10, TimeUnit.MILLISECONDS);
	final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(10) {
		@Override
		public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
			return future = super.scheduleWithFixedDelay(command, initialDelay, delay, unit);
		}
	};
	
	private class NullService extends AbstractScheduledService {
		@Override
		protected void runOneIteration() throws Exception {
			System.out.println("runOneIteration.......");
		}

		@Override
		protected Scheduler scheduler() {
			System.out.println("scheduler.......");
			return configuration;
		}

		@Override
		protected ScheduledExecutorService executor() {
			System.out.println("executor.......");
			return executor;
		}
	}

	public void testServiceStartStop() throws Exception {
		NullService service = new NullService();
		RecordingListener.record(service);
		
		// startAsync -> doStart -> executor & startUp & scheduler & notifyStarted
		// scheduler -> if (future.isCancelled() == false) { AbstractScheduledService.this.runOneIteration }
		service.startAsync().awaitRunning();
		System.out.println("isRunning: " + service.isRunning());
		System.out.println("state: " + service.state());
		System.out.println("isCancelled: " + future.isCancelled()); // false

		assertFalse(future.isDone());
		
		// stopAsync -> doStop -> cancel & if (state == Stopping) { shutDown & notifyStop }
		service.stopAsync().awaitTerminated();
		System.out.println("isRunning: " + service.isRunning());
		System.out.println("state: " + service.state());
		System.out.println("isCancelled: " + future.isCancelled()); // false
		
		assertTrue(future.isCancelled());
	}
	
	private class TestService extends AbstractScheduledService {
		CyclicBarrier runFirstBarrier = new CyclicBarrier(2);
		CyclicBarrier runSecondBarrier = new CyclicBarrier(2);

		volatile boolean startUpCalled = false;
		volatile boolean shutDownCalled = false;
		AtomicInteger numberOfTimesRunCalled = new AtomicInteger(0);
		AtomicInteger numberOfTimesExecutorCalled = new AtomicInteger(0);
		AtomicInteger numberOfTimesSchedulerCalled = new AtomicInteger(0);
		volatile Exception runException = null;
		volatile Exception startUpException = null;
		volatile Exception shutDownException = null;

		@Override
		protected void runOneIteration() throws Exception {
			System.out.println("runOneIteration..........");
			
			assertTrue(startUpCalled);
			assertFalse(shutDownCalled);
			numberOfTimesRunCalled.incrementAndGet();
			assertEquals(State.RUNNING, state());
			runFirstBarrier.await();
			runSecondBarrier.await();
			if (runException != null) {
				System.out.println("runOneIteration exception");
				throw runException;
			}
		}

		@Override
		protected void startUp() throws Exception {
			System.out.println("startUp..........");
			
			assertFalse(startUpCalled);
			assertFalse(shutDownCalled);
			startUpCalled = true;
			assertEquals(State.STARTING, state());
			if (startUpException != null) {
				System.out.println("startUp exception");
				
				throw startUpException;
			}
		}

		@Override
		protected void shutDown() throws Exception {
			System.out.println("shutDown..........");
			
			assertTrue(startUpCalled);
			assertFalse(shutDownCalled);
			shutDownCalled = true;
			if (shutDownException != null) {
				System.out.println("shutDown exception");
				
				throw shutDownException;
			}
		}

		@Override
		protected ScheduledExecutorService executor() {
			System.out.println("executor..........");
			
			numberOfTimesExecutorCalled.incrementAndGet();
			return executor;
		}

		@Override
		protected Scheduler scheduler() {
			System.out.println("scheduler..........");
			
			numberOfTimesSchedulerCalled.incrementAndGet();
			return configuration;
		}
	}

	public void testFailOnExceptionFromRun() throws Exception {
		TestService service = new TestService();
		RecordingListener.record(service);
		
		service.runException = new Exception("runOneIteration fail exception");
		
		// startAsync -> doStart -> executor & startUp & scheduler & notifyStarted
		// scheduler -> if (future.isCancelled() == false) { AbstractScheduledService.this.runOneIteration -> exception}
		// AbstractScheduledService.this.runOneIteration -> exception -> shutDown & notifyFailed
		service.startAsync().awaitRunning();
		
		service.runFirstBarrier.await();
		service.runSecondBarrier.await();
		
		try {
			future.get();
			fail();
		} catch (CancellationException expected) {
			System.out.println(expected);
			System.out.println(service.failureCause()); // Exception: runOneIteration fail exception
		}
		
		// An execution exception holds a runtime exception (from throwables.propogate) that holds our
		// original exception.
		assertEquals(service.runException, service.failureCause());
		assertEquals(Service.State.FAILED, service.state());
	}

	public void testFailOnExceptionFromStartUp() {
		TestService service = new TestService();
		RecordingListener.record(service);
		
		service.startUpException = new Exception("startUp Exception");
		
		try {
			// startAsync -> doStart -> executor & startUp & scheduler & notifyStarted
			// startUp -> fail exception -> cancel & notifyFailed
			service.startAsync().awaitRunning();
			fail();
		} catch (IllegalStateException e) {
			System.out.println(e.getCause());
			System.out.println(service.failureCause());
		}
		
		System.out.println("isRunning: " + service.isRunning());
		System.out.println("state: " + service.state());
		System.out.println("isCancelled: " + future.isCancelled()); // false
		
		assertEquals(0, service.numberOfTimesRunCalled.get());
		assertEquals(Service.State.FAILED, service.state());
	}

	public void testFailOnErrorFromStartUpListener() throws InterruptedException {
		final Error error = new Error();
		final CountDownLatch latch = new CountDownLatch(1);
		TestService service = new TestService();
		service.addListener(new Service.Listener() {
			@Override
			public void running() {
				throw error;
			}

			@Override
			public void failed(State from, Throwable failure) {
				assertEquals(State.RUNNING, from);
				assertEquals(error, failure);
				latch.countDown();
			}
		}, directExecutor());
		service.startAsync();
		latch.await();

		assertEquals(0, service.numberOfTimesRunCalled.get());
		assertEquals(Service.State.FAILED, service.state());
	}

	public void testFailOnExceptionFromShutDown() throws Exception {
		TestService service = new TestService();
		service.shutDownException = new Exception();
		service.startAsync().awaitRunning();
		service.runFirstBarrier.await();
		service.stopAsync();
		service.runSecondBarrier.await();
		try {
			service.awaitTerminated();
			fail();
		} catch (IllegalStateException e) {
			assertEquals(service.shutDownException, e.getCause());
		}
		assertEquals(Service.State.FAILED, service.state());
	}

	public void testRunOneIterationCalledMultipleTimes() throws Exception {
		TestService service = new TestService();
		service.startAsync().awaitRunning();
		for (int i = 1; i < 10; i++) {
			service.runFirstBarrier.await();
			assertEquals(i, service.numberOfTimesRunCalled.get());
			service.runSecondBarrier.await();
		}
		service.runFirstBarrier.await();
		service.stopAsync();
		service.runSecondBarrier.await();
		service.stopAsync().awaitTerminated();
	}

	public void testExecutorOnlyCalledOnce() throws Exception {
		TestService service = new TestService();
		service.startAsync().awaitRunning();
		// It should be called once during startup.
		assertEquals(1, service.numberOfTimesExecutorCalled.get());
		for (int i = 1; i < 10; i++) {
			service.runFirstBarrier.await();
			assertEquals(i, service.numberOfTimesRunCalled.get());
			service.runSecondBarrier.await();
		}
		service.runFirstBarrier.await();
		service.stopAsync();
		service.runSecondBarrier.await();
		service.stopAsync().awaitTerminated();
		// Only called once overall.
		assertEquals(1, service.numberOfTimesExecutorCalled.get());
	}

	public void testDefaultExecutorIsShutdownWhenServiceIsStopped() throws Exception {
		final AtomicReference<ScheduledExecutorService> executor = Atomics.newReference();
		
		AbstractScheduledService service = new AbstractScheduledService() {
			@Override
			protected void runOneIteration() throws Exception {
			}

			@Override
			public ScheduledExecutorService executor() {
				executor.set(super.executor());
				return executor.get();
			}

			@Override
			protected Scheduler scheduler() {
				return newFixedDelaySchedule(0, 1, TimeUnit.MILLISECONDS);
			}
			
			
		};

		service.startAsync();
		//assertFalse(service.executor().isShutdown());
		
		service.awaitRunning();
		service.stopAsync();
		service.awaitTerminated();
		assertTrue(executor.get().awaitTermination(100, TimeUnit.MILLISECONDS));
	}

	public void testDefaultExecutorIsShutdownWhenServiceFails() throws Exception {
		final AtomicReference<ScheduledExecutorService> executor = Atomics.newReference();
		AbstractScheduledService service = new AbstractScheduledService() {
			@Override
			protected void startUp() throws Exception {
				throw new Exception("Failed");
			}

			@Override
			protected void runOneIteration() throws Exception {
			}

			@Override
			protected ScheduledExecutorService executor() {
				executor.set(super.executor());
				return executor.get();
			}

			@Override
			protected Scheduler scheduler() {
				return newFixedDelaySchedule(0, 1, TimeUnit.MILLISECONDS);
			}
		};

		try {
			service.startAsync().awaitRunning();
			fail("Expected service to fail during startup");
		} catch (IllegalStateException expected) {
		}

		assertTrue(executor.get().awaitTermination(100, TimeUnit.MILLISECONDS));
	}

	public void testSchedulerOnlyCalledOnce() throws Exception {
		TestService service = new TestService();
		service.startAsync().awaitRunning();
		// It should be called once during startup.
		assertEquals(1, service.numberOfTimesSchedulerCalled.get());
		for (int i = 1; i < 10; i++) {
			service.runFirstBarrier.await();
			assertEquals(i, service.numberOfTimesRunCalled.get());
			service.runSecondBarrier.await();
		}
		service.runFirstBarrier.await();
		service.stopAsync();
		service.runSecondBarrier.await();
		service.awaitTerminated();
		// Only called once overall.
		assertEquals(1, service.numberOfTimesSchedulerCalled.get());
	}

	public void testTimeout() {
		// Create a service whose executor will never run its commands
		Service service = new AbstractScheduledService() {
			@Override
			protected Scheduler scheduler() {
				return Scheduler.newFixedDelaySchedule(0, 1, TimeUnit.NANOSECONDS);
			}

			@Override
			protected ScheduledExecutorService executor() {
				return TestingExecutors.noOpScheduledExecutor();
			}

			@Override
			protected void runOneIteration() throws Exception {
			}

			@Override
			protected String serviceName() {
				return "Foo";
			}
		};
		
		try {
			service.startAsync().awaitRunning(1, TimeUnit.MILLISECONDS);
			fail("Expected timeout");
		} catch (TimeoutException e) {
			System.out.println(e); //"Timed out waiting for Foo [STARTING] to reach the RUNNING state."
		}
	}

	public static class SchedulerTest extends TestCase {
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
			assertFalse(called); // only called once.
			called = true;
			assertEquals(SchedulerTest.initialDelay, initialDelay);
			assertEquals(SchedulerTest.delay, delay);
			assertEquals(SchedulerTest.unit, unit);
			assertEquals(testRunnable, command);
		}

		public void testFixedRateSchedule() {
			
			/*Scheduler schedule = Scheduler.newFixedRateSchedule(initialDelay, delay, unit);
			
			Future<?> unused = schedule.schedule(new NullService(), new ScheduledThreadPoolExecutor(1) {
				@Override
				public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
					assertSingleCallWithCorrectParameters(command, initialDelay, delay, unit);
					return null;
				}
			}, testRunnable);
			assertTrue(called);*/
		}

		public void testFixedDelaySchedule() {
			/*Scheduler schedule = newFixedDelaySchedule(initialDelay, delay, unit);
			
			Future<?> unused = schedule.schedule(null, new ScheduledThreadPoolExecutor(10) {
				@Override
				public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay,
						TimeUnit unit) {
					assertSingleCallWithCorrectParameters(command, initialDelay, delay, unit);
					return null;
				}
			}, testRunnable);
			assertTrue(called);*/
		}

		public void testFixedDelayScheduleFarFuturePotentiallyOverflowingScheduleIsNeverReached() throws Exception {
			TestAbstractScheduledCustomService service = new TestAbstractScheduledCustomService() {
				@Override
				protected Scheduler scheduler() {
					return newFixedDelaySchedule(Long.MAX_VALUE, Long.MAX_VALUE, SECONDS);
				}
			};
			service.startAsync().awaitRunning();
			try {
				service.firstBarrier.await(5, SECONDS);
				fail();
			} catch (TimeoutException expected) {
			}
			assertEquals(0, service.numIterations.get());
			service.stopAsync();
			service.awaitTerminated();
		}

		public void testCustomSchedulerFarFuturePotentiallyOverflowingScheduleIsNeverReached() throws Exception {
			TestAbstractScheduledCustomService service = new TestAbstractScheduledCustomService() {
				@Override
				protected Scheduler scheduler() {
					return new AbstractScheduledService.CustomScheduler() {
						@Override
						protected Schedule getNextSchedule() throws Exception {
							return new Schedule(Long.MAX_VALUE, SECONDS);
						}
					};
				}
			};
			service.startAsync().awaitRunning();
			try {
				service.firstBarrier.await(5, SECONDS);
				fail();
			} catch (TimeoutException expected) {
			}
			assertEquals(0, service.numIterations.get());
			service.stopAsync();
			service.awaitTerminated();
		}

		private class TestCustomScheduler extends AbstractScheduledService.CustomScheduler {
			public AtomicInteger scheduleCounter = new AtomicInteger(0);

			@Override
			protected Schedule getNextSchedule() throws Exception {
				scheduleCounter.incrementAndGet();
				return new Schedule(0, TimeUnit.SECONDS);
			}
			
			public Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable runnable) {
				
				return null;
			}
		}

		public void testCustomSchedule_startStop() throws Exception {
			final CyclicBarrier firstBarrier = new CyclicBarrier(2);
			final CyclicBarrier secondBarrier = new CyclicBarrier(2);
			final AtomicBoolean shouldWait = new AtomicBoolean(true);
			Runnable task = new Runnable() {
				@Override
				public void run() {
					try {
						if (shouldWait.get()) {
							firstBarrier.await();
							secondBarrier.await();
						}
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			};
			
			TestCustomScheduler scheduler = new TestCustomScheduler();
			Future<?> future = scheduler.schedule(null, Executors.newScheduledThreadPool(10), task);
			firstBarrier.await();
			assertEquals(1, scheduler.scheduleCounter.get());
			secondBarrier.await();
			firstBarrier.await();
			assertEquals(2, scheduler.scheduleCounter.get());
			shouldWait.set(false);
			secondBarrier.await();
			future.cancel(false);
		}

		public void testCustomSchedulerServiceStop() throws Exception {
			TestAbstractScheduledCustomService service = new TestAbstractScheduledCustomService();
			service.startAsync().awaitRunning();
			service.firstBarrier.await();
			assertEquals(1, service.numIterations.get());
			service.stopAsync();
			service.secondBarrier.await();
			service.awaitTerminated();
			// Sleep for a while just to ensure that our task wasn't called again.
			Thread.sleep(unit.toMillis(3 * delay));
			assertEquals(1, service.numIterations.get());
		}

		public void testCustomScheduler_deadlock() throws InterruptedException, BrokenBarrierException {
			final CyclicBarrier inGetNextSchedule = new CyclicBarrier(2);
			// This will flakily deadlock, so run it multiple times to increase the flake likelihood
			for (int i = 0; i < 1000; i++) {
				Service service = new AbstractScheduledService() {
					@Override
					protected void runOneIteration() {
					}

					@Override
					protected Scheduler scheduler() {
						return new CustomScheduler() {
							@Override
							protected Schedule getNextSchedule() throws Exception {
								if (state() != State.STARTING) {
									inGetNextSchedule.await();
									Thread.yield();
									throw new RuntimeException("boom");
								}
								return new Schedule(0, TimeUnit.NANOSECONDS);
							}
						};
					}
				};
				service.startAsync().awaitRunning();
				inGetNextSchedule.await();
				service.stopAsync();
			}
		}

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
			service.firstBarrier.await();
			int numIterations = service.numIterations.get();
			service.stopAsync();
			service.secondBarrier.await();
			service.awaitTerminated();
			assertEquals(numIterations, service.numIterations.get());
		}

		private static class TestAbstractScheduledCustomService extends AbstractScheduledService {
			final AtomicInteger numIterations = new AtomicInteger(0);
			volatile boolean useBarriers = true;
			final CyclicBarrier firstBarrier = new CyclicBarrier(2);
			final CyclicBarrier secondBarrier = new CyclicBarrier(2);

			@Override
			protected void runOneIteration() throws Exception {
				numIterations.incrementAndGet();
				if (useBarriers) {
					firstBarrier.await();
					secondBarrier.await();
				}
			}

			@Override
			protected ScheduledExecutorService executor() {
				// use a bunch of threads so that weird overlapping schedules are more likely to happen.
				return Executors.newScheduledThreadPool(10);
			}

			@Override
			protected Scheduler scheduler() {
				return new CustomScheduler() {
					@Override
					protected Schedule getNextSchedule() throws Exception {
						return new Schedule(delay, unit);
					}
				};
			}
		}

		public void testCustomSchedulerFailure() throws Exception {
			TestFailingCustomScheduledService service = new TestFailingCustomScheduledService();
			service.startAsync().awaitRunning();
			for (int i = 1; i < 4; i++) {
				service.firstBarrier.await();
				assertEquals(i, service.numIterations.get());
				service.secondBarrier.await();
			}
			Thread.sleep(1000);
			try {
				service.stopAsync().awaitTerminated(100, TimeUnit.SECONDS);
				fail();
			} catch (IllegalStateException e) {
				assertEquals(State.FAILED, service.state());
			}
		}

		private static class TestFailingCustomScheduledService extends AbstractScheduledService {
			final AtomicInteger numIterations = new AtomicInteger(0);
			final CyclicBarrier firstBarrier = new CyclicBarrier(2);
			final CyclicBarrier secondBarrier = new CyclicBarrier(2);

			@Override
			protected void runOneIteration() throws Exception {
				numIterations.incrementAndGet();
				firstBarrier.await();
				secondBarrier.await();
			}

			@Override
			protected ScheduledExecutorService executor() {
				// use a bunch of threads so that weird overlapping schedules are more likely to happen.
				return Executors.newScheduledThreadPool(10);
			}

			@Override
			protected Scheduler scheduler() {
				return new CustomScheduler() {
					@Override
					protected Schedule getNextSchedule() throws Exception {
						if (numIterations.get() > 2) {
							throw new IllegalStateException("Failed");
						}
						return new Schedule(delay, unit);
					}
				};
			}
		}
	}
	
	public static class RecordingListener extends Listener {
		static RecordingListener record(Service service) {
			RecordingListener listener = new RecordingListener(service);
			service.addListener(listener, MoreExecutors.directExecutor());
			return listener;
		}

		final Service service;

		RecordingListener(Service service) {
			this.service = service;
		}

		@Override
		public synchronized void starting() {
			System.out.println("Listener.starting: " + service.state() + ", isRunning: " + service.isRunning());
		}

		@Override
		public synchronized void running() {
			System.out.println("Listener.running: " + service.state() + ", isRunning: " + service.isRunning());
		}

		@Override
		public synchronized void stopping(State from) {
			System.out.println("Listener.stopping: " + service.state() + ", from:" + from + ", isRunning: " + service.isRunning());
		}

		@Override
		public synchronized void terminated(State from) {
			System.out.println("Listener.terminated: " + service.state() + ", from:" + from + ", isRunning: " + service.isRunning());
		}

		@Override
		public synchronized void failed(State from, Throwable failure) {
			System.out.println("Listener.failed: " + service.state() + ", from:" + from + ", isRunning: " + service.isRunning());
			System.out.println("Listener.failed: " + failure.getMessage());
		}
	}
}
