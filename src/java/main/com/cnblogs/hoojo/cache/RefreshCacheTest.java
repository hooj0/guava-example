package com.cnblogs.hoojo.cache;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

/**
 * <b>function:</b> 刷新缓存与自动加载缓存
 * @author hoojo
 * @createDate 2017年11月9日 下午1:53:10
 * @file RefreshCacheTest.java
 * @package com.cnblogs.hoojo.cache
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class RefreshCacheTest {

	private LoadingCache<String, Integer> cache; 
	
	@Before
	public void init() {

		Map<String, Integer> map = ImmutableMap.of("a", 10, "b", 20, "c", 30);

		cache = CacheBuilder.newBuilder()
				//.maximumSize(10)
				.expireAfterAccess(1000, TimeUnit.SECONDS)
				//.refreshAfterWrite(2, TimeUnit.SECONDS) // 为缓存增加自动定时刷新功能，在读操作情况下，如果时间达到设置值会进行 load 操作
				.recordStats() // 开启缓存统计数据
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
						//return super.reload(key, oldValue);
						
						if (oldValue.intValue() % 2 != 0) { // 使用旧数据
							System.out.println("reload old cache:" + key  + "#" + oldValue);
							
							return Futures.immediateFuture(-oldValue);
						} else {
							System.out.println("reload new cache:" + key  + "#" + (oldValue));
							
							// asynchronous!
							return ListenableFutureTask.create(new Callable<Integer>() { // 异步加载新数据
								public Integer call() {
									
									System.out.println("reload create cache:" + key  + "#" + (oldValue + 2000));
									//return Optional.fromNullable(map.get(key)).or(System.identityHashCode(key));
									return oldValue + 2000;
								}
							});
						}  
					}
				});
	}
	
	/**
	 * refreshAfterWrite(2, TimeUnit.SECONDS)
	 * 		当程序休眠2秒后，再次get获取缓存的元素都会被刷新：执行reload方法重新加载数据
	 * 
	 * CacheBuilder.refreshAfterWrite(long, TimeUnit)可以为缓存增加自动定时刷新功能。
	 * 和expireAfterWrite相反，refreshAfterWrite通过定时刷新可以让缓存项保持可用，
	 * 
	 * 但请注意：缓存项只有在被检索时才会真正刷新（如果CacheLoader.refresh实现为异步，那么检索不会被刷新拖慢）。
	 * 因此，如果你在缓存上同时声明expireAfterWrite和refreshAfterWrite，缓存并不会因为刷新盲目地定时重置，
	 * 如果缓存项没有被检索，那刷新就不会真的发生，缓存项在过期时间后也变得可以回收。
	 */
	@Test
	public void testRefreshAfterWrite() throws ExecutionException, InterruptedException {
		
		for (int i = 0; i < 15; i++) {
			cache.put(i + "_", i);
		}
		System.out.println(ImmutableSortedSet.copyOf(cache.asMap().keySet()));
		
		for (int i = 0; i < 15; i++) {
			try {
				// 会reload当前get的缓存数据
				System.out.println("get:" + cache.get(i + "_"));
				
				if (i > 5) {
					Thread.sleep(500);
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		System.out.println(ImmutableSortedSet.copyOf(cache.asMap().keySet()));
	}
	
	/**
	 * cache.refresh(key); 会强制刷新数据，将数据进行重新加载
	 * 
	 * 刷新指定key的缓存对象,刷新和回收不太一样。
	 * 刷新表示为键加载新值，这个过程可以是异步的。
	 * 在刷新操作进行时，缓存仍然可以向其他线程返回旧值，而不像回收操作，读缓存的线程必须等待新值加载完成。
	 * 如果刷新过程抛出异常，缓存将保留旧值，而异常会在记录到日志后被丢弃[swallowed]。
	 * 重载CacheLoader.reload可以扩展刷新时的行为，这个方法允许开发者在计算新值时使用旧的值
	 */
	@Test
	public void testRefreshCache() throws ExecutionException, InterruptedException {
		
		for (int i = 0; i < 15; i++) {
			cache.put(i + "_", i);
		}
		System.out.println(ImmutableSortedSet.copyOf(cache.asMap().keySet()));
		
		for (int i = 0; i < 15; i++) {
			try {
				cache.refresh(i + "_");
				
				if (i > 5) {
					Thread.sleep(500);
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		System.out.println(ImmutableSortedSet.copyOf(cache.asMap().keySet()));
	}
	
	/**
	 *  屏蔽掉 //.refreshAfterWrite(2, TimeUnit.SECONDS)
	 */
	@Test
	public void testRefreshCacheTimeout() throws ExecutionException, InterruptedException {
		
		for (int i = 0; i < 15; i++) {
			cache.put(i + "_", i);
		}
		System.out.println(ImmutableSortedSet.copyOf(cache.asMap().keySet()));
		
		new Thread(() -> {
			for (int i = 0; i < 15; i++) {
				try {
					cache.refresh(i + "_");
					Thread.sleep(100);
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		}).start();
		
		new Thread(() -> {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			for (int i = 0; i < 15; i++) {
				try {
					System.out.println("get:" + cache.get(i + "_"));
					Thread.sleep(300);
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		}).start();
		
		Thread.sleep(1000 * 100);
		System.out.println(ImmutableSortedSet.copyOf(cache.asMap().keySet()));
	}
}
