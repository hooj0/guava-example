package com.cnblogs.hoojo.collections.utility_classes;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Supplier;
import com.google.common.collect.*;
import com.google.common.primitives.Ints;
import org.junit.Test;

import java.util.*;

/**
 * 原生集合工具类

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
@SuppressWarnings({ "ALL", "AlibabaAvoidCommentBehindStatement" })
public class CollectionTest extends BasedTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testUtils() {
		/** FluentIterable （链式调用）语法 */
		// 转换为 List
		print(FluentIterable.of(3, 5).append(6).toList());	//[3, 5, 6]
		// 转换为 Multiset
		print(FluentIterable.of(3, 5).append(6).toMultiset());	//[3, 5, 6]
		// 转换为 Set
		print(FluentIterable.of(3, 5).append(6).toSet());	//[3, 5, 6]
		
		// from 构造
		print(FluentIterable.from(new Integer[] { 3, 4, 5 }).join(Joiner.on(";")));	//3;4;5
		
		Set<Integer> sets = Sets.newHashSet();
		// copyInto 拷贝
		print(FluentIterable.concat(Lists.newArrayList(2, 3, 4, 5)).copyInto(sets)); //[2, 3, 4, 5]
		print(sets);	//[2, 3, 4, 5]
		
		// 转为不可变map
		print(FluentIterable.of(3, 5).uniqueIndex(x -> "key_" + x)); //{key_3=3, key_5=5}
		print(FluentIterable.of(3, 5, 3, 1).stream().distinct().count()); //3
	}
	
	@Test
	public void testLists() {
		/** Lists */
		// 分组、分片、分块
		print(Lists.partition(Lists.newArrayList(2, 3, 5, 6, 7), 2)); //[[2, 3], [5, 6], [7]]
		// 反转排序
		print(Lists.reverse(Lists.newArrayList(2, 3, 5, 6, 7))); //[7, 6, 5, 3, 2]
		// list 进行交叉组合，笛卡尔
		print(Lists.cartesianProduct(Lists.newArrayList(2, 3, 5), Lists.newArrayList(12, 13))); //[[2, 12], [2, 13], [3, 12], [3, 13], [5, 12], [5, 13]]
		// 并发集合类
		Lists.newCopyOnWriteArrayList().add(2);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSets() {
		/** Sets */
		// 集合1和集合2的差集、比较不同
		print(Sets.difference(Sets.newHashSet(2, 3, 4), Sets.newHashSet(2, 4, 6))); //[3]
		// 并集
		print(Sets.union(Sets.newHashSet(2, 3, 4), Sets.newHashSet(2, 4, 6))); //[2, 3, 4, 6]
		// 交集
		print(Sets.intersection(Sets.newHashSet(2, 3, 4), Sets.newHashSet(2, 4, 6))); //[2, 4]
		// 两个集合差集的并集
		print(Sets.symmetricDifference(Sets.newHashSet(2, 3, 4), Sets.newHashSet(2, 4, 6))); //[3, 6]
		
		// 单集合内部自由组合，size是组合的元素数量
		Sets.combinations(Sets.newHashSet(1, 3, 2), 2).forEach(x -> print("combinations-2: " + x));
		/*
		 	combinations-2: [1, 2]
			combinations-2: [1, 3]
			combinations-2: [2, 3]
		 */
		
		Sets.combinations(Sets.newHashSet(1, 3, 2, 5), 3).forEach(x -> print("combinations-3: " + x));
		/*
			combinations-3: [1, 2, 3]
			combinations-3: [1, 2, 5]
			combinations-3: [1, 3, 5]
			combinations-3: [2, 3, 5]
		 */
		
		//返回所有集合的笛卡儿积
		print(Sets.cartesianProduct(Sets.newHashSet(1, 3), Sets.newHashSet(2, 4))); //[[1, 4], [1, 2], [3, 4], [3, 2]]
		
		// PowerSet 密集集合 （将每个元素进行交叉组合成子集）
		Sets.powerSet(Sets.newHashSet("a", "z", 4)).forEach(System.out::println);
		/*
			[]
			[a]
			[z]
			[a, z]
			[4]
			[a, 4]
			[z, 4]
			[a, z, 4]
		*/
	}
	
	@Test
	public void testMaps() {
		/** Maps **/
		print(Maps.asMap(Sets.newHashSet("a", "b"), x -> x + "-cache")); //{a=a-cache, b=b-cache}
		
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
		print(diff.entriesDiffering()); //{d=(2, 4)}
		// 两个Map中都有的映射项，包括匹配的键与值
		print(diff.entriesInCommon()); //{a=1}
		// 键只存在于左边Map的映射项
		print(diff.entriesOnlyOnLeft()); //{b=2}
		// 键只存在于右边Map的映射项
		print(diff.entriesOnlyOnRight()); //{c=3}
		
	}
	
	@Test
	public void testMultiset() {
		/** Multiset */
		Multiset<String> foo = HashMultiset.create();
		foo.add("a", 2);

		Multiset<String> bar = HashMultiset.create();
		bar.add("a", 5);

		print(foo.containsAll(bar)); //返回true；因为包含了所有不重复元素，
		//虽然foo实际上包含2个"a"，而bar包含5个"a"
		print(Multisets.containsOccurrences(foo, bar)); // returns false
		print(Multisets.removeOccurrences(bar, foo)); // true bar 现在包含3个"a"
		print(Multisets.retainOccurrences(bar, foo)); //true
		
		print(foo); //[a x 2]
		print(bar); //[a x 2]
		
		bar.removeAll(foo);//bar移除所有"a"，虽然foo只有2个"a"
		print(bar); // []
		print(bar.isEmpty()); // returns true

		foo.add("b", 9);
		foo.add("c", 3);
		foo.add("d", 1);
		// 按count数量进行降序排序
		print(Multisets.copyHighestCountFirst(foo)); // [b x 9, c x 3, a x 2, d]
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
		print(Multimaps.index(Arrays.asList("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"), x -> x.length() ));
		//{4=[zero, four, five, nine], 3=[one, two, six], 5=[three, seven, eight]}
		
		// 反转map，将value作为key进行反转
		HashMultimap<String, Integer> hash = HashMultimap.create();
		hash.putAll("a", Ints.asList(5, 3, 1));
		hash.putAll("b", Ints.asList(2, 3, 6));
		hash.putAll("c", Ints.asList(8, 2, 1, 5));
		
		HashMultimap<Integer, String> result = Multimaps.invertFrom(hash, HashMultimap.create());
		print(result); //{1=[a, c], 2=[b, c], 3=[a, b], 5=[a, c], 6=[b], 8=[c]}
		
		// 反转 不可变集合map
		try {
			ImmutableListMultimap<Integer, String> result2 = Multimaps.invertFrom(hash, ImmutableListMultimap.of());
			print(result2); //{5=[a, c], 1=[a, c], 3=[a, b], 2=[b, c], 6=[b], 8=[c]}
		} catch (UnsupportedOperationException e) {
			print(ImmutableListMultimap.copyOf(hash).inverse());
		}
		
		// forMap 将Map对象转换为 Multimap
		ImmutableMap<String, Object> map = ImmutableMap.of("a", 1, "b", 1, "c", 2, "e", "z");
		print(map); //{a=1, b=1, c=2, e=z}
		// 多对一
		SetMultimap<String, Object> multimap = Multimaps.forMap(map);
		print(multimap); //{a=[1], b=[1], c=[2], e=[z]}
		// 一对多
		print(Multimaps.invertFrom(multimap, HashMultimap.create())); //{1=[a, b], 2=[c], z=[e]}
		
		Multimap<String, String> result1 = Multimaps.newListMultimap(Maps.newIdentityHashMap(), new Supplier<LinkedList<String>>() {
			@Override
			public LinkedList<String> get() {
				print("get");
				return Lists.newLinkedList();
			}
		});
		
		result1.put("22", "33");
		result1.put("dd", "123");
		
		print(result1.get("aa").add("333"));
		print(result1.get("aa")); // [333]
		print(result1.keys()); // [dd, aa, 22]
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
		
		print(table); //{2000={1=18900, 2=56000, 3=55000, 4=15000}, 2015={2=11111, 3=20025, 4=400025, 5=20025}, 2017={5=1999}}
		
		// 使用LinkedHashMaps替代HashMaps，将key定义成TreeMap有序排列，Val定义TreeMap
		Table<String, String, Integer> table2 = Tables.newCustomTable(
			Maps.<String, Map<String, Integer>>newTreeMap(), new Supplier<Map<String, Integer>> () {
				public Map<String, Integer> get() {
					return new TreeMap<>();
				}
		});
		table2.putAll(table);
		
		print(table2); //{2000={1=18900, 2=56000, 3=55000, 4=15000}, 2015={2=11111, 3=20025, 4=400025, 5=20025}, 2017={5=1999}}
		
		// 将column和rowKey进行转换
		print(Tables.transpose(table)); //{1={2000=18900}, 2={2000=56000, 2015=11111}, 3={2000=55000, 2015=20025}, 4={2000=15000, 2015=400025}, 5={2015=20025, 2017=1999}}
		
		// 对table的value进行类型或数据转换
		Table<String, String, String> table3 = Tables.transformValues(table, new Function<Integer, String>() {
			@Override
			public String apply(Integer input) {
				
				return input + "元";
			}
		});
		print(table3); //{2000={1=18900元, 2=56000元, 3=55000元, 4=15000元}, 2015={2=11111元, 3=20025元, 4=400025元, 5=20025元}, 2017={5=1999元}}
	}
}
