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
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.cnblogs.hoojo.concurrency.testing.TestingExecutors;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.common.util.concurrent.AbstractScheduledService.Scheduler;
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
			future = super.scheduleWithFixedDelay(command, initialDelay, delay, unit);
			System.out.println(future);
			return future;
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
			
			System.out.println("----runFirstBarrier.await----");
			System.out.println("----runSecondBarrier.await----");
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
		
		assertEquals(0, service.numberOfTimesRunCalled.get());
		assertEquals(Service.State.FAILED, service.state());
	}

	public void testFailOnErrorFromStartUpListener() throws InterruptedException {
		final Error error = new Error("running error");
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
		
		// startAsync -> doStart & dispatch Listener.starting
		// doStart -> executor & startUp & scheduler & notifyStarted
		service.startAsync();
		latch.await();

		assertEquals(0, service.numberOfTimesRunCalled.get());
		assertEquals(Service.State.FAILED, service.state());
	}

	public void testFailOnExceptionFromShutDown() throws Exception {
		TestService service = new TestService();
		service.shutDownException = new Exception("shutDown Exception");
		
		service.startAsync().awaitRunning();
		service.runFirstBarrier.await();
		
		// stopAsync -> doStop -> cancel -> if (state() == State.STOPPING) { shutDown & notifyStop }
		// shutDown -> exception -> notifyFailed
		service.stopAsync();
		service.runSecondBarrier.await();
		
		try {
			// notifyFailed -> state = failed -> exception 
			service.awaitTerminated();
			fail();
		} catch (IllegalStateException e) {
			assertEquals(service.shutDownException, e.getCause());

			System.out.println(e); // IllegalStateException: Expected the service TestService [FAILED] to be TERMINATED, but the service has FAILED
			System.out.println(service.failureCause()); // Exception: shutDown Exception
		}
		
		assertEquals(Service.State.FAILED, service.state());
	}

	public void testRunOneIterationCalledMultipleTimes() throws Exception {
		TestService service = new TestService();
		RecordingListener.record(service);
		
		service.startAsync().awaitRunning();
		
		// runFirstBarrier每到2次就可以触发运行 runOneIteration
		// 因为runFirstBarrier.wait导致程序阻塞，底层线程运行被进行等待
		// 当程序恢复不再阻塞，底层线程尝试重新运行runOneIteration
		// 直到底层线程顺利运行完runOneIteration方法终止
		for (int i = 1; i < 10; i++) {
			System.out.println(service.runFirstBarrier.await());
			System.out.println(i + "->" + service.numberOfTimesRunCalled.get());
			System.out.println(service.runSecondBarrier.await());
		}
		
		service.runFirstBarrier.await();
		
		service.stopAsync();
		
		service.runSecondBarrier.await();
		service.stopAsync().awaitTerminated();
	}

	public void testExecutorOnlyCalledOnce() throws Exception {
		TestService service = new TestService();
		
		service.startAsync().awaitRunning();
		
		// numberOfTimesExecutorCalled 被执行一次，runOneIteration 却可以执行多次
		// It should be called once during startup.
		assertEquals(1, service.numberOfTimesExecutorCalled.get());
		for (int i = 1; i < 10; i++) {
			System.out.println(service.runFirstBarrier.await());
			System.out.println(i + "->" + service.numberOfTimesRunCalled.get());
			System.out.println(service.runSecondBarrier.await());
		}
		service.runFirstBarrier.await();
		
		service.stopAsync();
		service.runSecondBarrier.await();
		service.stopAsync().awaitTerminated();
		// Only called once overall.
		assertEquals(1, service.numberOfTimesExecutorCalled.get());//1
	}
	
	public void testSchedulerOnlyCalledOnce() throws Exception {
		TestService service = new TestService();
		service.startAsync().awaitRunning();
		
		// It should be called once during startup.
		assertEquals(1, service.numberOfTimesSchedulerCalled.get());
		for (int i = 1; i < 10; i++) {
			System.out.println(service.runFirstBarrier.await());
			System.out.println(i + "->" + service.numberOfTimesRunCalled.get());
			System.out.println(service.runSecondBarrier.await());
		}
		service.runFirstBarrier.await();
		service.stopAsync();
		service.runSecondBarrier.await();
		service.awaitTerminated();
		
		// Only called once overall.
		assertEquals(1, service.numberOfTimesSchedulerCalled.get());//1
	}

	public void testDefaultExecutorIsShutdownWhenServiceIsStopped() throws Exception {
		final AtomicReference<ScheduledExecutorService> executor = Atomics.newReference();
		
		AbstractScheduledService service = new AbstractScheduledService() {
			@Override
			protected void runOneIteration() throws Exception {
				System.out.println("runOneIteration........");
			}

			@Override
			public ScheduledExecutorService executor() {
				System.out.println("executor........");
				
				executor.set(super.executor());
				return executor.get();
			}

			@Override
			protected Scheduler scheduler() {
				System.out.println("scheduler........");
				
				return newFixedDelaySchedule(0, 1, TimeUnit.MILLISECONDS);
			}
		};

		
		service.startAsync();
		System.out.println("isShutdown: " + executor.get().isShutdown()); // false
		
		service.awaitRunning();
		service.stopAsync();
		System.out.println("isShutdown: " + executor.get().isShutdown()); // false
		service.awaitTerminated();
		
		System.out.println("isShutdown: " + executor.get().isShutdown()); // true
		
		// 阻塞，直到所有任务在关闭请求之后完成执行，或发生超时，或当前线程中断，以先发生者为准。
		// 程序调度终止完成
		System.out.println(executor.get().awaitTermination(100, TimeUnit.MILLISECONDS)); // true
	}

	public void testDefaultExecutorIsShutdownWhenServiceFails() throws Exception {
		final AtomicReference<ScheduledExecutorService> executor = Atomics.newReference();
		AbstractScheduledService service = new AbstractScheduledService() {
			@Override
			protected void startUp() throws Exception {
				System.out.println("startUp...........");
				throw new Exception("startUp Failed");
			}

			@Override
			protected void runOneIteration() throws Exception {
				System.out.println("runOneIteration...........");
			}

			@Override
			protected ScheduledExecutorService executor() {
				System.out.println("executor...........");
				executor.set(super.executor());
				return executor.get();
			}

			@Override
			protected Scheduler scheduler() {
				System.out.println("scheduler...........");
				return newFixedDelaySchedule(0, 1, TimeUnit.MILLISECONDS);
			}
		};

		try {
			// startAsync -> doStart -> executor & startUp & scheduler & notifyStarted
			// startUp -> throw Exception -> notifyFailed & runningTask.cancel -> failed
			service.startAsync().awaitRunning();
			fail("Expected service to fail during startup");
		} catch (IllegalStateException expected) {
			System.out.println(expected); // IllegalStateException: Expected the service  [FAILED] to be RUNNING, but the service has FAILED
			System.out.println(service.failureCause());// Exception: startUp Failed
		}

		System.out.println(executor.get().isShutdown()); // true
		
		// 程序调度终止完成
		assertTrue(executor.get().awaitTermination(100, TimeUnit.MILLISECONDS));
	}

	public void testTimeout() {
		// Create a service whose executor will never run its commands
		Service service = new AbstractScheduledService() {
			@Override
			protected Scheduler scheduler() {
				System.out.println("scheduler...........");
				
				return Scheduler.newFixedDelaySchedule(0, 1, TimeUnit.NANOSECONDS);
			}

			@Override
			protected ScheduledExecutorService executor() {
				System.out.println("executor...........");
				
				/** 没有执行线程任务，导致任务超时 */
				return TestingExecutors.noOpScheduledExecutor();
			}

			@Override
			protected void runOneIteration() throws Exception {
				System.out.println("runOneIteration...........");
			}

			@Override
			protected String serviceName() {
				System.out.println("serviceName...........");
				return "Foo";
			}
		};
		
		RecordingListener.record(service);
		try {
			service.startAsync().awaitRunning(1, TimeUnit.MILLISECONDS);
			fail("Expected timeout");
		} catch (TimeoutException e) {
			System.out.println(e); //"Timed out waiting for Foo [STARTING] to reach the RUNNING state."
		}
		System.out.println(service.state()); // STARTING
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
			System.out.println(command);
		}

		public void testFixedRateSchedule() {
			
			Service service = new AbstractScheduledService() {
				@Override
				protected Scheduler scheduler() {
					System.out.println("scheduler...........");
					
					return Scheduler.newFixedRateSchedule(initialDelay, delay, unit);
				}

				@Override
				protected ScheduledExecutorService executor() {
					System.out.println("executor...........");
					
					return new ScheduledThreadPoolExecutor(1) {
						@Override
						public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
							assertSingleCallWithCorrectParameters(command, initialDelay, period, unit);
							return null;
						}
					};
				}

				@Override
				protected void runOneIteration() throws Exception {
					System.out.println("runOneIteration...........");
				}

				@Override
				protected String serviceName() {
					System.out.println("serviceName...........");
					return "Foo";
				}
			};
			
			service.startAsync().awaitRunning();
		}

		public void testFixedDelaySchedule() {
			
			Service service = new AbstractScheduledService() {
				@Override
				protected Scheduler scheduler() {
					System.out.println("scheduler...........");
					
					return Scheduler.newFixedDelaySchedule(initialDelay, delay, unit);
				}

				@Override
				protected ScheduledExecutorService executor() {
					System.out.println("executor...........");
					
					return new ScheduledThreadPoolExecutor(1) {
						@Override
						public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long period, TimeUnit unit) {
							assertSingleCallWithCorrectParameters(command, initialDelay, period, unit);
							return null;
						}
					};
				}

				@Override
				protected void runOneIteration() throws Exception {
					System.out.println("runOneIteration...........");
				}

				@Override
				protected String serviceName() {
					System.out.println("serviceName...........");
					return "Foo";
				}
			};
			
			service.startAsync().awaitRunning();
		}
		
		private static class TestAbstractScheduledCustomService extends AbstractScheduledService {
			volatile boolean useBarriers = true;
			final AtomicInteger numIterations = new AtomicInteger(0);
			final CyclicBarrier firstBarrier = new CyclicBarrier(2);
			final CyclicBarrier secondBarrier = new CyclicBarrier(2);

			@Override
			protected void runOneIteration() throws Exception {
				System.out.println("runOneIteration...........");
				numIterations.incrementAndGet();
				if (useBarriers) {
					System.out.println("runOneIteration.await...........");
					System.out.println("firstBarrier: " + firstBarrier.await());
					System.out.println("secondBarrier: " + secondBarrier.await());
				}
				
				System.out.println("runOneIteration..........end");
			}

			@Override
			protected ScheduledExecutorService executor() {
				System.out.println("executor...........");
				
				// use a bunch of threads so that weird overlapping schedules are more likely to happen.
				return Executors.newScheduledThreadPool(10);
			}

			@Override
			protected Scheduler scheduler() {
				System.out.println("scheduler...........");

				return new CustomScheduler() {
					@Override
					protected Schedule getNextSchedule() throws Exception {
						System.out.println("getNextSchedule...........");
						return new Schedule(delay, unit);
					}
				};
			}
		}

		public void testFixedDelayScheduleFarFuturePotentiallyOverflowingScheduleIsNeverReached() throws Exception {
			TestAbstractScheduledCustomService service = new TestAbstractScheduledCustomService() {
				@Override
				protected Scheduler scheduler() {
					System.out.println("@Override.scheduler......");

					// 启动执行间隔Long最大值，导致程序等待，调度任务没有开始执行
					return newFixedDelaySchedule(Long.MAX_VALUE, Long.MAX_VALUE, SECONDS);
				}
			};
			
			RecordingListener.record(service);
			
			// 程序启动运行 
			// startAsync -> doStart -> executor & startUp & scheduler & notifyStarted
			// scheduler -> 执行频率 导致
			service.startAsync().awaitRunning();
			
			try {
				// service.firstBarrier.await();
				// 调度任务没有开始执行，导致runOneIteration方法没有运行，从而下面方法超时
				service.firstBarrier.await(5, SECONDS);
				fail();
			} catch (TimeoutException expected) {
				System.out.println(expected);//TimeoutException
			}
			
			System.out.println(service.numIterations.get()); // 0
			service.stopAsync();
			service.awaitTerminated();
		}

		public void testCustomSchedulerFarFuturePotentiallyOverflowingScheduleIsNeverReached() throws Exception {
			TestAbstractScheduledCustomService service = new TestAbstractScheduledCustomService() {
				@Override
				protected Scheduler scheduler() {
					System.out.println("@Override.scheduler..........");
					return new AbstractScheduledService.CustomScheduler() {
						
						@Override
						protected Schedule getNextSchedule() throws Exception {
							System.out.println("getNextSchedule..........");
							return new Schedule(Long.MAX_VALUE, SECONDS);
						}
					};
				}
			};
			service.startAsync().awaitRunning();
			try {
				// 由于 getNextSchedule 方法导致任务延迟运行，runOneIteration未运行，导致下面方法超时
				service.firstBarrier.await(5, SECONDS);
				fail();
			} catch (TimeoutException expected) {
				System.out.println(expected);
			}
			assertEquals(0, service.numIterations.get());
			service.stopAsync();
			service.awaitTerminated();
		}

		public void testCustomSchedulerServiceStop() throws Exception {
			TestAbstractScheduledCustomService service = new TestAbstractScheduledCustomService();
			RecordingListener.record(service);
			
			// startAsync -> doStart -> executor & startUp & scheduler & notifyStarted
			// scheduler -> runOneIteration -> firstBarrier.await
			service.startAsync().awaitRunning();
			System.out.println("firstBarrier.await");
			System.out.println(service.firstBarrier.await()); // 1，屏障抵达
			assertEquals(1, service.numIterations.get());
			service.stopAsync();
			service.secondBarrier.await(); // 屏障抵达，runOneIteration 运行结束
			service.awaitTerminated();
			
			// Sleep for a while just to ensure that our task wasn't called again.
			Thread.sleep(unit.toMillis(3 * delay));
			assertEquals(1, service.numIterations.get());
		}
		
		public void testBig() throws Exception {
			TestAbstractScheduledCustomService service = new TestAbstractScheduledCustomService() {
				@Override
				protected Scheduler scheduler() {
					System.out.println("scheduler........");
					
					return new AbstractScheduledService.CustomScheduler() {
						@Override
						protected Schedule getNextSchedule() throws Exception {
							System.out.println("getNextSchedule........");
							
							// Explicitly yield to increase the probability of a pathological scheduling.
							Thread.yield();
							
							return new Schedule(5, TimeUnit.MILLISECONDS);
						}
					};
				}
			};
			
			service.useBarriers = false;
			
			// 重复调度开启
			service.startAsync().awaitRunning();
			Thread.sleep(50);
			
			// 开启等待屏障
			service.useBarriers = true;
			
			System.out.println("-------firstBarrier.await--------");
			service.firstBarrier.await(); //抵达屏障点1
			int numIterations = service.numIterations.get();
			
			service.stopAsync();
			System.out.println("-------secondBarrier.await--------");
			service.secondBarrier.await();//抵达屏障点2，runOneIteration运行完毕
			
			service.awaitTerminated();
			assertEquals(numIterations, service.numIterations.get());
		}

		public void testCustomScheduler_deadlock() throws InterruptedException, BrokenBarrierException {
			final CyclicBarrier inGetNextSchedule = new CyclicBarrier(2);
			
			// This will flakily deadlock, so run it multiple times to increase the flake likelihood
			for (int i = 0; i < 1; i++) {
				Service service = new AbstractScheduledService() {
					@Override
					protected void runOneIteration() {
						System.out.println("runOneIteration........");
					}

					@Override
					protected Scheduler scheduler() {
						System.out.println("scheduler.........");
						
						return new CustomScheduler() {
							@Override
							protected Schedule getNextSchedule() throws Exception {
								System.out.println("getNextSchedule.........");
								System.out.println(state());
								
								if (state() != State.STARTING) { // 第二次running
									inGetNextSchedule.await();
									System.out.println("inGetNextSchedule.await------");
									Thread.yield();
									throw new RuntimeException("boom");
								}
								return new Schedule(0, TimeUnit.NANOSECONDS);
							}
						};
					}
				};
				
				service.startAsync().awaitRunning();
				Thread.sleep(100);
				
				inGetNextSchedule.await();// 程序抛出异常终止
				service.stopAsync();
			}
		}

		public void testCustomSchedulerFailure() throws Exception {
			TestFailingCustomScheduledService service = new TestFailingCustomScheduledService();
			RecordingListener.record(service);
			
			service.startAsync().awaitRunning();
			for (int i = 1; i < 4; i++) {
				System.out.println(service.firstBarrier.await());
				System.out.println(i + "->" + service.numIterations.get());
				System.out.println(service.secondBarrier.await());
			}
			
			Thread.sleep(2000);
			
			try {
				service.stopAsync().awaitTerminated(100, TimeUnit.SECONDS);
				fail();
			} catch (IllegalStateException e) {
				System.out.println(e);//IllegalStateException: Expected the service TestFailingCustomScheduledService [FAILED] to be TERMINATED, but the service has FAILED
				System.out.println(service.failureCause());//IllegalStateException: Failed
				assertEquals(State.FAILED, service.state());
			}
		}

		private static class TestFailingCustomScheduledService extends AbstractScheduledService {
			final AtomicInteger numIterations = new AtomicInteger(0);
			final CyclicBarrier firstBarrier = new CyclicBarrier(2);
			final CyclicBarrier secondBarrier = new CyclicBarrier(2);

			@Override
			protected void runOneIteration() throws Exception {
				System.out.println("runOneIteration______");
				numIterations.incrementAndGet();
				firstBarrier.await();
				secondBarrier.await();
				
				System.out.println("runOneIteration______end");
			}

			@Override
			protected ScheduledExecutorService executor() {
				System.out.println("executor");
				
				// use a bunch of threads so that weird overlapping schedules are more likely to happen.
				return Executors.newScheduledThreadPool(10);
			}

			@Override
			protected Scheduler scheduler() {
				System.out.println("scheduler");
				
				return new CustomScheduler() {
					@Override
					protected Schedule getNextSchedule() throws Exception {
						System.out.println("getNextSchedule");
						if (numIterations.get() > 2) {
							System.out.println("Failed");
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
