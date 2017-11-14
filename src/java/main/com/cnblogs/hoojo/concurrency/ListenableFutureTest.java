package com.cnblogs.hoojo.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.JdkFutureAdapters;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * <b>function:</b> 并发编程
 * @author hoojo
 * @createDate 2017年11月13日 上午10:37:32
 * @file ListenableFutureTest.java
 * @package com.cnblogs.hoojo.concurrency
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ListenableFutureTest {

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
				System.out.println("success:" + result);
			}

			@Override
			public void onFailure(Throwable t) {
				System.out.println("failure:" + Throwables.getStackTraceAsString(t));
			}
		});
		
		System.out.println("finished!");
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
				System.out.println("success:" + result);
			}

			@Override
			public void onFailure(Throwable t) {
				System.out.println("failure:" + Throwables.getStackTraceAsString(t));
			}
		}, MoreExecutors.directExecutor());
		
		// 捕获线程内部异常
		Futures.catching(result, RuntimeException.class, new Function<Exception, Integer>() {
			@Override
			public Integer apply(Exception input) {
				System.out.println("catch:" + input.getMessage());
				return -1;
			}
			
		}, executorService);
		
		// 捕获异常并异步处理新的返回值
		Futures.catchingAsync(result, RuntimeException.class, new AsyncFunction<Exception, Integer>() {
			@Override
			public ListenableFuture<Integer> apply(Exception input) throws Exception {
				System.out.println("AsyncFunction->" + input.getMessage());
				
				return service.submit(new Callable<Integer>() {
					@Override
					public Integer call() throws Exception {
						return -10;
					}
				});
			}
		}, executorService);
		
		System.out.println("finished!");
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
				System.out.println("exec...");
			}
		}, MoreExecutors.directExecutor());
		
		Futures.addCallback(result, new FutureCallback<Integer>() {
			@Override
			public void onSuccess(Integer result) {
				System.out.println("success:" + result);
			}

			@Override
			public void onFailure(Throwable t) {
				System.out.println("failure:" + Throwables.getStackTraceAsString(t));
			}
		}, MoreExecutors.directExecutor());
		
		System.out.println("finished!");
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
				System.out.println("success:" + result);
			}

			@Override
			public void onFailure(Throwable t) {
				System.out.println("failure:" + Throwables.getStackTraceAsString(t));
			}
		}, executorService);
		
		System.out.println("finished!");
		Thread.sleep(1000 * 7);
	}
}
