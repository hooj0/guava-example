package com.cnblogs.hoojo.cache;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.LongAdder;

import org.junit.Before;
import org.junit.Test;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.cache.Weigher;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;

/**
 * 【基于容量回收-权重】缓存回收策略 超出指定weights后回收
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
 * 并且用CacheBuilder.maximumWeight(long)指定最大总重。 在权重限定场景中，除了要注意回收也是在重量逼近限定值时就进行了，
 * 还要知道重量是在缓存创建时计算的，因此要考虑重量计算的复杂度。
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
public class WeigherBasedEvictionCacheTest {

	private LoadingCache<Integer, Integer> cache; 
	private LongAdder adder = new LongAdder();
	
	@Before
	public void init() {

		Map<Integer, Integer> map = ImmutableMap.of(11, 10, 12, 20, 13, 30);

		cache = CacheBuilder.newBuilder()
				//.concurrencyLevel(Runtime.getRuntime().availableProcessors() - 1)  
                //.initialCapacity(10240)
				// 删除长时间没被访问过的对象或者根据配置的被释放的对象
				.maximumWeight(20) // 最大权重，不能和 maximumSize 并用
				.weigher(new Weigher<Integer, Integer>() {
					@Override
					public int weigh(Integer key, Integer value) {
						// 基于key的字节数量
						// 在正常业务上，key->byte的转换也许需要占用一定的时间，所以返回指定权重值的需要慎重考虑
						/**
						 *  通常使用：
						 *  	key.length
						 *  	key.bytes.length
						 *  	value.length
						 *  	value.bytes.length
						 */
						adder.add(value);
						
						Map<Integer, Integer> cacheView = new TreeMap<Integer, Integer>(cache.asMap());
						cacheView.put(key, value);
						
						System.out.println("put cache: " + key + "=" + value + ", weigh: " + adder.sum() + ", cache view: " + cacheView.keySet());
						
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						return value.intValue();
					}
				})
				.recordStats() // 开启缓存统计数据
				.removalListener(new RemovalListener<Integer, Integer>() { // 删除缓存给予监听通知
					@Override
					public void onRemoval(RemovalNotification<Integer, Integer> notification) {
						long size = adder.sum() + notification.getValue();
						System.out.println("remove cache: " + notification.getKey() + "=" + notification.getValue() + ", weigh: " + size + ", wasEvicted: " + notification.wasEvicted() + ", cache view: " + ImmutableSortedSet.copyOf(cache.asMap().keySet()) + "\n");
						
						adder.add(-notification.getValue());
					}
				}).build(new CacheLoader<Integer, Integer>() { // 缓存加载方式
					@Override
					public Integer load(Integer key) throws RuntimeException {
						System.out.println("load: " + key);
						return map.get(key);
					}
				});
	}
	
	// 测试 超出最大缓存数量回收
	@Test
	public void testWriteEvictionCacheGC() throws InterruptedException {
		System.gc();
		
		System.out.println("cache init value: " + cache.asMap());
		
		// 默认最先被缓存的数据被回收，后面的缓存会挤走最前面的
		for (int i = 1; i < 15; i++) {
			int size = (int) (Math.random() * 9 + 1L);
			cache.put(i, size);
			
			//System.out.println("keys bytes length: " + cache.asMap().keySet().size() * 2 + ", cache view: " + cache.asMap());
			//Thread.sleep(1000);
		}
		System.out.println("cache size: " + cache.size());
		System.out.println("cache final view: " + ImmutableSortedMap.copyOf(cache.asMap()));
	}
	
	@Test
	public void testWriteEvictionCacheGC2() throws InterruptedException, ExecutionException {
		
		System.out.println("cache init value: " + cache.asMap());
		
		// 默认最先被缓存的数据被回收，后面的缓存会挤走最前面的
		for (int i = 1; i <= 10; i++) {
			cache.put(i, 2);
		}
		System.out.println("put 1-10 limit cache：" + ImmutableSortedMap.copyOf(cache.asMap()) + "\n");

		for (int i = 11; i <= 15; i++) {
			cache.put(i, 1); // 再次put cache，之前的cache会被回收
		}
		System.out.println("put 11-15 limit cache：" + ImmutableSortedMap.copyOf(cache.asMap()));
	}
	
	@Test
	public void testReadWriteEvictionCacheGC() throws InterruptedException, ExecutionException {
		
		System.out.println("cache init value: " + cache.asMap());
		
		// 默认最先被缓存的数据被回收，后面的缓存会挤走最前面的
		for (int i = 1; i <= 10; i++) {
			cache.put(i, 2);
		}
		System.out.println("put 1-10 limit cache：" + ImmutableSortedMap.copyOf(cache.asMap()) + "\n");

		for (int i = 11; i <= 15; i++) {
			// 访问特定的某些cache，这些访问的cache将没有被系统回收
			cache.getAllPresent(Arrays.asList(1, 2, 3, 4));

			cache.put(i, 2); // 再次put cache，之前的cache会被回收
		}
		System.out.println("put 11-15 limit cache：" + ImmutableSortedMap.copyOf(cache.asMap()));
	}
}
