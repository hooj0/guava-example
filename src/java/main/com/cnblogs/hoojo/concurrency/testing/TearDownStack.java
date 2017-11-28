package com.cnblogs.hoojo.concurrency.testing;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.concurrent.GuardedBy;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.Lists;

/**
 * <b>function:</b>
 * 
 * @author hoojo
 * @createDate 2017年11月24日 下午4:21:22
 * @file TearDownStack.java
 * @package com.cnblogs.hoojo.concurrency.service.execution
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Beta
@GwtCompatible
public class TearDownStack implements TearDownAccepter {
	private static final Logger logger = Logger.getLogger(TearDownStack.class.getName());

	@GuardedBy("stack")
	final LinkedList<TearDown> stack = new LinkedList<>();

	private final boolean suppressThrows;

	public TearDownStack() {
		this.suppressThrows = false;
	}

	public TearDownStack(boolean suppressThrows) {
		this.suppressThrows = suppressThrows;
	}

	@Override
	public final void addTearDown(TearDown tearDown) {
		synchronized (stack) {
			stack.addFirst(checkNotNull(tearDown));
		}
	}

	/**
	 * Causes teardown to execute.
	 */
	public final void runTearDown() {
		List<Throwable> exceptions = new ArrayList<>();
		List<TearDown> stackCopy;
		synchronized (stack) {
			stackCopy = Lists.newArrayList(stack);
			stack.clear();
		}
		for (TearDown tearDown : stackCopy) {
			try {
				tearDown.tearDown();
			} catch (Throwable t) {
				if (suppressThrows) {
					logger.log(java.util.logging.Level.ALL, "exception thrown during tearDown", t);
				} else {
					exceptions.add(t);
				}
			}
		}
		if ((!suppressThrows) && (exceptions.size() > 0)) {
			throw ClusterException.create(exceptions);
		}
	}
}
