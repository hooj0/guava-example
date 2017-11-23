package com.cnblogs.hoojo.concurrency.service._abstract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.concurrent.GuardedBy;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.common.util.concurrent.AbstractService;
import com.google.common.util.concurrent.Atomics;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;

public class AbstractServiceTest {

	private static final long LONG_TIMEOUT_MILLIS = 2500;
	TimeUnit SECONDS = TimeUnit.SECONDS;

	private Thread executionThread;
	private Throwable thrownByExecutionThread;
	
	private static class RecordingListener extends Listener {
		static RecordingListener record(Service service) {
			RecordingListener listener = new RecordingListener(service);
			service.addListener(listener, MoreExecutors.directExecutor());
			return listener;
		}

		final Service service;

		RecordingListener(Service service) {
			this.service = service;
		}

		@GuardedBy("this")
		final List<State> stateHistory = Lists.newArrayList();
		final CountDownLatch completionLatch = new CountDownLatch(1);

		ImmutableList<State> getStateHistory() throws Exception {
			completionLatch.await();
			synchronized (this) {
				return ImmutableList.copyOf(stateHistory);
			}
		}

		@Override
		public synchronized void starting() {
			System.out.println("Listener.starting: " + service.state() + ", isRunning: " + service.isRunning());
			
			assertTrue(stateHistory.isEmpty()); // empty
			assertNotSame(State.NEW, service.state()); // NEW
			stateHistory.add(State.STARTING); 
		}

		@Override
		public synchronized void running() {
			System.out.println("Listener.running: " + service.state() + ", isRunning: " + service.isRunning());
			
			assertEquals(State.STARTING, Iterables.getOnlyElement(stateHistory)); // 仅仅启动一次 STARTING
			stateHistory.add(State.RUNNING);
			
			System.out.println("--------running.awaitRunning--------");
			service.awaitRunning(); // 运行
			assertNotSame(State.STARTING, service.state()); // service is STARTING
		}

		@Override
		public synchronized void stopping(State from) {
			System.out.println("Listener.stopping: " + service.state() + ", from:" + from + ", isRunning: " + service.isRunning());
			
			assertEquals(from, Iterables.getLast(stateHistory)); // TERMINATED 正常
			stateHistory.add(State.STOPPING);
			if (from == State.STARTING) { // 如果状态又变为 STARTING 则继续运行
				try {
					System.out.println("--------stopping.awaitRunning--------");
					service.awaitRunning();
					fail();
				} catch (IllegalStateException expected) {
					assertNull(expected.getCause());
					assertTrue(expected.getMessage().equals("Expected the service " + service + " to be RUNNING, but was STOPPING"));
				}
			}
			assertNotSame(from, service.state());
		}

		@Override
		public synchronized void terminated(State from) {
			System.out.println("Listener.terminated: " + service.state() + ", from:" + from + ", isRunning: " + service.isRunning());
			
			assertEquals(from, Iterables.getLast(stateHistory, State.NEW)); // NEW
			stateHistory.add(State.TERMINATED);
			assertEquals(State.TERMINATED, service.state()); // TERMINATED
			if (from == State.NEW) { // 如果是NEW则继续运行
				try {
					System.out.println("--------terminated.awaitRunning--------");
					service.awaitRunning();
					fail();
				} catch (IllegalStateException expected) {
					assertNull(expected.getCause());
					assertTrue(expected.getMessage().equals("Expected the service " + service + " to be RUNNING, but was TERMINATED"));
				}
			}
			completionLatch.countDown(); // 结束
		}

		@Override
		public synchronized void failed(State from, Throwable failure) {
			System.out.println("Listener.failed: " + service.state() + ", from:" + from + ", isRunning: " + service.isRunning());
			
			assertEquals(from, Iterables.getLast(stateHistory));
			stateHistory.add(State.FAILED);
			assertEquals(State.FAILED, service.state()); // FAILED
			assertEquals(failure, service.failureCause()); // Throwable
			if (from == State.STARTING) {
				try {
					System.out.println("--------failed.awaitRunning--------");
					service.awaitRunning();
					fail();
				} catch (IllegalStateException e) {
					assertEquals(failure, e.getCause());
				}
			}
			try {
				System.out.println("--------failed.awaitTerminated--------");
				service.awaitTerminated();
				fail();
			} catch (IllegalStateException e) {
				assertEquals(failure, e.getCause());
			}
			completionLatch.countDown(); // 结束
		}
	}
	
