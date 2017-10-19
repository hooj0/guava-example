package com.cnblogs.hoojo.compare;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

/**
 * 
 * <table cellspacing="0" cellpadding="0" border="1">
<tbody>
<tr>
<td width="199"><b>方法</b><b></b></td>
<td width="419"><b>描述</b><b></b></td>
</tr>
<tr>
<td width="199"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Ordering.html#natural()"><tt>natural()</tt></a></td>
<td width="419">对可排序类型做自然排序，如数字按大小，日期按先后排序</td>
</tr>
<tr>
<td width="199"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Ordering.html#usingToString()"><tt>usingToString()</tt></a></td>
<td width="419">按对象的字符串形式做字典排序[lexicographical ordering]</td>
</tr>
<tr>
<td width="199"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Ordering.html#from(java.util.Comparator)"><tt>from(Comparator)</tt></a></td>
<td width="419">把给定的Comparator转化为排序器</td>
</tr>
</tbody>
</table>

<table cellspacing="0" cellpadding="0" border="1">
<tbody>
<tr>
<td width="199"><b>方法</b></td>
<td width="419"><b>描述</b></td>
</tr>
<tr>
<td width="199"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Ordering.html#reverse()"><tt>reverse()</tt></a></td>
<td width="419">获取语义相反的排序器</td>
</tr>
<tr>
<td width="199"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Ordering.html#nullsFirst()"><tt>nullsFirst()</tt></a></td>
<td width="419">使用当前排序器，但额外把null值排到最前面。</td>
</tr>
<tr>
<td width="199"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Ordering.html#nullsLast()"><tt>nullsLast()</tt></a></td>
<td width="419">使用当前排序器，但额外把null值排到最后面。</td>
</tr>
<tr>
<td width="199"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Ordering.html#compound(java.util.Comparator)"><tt>compound(Comparator)</tt></a></td>
<td width="419">合成另一个比较器，以处理当前排序器中的相等情况。</td>
</tr>
<tr>
<td width="199"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Ordering.html#lexicographical()"><tt>lexicographical()</tt></a></td>
<td width="419">基于处理类型T的排序器，返回该类型的可迭代对象Iterable&lt;T&gt;的排序器。</td>
</tr>
<tr>
<td width="199"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Ordering.html#onResultOf(com.google.common.base.Function)"><tt>onResultOf(Function)</tt></a></td>
<td width="419">对集合中元素调用Function，再按返回值用当前排序器排序。</td>
</tr>
</tbody>
</table>

<table cellspacing="0" cellpadding="0" border="1">
<tbody>
<tr>
<td><strong>方法</strong></td>
<td><strong>描述</strong></td>
<td><strong>另请参见</strong></td>
</tr>
<tr>
<td><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Ordering.html#greatestOf(java.lang.Iterable, int)" rel="nofollow"><tt>greatestOf(Iterable iterable, int k)</tt></a></td>
<td>获取可迭代对象中最大的k个元素。</td>
<td><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Ordering.html#leastOf(java.lang.Iterable, int)" rel="nofollow"><tt>leastOf</tt></a></td>
</tr>
<tr>
<td><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Ordering.html#isOrdered(java.lang.Iterable)" rel="nofollow"><tt>isOrdered(Iterable)</tt></a></td>
<td>判断可迭代对象是否已按排序器排序：允许有排序值相等的元素。</td>
<td><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Ordering.html#isStrictlyOrdered(java.lang.Iterable)" rel="nofollow"><tt>isStrictlyOrdered</tt></a></td>
</tr>
<tr>
<td><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Ordering.html#sortedCopy(java.lang.Iterable)" rel="nofollow"><tt>sortedCopy(Iterable)</tt></a></td>
<td>判断可迭代对象是否已严格按排序器排序：不允许排序值相等的元素。</td>
<td><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Ordering.html#immutableSortedCopy(java.lang.Iterable)" rel="nofollow"><tt>immutableSortedCopy</tt></a></td>
</tr>
<tr>
<td><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Ordering.html#min(E, E)" rel="nofollow"><tt>min(E, E)</tt></a></td>
<td>返回两个参数中最小的那个。如果相等，则返回第一个参数。</td>
<td><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Ordering.html#max(E, E)" rel="nofollow"><tt>max(E, E)</tt></a></td>
</tr>
<tr>
<td><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Ordering.html#min(E, E, E, E...)" rel="nofollow"><tt>min(E, E, E, E...)</tt></a></td>
<td>返回多个参数中最小的那个。如果有超过一个参数都最小，则返回第一个最小的参数。</td>
<td><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Ordering.html#max(E, E, E, E...)" rel="nofollow"><tt>max(E, E, E, E...)</tt></a></td>
</tr>
<tr>
<td><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Ordering.html#min(java.lang.Iterable)" rel="nofollow"><tt>min(Iterable)</tt></a></td>
<td>返回迭代器中最小的元素。如果可迭代对象中没有元素，则抛出NoSuchElementException。</td>
<td><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Ordering.html#max(java.lang.Iterable)" rel="nofollow"><tt>max(Iterable)</tt></a>, <a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Ordering.html#min(java.util.Iterator)" rel="nofollow"><tt>min(Iterator)</tt></a>, <a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/Ordering.html#max(java.util.Iterator)" rel="nofollow"><tt>max(Iterator)</tt></a></td>
</tr>
</tbody>
</table>
 * 
 * @author hoojo
 * @createDate 2017年10月10日 下午6:19:03
 * @file OrderingTest.java
 * @package com.cnblogs.hoojo.compare
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class OrderingTest {
	
	@Test
	public void testSort() {
		
		///////////////// natural 数字自然排序
		List<Integer> list = Lists.newArrayList(1, 3, 2, 7, 5, 4);
		
		try {
			System.out.println(list);
			
			list.sort(Ordering.natural());
			
			System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//////////////////////////// usingToString 字符串排序
		List<String> list2 = Lists.newArrayList("aa", "abc", "word", "炸弹", "1limt", "3", "哈哈");
		
		try {
			System.out.println(list2);
			
			list2.sort(Ordering.usingToString());
			
			System.out.println(list2);
			
			list2.sort(Ordering.usingToString().reversed());
			
			System.out.println(list2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		///////////////////////////////////// 自定义排序
		list = Lists.newArrayList(1, 3, 2, 7, 5, 4, null);
		
		Ordering<Integer> order = Ordering.from(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return ComparisonChain.start().compare(o1, o2).result();
			}
		});
		
		System.out.println(list);
		
		list.sort(order.nullsFirst());
		System.out.println(list);
	}
	
	@Test
	public void testSortEntity() {
		
		///////////////// 对象排序，相等的元素compound进行二次排序
		List<Entity> list = Lists.newArrayList(new Entity(1, 2), new Entity(4, 3), null, new Entity(2, 1), new Entity(4, 1));
		
		try {
			System.out.println(list);
			
			Ordering<Entity> order = Ordering.from(new Comparator<Entity>() {
				@Override
				public int compare(Entity o1, Entity o2) {
					return ComparisonChain.start().compare(o1.a, o2.a).result();
				}
			});
			
			list.sort(order.nullsFirst());
			System.out.println(list);
			
			list.sort(order.nullsLast().compound(new Comparator<Entity>() {
				@Override
				public int compare(Entity o1, Entity o2) {
					return ComparisonChain.start().compare(o1.b, o2.b).result();
				}
			}));
			System.out.println(list);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/////////////////选择排序的key
		list = Lists.newArrayList(new Entity(1, 2), new Entity(4, 3), null, new Entity(2, 1), new Entity(4, 1));
		
		try {
			System.out.println(list);
			
			Ordering<Entity> order = Ordering.natural().nullsFirst().onResultOf(new Function<Entity, Integer>() {
		
				@Override
				public Integer apply(Entity input) {
					if (input != null)
						return input.b;
					
					return null;
				}
			});
			
			list.sort(order);
			System.out.println(list);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test() {
		
		List<Integer> list = Lists.newArrayList(1, 3, 2, 7, 5, 4, 9, 6, 3, 7, 1);
		
		// 从大到校的前3个元素
		System.out.println(Ordering.natural().greatestOf(list.iterator(), 3));
		// 从小到大的前三个元素
		System.out.println(Ordering.natural().leastOf(list.iterator(), 3));
		
		LinkedList<Integer> list2 = Lists.newLinkedList();
		list2.add(1);
		list2.add(3);
		list2.add(5);
		list2.add(4);
		list2.add(1);
		
		System.out.println(Ordering.natural().isOrdered(list2));
		Collections.sort(list);
		System.out.println(Ordering.natural().isOrdered(list));
		
		System.out.println(Ordering.natural().sortedCopy(list2));
		
		System.out.println(Ordering.natural().max(3, 1));
		System.out.println(Ordering.natural().max(list2));
		
		System.out.println(Ordering.natural().min(list2));
		
		list = Lists.newArrayList(1, 3, 2, 7, null, 5, 4, 9, null, 6, 3, 7, 1);
		// 无排序，返回Ordering实例
		list.sort(Ordering.allEqual());
		System.out.println(list);
		
		// 相同的元素排一起
		list.sort(Ordering.arbitrary());
		System.out.println(list);
		
		// 根据指定元素的顺序进排序
		list = Lists.newArrayList(1, 3, 2, 7, 5, 4, 9, 6, 3, 7, 1, 8);
		list.sort(Ordering.explicit(4, 3, 2, 1, 0, 5, 6, 7, 8, 9));
		System.out.println(list);
	}
	
	class Entity {
		public int a;
		public int b;
		
		Entity(int a, int b) {
			this.a = a;
			this.b = b;
		}
		
		@Override
		public String toString() {
			return "E{" + this.a + ", " + this.b + "}";
		}
	}
}
