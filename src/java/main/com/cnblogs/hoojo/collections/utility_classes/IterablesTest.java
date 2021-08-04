package com.cnblogs.hoojo.collections.utility_classes;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
集合迭代工具类
<table cellspacing="0" cellpadding="0" border="1">
<tbody>
<tr>
<td width="186"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Iterables.html#concat(java.lang.Iterable)"><tt>concat(Iterable&lt;Iterable&gt;)</tt></a></td>
<td width="222">串联多个iterables的懒视图*</td>
<td width="211"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Iterables.html#concat(java.lang.Iterable...)"><tt>concat(Iterable...)</tt></a></td>
</tr>
<tr>
<td width="186"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Iterables.html#frequency(java.lang.Iterable, java.lang.Object)"><tt>frequency(Iterable, Object)</tt></a></td>
<td width="222">返回对象在iterable中出现的次数</td>
<td width="211">与Collections.frequency (Collection, &nbsp; Object)比较；<tt></tt><a href="http://code.google.com/p/guava-libraries/wiki/NewCollectionTypesExplained#Multiset">Multiset</a></td>
</tr>
<tr>
<td width="186"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Iterables.html#partition(java.lang.Iterable, int)"><tt>partition(Iterable, int)</tt></a></td>
<td width="222">把iterable按指定大小分割，得到的子集都不能进行修改操作</td>
<td width="211"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Lists.html#partition(java.util.List, int)"><tt>Lists.partition(List, int)</tt></a>；<a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Iterables.html#paddedPartition(java.lang.Iterable, int)"><tt>paddedPartition(Iterable, int)</tt></a></td>
</tr>
<tr>
<td width="186"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Iterables.html#getFirst(java.lang.Iterable, T)"><tt>getFirst(Iterable, T default)</tt></a></td>
<td width="222">返回iterable的第一个元素，若iterable为空则返回默认值</td>
<td width="211">与Iterable.iterator(). next()比较;<a href="http://docs.guava-libraries.googlecode.com/git-history/release12/javadoc/com/google/common/collect/FluentIterable.html#first()"><tt>FluentIterable.first()</tt></a></td>
</tr>
<tr>
<td width="186"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Iterables.html#getLast(java.lang.Iterable)"><tt>getLast(Iterable)</tt></a></td>
<td width="222">返回iterable的最后一个元素，若iterable为空则抛出NoSuchElementException</td>
<td width="211"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Iterables.html#getLast(java.lang.Iterable, T)"><tt>getLast(Iterable, T default)</tt></a>；<br>
<a href="http://docs.guava-libraries.googlecode.com/git-history/release12/javadoc/com/google/common/collect/FluentIterable.html#last()"><tt>FluentIterable.last()</tt></a></td>
</tr>
<tr>
<td width="186"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Iterables.html#elementsEqual(java.lang.Iterable, java.lang.Iterable)"><tt>elementsEqual(Iterable, Iterable)</tt></a></td>
<td width="222">如果两个iterable中的所有元素相等且顺序一致，返回true</td>
<td width="211">与List.equals(Object)比较</td>
</tr>
<tr>
<td width="186"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Iterables.html#unmodifiableIterable(java.lang.Iterable)"><tt>unmodifiableIterable(Iterable)</tt></a></td>
<td width="222">返回iterable的不可变视图</td>
<td width="211">与Collections. unmodifiableCollection(Collection)比较</td>
</tr>
<tr>
<td width="186"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Iterables.html#limit(java.lang.Iterable, int)"><tt>limit(Iterable, int)</tt></a></td>
<td width="222">限制iterable的元素个数限制给定值</td>
<td width="211"><a href="http://docs.guava-libraries.googlecode.com/git-history/release12/javadoc/com/google/common/collect/FluentIterable.html#limit(int)"><tt>FluentIterable.limit(int)</tt></a></td>
</tr>
<tr>
<td width="186"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Iterables.html#getOnlyElement(java.lang.Iterable)"><tt>getOnlyElement(Iterable)</tt></a></td>
<td width="222">获取iterable中唯一的元素，如果iterable为空或有多个元素，则快速失败</td>
<td width="211"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Iterables.html#getOnlyElement(java.lang.Iterable, T)"><tt>getOnlyElement(Iterable, T default)</tt></a></td>
</tr>
</tbody>
</table>
 * @author hoojo
 * @createDate 2017年10月17日 下午3:55:32
 * @file IterablesTest.java
 * @package com.cnblogs.hoojo.collections
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class IterablesTest extends BasedTest {

	@Test
	public void testIterables() {
		// 集合拼接合并
		Iterable<Integer> iter = Iterables.concat(Ints.asList(2, 3, 5), Ints.asList(1, 7, 4, 2));
		
		out(iter); //[2, 3, 5, 1, 7, 4, 2]
		// 获取 最后一个元素
		out(Iterables.getLast(iter)); //2
		// 获取唯一，仅有一个元素
		out(Iterables.getOnlyElement(Ints.asList(2))); //2
		//out(Iterables.getOnlyElement(Ints.asList(2, 3, 5)));
		// 获取前 4个 元素
		out(Iterables.limit(iter, 4)); //[2, 3, 5, 1]
		// 分组
		out(Iterables.partition(iter, 2)); //[[2, 3], [5, 1], [7, 4], [2]]
		
		Iterable<Integer> temp = Iterables.cycle(3, 5, 22, 66);
		//temp.forEach(x -> out("cycle: " + x));
		//temp.iterator().hasNext();
		out(temp); //[3, 5, 22, 66] (cycled)
		
		// 全部满足条件 为 true，否则为 false
		out(Iterables.all(iter, new Predicate<Integer>() {
			@Override
			public boolean apply(Integer input) {
				if (input >= 4) {
					return true;
				}
				return false;
			}
		})); // false
		
		out(Iterables.all(iter, (input) -> input > 0)); // true
		
		// 任意满足 就返回 true， 没有一个元素满足就为 false
		out(Iterables.any(iter, (x) -> x > 3)); // true
		out(Iterables.any(iter, (x) -> x > 13)); // false
		out(Iterables.any(iter, (x) -> { return x > 13; })); // false

		// 包含指定元素
		out(Iterables.contains(iter, 3)); // true
		
		// 比较两个集合元素是否全部相同
		out(Iterables.elementsEqual(Lists.newArrayList(2, 3), Lists.newArrayList(2, 3))); // true
		out(Iterables.elementsEqual(Lists.newArrayList(2, 3), Lists.newArrayList(2, 4))); // false
		
		// 查询元素出现的次数
		out(Iterables.frequency(iter, 3)); // 1
		out(Iterables.frequency(iter, 2)); // 2
		
		// 查询元素索引，只返回第一个满足条件元素索引，没找到返回 -1
		out(Iterables.indexOf(iter, x -> x >= 1)); // 0
		
		// 获取指定索引位置元素
		out(Iterables.get(iter, 5)); // 4
		out(Iterables.get(iter, 30, -1)); // -1
		
		// 转换为Quque队列，遍历完后将删除所有内部元素
		temp = Iterables.consumingIterable(Lists.newArrayList(2, 3));
		out(temp.iterator().hasNext()); // true
		temp.forEach(x -> out("consumingIterable: " + x)); // 2、3
		out(temp.iterator().hasNext()); // false
		
		// 过滤匹配条件的元素
		out(Iterables.filter(iter, x -> x >= 5)); //[5, 7]
		// 查找满足条件的元素并立即返回
		out(Iterables.find(iter, x -> x >= 5, -1)); // 5
		// 获取首个元素
		out(Iterables.getFirst(iter, -1)); // 2
		
		// 将集合中的元素集合合并成单集合
		Iterable<List<Integer>> iters = Lists.<List<Integer>>newArrayList(Lists.newArrayList(1, 2, 3), Lists.newArrayList(3, 5, 4));
		out(iters); //[[1, 2, 3], [3, 5, 4]]
		out(Iterables.mergeSorted(iters, Ordering.natural())); //[1, 2, 3, 3, 5, 4]
		
		// 将集合拆分为指定个数子集合
		out(Iterables.paddedPartition(iter, 2)); // [[2, 3], [5, 1], [7, 4], [2, null]]
		out(Iterables.paddedPartition(iter, 4)); // [[2, 3, 5, 1], [7, 4, 2, null]]
		
		// 跳过前 3 个元素，取下标3后的元素，和limit相反
		out(Iterables.skip(iter, 3)); // [1, 7, 4, 2]
		
		// 转换对象数据，将集合Integer 转换为 String
		out(Iterables.transform(iter, new Function<Integer, String>() {
			@Override
			public String apply(Integer input) {
				return null;//"s-" + input;
			}
		})); // [s-2, s-3, s-5, s-1, s-7, s-4, s-2]
		
		out(Iterables.transform(iter, x -> x + 2)); // [4, 5, 7, 3, 9, 6, 4]
		
		// 查找对象，返回Optional 类型
		out(Iterables.tryFind(iter, x -> x == 8).isPresent()); // false
		out(Iterables.tryFind(iter, x -> x == 3).isPresent()); // true

		// 不可变集合
		out(Iterables.unmodifiableIterable(Arrays.asList(1, 2, 3))); // [1, 2, 3]
	}
	
	@Test
	public void testIterators() {
		
		Collection<Integer> collect = Lists.newArrayList(1, 2, 0);
		Iterators.addAll(collect, Ints.asList(2, 3, 5).iterator());
		out(collect); // [1, 2, 0, 2, 3, 5]
	}
}
