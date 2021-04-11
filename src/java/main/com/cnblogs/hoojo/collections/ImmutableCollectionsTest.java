package com.cnblogs.hoojo.collections;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.collect.*;
import org.junit.Test;

import java.util.*;

/**
 * 不可变集合
 * 
 * <table cellspacing="0" cellpadding="0" border="1">
<tbody>
<tr>
<td width="184"><b>可变集合接口</b><b></b></td>
<td width="158" valign="top"><b>属于</b><b>JDK</b><b>还是</b><b>Guava</b><b></b></td>
<td width="277"><b>不可变版本</b><b></b></td>
</tr>
<tr>
<td width="184">Collection</td>
<td width="158">JDK</td>
<td width="277"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/ImmutableCollection.html"><tt>ImmutableCollection</tt></a></td>
</tr>
<tr>
<td width="184">List</td>
<td width="158">JDK</td>
<td width="277"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/ImmutableList.html"><tt>ImmutableList</tt></a></td>
</tr>
<tr>
<td width="184">Set</td>
<td width="158">JDK</td>
<td width="277"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/ImmutableSet.html"><tt>ImmutableSet</tt></a></td>
</tr>
<tr>
<td width="184">SortedSet/NavigableSet</td>
<td width="158">JDK</td>
<td width="277"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/ImmutableSortedSet.html"><tt>ImmutableSortedSet</tt></a></td>
</tr>
<tr>
<td width="184">Map</td>
<td width="158">JDK</td>
<td width="277"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/ImmutableMap.html"><tt>ImmutableMap</tt></a></td>
</tr>
<tr>
<td width="184">SortedMap</td>
<td width="158">JDK</td>
<td width="277"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/ImmutableSortedMap.html"><tt>ImmutableSortedMap</tt></a></td>
</tr>
<tr>
<td width="184"><a href="http://code.google.com/p/guava-libraries/wiki/NewCollectionTypesExplained#Multiset">Multiset</a></td>
<td width="158">Guava</td>
<td width="277"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/ImmutableMultiset.html"><tt>ImmutableMultiset</tt></a></td>
</tr>
<tr>
<td width="184">SortedMultiset</td>
<td width="158">Guava</td>
<td width="277"><a href="http://docs.guava-libraries.googlecode.com/git-history/release12/javadoc/com/google/common/collect/ImmutableSortedMultiset.html"><tt>ImmutableSortedMultiset</tt></a></td>
</tr>
<tr>
<td width="184"><a href="http://code.google.com/p/guava-libraries/wiki/NewCollectionTypesExplained#Multimap">Multimap</a></td>
<td width="158">Guava</td>
<td width="277"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/ImmutableMultimap.html"><tt>ImmutableMultimap</tt></a></td>
</tr>
<tr>
<td width="184">ListMultimap</td>
<td width="158">Guava</td>
<td width="277"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/ImmutableListMultimap.html"><tt>ImmutableListMultimap</tt></a></td>
</tr>
<tr>
<td width="184">SetMultimap</td>
<td width="158">Guava</td>
<td width="277"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/ImmutableSetMultimap.html"><tt>ImmutableSetMultimap</tt></a></td>
</tr>
<tr>
<td width="184"><a href="http://code.google.com/p/guava-libraries/wiki/NewCollectionTypesExplained#BiMap">BiMap</a></td>
<td width="158">Guava</td>
<td width="277"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/ImmutableBiMap.html"><tt>ImmutableBiMap</tt></a></td>
</tr>
<tr>
<td width="184"><a href="http://code.google.com/p/guava-libraries/wiki/NewCollectionTypesExplained#ClassToInstanceMap">ClassToInstanceMap</a></td>
<td width="158">Guava</td>
<td width="277"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/ImmutableClassToInstanceMap.html"><tt>ImmutableClassToInstanceMap</tt></a></td>
</tr>
<tr>
<td width="184"><a href="http://code.google.com/p/guava-libraries/wiki/NewCollectionTypesExplained#Table">Table</a></td>
<td width="158">Guava</td>
<td width="277"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/ImmutableTable.html"><tt>ImmutableTable</tt></a></td>
</tr>
</tbody>
</table>
 * 
 * @author hoojo
 * @createDate 2017年10月13日 下午3:13:32
 * @file ImmutableCollectionsTest.java
 * @package com.cnblogs.hoojo.collections
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class ImmutableCollectionsTest extends BasedTest {

	// 创建不可变集合
	@Test
	public void testCreate() {
		// copyOf 
		try {
			Set<String> sets = ImmutableSet.<String>copyOf(new String[] { "a", "b" });
			out(sets); // [a, b]
			
			sets.add("d"); // throw UnsupportedOperationException 不可变不能添加对象
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// of
		try {
			Set<String> sets = ImmutableSet.of("a", "c");
			out(sets); // [a, c]
			
			sets.add("d"); // throw UnsupportedOperationException 不可变不能添加对象
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// builder
		try {
			Set<String> sets = ImmutableSet.<String>builder().add("a").add("e", "f").addAll(Arrays.asList("1", "2")).build();
			out(sets); // [a, e, f, 1, 2]
			
			sets.add("d"); // throw UnsupportedOperationException 不可变不能添加对象
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 不可变集合排序
	@Test
	public void testSort() {
		// 不可变集合不能排序
		try {
			List<String> list = ImmutableList.<String>copyOf(new String[] { "0", "a", "b", "1", "2" });
			out(list); // [0, a, b, 1, 2]
			
			Collections.sort(list, Ordering.<String>natural()); // throw UnsupportedOperationException 不可变集合不能排序
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			List<String> list = ImmutableList.<String>copyOf(new String[] { "0", "a", "b", "1", "2" });
			out(list); // [0, a, b, 1, 2]
			
			// copy 不可变集合并进行排序
			out(Ordering.<String>natural().immutableSortedCopy(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 不可变集合不能排序
		try {
			ImmutableSet<String> sets = ImmutableSet.of("0", "a", "b", "1", "2");
			out(sets); // [0, 1, 2, a, b]
			
			List<String> list = sets.asList();
			Collections.sort(list, Ordering.<String>natural());
			
			out(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testImmutableCollections() {
		
		// collection
		ImmutableCollection<Integer> collection = ImmutableSet.<Integer>of(4, 1, 2, 3);
		out(collection); // [4, 1, 2, 3]
		// 转换为ImmutableList
		out(collection.asList()); //[4, 1, 2, 3]
		// 长度
		out(collection.size()); // 4
		// iterator 迭代器
		out(collection.iterator().hasNext());
		collection.forEach(x -> out("print: " + x));
		
		// list -> jdk -> ImmutableList
		List<Integer> list = ImmutableList.sortedCopyOf(collection);
		out(list); // [1, 2, 3, 4]
		
		// set -> jdk -> ImmutableSet
		Set<Integer> set = ImmutableSet.<Integer>of(4, 1, 2, 3, 3);
		out(set); // [4, 1, 2, 3]
		
		//SortedSet/NavigableSet-> jdk -> ImmutableSortedSet
		SortedSet<Integer> sortedSet = ImmutableSortedSet.<Integer>of(4, 1, 2, 3, 3);
		out(sortedSet); // [1, 2, 3, 4]
		
		// map-> jdk -> ImmutableMap
		Map<String, Integer> map = ImmutableMap.<String, Integer>of("key", 30);
		out(map); // {key=30}
		
		// SortedMap-> jdk -> ImmutableSortedMap
		SortedMap<String, Integer> sortedMap = ImmutableSortedMap.<String, Integer>copyOf(map);
		out(sortedMap); // {key=30}
		
		// Multiset 	Guava 	ImmutableMultiset
		Multiset<Integer> multiset = ImmutableMultiset.<Integer>of(1, 2, 3, 1, 3);
		out(multiset); // [1 x 2, 2, 3 x 2]
		
		// SortedMultiset 	Guava 	ImmutableSortedMultiset 有序
		SortedMultiset<Integer> sortedMultiset = ImmutableSortedMultiset.<Integer>of(4, 1, 2, 3, 1, 3);
		out(sortedMultiset); // [1 x 2, 2, 3 x 2, 4]
		
		// Multimap 	Guava 	ImmutableMultimap
		Multimap<Integer, Integer> multimap = ImmutableMultimap.<Integer, Integer>of(4, 1, 2, 3, 4, 0);
		out(multimap); // {4=[1, 0], 2=[3]}
		
		// ListMultimap 	Guava 	ImmutableListMultimap //List
		ListMultimap<Integer, Integer> listMultimap = ImmutableListMultimap.<Integer, Integer>of(4, 1, 2, 3, 4, 0, 4, 0);
		out(listMultimap); // {4=[1, 0, 0], 2=[3]}
		
		// SetMultimap 	Guava 	ImmutableSetMultimap set构造去重
		SetMultimap<Integer, Integer> setMultimap = ImmutableSetMultimap.<Integer, Integer>of(4, 1, 2, 3, 4, 0, 4, 0);
		out(setMultimap); // {4=[1, 0], 2=[3]}
		
		// BiMap 	Guava 	ImmutableBiMap
		BiMap<Integer, Integer> bimap = ImmutableBiMap.<Integer, Integer>of(4, 1, 2, 3, 5, 0);
		out(bimap); // {4=1, 2=3, 5=0}
		
		// ClassToInstanceMap 	Guava 	ImmutableClassToInstanceMap
		ClassToInstanceMap<String> instanceMap = ImmutableClassToInstanceMap.of(String.class, "--");
		out(instanceMap); // {class java.lang.String=--}
		
		// Table 	Guava 	ImmutableTable
		Table<String, String, Integer> table = ImmutableTable.<String, String, Integer>builder().put(Tables.immutableCell("2", "3", 2)).build();
		Table<String, String, Integer> table2 = ImmutableTable.<String, String, Integer>of("2", "3", 2).of("2", "3", 2);
		out(table); // {2={3=2}}
		out(table2); // {2={3=2}}
	}
}
