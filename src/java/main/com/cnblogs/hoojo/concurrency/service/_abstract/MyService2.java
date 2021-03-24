package com.cnblogs.hoojo.concurrency.service._abstract;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AbstractService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Service;
import org.junit.Assert;

import javax.annotation.concurrent.GuardedBy;
import java.util.List;

/**
 * 自定义的线程管理
 * 
 * doStart和doStop方法的实现需要考虑下性能，尽可能的低延迟。
 * 如果初始化的开销较大，如读文件，打开网络连接，
 * 或者其他任何可能引起阻塞的操作，建议移到另外一个单独的线程去处理。
 * <br/>
 * 注意：
 * 		1、不允许重复启动，启动前会检查启动状态，若已启动会抛出异常IllegalStateException <br/>
 * 		2、_notifyStarted 不能重复调用；若重复调用时，State = STARTING 才可以；
 * 			// State != STARTING
			// Exception，Cannot notifyStarted() when the service is RUNNING
 * 		3、awaitRunning 可以重复调用
 * 		4、重复关闭，底层不做任何业务，不触发 Listener.stopping
 * 		5、notifyStopped 不能重复调用；
 * 			// previous != STOPPING && previous != RUNNING
			// Exception，Cannot notifyStopped() when the service is TERMINATED
		6、awaitTerminated 可重复调用
 * 
 * @author hoojo
 * @createDate 2017年11月14日 下午5:35:50
 * @file MyService.java
 * @package com.cnblogs.hoojo.concurrency.service
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class MyService2 extends AbstractService {

	String fail;
	
	public MyService2() {
	}
	
	public MyService2(String fail) {
		this.fail = fail;
	}
	
	// 首次调用startAsync()时会同时调用doStart(), doStart()内部需要处理所有的初始化工作
	// 如果启动成功则调用notifyStarted()方法；启动失败则调用notifyFailed()
	@SuppressWarnings("unused")
	@Override
	protected void doStart() {
		System.out.println("doStart--->state: " + this.state() + ", isRunning: " + this.isRunning());
		
		if ("start".equals(fail)) {
			Assert.fail(fail);
			int a = 1/0;
		}
		
		System.out.println("--------notifyStarted--------");
		notifyStarted();
		
		System.out.println(state()); // State
		System.out.println(isRunning()); // 
	}

	// 首次调用stopAsync()会同时调用doStop(),doStop()要做的事情就是停止服务，
	// 如果停止成功则调用 notifyStopped()方法；停止失败则调用 notifyFailed()方法。
	@SuppressWarnings("unused")
	@Override
	protected void doStop() {
		System.out.println("doStop--->state: " + this.state() + ", isRunning: " + this.isRunning());
		
		if ("stop".equals(fail)) {
			Assert.fail(fail);
			int a = 1/0;
		}
		
		System.out.println("--------notifyStopped--------");
		notifyStopped();
		
		System.out.println(state()); // State
		System.out.println(isRunning()); // 
	}
	
	public static class RecordingListener extends Listener {
		static RecordingListener record(Service service) {
			RecordingListener listener = new RecordingListener(service);
			service.addListener(listener, MoreExecutors.directExecutor());
			return listener;
		}

		final Service service;

		RecordingListener(Service service) {
			this.service = service;
		}

		@GuardedBy("this")
		final List<State> stateHistory = Lists.newArrayList();

		@Override
		public synchronized void starting() {
			System.out.println("Listener.starting: " + service.state() + ", isRunning: " + service.isRunning());
		}

		@Override
		public synchronized void running() {
			System.out.println("Listener.running: " + service.state() + ", isRunning: " + service.isRunning());
			
			//service.awaitRunning(); // 运行
		}

		@Override
		public synchronized void stopping(State from) {
			System.out.println("Listener.stopping: " + service.state() + ", from:" + from + ", isRunning: " + service.isRunning());
		}

		@Override
		public synchronized void terminated(State from) {
			System.out.println("Listener.terminated: " + service.state() + ", from:" + from + ", isRunning: " + service.isRunning());
		}

		@Override
		public synchronized void failed(State from, Throwable failure) {
			System.out.println("Listener.failed: " + service.state() + ", from:" + from + ", isRunning: " + service.isRunning());
			System.out.println(failure.getMessage());
		}
	}
	
	public static void main(String[] args) throws Exception {
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
}
