package com.cnblogs.hoojo.cache;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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
public class ReferenceBasedEvictionCacheTest {

	private LoadingCache<String, User> cache; 
	private static Long sleepTimed = null;
	
	class User {
		private int index;
		private byte[] buff;
		
		public User(int index) {
			this.index = index;
			buff = new byte[1024 * 1024 * 2];
		}
		
		@Override
		public String toString() {
			return this.index + "#" + buff.length;
		}
	}
	
	@Before
	public void init() {

		Map<String, User> map = ImmutableMap.of();

		cache = CacheBuilder.newBuilder()
				//.weakKeys()
				//.weakValues()
				.softValues()
				.recordStats() // 开启缓存统计数据
				.removalListener(new RemovalListener<String, User>() { // 删除缓存给予监听通知
					@Override
					public void onRemoval(RemovalNotification<String, User> notification) {
						sleepTimed = 10L;
						String key = "";//Optional.fromNullable(notification.getKey()).or(notification.getValue().index + "");
						System.out.println("remove cache: " + notification.getKey() + "=" + notification.getValue() + ", wasEvicted: " + notification.wasEvicted() + ", cache view: " + ImmutableSortedSet.copyOf(cache.asMap().keySet()));
					}
				}).build(new CacheLoader<String, User>() { // 缓存加载方式
					@Override
					public User load(String key) throws RuntimeException {
						System.out.println("load:" + key);
						return map.get(key);
					}
				});
	}
	
	/**
	 * weakKeys:
	 * 		1、 当添加到缓存的对象，foo/bar 还被引用（key）的时候，系统并没有进行回收该资源
	 * 		2、而key = "myKey" 也被引用，没有被回收
	 * weakValues：
	 * 		1、由于foo/bar 还被引用（User）的时候，系统没有回收该资源
	 * 		2、key = "myKey" 是key，在value引用的情况下被回收
	 * softValues：
	 * 		1、由于foo/bar 还被引用（User）的时候，系统没有回收该资源
	 * 		2、key = "myKey" 是key，在value引用的情况下被回收
	 */
	@Test
	public void testWriteEvictionCacheGC() throws InterruptedException {
		
		System.out.println("cache size: " + cache.size());
		
		User foo = new User(0);
		User bar = new User(-1);
		cache.put("-1", foo);
		cache.put("-0", bar);
		
		String key = "myKey";
		
		for (int i = 1; i < 1030; i++) { // 20
			if (i == 3) {
				cache.put(key, new User(i));
			} else {
				cache.put("cache-" + i, new User(i));
			}
			
			if (sleepTimed != null) {
				Thread.sleep(sleepTimed);
			}
		}
		
		System.out.println("size:" + cache.size() + ", cache view:" + cache.asMap().keySet());
		System.out.println("finish...");
	}
	
	/**
	 * weakKeys：
	 *  	1、当不断进行访问缓存资源 "cache-2" 的情况下，发现并不能阻止资源被回收
	 * weakValues:
	 * 		1、当在push缓存的时候，系统有User对象被其他对象赋值（key/tmp 还被引用），并且长时间hold住，导致值引用的对象没有被回收
	 * softValues：
	 * 		1、由于key/tmp 还被引用（User）的时候，系统没有回收该资源
	 */
	@Test
	public void testReadWriteEvictionCacheGC() throws InterruptedException, ExecutionException {
		
		Object key = null;
		User tmp = null;
		for (int i = 1; i <= 1080; i++) { // 20
			cache.put("cache-" + i, new User(i));
			
			key = cache.getIfPresent("cache-2");
			tmp = cache.getIfPresent("cache-3");
			if (tmp != null) {
				tmp.buff.hashCode();
			}
			cache.getIfPresent("cache-1");
		}
		System.out.println(tmp + "#" + key);
		
		System.out.println("size:" + cache.size() + ", cache view:" + cache.asMap().keySet());
		System.out.println("finish...");
	}
}
