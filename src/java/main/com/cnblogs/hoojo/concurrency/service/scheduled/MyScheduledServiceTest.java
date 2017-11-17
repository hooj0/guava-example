package com.cnblogs.hoojo.concurrency.service.scheduled;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * <b>function:</b> 定时任务线程测试
 * 
 * @author hoojo
 * @createDate 2017年11月15日 下午4:38:53
 * @file MyScheduledServiceTest.java
 * @package com.cnblogs.hoojo.concurrency.test
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class MyScheduledServiceTest {

	@Test
	public void testFailOnExceptionFromRun() throws InterruptedException, BrokenBarrierException {
		MyScheduledService service = new MyScheduledService();
		service.runException = new Exception("run exception");
		
		service.startAsync().awaitRunning();
		System.out.println("startUpCalled: " + service.startUpCalled + ", shutDownCalled: " + service.shutDownCalled + ", States: " + service.state());
		
		System.out.print("executor: " + service.numberOfTimesExecutorCalled.intValue());
		System.out.print(", run: " + service.numberOfTimesRunCalled.intValue());
		System.out.println(", scheduler: " + service.numberOfTimesSchedulerCalled.intValue());
		
		// 保证2个线程都抵达屏障
		service.runFirstBarrier.await();
		service.runSecondBarrier.await();
		
		try {
			System.out.println("get:" + service.future.get());
		} catch (Exception e) {
			System.out.println(service.runException);
		}
		
		System.out.println("state: " + service.state());
		
		service.stopAsync().awaitTerminated();
		System.out.println("state: " + service.state());
	}
	
	@Test
	public void testFailOnExceptionFromStart() throws InterruptedException, BrokenBarrierException {
		MyScheduledService service = new MyScheduledService();
		service.startUpException = new Exception("start exception");
		
		// startUpCalled: false, shutDownCalled: false, States: STARTING
		// 在start的时候触发异常
		try {
			service.startAsync();
			service.awaitRunning();
		} catch (Exception e) {
			System.out.println(service.startUpException);
		}
		System.out.println("startUpCalled: " + service.startUpCalled + ", shutDownCalled: " + service.shutDownCalled + ", States: " + service.state());
		
		System.out.print("executor: " + service.numberOfTimesExecutorCalled.intValue());
		System.out.print(", run: " + service.numberOfTimesRunCalled.intValue());
		System.out.println(", scheduler: " + service.numberOfTimesSchedulerCalled.intValue());
		
		System.out.println("state: " + service.state());
	}
	
	@Test
	public void testFailOnExceptionFromShutDown() throws Exception {
		MyScheduledService service = new MyScheduledService();
		service.shutDownException = new Exception("start exception");
		
		service.startAsync().awaitRunning();
		System.out.println("startUpCalled: " + service.startUpCalled + ", shutDownCalled: " + service.shutDownCalled + ", States: " + service.state());
		
		service.runFirstBarrier.await();
		service.stopAsync();
		service.runSecondBarrier.await();
		
		System.out.println("startUpCalled: " + service.startUpCalled + ", shutDownCalled: " + service.shutDownCalled + ", States: " + service.state());
		
		// startUpCalled: true, shutDownCalled: false, States: STOPPING
		// STOP后异常
		try {
			service.awaitTerminated();
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("startUpCalled: " + service.startUpCalled + ", shutDownCalled: " + service.shutDownCalled + ", States: " + service.state());
	}
	
	@Test
	public void testRunOneIterationCalledMultipleTimes() throws Exception {
		MyScheduledService service = new MyScheduledService();
		service.startAsync().awaitRunning();
		System.out.println("startUpCalled: " + service.startUpCalled + ", shutDownCalled: " + service.shutDownCalled + ", States: " + service.state());
		
		// 循环中的屏障会导致多次运行 runOneIteration
		for (int i = 1; i < 10; i++) {
			service.runFirstBarrier.await();
			System.out.println(service.numberOfTimesRunCalled.get());
			service.runSecondBarrier.await();
		}
		
		service.runFirstBarrier.await();
		service.stopAsync();
		System.out.println("startUpCalled: " + service.startUpCalled + ", shutDownCalled: " + service.shutDownCalled + ", States: " + service.state());
		service.runSecondBarrier.await();
		service.stopAsync().awaitTerminated();
		System.out.println("startUpCalled: " + service.startUpCalled + ", shutDownCalled: " + service.shutDownCalled + ", States: " + service.state());
	}
	
	@Test
	public void testExecutorOnlyCalledOnce() throws Exception {
		MyScheduledService service = new MyScheduledService();
		service.startAsync().awaitRunning();
		System.out.println("startUpCalled: " + service.startUpCalled + ", shutDownCalled: " + service.shutDownCalled + ", States: " + service.state());
		
		// It should be called once during startup.
		System.out.println(service.numberOfTimesExecutorCalled.get());
		
		for (int i = 1; i < 10; i++) {
			service.runFirstBarrier.await();
			System.out.println(service.numberOfTimesRunCalled.get());
			service.runSecondBarrier.await();
		}
		service.runFirstBarrier.await();
		service.stopAsync();
		service.runSecondBarrier.await();
		
		service.stopAsync().awaitTerminated();
		System.out.println("startUpCalled: " + service.startUpCalled + ", shutDownCalled: " + service.shutDownCalled + ", States: " + service.state());
		// Only called once overall.
		System.out.println(service.numberOfTimesExecutorCalled.get());
	}
	
	@Test
	public void testDefaultExecutorIsShutdownWhenServiceIsStopped() throws Exception {
		MyScheduledService2 service = new MyScheduledService2();

		service.startAsync();
		System.out.println(service.executor().isShutdown());
		
		service.awaitRunning();
		service.stopAsync();
		
		// 不进行等待情况下输出
		/*
			false
			start...
			scheduler...
			runOneIteration...
			isShutdown:false
			terminated...
			awaitTermination:true
		 */
		//service.terminationLatch.await();
		
		// 等待所有线程完成
		/*
			false
			start...
			scheduler...
			runOneIteration...
			terminated...
			isShutdown:true
			awaitTermination:true
		 */
		
		System.out.println("isShutdown:" + service.executor().isShutdown());
		System.out.println("awaitTermination:" + service.executor().awaitTermination(100, TimeUnit.MILLISECONDS));
	}
	
	@Test
	public void testDefaultExecutorIsShutdownWhenServiceFails() throws Exception {
		MyScheduledService2 service = new MyScheduledService2();
		service.startException = new Exception("startUp Failed");
		
		try {
			service.startAsync().awaitRunning(); // Expected service to fail during startup
		} catch (IllegalStateException expected) {
			System.out.println(expected.getMessage());
		}
		
		// 在出现异常情况下，线程等待或不等待效果一样
		/*
		 	start...
			Expected the service MyScheduledService2 [FAILED] to be RUNNING, but the service has FAILED
			failed...startUp Failed
			isShutdown: true
			awaitTermination: true
		 */
		service.terminationLatch.await();
		
		System.out.println("isShutdown: " + service.executor().isShutdown());
		System.out.println("awaitTermination: " + service.executor().awaitTermination(100, TimeUnit.MILLISECONDS));
	}
	
	@Test
	public void testSchedulerOnlyCalledOnce() throws Exception {
		MyScheduledService service = new MyScheduledService();
		service.startAsync().awaitRunning();
		
		// It should be called once during startup.
		System.out.println(service.numberOfTimesSchedulerCalled.get());
		
		for (int i = 1; i < 10; i++) {
			service.runFirstBarrier.await();
			System.out.println(service.numberOfTimesRunCalled.get());
			service.runSecondBarrier.await();
		}
		
		service.runFirstBarrier.await();
		service.stopAsync();
		service.runSecondBarrier.await();
		service.awaitTerminated();
		
		// Only called once overall.
		System.out.println(service.numberOfTimesSchedulerCalled.get());
	}
}
