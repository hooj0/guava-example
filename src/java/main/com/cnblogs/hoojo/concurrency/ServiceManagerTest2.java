/*
 * Copyright (C) 2012 The Guava Authors
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

package com.cnblogs.hoojo.concurrency;

import static com.google.common.truth.Truth.assertThat;
import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.cnblogs.hoojo.concurrency.testing.NullPointerTester;
import com.cnblogs.hoojo.concurrency.testing.TestLogHandler;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.common.util.concurrent.AbstractService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.Service.State;
import com.google.common.util.concurrent.ServiceManager;
import com.google.common.util.concurrent.ServiceManager.Listener;
import com.google.common.util.concurrent.Uninterruptibles;

import junit.framework.TestCase;

/**
 * Tests for {@link ServiceManager}.
 *
 * @author Luke Sandberg
 * @author Chris Nokleberg
 */
public class ServiceManagerTest2 extends TestCase {
	
	public static class MyLisener extends com.google.common.util.concurrent.Service.Listener {
		static MyLisener record(Service service) {
			MyLisener listener = new MyLisener(service);
			service.addListener(listener, MoreExecutors.directExecutor());
			return listener;
		}

		final Service service;

		MyLisener(Service service) {
			this.service = service;
		}

		@Override
		public synchronized void starting() {
			System.out.println(service + " -> Listener.starting: " + service.state() + ", isRunning: " + service.isRunning());
		}

		@Override
		public synchronized void running() {
			System.out.println(service + " -> Listener.running: " + service.state() + ", isRunning: " + service.isRunning());
		}

		@Override
		public synchronized void stopping(State from) {
			System.out.println(service + " -> Listener.stopping: " + service.state() + ", from:" + from + ", isRunning: " + service.isRunning());
		}

		@Override
		public synchronized void terminated(State from) {
			System.out.println(service + " -> Listener.terminated: " + service.state() + ", from:" + from + ", isRunning: " + service.isRunning());
		}

		@Override
		public synchronized void failed(State from, Throwable failure) {
			System.out.println(service + " -> Listener.failed: " + service.state() + ", from:" + from + ", isRunning: " + service.isRunning());
			System.out.println(service + " -> Listener.failed: " + failure.getMessage());
		}
	}

	private static final class RecordingListener extends ServiceManager.Listener {
		volatile boolean healthyCalled;
		volatile boolean stoppedCalled;
		final Set<Service> failedServices = Sets.newConcurrentHashSet();

		@Override
		public void healthy() {
			System.out.println("RecordingListener.healthy___");
			healthyCalled = true;
		}

		@Override
		public void stopped() {
			System.out.println("RecordingListener.stopped___");
			stoppedCalled = true;
		}

		@Override
		public void failure(Service service) {
			System.out.println("RecordingListener.failure___");
			failedServices.add(service);
		}
	}
	
	private static class NoOpService extends AbstractService {
		@Override
		protected void doStart() {
			System.out.println("NoOpService.doStart...");
			notifyStarted();
		}

		@Override
		protected void doStop() {
			System.out.println("NoOpService.doStop...");
			notifyStopped();
		}
	}

	/*
	 * A NoOp service that will delay the startup and shutdown notification for a configurable amount of time.
	 */
	private static class NoOpDelayedService extends NoOpService {
		private long delay;

		public NoOpDelayedService(long delay) {
			this.delay = delay;
		}

		@Override
		protected void doStart() {
			System.out.println("NoOpDelayedService.doStart......");
			
			new Thread() {
				@Override
				public void run() {
					System.out.println("NoOpDelayedService.doStart run......");
					
					// 休眠
					System.out.println("NoOpDelayedService.doStart run->sleep......");
					Uninterruptibles.sleepUninterruptibly(delay, TimeUnit.MILLISECONDS);

					System.out.println("NoOpDelayedService.doStart run->notifyStarted......");
					notifyStarted();
				}
			}.start();
		}