	private static class NoOpService extends AbstractService {
		boolean running = false;

		@Override
		protected void doStart() {
			System.out.println("doStart: " + running + ", state: " + this.state() + ", isRunning: " + this.isRunning());
			running = true;
			
			System.out.println("--------doStart.notifyStarted--------");
			notifyStarted();
		}

		@Override
		protected void doStop() {
			System.out.println("doStop: " + running + ", state: " + this.state() + ", isRunning: " + this.isRunning());
			running = false;
			
			System.out.println("--------doStop.notifyStopped--------");
			notifyStopped();
		}

		public void notifyFailed2(Throwable cause) {
			System.out.println("1.notifyFailed: " + running + ", state: " + this.state() + ", isRunning: " + this.isRunning());

			System.out.println("--------notifyFailed--------");
			super.notifyFailed(cause);
			
			System.out.println("2.notifyFailed: " + running + ", state: " + this.state() + ", isRunning: " + this.isRunning());
		}
	}

	@Test
	public void testNoOpServiceStartStop() throws Exception {
		
		System.out.println("--------new--------");
		NoOpService service = new NoOpService();
		RecordingListener listener = RecordingListener.record(service);

		System.out.println(service.state()); // State.NEW
		System.out.println(service.isRunning()); // false
		System.out.println(service.running); // false

		System.out.println("--------startAsync--------");
		// 运行 startAsync 立即运行 doStart，doStart中运行notifyStarted，触发enqueueRunningEvent，状态以被置为 running
		service.startAsync();
		System.out.println(service.state()); // State.RUNNING
		System.out.println(service.isRunning()); // true
		System.out.println(service.running); // true

		System.out.println("--------stopAsync--------");
		// 运行 stopAsync 立即运行 doStop，doStart中运行notifyStarted，触发enqueueTerminatedEvent，状态以被置为 Terminated
		service.stopAsync();
		System.out.println(service.state()); // State.TERMINATED
		System.out.println(service.isRunning()); // false
		System.out.println(service.running); // false
		assertEquals(ImmutableList.of(State.STARTING, State.RUNNING, State.STOPPING, State.TERMINATED), listener.getStateHistory());
		
		System.out.println(listener.getStateHistory());
	}

	@Test
	public void testNoOpServiceStartAndWaitStopAndWait() throws Exception {
		System.out.println("--------new--------");
		NoOpService service = new NoOpService();
		RecordingListener.record(service);
		
		System.out.println(service.state()); // State.NEW
		System.out.println(service.isRunning()); // false
		System.out.println(service.running); // false

		System.out.println("--------startAsync.awaitRunning--------");
		service.startAsync().awaitRunning();
		assertEquals(State.RUNNING, service.state());
		System.out.println(service.state()); // State.RUNNING
		System.out.println(service.isRunning()); // true
		System.out.println(service.running); // true
		
		System.out.println("--------stopAsync.awaitTerminated--------");
		service.stopAsync().awaitTerminated();
		assertEquals(State.TERMINATED, service.state());
		System.out.println(service.state()); // State.TERMINATED
		System.out.println(service.isRunning()); // false
		System.out.println(service.running); // false
	}

	@Test
	public void testNoOpServiceStartAsyncAndAwaitStopAsyncAndAwait() throws Exception {
		System.out.println("--------new--------");
		NoOpService service = new NoOpService();
		
		System.out.println(service.state()); // State.NEW
		System.out.println(service.isRunning()); // false
		System.out.println(service.running); // false

		System.out.println("--------startAsync.awaitRunning--------");
		service.startAsync().awaitRunning();
		assertEquals(State.RUNNING, service.state());
		
		System.out.println(service.state()); // State.RUNNING
		System.out.println(service.isRunning()); // true
		System.out.println(service.running); // true

		System.out.println("--------stopAsync.awaitTerminated--------");
		service.stopAsync().awaitTerminated();
		assertEquals(State.TERMINATED, service.state());
		
		System.out.println(service.state()); // State.TERMINATED
		System.out.println(service.isRunning()); // false
		System.out.println(service.running); // false
	}

