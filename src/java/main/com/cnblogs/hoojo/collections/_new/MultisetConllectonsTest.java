package com.cnblogs.hoojo.collections._new;

import java.util.Arrays;

import org.junit.Test;

import com.google.common.collect.BoundType;
import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSortedMultiset;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultiset;

/**
 * <b>function:</b> 新集合对象


API：
<table cellspacing="0" cellpadding="0" border="1">
<tbody>
<tr>
<td width="142"><b>方法</b><b></b></td>
<td width="470"><b>描述</b><b></b></td>
</tr>
<tr>
<td width="142"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/Multiset.html#count(java.lang.Object)">count(E)</a><b></b></td>
<td width="470">给定元素在Multiset中的计数</td>
</tr>
<tr>
<td width="142"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/Multiset.html#elementSet()">elementSet()</a></td>
<td width="470">Multiset中不重复元素的集合，类型为Set&lt;E&gt;</td>
</tr>
<tr>
<td width="142"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/Multiset.html#entrySet()">entrySet()</a></td>
<td width="470">和Map的entrySet类似，返回Set&lt;Multiset.Entry&lt;E&gt;&gt;，其中包含的Entry支持getElement()和getCount()方法</td>
</tr>
<tr>
<td width="142"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/Multiset.html#add(java.lang.Object,int)">add(E, int)</a></td>
<td width="470">增加给定元素在Multiset中的计数</td>
</tr>
<tr>
<td width="142"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Multiset.html#remove(java.lang.Object, int)">remove(E, int)</a></td>
<td width="470">减少给定元素在Multiset中的计数</td>
</tr>
<tr>
<td width="142"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Multiset.html#setCount(E, int)">setCount(E, int)</a></td>
<td width="470">设置给定元素在Multiset中的计数，不可以为负数</td>
</tr>
<tr>
<td width="142">size()</td>
<td width="470">返回集合元素的总个数（包括重复的元素）</td>
</tr>
</tbody>
</table>

implement：
<table width="614" cellspacing="0" cellpadding="0" border="1">
<tbody>
<tr>
<td width="161"><b>Map</b></td>
<td width="198"><b>对应的</b><b>Multiset</b><b></b></td>
<td width="255" valign="top"><b>是否支持</b><b>null</b><b>元素</b><b></b></td>
</tr>
<tr>
<td width="161">HashMap</td>
<td width="198"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/HashMultiset.html">HashMultiset</a></td>
<td width="255" valign="top">是</td>
</tr>
<tr>
<td width="161">TreeMap</td>
<td width="198"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/TreeMultiset.html">TreeMultiset</a></td>
<td width="255" valign="top">是（如果comparator支持的话）</td>
</tr>
<tr>
<td width="161">LinkedHashMap</td>
<td width="198"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/LinkedHashMultiset.html">LinkedHashMultiset</a></td>
<td width="255" valign="top">是</td>
</tr>
<tr>
<td width="161">ConcurrentHashMap</td>
<td width="198"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/ConcurrentHashMultiset.html">ConcurrentHashMultiset</a></td>
<td width="255" valign="top">否</td>
</tr>
<tr>
<td width="161">ImmutableMap</td>
<td width="198"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/ImmutableMultiset.html">ImmutableMultiset</a></td>
<td width="255" valign="top">否</td>
</tr>
</tbody>
</table>

 * @author hoojo
 * @createDate 2017年10月13日 下午3:51:06
 * @file NewConllectonsTest.java
 * @package com.cnblogs.hoojo.collections
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class MultisetConllectonsTest {

	@Test
	public void testMultisetAPI() {
		try {
		
			HashMultiset<String> hash = HashMultiset.<String>create();
			
			hash.add("home");
			hash.add("china", 2);
			hash.addAll(Arrays.asList("world", "java", "world"));
			
			System.out.println(hash); // [world x 2, java, china x 2, home]
			
			// 总数量、长度
			System.out.println(hash.size()); // 6
			// 去掉重复的元素
			System.out.println(hash.elementSet()); // [world, java, china, home]
			// entrySet 元素的集合
			System.out.println(hash.entrySet()); // [world x 2, java, china x 2, home]

			// 查看元素 java的数量
			System.out.println(hash.count("java")); // 1
			
			hash.setCount("java", 5); // 设置 java 元素数量为 5
			System.out.println(hash.count("java"));  // 5
			
			// 设置home x 1 的元素个数为3
			System.out.println(hash.setCount("home", 1, 3)); // true
			System.out.println(hash); // [world x 2, java x 5, china x 2, home x 3]
			
			hash.remove("java"); // 删除1个元素
			System.out.println(hash.count("java")); // 4
			
			hash.remove("java", 2); // 删除2个元素
			System.out.println(hash.count("java")); // 2
			
			hash.setCount("home", 0); // 等于 删除 home
			hash.forEach(s -> System.out.println(s));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testTreeMultiset() {
		try {
			TreeMultiset<String> tree = TreeMultiset.<String>create();
			
			/*TreeMultiset<String> tree = TreeMultiset.<String>create(Ordering.from(new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return -ComparisonChain.start().compare(o1, o2).result();
				}
			}));*/
			
			tree.add("home");
			tree.add("china", 2);
			tree.addAll(Arrays.asList("world", "java", "world", "zbus", "zday", "zday"));
			
			System.out.println("tree: " + tree); // [china x 2, home, java, world x 2, zbus, zday x 2]
			System.out.println(tree.descendingMultiset()); // 倒序 [zday x 2, zbus, world x 2, java, home, china x 2]
			
			System.out.println("------------截取元素--------");
			System.out.println(tree); // [china x 2, home, java, world x 2, zbus, zday x 2]
			// 取从头部到指定元素区间的元素(上限)
			System.out.println(tree.headMultiset("java", BoundType.CLOSED)); // 闭区间  <= java  [china x 2, home, java]
			System.out.println(tree.headMultiset("java", BoundType.OPEN)); // 开区间 < java   [china x 2, home]
			
			System.out.println(tree.tailMultiset("java", BoundType.CLOSED)); // 闭区间  >= java	[java, world x 2, zbus, zday x 2]
			System.out.println(tree.tailMultiset("java", BoundType.OPEN)); // 开区间 > java		[world x 2, zbus, zday x 2]
			
			System.out.println(tree.subMultiset("home", BoundType.CLOSED, "world", BoundType.CLOSED)); // [home, java, world x 2] 获取 home 到 world 元素中间的节点，包含起始元素
			System.out.println(tree.subMultiset("home", BoundType.OPEN, "world", BoundType.OPEN)); // [java] 获取 home 到 world 元素中间的节点
			System.out.println("------------截取元素--------");
			
			System.out.println(tree.firstEntry()); // china x 2 取到首个元素
			
			System.out.println(tree.pollFirstEntry()); // china x 2 拉出首个元素
			System.out.println("tree: " + tree); // [home, java, world x 2, zbus, zday x 2]
			
			System.out.println(tree.pollLastEntry()); // zday x 2 拉出最后一个元素
			System.out.println("tree: " + tree); // [home, java, world x 2, zbus]
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testMultiset() {
			
		// 有序
		try {
			LinkedHashMultiset<Integer> linked = LinkedHashMultiset.<Integer>create(Arrays.asList(2, 3, 5, 1, 7, 2, 3));
			
			linked.add(1);
			linked.addAll(Lists.newArrayList(9, 99, 55));
			
			System.out.println(linked); // [2 x 2, 3 x 2, 5, 1 x 2, 7, 9, 99, 55]
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 多线程
		try {
			ConcurrentHashMultiset<Integer> concurrent = ConcurrentHashMultiset.<Integer>create(Arrays.asList(2, 3, 5, 1, 7, 2, 3));
			
			concurrent.add(1);
			concurrent.addAll(Lists.newArrayList(9, 99, 55));
			
			System.out.println(concurrent); // [1 x 2, 2 x 2, 3 x 2, 99, 5, 7, 55, 9]
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 不可变
		try {
			ImmutableMultiset<Integer> immutable = ImmutableMultiset.<Integer>builder().add(2, 3, 5, 1, 7, 2, 3).build();
			
			System.out.println(immutable); // [2 x 2, 3 x 2, 5, 1, 7]
			System.out.println(immutable.asList()); // [2, 2, 3, 3, 5, 1, 7]
			// immutable.of(2, 3);
			
			// 排序
			ImmutableSortedMultiset<Integer> sort = ImmutableSortedMultiset.<Integer>of(2, 3, 5, 1, 7, 2, 3);
			
			System.out.println(sort); // [1, 2 x 2, 3 x 2, 5, 7]
			System.out.println(sort.asList()); // [1, 2, 2, 3, 3, 5, 7]
			
			// 逆序
			sort = ImmutableSortedMultiset.orderedBy(Ordering.<Integer>natural().reverse()).add(2, 3, 5, 1, 7, 2, 3).build();
			System.out.println(sort); // [7, 5, 3 x 2, 2 x 2, 1]
			
			sort = ImmutableSortedMultiset.<Integer>reverseOrder().add(2, 3, 5, 1, 7, 2, 3, 77, 99).build();
			System.out.println(sort); // [99, 77, 7, 5, 3 x 2, 2 x 2, 1]
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Multisets
	}
}
