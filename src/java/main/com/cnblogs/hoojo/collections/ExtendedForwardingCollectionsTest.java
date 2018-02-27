package com.cnblogs.hoojo.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Sets;

/**
 * <b>function:</b> 集合扩展工具类

<table>
<thead>
<tr>
<th align="left">Interface</th>
<th align="left">Forwarding Decorator</th>
</tr>
</thead>
<tbody>
<tr>
<td align="left"><code>Collection</code></td>
<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/ForwardingCollection.html"><code>ForwardingCollection</code></a></td>
</tr>
<tr>
<td align="left"><code>List</code></td>
<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/ForwardingList.html"><code>ForwardingList</code></a></td>
</tr>
<tr>
<td align="left"><code>Set</code></td>
<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/ForwardingSet.html"><code>ForwardingSet</code></a></td>
</tr>
<tr>
<td align="left"><code>SortedSet</code></td>
<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/ForwardingSortedSet.html"><code>ForwardingSortedSet</code></a></td>
</tr>
<tr>
<td align="left"><code>Map</code></td>
<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/ForwardingMap.html"><code>ForwardingMap</code></a></td>
</tr>
<tr>
<td align="left"><code>SortedMap</code></td>
<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/ForwardingSortedMap.html"><code>ForwardingSortedMap</code></a></td>
</tr>
<tr>
<td align="left"><code>ConcurrentMap</code></td>
<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/ForwardingConcurrentMap.html"><code>ForwardingConcurrentMap</code></a></td>
</tr>
<tr>
<td align="left"><code>Map.Entry</code></td>
<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/ForwardingMapEntry.html"><code>ForwardingMapEntry</code></a></td>
</tr>
<tr>
<td align="left"><code>Queue</code></td>
<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/ForwardingQueue.html"><code>ForwardingQueue</code></a></td>
</tr>
<tr>
<td align="left"><code>Iterator</code></td>
<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/ForwardingIterator.html"><code>ForwardingIterator</code></a></td>
</tr>
<tr>
<td align="left"><code>ListIterator</code></td>
<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/ForwardingListIterator.html"><code>ForwardingListIterator</code></a></td>
</tr>
<tr>
<td align="left"><code>Multiset</code></td>
<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/ForwardingMultiset.html"><code>ForwardingMultiset</code></a></td>
</tr>
<tr>
<td align="left"><code>Multimap</code></td>
<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/ForwardingMultimap.html"><code>ForwardingMultimap</code></a></td>
</tr>
<tr>
<td align="left"><code>ListMultimap</code></td>
<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/ForwardingListMultimap.html"><code>ForwardingListMultimap</code></a></td>
</tr>
<tr>
<td align="left"><code>SetMultimap</code></td>
<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/ForwardingSetMultimap.html"><code>ForwardingSetMultimap</code></a></td>
</tr>
</tbody>
</table>

 * @author hoojo
 * @createDate 2017年10月19日 下午4:20:14
 * @file ExtendedForwardingCollectionsTest.java
 * @package com.cnblogs.hoojo.collections
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ExtendedForwardingCollectionsTest {

	// 自定义Set
	class MyGuavaSet<E> extends ForwardingSet<E> {

		private Set<E> delegate = Sets.newHashSet();
		
		// 源对象实现的模式
		@Override
		protected Set<E> delegate() {
			return delegate;
		}
		
		// 覆盖的方法
		@Override
		public String toString() {
			return "MyGuavaSet: " + super.toString();
		}
		
		// 扩展的方法
		public boolean add(@SuppressWarnings("unchecked") E... elements) {
			
			for (E e : elements) {
				super.add(e);
			}
			
			System.out.println(standardToString());
			return true;
		}
		
		// 扩展的方法
		public void print() {
			super.forEach(x -> System.out.println("elements: " + x));
		}
	}
	
	@Test
	public void testForwardingSet() {
		
		MyGuavaSet<Integer> sets = new MyGuavaSet<Integer>();
		sets.add(1);
		sets.add(2, 3, 4);
		
		sets.print();
		System.out.println(sets.toString()); // MyGuavaSet: [1, 2, 3, 4]
	}
	
	@SuppressWarnings("all")
	class MyGuavaValueSortedMap<K, V extends Integer> extends ForwardingMap<K, V> {

		final Map<K, V> delegate = new HashMap<K, V>();
		
		@Override
		protected Map<K, V> delegate() {
			return delegate;
		}
		
		public MyGuavaValueSortedMap(K k, V v, K k2, V v2) {
			put(k, v);
			put(k2, v2);
		}
		
		@Override
		public String toString() {
	        //这里将map.entrySet()转换成list
	        List<Map.Entry<K, V>> list = new ArrayList<Map.Entry<K, V>>(delegate.entrySet());
	        list.sort((Entry<K, V> x, Entry<K, V> y) -> ComparisonChain.start().compare(x.getValue(), y.getValue()).result());
			
			return list.toString();
		}
	}
	
	@Test
	public void testForwardingMap() {
		
		MyGuavaValueSortedMap<String, Integer> map = new MyGuavaValueSortedMap<String, Integer>("a", 1, "b", 3);
		map.put("d", 10);
		map.put("z", 5);
		map.put("x", 7);
		
		System.out.println(map.toString()); // [a=1, b=3, z=5, x=7, d=10]
	}
}