	@Test
	public void testNoOpServiceStopIdempotence() throws Exception {
		System.out.println("--------new--------");
		NoOpService service = new NoOpService();
		RecordingListener listener = RecordingListener.record(service);
		System.out.println(service.state()); // State.NEW
		System.out.println(service.isRunning()); // false
		System.out.println(service.running); // false
		
		System.out.println("--------startAsync.awaitRunning--------");
		service.startAsync().awaitRunning();
		assertEquals(State.RUNNING, service.state());
		System.out.println(service.state()); // State.RUNNING
		System.out.println(service.isRunning()); // true
		System.out.println(service.running); // true

		System.out.println("--------stopAsync--------");
		service.stopAsync();
		System.out.println(service.state()); // State.TERMINATED
		System.out.println(service.isRunning()); // false
		System.out.println(service.running); // false
		
		System.out.println("--------stopAsync--------");
		service.stopAsync();
		assertEquals(State.TERMINATED, service.state());
		System.out.println(service.state()); // State.TERMINATED
		System.out.println(service.isRunning()); // false
		System.out.println(service.running); // false
		
		assertEquals(ImmutableList.of(State.STARTING, State.RUNNING, State.STOPPING, State.TERMINATED), listener.getStateHistory());
	}

	@Test
	public void testNoOpServiceStopIdempotenceAfterWait() throws Exception {
		System.out.println("--------new--------");
		NoOpService service = new NoOpService();
		System.out.println(service.state()); // State.NEW
		System.out.println(service.isRunning()); // false
		System.out.println(service.running); // false

		System.out.println("--------startAsync.awaitRunning--------");
		service.startAsync().awaitRunning();
		System.out.println(service.state()); // State.RUNNING
		System.out.println(service.isRunning()); // true
		System.out.println(service.running); // true

		System.out.println("--------stopAsync.awaitTerminated--------");
		service.stopAsync().awaitTerminated();
		System.out.println(service.state()); // State.TERMINATED
		System.out.println(service.isRunning()); // false
		System.out.println(service.running); // false
		
		System.out.println("--------stopAsync--------");
		service.stopAsync();
		assertEquals(State.TERMINATED, service.state());
		System.out.println(service.state()); // State.NEW
		System.out.println(service.isRunning()); // false
		System.out.println(service.running); // false
	}

	@Test
	public void testNoOpServiceStopIdempotenceDoubleWait() throws Exception {
		System.out.println("--------new--------");
		NoOpService service = new NoOpService();
		System.out.println(service.state()); // State.NEW
		System.out.println(service.isRunning()); // false
		System.out.println(service.running); // false

		System.out.println("--------startAsync.awaitRunning--------");
		service.startAsync().awaitRunning();
		assertEquals(State.RUNNING, service.state());
		System.out.println(service.state()); // State.RUNNING
		System.out.println(service.isRunning()); // true
		System.out.println(service.running); // true

		System.out.println("--------stopAsync.awaitTerminated--------");
		service.stopAsync().awaitTerminated();
		System.out.println(service.state()); // State.TERMINATED
		System.out.println(service.isRunning()); // false
		System.out.println(service.running); // false
		
		System.out.println("--------stopAsync.awaitTerminated--------");
		service.stopAsync().awaitTerminated();
		assertEquals(State.TERMINATED, service.state());
		System.out.println(service.state()); // State.TERMINATED
		System.out.println(service.isRunning()); // false
		System.out.println(service.running); // false
	}

	@Test
	public void testNoOpServiceStartStopAndWaitUninterruptible() throws Exception {
		System.out.println("--------new--------");
		NoOpService service = new NoOpService();
		System.out.println(service.state()); // State.NEW
		System.out.println(service.isRunning()); // false
		System.out.println(service.running); // false


		Thread.currentThread().interrupt();
		try {
			System.out.println("--------startAsync--------");
			service.startAsync().awaitRunning();
			assertEquals(State.RUNNING, service.state());
			System.out.println(service.state()); // State.RUNNING
			System.out.println(service.isRunning()); // true
			System.out.println(service.running); // true

			System.out.println("--------stopAsync--------");
			service.stopAsync().awaitTerminated();
			assertEquals(State.TERMINATED, service.state());
			System.out.println(service.state()); // State.TERMINATED
			System.out.println(service.isRunning()); // false
			System.out.println(service.running); // false

			assertTrue(Thread.currentThread().isInterrupted());
		} finally {
			Thread.interrupted(); // clear interrupt for future tests
		}
	}
	
