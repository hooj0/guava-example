/*
 * Copyright (C) 2008 The Guava Authors
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

/*
 * Portions of this file are modified versions of
 * http://gee.cs.oswego.edu/cgi-bin/viewcvs.cgi/jsr166/src/test/tck/AbstractExecutorServiceTest.java?revision=1.30
 * which contained the following notice:
 *
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 * Other contributors include Andrew Wright, Jeffrey Hayes,
 * Pat Fisher, Mike Judd.
 */

package com.cnblogs.hoojo.concurrency;

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;
import static com.google.common.util.concurrent.MoreExecutors.listeningDecorator;
import static com.google.common.util.concurrent.MoreExecutors.newDirectExecutorService;
import static com.google.common.util.concurrent.MoreExecutors.shutdownAndAwaitTermination;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.Thread.State;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.mockito.Mockito;

import com.cnblogs.hoojo.concurrency.testing.JSR166TestCase;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.Callables;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Uninterruptibles;

/**
 * Tests for MoreExecutors.
 *
 * @author Kyle Littlefield (klittle)
 */
public class MoreExecutorsTest2 extends JSR166TestCase {

	private static final Runnable EMPTY_RUNNABLE = new Runnable() {
		@Override
		public void run() {
		}
	};
	
	private static void assertListenerRunImmediately(ListenableFuture<?> future) {
		CountingRunnable listener = new CountingRunnable();
		future.addListener(listener, directExecutor());
		assertEquals(1, listener.count);
	}

	private static final class CountingRunnable implements Runnable {
		int count;

		@Override
		public void run() {
			count++;
			System.out.println("run++");
		}
	}

