package com.cnblogs.hoojo.concurrency.testing;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Longs;
import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.AbstractListeningExecutorService;
import com.google.common.util.concurrent.ListenableScheduledFuture;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;

/**
 * Factory methods for {@link ExecutorService} for testing.
 *
 * @author Chris Nokleberg
 * @since 14.0
 */
@Beta
@GwtIncompatible
public final class TestingExecutors {
	private TestingExecutors() {
	}

	/**
	 * Returns a {@link ScheduledExecutorService} that never executes anything.
	 * <p>
	 * The {@code shutdownNow} method of the returned executor always returns an empty list despite the fact that
	 * everything is still technically awaiting execution. The {@code getDelay} method of any {@link ScheduledFuture}
	 * returned by the executor will always return the max long value instead of the time until the user-specified
	 * delay.
	 */
	public static ListeningScheduledExecutorService noOpScheduledExecutor() {
		return new NoOpScheduledExecutorService();
	}

	private static final class NoOpScheduledExecutorService extends AbstractListeningExecutorService implements ListeningScheduledExecutorService {

		private volatile boolean shutdown;

		@Override
		public void shutdown() {
			shutdown = true;
			System.out.println("shutdown");
		}

		@Override
		public List<Runnable> shutdownNow() {
			System.out.println("shutdownNow");
			shutdown();
			return ImmutableList.of();
		}

		@Override
		public boolean isShutdown() {
			return shutdown;
		}

		@Override
		public boolean isTerminated() {
			return shutdown;
		}

		@Override
		public boolean awaitTermination(long timeout, TimeUnit unit) {
			System.out.println("awaitTermination");
			return true;
		}

		@Override
		public void execute(Runnable runnable) {
			System.out.println("execute");
		}

		@Override
		public <V> ListenableScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
			System.out.println("schedule");
			return NeverScheduledFuture.create();
		}

		@Override
		public ListenableScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
			System.out.println("schedule");
			return NeverScheduledFuture.create();
		}

		@Override
		public ListenableScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period,
				TimeUnit unit) {
			return NeverScheduledFuture.create();
		}

		@Override
		public ListenableScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay,
				TimeUnit unit) {
			return NeverScheduledFuture.create();
		}

		private static class NeverScheduledFuture<V> extends AbstractFuture<V> implements ListenableScheduledFuture<V> {

			static <V> NeverScheduledFuture<V> create() {
				return new NeverScheduledFuture<V>();
			}

			@Override
			public long getDelay(TimeUnit unit) {
				return Long.MAX_VALUE;
			}

			@Override
			public int compareTo(Delayed other) {
				return Longs.compare(getDelay(TimeUnit.NANOSECONDS), other.getDelay(TimeUnit.NANOSECONDS));
			}
		}
	}
}