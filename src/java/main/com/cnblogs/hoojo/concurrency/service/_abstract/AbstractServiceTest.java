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
import com.google.common.util.concurrent.AbstractService;
import com.google.common.util.concurrent.Atomics;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;

/**
继承AbstractService方法必须实现两个方法.
    doStart():  首次调用startAsync()时会同时调用doStart(),
    			doStart()内部需要处理所有的初始化工作、如果启动成功则调用notifyStarted()方法；启动失败则调用notifyFailed()
    doStop():  首次调用stopAsync()会同时调用doStop(),
    			doStop()要做的事情就是停止服务，如果停止成功则调用 notifyStopped()方法；停止失败则调用 notifyFailed()方法。
    			
	doStart和doStop方法的实现需要考虑下性能，尽可能的低延迟。
	如果初始化的开销较大，如读文件，打开网络连接，或者其他任何可能引起阻塞的操作，建议移到另外一个单独的线程去处理。

 一个服务正常生命周期有：
    Service.State.NEW
    Service.State.STARTING
    Service.State.RUNNING
    Service.State.STOPPING
    Service.State.TERMINATED
    
服务一旦被停止就无法再重新启动了。
	如果服务在starting、running、stopping状态出现问题、会进入Service.State.FAILED.状态。
	调用 startAsync()方法可以异步开启一个服务,同时返回this对象形成方法调用链。
	注意：只有在当前服务的状态是NEW时才能调用startAsync()方法，因此最好在应用中有一个统一的地方初始化相关服务。
	停止一个服务也是类似的、使用异步方法stopAsync() 。
	但是不像startAsync(),多次调用这个方法是安全的。这是为了方便处理关闭服务时候的锁竞争问题。

Service也提供了一些方法用于等待服务状态转换的完成:
	通过 addListener()方法异步添加监听器。此方法允许你添加一个 Service.Listener 、它会在每次服务状态转换的时候被调用。
	注意：最好在服务启动之前添加Listener（这时的状态是NEW）、否则之前已发生的状态转换事件是无法在新添加的Listener上被重新触发的。

同步使用awaitRunning()。这个方法不能被打断、不强制捕获异常、一旦服务启动就会返回。如果服务没有成功启动，会抛出IllegalStateException异常。
同样的， awaitTerminated() 方法会等待服务达到终止状态（TERMINATED 或者 FAILED）。两个方法都有重载方法允许传入超时时间。
*/
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
					System.out.println("stopping.awaitRunning:" + expected);
					
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
					System.out.println("terminated.awaitRunning:" + expected);
					
					assertNull(expected.getCause());
					assertTrue(expected.getMessage().equals("Expected the service " + service + " to be RUNNING, but was TERMINATED"));
				}
			}
			completionLatch.countDown(); // 结束
		}

		@Override
		public synchronized void failed(State from, Throwable failure) {
			System.out.println("Listener.failed: " + service.state() + ", from:" + from + ", isRunning: " + service.isRunning() + ", error: " + failure);
			
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
					System.out.println("failed.awaitRunning: " + e);
					
					assertEquals(failure, e.getCause());
				}
			}
			try {
				System.out.println("--------failed.awaitTerminated--------");
				service.awaitTerminated();
				fail();
			} catch (IllegalStateException e) {
				System.out.println("failed.awaitTerminated: " + e);
				
				assertEquals(failure, e.getCause());
			}
			
			completionLatch.countDown(); // 结束
		}
	}
	
	/**
	 * 自动模式，在startAsync()后调用doStart(), 在doStart()中调用notifyStarted()方法启动线程运行，触发Listener.running() 运行方法awaitRunning()
	 * 在stopAsync()运行后调用doStop(), 在doStop()方法中调用notifyStopped()方法
	 */
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
		// 运行 startAsync 立即运行 doStart，doStart中运行notifyStarted，触发enqueueRunningEvent，状态以被置为 RUNNING
		service.startAsync();
		System.out.println(service.state()); // State.RUNNING
		System.out.println(service.isRunning()); // true
		System.out.println(service.running); // true

		System.out.println("--------stopAsync--------");
		// 运行 stopAsync 立即运行 doStop，doStart中运行notifyStarted，触发enqueueTerminatedEvent，状态以被置为 TERMINATED
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
		service.startAsync().awaitRunning(); // 此处的awaitRunning是在doStart方法之后调用，属于重复调用
		assertEquals(State.RUNNING, service.state());
		System.out.println(service.state()); // State.RUNNING
		System.out.println(service.isRunning()); // true
		System.out.println(service.running); // true
		
		System.out.println("--------stopAsync.awaitTerminated--------");
		service.stopAsync().awaitTerminated(); // 此处的awaitTerminated是在doStop方法之后调用，属于重复调用
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
		service.stopAsync();	// stopAsync被重复调用，底层不执行业务
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
		service.stopAsync(); // stopAsync被重复调用，底层不执行业务
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
		service.stopAsync().awaitTerminated(); // stopAsync被重复调用，底层不执行业务
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
	
	@Test
	public void testStopUnstartedService() throws Exception {
		NoOpService service = new NoOpService();
		RecordingListener listener = RecordingListener.record(service);

		service.stopAsync(); // statue=NEW，直接触发enqueueTerminatedEvent -> Listener.terminated
		assertEquals(State.TERMINATED, service.state());

		try {
			// 已停止的服务不能被重启运行
			service.startAsync(); // Service NoOpService [TERMINATED] has already been started
			fail();
		} catch (IllegalStateException expected) {
			System.out.println(expected);
		}
		assertEquals(State.TERMINATED, Iterables.getOnlyElement(listener.getStateHistory()));
	}
	
	@Test
	public void testNotifyFailedWhenTerminated() {
		NoOpService service = new NoOpService();
		RecordingListener.record(service);
		
		System.out.println("-------startAsync.awaitRunning-------");
		service.startAsync().awaitRunning();
		System.out.println(service.state()); // State.STARTING
		System.out.println(service.isRunning()); // false

		System.out.println("-------stopAsync.awaitTerminated-------");
		service.stopAsync().awaitTerminated();
		System.out.println(service.state()); // State.STOPPING
		System.out.println(service.isRunning()); // false
		// 至此，整个流程顺利完成
		
		System.out.println("-------notifyFailed2-------");
		try {
			// notifyFailed() -> state = TERMINATED -> IllegalStateException: Failed while in state:TERMINATED
			service.notifyFailed2(new Exception("my exception")); // 由于状态 TERMINATED 不触发Listener
			fail();
		} catch (IllegalStateException expected) {
			// failureCause() is only valid if the service has failed, service is TERMINATED
			//System.out.println(service.failureCause()); // 程序流程顺利完成没有发生异常情况下，再调用该方法会抛出异常
			
			System.out.println(expected);//java.lang.IllegalStateException: Failed while in state:TERMINATED
			System.out.println(expected.getCause());
		}
		
		System.out.println(service.state()); // State.TERMINATED
		System.out.println(service.isRunning()); // false
	}
	
	/**
	 * 手动模式，需要自调用notifyStarted、notifyStopped，分别在startAsync和stopAsync执行后
	 * 
	 * The user of this service should call {@link #notifyStarted} and {@link #notifyStopped} after calling
	 * {@link #startAsync} and {@link #stopAsync}.
	 */
	private static class ManualSwitchedService extends AbstractService {
		boolean doStartCalled = false;
		boolean doStopCalled = false;

		@Override
		protected void doStart() {
			System.out.println("doStart……");
			
			assertFalse(doStartCalled);
			doStartCalled = true;
		}

		@Override
		protected void doStop() {
			System.out.println("doStop……");
			
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

	@Test
	public void testManualServiceStartStop() throws Exception {
		ManualSwitchedService service = new ManualSwitchedService();
		RecordingListener listener = RecordingListener.record(service);

		System.out.println("-----------startAsync----------");
		service.startAsync(); // 1、invoke doStart() & 2、dispatch Listener.starting
		assertEquals(State.STARTING, service.state());
		assertFalse(service.isRunning());
		assertTrue(service.doStartCalled);

		System.out.println("-----------notifyStarted2----------");
		// dispatch Listener.running
		service.notifyStarted2(); // usually this would be invoked by another thread
		assertEquals(State.RUNNING, service.state());
		assertTrue(service.isRunning());

		System.out.println("-----------stopAsync----------");
		service.stopAsync(); // 1、invoke doStop() & 2、dispatch Listener.stopping
		assertEquals(State.STOPPING, service.state());
		assertFalse(service.isRunning());
		assertTrue(service.doStopCalled);

		System.out.println("-----------notifyStopped2----------");
		// dispatch Listener.terminated
		service.notifyStopped2(); // usually this would be invoked by another thread
		assertEquals(State.TERMINATED, service.state());
		assertFalse(service.isRunning());
		assertEquals(ImmutableList.of(State.STARTING, State.RUNNING, State.STOPPING, State.TERMINATED), listener.getStateHistory());

	}

	@Test
	public void testManualServiceNotifyStoppedWhileRunning() throws Exception {
		ManualSwitchedService service = new ManualSwitchedService();
		RecordingListener listener = RecordingListener.record(service);

		System.out.println("-----------startAsync----------");
		service.startAsync(); // 1、invoke doStart() & 2、dispatch Listener.starting
		System.out.println(service.state()); // STARTING
		
		System.out.println("-----------notifyStarted2----------");
		service.notifyStarted2(); // dispatch event Listener.running
		System.out.println(service.state()); // RUNNING
		
		// 程序没有调用stopAsync 直接跳过STOPPING进入TERMINATED
		System.out.println("-----------notifyStopped2----------");
		service.notifyStopped2(); // dispatch event Listener.terminated
		System.out.println(service.state()); // TERMINATED
		
		assertEquals(State.TERMINATED, service.state());
		assertFalse(service.isRunning());
		assertFalse(service.doStopCalled);

		assertEquals(ImmutableList.of(State.STARTING, State.RUNNING, State.TERMINATED), listener.getStateHistory());
	}

	@Test
	public void testManualServiceStopWhileStarting() throws Exception {
		ManualSwitchedService service = new ManualSwitchedService();
		RecordingListener listener = RecordingListener.record(service);

		System.out.println("-----------startAsync----------");
		// 回调doStart(), 触发Listener.stopping
		service.startAsync(); // STARTING
		assertEquals(State.STARTING, service.state());
		assertFalse(service.isRunning());
		assertTrue(service.doStartCalled);

		System.out.println("-----------stopAsync----------");
		// 触发Listener.stopping
		service.stopAsync(); // STOPPING
		assertEquals(State.STOPPING, service.state());
		assertFalse(service.isRunning());
		assertFalse(service.doStopCalled);

		System.out.println("-----------notifyStarted2----------");
		// 回调doStop()，因为shutdownWhenStartupFinishes = false
		service.notifyStarted2(); //State.STOPPING 
		assertEquals(State.STOPPING, service.state());
		assertFalse(service.isRunning());
		assertTrue(service.doStopCalled);

		System.out.println("-----------notifyStopped2----------");
		// 触发 Listener.terminated
		service.notifyStopped2();
		assertEquals(State.TERMINATED, service.state());
		assertFalse(service.isRunning());
		
		assertEquals(ImmutableList.of(State.STARTING, State.STOPPING, State.TERMINATED), listener.getStateHistory());
	}

	/**
	 * This tests for a bug where if {@link Service#stopAsync()} was called while the service was {@link State#STARTING}
	 * more than once, the {@link Listener#stopping(State)} callback would get called multiple times.
	 */
	@Test
	public void testManualServiceStopMultipleTimesWhileStarting() throws Exception {
		ManualSwitchedService service = new ManualSwitchedService();
		final AtomicInteger stopppingCount = new AtomicInteger();
		service.addListener(new Listener() {
			@Override
			public void stopping(State from) {
				System.out.println("stopping……");
				stopppingCount.incrementAndGet();
			}
		}, MoreExecutors.directExecutor());

		service.startAsync();
		System.out.println("state:" + service.state() + ", run:" + service.isRunning());//state:STARTING, run:false
		
		// 在调用stopAsync的时候，前置状态state=STARTING；底层执行enqueueStoppingEvent事件，触发Listener.stopping
		service.stopAsync();
		System.out.println("state:" + service.state() + ", run:" + service.isRunning());//state:STOPPING, run:false
		assertEquals(1, stopppingCount.get());
		
		// 再次调用stopAsync的时候，前置状态state=state:STOPPING，底层不执行任何业务，故不触发任何事件。
		// 最终stopppingCount=1
		service.stopAsync();
		System.out.println("state:" + service.state() + ", run:" + service.isRunning());//state:STOPPING, run:false
		assertEquals(1, stopppingCount.get());
	}

	@Test
	public void testManualServiceStopWhileNew() throws Exception {
		ManualSwitchedService service = new ManualSwitchedService();
		RecordingListener listener = RecordingListener.record(service);

		System.out.println("-------stopAsync-------");
		// 执行stopAsync方法，前置状态为NEW。底层直接触发enqueueTerminatedEvent，调用Listener.terminated
		service.stopAsync();
		System.out.println("state:" + service.state() + ", run:" + service.isRunning());//state:TERMINATED, run:false
		
		assertEquals(State.TERMINATED, service.state());
		assertFalse(service.isRunning());
		assertFalse(service.doStartCalled);
		assertFalse(service.doStopCalled);
		assertEquals(ImmutableList.of(State.TERMINATED), listener.getStateHistory());
	}

	@Test
	public void testManualServiceFailWhileStarting() throws Exception {
		ManualSwitchedService service = new ManualSwitchedService();
		RecordingListener listener = RecordingListener.record(service);
		
		System.out.println("--------startAsync-------");
		// doStart & Listener.starting
		service.startAsync();
		
		System.out.println("--------notifyFailed2-------");
		// Listener.failed
		service.notifyFailed2(EXCEPTION);
		
		assertEquals(ImmutableList.of(State.STARTING, State.FAILED), listener.getStateHistory());
	}

	@Test
	public void testManualServiceFailWhileRunning() throws Exception {
		ManualSwitchedService service = new ManualSwitchedService();
		RecordingListener listener = RecordingListener.record(service);
		
		// doStart() & Listener.starting()
		service.startAsync(); // STARTING, isRunning: false
		
		// Listener.running
		service.notifyStarted2(); // state: RUNNING, isRunning: true
		
		// Listener.failed
		service.notifyFailed2(EXCEPTION); // FAILED
		assertEquals(ImmutableList.of(State.STARTING, State.RUNNING, State.FAILED), listener.getStateHistory());
	}

	@Test
	public void testManualServiceFailWhileStopping() throws Exception {
		ManualSwitchedService service = new ManualSwitchedService();
		RecordingListener listener = RecordingListener.record(service);
		
		// doStart() & Listener.starting
		service.startAsync();
		
		// Listener.running
		service.notifyStarted2();
		
		// doStop() & Listener.stopping
		service.stopAsync();
		
		// Listener.failed
		service.notifyFailed2(EXCEPTION);
		assertEquals(ImmutableList.of(State.STARTING, State.RUNNING, State.STOPPING, State.FAILED), listener.getStateHistory());
	}

	@Test
	public void testManualServiceUnrequestedStop() {
		ManualSwitchedService service = new ManualSwitchedService();

		// doStart()
		service.startAsync();
		System.out.println("state:" + service.state() + ", run:" + service.isRunning());//state:STARTING, run:false

		service.notifyStarted2();
		System.out.println("state:" + service.state() + ", run:" + service.isRunning());//state:RUNNING, run:true
		
		assertEquals(State.RUNNING, service.state());
		assertTrue(service.isRunning());
		assertFalse(service.doStopCalled);

		// previous != STOPPING && previous != RUNNING -> false ---------dispatch terminated/TERMINATED
		service.notifyStopped2();//state:TERMINATED, run:false
		System.out.println("state:" + service.state() + ", run:" + service.isRunning());
		
		assertEquals(State.TERMINATED, service.state());
		assertFalse(service.isRunning());
		assertFalse(service.doStopCalled);
	}

	@Test
	public void testAwaitTerminated_FailedService() throws Exception {
		final ManualSwitchedService service = new ManualSwitchedService();
		final AtomicReference<Throwable> exception = Atomics.newReference();
		
		Thread waiter = new Thread() {
			@Override
			public void run() {
				try {
					System.out.println("awaitTerminated...");
					
					// 运行awaitTerminated到这里被阻塞
					service.awaitTerminated(); // Expected the service ManualSwitchedService [FAILED] to be TERMINATED, but the service has FAILED
					
					// 下面的无法执行到，正常情况下在notifyStopped会执行到下面的代码块
					System.out.println("exception...");
					fail("Expected an IllegalStateException");
				} catch (Throwable t) {
					exception.set(t);
					System.out.println("catch: " + t.getMessage());
				}
			}
		};
		waiter.start();
		
		System.out.println("---------startAsync---------");
		// doStart()
		service.startAsync();
		System.out.println("state:" + service.state() + ", run:" + service.isRunning());//state:STARTING, run:false
		
		System.out.println("---------notifyStarted2---------");
		service.notifyStarted2();
		System.out.println("state:" + service.state() + ", run:" + service.isRunning());//state:RUNNING, run:true
		assertEquals(State.RUNNING, service.state());
		
		System.out.println("---------notifyFailed2---------");
		// fail Exception，导致上面waiter线程中的awaitTerminated阻塞停止
		service.notifyFailed2(EXCEPTION);
		System.out.println("state:" + service.state() + ", run:" + service.isRunning());//state:FAILED, run:false
		assertEquals(State.FAILED, service.state());
		
		// 正常流程，会运行waiter中的fail Exception
		//service.notifyStopped2();
		waiter.join(LONG_TIMEOUT_MILLIS);
		assertFalse(waiter.isAlive());
		
		//assertEquals(exception.get(), IllegalStateException.class);
		assertEquals(EXCEPTION, exception.get().getCause());
	}
	
	@Test
	public void testManualServiceFailureIdempotence() {
		ManualSwitchedService service = new ManualSwitchedService();
		/*
		 * Set up a RecordingListener to perform its built-in assertions, even though we won't look at its state
		 * history.
		 */
		RecordingListener.record(service);
		
		System.out.println("---------startAsync--------");
		service.startAsync();
		System.out.println("state:" + service.state() + ", run:" + service.isRunning()); //state:STARTING, run:false
		
		System.out.println("---------notifyFailed2--------");
		// 前置状态 state=STARTING，触发Listener.failed
		service.notifyFailed2(new Exception("1"));
		System.out.println("state:" + service.state() + ", run:" + service.isRunning()); //state:FAILED, run:false
		
		System.out.println("---------notifyFailed2--------");
		// 前置状态 state=state:FAILED 不做任何处理
		service.notifyFailed2(new Exception("2"));
		System.out.println("state:" + service.state() + ", run:" + service.isRunning()); //state:FAILED, run:false
		
		// 由于service.notifyFailed2(new Exception("2")) 没有被触发和处理，所以下面的Exception=1
		System.out.println(service.failureCause()); // java.lang.Exception: 1
		// assertThat(service.failureCause()).hasMessage("1");
		try {
			service.awaitRunning(); // failure Exception("1")
			// 由于上面的Exception，此时这里的不被运行
			System.out.println("un exec");
			fail();
		} catch (Exception e) {
			// assertThat(e.getCause()).hasMessage("1");
			System.out.println(e.getCause());
		}
	}

	private static void throwIfSet(Throwable t) throws Throwable {
		if (t != null) {
			throw t;
		}
	}
	
	/**
	 * 捕获线程执行异常，让线程资源顺利释放 
	 */
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
	
	private class ThreadedService extends AbstractService {
		final CountDownLatch hasConfirmedIsRunning = new CountDownLatch(1);

		/**
		 * The main test thread tries to stop() the service shortly after confirming that it is running. Meanwhile, the
		 * service itself is trying to confirm that it is running. If the main thread's stop() call happens before it
		 * has the chance, the test will fail. To avoid this, the main thread calls this method, which waits until the
		 * service has performed its own "running" check.
		 * 
		 * 主测试线程在确认服务正在运行后立即尝试stop()服务。 
		 * 同时，服务本身正试图确认它正在运行。 如果主线程的stop()调用在发生之前发生，则测试将失败。 
		 * 为了避免这种情况，主线程调用这个方法，等待服务执行自己的“运行”检查。
		 */
		void awaitRunChecks() throws InterruptedException {
			System.out.println("---------awaitRunChecks----------");
			assertTrue("Service thread hasn't finished its checks. " + "Exception status (possibly stale): " + thrownByExecutionThread, hasConfirmedIsRunning.await(1000, SECONDS));
		}

		@Override
		protected void doStart() {
			System.out.println("doStart........");
			
			assertEquals(State.STARTING, state());
			invokeOnExecutionThreadForTest(new Runnable() {
				@Override
				public void run() {
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					
					assertEquals(State.STARTING, state());
					System.out.println("----notifyStarted------start");
					notifyStarted();
					System.out.println("----notifyStarted------end");
					assertEquals(State.RUNNING, state());
					
					hasConfirmedIsRunning.countDown();
				}
			});
		}

		@Override
		protected void doStop() {
			System.out.println("doStop........");
			
			assertEquals(State.STOPPING, state());
			invokeOnExecutionThreadForTest(new Runnable() {
				@Override
				public void run() {
					assertEquals(State.STOPPING, state());
					System.out.println("----notifyStopped------start");
					notifyStopped();
					System.out.println("----notifyStopped------end");
					assertEquals(State.TERMINATED, state());
				}
			});
		}
	}

	@Test
	public void testThreadedServiceStartAndWaitStopAndWait() throws Throwable {
		ThreadedService service = new ThreadedService();
		RecordingListener listener = RecordingListener.record(service);
		
		service.startAsync().awaitRunning();
		assertEquals(State.RUNNING, service.state());

		/**
		 * 主测试线程在确认服务正在运行后立即尝试stop()服务。 
		 * 同时，服务本身正试图确认它正在运行。 如果主线程的stop()调用在发生之前发生，则测试将失败。 
		 * 为了避免这种情况，主线程调用这个方法，等待服务执行自己的“运行”检查。
		 */
		service.awaitRunChecks();

		service.stopAsync().awaitTerminated();
		assertEquals(State.TERMINATED, service.state());

		throwIfSet(thrownByExecutionThread);
		assertEquals(ImmutableList.of(State.STARTING, State.RUNNING, State.STOPPING, State.TERMINATED), listener.getStateHistory());
	}

	@Test
	public void testThreadedServiceStopIdempotence() throws Throwable {
		ThreadedService service = new ThreadedService();

		service.startAsync().awaitRunning();
		assertEquals(State.RUNNING, service.state());

		service.awaitRunChecks();

		service.stopAsync();
		// stopAsync 只会被底层“有效的”执行一次
		service.stopAsync().awaitTerminated();
		assertEquals(State.TERMINATED, service.state());

		throwIfSet(thrownByExecutionThread);
	}

	@Test
	public void testThreadedServiceStopIdempotenceAfterWait() throws Throwable {
		ThreadedService service = new ThreadedService();

		service.startAsync().awaitRunning();
		assertEquals(State.RUNNING, service.state());

		service.awaitRunChecks();

		service.stopAsync().awaitTerminated();
		// stopAsync 只会被底层“有效的”执行一次
		service.stopAsync();
		assertEquals(State.TERMINATED, service.state());

		executionThread.join();

		throwIfSet(thrownByExecutionThread);
	}

	@Test
	public void testThreadedServiceStopIdempotenceDoubleWait() throws Throwable {
		ThreadedService service = new ThreadedService();

		service.startAsync().awaitRunning();
		assertEquals(State.RUNNING, service.state());

		service.awaitRunChecks();

		// stopAsync 只会被底层“有效的”执行一次
		service.stopAsync().awaitTerminated();
		service.stopAsync().awaitTerminated();
		assertEquals(State.TERMINATED, service.state());

		throwIfSet(thrownByExecutionThread);
	}

	private static class StartFailingService extends AbstractService {
		@Override
		protected void doStart() {
			System.out.println("doStart().......");
			
			System.out.println("------notifyFailed------");
			notifyFailed(EXCEPTION);
		}

		@Override
		protected void doStop() {
			System.out.println("doStop().......");
			
			System.out.println("------fail------");
			fail();
		}
	}
	
	@Test
	public void testFailingServiceStartAndWait() throws Exception {
		StartFailingService service = new StartFailingService();
		RecordingListener listener = RecordingListener.record(service);

		try {
			// 1、invoke doStart() & dispatch Listener.starting
			// 2、doStart() -> notifyFailed "java.lang.Exception: my exception" -> state=FAILED
			// 3、notifyFailed() -> dispatch Listener.failed()
			// 程序触发 failed后直接结束完成
			service.startAsync().awaitRunning(); 
			fail(); // 此处不被执行，上面的代码异常
		} catch (IllegalStateException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			System.out.println(service.failureCause());
			
			assertEquals(EXCEPTION, service.failureCause());
			assertEquals(EXCEPTION, e.getCause());
		}
		assertEquals(ImmutableList.of(State.STARTING, State.FAILED), listener.getStateHistory());
	}
	
	@Test
	public void testAddListenerAfterFailureDoesntCauseDeadlock() throws InterruptedException {
		final StartFailingService service = new StartFailingService();
		RecordingListener.record(service);
		
		// 1、invoke doStart() & dispatch Listener.starting()
		// 2、doStart() -> notifyFailed "java.lang.Exception: my exception" -> state=FAILED
		// 3、notifyFailed() -> dispatch Listener.failed()
		service.startAsync();
		assertEquals(State.FAILED, service.state());
		
		//service.addListener(new RecordingListener(service), MoreExecutors.directExecutor());
		Thread thread = new Thread() {
			@Override
			public void run() {
				// Internally stopAsync() grabs a lock, this could be any such method on AbstractService.
				System.out.println("---run---start");
				service.stopAsync();
				System.out.println("---run---end");
			}
		};
		thread.start();
		// 等待 线程运行完成
		thread.join(LONG_TIMEOUT_MILLIS);
		
		System.out.println(thread.isAlive());
		assertFalse(thread + " is deadlocked", thread.isAlive());
	}

	private static class StopFailingService extends AbstractService {
		@Override
		protected void doStart() {
			System.out.println("doStart..........");
			
			System.out.println("notifyStarted..........start");
			notifyStarted();
			System.out.println("notifyStarted..........end");
		}

		@Override
		protected void doStop() {
			System.out.println("doStop..........");

			System.out.println("notifyFailed..........start");
			notifyFailed(EXCEPTION);
			System.out.println("notifyFailed..........end");
		}
	}
	
	@Test
	public void testFailingServiceStopAndWait_stopFailing() throws Exception {
		StopFailingService service = new StopFailingService();
		RecordingListener listener = RecordingListener.record(service);

		// startAsync: invoke doStart() -> invoke notifyStarted() & dispatch Listener.starting()
		// awaitRunning: dispatch Listener.running()
		service.startAsync().awaitRunning(); 
		try {
			// stopAsync: invoke doStop() -> invoke notifyFailed() -> dispatch Listener.failed() & dispatch Listener.stopping()
			service.stopAsync().awaitTerminated(); // awaitTerminated() 遇到异常
			System.out.println("---fail---");
			fail(); // no execute
		} catch (IllegalStateException e) {
			System.out.println(e.getCause()); // java.lang.Exception: my exception
			
			assertEquals(EXCEPTION, service.failureCause());
			assertEquals(EXCEPTION, e.getCause());
		}
		
		assertEquals(ImmutableList.of(State.STARTING, State.RUNNING, State.STOPPING, State.FAILED), listener.getStateHistory());
	}
	
	@Test
	public void testFailureCause_throwsIfNotFailed() {
		StopFailingService service = new StopFailingService();
		
		try {
			// failureCause 仅在服务失败failed情况时，有效
			System.out.println("1.failureCause: " + service.failureCause());
			fail();
		} catch (IllegalStateException expected) {
			System.out.println("1.expected: " + expected);
		}
		
		service.startAsync().awaitRunning();
		
		try {
			System.out.println("2.failureCause: " + service.failureCause());
			fail();
		} catch (IllegalStateException expected) {
			System.out.println("2.expected: " + expected);
		}
		
		try {
			// 1、invoke doStop() & dispatch Listener.stopping()
			// 2、doStop() -> notifyFailed() "java.lang.Exception: my exception"
			// 3、notifyFailed() -> dispatch Listener.failed()
			service.stopAsync().awaitTerminated();
			
			System.out.println("--fail--");
			fail(); // 
		} catch (IllegalStateException e) {
			System.out.println("3.expected: " + e); // java.lang.Exception: my exception
			System.out.println("3.expected: " + e.getCause());
			System.out.println("3.expected: " + service.failureCause());
			
			assertEquals(EXCEPTION, service.failureCause());
			assertEquals(EXCEPTION, e.getCause());
		}
	}

	private static class RunFailingService extends AbstractService {
		@Override
		protected void doStart() {
			System.out.println("--doStart--");
			
			System.out.println("--notifyStarted--");
			notifyStarted();
			
			System.out.println("--notifyFailed--");
			notifyFailed(EXCEPTION);
		}

		@Override
		protected void doStop() {
			System.out.println("--doStop--");
			
			System.out.println("--doStop.fail--");
			fail();
		}
	}
	
	@Test
	public void testFailingServiceStopAndWait_runFailing() throws Exception {
		RunFailingService service = new RunFailingService();
		RecordingListener listener = RecordingListener.record(service);

		// 1、doStart() -> invoke notifyStarted() & notifyFailed() && dispatch Listener.starting()
		// 2、notifyStarted() -> dispatch Listener.running() 
		// 3、notifyFailed() -> dispatch Listener.failed() 
		service.startAsync();
		try {
			service.awaitRunning(); // java.lang.Exception: my exception
			fail(); // no execute
		} catch (IllegalStateException e) {
			System.out.println("exception: " + e); // java.lang.Exception: my exception
			System.out.println("exception: " + e.getCause());
			
			assertEquals(EXCEPTION, service.failureCause());
			assertEquals(EXCEPTION, e.getCause());
		}
		assertEquals(ImmutableList.of(State.STARTING, State.RUNNING, State.FAILED), listener.getStateHistory());
	}

	private static class StartThrowingService extends AbstractService {

		final RuntimeException exception = new RuntimeException("deliberate");

		@Override
		protected void doStart() {
			System.out.println("doStart........");
			throw exception;
		}

		@Override
		protected void doStop() {
			System.out.println("doStop........");
		
			System.out.println("doStop.fail........");
			fail();
		}
	}
	
	@Test
	public void testThrowingServiceStartAndWait() throws Exception {
		StartThrowingService service = new StartThrowingService();
		RecordingListener listener = RecordingListener.record(service);

		try {
			// 0、startAsync() -> doStart() & dispatch Listener.starting()
			// 1、doStart() -> throw Exception -> notifyFailed()
			// 2、notifyFailed() -> dispatch Listener.failed()  
			service.startAsync().awaitRunning();
			
			System.out.println("---fail---");
			fail("un execute");
		} catch (IllegalStateException e) {
			System.out.println("exception: " + e); // java.lang.RuntimeException: deliberate
			System.out.println("exception: " + e.getCause());
			
			assertEquals(service.exception, service.failureCause());
			assertEquals(service.exception, e.getCause());
		}
		assertEquals(ImmutableList.of(State.STARTING, State.FAILED), listener.getStateHistory());
	}

	private static class StopThrowingService extends AbstractService {

		final RuntimeException exception = new RuntimeException("deliberate");

		@Override
		protected void doStart() {
			notifyStarted();
		}

		@Override
		protected void doStop() {
			System.out.println("doStop....");
			throw exception;
		}
	}
	
	@Test
	public void testThrowingServiceStopAndWait_stopThrowing() throws Exception {
		StopThrowingService service = new StopThrowingService();
		RecordingListener listener = RecordingListener.record(service);

		service.startAsync().awaitRunning();
		
		try {
			// 1、stopAsync() -> invoke doStop() & dispatch Listener.stopping()
			// 2、doStop() -> throw exception -> notifyFailed()
			// 3、notifyFailed() -> dispatch Listener.failed()
			service.stopAsync().awaitTerminated(); // java.lang.RuntimeException: deliberate
			
			fail("no execute...");
		} catch (IllegalStateException e) {
			System.out.println("exception: " + e); // java.lang.RuntimeException: deliberate
			System.out.println("exception: " + e.getCause());
			
			assertEquals(service.exception, service.failureCause());
			assertEquals(service.exception, e.getCause());
		}
		assertEquals(ImmutableList.of(State.STARTING, State.RUNNING, State.STOPPING, State.FAILED), listener.getStateHistory());
	}

	private static class RunThrowingService extends AbstractService {

		final RuntimeException exception = new RuntimeException("deliberate");

		@Override
		protected void doStart() {
			System.out.println("doStart.........");

			System.out.println("notifyStarted.........");
			notifyStarted();
			throw exception;
		}

		@Override
		protected void doStop() {
			System.out.println("doStop.........");

			System.out.println("doStop.fail.........");
			fail();
		}
	}
	
	@Test
	public void testThrowingServiceStopAndWait_runThrowing() throws Exception {
		RunThrowingService service = new RunThrowingService();
		RecordingListener listener = RecordingListener.record(service);

		System.out.println("---startAsync---");
		// 0、startAsync() -> doStart() & dispatch Listener.starting()
		// 1、doStart() -> notifyStarted() & throw Exception -> notifyFailed()
		// 2、notifyStarted() -> dispatch Listener.running()  
		// 3、notifyFailed() -> dispatch Listener.failed()  
		service.startAsync();
		try {
			System.out.println("---awaitTerminated---");
			service.awaitTerminated(); // java.lang.RuntimeException: deliberate
			fail("no executed");
		} catch (IllegalStateException e) {
			System.out.println("exception: " + e); // java.lang.RuntimeException: deliberate
			System.out.println("exception: " + e.getCause());
			
			assertEquals(service.exception, service.failureCause());
			assertEquals(service.exception, e.getCause());
		}
		assertEquals(ImmutableList.of(State.STARTING, State.RUNNING, State.FAILED), listener.getStateHistory());
	}
	
	private static class DefaultService extends AbstractService {
		@Override
		protected void doStart() {
			System.out.println("doStart..........");
		}

		@Override
		protected void doStop() {
			System.out.println("doStop..........");
		}

		public void notifyStarted2() {
			System.out.println("notifyStarted2..........");
			super.notifyStarted();
		}

		public void notifyStopped2() {
			System.out.println("notifyStopped2..........");
			super.notifyStopped();
		}

		public void notifyFailed2(Throwable cause) {
			System.out.println("notifyFailed2..........");
			super.notifyFailed(cause);
		}
	}

	@Test
	public void testNotifyStartedWhenNotStarting() {
		DefaultService service = new DefaultService();
		try {
			// 1、notifyStarted() -> state != STARTING -> notifyFailed() 
			// 2、notifyFailed() -> state = NEW -> IllegalStateException
			service.notifyStarted2(); // java.lang.IllegalStateException: Failed while in state:NEW
			fail();
		} catch (IllegalStateException expected) {
			System.out.println(expected);
			System.out.println(expected.getCause());
		}
	}

	@Test
	public void testNotifyStoppedWhenNotRunning() {
		DefaultService service = new DefaultService();
		try {
			
			// 1、notifyStopped() -> previous != STOPPING && previous != RUNNING -> notifyFailed() 
			// 2、notifyFailed() -> state = NEW -> IllegalStateException
			service.notifyStopped2();
			
			fail();
		} catch (IllegalStateException expected) {
			System.out.println(expected);
			System.out.println(expected.getCause());
		}
	}

	@Test
	public void testNotifyFailedWhenNotStarted() {
		DefaultService service = new DefaultService();
		try {
			// notifyFailed() -> state = NEW -> IllegalStateException
			service.notifyFailed2(new Exception());
			fail();
		} catch (IllegalStateException expected) {
			System.out.println(expected);
			System.out.println(expected.getCause());
		}
	}

	private static final Exception EXCEPTION = new Exception("my exception");

	private static void fail() throws IllegalStateException {
		throw new IllegalStateException("fail IllegalStateException");
	}

	private void fail(String msg) throws Exception {
		throw new Exception(msg);
	}
}