		@Override
		protected void doStop() {
			System.out.println("NoOpDelayedService.doStop......");
			
			new Thread() {
				@Override
				public void run() {
					System.out.println("NoOpDelayedService.doStop run......");
					
					System.out.println("NoOpDelayedService.doStop run->sleep......");
					Uninterruptibles.sleepUninterruptibly(delay, TimeUnit.MILLISECONDS);

					System.out.println("NoOpDelayedService.doStop run->notifyStopped......");
					notifyStopped();
				}
			}.start();
		}
	}

	private static class FailStartService extends NoOpService {
		@Override
		protected void doStart() {
			System.out.println("FailStartService.doStart...fail");
			notifyFailed(new IllegalStateException("FailStartService.start failure"));
		}
	}

	private static class FailRunService extends NoOpService {
		@Override
		protected void doStart() {
			System.out.println("FailRunService.doStart...super.doStart");
			super.doStart(); // notifyStarted()
			
			System.out.println("FailRunService.doStart...fail");
			notifyFailed(new IllegalStateException("FailStopService.run failure"));
		}
	}

	private static class FailStopService extends NoOpService {
		@Override
		protected void doStop() {
			System.out.println("FailStopService.doStop...fail");
			notifyFailed(new IllegalStateException("FailStopService.stop failure"));
		}
	}

	public void testServiceStartupTimes() {
		Service a = new NoOpDelayedService(150);
		Service b = new NoOpDelayedService(353);
		
		ServiceManager serviceManager = new ServiceManager(asList(a, b));
		System.out.println(serviceManager); //ServiceManager{services=[NoOpDelayedService [NEW], NoOpDelayedService [NEW]]}
		
		// 会循环遍历所有services, 调用service的startAsync方法
		// forEach -> service.startAsync -> doStart
		serviceManager.startAsync().awaitHealthy();
		System.out.println(serviceManager); //ServiceManager{services=[NoOpDelayedService [RUNNING], NoOpDelayedService [RUNNING]]}
		
		ImmutableMap<Service, Long> startupTimes = serviceManager.startupTimes();
		// 启动运行时间
		System.out.println(startupTimes); //{NoOpDelayedService [RUNNING]=151, NoOpDelayedService [RUNNING]=353}
		
		assertEquals(2, startupTimes.size());
		// TODO(kak): Use assertThat(startupTimes.get(a)).isAtLeast(150);
		assertTrue(startupTimes.get(a) >= 150);
		// TODO(kak): Use assertThat(startupTimes.get(b)).isAtLeast(353);
		assertTrue(startupTimes.get(b) >= 353);
	}

	public void testServiceStartupTimes_selfStartingServices() {
		// This tests to ensure that:
		// 1. service times are accurate when the service is started by the manager
		// 2. service times are recorded when the service is not started by the manager (but they may not be accurate).
		final Service b = new NoOpDelayedService(353) {
			@Override
			protected void doStart() {
				super.doStart();
				// This will delay service listener execution at least 150 milliseconds
				Uninterruptibles.sleepUninterruptibly(150, TimeUnit.MILLISECONDS);
			}
		};
		
		Service a = new NoOpDelayedService(150) {
			@Override
			protected void doStart() {
				b.startAsync();
				super.doStart();
			}
		};
		
		ServiceManager serviceManager = new ServiceManager(asList(a, b));
		System.out.println(serviceManager); // ServiceManager{services=[ [NEW],  [NEW]]}
		
		// 会循环遍历所有services, 调用service的startAsync方法
		// service.startAsync -> doStart
		// doStart -> b.startAsync 会重复启动，导致IllegalStateException: Service  [STARTING] has already been started
		serviceManager.startAsync().awaitHealthy();
		System.out.println(serviceManager); // ServiceManager{services=[ [RUNNING],  [RUNNING]]}
		
		ImmutableMap<Service, Long> startupTimes = serviceManager.startupTimes();
		System.out.println(startupTimes);
		
		assertEquals(2, startupTimes.size());
		// TODO(kak): Use assertThat(startupTimes.get(a)).isAtLeast(150);
		assertTrue(startupTimes.get(a) >= 150);
		// Service b startup takes at least 353 millis, but starting the timer is delayed by at least
		// 150 milliseconds. so in a perfect world the timing would be 353-150=203ms, but since either
		// of our sleep calls can be arbitrarily delayed we should just assert that there is a time
		// recorded.
		assertThat(startupTimes.get(b)).isNotNull();
	}

