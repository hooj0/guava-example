package com.cnblogs.hoojo.concurrency.service;

import com.google.common.util.concurrent.AbstractService;

/**
 * <b>function:</b> 自定义的线程管理
 * 
 * doStart和doStop方法的实现需要考虑下性能，尽可能的低延迟。
 * 如果初始化的开销较大，如读文件，打开网络连接，
 * 或者其他任何可能引起阻塞的操作，建议移到另外一个单独的线程去处理。
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
public class MyService extends AbstractService {

	// 首次调用startAsync()时会同时调用doStart(), doStart()内部需要处理所有的初始化工作
	// 如果启动成功则调用notifyStarted()方法；启动失败则调用notifyFailed()
	@Override
	protected void doStart() {
		System.out.println("MyService.doStart...");
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 首次调用stopAsync()会同时调用doStop(),doStop()要做的事情就是停止服务，
	// 如果停止成功则调用 notifyStopped()方法；停止失败则调用 notifyFailed()方法。
	@Override
	protected void doStop() {
		System.out.println("MyService.doStop...");

	}
}