	// 单个任务线程执行
	public void testDirectExecutorServiceServiceInThreadExecution() throws Exception {
		
		final ListeningExecutorService executor = newDirectExecutorService();
		// 线程变量计数，独立安全，避免多个线程操作产生脏数据
		final ThreadLocal<Integer> threadLocalCount = new ThreadLocal<Integer>() {
			@Override
			protected Integer initialValue() {
				System.out.println("init count");
				return 0;
			}
		};
		
		// 异常原子引用
		final AtomicReference<Throwable> throwableFromOtherThread = new AtomicReference<>(null);
		// 计数任务
		final Runnable incrementTask = new Runnable() {
			@Override
			public void run() {
				// 计数
				threadLocalCount.set(threadLocalCount.get() + 1);
				System.out.println("count++");
			}
		};

		Thread otherThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// 提交任务
					Future<?> future = executor.submit(incrementTask);
					System.out.println(threadLocalCount.get()); //1， 获取变量数据
					
					assertTrue(future.isDone());
					assertEquals(1, threadLocalCount.get().intValue());//1
				} catch (Throwable t) {
					throwableFromOtherThread.set(t);
				}
			}
		});

		otherThread.start();

		// 提交任务
		ListenableFuture<?> future = executor.submit(incrementTask);
		System.out.println(threadLocalCount.get());// 1
		
		assertTrue(future.isDone());
		assertListenerRunImmediately(future);
		assertEquals(1, threadLocalCount.get().intValue());
		
		otherThread.join(1000); // 等待otherThread 完成
		
		assertEquals(Thread.State.TERMINATED, otherThread.getState());
		Throwable throwable = throwableFromOtherThread.get();
		System.out.println(throwable);

		assertNull("Throwable from other thread: " + (throwable == null ? null : Throwables.getStackTraceAsString(throwable)), throwableFromOtherThread.get());
	}

	// 多任务线程执行服务
	public void testDirectExecutorServiceInvokeAll() throws Exception {
		// 创建服务
		final ExecutorService executor = newDirectExecutorService();
		// 安全计数线程变量
		final ThreadLocal<Integer> threadLocalCount = new ThreadLocal<Integer>() {
			@Override
			protected Integer initialValue() {
				return 0;
			}
		};

		// 计数任务
		final Callable<Integer> incrementTask = new Callable<Integer>() {
			@Override
			public Integer call() {
				int i = threadLocalCount.get();
				threadLocalCount.set(i + 1);
				return i;
			}
		};

		// 执行多个任务集合
		// Collections.nCopies 返回一个不可变列表组成的n个拷贝的指定对象
		List<Future<Integer>> futures = executor.invokeAll(Collections.nCopies(10, incrementTask));

		for (int i = 0; i < 10; i++) {
			Future<Integer> future = futures.get(i);
			assertTrue("Task should have been run before being returned", future.isDone());
			assertEquals(i, future.get().intValue());
		}

		assertEquals(10, threadLocalCount.get().intValue()); // 10
	}

	//
	public void testDirectExecutorServiceServiceTermination() throws Exception {
		// 创建服务
		final ExecutorService executor = newDirectExecutorService();
		// 屏障
		final CyclicBarrier barrier = new CyclicBarrier(2);
		// 原子引用
		final AtomicReference<Throwable> throwableFromOtherThread = new AtomicReference<>(null);
		
		final Runnable doNothingRunnable = new Runnable() {
			@Override
			public void run() {
			}
		};

		Thread otherThread = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("run-----start");
				try {
					Future<?> future = executor.submit(new Callable<Void>() {
						@Override
						public Void call() throws Exception {
							System.out.println("call---start");
							// WAIT #1
							System.out.println("2#" + barrier.await(1, TimeUnit.SECONDS));

							// WAIT #2
							System.out.println("3#" + barrier.await(1, TimeUnit.SECONDS));
							assertTrue(executor.isShutdown());
							assertFalse(executor.isTerminated()); // 服务未完成

							// WAIT #3
							System.out.println("4#" + barrier.await(1, TimeUnit.SECONDS));
							
							System.out.println("call---end");
							return null;
						}
					});
					
					assertTrue(future.isDone());
					assertTrue(executor.isShutdown());
					assertTrue(executor.isTerminated()); // 服务中所有任务都完成
					
					System.out.println("run-----end");
				} catch (Throwable t) {
					throwableFromOtherThread.set(t);
				}
			}
		});

		otherThread.start();

		// WAIT #1
		System.out.println("1#" + barrier.await(1, TimeUnit.SECONDS)); // 抵达屏障
		assertFalse(executor.isShutdown()); // 线程服务未关闭
		assertFalse(executor.isTerminated()); // 线程服务未完成

		System.out.println("------shutdown-------");
		// 关闭
		executor.shutdown();
		assertTrue(executor.isShutdown());
		
		try {
			// 关闭后的线程服务再加入任务会异常
			executor.submit(doNothingRunnable);
			fail("Should have encountered RejectedExecutionException");
		} catch (RejectedExecutionException ex) {
			// good to go
			System.out.println(ex);
		}
		assertFalse(executor.isTerminated()); // 线程服务内屏障未完成

		// WAIT #2
		System.out.println("5#" + barrier.await(1, TimeUnit.SECONDS));
		assertFalse(executor.awaitTermination(20, TimeUnit.MILLISECONDS));

		// WAIT #3
		System.out.println("6#" + barrier.await(1, TimeUnit.SECONDS));
		
		// 抵达所有屏障，call方法执行完毕
		assertTrue(executor.awaitTermination(1, TimeUnit.SECONDS));
		assertTrue(executor.awaitTermination(0, TimeUnit.SECONDS));
		assertTrue(executor.isShutdown());
		
		try {
			executor.submit(doNothingRunnable);
			fail("Should have encountered RejectedExecutionException");
		} catch (RejectedExecutionException ex) {
			// good to go
			System.out.println(ex);
		}
		assertTrue(executor.isTerminated());

		// 等待线程完成
		otherThread.join(1000);
		assertEquals(Thread.State.TERMINATED, otherThread.getState());
		
		Throwable throwable = throwableFromOtherThread.get();
		assertNull("Throwable from other thread: " + (throwable == null ? null : Throwables.getStackTraceAsString(throwable)), throwableFromOtherThread.get());
	}

	/**
	 * Test for a bug where threads weren't getting signaled when shutdown was called, only when tasks completed.
	 */

	public void testDirectExecutorService_awaitTermination_missedSignal() {
		final ExecutorService service = MoreExecutors.newDirectExecutorService();
		
		Thread waiter = new Thread() {
			@Override
			public void run() {
				try {
					System.out.println("1 DAYS awaitTermination");
					service.awaitTermination(1, TimeUnit.DAYS);
				} catch (InterruptedException e) {
					return;
				}
			}
		};
		waiter.start();
		
		awaitTimedWaiting(waiter);
		// 关闭服务
		service.shutdown();
		
		// 执行定时的join线程操作
		Uninterruptibles.joinUninterruptibly(waiter, 10, TimeUnit.SECONDS);
		if (waiter.isAlive()) { // 是否活着，启动并没有死亡
			waiter.interrupt(); // 线程中断
			fail("awaitTermination failed to trigger after shutdown()");
		}
	}

	/** Wait for the given thread to reach the {@link State#TIMED_WAITING} thread state. */
	void awaitTimedWaiting(Thread thread) {
		System.out.println(thread.getState());
		
		while (true) {
			switch (thread.getState()) {
				case BLOCKED:
				case NEW:
				case RUNNABLE:
				case WAITING:
					System.out.println("yield");
					Thread.yield();
					break;
				case TIMED_WAITING:
					System.out.println("break");
					return;
				case TERMINATED:
				default:
					System.out.println("error");
					throw new AssertionError();
			}
		}
	}

	// 立即关闭服务，并返回服务中的任务
	public void testDirectExecutorService_shutdownNow() {
		ExecutorService executor = newDirectExecutorService();
		
		assertEquals(ImmutableList.of(), executor.shutdownNow()); // 关闭服务
		assertTrue(executor.isShutdown()); // 是否关闭
	}

	// 关闭服务不支持添加任务
	public void testExecuteAfterShutdown() {
		ExecutorService executor = newDirectExecutorService();
		executor.shutdown();
		
		try {
			// 已关闭不支持执行任务
			executor.execute(EMPTY_RUNNABLE); // RejectedExecutionException: Executor already shutdown
			fail();
		} catch (RejectedExecutionException expected) {
			System.out.println(expected);
		}
	}

	public <T> void testListeningExecutorServiceInvokeAllJavadocCodeCompiles() throws Exception {
		ListeningExecutorService executor = newDirectExecutorService();
		
		List<Callable<T>> tasks = ImmutableList.of();
		// 执行批量任务
		List<? extends Future<?>> unused = executor.invokeAll(tasks);
		
		System.out.println(unused);
	}

	public void testListeningDecorator() throws Exception {
		
		// 创建监听服务，其调用执行会利用底层的 delegate executor 完成
		ListeningExecutorService service = listeningDecorator(newDirectExecutorService());
		assertSame(service, listeningDecorator(service));
		
		// 创建Callable 返回指定数据
		List<Callable<String>> callables = ImmutableList.of(Callables.returning("x"));
		
		List<Future<String>> results;

		// 执行所有回调任务
		results = service.invokeAll(callables);
		System.out.println(results); // TrustedListenableFutureTask

		// 定时执行
		results = service.invokeAll(callables, 1, SECONDS);
		System.out.println(results); // TrustedListenableFutureTask
	}

	public void testListeningDecorator_noWrapExecuteTask() {
		ExecutorService delegate = Mockito.mock(ExecutorService.class);
		
		ListeningExecutorService service = listeningDecorator(delegate);
		Runnable task = new Runnable() {
			@Override
			public void run() {
				System.out.println("run...");
			}
		};
		service.execute(task);
		Mockito.verify(delegate).execute(task);
	}

	public void testListeningDecorator_scheduleSuccess() throws Exception {
		final CountDownLatch completed = new CountDownLatch(1);
		
		// 调度任务执行器
		ScheduledThreadPoolExecutor delegate = new ScheduledThreadPoolExecutor(1) {
			@Override
			protected void afterExecute(Runnable r, Throwable t) {
				System.out.println("afterExecute...");
				
				completed.countDown(); //抵达
			}
		};
		
		// 监听服务
		ListeningScheduledExecutorService service = listeningDecorator(delegate);
		// 定时调度任务
		ListenableFuture<Integer> future = service.schedule(Callables.returning(42), 1, TimeUnit.MILLISECONDS);

		/*
		 * Wait not just until the Future's value is set (as in future.get()) but also until
		 * ListeningScheduledExecutorService's wrapper task is done executing listeners, as detected by yielding control
		 * to afterExecute.
		 */
		completed.await(); // 等待
		assertTrue(future.isDone());// 完成
		
		System.out.println(future.get()); // 42
		
		// 加入监听
		assertListenerRunImmediately(future);
		assertEquals(0, delegate.getQueue().size());
	}

	public void testListeningDecorator_scheduleFailure() throws Exception {
		ScheduledThreadPoolExecutor delegate = new ScheduledThreadPoolExecutor(1);
		ListeningScheduledExecutorService service = listeningDecorator(delegate);
		
		RuntimeException ex = new RuntimeException();
		
		// 调度任务异常
		ListenableFuture<?> future = service.schedule(new ThrowingRunnable(0, ex), 1, TimeUnit.MILLISECONDS);
		System.out.println(future); // future.get 将无法返回正常结果
		
		assertExecutionException(future, ex);
		assertEquals(0, delegate.getQueue().size());
	}

	public void testListeningDecorator_schedulePeriodic() throws Exception {
		ScheduledThreadPoolExecutor delegate = new ScheduledThreadPoolExecutor(1);
		ListeningScheduledExecutorService service = listeningDecorator(delegate);
		RuntimeException ex = new RuntimeException();

		ListenableFuture<?> future;

		// 调度4次后异常
		ThrowingRunnable runnable = new ThrowingRunnable(5, ex);
		future = service.scheduleAtFixedRate(runnable, 1, 1, TimeUnit.MILLISECONDS); // future.get 最终会返回异常
		
		assertExecutionException(future, ex);
		assertEquals(5, runnable.count);
		assertEquals(0, delegate.getQueue().size());
		System.out.println(delegate.getQueue());

		runnable = new ThrowingRunnable(5, ex);
		future = service.scheduleWithFixedDelay(runnable, 1, 1, TimeUnit.MILLISECONDS);// future.get 最终会返回异常
		assertExecutionException(future, ex);
		
		assertEquals(5, runnable.count);
		assertEquals(0, delegate.getQueue().size());
	}

	public void testListeningDecorator_cancelled() throws Exception {
		ScheduledThreadPoolExecutor delegate = new ScheduledThreadPoolExecutor(1);
		
		BlockingQueue<?> delegateQueue = delegate.getQueue();
		ListeningScheduledExecutorService service = listeningDecorator(delegate);
		ListenableFuture<?> future;
		ScheduledFuture<?> delegateFuture;

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				System.out.println("run...");
			}
		};

		// 调度任务
		future = service.schedule(runnable, 5, TimeUnit.MILLISECONDS);
		//Thread.sleep(1000 * 5);
		
		future.cancel(true); // 取消任务
		
		assertTrue(future.isCancelled()); // 成功取消
		
		System.out.println(delegateQueue);
		System.out.println(delegateQueue.size());
		
		delegateFuture = (ScheduledFuture<?>) delegateQueue.element();
		assertTrue(delegateFuture.isCancelled()); // 队列返回取消

		// 清空任务队列
		delegateQueue.clear();

		// 定时任务
		future = service.scheduleAtFixedRate(runnable, 5, 5, TimeUnit.MINUTES);
		future.cancel(true);
		
		assertTrue(future.isCancelled());
		delegateFuture = (ScheduledFuture<?>) delegateQueue.element();
		assertTrue(delegateFuture.isCancelled());

		delegateQueue.clear();

		// 定时延迟任务
		future = service.scheduleWithFixedDelay(runnable, 5, 5, TimeUnit.MINUTES);
		future.cancel(true);
		assertTrue(future.isCancelled());
		delegateFuture = (ScheduledFuture<?>) delegateQueue.element();
		assertTrue(delegateFuture.isCancelled());
	}

	private static final class ThrowingRunnable implements Runnable {
		final int throwAfterCount;
		final RuntimeException thrown;
		int count;

		ThrowingRunnable(int throwAfterCount, RuntimeException thrown) {
			this.throwAfterCount = throwAfterCount;
			this.thrown = thrown;
		}

		@Override
		public void run() {
			if (++count >= throwAfterCount) {
				System.out.println("throw exception");
				throw thrown;
			}
			System.out.println(count);
		}
	}

	private static void assertExecutionException(Future<?> future, Exception expectedCause) throws Exception {
		try {
			future.get(); // 由于执行调度任务异常，get将无法返回正常数据
			fail("Expected ExecutionException");
		} catch (ExecutionException e) {
			System.out.println(e); // RuntimeException
			System.out.println(e.getCause());
			assertSame(expectedCause, e.getCause());
		}
	}

	public void testPlatformThreadFactory_default() {
		// 创建新线程的默认线程工厂
		ThreadFactory factory = MoreExecutors.platformThreadFactory();
		assertNotNull(factory);
		// Executors#defaultThreadFactory() may return a new instance each time.
		assertEquals(factory.getClass(), Executors.defaultThreadFactory().getClass());
	}

	/* Half of a 1-second timeout in nanoseconds */
	private static final long HALF_SECOND_NANOS = NANOSECONDS.convert(1L, SECONDS) / 2;

	public void testShutdownAndAwaitTermination_immediateShutdown() throws Exception {
		ExecutorService service = Executors.newSingleThreadExecutor();
		
		
		/**
			该方法采取以下步骤：
				调用ExecutorService.shutdown，禁止接受新提交的任务。
				等待执行程序服务终止指定超时的一半。
				如果超时过期，则调用ExecutorService.shutdownNow，取消挂起的任务并中断正在运行的任务。
				等待指定超时的另一半的执行程序服务终止。
				如果在进程的任何一步中，调用线程被中断，则该方法将调用ExecutorService.shutdownNow并返回。
		 */
		// 逐渐关闭给定的执行者服务，首先禁用新的提交，并在必要时取消剩余的任务。
		assertTrue(shutdownAndAwaitTermination(service, 1L, SECONDS));
		
		assertTrue(service.isTerminated());
	}

	public void testShutdownAndAwaitTermination_immediateShutdownInternal() throws Exception {
		ExecutorService service = mock(ExecutorService.class);
		when(service.awaitTermination(HALF_SECOND_NANOS, NANOSECONDS)).thenReturn(true);
		when(service.isTerminated()).thenReturn(true);
		assertTrue(shutdownAndAwaitTermination(service, 1L, SECONDS));
		verify(service).shutdown();
		verify(service).awaitTermination(HALF_SECOND_NANOS, NANOSECONDS);
	}

	public void testShutdownAndAwaitTermination_forcedShutDownInternal() throws Exception {
		ExecutorService service = mock(ExecutorService.class);
		when(service.awaitTermination(HALF_SECOND_NANOS, NANOSECONDS)).thenReturn(false).thenReturn(true);
		when(service.isTerminated()).thenReturn(true);
		assertTrue(shutdownAndAwaitTermination(service, 1L, SECONDS));
		verify(service).shutdown();
		verify(service, times(2)).awaitTermination(HALF_SECOND_NANOS, NANOSECONDS);
		verify(service).shutdownNow();
	}

	public void testShutdownAndAwaitTermination_nonTerminationInternal() throws Exception {
		ExecutorService service = mock(ExecutorService.class);
		when(service.awaitTermination(HALF_SECOND_NANOS, NANOSECONDS)).thenReturn(false).thenReturn(false);
		assertFalse(shutdownAndAwaitTermination(service, 1L, SECONDS));
		verify(service).shutdown();
		verify(service, times(2)).awaitTermination(HALF_SECOND_NANOS, NANOSECONDS);
		verify(service).shutdownNow();
	}

	public void testShutdownAndAwaitTermination_interruptedInternal() throws Exception {
		final ExecutorService service = mock(ExecutorService.class);
		when(service.awaitTermination(HALF_SECOND_NANOS, NANOSECONDS)).thenThrow(new InterruptedException());

		final AtomicBoolean terminated = new AtomicBoolean();
		// we need to keep this in a flag because t.isInterrupted() returns false after t.join()
		final AtomicBoolean interrupted = new AtomicBoolean();
		// we need to use another thread because it will be interrupted and thus using
		// the current one, owned by JUnit, would make the test fail
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				terminated.set(shutdownAndAwaitTermination(service, 1L, SECONDS)); // 中断服务中的任务线程
				interrupted.set(Thread.currentThread().isInterrupted()); // 当前线程是否中断
			}
		});
		thread.start();
		thread.join();
		
		verify(service).shutdown();
		verify(service).awaitTermination(HALF_SECOND_NANOS, NANOSECONDS);
		verify(service).shutdownNow();
		assertTrue(interrupted.get());
		assertFalse(terminated.get());
	}
}