	public void testServiceStartStop() {
		Service a = new NoOpService();
		Service b = new NoOpService();
		
		ServiceManager manager = new ServiceManager(asList(a, b));
		System.out.println(manager);//ServiceManager{services=[NoOpService [NEW], NoOpService [NEW]]}
		
		RecordingListener listener = new RecordingListener();
		manager.addListener(listener);
		
		assertState(manager, Service.State.NEW, a, b);
		assertFalse(manager.isHealthy());
		
		// startAsync -> for each service.startAsync -> service.doStart & dispatch event listener.starting
		// 当services 遍历启动完所有的service，就触发Listener.healthy
		// 会等待所有的服务达到Running状态
		manager.startAsync().awaitHealthy();
		System.out.println(manager);//ServiceManager{services=[NoOpService [RUNNING], NoOpService [RUNNING]]}
		
		assertState(manager, Service.State.RUNNING, a, b);
		assertTrue(manager.isHealthy()); //如果所有的服务处于Running状态、会返回True
		
		assertTrue(listener.healthyCalled);
		assertFalse(listener.stoppedCalled);
		assertTrue(listener.failedServices.isEmpty());
		
		// stopAsync -> for each service.stopAsync -> service.doStop & dispatch event listener.stoping
		// 当services 遍历停止完所有的service，就触发Listener.stopped
		// 会等待所有服务达到终止状态
		manager.stopAsync().awaitStopped();
		System.out.println(manager);//ServiceManager{services=[NoOpService [TERMINATED], NoOpService [TERMINATED]]}
		
		assertState(manager, Service.State.TERMINATED, a, b);
		assertFalse(manager.isHealthy()); //所有的服务达到Running状态，部分线程结束完成，所以False
		assertTrue(listener.stoppedCalled);
		assertTrue(listener.failedServices.isEmpty());
	}

	public void testFailStart() throws Exception {
		Service a = new NoOpService();
		Service b = new FailStartService();
		Service c = new NoOpService();
		Service d = new FailStartService();
		Service e = new NoOpService();
		
		ServiceManager manager = new ServiceManager(asList(a, b, c, d, e));
		
		RecordingListener listener = new RecordingListener();
		manager.addListener(listener);
		System.out.println(manager);
		
		assertState(manager, Service.State.NEW, a, b, c, d, e);
		
		try {
			// 在遍历时，调用service.startAsync -> doStart -> throw exception -> RecordingListener.failure
			// 在遍历时，调用service.startAsync -> doStart -> services.running.count == services.count -> RecordingListener.healthy
			// 当所有服务启动成功，触发 RecordingListener.healthy
			// 当服务失败，触发RecordingListener.failure
			// 当服务完成，触发RecordingListener.stopped
			manager.startAsync().awaitHealthy();
			fail();
		} catch (IllegalStateException expected) {
			System.out.println(expected); //IllegalStateException: Expected to be healthy after starting. 
			//The following services are not running: {FAILED=[FailStartService [FAILED], FailStartService [FAILED]]}
		}
		
		assertFalse(listener.healthyCalled);
		assertState(manager, Service.State.RUNNING, a, c, e);
		assertEquals(ImmutableSet.of(b, d), listener.failedServices);
		assertState(manager, Service.State.FAILED, b, d);
		assertFalse(manager.isHealthy());

		// 当服务完成，触发RecordingListener.stopped
		manager.stopAsync().awaitStopped();
		
		assertFalse(manager.isHealthy());
		assertFalse(listener.healthyCalled);
		assertTrue(listener.stoppedCalled);
	}

