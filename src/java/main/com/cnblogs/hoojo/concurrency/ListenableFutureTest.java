package com.cnblogs.hoojo.concurrency;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.base.Function;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 线程任务调用监听服务
	getExitingExecutorService( ThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit)：将给定的ThreadPoolExecutor转换成ExecutorService实例，在程序完成时退出， 它是通过使用守护线程和添加一个关闭钩子来等待他们完成。
	getExitingScheduledExecutorService( ScheduledThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit)：将给定的ScheduledThreadPoolExecutor转换成ScheduledExecutorService实例，在程序完成时退出， 它是通过使用守护线程和添加一个关闭钩子来等待他们完成。
	addDelayedShutdownHook( ExecutorService service, long terminationTimeout, TimeUnit timeUnit)：添加一个关闭的钩子来等待给定的ExecutorService中的线程完成。
	getExitingExecutorService(ThreadPoolExecutor executor)：将给定的ThreadPoolExecutor转换成ExecutorService实例，在程序完成时退出， 它是通过使用守护线程和添加一个关闭钩子来等待他们完成。
	getExitingScheduledExecutorService( ScheduledThreadPoolExecutor executor)：将给定的ThreadPoolExecutor转换成ScheduledExecutorService实例，在程序完成时退出， 它是通过使用守护线程和添加一个关闭钩子来等待他们完成。

	sameThreadExecutor()：创建一个ExecutorService实例，运行线程中的每一个任务。

	listeningDecorator( ExecutorService delegate)：创建一个ExecutorService实例，通过线程提交或者唤醒其他线程提交ListenableFutureTask到给定的ExecutorService实例。
	listeningDecorator( ScheduledExecutorService delegate)：创建一个ScheduledExecutorService实例，通过线程提交或者唤醒其他线程提交ListenableFutureTask到给定的ExecutorService实例。
	platformThreadFactory()：返回一个默认的线程工厂用于创建新的线程。
	shutdownAndAwaitTermination( ExecutorService service, long timeout, TimeUnit unit)：逐渐关闭指定的ExecutorService，首先会禁用新的提交， 然后会取消现有的任务。
 */
@SuppressWarnings("ALL")
public class ListenableFutureTest extends BasedTest {

	@SuppressWarnings("deprecation")
	@Test
	public void testListeningExecutorService() throws InterruptedException {
		
		// 1、create listen service
		ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(5));
		
