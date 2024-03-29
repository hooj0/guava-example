package com.cnblogs.hoojo.cache;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.cache.*;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 【基于容量回收-数量】缓存回收策略 超出指定size后回收
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
 * expireAfterWrite(long,TimeUnit)：缓存项在给定时间内没有被写访问（创建或覆盖），则回收。如果认为缓存数据总是在固定时候后变得陈旧不可用，这种回收方式是可取的。如下文所讨论，定时回收周期性地在写操作中执行，偶尔在读操作中执行。
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
@SuppressWarnings("ALL")
public class SizeBasedEvictionCacheTest extends BasedTest {

	private LoadingCache<String, Integer> cache; 
	
	@Before
	public void init() {

		Map<String, Integer> map = ImmutableMap.of("a", 10, "b", 20, "c", 30);

		cache = CacheBuilder.newBuilder()
				.maximumSize(10) // （不能和 maximumWeight 并用）最大缓存元素个数，删除长时间没被访问过的对象或者根据配置的被释放的对象
				.recordStats() // 开启缓存统计数据
				.removalListener(new RemovalListener<String, Integer>() { // 删除缓存给予监听通知
					@Override
					public void onRemoval(RemovalNotification<String, Integer> notification) {
						out(notification.wasEvicted());
						out("remove cache key: " + notification.getKey() + ", value: " + notification.getValue());
					}
				}).build(new CacheLoader<String, Integer>() { // 缓存加载方式
					@Override
					public Integer load(String key) throws RuntimeException {
						out("loading......");
						return map.get(key);
					}
				});
	}
	
	// 测试 超出最大缓存数量回收
	@Test
	public void testWriteEvictionCacheGC() throws InterruptedException {
		
		out("cache size: " + cache.size());
		out("cache init value: " + cache.asMap());
		
		// 默认最先被缓存的数据被回收，后面的缓存会挤走最前面的
		for (int i = 1; i < 15; i++) {
			cache.put("cache-" + i, i);
			out(cache.asMap());
			Thread.sleep(1000);
		}
	}
	
	@Test
	public void testReadWriteEvictionCacheGC() throws InterruptedException, ExecutionException {
		
		out("cache size: " + cache.size());
		out("cache init value: " + cache.asMap());
		
		// 默认最先被缓存的数据被回收，后面的缓存会挤走最前面的
		for (int i = 1; i < 15; i++) {
			// 后面访问特定的某些值，这些访问的值没有被系统回收
			if (i > 8) {
				out(("get-cache-" + (i - 8)) + ": " + cache.getIfPresent("cache-" + (i - 8)));
			}
			cache.put("cache-" + i, i);
			out(cache.asMap());
			Thread.sleep(1000);
		}
	}
	
	@Test
	public void testReadWriteEvictionCacheGC2() throws InterruptedException, ExecutionException {
		
		out("cache size: " + cache.size());
		out("cache init value: " + cache.asMap());
		
		// 默认最先被缓存的数据被回收，后面的缓存会挤走最前面的
		for (int i = 1; i <= 10; i++) {
			cache.put("cache-" + i, i);
		}
		out("put 1-10 limit cache：" + cache.asMap());
		
		// 访问特定的某些cache，这些访问的cache将没有被系统回收
		cache.getAll(Arrays.asList("cache-1", "cache-2", "cache-3", "cache-4"));

		for (int i = 11; i <= 15; i++) {
			cache.put("cache-" + i, i); // 再次put cache，之前的cache会被回收
		}
		out("put 11-15 limit cache：" + cache.asMap());
	}
}
