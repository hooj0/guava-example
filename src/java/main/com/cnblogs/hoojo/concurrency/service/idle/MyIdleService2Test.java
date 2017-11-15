package com.cnblogs.hoojo.concurrency.service.idle;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

import com.cnblogs.hoojo.concurrency.service.idle.MyIdleService2;
import com.google.common.util.concurrent.Service;

/**
 * MyIdeService Test
 * @author hoojo
 * @createDate 2017年11月15日 下午3:29:46
 * @file MyIdeServiceTest.java
 * @package com.cnblogs.hoojo.concurrency.test
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class MyIdleService2Test {

	@Test
	public void testIdeService() {
		// 创建
		MyIdleService2 ideService = new MyIdleService2();
		System.out.println(ideService);
		System.out.println("startUpCalled: " + ideService.startUpCalled + ", shutDownCalled: " + ideService.shutDownCalled + ", transitionStates: " + ideService.transitionStates);
		
		System.out.println(ideService.state()); // NEW
		
		// 启动，启动服务会调用 startUp 方法
		// 只有new状态的才可以启动，已停止的状态stop无法启动
		System.out.println(ideService.startAsync());
		// 由于重写了 executor() 方法，程序会在启动后调用run方法进入运行状态
		System.out.println(ideService.state()); // RUNNING
		System.out.println("startUpCalled: " + ideService.startUpCalled + ", shutDownCalled: " + ideService.shutDownCalled + ", transitionStates: " + ideService.transitionStates);
		
		// 运行，等待服务到运行running状态
		ideService.awaitRunning();
		System.out.println(ideService.state()); // RUNNING

		System.out.println("---------");
		// 在未停止服务情况，调用 awaitTerminated终止 会导致阻塞
		//ideService.awaitTerminated();
		//System.out.println(ideService.state()); 
		System.out.println("---------");

		// 停止服务，停止服务会调用 shutDown 方法
		ideService.stopAsync();
		// 由于重写了 executor() 方法，程序会在停止后调用 方法进入终止状态
		System.out.println(ideService.state()); // TERMINATED
		System.out.println("startUpCalled: " + ideService.startUpCalled + ", shutDownCalled: " + ideService.shutDownCalled + ", transitionStates: " + ideService.transitionStates);
		
		// 终止，等待服务到终止状态
		ideService.awaitTerminated();
		System.out.println(ideService.state()); // TERMINATED
		System.out.println("startUpCalled: " + ideService.startUpCalled + ", shutDownCalled: " + ideService.shutDownCalled + ", transitionStates: " + ideService.transitionStates);
	}
	
	@Test
	public void testIdeServiceStartException() {
		// 创建
		MyIdleService2 ideService = new MyIdleService2("start");
		System.out.println(ideService);
		
		System.out.println(ideService.state()); // NEW
		
		// 启动，启动服务会调用 startUp 方法
		// 只有new状态的才可以启动，已停止的状态stop无法启动
		System.out.println(ideService.startAsync());
		System.out.println(ideService.state()); // STARTING
		System.out.println("startUpCalled: " + ideService.startUpCalled + ", shutDownCalled: " + ideService.shutDownCalled + ", transitionStates: " + ideService.transitionStates);
		
		try {
			// 运行，等待服务到运行running状态
			// 程序在STARTING 时，发生异常。在awaitRunning时，等待服务到运行running不成功，故抛出异常
			ideService.awaitRunning();
		} catch (Exception e) {
			System.out.println(ideService.state()); // FAILED
		}
		System.out.println("startUpCalled: " + ideService.startUpCalled + ", shutDownCalled: " + ideService.shutDownCalled + ", transitionStates: " + ideService.transitionStates);
	}
	
	@Test
	public void testIdeServiceStopException() {
		// 创建
		MyIdleService2 ideService = new MyIdleService2("shut");
		
		System.out.println(ideService.startAsync());
		
		ideService.awaitRunning();
				
		// 停止服务，停止服务会调用 shutDown 方法
		ideService.stopAsync();
		System.out.println(ideService.state()); // STOPPING
		System.out.println("startUpCalled: " + ideService.startUpCalled + ", shutDownCalled: " + ideService.shutDownCalled + ", transitionStates: " + ideService.transitionStates);
		
		try {
			// 终止，等待服务到终止状态
			// 程序在STOPPING 时，发生异常。在awaitTerminated时，等待所有服务终止TERMINATED不成功，故抛出异常
			ideService.awaitTerminated();
		} catch (Exception e) {
			System.out.println(ideService.state()); // FAILED
		}
		System.out.println("startUpCalled: " + ideService.startUpCalled + ", shutDownCalled: " + ideService.shutDownCalled + ", transitionStates: " + ideService.transitionStates);
	}
	
	@Test
	public void testTimeout() {
		Service service = new MyIdleService2() {
			@Override
			protected Executor executor() {
				return new Executor() {
					@Override
					public void execute(Runnable command) {
					}
				};
			}
		};
		
		try {
			// 在指定时间内没有运行就超时处理
			service.startAsync().awaitRunning(1, TimeUnit.MILLISECONDS); // 设置Expected timeout时间
			// Expected timeout
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}
}