	public void testFailRun() throws Exception {
		Service a = new NoOpService();
		MyLisener.record(a);

		Service b = new FailRunService();
		MyLisener.record(b);

		
		ServiceManager manager = new ServiceManager(asList(a, b));
		System.out.println(manager); //ServiceManager{services=[NoOpService [NEW], FailRunService [NEW]]}
		
		RecordingListener listener = new RecordingListener();
		manager.addListener(listener);
		
		assertState(manager, Service.State.NEW, a, b);
		try {
			// 1. manager.startAsync -> service.startAsync -> doStart -> notifyStarted & notifyFailed
			// 2. notifyStarted -> state=running & dispatch Listener.running
			// 3. notifyFailed -> dispatch Listener.failed
			manager.startAsync(); // 启动所有服务
			System.out.println(manager);
			
			manager.awaitHealthy(); // 等待进入running状态
			fail();
		} catch (IllegalStateException expected) {
			System.out.println(expected); // IllegalStateException: Expected to be healthy after starting. 
			// The following services are not running: {FAILED=[FailRunService [FAILED]]}
		}
		
		// running 状态触发notifyFailed 抛出 异常，listener.healthy 会被执行触发
		assertTrue(listener.healthyCalled);
		assertEquals(ImmutableSet.of(b), listener.failedServices);
		System.out.println(manager.isHealthy());

		manager.stopAsync().awaitStopped();
		System.out.println(manager);
		
		assertState(manager, Service.State.FAILED, b);
		assertState(manager, Service.State.TERMINATED, a);

		assertTrue(listener.stoppedCalled);
	}

	public void testFailStop() throws Exception {
		Service a = new NoOpService();
		Service b = new FailStopService();
		Service c = new NoOpService();
		
		ServiceManager manager = new ServiceManager(asList(a, b, c));
		System.out.println(manager);
		
		RecordingListener listener = new RecordingListener();
		manager.addListener(listener);

		manager.startAsync().awaitHealthy();
		System.out.println(manager);
		
		assertTrue(listener.healthyCalled);
		assertFalse(listener.stoppedCalled);
		
		// stop 抛出异常
		manager.stopAsync().awaitStopped();
		System.out.println(manager);

		assertTrue(listener.stoppedCalled);
		assertEquals(ImmutableSet.of(b), listener.failedServices);
		assertState(manager, Service.State.FAILED, b);
		assertState(manager, Service.State.TERMINATED, a, c);
	}

	public void testToString() throws Exception {
		Service a = new NoOpService();
		Service b = new FailStartService();
		
		ServiceManager manager = new ServiceManager(asList(a, b));
		System.out.println(manager); // ServiceManager{services=[NoOpService [NEW], FailStartService [NEW]]}
		
		String toString = manager.toString();
		assertThat(toString).contains("NoOpService");
		assertThat(toString).contains("FailStartService");
	}

	public void testTimeouts() throws Exception {
		Service a = new NoOpDelayedService(50);
		ServiceManager manager = new ServiceManager(asList(a));
		
		manager.startAsync();
		try {
			manager.awaitHealthy(1, TimeUnit.MILLISECONDS); // 超时，由于 NoOpDelayedService.doStart -> notifyStarted 有休眠延时，导致超时
			fail();
		} catch (TimeoutException expected) {
			System.out.println(expected); // TimeoutException: Timeout waiting for the services to become healthy. 
			// The following services have not started: {STARTING=[NoOpDelayedService [STARTING]]}
		}
		manager.awaitHealthy(5, SECONDS); // no exception thrown

		manager.stopAsync();
		try {
			manager.awaitStopped(1, TimeUnit.MILLISECONDS);// 超时，由于 NoOpDelayedService.doStop -> notifyStoped 有休眠延时，导致超时
			fail();
		} catch (TimeoutException expected) {
			System.out.println(expected); // TimeoutException: Timeout waiting for the services to stop. 
			// The following services have not stopped: {STOPPING=[NoOpDelayedService [STOPPING]]}
		}
		manager.awaitStopped(5, SECONDS); // no exception thrown
	}

	
	/**
	 * This covers a case where if the last service to stop failed then the stopped callback would never be called.
	 */
	public void testSingleFailedServiceCallsStopped() {
		Service a = new FailStartService();
		MyLisener.record(a);
		
		ServiceManager manager = new ServiceManager(asList(a));
		System.out.println(manager);
		
		RecordingListener listener = new RecordingListener();
		manager.addListener(listener);
		
		try {
			// startAsync -> doStart -> throw exception -> event failure -> dispatcher Listener.failure
			/*
				if (states.count(TERMINATED) + states.count(FAILED) == numberOfServices) {
          			enqueueStoppedEvent();
        		}
			 */
			manager.startAsync().awaitHealthy();
			fail();
		} catch (IllegalStateException expected) {
			System.out.println(expected); //IllegalStateException: Expected to be healthy after starting. 
			//The following services are not running: {FAILED=[FailStartService [FAILED]]}
		}
		System.out.println(manager);
		
		// 由于startAsync 触发 doStart 抛出异常，导致执行  listener.failure，
		// 执行完listener.failure后发现service数量迭代遍历完成，执行listener.stopped
		// if (states.count(TERMINATED) + states.count(FAILED) == numberOfServices) { enqueueStoppedEvent(); }
		assertTrue(listener.stoppedCalled);
	}

