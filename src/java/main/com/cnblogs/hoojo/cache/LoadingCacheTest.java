package com.cnblogs.hoojo.cache;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.base.Ticker;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalListeners;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.ImmutableMap;

/**
 * <b>function:</b> LoadingCache
 * 
	<table class="src" style="box-sizing: border-box; border-collapse: collapse; border-spacing: 0px;  border-color: rgb(214, 214, 214); width: 495.555572509766px; vertical-align: top; margin-top: 8px; margin-bottom: 8px; color: rgb(49, 49, 49);    font-size: 14.4444446563721px; line-height: 22px; background-color: rgb(247, 247, 247);">
	<tbody style="box-sizing: border-box;">
		<tr style="box-sizing: border-box;">
			<th style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px; width: 38.8888893127441px; background: rgb(238, 238, 238);">
				S.N.</th>
			<th style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px; background: rgb(238, 238, 238);">
				方法及说明</th>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				1</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">V apply(K key)</b><br style="box-sizing: border-box;">
				不推荐使用。提供满足功能接口;使用get(K)或getUnchecked(K)代替。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				2</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">ConcurrentMap&lt;K,V&gt; asMap()</b><br style="box-sizing: border-box;">
				返回存储在该缓存作为一个线程安全的映射条目的视图。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				3</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">V get(K key)</b><br style="box-sizing: border-box;">
				返回一个键在这个高速缓存中，首先装载如果需要该值相关联的值。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				4</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">ImmutableMap&lt;K,V&gt; getAll(Iterable&lt;? extends K&gt; keys)</b><br style="box-sizing: border-box;">
				返回一个键相关联的值的映射，创建或必要时检索这些值。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				5</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">V getUnchecked(K key)</b><br style="box-sizing: border-box;">
				返回一个键在这个高速缓存中，首先装载如果需要该值相关联的值。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				6</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">void refresh(K key)</b><br style="box-sizing: border-box;">
				加载键key，可能是异步的一个新值。</td>
		</tr>
	</tbody>
</table>

通常来说，Guava Cache适用于：

    你愿意消耗一些内存空间来提升速度。
    你预料到某些键会被查询一次以上。
    缓存中存放的数据总量不会超出内存容量。（Guava Cache是单个应用运行时的本地缓存。它不把数据存放到文件或外部服务器。如果这不符合你的需求，请尝试Memcached这类工具）

 * @author hoojo
 * @createDate 2017年10月20日 下午4:33:50
 * @file LoadingCacheTest.java
 * @package com.cnblogs.hoojo.cache
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class LoadingCacheTest {

	@Test
	public void testApi() throws ExecutionException, InterruptedException {
		
		Map<String, Integer> map = ImmutableMap.of("a", 10, "b", 20, "c", 30);
		
		LoadingCache<String, Integer> cache = CacheBuilder.newBuilder()
			.maximumSize(10) // 最大缓存元素个数，删除长时间没被访问过的对象或者根据配置的被释放的对象
			.expireAfterWrite(10, TimeUnit.MINUTES) // 缓存项在给定时间内没有被写访问（创建或覆盖），则回收。
			.expireAfterAccess(10, TimeUnit.MINUTES) // 缓存项在给定时间内没有被读/写访问，则回收。
			//.refreshAfterWrite(10, TimeUnit.MINUTES) // 为缓存增加自动定时刷新功能，在读操作情况下，如果时间达到设置值会进行 load 操作
			.recordStats() // 开启缓存统计数据
			.removalListener(new RemovalListener<String, Integer>() { // 删除缓存给予监听通知
				@Override
				public void onRemoval(RemovalNotification<String, Integer> notification) {
					System.out.println("是否是回收缓存：" + notification.wasEvicted()); // 显示的删除缓存时，值为 false
					System.out.println("remove cache key: " + notification.getKey() + ", value: " + notification.getValue());
				}
			})
			.ticker(new Ticker() {
				
				@Override
				public long read() {
					System.out.println("ticker...");
					return 1000L;
				}
			})
			.build(new CacheLoader<String, Integer>() { // 缓存加载方式
				@Override
				public Integer load(String key) throws RuntimeException {
					System.out.println("loading......");
					return map.get(key); 
				}
			});
		/**
		 * 警告：默认情况下，监听器方法是在移除缓存时同步调用的。
		 * 因为缓存的维护和请求响应通常是同时进行的，代价高昂的监听器方法在同步模式下会拖慢正常的缓存请求。
		 * 在这种情况下，你可以使用RemovalListeners.asynchronous(RemovalListener, Executor)把监听器装饰为异步操作
		 */
		RemovalListener<String, Integer> listener = RemovalListeners.asynchronous(new RemovalListener<String, Integer>() { // 删除缓存给予监听通知
			@Override
			public void onRemoval(RemovalNotification<String, Integer> notification) {
				System.out.println("是否是回收缓存：" + notification.wasEvicted()); // 显示的删除缓存时，值为 false
				System.out.println("remove cache key: " + notification.getKey() + ", value: " + notification.getValue());
			}
		}, Executors.newFixedThreadPool(5));
		
		CacheBuilder.newBuilder().removalListener(listener);
		
		// 获取缓存
		System.out.println(cache.get("a")); // 10
		try {
			System.out.println(cache.get("e")); // CacheLoader$InvalidCacheLoadException
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		/**
		 * 不管有没有自动加载功能，都支持get(K, Callable<V>)方法。
		 * 这个方法返回缓存中相应的值，或者用给定的Callable运算并把结果加入到缓存中。
		 * 在整个加载方法完成前，缓存项相关的可观察状态都不会更改。
		 * 这个方法简便地实现了模式"如果有缓存则返回；
		 * 否则运算、缓存、然后返回"
		 */
		try {
			System.out.println(cache.get("e", new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					//return null; // Can not be null, otherwise it will throw an InvalidCacheLoadException
					return -1;
				}
			}));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		// 存在数据就返回，不能存在就为null
		System.out.println(cache.getIfPresent("a"));
		System.out.println(cache.getIfPresent("xx"));
		
		// 由于CacheLoader可能抛出异常，LoadingCache.get(K)也声明为抛出ExecutionException异常。
		// 如果你定义的CacheLoader没有声明任何检查型异常，则可以通过getUnchecked(K)查找缓存；
		// 但必须注意，一旦CacheLoader声明了检查型异常，就不可以调用getUnchecked(K)
		System.out.println(cache.getUnchecked("a"));
		//System.out.println(cache.getUnchecked("z"));
		
		// 缓存视图，调用过的、已被缓存过的缓存数据
		System.out.println(cache.asMap()); // {e=-1, a=10}
		
		// ImmutableMap<String, Integer> 没有数据将抛出异常
		System.out.println(cache.getAll(Arrays.asList("a", "b"))); // {a=10, b=20} 
		
		// x 没有数据，就不处理；默认getAll 中的元素没有数据将抛出异常
		System.out.println(cache.getAllPresent(Arrays.asList("a", "b", "x"))); // {a=10, b=20}
		
		// 缓存统计 需要开启 recordStats()
		// hitRate()：缓存命中率；averageLoadPenalty()：加载新值的平均时间，单位为纳秒；evictionCount()：缓存项被回收的总数，不包括显式清除
		System.out.println(cache.stats());
		
		// 缓存的数量
		System.out.println(cache.size());
		
		System.out.println(cache.asMap()); // {e=-1, b=20, a=10}
		cache.invalidate("e"); // 显式地清除指定key的缓存对象
		System.out.println(cache.asMap()); // {b=20, a=10}
		
		// 清除指定key集合缓存
		cache.invalidateAll(Arrays.asList("a", "b", "x"));
		
		cache.invalidateAll(); // 清楚所有缓存
		System.out.println(cache.asMap());
		
		Thread.sleep(1000 * 10);
		
		// 显式插入，手动push数据
		cache.put("z", 11);
		cache.putAll(ImmutableMap.<String, Integer>of("xx", 22, "zz", 33));
		System.out.println(cache.asMap());
		
		/**
		 * 刷新指定key的缓存对象,刷新和回收不太一样。
		 * 刷新表示为键加载新值，这个过程可以是异步的。
		 * 在刷新操作进行时，缓存仍然可以向其他线程返回旧值，而不像回收操作，读缓存的线程必须等待新值加载完成。
		 * 如果刷新过程抛出异常，缓存将保留旧值，而异常会在记录到日志后被丢弃[swallowed]。
		 * 重载CacheLoader.reload可以扩展刷新时的行为，这个方法允许开发者在计算新值时使用旧的值
		 */
		cache.refresh("z");
		
		// 清理缓存，根据不同的回收策略进行回收
		cache.cleanUp();
	}
	
	public void testSizeBasedEvictionCacheGC() throws InterruptedException {
		
		//测试定时回收
		//对定时回收进行测试时，不一定非得花费两秒钟去测试两秒的过期。
		//你可以使用Ticker接口和CacheBuilder.ticker(Ticker)方法在缓存中自定义一个时间源，而不是非得用系统时钟。
		
		CacheBuilder.newBuilder().ticker(new Ticker() {
			@Override
			public long read() {
				return 1L;
			}
		});
	}

	/**
	 * 使用CacheBuilder构建的缓存不会"自动"执行清理和回收工作，也不会在某个缓存项过期后马上清理，也没有诸如此类的清理机制。
	 * 相反，它会在写操作时顺带做少量的维护工作，或者偶尔在读操作时做——如果写操作实在太少的话。<br/>
	 * 这样做的原因在于：如果要自动地持续清理缓存，就必须有一个线程，这个线程会和用户操作竞争共享锁。
	 * 此外，某些环境下线程创建可能受限制，这样CacheBuilder就不可用了。<br/>
	 * 相反，我们把选择权交到你手里。如果你的缓存是高吞吐的，那就无需担心缓存的维护和清理等工作。<br/>
	 * 如果你的 缓存只会偶尔有写操作，而你又不想清理工作阻碍了读操作，那么可以创建自己的维护线程，以固定的时间间隔调用Cache.cleanUp()。
	 * ScheduledExecutorService可以帮助你很好地实现这样的定时调度。
	 */
	@Test
	public void testCleanCache() {
		
		long a = 2 & 3 & 4;
		System.out.println(a);
	}
}
