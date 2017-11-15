package com.cnblogs.hoojo.concurrency.service.idle;

import java.util.List;
import java.util.concurrent.Executor;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.MoreExecutors;

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
public class MyIdleService2 extends AbstractIdleService {

	private String exceptionState;
	
	public final List<State> transitionStates = Lists.newArrayList();
	public int startUpCalled = 0;
	public int shutDownCalled = 0;

	public MyIdleService2() {
	}

	public MyIdleService2(String exceptionState) {
		this.exceptionState = exceptionState;
	}

	@Override
	protected void startUp() throws Exception {
		System.out.println("MyIdeService.startUp...");
		Thread.sleep(10);

		startUpCalled++;

		if ("start".equals(exceptionState)) {
			throw new Exception("start exception");
		}
	}

	@Override
	protected void shutDown() throws Exception {
		System.out.println("MyIdeService.shutDown...");

		shutDownCalled++;

		if ("shut".equals(exceptionState)) {
			throw new Exception("start exception");
		}
	}

	@Override
	protected Executor executor() {
		transitionStates.add(state());
		return MoreExecutors.directExecutor();
	}
}