	/**
	 * This covers a bug where listener.healthy would get called when a single service failed during startup (it
	 * occurred in more complicated cases also).
	 */
	public void testFailStart_singleServiceCallsHealthy() {
		Service a = new FailStartService();
		ServiceManager manager = new ServiceManager(asList(a));
		
		RecordingListener listener = new RecordingListener();
		manager.addListener(listener);
		
		try {
			// startAsync -> doStart -> throw exception -> event failure -> dispatcher Listener.failure
			// if (states.count(TERMINATED) + states.count(FAILED) == numberOfServices) { enqueueStoppedEvent }
			manager.startAsync().awaitHealthy();
			fail();
		} catch (IllegalStateException expected) {
			System.out.println(expected); //IllegalStateException: Expected to be healthy after starting. 
			//The following services are not running: {FAILED=[FailStartService [FAILED]]}
		}
		
		// 由于startAsync 触发 doStart 导致service没有 遍历完成，所以 listener.healthy 未被触发
		assertFalse(listener.healthyCalled);
	}

	/**
	 * This covers a bug where if a listener was installed that would stop the manager if any service fails and
	 * something failed during startup before service.start was called on all the services, then awaitStopped would
	 * deadlock due to an IllegalStateException that was thrown when trying to stop the timer(!).
	 */
	public void testFailStart_stopOthers() throws TimeoutException {
		Service a = new FailStartService();
		Service b = new NoOpService();
		Service c = new NoOpService();
		Service d = new NoOpService();
		
		MyLisener.record(a);
		MyLisener.record(b);
		MyLisener.record(c);
		MyLisener.record(d);
		
		final ServiceManager manager = new ServiceManager(asList(a, b, c, d));
		manager.addListener(new Listener() {
			@Override
			public void failure(Service service) {
				System.out.println("-----failure------>" + service);
				manager.stopAsync();
				System.out.println("-----failure------stop->" + service);
			}
		});
		
		// 1、manager.startAsync -> forEach services -> service.startAsync
		// 2、service.startAsync -> service.doStart & listener.starting
		// 3、doStart -> throw exception -> manager.listener.failure
		// 4、manager.listener.failure -> manager.stopAsync
		// 5、manager.stopAsync -> service.doStop & listener.stopping
		
		// 由于 第一个service(FailStartService) 触发manager.listener.failure 
		// 从而执行 manager.stopAsync，导致后面的service都从NEW状态进入 TERMINATED
		// 因为后面的service都没有started(startAsync)，
		// 所以Service被stopAsync后再运行startAsync会出现异常：IllegalStateException: Service NoOpService [TERMINATED] has already been started
		System.out.println("-----startAsync----");
		manager.startAsync(); // IllegalStateException: Service NoOpService [TERMINATED] has already been started
		System.out.println("-----awaitStopped----");
		manager.awaitStopped(10, TimeUnit.MILLISECONDS);
	}

	private static void assertState(ServiceManager manager, Service.State state, Service... services) {
		Collection<Service> managerServices = manager.servicesByState().get(state);
		for (Service service : services) {
			assertEquals(service.toString(), state, service.state());
			assertEquals(service.toString(), service.isRunning(), state == Service.State.RUNNING);
			assertTrue(managerServices + " should contain " + service, managerServices.contains(service));
		}
	}

