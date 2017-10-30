package com.cnblogs.hoojo.string;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;

/**
 * Joiner 连接器

<table class="src" style="box-sizing: border-box; border-collapse: collapse; border-spacing: 0px;  border-color: rgb(214, 214, 214); width: 495.555572509766px; vertical-align: top; margin-top: 8px; margin-bottom: 8px; color: rgb(49, 49, 49);    font-size: 14.4444446563721px; line-height: 22px; background-color: rgb(247, 247, 247);">
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
				<b style="box-sizing: border-box;">&lt;A extends Appendable&gt; A appendTo(A appendable, Iterable&lt;?&gt; parts)</b><br style="box-sizing: border-box;">
				每个追加部分的字符串表示，使用每个之间先前配置的分离器，可用来追加。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				2</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">&lt;A extends Appendable&gt; A appendTo(A appendable, Iterator&lt;?&gt; parts)</b><br style="box-sizing: border-box;">
				每个追加部分的字符串表示，使用每个之间先前配置的分离器，可用来追加。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				3</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">&lt;A extends Appendable&gt; A appendTo(A appendable, Object[] parts)</b><br style="box-sizing: border-box;">
				每个追加部分的字符串表示，使用每个之间先前配置的分离器，可用来追加。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				4</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">&lt;A extends Appendable&gt; A appendTo(A appendable, Object first, Object second, Object... rest)</b><br style="box-sizing: border-box;">
				追加到可追加的每个其余参数的字符串表示。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				5</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">StringBuilder appendTo(StringBuilder builder, Iterable&lt;?&gt; parts)</b><br style="box-sizing: border-box;">
				每个追加部分的字符串表示，使用每个之间先前配置的分离器<span style="font-size: 14.4444446563721px;">，为构建</span><span style="font-size: 14.4444446563721px;">者。</span></td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				6</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">StringBuilder appendTo(StringBuilder builder, Iterator&lt;?&gt; parts)</b><br style="box-sizing: border-box;">
				每个追加部分的字符串表示，使用每个之间先前配置的分离器，为构建者。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				7</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">StringBuilder appendTo(StringBuilder builder, Object[] parts)</b><br style="box-sizing: border-box;">
				每个追加部分的字符串表示，使用每个之间先前配置的分离器，为构建者。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				8</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">StringBuilder appendTo(StringBuilder builder, Object first, Object second, Object... rest)</b><br style="box-sizing: border-box;">
				追加到构建器的每个其余参数的字符串表示。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				9</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">String join(Iterable&lt;?&gt; parts)</b><br style="box-sizing: border-box;">
				返回一个包含每个部分的字符串表示，使用每个之间先前配置的分隔符的字符串。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				10</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">String join(Iterator&lt;?&gt; parts)</b><br style="box-sizing: border-box;">
				返回一个包含每个部分的字符串表示，使用每个之间先前配置的分隔符的字符串。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				11</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">String join(Object[] parts)</b><br style="box-sizing: border-box;">
				返回一个包含每个部分的字符串表示，使用每个之间先前配置的分隔符的字符串。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				12</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">String join(Object first, Object second, Object... rest)</b><br style="box-sizing: border-box;">
				返回一个包含每个参数的字符串表示，使用每个之间先前配置的分隔符的字符串。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				13</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">static Joiner on(char separator)</b><br style="box-sizing: border-box;">
				返回一个加入者其连续元素之间自动地分隔符。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				14</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">static Joiner on(String separator)</b><br style="box-sizing: border-box;">
				返回一个加入者其连续元素之间自动地分隔符。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				15</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">Joiner skipNulls()</b><br style="box-sizing: border-box;">
				返回一个相同的行为，因为这加入者，除了自动跳过任何提供空元素的加入者。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				16</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">Joiner useForNull(String nullText)</b><br style="box-sizing: border-box;">
				返回一个相同的行为，因为这一个加入者，除了自动替换nullText任何提供null元素。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				17</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">Joiner.MapJoiner withKeyValueSeparator(String keyValueSeparator)</b><br style="box-sizing: border-box;">
				返回使用给定键值分离器MapJoiner，和相同的结构，否则为Joiner连接符 。</td>
		</tr>
	</tbody>
</table>

 * @author hoojo
 * @createDate 2017年10月30日 下午3:35:30
 * @file JoinerTest.java
 * @package com.cnblogs.hoojo.string
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class JoinerTest {

	private static void print(Object text) {
		System.out.println(text);
	}
	
	@Test
	public void testAPI() throws IOException {
		// 跳过Null值，用“;”链接
		Joiner joiner = Joiner.on(";").skipNulls();
		String result = joiner.join(Arrays.asList("a", "b", 1, null, 3, "c"));
		
		print(result); //a;b;1;3;c
		
		// 空值用None代替，用“;”链接
		print(Joiner.on(";").useForNull("None").join(Arrays.asList("a", "b", 1, null, 3, "c")));//a;b;1;None;3;c
		
		// 返回StringBuilder
		StringBuilder builder = new StringBuilder();
		Joiner.on(',').skipNulls().appendTo(builder, Arrays.asList("a", "b", 1, null, 3, "c"));
		print(builder); //a,b,1,3,c
		
		// join 支持多参数签名
		print(Joiner.on(",").join("oh", '!', 1, new Integer(3))); //oh,!,1,3
		
		// 传入 Appendable 接口实现
		print(Joiner.on(',').skipNulls().appendTo(new StringWriter(), Arrays.asList("a", "b", 1, null, 3, "c"))); //a,b,1,3,c
		
		// 链接Map数据，并且可以链接Key和Value
		print(Joiner.on(',').withKeyValueSeparator("_").join(ImmutableMap.of("name", "jack", "age", 24))); //name_jack,age_24
		
		print(Joiner.on(";").join(new Object[] {}));
		Object[] o = null;
		print(Joiner.on(";").join(o));
	}
}
