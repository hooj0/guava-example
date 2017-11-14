package com.cnblogs.hoojo.concurrency;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.util.concurrent.MoreExecutors;

/**
 * <b>function:</b>
 * @author hoojo
 * @createDate 2017年11月13日 下午3:32:43
 * @file MoreExecutorsTest.java
 * @package com.cnblogs.hoojo.concurrency
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class MoreExecutorsTest {

	@Test
	public void test1() {
		// 返回一个执行器
		Executor exec = MoreExecutors.directExecutor();
		exec.execute(() -> {
			System.out.println("run...");
		});
		
		ExecutorService service = Executors.newFixedThreadPool(3);
		service.submit(() -> {
			System.out.println("exec...");
		});
		// 在JVM关闭之前，保证线程服务中的任务执行完成
		MoreExecutors.addDelayedShutdownHook(service, 15, TimeUnit.SECONDS);
		
		BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
		ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 10, 1000 * 15, TimeUnit.SECONDS, workQueue);
		
		// 返回执行器服务 
		ExecutorService executorService = MoreExecutors.getExitingExecutorService(pool);
		System.out.println(executorService);
		
		// 比其他的服务多了一些额外的锁的行为
		MoreExecutors.newDirectExecutorService();
		
		// 逐渐关闭给定的执行者服务，首先禁用新的提交，并在必要时取消剩余的任务。
		MoreExecutors.shutdownAndAwaitTermination(service, 15, TimeUnit.SECONDS);
		
		MoreExecutors.platformThreadFactory().newThread(() -> {
			
		});
	}
}