	/**
	 * This is for covering a case where the ServiceManager would behave strangely if constructed with no service under
	 * management. Listeners would never fire because the ServiceManager was healthy and stopped at the same time. This
	 * test ensures that listeners fire and isHealthy makes sense.
	 */
	public void testEmptyServiceManager() {
		Logger logger = Logger.getLogger(ServiceManager.class.getName());
		logger.setLevel(Level.FINEST);
		
		TestLogHandler logHandler = new TestLogHandler();
		logger.addHandler(logHandler);
		
		ServiceManager manager = new ServiceManager(Arrays.<Service>asList());
		System.out.println(manager);
		
		RecordingListener listener = new RecordingListener();
		manager.addListener(listener);
		
		manager.startAsync().awaitHealthy();
		System.out.println(manager);
		
		assertTrue(manager.isHealthy());
		assertTrue(listener.healthyCalled);
		assertFalse(listener.stoppedCalled);
		assertTrue(listener.failedServices.isEmpty());
		
		manager.stopAsync().awaitStopped();
		System.out.println(manager);
		
		assertFalse(manager.isHealthy());
		assertTrue(listener.stoppedCalled);
		assertTrue(listener.failedServices.isEmpty());
		// check that our NoOpService is not directly observable via any of the inspection methods or
		// via logging.
		assertEquals("ServiceManager{services=[]}", manager.toString());
		assertTrue(manager.servicesByState().isEmpty());
		assertTrue(manager.startupTimes().isEmpty());
		
		Formatter logFormatter = new Formatter() {
			@Override
			public String format(LogRecord record) {
				return formatMessage(record);
			}
		};
		
		for (LogRecord record : logHandler.getStoredLogRecords()) {
			assertThat(logFormatter.format(record)).doesNotContain("NoOpService");
		}
	}

	/**
	 * Tests that a ServiceManager can be fully shut down if one of its failure listeners is slow or even permanently
	 * blocked.
	 */

	public void testListenerDeadlock() throws InterruptedException {
		final CountDownLatch failEnter = new CountDownLatch(1);
		final CountDownLatch failLeave = new CountDownLatch(1);
		final CountDownLatch afterStarted = new CountDownLatch(1);
		
		Service failRunService = new AbstractService() {
			@Override
			protected void doStart() {
				new Thread() {
					@Override
					public void run() {
						notifyStarted();
						// We need to wait for the main thread to leave the ServiceManager.startAsync call
						// to
						// ensure that the thread running the failure callbacks is not the main thread.
						Uninterruptibles.awaitUninterruptibly(afterStarted);
						notifyFailed(new Exception("boom"));
					}
				}.start();
			}

			@Override
			protected void doStop() {
				notifyStopped();
			}
		};
		
		final ServiceManager manager = new ServiceManager(Arrays.asList(failRunService, new NoOpService()));
		manager.addListener(new ServiceManager.Listener() {
			@Override
			public void failure(Service service) {
				failEnter.countDown();
				// block until after the service manager is shutdown
				Uninterruptibles.awaitUninterruptibly(failLeave);
			}
		});
		manager.startAsync();
		
		afterStarted.countDown();
		// We do not call awaitHealthy because, due to races, that method may throw an exception. But
		// we really just want to wait for the thread to be in the failure callback so we wait for that
		// explicitly instead.
		failEnter.await();
		assertFalse("State should be updated before calling listeners", manager.isHealthy());
		// now we want to stop the services.
		Thread stoppingThread = new Thread() {
			@Override
			public void run() {
				manager.stopAsync().awaitStopped();
			}
		};
		stoppingThread.start();
		// this should be super fast since the only non stopped service is a NoOpService
		stoppingThread.join(1000);
		assertFalse("stopAsync has deadlocked!.", stoppingThread.isAlive());
		failLeave.countDown(); // release the background thread
	}

