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
		System.out.println("----------------");
		// 在JVM关闭之前，保证线程服务中的任务执行完成
		MoreExecutors.addDelayedShutdownHook(service, 15, TimeUnit.SECONDS);
		
		System.out.println("-----------------");
		BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
		ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 10, 1000 * 15, TimeUnit.SECONDS, workQueue);
		
		// 返回执行器服务 
		ExecutorService executorService = MoreExecutors.getExitingExecutorService(pool);
		System.out.println(executorService);
		
		// 比其他的服务多了一些额外的锁的行为
		service = MoreExecutors.newDirectExecutorService();
		
		// 逐渐关闭给定的执行者服务，首先禁用新的提交，并在必要时取消剩余的任务。
		MoreExecutors.shutdownAndAwaitTermination(service, 15, TimeUnit.SECONDS);
		
		MoreExecutors.platformThreadFactory().newThread(() -> {
			System.out.println("cancel");
		});
	}
}
