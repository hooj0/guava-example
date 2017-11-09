package com.cnblogs.hoojo.cache;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.base.Ticker;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * <b>function:</b> 测试模拟时间
 * @author hoojo
 * @createDate 2017年11月9日 下午3:42:50
 * @file TickerTest.java
 * @package com.cnblogs.hoojo.cache
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class TickerTest {

	private LoadingCache<String, Integer> cache;
	private MyTicker ticker;
	
	private class MyTicker extends Ticker {
		private long start = Ticker.systemTicker().read();  
        private long elapsedNano = 0;  
  
        @Override  
        public long read() {  
            return start + elapsedNano;  
        }  
  
        public void addElapsedTime(long elapsedNano) {  
            this.elapsedNano = elapsedNano;  
        }  
	}
	
	@Before
	public void init() {

		Map<String, Integer> map = ImmutableMap.of("a", 10, "b", 20, "c", 30);

		ticker = new MyTicker();
		
		cache = CacheBuilder.newBuilder()
				.expireAfterAccess(1, TimeUnit.HOURS) // 设置1小时过期
				.refreshAfterWrite(30, TimeUnit.MINUTES) // 设置半小时的时候，在读操作情况下，如果时间达到设置值会进行 load 操作
				.recordStats() // 开启缓存统计数据
				.ticker(ticker)
				.removalListener(new RemovalListener<String, Integer>() { // 删除缓存给予监听通知
					@Override
					public void onRemoval(RemovalNotification<String, Integer> notification) {
						System.out.println("remove cache key: " + notification.getKey() + "=" + notification.getValue() + ", wasEvicted:" + notification.wasEvicted());
					}
				}).build(new CacheLoader<String, Integer>() { // 缓存加载方式
					@Override
					public Integer load(String key) throws RuntimeException {
						System.out.println("load:" + key);
						return Optional.fromNullable(map.get(key)).or(System.identityHashCode(key));
					}
					
					@Override
					public ListenableFuture<Integer> reload(String key, Integer oldValue) throws Exception {
						System.out.println("reload cache:" + key  + "#" + oldValue);
						return Futures.immediateFuture(-oldValue);
					}
				});
	}
	
	@Test
	public void testRefreshCache() throws ExecutionException, InterruptedException {
		
		for (int i = 0; i < 15; i++) {
			cache.put(i + "_", i);
		}
		System.out.println(ImmutableSortedSet.copyOf(cache.asMap().keySet()));
		
		// 模拟时间流逝25 分钟，数据没有被刷新
		// ticker.addElapsedTime(TimeUnit.NANOSECONDS.convert(25, TimeUnit.MINUTES));
		// 模拟时间流逝半小时，数据被更新
		ticker.addElapsedTime(TimeUnit.NANOSECONDS.convert(31, TimeUnit.MINUTES));
		
		for (int i = 0; i < 15; i++) {
			try {
				// 会reload当前get的缓存数据
				System.out.println("get:" + cache.get(i + "_"));
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		System.out.println(ImmutableSortedSet.copyOf(cache.asMap().keySet()));
	}
	
	@Test
	public void testCleanUpCache() throws ExecutionException, InterruptedException {
		
		for (int i = 0; i < 15; i++) {
			cache.put(i + "_", i);
		}
		System.out.println(ImmutableSortedSet.copyOf(cache.asMap().keySet()));
		
		// 模拟时间流逝35 分钟，数据没有被刷新
		// ticker.addElapsedTime(TimeUnit.NANOSECONDS.convert(35, TimeUnit.MINUTES));
		// 模拟时间流逝1小时，数据被更新
		ticker.addElapsedTime(TimeUnit.NANOSECONDS.convert(62, TimeUnit.MINUTES));
		
		cache.cleanUp();
		System.out.println(ImmutableSortedSet.copyOf(cache.asMap().keySet()));
	}
}
