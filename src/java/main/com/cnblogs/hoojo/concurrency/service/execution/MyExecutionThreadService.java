package com.cnblogs.hoojo.concurrency.service.execution;

import com.google.common.util.concurrent.AbstractExecutionThreadService;

/**
 * <b>function:</b> 通过单线程处理启动、运行、和关闭等操作
 * 
 * @author hoojo
 * @createDate 2017年11月14日 上午11:33:03
 * @file MyExecutionThreadService.java
 * @package com.cnblogs.hoojo.concurrency.service
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class MyExecutionThreadService extends AbstractExecutionThreadService {

	@Override
	protected void run() throws Exception {
		System.out.println("MyExecutionThreadService.run...");
	}

	// start()内部会调用startUp()方法，创建一个线程、然后在线程内调用run()方法
	protected void startUp() throws InterruptedException {
		System.out.println("MyExecutionThreadService.startUp...");
		
		Thread.sleep(10);
	}

	// 让run()方法结束返回
	// stop()会调用 triggerShutdown()方法并且等待线程终止。
	protected void triggerShutdown() {
		System.out.println("MyExecutionThreadService.triggerShutdown...");
	}
}
