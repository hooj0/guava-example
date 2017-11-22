package com.cnblogs.hoojo.concurrency.service._abstract;

import org.junit.Test;

import com.cnblogs.hoojo.concurrency.service._abstract.MyService.RecordingListener;

/**
 * AbstractService 用例流程测试示例
 * @author hoojo
 * @createDate 2017年11月22日 下午4:32:27
 * @file MyServiceTest.java
 * @package com.cnblogs.hoojo.concurrency.service
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class MyServiceTest2 {

	@Test
	public void test() {

		System.out.println("--------new--------");
		MyService2 service = new MyService2();
		RecordingListener.record(service);

		System.out.println(service.state()); // State.NEW
		System.out.println(service.isRunning()); // false

		System.out.println("--------startAsync--------");
		service.startAsync();
		System.out.println(service.state()); // State.STARTING
		System.out.println(service.isRunning()); // false
		
		System.out.println("--------awaitRunning--------");
		service.awaitRunning();
		System.out.println(service.state()); // State.RUNNING
		System.out.println(service.isRunning()); // true
		
		System.out.println("--------stopAsync--------");
		service.stopAsync();
		System.out.println(service.state()); // State.STOPPING
		System.out.println(service.isRunning()); // false
		
		System.out.println("--------awaitTerminated--------");
		service.awaitTerminated();
		System.out.println(service.state()); // State.TERMINATED
		System.out.println(service.isRunning()); // false
	}
	
	@Test
	public void test2() {

		System.out.println("--------new--------");
		MyService2 service = new MyService2();
		RecordingListener.record(service);

		System.out.println(service.state()); // State.NEW
		System.out.println(service.isRunning()); // false

		System.out.println("--------startAsync--------");
		service.startAsync().awaitRunning();
		System.out.println(service.state()); // State.STARTING
		System.out.println(service.isRunning()); // false
		
		System.out.println("--------startAsync--------");
		service.startAsync(); // Exception，不能重复启动
	}
	
	@Test
	public void test3() {

		System.out.println("--------new--------");
		MyService2 service = new MyService2();
		RecordingListener.record(service);

		System.out.println(service.state()); // State.NEW
		System.out.println(service.isRunning()); // false

		System.out.println("--------startAsync--------");
		service.startAsync();
		System.out.println(service.state()); // State.STARTING
		System.out.println(service.isRunning()); // false
		
	}
	
	@Test
	public void test4() {

		System.out.println("--------new--------");
		MyService2 service = new MyService2();
		RecordingListener.record(service);

		System.out.println(service.state()); // State.NEW
		System.out.println(service.isRunning()); // false

		System.out.println("--------startAsync--------");
		service.startAsync();
		System.out.println(service.state()); // State.STARTING
		System.out.println(service.isRunning()); // false
		
		System.out.println("--------awaitRunning--------");
		service.awaitRunning();
		System.out.println(service.state()); // State.RUNNING
		System.out.println(service.isRunning()); // true
		
		System.out.println("--------awaitRunning--------");
		service.awaitRunning();
		System.out.println(service.state()); // State.RUNNING
		System.out.println(service.isRunning()); // true
		
		System.out.println("--------stopAsync--------");
		service.stopAsync();
		System.out.println(service.state()); // State.STOPPING
		System.out.println(service.isRunning()); // false
		
		System.out.println("--------awaitTerminated--------");
		service.awaitTerminated();
		System.out.println(service.state()); // State.TERMINATED
		System.out.println(service.isRunning()); // false
	}
	
	@Test
	public void test5() {

		System.out.println("--------new--------");
		MyService2 service = new MyService2();
		RecordingListener.record(service);

		System.out.println(service.state()); // State.NEW
		System.out.println(service.isRunning()); // false

		System.out.println("--------startAsync--------");
		service.startAsync();
		System.out.println(service.state()); // State.STARTING
		System.out.println(service.isRunning()); // false
		
		System.out.println("--------awaitRunning--------");
		service.awaitRunning();
		System.out.println(service.state()); // State.RUNNING
		System.out.println(service.isRunning()); // true
		
		System.out.println("--------stopAsync--------");
		service.stopAsync();
		System.out.println(service.state()); // State.STOPPING
		System.out.println(service.isRunning()); // false
		
		System.out.println("--------stopAsync--------");
		service.stopAsync(); // 重复关闭，底层不做任何业务，不触发 Listener.stopping
		System.out.println(service.state()); // State.STOPPING
		System.out.println(service.isRunning()); // false
		
		System.out.println("--------awaitTerminated--------");
		service.awaitTerminated();
		System.out.println(service.state()); // State.TERMINATED
		System.out.println(service.isRunning()); // false
	}
	
	@Test
	public void test6() {
		
		System.out.println("--------new--------");
		MyService2 service = new MyService2();
		RecordingListener.record(service);
		
		System.out.println(service.state()); // State.NEW
		System.out.println(service.isRunning()); // false
		
		System.out.println("--------startAsync--------");
		service.startAsync();
		System.out.println(service.state()); // State.STARTING
		System.out.println(service.isRunning()); // false
		
		System.out.println("--------awaitRunning--------");
		service.awaitRunning();
		System.out.println(service.state()); // State.RUNNING
		System.out.println(service.isRunning()); // true
		
		System.out.println("--------stopAsync--------");
		service.stopAsync();
		System.out.println(service.state()); // State.STOPPING
		System.out.println(service.isRunning()); // false
		
		System.out.println("--------awaitTerminated--------");
		service.awaitTerminated();
		System.out.println(service.state()); // State.TERMINATED
		System.out.println(service.isRunning()); // false
	}
	
	@Test
	public void test7() {

		System.out.println("--------new--------");
		MyService2 service = new MyService2();
		RecordingListener.record(service);

		System.out.println(service.state()); // State.NEW
		System.out.println(service.isRunning()); // false

		System.out.println("--------startAsync--------");
		service.startAsync();
		System.out.println(service.state()); // State.STARTING
		System.out.println(service.isRunning()); // false
		
		System.out.println("--------awaitRunning--------");
		service.awaitRunning();
		System.out.println(service.state()); // State.RUNNING
		System.out.println(service.isRunning()); // true
		
		System.out.println("--------stopAsync--------");
		service.stopAsync();
		System.out.println(service.state()); // State.STOPPING
		System.out.println(service.isRunning()); // false
		
		System.out.println("--------awaitTerminated--------");
		service.awaitTerminated();
		System.out.println(service.state()); // State.TERMINATED
		System.out.println(service.isRunning()); // false

		System.out.println("--------awaitTerminated--------");
		service.awaitTerminated();
		System.out.println(service.state()); // State.TERMINATED
		System.out.println(service.isRunning()); // false
	}
	
	@Test
	public void testStartException() {

		String fail = "start";
		
		System.out.println("--------new--------");
		MyService service = new MyService(fail);
		RecordingListener.record(service);

		System.out.println(service.state()); // State.NEW
		System.out.println(service.isRunning()); // false

		System.out.println("--------startAsync--------");
		service.startAsync();
		// doStart fail exception
		// doStart--->state: STARTING, isRunning: false
		// Listener.starting: FAILED, isRunning: false
		
		System.out.println(service.state()); // State.FAILED
		System.out.println(service.isRunning()); // false
	}
	
	@Test
	public void testStopException() {

		String fail = "stop";
		
		System.out.println("--------new--------");
		MyService service = new MyService(fail);
		RecordingListener.record(service);

		System.out.println(service.state()); // State.NEW
		System.out.println(service.isRunning()); // false

		System.out.println("--------startAsync--------");
		service.startAsync();
		System.out.println(service.state()); // State.STARTING
		System.out.println(service.isRunning()); // false
		
		System.out.println("--------notifyStarted--------");
		service._notifyStarted();
		System.out.println(service.state()); // State.RUNNING
		System.out.println(service.isRunning()); // true
		
		System.out.println("--------awaitRunning--------");
		service.awaitRunning();
		System.out.println(service.state()); // State.RUNNING
		System.out.println(service.isRunning()); // true
		
		System.out.println("--------stopAsync--------");
		service.stopAsync();
		
		// doStop--->state: STOPPING, isRunning: false
		// Listener.stopping: FAILED, from:RUNNING, isRunning: false
		// Listener.failed: FAILED, from:STOPPING, isRunning: false
		System.out.println(service.state()); // State.FAILED
		System.out.println(service.isRunning()); // false
	}
}
