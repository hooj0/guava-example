package com.cnblogs.hoojo.cache;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Ticker;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.ImmutableMap;

/**
 * <b>function:</b> 【基于容量回收-数量】缓存回收策略 超出指定size后回收
 * 
 * <h2>缓存回收</h2> 缓存回收方式：基于容量回收、定时回收和基于引用回收；<br/>
 * <br/>
 * <b>基于容量的回收（size-based eviction）</b><br/>
 * <p>
 * 如果要规定缓存项的数目不超过固定值，只需使用CacheBuilder.maximumSize(long)。 缓存将尝试回收最近没有使用或总体上很少使用的缓存项。
 * ——警告：在缓存项的数目达到限定值之前，缓存就可能进行回收操作——通常来说，这种情况发生在缓存项的数目逼近限定值时。
 * </p>
 * <p>
 * 另外，不同的缓存项有不同的“权重”（weights） ——例如，如果你的缓存值，占据完全不同的内存空间， 你可以使用CacheBuilder.weigher(Weigher)指定一个权重函数，
 * 并且用CacheBuilder.maximumWeight(long)指定最大总重。 在权重限定场景中，除了要注意回收也是在重量逼近限定值时就进行了，还要知道重量是在缓存创建时计算的，因此要考虑重量计算的复杂度。
 * </p>
 * <b>定时回收（Timed Eviction）</b>
 * <p>
 * CacheBuilder提供两种定时回收的方法：
 * </p>
 * <p>
 * expireAfterAccess(long, TimeUnit)：缓存项在给定时间内没有被读/写访问，则回收。请注意这种缓存的回收顺序和基于大小回收一样。<br/>
 * expireAfterWrite(long,TimeUnit)：缓存项在给定时间内没有被写访问（创建或覆盖），则回收。
 * 如果认为缓存数据总是在固定时候后变得陈旧不可用，这种回收方式是可取的。如下文所讨论，定时回收周期性地在写操作中执行，偶尔在读操作中执行。
 * </p>
 * <b>基于引用的回收（Reference-based Eviction）</b>
 * <p>
 * 通过使用弱引用的键、或弱引用的值、或软引用的值，Guava Cache可以把缓存设置为允许垃圾回收：<br/>
 * CacheBuilder.weakKeys()：使用弱引用存储键。 <br/>
 * 当键没有其它（强或软）引用时，缓存项可以被垃圾回收。因为垃圾回收仅依赖恒等式（==），使用弱引用键的缓存用==而不是equals比较键。<br/>
 * CacheBuilder.weakValues()：使用弱引用存储值。 <br/>
 * 当值没有其它（强或软）引用时，缓存项可以被垃圾回收。因为垃圾回收仅依赖恒等式（==），使用弱引用值的缓存用==而不是equals比较值。<br/>
 * CacheBuilder.softValues()：使用软引用存储值。 <br/>
 * 软引用只有在响应内存需要时，才按照全局最近最少使用的顺序回收。
 * 考虑到使用软引用的性能影响，我们通常建议使用更有性能预测性的缓存大小限定（见上文，基于容量回收）。
 * 使用软引用值的缓存同样用==而不是equals比较值。<br/>
 * </p>
 * 
 * @author hoojo
 * @createDate 2017年10月25日 上午11:09:30
 * @file SizeBasedEvictionCacheTest.java
 * @package com.cnblogs.hoojo.cache
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class TimeBasedEvictionCacheTest {

	private LoadingCache<String, Integer> cache; 
	
	@Before
	public void init() {

		Map<String, Integer> map = ImmutableMap.of("a", 10, "b", 20, "c", 30);

		cache = CacheBuilder.newBuilder()
				//.expireAfterWrite(10, TimeUnit.SECONDS)  // 缓存项在给定时间内没有被写访问（创建或覆盖），则回收。
				.expireAfterAccess(10, TimeUnit.SECONDS) // 缓存项在给定时间内没有被读/写访问，则回收。
				.recordStats() // 开启缓存统计数据
				.ticker(new Ticker() {
					@Override
					public long read() {
						System.out.println("ticker...");
						return 1000L;
					}
				})
				.removalListener(new RemovalListener<String, Integer>() { // 删除缓存给予监听通知
					@Override
					public void onRemoval(RemovalNotification<String, Integer> notification) {
						System.out.println(notification.wasEvicted());
						System.out.println("remove cache key: " + notification.getKey() + ", value: " + notification.getValue());
					}
				}).build(new CacheLoader<String, Integer>() { // 缓存加载方式
					@Override
					public Integer load(String key) throws RuntimeException {
						System.out.println("loading......");
						return map.get(key);
					}
				});
	}
	
	/**
	 * expireAfterWrite
	 * 测试 缓存项在给定时间内没有被写访问（创建或覆盖），则回收 
	 */
	@Test
	public void testWriteEvictionCacheGC() throws InterruptedException {
		
		System.out.println("cache size: " + cache.size());
		System.out.println("cache init value: " + cache.asMap());
		
		// expireAfterWrite 默认没有被创建的缓存
		for (int i = 1; i <= 15; i++) {
			cache.put("cache-" + i, i);
			System.out.println(cache.asMap());
		}

		// 开启独立线程进行回收操作
		new Thread(new Runnable() {
			int i = 0;

			@Override
			public void run() {
				
				while (true) {
					i++;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					
					if (i == 8) {
						/** 1/2 没有被回收，3/4虽然被读取过还是被回收  */
						cache.put("cache-1", 23);
						cache.put("cache-2", 55);
						
						// 没有被写访问（创建或覆盖），则回收 
						cache.getIfPresent("cache-3");
						cache.getIfPresent("cache-4");
					}
					
					// 清理缓存
					cache.cleanUp();
					System.out.println(i + "-cleanUp...");
				}
			}
		}).start();
		
		Thread.sleep(1000 * 15);
		System.out.println("finish...");
	}
	
	/**
	 * expireAfterAccess(long, TimeUnit)：缓存项在给定时间内没有被读/写访问，则回收。
	 * 请注意这种缓存的回收顺序和基于大小回收一样：基于使用的次数和加入缓存的顺序进行回收
	 */
	@Test
	public void testReadWriteEvictionCacheGC() throws InterruptedException, ExecutionException {
		
		System.out.println("cache size: " + cache.size());
		System.out.println("cache init value: " + cache.asMap());
		
		// expireAfterAccess 默认没有被读/写访问，则回收
		for (int i = 1; i <= 15; i++) {
			cache.put("cache-" + i, i);
			System.out.println(cache.asMap());
		}

		// 开启独立线程进行回收操作
		new Thread(new Runnable() {
			int i = 0;

			@Override
			public void run() {
				
				while (true) {
					i++;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					
					// 1/2/3/4 都没有被回收
					if (i == 8) {
						cache.put("cache-1", 23);
						cache.put("cache-2", 55);
						
						cache.getIfPresent("cache-3");
						cache.getIfPresent("cache-4");
					}
					
					// 清理缓存
					//cache.cleanUp();
					System.out.println(i + "-cleanUp...");
				}
			}
		}).start();
		
		Thread.sleep(1000 * 15);
		System.out.println("finish...");
	}
}
