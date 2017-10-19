package com.cnblogs.hoojo.collections;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import org.junit.Test;

import com.google.common.collect.BiMap;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedMultiset;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;

/**
 * <b>function:</b> 不可变集合
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
public class ImmutableCollectionsTest {

	// 创建不可变集合
	@Test
	public void testCreate() {
		// copyOf 
		try {
			Set<String> sets = ImmutableSet.<String>copyOf(new String[] { "a", "b" });
			System.out.println(sets);
			
			sets.add("d"); // throw UnsupportedOperationException 不可变不能添加对象
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// of
		try {
			Set<String> sets = ImmutableSet.of("a", "c");
			System.out.println(sets);
			
			sets.add("d"); // throw UnsupportedOperationException 不可变不能添加对象
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// builder
		try {
			Set<String> sets = ImmutableSet.<String>builder().add("a").add("e", "f").addAll(Arrays.asList("1", "2")).build();
			System.out.println(sets);
			
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
			System.out.println(list);
			
			Collections.sort(list, Ordering.<String>natural()); // throw UnsupportedOperationException 不可变集合不能排序
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			List<String> list = ImmutableList.<String>copyOf(new String[] { "0", "a", "b", "1", "2" });
			System.out.println(list);
			
			// copy 不可变集合并进行排序
			System.out.println(Ordering.<String>natural().immutableSortedCopy(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 不可变集合不能排序
		try {
			ImmutableSet<String> sets = ImmutableSet.of("0", "a", "b", "1", "2");
			System.out.println(sets);
			
			List<String> list = sets.asList();
			Collections.sort(list, Ordering.<String>natural());
			
			System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testImmutableCollections() {
		
		// collection
		ImmutableCollection<Integer> collection = ImmutableSet.<Integer>of(4, 1, 2, 3);
		System.out.println(collection);
		// 转换为ImmutableList
		System.out.println(collection.asList());
		// 长度
		System.out.println(collection.size());
		// iterator 迭代器
		System.out.println(collection.iterator().hasNext());
		collection.forEach(x -> System.out.println("print: " + x));
		
		// list -> jdk -> ImmutableList
		List<Integer> list = ImmutableList.sortedCopyOf(collection);
		System.out.println(list);
		
		// set -> jdk -> ImmutableSet
		Set<Integer> set = ImmutableSet.<Integer>of(4, 1, 2, 3, 3);
		System.out.println(set);
		
		//SortedSet/NavigableSet-> jdk -> ImmutableSortedSet
		SortedSet<Integer> sortedSet = ImmutableSortedSet.<Integer>of(4, 1, 2, 3, 3);
		System.out.println(sortedSet);
		
		// map-> jdk -> ImmutableMap
		Map<String, Integer> map = ImmutableMap.<String, Integer>of("key", 30);
		System.out.println(map);
		
		// SortedMap-> jdk -> ImmutableSortedMap
		SortedMap<String, Integer> sortedMap = ImmutableSortedMap.<String, Integer>copyOf(map);
		System.out.println(sortedMap);
		
		// Multiset 	Guava 	ImmutableMultiset
		Multiset<Integer> multiset = ImmutableMultiset.<Integer>of(1, 2, 3, 1, 3);
		System.out.println(multiset);
		
		// SortedMultiset 	Guava 	ImmutableSortedMultiset 有序
		SortedMultiset<Integer> sortedMultiset = ImmutableSortedMultiset.<Integer>of(4, 1, 2, 3, 1, 3);
		System.out.println(sortedMultiset);
		
		// Multimap 	Guava 	ImmutableMultimap
		Multimap<Integer, Integer> multimap = ImmutableMultimap.<Integer, Integer>of(4, 1, 2, 3, 4, 0);
		System.out.println(multimap);
		
		// ListMultimap 	Guava 	ImmutableListMultimap //List
		ListMultimap<Integer, Integer> listMultimap = ImmutableListMultimap.<Integer, Integer>of(4, 1, 2, 3, 4, 0, 4, 0);
		System.out.println(listMultimap);
		
		// SetMultimap 	Guava 	ImmutableSetMultimap set构造去重
		SetMultimap<Integer, Integer> setMultimap = ImmutableSetMultimap.<Integer, Integer>of(4, 1, 2, 3, 4, 0, 4, 0);
		System.out.println(setMultimap);
		
		// BiMap 	Guava 	ImmutableBiMap
		BiMap<Integer, Integer> bimap = ImmutableBiMap.<Integer, Integer>of(4, 1, 2, 3, 5, 0);
		System.out.println(bimap);
		
		// ClassToInstanceMap 	Guava 	ImmutableClassToInstanceMap
		ClassToInstanceMap<String> instanceMap = ImmutableClassToInstanceMap.of(String.class, "--");
		System.out.println(instanceMap);
		
		// Table 	Guava 	ImmutableTable
		Table<String, String, Integer> table = ImmutableTable.<String, String, Integer>builder().put(Tables.immutableCell("2", "3", 2)).build();
		Table<String, String, Integer> table2 = ImmutableTable.<String, String, Integer>of("2", "3", 2).of("2", "3", 2);
		System.out.println(table);
		System.out.println(table2);
	}
}
