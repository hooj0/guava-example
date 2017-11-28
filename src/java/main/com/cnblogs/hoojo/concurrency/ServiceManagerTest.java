package com.cnblogs.hoojo.concurrency;

import java.util.ArrayList;
import java.util.List;

import com.cnblogs.hoojo.concurrency.service._abstract.MyService;
import com.cnblogs.hoojo.concurrency.service.execution.MyExecutionThreadService;
import com.cnblogs.hoojo.concurrency.service.idle.MyIdleService;
import com.cnblogs.hoojo.concurrency.service.scheduled.MyScheduledService;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.google.common.util.concurrent.ServiceManager.Listener;

/**
 * 
    startAsync()  ： 将启动所有被管理的服务。如果当前服务的状态都是NEW的话、那么你只能调用该方法一次、这跟 Service#startAsync()是一样的。
    stopAsync() ：将停止所有被管理的服务。
    addListener ：会添加一个ServiceManager.Listener，在服务状态转换中会调用该Listener
    awaitHealthy() ：会等待所有的服务达到Running状态
    awaitStopped()：会等待所有服务达到终止状态

	检测类的方法有：

    isHealthy()  ：如果所有的服务处于Running状态、会返回True
    servicesByState()：以状态为索引返回当前所有服务的快照
    startupTimes() ：返回一个Map对象，记录被管理的服务启动的耗时、以毫秒为单位，同时Map默认按启动时间排序。

	服务状态：
	    Service.State.NEW
	    Service.State.STARTING
	    Service.State.RUNNING
	    Service.State.STOPPING
	    Service.State.TERMINATED

 * @author hoojo
 * @createDate 2017年11月14日 下午5:44:53
 * @file ServiceManagerTest.java
 * @package com.cnblogs.hoojo.concurrency.service
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ServiceManagerTest {
	
	public static void main(String[] args) {

		List<Service> services = new ArrayList<Service>();
		services.add(new MyIdleService());
		services.add(new MyExecutionThreadService());
		services.add(new MyScheduledService());
		services.add(new MyService());
		
		ServiceManager manager = new ServiceManager(services);
		
		// 监听各服务状态切换
		manager.addListener(new Listener() {
			@Override
			public void healthy() {
				super.healthy();
				
				System.out.println("healthy...");
			}

			@Override
			public void stopped() {
				super.stopped();
				
				System.out.println("stopped...");
			}

			@Override
			public void failure(Service service) {
				super.failure(service);
				
				System.out.println("failure..." + service);
			}
		});
		
		// 状态为索引返回当前所有服务的快照
		System.out.println(manager.servicesByState());

		// 开启服务
		manager.startAsync();
		System.out.println(manager.servicesByState());
		
		// 统计启动各服务时间
		System.out.println(manager.startupTimes());
		
		// 会等待所有的服务达到Running状态
		manager.awaitHealthy();
		System.out.println(manager.servicesByState());
		
		// 判断所有服务是否都在运行
		System.out.println(manager.isHealthy());

		// 会等待所有服务达到终止状态
		manager.awaitStopped();
		System.out.println(manager.servicesByState());
		
		// 停止服务
		manager.stopAsync();
		System.out.println(manager.servicesByState());
	}
}
