package com.cnblogs.hoojo.concurrency.service.idle;

import org.junit.Test;

import com.cnblogs.hoojo.concurrency.service.idle.MyIdleService;

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
public class MyIdleServiceTest {

	@Test
	public void testIdeService() {
		// 创建
		MyIdleService ideService = new MyIdleService();
		System.out.println(ideService);
		
		System.out.println(ideService.state()); // NEW
		
		// 启动，启动服务会调用 startUp 方法
		// 只有new状态的才可以启动，已停止的状态stop无法启动
		System.out.println(ideService.startAsync());
		System.out.println(ideService.state()); // STARTING
		
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
		System.out.println(ideService.state()); // STOPPING
		
		// 终止，等待服务到终止状态
		ideService.awaitTerminated();
		System.out.println(ideService.state()); // TERMINATED
	}
	
	@Test
	public void testIdeServiceStartException() {
		// 创建
		MyIdleService ideService = new MyIdleService("start");
		System.out.println(ideService);
		
		System.out.println(ideService.state()); // NEW
		
		// 启动，启动服务会调用 startUp 方法
		// 只有new状态的才可以启动，已停止的状态stop无法启动
		System.out.println(ideService.startAsync());
		System.out.println(ideService.state()); // STARTING
		
		try {
			// 运行，等待服务到运行running状态
			// 程序在STARTING 时，发生异常。在awaitRunning时，等待服务到运行running不成功，故抛出异常
			ideService.awaitRunning();
		} catch (Exception e) {
			System.out.println(ideService.state()); // FAILED
		}
	}
	
	@Test
	public void testIdeServiceStopException() {
		// 创建
		MyIdleService ideService = new MyIdleService("shut");
		
		System.out.println(ideService.startAsync());
		
		ideService.awaitRunning();
				
		// 停止服务，停止服务会调用 shutDown 方法
		ideService.stopAsync();
		System.out.println(ideService.state()); // STOPPING

		try {
			// 终止，等待服务到终止状态
			// 程序在STOPPING 时，发生异常。在awaitTerminated时，等待所有服务终止TERMINATED不成功，故抛出异常
			ideService.awaitTerminated();
		} catch (Exception e) {
			System.out.println(ideService.state()); // FAILED
		}
	}
}
