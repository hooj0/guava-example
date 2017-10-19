package com.cnblogs.hoojo.collections;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Supplier;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import com.google.common.primitives.Ints;

/**
 * <b>function:</b> 集合工具类

<table cellspacing="0" cellpadding="0" border="1">
<tbody>
<tr>
<td width="96"><b>集合接口</b><b></b></td>
<td width="144"><b>属于</b><b>JDK</b><b>还是</b><b>Guava</b></td>
<td width="372"><b>对应的</b><b>Guava</b><b>工具类</b><b></b></td>
</tr>
<tr>
<td width="96">Collection</td>
<td width="144">JDK</td>
<td width="372"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Collections2.html"><tt>Collections2</tt></a>：不要和java.util.Collections混淆</td>
</tr>
<tr>
<td width="96">List</td>
<td width="144">JDK</td>
<td width="372"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/Lists.html"><tt>Lists</tt></a></td>
</tr>
<tr>
<td width="96">Set</td>
<td width="144">JDK</td>
<td width="372"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/Sets.html"><tt>Sets</tt></a></td>
</tr>
<tr>
<td width="96">SortedSet</td>
<td width="144">JDK</td>
<td width="372"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/Sets.html"><tt>Sets</tt></a></td>
</tr>
<tr>
<td width="96">Map</td>
<td width="144">JDK</td>
<td width="372"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/Maps.html"><tt>Maps</tt></a></td>
</tr>
<tr>
<td width="96">SortedMap</td>
<td width="144">JDK</td>
<td width="372"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/Maps.html"><tt>Maps</tt></a></td>
</tr>
<tr>
<td width="96">Queue</td>
<td width="144">JDK</td>
<td width="372"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/Queues.html"><tt>Queues</tt></a></td>
</tr>
<tr>
<td width="96"><a href="http://code.google.com/p/guava-libraries/wiki/NewCollectionTypesExplained#Multiset">Multiset</a></td>
<td width="144">Guava</td>
<td width="372"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Multisets.html"><tt>Multisets</tt></a></td>
</tr>
<tr>
<td width="96"><a href="http://code.google.com/p/guava-libraries/wiki/NewCollectionTypesExplained#Multimap">Multimap</a></td>
<td width="144">Guava</td>
<td width="372"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Multimaps.html"><tt>Multimaps</tt></a></td>
</tr>
<tr>
<td width="96"><a href="http://code.google.com/p/guava-libraries/wiki/NewCollectionTypesExplained#BiMap">BiMap</a></td>
<td width="144">Guava</td>
<td width="372"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Maps.html"><tt>Maps</tt></a></td>
</tr>
<tr>
<td width="96"><a href="http://code.google.com/p/guava-libraries/wiki/NewCollectionTypesExplained#Table">Table</a></td>
<td width="144">Guava</td>
<td width="372"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Tables.html"><tt>Tables</tt></a></td>
</tr>
</tbody>
</table>

 * @author hoojo
 * @createDate 2017年10月16日 下午4:02:22
 * @file UtilCollectionTest.java
 * @package com.cnblogs.hoojo.collections
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class UtilCollectionTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testUtils() {
		/** FluentIterable （链式调用）语法 */
		// 转换为 List
		System.out.println(FluentIterable.of(3, 5).append(6).toList());
		// 转换为 Multiset
		System.out.println(FluentIterable.of(3, 5).append(6).toMultiset());
		// 转换为 Set
		System.out.println(FluentIterable.of(3, 5).append(6).toSet());
		
		// from 构造
		System.out.println(FluentIterable.from(new Integer[] { 3, 4, 5 }).join(Joiner.on(";")));
		
		Set<Integer> sets = Sets.newHashSet();
		// copyInto 拷贝
		System.out.println(FluentIterable.concat(Lists.newArrayList(2, 3, 4, 5)).copyInto(sets));
		System.out.println(sets);
		
		// 转为不可变map
		System.out.println(FluentIterable.of(3, 5).uniqueIndex(x -> "key_" + x));
		System.out.println(FluentIterable.of(3, 5).stream().distinct().count());
	}
	
	@Test
	public void testLists() {
		/** Lists */
		// 分组、分片、分块
		System.out.println(Lists.partition(Lists.newArrayList(2, 3, 5, 6, 7), 2)); 
		// 反转排序
		System.out.println(Lists.reverse(Lists.newArrayList(2, 3, 5, 6, 7)));
		// list 进行交叉组合，笛卡尔
		System.out.println(Lists.cartesianProduct(Lists.newArrayList(2, 3, 5), Lists.newArrayList(12, 13)));
		// 并发集合类
		Lists.newCopyOnWriteArrayList().add(2);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSets() {
		/** Sets */
		// 集合1和集合2的差集、比较不同
		System.out.println(Sets.difference(Sets.newHashSet(2, 3, 4), Sets.newHashSet(2, 4, 6)));
		// 并集
		System.out.println(Sets.union(Sets.newHashSet(2, 3, 4), Sets.newHashSet(2, 4, 6)));
		// 交集
		System.out.println(Sets.intersection(Sets.newHashSet(2, 3, 4), Sets.newHashSet(2, 4, 6)));
		// 两个集合差集的并集
		System.out.println(Sets.symmetricDifference(Sets.newHashSet(2, 3, 4), Sets.newHashSet(2, 4, 6))); 
		
		// 单集合内部自由组合，size是组合的元素数量
		Sets.combinations(Sets.newHashSet(1, 3, 2), 2).forEach(x -> System.out.println("combinations: " + x));
		Sets.combinations(Sets.newHashSet(1, 3, 2, 5), 3).forEach(x -> System.out.println("combinations: " + x));
		
		//返回所有集合的笛卡儿积
		System.out.println(Sets.cartesianProduct(Sets.newHashSet(1, 3), Sets.newHashSet(2, 4)));
		
		// PowerSet 密集集合 （将每个元素进行交叉组合成子集）
		Sets.powerSet(Sets.newHashSet("a", "z", 4)).forEach(System.out::println);
	}
	
	@Test
	public void testMaps() {
		/** Maps **/
		System.out.println(Maps.asMap(Sets.newHashSet("a", "b"), x -> x + "-cache"));
		
		Map<String, Integer> a = Maps.newHashMap();
		a.put("a", 1);
		a.put("b", 2);
		a.put("d", 2);
		Map<String, Integer> b = Maps.newHashMap();
		b.put("c", 3);
		b.put("d", 4);
		b.put("a", 1);
		
		MapDifference<String, Integer> diff = Maps.difference(a, b);
		// 键相同但是值不同值映射项。返回的Map的值类型为MapDifference.ValueDifference，以表示左右两个不同的值
		System.out.println(diff.entriesDiffering());
		// 两个Map中都有的映射项，包括匹配的键与值
		System.out.println(diff.entriesInCommon());
		// 键只存在于左边Map的映射项
		System.out.println(diff.entriesOnlyOnLeft());
		// 键只存在于右边Map的映射项
		System.out.println(diff.entriesOnlyOnRight());
		
	}
	
	@Test
	public void testMultiset() {
		/** Multiset */
		Multiset<String> foo = HashMultiset.create();
		foo.add("a", 2);

		Multiset<String> bar = HashMultiset.create();
		bar.add("a", 5);

		System.out.println(foo.containsAll(bar)); //返回true；因为包含了所有不重复元素，
		//虽然foo实际上包含2个"a"，而bar包含5个"a"
		System.out.println(Multisets.containsOccurrences(foo, bar)); // returns false
		System.out.println(Multisets.removeOccurrences(bar, foo)); // bar 现在包含3个"a"
		System.out.println(Multisets.retainOccurrences(bar, foo));
		
		System.out.println(foo);
		System.out.println(bar);
		
		bar.removeAll(foo);//bar移除所有"a"，虽然foo只有2个"a"
		System.out.println(bar);
		System.out.println(bar.isEmpty()); // returns true

		foo.add("b", 9);
		foo.add("c", 3);
		foo.add("d", 1);
		// 按count数量进行降序排序
		System.out.println(Multisets.copyHighestCountFirst(foo));
	}
	
	/**
	<table border=1>
	<thead>
	<tr>
	<th align="left">Multimap type</th>
	<th align="left">Unmodifiable</th>
	<th align="left">Synchronized</th>
	<th align="left">Custom</th>
	</tr>
	</thead>
	<tbody>
	<tr>
	<td align="left"><code>Multimap</code></td>
	<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/Multimaps.html#unmodifiableMultimap(com.google.common.collect.Multimap)"><code>unmodifiableMultimap</code></a></td>
	<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/Multimaps.html#synchronizedMultimap(com.google.common.collect.Multimap)"><code>synchronizedMultimap</code></a></td>
	<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/Multimaps.html#newMultimap(java.util.Map,%20com.google.common.base.Supplier)"><code>newMultimap</code></a></td>
	</tr>
	<tr>
	<td align="left"><code>ListMultimap</code></td>
	<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/Multimaps.html#unmodifiableListMultimap(com.google.common.collect.ListMultimap)"><code>unmodifiableListMultimap</code></a></td>
	<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/Multimaps.html#synchronizedListMultimap(com.google.common.collect.ListMultimap)"><code>synchronizedListMultimap</code></a></td>
	<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/Multimaps.html#newListMultimap(java.util.Map,%20com.google.common.base.Supplier)"><code>newListMultimap</code></a></td>
	</tr>
	<tr>
	<td align="left"><code>SetMultimap</code></td>
	<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/Multimaps.html#unmodifiableSetMultimap(com.google.common.collect.SetMultimap)"><code>unmodifiableSetMultimap</code></a></td>
	<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/Multimaps.html#synchronizedSetMultimap(com.google.common.collect.SetMultimap)"><code>synchronizedSetMultimap</code></a></td>
	<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/Multimaps.html#newSetMultimap(java.util.Map,%20com.google.common.base.Supplier)"><code>newSetMultimap</code></a></td>
	</tr>
	<tr>
	<td align="left"><code>SortedSetMultimap</code></td>
	<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/Multimaps.html#unmodifiableSortedSetMultimap(com.google.common.collect.SortedSetMultimap)"><code>unmodifiableSortedSetMultimap</code></a></td>
	<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/Multimaps.html#synchronizedSortedSetMultimap(com.google.common.collect.SortedSetMultimap)"><code>synchronizedSortedSetMultimap</code></a></td>
	<td align="left"><a href="http://google.github.io/guava/releases/snapshot/api/docs/com/google/common/collect/Multimaps.html#newSortedSetMultimap(java.util.Map,%20com.google.common.base.Supplier)"><code>newSortedSetMultimap</code></a></td>
	</tr>
	</tbody>
	</table>
	 * <b>function:</b>
	 * @author hoojo
	 * @createDate 2017年10月18日 上午10:47:27
	 */
	@Test
	public void testMulitimaps() {
		/** Multimaps */
		
		// 场景是：有一组对象，它们有共同的特定属性，我们希望按照这个属性的值查询对象，但属性值不一定是独一无二的
		// 对集合进行分组，转换ImmutableListMultimap
		System.out.println(Multimaps.index(Arrays.asList("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"), x -> x.length() ));
		
		// 反转map，将value作为key进行反转
		HashMultimap<String, Integer> hash = HashMultimap.create();
		hash.putAll("a", Ints.asList(5, 3, 1));
		hash.putAll("b", Ints.asList(2, 3, 6));
		hash.putAll("c", Ints.asList(8, 2, 1, 5));
		
		HashMultimap<Integer, String> result = Multimaps.invertFrom(hash, HashMultimap.create());
		System.out.println(result);
		
		// 反转 不可变集合map
		try {
			ImmutableListMultimap<Integer, String> result2 = Multimaps.invertFrom(hash, ImmutableListMultimap.of());
			System.out.println(result2);
		} catch (UnsupportedOperationException e) {
			System.out.println(ImmutableListMultimap.copyOf(hash).inverse());
		}
		
		// forMap 将Map对象转换为 Multimap
		ImmutableMap<String, Object> map = ImmutableMap.of("a", 1, "b", 1, "c", 2, "e", "z");
		System.out.println(map);
		// 多对一
		SetMultimap<String, Object> multimap = Multimaps.forMap(map);
		System.out.println(multimap);
		// 一对多
		System.out.println(Multimaps.invertFrom(multimap, HashMultimap.create()));
		
		Multimap<String, String> result1 = Multimaps.newListMultimap(Maps.newIdentityHashMap(), new Supplier<LinkedList<String>>() {
			@Override
			public LinkedList<String> get() {
				System.out.println("get");
				return Lists.newLinkedList();
			}
		});
		
		result1.put("22", "33");
		result1.put("dd", "123");
		
		System.out.println(result1.get("aa").add("333"));
		System.out.println(result1.get("aa"));
		System.out.println(result1.keys());
	}
	
	@Test
	public void testTables() {
		
		HashBasedTable<String, String, Integer> table = HashBasedTable.create();
		
		table.put("2000", "1", 18900);
		table.put("2000", "2", 56000);
		table.put("2000", "3", 55000);
		table.put("2000", "4", 15000);
		
		table.put("2015", "2", 11111);
		table.put("2015", "3", 20025);
		table.put("2015", "4", 400025);
		table.put("2015", "5", 20025);

		table.put("2017", "5", 1999);
		
		System.out.println(table);
		
		// 使用LinkedHashMaps替代HashMaps，将key定义成TreeMap有序排列，Val定义TreeMap
		Table<String, String, Integer> table2 = Tables.newCustomTable(
			Maps.<String, Map<String, Integer>>newTreeMap(),
			new Supplier<Map<String, Integer>> () {
				public Map<String, Integer> get() {
					return new TreeMap<>();
				}
		});
		table2.putAll(table);
		
		System.out.println(table2);
		
		// 将column和rowKey进行转换
		System.out.println(Tables.transpose(table));
		
		// 对table的value进行类型或数据转换
		Table<String, String, String> table3 = Tables.transformValues(table, new Function<Integer, String>() {
			@Override
			public String apply(Integer input) {
				
				return input + "元";
			}
		});
		System.out.println(table3);
	}
}