	/**
	 * Catches a bug where when constructing a service manager failed, later interactions with the service could cause
	 * IllegalStateExceptions inside the partially constructed ServiceManager. This ISE wouldn't actually bubble up but
	 * would get logged by ExecutionQueue. This obfuscated the original error (which was not constructing ServiceManager
	 * correctly).
	 */
	public void testPartiallyConstructedManager() {
		Logger logger = Logger.getLogger("global");
		logger.setLevel(Level.FINEST);
		TestLogHandler logHandler = new TestLogHandler();
		logger.addHandler(logHandler);
		NoOpService service = new NoOpService();
		service.startAsync();
		try {
			new ServiceManager(Arrays.asList(service));
			fail();
		} catch (IllegalArgumentException expected) {
		}
		service.stopAsync();
		// Nothing was logged!
		assertEquals(0, logHandler.getStoredLogRecords().size());
	}

	public void testPartiallyConstructedManager_transitionAfterAddListenerBeforeStateIsReady() {
		// The implementation of this test is pretty sensitive to the implementation :( but we want to
		// ensure that if weird things happen during construction then we get exceptions.
		final NoOpService service1 = new NoOpService();
		// This service will start service1 when addListener is called. This simulates service1 being
		// started asynchronously.
		Service service2 = new Service() {
			final NoOpService delegate = new NoOpService();

			@Override
			public final void addListener(Listener listener, Executor executor) {
				service1.startAsync();
				delegate.addListener(listener, executor);
			}

			// Delegates from here on down
			@Override
			public final Service startAsync() {
				return delegate.startAsync();
			}

			@Override
			public final Service stopAsync() {
				return delegate.stopAsync();
			}

			@Override
			public final void awaitRunning() {
				delegate.awaitRunning();
			}

			@Override
			public final void awaitRunning(long timeout, TimeUnit unit) throws TimeoutException {
				delegate.awaitRunning(timeout, unit);
			}

			@Override
			public final void awaitTerminated() {
				delegate.awaitTerminated();
			}

			@Override
			public final void awaitTerminated(long timeout, TimeUnit unit) throws TimeoutException {
				delegate.awaitTerminated(timeout, unit);
			}

			@Override
			public final boolean isRunning() {
				return delegate.isRunning();
			}

			@Override
			public final State state() {
				return delegate.state();
			}

			@Override
			public final Throwable failureCause() {
				return delegate.failureCause();
			}
		};
		try {
			new ServiceManager(Arrays.asList(service1, service2));
			fail();
		} catch (IllegalArgumentException expected) {
			assertThat(expected.getMessage()).contains("started transitioning asynchronously");
		}
	}

	/**
	 * This test is for a case where two Service.Listener callbacks for the same service would call transitionService in
	 * the wrong order due to a race. Due to the fact that it is a race this test isn't guaranteed to expose the issue,
	 * but it is at least likely to become flaky if the race sneaks back in, and in this case flaky means something is
	 * definitely wrong.
	 * <p>
	 * Before the bug was fixed this test would fail at least 30% of the time.
	 */

	public void testTransitionRace() throws TimeoutException {
		for (int k = 0; k < 1000; k++) {
			List<Service> services = Lists.newArrayList();
			for (int i = 0; i < 5; i++) {
				services.add(new SnappyShutdownService(i));
			}
			ServiceManager manager = new ServiceManager(services);
			manager.startAsync().awaitHealthy();
			manager.stopAsync().awaitStopped(1, TimeUnit.SECONDS);
		}
	}

	/**
	 * This service will shutdown very quickly after stopAsync is called and uses a background thread so that we know
	 * that the stopping() listeners will execute on a different thread than the terminated() listeners.
	 */
	private static class SnappyShutdownService extends AbstractExecutionThreadService {
		final int index;
		final CountDownLatch latch = new CountDownLatch(1);

		SnappyShutdownService(int index) {
			this.index = index;
		}

		@Override
		protected void run() throws Exception {
			latch.await();
		}

		@Override
		protected void triggerShutdown() {
			latch.countDown();
		}

		@Override
		protected String serviceName() {
			return this.getClass().getSimpleName() + "[" + index + "]";
		}
	}

	public void testNulls() {
		ServiceManager manager = new ServiceManager(Arrays.<Service> asList());
		new NullPointerTester().setDefault(ServiceManager.Listener.class, new RecordingListener()).testAllPublicInstanceMethods(manager);
	}
}