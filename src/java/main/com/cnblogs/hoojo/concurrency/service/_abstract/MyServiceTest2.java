package com.cnblogs.hoojo.concurrency.service._abstract;

import com.cnblogs.hoojo.BasedTest;
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
@SuppressWarnings("ALL")
public class MyServiceTest2 extends BasedTest {

	@Test
	public void test() {

		out("--------new--------");
		MyService2 service = new MyService2();
		RecordingListener.record(service);

		out(service.state()); // State.NEW
		out(service.isRunning()); // false

		out("--------startAsync--------");
		service.startAsync();
		out(service.state()); // State.STARTING
		out(service.isRunning()); // false
		
		out("--------awaitRunning--------");
		service.awaitRunning();
		out(service.state()); // State.RUNNING
		out(service.isRunning()); // true
		
		out("--------stopAsync--------");
		service.stopAsync();
		out(service.state()); // State.STOPPING
		out(service.isRunning()); // false
		
		out("--------awaitTerminated--------");
		service.awaitTerminated();
		out(service.state()); // State.TERMINATED
		out(service.isRunning()); // false
	}
	
	@Test
	public void test2() {

		out("--------new--------");
		MyService2 service = new MyService2();
		RecordingListener.record(service);

		out(service.state()); // State.NEW
		out(service.isRunning()); // false

		out("--------startAsync--------");
		service.startAsync().awaitRunning();
		out(service.state()); // State.STARTING
		out(service.isRunning()); // false
		
		out("--------startAsync--------");
		service.startAsync(); // Exception，不能重复启动
	}
	
	@Test
	public void test3() {

		out("--------new--------");
		MyService2 service = new MyService2();
		RecordingListener.record(service);

		out(service.state()); // State.NEW
		out(service.isRunning()); // false

		out("--------startAsync--------");
		service.startAsync();
		out(service.state()); // State.STARTING
		out(service.isRunning()); // false
		
	}
	
	@Test
	public void test4() {

		out("--------new--------");
		MyService2 service = new MyService2();
		RecordingListener.record(service);

		out(service.state()); // State.NEW
		out(service.isRunning()); // false

		out("--------startAsync--------");
		service.startAsync();
		out(service.state()); // State.STARTING
		out(service.isRunning()); // false
		
		out("--------awaitRunning--------");
		service.awaitRunning();
		out(service.state()); // State.RUNNING
		out(service.isRunning()); // true
		
		out("--------awaitRunning--------");
		service.awaitRunning();
		out(service.state()); // State.RUNNING
		out(service.isRunning()); // true
		
		out("--------stopAsync--------");
		service.stopAsync();
		out(service.state()); // State.STOPPING
		out(service.isRunning()); // false
		
		out("--------awaitTerminated--------");
		service.awaitTerminated();
		out(service.state()); // State.TERMINATED
		out(service.isRunning()); // false
	}
	
	@Test
	public void test5() {

		out("--------new--------");
		MyService2 service = new MyService2();
		RecordingListener.record(service);

		out(service.state()); // State.NEW
		out(service.isRunning()); // false

		out("--------startAsync--------");
		service.startAsync();
		out(service.state()); // State.STARTING
		out(service.isRunning()); // false
		
		out("--------awaitRunning--------");
		service.awaitRunning();
		out(service.state()); // State.RUNNING
		out(service.isRunning()); // true
		
		out("--------stopAsync--------");
		service.stopAsync();
		out(service.state()); // State.STOPPING
		out(service.isRunning()); // false
		
		out("--------stopAsync--------");
		service.stopAsync(); // 重复关闭，底层不做任何业务，不触发 Listener.stopping
		out(service.state()); // State.STOPPING
		out(service.isRunning()); // false
		
		out("--------awaitTerminated--------");
		service.awaitTerminated();
		out(service.state()); // State.TERMINATED
		out(service.isRunning()); // false
	}
	
	@Test
	public void test6() {
		
		out("--------new--------");
		MyService2 service = new MyService2();
		RecordingListener.record(service);
		
		out(service.state()); // State.NEW
		out(service.isRunning()); // false
		
		out("--------startAsync--------");
		service.startAsync();
		out(service.state()); // State.STARTING
		out(service.isRunning()); // false
		
		out("--------awaitRunning--------");
		service.awaitRunning();
		out(service.state()); // State.RUNNING
		out(service.isRunning()); // true
		
		out("--------stopAsync--------");
		service.stopAsync();
		out(service.state()); // State.STOPPING
		out(service.isRunning()); // false
		
		out("--------awaitTerminated--------");
		service.awaitTerminated();
		out(service.state()); // State.TERMINATED
		out(service.isRunning()); // false
	}
	
	@Test
	public void test7() {

		out("--------new--------");
		MyService2 service = new MyService2();
		RecordingListener.record(service);

		out(service.state()); // State.NEW
		out(service.isRunning()); // false

		out("--------startAsync--------");
		service.startAsync();
		out(service.state()); // State.STARTING
		out(service.isRunning()); // false
		
		out("--------awaitRunning--------");
		service.awaitRunning();
		out(service.state()); // State.RUNNING
		out(service.isRunning()); // true
		
		out("--------stopAsync--------");
		service.stopAsync();
		out(service.state()); // State.STOPPING
		out(service.isRunning()); // false
		
		out("--------awaitTerminated--------");
		service.awaitTerminated();
		out(service.state()); // State.TERMINATED
		out(service.isRunning()); // false

		out("--------awaitTerminated--------");
		service.awaitTerminated();
		out(service.state()); // State.TERMINATED
		out(service.isRunning()); // false
	}
	
	@Test
	public void testStartException() {

		String fail = "start";
		
		out("--------new--------");
		MyService service = new MyService(fail);
		RecordingListener.record(service);

		out(service.state()); // State.NEW
		out(service.isRunning()); // false

		out("--------startAsync--------");
		service.startAsync();
		// doStart fail exception
		// doStart--->state: STARTING, isRunning: false
		// Listener.starting: FAILED, isRunning: false
		
		out(service.state()); // State.FAILED
		out(service.isRunning()); // false
	}
	
	@Test
	public void testStopException() {

		String fail = "stop";
		
		out("--------new--------");
		MyService service = new MyService(fail);
		RecordingListener.record(service);

		out(service.state()); // State.NEW
		out(service.isRunning()); // false

		out("--------startAsync--------");
		service.startAsync();
		out(service.state()); // State.STARTING
		out(service.isRunning()); // false
		
		out("--------notifyStarted--------");
		service._notifyStarted();
		out(service.state()); // State.RUNNING
		out(service.isRunning()); // true
		
		out("--------awaitRunning--------");
		service.awaitRunning();
		out(service.state()); // State.RUNNING
		out(service.isRunning()); // true
		
		out("--------stopAsync--------");
		service.stopAsync();
		
		// doStop--->state: STOPPING, isRunning: false
		// Listener.stopping: FAILED, from:RUNNING, isRunning: false
		// Listener.failed: FAILED, from:STOPPING, isRunning: false
		out(service.state()); // State.FAILED
		out(service.isRunning()); // false
	}
}
