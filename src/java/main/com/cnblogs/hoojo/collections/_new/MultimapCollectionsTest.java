package com.cnblogs.hoojo.collections._new;

import java.util.Arrays;

import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.TreeMultimap;

/**
 * 一键多值集合
Implement：
<table width="614" cellspacing="0" cellpadding="0" border="1">
<tbody>
<tr>
<td width="189"><b>实现</b><b></b></td>
<td width="198"><b>键行为类似</b><b></b></td>
<td width="227" valign="top"><b>值行为类似</b><b></b></td>
</tr>
<tr>
<td width="189"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/ArrayListMultimap.html">ArrayListMultimap</a><b></b></td>
<td width="198">HashMap</td>
<td width="227">ArrayList</td>
</tr>
<tr>
<td width="189"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/HashMultimap.html">HashMultimap</a><b></b></td>
<td width="198">HashMap</td>
<td width="227">HashSet</td>
</tr>
<tr>
<td width="189"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/LinkedListMultimap.html">LinkedListMultimap</a>*<b></b></td>
<td width="198">LinkedHashMap*</td>
<td width="227">LinkedList*</td>
</tr>
<tr>
<td width="189"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/LinkedHashMultimap.html">LinkedHashMultimap</a>**<b></b></td>
<td width="198">LinkedHashMap</td>
<td width="227">LinkedHashMap</td>
</tr>
<tr>
<td width="189"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/TreeMultimap.html">TreeMultimap</a><b></b></td>
<td width="198">TreeMap</td>
<td width="227">TreeSet</td>
</tr>
<tr>
<td width="189"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/ImmutableListMultimap.html"><tt>ImmutableListMultimap</tt></a></td>
<td width="198">ImmutableMap</td>
<td width="227">ImmutableList</td>
</tr>
<tr>
<td width="189"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/ImmutableSetMultimap.html">ImmutableSetMultimap</a><b></b></td>
<td width="198">ImmutableMap</td>
<td width="227">ImmutableSet</td>
</tr>
</tbody>
</table>

API:
<table width="614" cellspacing="0" cellpadding="0" border="1">
<tbody>
<tr>
<td width="132"><b>方法签名</b><b></b></td>
<td width="246"><b>描述</b><b></b></td>
<td width="236" valign="top"><b>等价于</b><b></b></td>
</tr>
<tr>
<td width="132"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Multimap.html#put(K, V)">put(K, V)</a></td>
<td width="246">添加键到单个值的映射</td>
<td width="236" valign="top">multimap.get(key).add(value)</td>
</tr>
<tr>
<td width="132"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Multimap.html#putAll(K, java.lang.Iterable)">putAll(K, Iterable&lt;V&gt;)</a></td>
<td width="246">依次添加键到多个值的映射</td>
<td width="236" valign="top">Iterables.addAll(multimap.get(key), values)</td>
</tr>
<tr>
<td width="132"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Multimap.html#remove(java.lang.Object, java.lang.Object)">remove(K, V)</a></td>
<td width="246">移除键到值的映射；如果有这样的键值并成功移除，返回true。</td>
<td width="236" valign="top">multimap.get(key).remove(value)</td>
</tr>
<tr>
<td width="132"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Multimap.html#removeAll(java.lang.Object)">removeAll(K)</a></td>
<td width="246">清除键对应的所有值，返回的集合包含所有之前映射到K的值，但修改这个集合就不会影响Multimap了。</td>
<td width="236" valign="top">multimap.get(key).clear()</td>
</tr>
<tr>
<td width="132"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Multimap.html#replaceValues(K, java.lang.Iterable)">replaceValues(K, Iterable&lt;V&gt;)</a></td>
<td width="246">清除键对应的所有值，并重新把key关联到Iterable中的每个元素。返回的集合包含所有之前映射到K的值。</td>
<td width="236" valign="top">multimap.get(key).clear(); Iterables.addAll(multimap.get(key), values)</td>
</tr>
</tbody>
</table>

 * 
 * @author hoojo
 * @createDate 2017年10月13日 下午5:51:48
 * @file MultimapCollectionsTest.java
 * @package com.cnblogs.hoojo.collections
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class MultimapCollectionsTest {

	@Test
	public void testApi() {
		
		HashMultimap<String, Integer> hash = HashMultimap.<String, Integer>create();
		
		hash.put("a", 1);
		hash.putAll("b", Arrays.asList(2, 4, 2, 3));
		hash.putAll(hash);
		
		System.out.println(hash); // {a=[1], b=[4, 2, 3]}
		System.out.println(hash.get("b")); // [4, 2, 3] 获取b的值，返回Set集合
		
		System.out.println(hash.containsValue(2)); // true
		System.out.println(hash.containsKey("a")); // true
		System.out.println(hash.containsEntry("b", 4)); // true
		System.out.println(hash.containsEntry("b", 5)); // false
		
		System.out.println(hash.asMap()); // {a=[1], b=[4, 2, 3]} 转换顶级Java集合对象
		
		System.out.println(hash.size()); // 4 总元素数量
		System.out.println(hash.entries()); // [a=1, b=4, b=2, b=3] 所有键值对
		
		System.out.println(hash.keys()); // [a, b x 3] Multiset 键值统计数量
		System.out.println(hash.keySet()); // [a, b] 所有主键
		System.out.println(hash.values()); // [1, 4, 2, 3] 所有值
		
		System.out.println(hash.remove("b", 2)); // true
		System.out.println(hash); // {a=[1], b=[4, 3]}
		
		System.out.println(hash.replaceValues("a", Arrays.asList(33, 44))); // [1]
		System.out.println(hash); // {a=[44, 33], b=[4, 3]}
	}
	
	@Test
	public void testMultimap() {
		// ArrayList
		ArrayListMultimap<String, Integer> list = ArrayListMultimap.<String, Integer>create();
		list.put("a", 1);
		list.putAll("b", Arrays.asList(2, 4, 2, 3));
		
		System.out.println(list); // {a=[1], b=[2, 4, 2, 3]}
		
		// HashSet
		HashMultimap<String, Integer> hash = HashMultimap.<String, Integer>create();
		hash.put("a", 1);
		hash.putAll("b", Arrays.asList(2, 4, 2, 3));
		
		System.out.println(hash); // {a=[1], b=[4, 2, 3]}
		
		// linkedList
		LinkedListMultimap<String, Integer> linkedList = LinkedListMultimap.<String, Integer>create();
		linkedList.put("a", 1);
		linkedList.putAll("b", Arrays.asList(2, 4, 2, 3));
		
		System.out.println(linkedList); // {a=[1], b=[2, 4, 2, 3]}
		
		// LinkedHashMap
		LinkedHashMultimap<String, Integer> linkedhash = LinkedHashMultimap.<String, Integer>create();
		linkedhash.put("a", 1);
		linkedhash.putAll("b", Arrays.asList(2, 4, 2, 3));
		
		System.out.println(linkedhash); // {a=[1], b=[2, 4, 3]}
		
		// tree
		TreeMultimap<String, Integer> tree = TreeMultimap.<String, Integer>create();
		tree.put("a", 1);
		tree.putAll("b", Arrays.asList(2, 4, 2, 3));
		
		System.out.println(tree); // {a=[1], b=[2, 3, 4]}
		
		// 不可变集合
		ImmutableListMultimap<String, Integer> immulist = ImmutableListMultimap.<String, Integer>builder().put("a", 1).putAll("c", 2, 2, 4, 5).build();
		System.out.println(immulist); // {a=[1], c=[2, 2, 4, 5]}
		
		System.out.println(immulist.inverse()); // {1=[a], 2=[c, c], 4=[c], 5=[c]} 反转，用值做Key、源Key作Value
		
		ImmutableSetMultimap<String, Integer> immuset = ImmutableSetMultimap.<String, Integer>of("d", 1, "c", 2);
		System.out.println(immuset); // {d=[1], c=[2]}
		
		System.out.println(immuset.inverse()); // {1=[d], 2=[c]} 反转，用值做Key、源Key作Value
		
		// Multimaps
	}
}
