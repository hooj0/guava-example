package com.cnblogs.hoojo.nullable;

import org.junit.Test;

import com.google.common.base.Optional;

/**
 * <table class="src" style="box-sizing: border-box; border-collapse: collapse; border-spacing: 0px;  border-color: rgb(214, 214, 214); width: 603.333374023438px; vertical-align: top; margin-top: 8px; margin-bottom: 8px; color: rgb(49, 49, 49);    font-size: 14.4444446563721px; line-height: 22px; background-color: rgb(247, 247, 247);">
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
				<b style="box-sizing: border-box;">static &lt;T&gt; Optional&lt;T&gt; absent()</b><br style="box-sizing: border-box;">
				返回没有包含的参考Optional的实例。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				2</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">abstract Set&lt;T&gt; asSet()</b><br style="box-sizing: border-box;">
				返回一个不可变的单集的唯一元素所包含的实例(如果存在);否则为一个空的不可变的集合。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				3</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">abstract boolean equals(Object object)</b><br style="box-sizing: border-box;">
				返回true如果对象是一个Optional实例，无论是包含引用彼此相等或两者都不存在。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				4</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">static &lt;T&gt; Optional&lt;T&gt; fromNullable(T nullableReference)</b><br style="box-sizing: border-box;">
				如果nullableReference非空，返回一个包含引用Optional实例;否则返回absent()。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				5</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">abstract T get()</b><br style="box-sizing: border-box;">
				返回所包含的实例，它必须存在。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				6</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">abstract int hashCode()</b><br style="box-sizing: border-box;">
				返回此实例的哈希码。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				7</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">abstract boolean isPresent()</b><br style="box-sizing: border-box;">
				返回true，如果这支架包含一个(非空)的实例。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				8</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">static &lt;T&gt; Optional&lt;T&gt; of(T reference)</b><br style="box-sizing: border-box;">
				返回包含给定的非空引用Optional实例。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				9</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">abstract Optional&lt;T&gt; or(Optional&lt;? extends T&gt; secondChoice)</b><br style="box-sizing: border-box;">
				返回此Optional，如果它有一个值存在; 否则返回secondChoice。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				10</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">abstract T or(Supplier&lt;? extends T&gt; supplier)</b><br style="box-sizing: border-box;">
				返回所包含的实例(如果存在); 否则supplier.get()。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				11</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">abstract T or(T defaultValue)</b><br style="box-sizing: border-box;">
				返回所包含的实例(如果存在);否则为默认值。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				12</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">abstract T orNull()</b><br style="box-sizing: border-box;">
				返回所包含的实例(如果存在);否则返回null。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				13</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">static &lt;T&gt; Iterable&lt;T&gt; presentInstances(Iterable&lt;? extends Optional&lt;? extends T&gt;&gt; optionals)</b><br style="box-sizing: border-box;">
				从提供的optionals返回每个实例的存在的值，从而跳过absent()。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				14</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">abstract String toString()</b><br style="box-sizing: border-box;">
				返回此实例的字符串表示。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				15</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">abstract &lt;V&gt; Optional&lt;V&gt; transform(Function&lt;? super T,V&gt; function)</b><br style="box-sizing: border-box;">
				如果实例存在，则它被转换给定的功能;否则absent()被返回。</td>
		</tr>
	</tbody>
</table>
 * <b>function:</b> Optional Null 
 * @author hoojo
 * @createDate 2017年9月29日 下午5:57:12
 * @file OptionalTest.java
 * @package com.cnblogs.hoojo.nullable
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class OptionalTest {

	@Test
	public void testNull() {
		try {
			
			Optional<Integer> a = Optional.fromNullable(3);
			
			System.out.println(a.isPresent()); // 是否不为空
			System.out.println(a.get()); // 获取源对象
			System.out.println(a.or(2)); // 如果源对象为空就返回2
			System.out.println(a.orNull()); // 如果源对象为空就返回null
			
			System.out.println(a.asSet());
			
			Optional<Integer> b = Optional.fromNullable(null);
			
			System.out.println(b.isPresent());
			System.out.println(b.or(1));
			System.out.println(b.orNull());
			
			System.out.println(b.get());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testOfNull() {
		
		try {
			Optional<Integer> a = Optional.absent();
			System.out.println(a.isPresent());
			System.out.println(a.or(3));
			
			System.out.println(a.asSet());
			
			System.out.println(Optional.fromNullable(null).or(333));
			System.out.println(a.get());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
