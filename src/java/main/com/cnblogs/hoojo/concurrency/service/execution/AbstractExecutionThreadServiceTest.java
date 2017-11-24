package com.cnblogs.hoojo.concurrency.service.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

import com.cnblogs.hoojo.concurrency.testing.TearDown;
import com.cnblogs.hoojo.concurrency.testing.TearDownStack;
import com.cnblogs.hoojo.concurrency.testing.TestingExecutors;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;

/**
 * AbstractExecutionThreadService 异步线程任务执行器示例
 * @author hoojo
 * @createDate 2017年11月24日 下午4:14:04
 * @file AbstractExecutionThreadServiceTest.java
 * @package com.cnblogs.hoojo.concurrency.service.execution
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class AbstractExecutionThreadServiceTest {

	private final TearDownStack tearDownStack = new TearDownStack(true);
	private final CountDownLatch enterRun = new CountDownLatch(1);
	private final CountDownLatch exitRun = new CountDownLatch(1);

	private Thread executionThread;
	private Throwable thrownByExecutionThread;
	
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
	
	private final Executor exceptionCatchingExecutor = new Executor() {
		@Override
		public void execute(Runnable command) {
			executionThread = new Thread(command);
			executionThread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
				@Override
				public void uncaughtException(Thread thread, Throwable e) {
					thrownByExecutionThread = e;
					System.out.println("uncaughtException: " + thread + ", Throwable: " + e);
				}
			});
			executionThread.start();
		}
	};

	protected final void tearDown() {
		tearDownStack.runTearDown();
		assertNull("exceptions should not be propagated to uncaught exception handlers", thrownByExecutionThread);
	}
	
	private class WaitOnRunService extends AbstractExecutionThreadService {
		private boolean startUpCalled = false;
		private boolean runCalled = false;
		private boolean shutDownCalled = false;
		
		private State expectedShutdownState = State.STOPPING;

		@Override
		protected void startUp() {
			System.out.println("---------startUp--------");
			
			System.out.println("startUpCalled: " + startUpCalled); // false
			System.out.println("runCalled: " + runCalled); // false
			System.out.println("shutDownCalled: " + shutDownCalled); // false
			System.out.println("state: " + state()); // State.STARTING

			startUpCalled = true;
		}

		@Override
		protected void run() {
			System.out.println("---------run--------start");
			
			System.out.println("startUpCalled: " + startUpCalled); // true
			System.out.println("runCalled: " + runCalled); // false
			System.out.println("shutDownCalled: " + shutDownCalled); // false
			System.out.println("state: " + state()); // State.RUNNING
			
			runCalled = true;

			System.out.println("-----run-enterRun.countDown------");
			enterRun.countDown();
			try {
				System.out.println("-----run-exitRun.await------");
				exitRun.await();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			
			System.out.println("---------run---------finish");
		}

		@Override
		protected void shutDown() {
			System.out.println("---------shutDown--------");
			
			shutDownCalled = true;

			System.out.println("startUpCalled: " + startUpCalled); // true
			System.out.println("runCalled: " + runCalled); // true
			System.out.println("shutDownCalled: " + shutDownCalled); // true
			System.out.println("state: " + state()); // State.RUNNING expectedShutdownState
			
			assertEquals(expectedShutdownState, state());
		}

		@Override
		protected void triggerShutdown() {
			System.out.println("---------triggerShutdown--------");
			
			System.out.println("-----triggerShutdown-exitRun.countDown------");
			exitRun.countDown(); // 导致triggerShutdown方法在run结束前运行完成
		}

		@Override
		protected Executor executor() {
			System.out.println("---------executor--------");
			
			return exceptionCatchingExecutor;
		}
	}

	@Test
	public void testServiceStartStop() throws Exception {
		WaitOnRunService service = new WaitOnRunService();
		RecordingListener.record(service);
		
		assertFalse(service.startUpCalled);

		// 1、startAsync() -> doStart() & dispatch Listener.starting()
		// 2、doStart() -> startUp() & notifyStart() & AbstractExecutionThreadService.this.run & shutDown() & notifyStop()
		// 3、awaitRunning() -> dispatch Listener.running()
		service.startAsync().awaitRunning();
		assertTrue(service.startUpCalled);
		assertEquals(Service.State.RUNNING, service.state());

		// 等待run函数运行完毕
		System.out.println("-------test-enterRun.await------");
		enterRun.await(); // to avoid stopping the service until run() is invoked

		// 1、stopAsync() -> doStop() & dispatch Lisener.stopping()
		// 2、doStop() -> triggerShutdown()
		service.stopAsync().awaitTerminated();
		assertTrue(service.shutDownCalled);
		assertEquals(Service.State.TERMINATED, service.state());
		executionThread.join(); // 截获所有异常
	}

	@Test
	public void testServiceStopIdempotence() throws Exception {
		WaitOnRunService service = new WaitOnRunService();

		service.startAsync().awaitRunning();
		// 在stopAsync 之前 ，run方法被执行完成
		System.out.println("-------test-enterRun.await------");
		enterRun.await(); // to avoid stopping the service until run() is invoked

		// stopAsync 只执行一次，triggerShutdown 也就触发运行一次
		service.stopAsync(); // 触发triggerShutdown()
		service.stopAsync();
		service.stopAsync().awaitTerminated(); // 触发 Listener.
		assertEquals(Service.State.TERMINATED, service.state());
		service.stopAsync().awaitTerminated();
		assertEquals(Service.State.TERMINATED, service.state());

		executionThread.join();
	}

	@Test
	public void testServiceExitingOnItsOwn() throws Exception {
		WaitOnRunService service = new WaitOnRunService();
		RecordingListener.record(service);
		
		service.expectedShutdownState = Service.State.RUNNING;

		service.startAsync().awaitRunning();
		assertTrue(service.startUpCalled);
		assertEquals(Service.State.RUNNING, service.state());

		// run()运行结束，导致 triggerShutdown 没有在run之前完成
		System.out.println("-------test-exitRun.countDown------");
		exitRun.countDown(); // the service will exit voluntarily
		executionThread.join();

		assertTrue(service.shutDownCalled);
		assertEquals(Service.State.TERMINATED, service.state());

		service.stopAsync().awaitTerminated(); // no-op
		assertEquals(Service.State.TERMINATED, service.state());
		assertTrue(service.shutDownCalled);
	}
	
	@Test
	public void testDefaultService() throws InterruptedException {
		WaitOnRunService service = new WaitOnRunService();
		service.startAsync().awaitRunning();
		enterRun.await();
		service.stopAsync().awaitTerminated();
	}
	
	private class ThrowOnStartUpService extends AbstractExecutionThreadService {
		private boolean startUpCalled = false;

		@Override
		protected void startUp() {
			startUpCalled = true;
			
			System.out.println("------startUp-----");
			throw new UnsupportedOperationException("kaboom!");
		}

		@Override
		protected void run() {
			System.out.println("------run-----");
			throw new AssertionError("run() should not be called");
		}

		@Override
		protected Executor executor() {
			System.out.println("------executor-----");
			return exceptionCatchingExecutor;
		}
	}

	@Test
	public void testServiceThrowOnStartUp() throws Exception {
		ThrowOnStartUpService service = new ThrowOnStartUpService();
		RecordingListener.record(service);
		
		assertFalse(service.startUpCalled);

		// startAsync() -> doStart() -> startUp() -> exception -> shutDown() -> notifyFailed()
		service.startAsync();
		System.out.println(service.state());
		try {
			service.awaitRunning(); // 由于 notifyFailed() 没有执行 notifyStart() , 所以这里执行异常
			fail();
		} catch (IllegalStateException expected) {
			System.out.println(expected.getCause()); // UnsupportedOperationException: kaboom!
		}
		executionThread.join();

		assertTrue(service.startUpCalled);
		assertEquals(Service.State.FAILED, service.state());
		System.out.println(service.failureCause());
	}

	private class ThrowOnRunService extends AbstractExecutionThreadService {
		private boolean shutDownCalled = false;
		private boolean throwOnShutDown = false;

		@Override
		protected void run() {
			System.out.println("-----run----");
			throw new UnsupportedOperationException("kaboom!");
		}

		@Override
		protected void shutDown() {
			System.out.println("-----shutDown----");
			
			shutDownCalled = true;
			if (throwOnShutDown) {
				System.out.println("-----throwOnShutDown----");
				throw new UnsupportedOperationException("double kaboom!");
			}
		}

		@Override
		protected Executor executor() {
			System.out.println("-----executor----");
			
			return exceptionCatchingExecutor;
		}
	}
	
	@Test
	public void testServiceThrowOnRun() throws Exception {
		ThrowOnRunService service = new ThrowOnRunService();
		RecordingListener.record(service);

		// startAsync() -> doStart() -> run() -> exception -> shutDown() -> notifyFailed()
		service.startAsync();
		try {
			service.awaitTerminated(); // UnsupportedOperationException: kaboom!
			fail();
		} catch (IllegalStateException expected) {
			executionThread.join();
			assertEquals(service.failureCause(), expected.getCause());
			System.out.println(expected.getCause());
		}
		assertTrue(service.shutDownCalled);
		assertEquals(Service.State.FAILED, service.state());
	}

	@Test
	public void testServiceThrowOnRunAndThenAgainOnShutDown() throws Exception {
		ThrowOnRunService service = new ThrowOnRunService();
		RecordingListener.record(service);
		
		service.throwOnShutDown = true;

		/*
		 * try {
            AbstractExecutionThreadService.this.run();
          } catch (Throwable t) {
            try {
              shutDown();
            } catch (Exception ignored) {
              logger.log(
                  Level.WARNING,
                  "Error while attempting to shut down the service after failure.",
                  ignored);
            }
            notifyFailed(t);
            return;
          }
		 */
		// startAsync() -> doStart() -> run() -> exception -> shutDown() -> exception -> notifyFailed()
		service.startAsync(); // run 异常会触发 shutDown 方法， shutDown方法异常会打印异常信息
		try {
			service.awaitTerminated();
			fail();
		} catch (IllegalStateException expected) {
			executionThread.join();
			assertEquals(service.failureCause(), expected.getCause());
			System.out.println(expected.getCause());
		}

		assertTrue(service.shutDownCalled);
		assertEquals(Service.State.FAILED, service.state());
	}
	
	private class ThrowOnShutDown extends AbstractExecutionThreadService {
		
		@Override
		protected void run() {
			System.out.println("--------run-------");
			try {
				System.out.println("-----run-enterRun.await------");
				enterRun.await();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		protected void shutDown() {
			System.out.println("------shutDown------");
			throw new UnsupportedOperationException("kaboom!");
		}

		@Override
		protected Executor executor() {
			System.out.println("------executor------");
			return exceptionCatchingExecutor;
		}
	}

	@Test
	public void testServiceThrowOnShutDown() throws Exception {
		ThrowOnShutDown service = new ThrowOnShutDown();
		RecordingListener.record(service);

		// startAsync() -> doStart() -> run() -> exception -> shutDown() -> exception -> notifyFailed()
		service.startAsync().awaitRunning();
		assertEquals(Service.State.RUNNING, service.state());

		System.out.println("----stopAsync----");
		service.stopAsync();
		
		enterRun.countDown();
		executionThread.join();

		assertEquals(Service.State.FAILED, service.state()); // 触发notifyFailed后，状态为 FAILED
		System.out.println(service.failureCause()); // UnsupportedOperationException: kaboom!
	}

	private class TimeoutOnStartUp extends AbstractExecutionThreadService {
		@Override
		protected Executor executor() {
			return new Executor() {
				@Override
				public void execute(Runnable command) {
					System.out.println("executor.Runnable......." + command);
					
					/*
					不运行任务导致超时
					Thread t = new Thread(command);
					t.start();
					**/
				}
			};
		}

		@Override
		protected void run() throws Exception {
			System.out.println("run..............");
		}
	}
	
	@Test
	public void testServiceTimeoutOnStartUp() throws Exception {
		TimeoutOnStartUp service = new TimeoutOnStartUp();
		
		try {
			// 设置 1 秒超时
			// 因为 executor 方法没有执行异步线程
			service.startAsync().awaitRunning(1, TimeUnit.MILLISECONDS);
			//fail();
		} catch (TimeoutException e) {
			System.out.println(e.getMessage());//Timed out waiting for TimeoutOnStartUp [STARTING] to reach the RUNNING state.
		}
	}

	private class FakeService extends AbstractExecutionThreadService implements TearDown {

		private final ExecutorService executor = Executors.newSingleThreadExecutor();

		FakeService() {
			tearDownStack.addTearDown(this);
		}

		volatile int startupCalled = 0;
		volatile int shutdownCalled = 0;
		volatile int runCalled = 0;

		@Override
		protected void startUp() throws Exception {
			System.out.println("startUp……");
			
			assertEquals(0, startupCalled);
			assertEquals(0, runCalled);
			assertEquals(0, shutdownCalled);
			startupCalled++;
			
			System.out.println("startUp…end…");
		}

		@Override
		protected void run() throws Exception {
			System.out.println("run……");
			
			assertEquals(1, startupCalled);
			assertEquals(0, runCalled);
			assertEquals(0, shutdownCalled);
			runCalled++;
			
			System.out.println("run…end…");
		}

		@Override
		protected void shutDown() throws Exception {
			System.out.println("shutDown……");
			
			assertEquals(1, startupCalled);
			assertEquals(0, shutdownCalled);
			assertEquals(Service.State.STOPPING, state());
			shutdownCalled++;
			
			System.out.println("shutDown…end…");
		}

		@Override
		protected Executor executor() {
			System.out.println("executor……");
			
			return executor;
		}

		@Override
		public void tearDown() throws Exception {
			System.out.println("tearDown……");
			
			executor.shutdown();
		}
	}
	
	@Test
	public void testStopWhileStarting_runNotCalled() throws Exception {
		final CountDownLatch started = new CountDownLatch(1);
		FakeService service = new FakeService() {
			@Override
			protected void startUp() throws Exception {
				System.out.println("@Override.startUp.....");
				
				super.startUp();
				started.await(); // 线程进入等待
				
				System.out.println("@Override.startUp.....end");
			}
		};
		
		RecordingListener.record(service);
		
		// startAsync() -> doStart() -> startUp() & if (isRunning()) { run() } & shutDown() & notifyFailed()
		// 由于 Override.startUp 方法进入等待，导致程序无法进入running状态
		// 程序没有进入 running状态，导致run方法不被运行
		System.out.println("--------startAsync--------");
		service.startAsync();
		
		System.out.println("--------stopAsync--------");
		service.stopAsync();
		
		System.out.println("--------countDown--------");
		started.countDown(); // Override.startUp() 执行完成，但程序已被转入STOPPING状态
		
		System.out.println("--------awaitTerminated--------");
		service.awaitTerminated(); // 程序运行完成
		
		assertEquals(Service.State.TERMINATED, service.state());
		assertEquals(1, service.startupCalled);
		assertEquals(0, service.runCalled);
		assertEquals(1, service.shutdownCalled);
	}

	@Test
	public void testStop_noStart() {
		
		FakeService service = new FakeService();
		System.out.println(service.state());
		
		// NEW 状态调用stop 直接完成
		service.stopAsync().awaitTerminated();
		System.out.println(service.state());
		
		assertEquals(Service.State.TERMINATED, service.state());
		assertEquals(0, service.startupCalled);
		assertEquals(0, service.runCalled);
		assertEquals(0, service.shutdownCalled);
	}

	@Test
	public void testTimeout() {
		// Create a service whose executor will never run its commands
		Service service = new AbstractExecutionThreadService() {
			@Override
			protected void run() throws Exception {
			}

			@Override
			protected ScheduledExecutorService executor() {
				return TestingExecutors.noOpScheduledExecutor();
			}

			@Override
			protected String serviceName() {
				return "Foo";
			}
		};
		
		RecordingListener.record(service);
		try {
			System.out.println("---------startAsync--------");
			// 因为 executor 方法没有执行异步线程; TestingExecutors.NoOpScheduledExecutorService.execute()
			service.startAsync().awaitRunning(1, TimeUnit.MILLISECONDS);
			fail("Expected timeout"); // no executed
		} catch (Exception e) {
			System.out.println(e); //TimeoutException: Timed out waiting for Foo [STARTING] to reach the RUNNING state.
		}
	}

	private static void fail() throws IllegalStateException {
		throw new IllegalStateException("fail IllegalStateException");
	}
	
	private static void fail(String msg) throws Exception {
		throw new Exception(msg);
	}
}