	@Test
	public void testAwaitTerminated() throws Exception {
		System.out.println("--------new--------");
		final NoOpService service = new NoOpService();
		System.out.println(service.state()); // State.NEW
		System.out.println(service.isRunning()); // false
		System.out.println(service.running); // false
		
		// 在stopAsync后，未调用awaitTerminated 结束线程，会导致长时间等待阻塞
		Thread waiter = new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				// 阻塞
				service.awaitTerminated();
			}
		};
		waiter.start();
		
		// 一直等待 waiter执行完成，由于没有调用stopAsync，导致awaitTerminated长时间等待阻塞
		//waiter.join();
		
		System.out.println("--------startAsync.awaitRunning--------");
		service.startAsync().awaitRunning();
		assertEquals(State.RUNNING, service.state());
		System.out.println(service.state()); // State.RUNNING
		System.out.println(service.isRunning()); // true
		System.out.println(service.running); // true
		
		System.out.println("--------stopAsync--------");
		service.stopAsync();
		
		// 正常调用awaitTerminated
		// service.awaitTerminated();
		
		// waiter.start() 后调用 service.awaitTerminated()后，waiter进入阻塞状态
		System.out.println("waiter state:" + waiter.getState()); // TIMED_WAITING
		
		// 正常情况下waiter启动后立即运行就结束， waiter.start() 后调用 service.awaitTerminated()后，waiter进入阻塞状态
		// 由于waiter.join主线程会等待执行完成
		// 同时，由于主线程调用service.stopAsync()后，才能运行awaitTerminated() ，故上面代码可以顺利执行
		waiter.join(LONG_TIMEOUT_MILLIS); // ensure that the await in the other thread is triggered
		System.out.println("waiter state:" + waiter.getState()); // TERMINATED
		assertFalse(waiter.isAlive());
		
		System.out.println(service.state()); // State.TERMINATED
		System.out.println(service.isRunning()); // false
		System.out.println(service.running); // false
	}
	
	/**
	 * The user of this service should call {@link #notifyStarted} and {@link #notifyStopped} after calling
	 * {@link #startAsync} and {@link #stopAsync}.
	 */
	private static class ManualSwitchedService extends AbstractService {
		boolean doStartCalled = false;
		boolean doStopCalled = false;

		@Override
		protected void doStart() {
			assertFalse(doStartCalled);
			doStartCalled = true;
		}

		@Override
		protected void doStop() {
			assertFalse(doStopCalled);
			doStopCalled = true;
		}

		public void notifyStarted2() {
			super.notifyStarted();
		}

		public void notifyStopped2() {
			super.notifyStopped();
		}

		public void notifyFailed2(Throwable cause) {
			super.notifyFailed(cause);
		}
	}

	public void testManualServiceStartStop() throws Exception {
		ManualSwitchedService service = new ManualSwitchedService();
		RecordingListener listener = RecordingListener.record(service);

		service.startAsync();
		assertEquals(State.STARTING, service.state());
		assertFalse(service.isRunning());
		assertTrue(service.doStartCalled);

		service.notifyStarted2(); // usually this would be invoked by another thread
		assertEquals(State.RUNNING, service.state());
		assertTrue(service.isRunning());

		service.stopAsync();
		assertEquals(State.STOPPING, service.state());
		assertFalse(service.isRunning());
		assertTrue(service.doStopCalled);

		service.notifyStopped2(); // usually this would be invoked by another thread
		assertEquals(State.TERMINATED, service.state());
		assertFalse(service.isRunning());
		assertEquals(ImmutableList.of(State.STARTING, State.RUNNING, State.STOPPING, State.TERMINATED), listener.getStateHistory());

	}

	public void testManualServiceNotifyStoppedWhileRunning() throws Exception {
		ManualSwitchedService service = new ManualSwitchedService();
		RecordingListener listener = RecordingListener.record(service);

		service.startAsync();
		service.notifyStarted2();
		service.notifyStopped2();
		assertEquals(State.TERMINATED, service.state());
		assertFalse(service.isRunning());
		assertFalse(service.doStopCalled);

		assertEquals(ImmutableList.of(State.STARTING, State.RUNNING, State.TERMINATED), listener.getStateHistory());
	}

	public void testManualServiceStopWhileStarting() throws Exception {
		ManualSwitchedService service = new ManualSwitchedService();
		RecordingListener listener = RecordingListener.record(service);

		service.startAsync();
		assertEquals(State.STARTING, service.state());
		assertFalse(service.isRunning());
		assertTrue(service.doStartCalled);

		service.stopAsync();
		assertEquals(State.STOPPING, service.state());
		assertFalse(service.isRunning());
		assertFalse(service.doStopCalled);

		service.notifyStarted2();
		assertEquals(State.STOPPING, service.state());
		assertFalse(service.isRunning());
		assertTrue(service.doStopCalled);

		service.notifyStopped2();
		assertEquals(State.TERMINATED, service.state());
		assertFalse(service.isRunning());
		assertEquals(ImmutableList.of(State.STARTING, State.STOPPING, State.TERMINATED), listener.getStateHistory());
	}

	/**
	 * This tests for a bug where if {@link Service#stopAsync()} was called while the service was {@link State#STARTING}
	 * more than once, the {@link Listener#stopping(State)} callback would get called multiple times.
	 */
	public void testManualServiceStopMultipleTimesWhileStarting() throws Exception {
		ManualSwitchedService service = new ManualSwitchedService();
		final AtomicInteger stopppingCount = new AtomicInteger();
		service.addListener(new Listener() {
			@Override
			public void stopping(State from) {
				stopppingCount.incrementAndGet();
			}
		}, MoreExecutors.directExecutor());

		service.startAsync();
		service.stopAsync();
		assertEquals(1, stopppingCount.get());
		service.stopAsync();
		assertEquals(1, stopppingCount.get());
	}

	public void testManualServiceStopWhileNew() throws Exception {
		ManualSwitchedService service = new ManualSwitchedService();
		RecordingListener listener = RecordingListener.record(service);

		service.stopAsync();
		assertEquals(State.TERMINATED, service.state());
		assertFalse(service.isRunning());
		assertFalse(service.doStartCalled);
		assertFalse(service.doStopCalled);
		assertEquals(ImmutableList.of(State.TERMINATED), listener.getStateHistory());
	}

	public void testManualServiceFailWhileStarting() throws Exception {
		ManualSwitchedService service = new ManualSwitchedService();
		RecordingListener listener = RecordingListener.record(service);
		service.startAsync();
		service.notifyFailed2(EXCEPTION);
		assertEquals(ImmutableList.of(State.STARTING, State.FAILED), listener.getStateHistory());
	}

	public void testManualServiceFailWhileRunning() throws Exception {
		ManualSwitchedService service = new ManualSwitchedService();
		RecordingListener listener = RecordingListener.record(service);
		service.startAsync();
		service.notifyStarted2();
		service.notifyFailed2(EXCEPTION);
		assertEquals(ImmutableList.of(State.STARTING, State.RUNNING, State.FAILED), listener.getStateHistory());
	}

	public void testManualServiceFailWhileStopping() throws Exception {
		ManualSwitchedService service = new ManualSwitchedService();
		RecordingListener listener = RecordingListener.record(service);
		service.startAsync();
		service.notifyStarted2();
		service.stopAsync();
		service.notifyFailed2(EXCEPTION);
		assertEquals(ImmutableList.of(State.STARTING, State.RUNNING, State.STOPPING, State.FAILED),
				listener.getStateHistory());
	}

	public void testManualServiceUnrequestedStop() {
		ManualSwitchedService service = new ManualSwitchedService();

		service.startAsync();

		service.notifyStarted2();
		assertEquals(State.RUNNING, service.state());
		assertTrue(service.isRunning());
		assertFalse(service.doStopCalled);

		service.notifyStopped2();
		assertEquals(State.TERMINATED, service.state());
		assertFalse(service.isRunning());
		assertFalse(service.doStopCalled);
	}

	public void testAwaitTerminated_FailedService() throws Exception {
		final ManualSwitchedService service = new ManualSwitchedService();
		final AtomicReference<Throwable> exception = Atomics.newReference();
		Thread waiter = new Thread() {
			@Override
			public void run() {
				try {
					service.awaitTerminated();
					fail("Expected an IllegalStateException");
				} catch (Throwable t) {
					exception.set(t);
				}
			}
		};
		waiter.start();
		service.startAsync();
		service.notifyStarted2();
		assertEquals(State.RUNNING, service.state());
		service.notifyFailed2(EXCEPTION);
		assertEquals(State.FAILED, service.state());
		waiter.join(LONG_TIMEOUT_MILLIS);
		assertFalse(waiter.isAlive());
		// assertThat(exception.get(), IllegalStateException.class);
		assertEquals(EXCEPTION, exception.get().getCause());
	}

	public void testThreadedServiceStartAndWaitStopAndWait() throws Throwable {
		ThreadedService service = new ThreadedService();
		RecordingListener listener = RecordingListener.record(service);
		service.startAsync().awaitRunning();
		assertEquals(State.RUNNING, service.state());

		service.awaitRunChecks();

		service.stopAsync().awaitTerminated();
		assertEquals(State.TERMINATED, service.state());

		throwIfSet(thrownByExecutionThread);
		assertEquals(ImmutableList.of(State.STARTING, State.RUNNING, State.STOPPING, State.TERMINATED),
				listener.getStateHistory());
	}

	public void testThreadedServiceStopIdempotence() throws Throwable {
		ThreadedService service = new ThreadedService();

		service.startAsync().awaitRunning();
		assertEquals(State.RUNNING, service.state());

		service.awaitRunChecks();

		service.stopAsync();
		service.stopAsync().awaitTerminated();
		assertEquals(State.TERMINATED, service.state());

		throwIfSet(thrownByExecutionThread);
	}

	public void testThreadedServiceStopIdempotenceAfterWait() throws Throwable {
		ThreadedService service = new ThreadedService();

		service.startAsync().awaitRunning();
		assertEquals(State.RUNNING, service.state());

		service.awaitRunChecks();

		service.stopAsync().awaitTerminated();
		service.stopAsync();
		assertEquals(State.TERMINATED, service.state());

		executionThread.join();

		throwIfSet(thrownByExecutionThread);
	}

	public void testThreadedServiceStopIdempotenceDoubleWait() throws Throwable {
		ThreadedService service = new ThreadedService();

		service.startAsync().awaitRunning();
		assertEquals(State.RUNNING, service.state());

		service.awaitRunChecks();

		service.stopAsync().awaitTerminated();
		service.stopAsync().awaitTerminated();
		assertEquals(State.TERMINATED, service.state());

		throwIfSet(thrownByExecutionThread);
	}

	public void testManualServiceFailureIdempotence() {
		ManualSwitchedService service = new ManualSwitchedService();
		/*
		 * Set up a RecordingListener to perform its built-in assertions, even though we won't look at its state
		 * history.
		 */
		RecordingListener unused = RecordingListener.record(service);
		service.startAsync();
		service.notifyFailed2(new Exception("1"));
		service.notifyFailed2(new Exception("2"));
		// assertThat(service.failureCause()).hasMessage("1");
		try {
			service.awaitRunning();
			fail();
		} catch (Exception e) {
			// assertThat(e.getCause()).hasMessage("1");
		}
	}

	private class ThreadedService extends AbstractService {
		final CountDownLatch hasConfirmedIsRunning = new CountDownLatch(1);

		/*
		 * The main test thread tries to stop() the service shortly after confirming that it is running. Meanwhile, the
		 * service itself is trying to confirm that it is running. If the main thread's stop() call happens before it
		 * has the chance, the test will fail. To avoid this, the main thread calls this method, which waits until the
		 * service has performed its own "running" check.
		 */
		void awaitRunChecks() throws InterruptedException {
			assertTrue("Service thread hasn't finished its checks. " + "Exception status (possibly stale): "
					+ thrownByExecutionThread, hasConfirmedIsRunning.await(10, SECONDS));
		}

		@Override
		protected void doStart() {
			assertEquals(State.STARTING, state());
			invokeOnExecutionThreadForTest(new Runnable() {
				@Override
				public void run() {
					assertEquals(State.STARTING, state());
					notifyStarted();
					assertEquals(State.RUNNING, state());
					hasConfirmedIsRunning.countDown();
				}
			});
		}

		@Override
		protected void doStop() {
			assertEquals(State.STOPPING, state());
			invokeOnExecutionThreadForTest(new Runnable() {
				@Override
				public void run() {
					assertEquals(State.STOPPING, state());
					notifyStopped();
					assertEquals(State.TERMINATED, state());
				}
			});
		}
	}

	private void invokeOnExecutionThreadForTest(Runnable runnable) {
		executionThread = new Thread(runnable);
		executionThread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable e) {
				thrownByExecutionThread = e;
			}
		});
		executionThread.start();
	}

	private static void throwIfSet(Throwable t) throws Throwable {
		if (t != null) {
			throw t;
		}
	}

	public void testStopUnstartedService() throws Exception {
		NoOpService service = new NoOpService();
		RecordingListener listener = RecordingListener.record(service);

		service.stopAsync();
		assertEquals(State.TERMINATED, service.state());

		try {
			service.startAsync();
			fail();
		} catch (IllegalStateException expected) {
		}
		assertEquals(State.TERMINATED, Iterables.getOnlyElement(listener.getStateHistory()));
	}

	public void testFailingServiceStartAndWait() throws Exception {
		StartFailingService service = new StartFailingService();
		RecordingListener listener = RecordingListener.record(service);

		try {
			service.startAsync().awaitRunning();
			fail();
		} catch (IllegalStateException e) {
			assertEquals(EXCEPTION, service.failureCause());
			assertEquals(EXCEPTION, e.getCause());
		}
		assertEquals(ImmutableList.of(State.STARTING, State.FAILED), listener.getStateHistory());
	}

	public void testFailingServiceStopAndWait_stopFailing() throws Exception {
		StopFailingService service = new StopFailingService();
		RecordingListener listener = RecordingListener.record(service);

		service.startAsync().awaitRunning();
		try {
			service.stopAsync().awaitTerminated();
			fail();
		} catch (IllegalStateException e) {
			assertEquals(EXCEPTION, service.failureCause());
			assertEquals(EXCEPTION, e.getCause());
		}
		assertEquals(ImmutableList.of(State.STARTING, State.RUNNING, State.STOPPING, State.FAILED),
				listener.getStateHistory());
	}

	public void testFailingServiceStopAndWait_runFailing() throws Exception {
		RunFailingService service = new RunFailingService();
		RecordingListener listener = RecordingListener.record(service);

		service.startAsync();
		try {
			service.awaitRunning();
			fail();
		} catch (IllegalStateException e) {
			assertEquals(EXCEPTION, service.failureCause());
			assertEquals(EXCEPTION, e.getCause());
		}
		assertEquals(ImmutableList.of(State.STARTING, State.RUNNING, State.FAILED), listener.getStateHistory());
	}

	public void testThrowingServiceStartAndWait() throws Exception {
		StartThrowingService service = new StartThrowingService();
		RecordingListener listener = RecordingListener.record(service);

		try {
			service.startAsync().awaitRunning();
			fail();
		} catch (IllegalStateException e) {
			assertEquals(service.exception, service.failureCause());
			assertEquals(service.exception, e.getCause());
		}
		assertEquals(ImmutableList.of(State.STARTING, State.FAILED), listener.getStateHistory());
	}

	public void testThrowingServiceStopAndWait_stopThrowing() throws Exception {
		StopThrowingService service = new StopThrowingService();
		RecordingListener listener = RecordingListener.record(service);

		service.startAsync().awaitRunning();
		try {
			service.stopAsync().awaitTerminated();
			fail();
		} catch (IllegalStateException e) {
			assertEquals(service.exception, service.failureCause());
			assertEquals(service.exception, e.getCause());
		}
		assertEquals(ImmutableList.of(State.STARTING, State.RUNNING, State.STOPPING, State.FAILED),
				listener.getStateHistory());
	}

	public void testThrowingServiceStopAndWait_runThrowing() throws Exception {
		RunThrowingService service = new RunThrowingService();
		RecordingListener listener = RecordingListener.record(service);

		service.startAsync();
		try {
			service.awaitTerminated();
			fail();
		} catch (IllegalStateException e) {
			assertEquals(service.exception, service.failureCause());
			assertEquals(service.exception, e.getCause());
		}
		assertEquals(ImmutableList.of(State.STARTING, State.RUNNING, State.FAILED), listener.getStateHistory());
	}

	public void testFailureCause_throwsIfNotFailed() {
		StopFailingService service = new StopFailingService();
		try {
			service.failureCause();
			fail();
		} catch (IllegalStateException expected) {
		}
		service.startAsync().awaitRunning();
		try {
			service.failureCause();
			fail();
		} catch (IllegalStateException expected) {
		}
		try {
			service.stopAsync().awaitTerminated();
			fail();
		} catch (IllegalStateException e) {
			assertEquals(EXCEPTION, service.failureCause());
			assertEquals(EXCEPTION, e.getCause());
		}
	}

	public void testAddListenerAfterFailureDoesntCauseDeadlock() throws InterruptedException {
		final StartFailingService service = new StartFailingService();
		service.startAsync();
		assertEquals(State.FAILED, service.state());
		service.addListener(new RecordingListener(service), MoreExecutors.directExecutor());
		Thread thread = new Thread() {
			@Override
			public void run() {
				// Internally stopAsync() grabs a lock, this could be any such method on AbstractService.
				service.stopAsync();
			}
		};
		thread.start();
		thread.join(LONG_TIMEOUT_MILLIS);
		assertFalse(thread + " is deadlocked", thread.isAlive());
	}

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

	private static class NoOpThreadedService extends AbstractExecutionThreadService {
		final CountDownLatch latch = new CountDownLatch(1);

		@Override
		protected void run() throws Exception {
			latch.await();
		}

		@Override
		protected void triggerShutdown() {
			latch.countDown();
		}
	}

	private static class StartFailingService extends AbstractService {
		@Override
		protected void doStart() {
			notifyFailed(EXCEPTION);
		}

		@Override
		protected void doStop() {
			fail();
		}
	}

	private static class RunFailingService extends AbstractService {
		@Override
		protected void doStart() {
			notifyStarted();
			notifyFailed(EXCEPTION);
		}

		@Override
		protected void doStop() {
			fail();
		}
	}

	private static class StopFailingService extends AbstractService {
		@Override
		protected void doStart() {
			notifyStarted();
		}

		@Override
		protected void doStop() {
			notifyFailed(EXCEPTION);
		}
	}

	private static class StartThrowingService extends AbstractService {

		final RuntimeException exception = new RuntimeException("deliberate");

		@Override
		protected void doStart() {
			throw exception;
		}

		@Override
		protected void doStop() {
			fail();
		}
	}

	private static class RunThrowingService extends AbstractService {

		final RuntimeException exception = new RuntimeException("deliberate");

		@Override
		protected void doStart() {
			notifyStarted();
			throw exception;
		}

		@Override
		protected void doStop() {
			fail();
		}
	}

	private static class StopThrowingService extends AbstractService {

		final RuntimeException exception = new RuntimeException("deliberate");

		@Override
		protected void doStart() {
			notifyStarted();
		}

		@Override
		protected void doStop() {
			throw exception;
		}
	}

	

	public void testNotifyStartedWhenNotStarting() {
		DefaultService service = new DefaultService();
		try {
			service.notifyStarted2();
			fail();
		} catch (IllegalStateException expected) {
		}
	}

	public void testNotifyStoppedWhenNotRunning() {
		DefaultService service = new DefaultService();
		try {
			service.notifyStopped2();
			fail();
		} catch (IllegalStateException expected) {
		}
	}

	public void testNotifyFailedWhenNotStarted() {
		DefaultService service = new DefaultService();
		try {
			service.notifyFailed2(new Exception());
			fail();
		} catch (IllegalStateException expected) {
		}
	}

	public void testNotifyFailedWhenTerminated() {
		NoOpService service = new NoOpService();
		service.startAsync().awaitRunning();
		service.stopAsync().awaitTerminated();
		try {
			service.notifyFailed2(new Exception());
			fail();
		} catch (IllegalStateException expected) {
		}
	}

	private static class DefaultService extends AbstractService {
		@Override
		protected void doStart() {
		}

		@Override
		protected void doStop() {
		}

		public void notifyStarted2() {
			super.notifyStarted();
		}

		public void notifyStopped2() {
			super.notifyStopped();
		}

		public void notifyFailed2(Throwable cause) {
			super.notifyFailed(cause);
		}
	}

	private static final Exception EXCEPTION = new Exception();

	private static void fail() throws IllegalStateException {
		throw new IllegalStateException();
	}

	private void fail(String msg) throws Exception {
		throw new Exception(msg);
	}
}