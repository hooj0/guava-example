package com.cnblogs.hoojo.concurrency.service.idle;

import com.google.common.util.concurrent.AbstractIdleService;

/**
 * 状态切换监控
 * 
 * @author hoojo
 * @createDate 2017年11月14日 上午11:29:56
 * @file MyIdeService.java
 * @package com.cnblogs.hoojo.concurrency.service
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class MyIdleService extends AbstractIdleService {

	private String exceptionState;
	
	public MyIdleService() {
	}

	public MyIdleService(String exceptionState) {
		this.exceptionState = exceptionState;
	}

	@Override
	protected void startUp() throws Exception {
		System.out.println("MyIdeService.startUp...");
		Thread.sleep(10);

		if ("start".equals(exceptionState)) {
			throw new Exception("start exception");
		}
	}

	@Override
	protected void shutDown() throws Exception {
		System.out.println("MyIdeService.shutDown...");

		if ("shut".equals(exceptionState)) {
			throw new Exception("start exception");
		}
	}
}
