package com.cnblogs.hoojo.concurrency.monitor;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.util.concurrent.Monitor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 数据安全并发
 * @author hoojo
 * @createDate 2018年9月24日 下午5:12:57
 * @file MonitorExample.java
 * @package com.cnblogs.hoojo.concurrency.monitor
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class MonitorExample extends BasedTest {

	private final Monitor monitor = new Monitor();
	private volatile boolean condition = true;
	private int taskDoneCounter;
	private int stopTaskCount;
	// AtomicInteger：线程安全的加减操作
	private AtomicInteger taskSkippedCounter = new AtomicInteger(0);

	private Monitor.Guard conditionGuard = new Monitor.Guard(monitor) {
		@Override
		public boolean isSatisfied() {
			return condition;
		}
	};

	public void demoTryEnterIf() throws InterruptedException {
		out("* demoTryEnterIf -> " + Thread.currentThread().getName());
		
		if (monitor.tryEnterIf(conditionGuard)) {

			try {
				simulatedWork();
				taskDoneCounter++;

				out(Thread.currentThread().getName() + " -> tryEnterIf ->" + taskDoneCounter + "/" + condition);
			} finally {
				monitor.leave();
			}
			
		} else {
			out(Thread.currentThread().getName() + " -> No-tryEnterIf: " + taskSkippedCounter.get() + "/" + condition);
			// 自增加1
			taskSkippedCounter.incrementAndGet();
		}
	}

	public void demoEnterIf() throws InterruptedException {
		out("* demoEnterIf -> " + Thread.currentThread().getName());

		if (monitor.enterIf(conditionGuard)) {
			
			try {
				taskDoneCounter++;
				if (taskDoneCounter == stopTaskCount) {
					condition = false;
				}

				out(Thread.currentThread().getName() + " -> enterIf: " + taskDoneCounter + "/" + condition);
			} finally {
				monitor.leave();
			}
		} else {
			out(Thread.currentThread().getName() + "-> No-demoEnterIf: " + taskSkippedCounter.get() + "/" + condition);
			taskSkippedCounter.incrementAndGet();
		}

	}

	public void demoEnterWhen() throws InterruptedException {
		out("* demoEnterWhen: " + Thread.currentThread().getName());
		
		monitor.enterWhen(conditionGuard);
		try {
			taskDoneCounter++;
			
			if (taskDoneCounter == stopTaskCount) {
				condition = false;
			}
			out(Thread.currentThread().getName() + "-> enterWhen: " + taskDoneCounter + "/" + condition);
		} finally {
			monitor.leave();
		}
	}

	private void simulatedWork() throws InterruptedException {
		out("sleep->250");
		Thread.sleep(250);
	}

	// public void reEvaluateGuardCondition() {
	// monitor.reevaluateGuards();
	// }

	public int getStopTaskCount() {
		return stopTaskCount;
	}

	public void setStopTaskCount(int stopTaskCount) {
		this.stopTaskCount = stopTaskCount;
	}

	public void setCondition(boolean condition) {
		this.condition = condition;
	}

	public int getTaskSkippedCounter() {
		return taskSkippedCounter.get();
	}

	public int getTaskDoneCounter() {
		return taskDoneCounter;
	}
}