		// 2、submit async task
		ListenableFuture<Integer> result = service.submit(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				Thread.sleep(1000 * 5);
				
				Long now = System.currentTimeMillis();
				if (now % 2 == 0) {
					return now.intValue();
				}
				
				throw new RuntimeException("exception");
			}
		});
		
		// 3、add callback
		Futures.addCallback(result, new FutureCallback<Integer>() {
			@Override
			public void onSuccess(Integer result) {
				out("success:" + result);
			}

			@Override
			public void onFailure(Throwable t) {
				out("failure:" + Throwables.getStackTraceAsString(t));
			}
		}, Executors.newCachedThreadPool());
		
		Futures.transformAsync(result, new AsyncFunction<Integer, String>() {
			private ConcurrentMap<Integer, String> map = Maps.newConcurrentMap();
			private ListeningExecutorService listeningExecutorService;
			// 这里简单的模拟一个service
			@SuppressWarnings("unchecked")
			private Map<Integer, String> service = new HashMap() {
				{
					put(1, "retrieved");
				}
			};

			@Override
			public ListenableFuture<String> apply(Integer input) throws Exception {

				if (map.containsKey(input)) {
					SettableFuture<String> listenableFuture = SettableFuture.create();
					listenableFuture.set(map.get(input));
					return listenableFuture;
				} else {
					return listeningExecutorService.submit(new Callable<String>() {
						@Override
						public String call() throws Exception {
							// service中通过input获取retrieved
							String retrieved = service.get(input);
							map.putIfAbsent(input, retrieved);
							return retrieved;
						}
					});
				}
			}
		}, Executors.newCachedThreadPool());
		
		out("finished!");
		Thread.sleep(1000 * 7);
	}
	
	@Test
	public void testListeningExecutorService2() throws InterruptedException {
		
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		ListeningExecutorService service = MoreExecutors.listeningDecorator(executorService);
		
		ListenableFuture<Integer> result = service.submit(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				Thread.sleep(1000 * 5);
				
				Long now = System.currentTimeMillis();
				if (now % 2 == 0) {
					return now.intValue();
				}
				
				throw new RuntimeException("exception");
			}
		});
		
		Futures.addCallback(result, new FutureCallback<Integer>() {
			@Override
			public void onSuccess(Integer result) {
				out("success:" + result);
			}

			@Override
			public void onFailure(Throwable t) {
				out("failure:" + Throwables.getStackTraceAsString(t));
			}
		}, MoreExecutors.directExecutor());
		
		// 捕获线程内部异常
		Futures.catching(result, RuntimeException.class, new Function<Exception, Integer>() {
			@Override
			public Integer apply(Exception input) {
				out("catch:" + input.getMessage());
				return -1;
			}
			
		}, executorService);
		
		// 捕获异常并异步处理新的返回值
		Futures.catchingAsync(result, RuntimeException.class, new AsyncFunction<Exception, Integer>() {
			@Override
			public ListenableFuture<Integer> apply(Exception input) throws Exception {
				out("AsyncFunction->" + input.getMessage());
				
				return service.submit(new Callable<Integer>() {
					@Override
					public Integer call() throws Exception {
						return -10;
					}
				});
			}
		}, executorService);
		
		out("finished!");
		Thread.sleep(1000 * 7);
	}
	
	@Test
	public void testCustomCallback() throws InterruptedException {
		
		ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(5));

		ListenableFuture<Integer> result = service.submit(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				Thread.sleep(1000 * 5);
				
				Long now = System.currentTimeMillis();
				if (now % 2 == 0) {
					return now.intValue();
				}
				
				throw new RuntimeException("exception");
			}
		});
		
		result.addListener(new Runnable() {
			@Override
			public void run() {
				out("exec...");
			}
		}, MoreExecutors.directExecutor());
		
		Futures.addCallback(result, new FutureCallback<Integer>() {
			@Override
			public void onSuccess(Integer result) {
				out("success:" + result);
			}

			@Override
			public void onFailure(Throwable t) {
				out("failure:" + Throwables.getStackTraceAsString(t));
			}
		}, MoreExecutors.directExecutor());
		
		out("finished!");
		Thread.sleep(1000 * 7);
	}
	
	@Test
	public void testCustomCallback2() throws InterruptedException {
		
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		Future<Integer> futrue = executorService.submit(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				Thread.sleep(1000 * 5);
				
				Long now = System.currentTimeMillis();
				if (now % 2 == 0) {
					return now.intValue();
				}
				
				throw new RuntimeException("exception");
			}
		});
		
		ListenableFuture<Integer> result = JdkFutureAdapters.listenInPoolThread(futrue);
		Futures.addCallback(result, new FutureCallback<Integer>() {
			@Override
			public void onSuccess(Integer result) {
				out("success:" + result);
			}

			@Override
			public void onFailure(Throwable t) {
				out("failure:" + Throwables.getStackTraceAsString(t));
			}
		}, executorService);
		
		out(result.isCancelled());
		out(result.isDone());
		
		out("finished!");
		Thread.sleep(1000 * 7);
	}
	
	@Test
	public void test1() throws InterruptedException {
		
		Long now = System.currentTimeMillis();

		ExecutorService executorService = Executors.newFixedThreadPool(5);
		Future<Integer> futrue = executorService.submit(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				out("call...");
				//Thread.sleep(1000 * 5);
				
				return now.intValue();
			}
		});
		
		ListenableFuture<Integer> result = JdkFutureAdapters.listenInPoolThread(futrue);
		
		ListenableFutureTester tester = new ListenableFutureTester(result);
		tester.setUp();
		result.cancel(true);
		
		try {
			//tester.testCancelledFuture(); // CancellationException
			
			tester.tearDown();
			tester.testCompletedFuture(now.intValue());
			//tester.testFailedFuture("fail");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		out(result.isCancelled());
		out(result.isDone());
		
		out("finished!");
		
		Thread.sleep(1000 * 7);
	}
}
