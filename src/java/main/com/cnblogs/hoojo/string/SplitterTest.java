package com.cnblogs.hoojo.string;

import com.cnblogs.hoojo.BasedTest;
import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

/**
 * 字符串分割器
 * 
<table class="src" style="box-sizing: border-box; border-collapse: collapse; border-spacing: 0px;  border-color: rgb(214, 214, 214); width: 603.333374023438px; vertical-align: top; margin-top: 8px; margin-bottom: 8px; color: rgb(49, 49, 49);    font-size: 14.4444446563721px; line-height: 22px; background-color: rgb(247, 247, 247);">
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
				<b style="box-sizing: border-box;">static Splitter fixedLength(int length)</b><br style="box-sizing: border-box;">
				返回分离器的划分字符串到给定长度的片段。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				2</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">Splitter limit(int limit)</b><br style="box-sizing: border-box;">
				返回一个分离器，其行为等同于这个分离器，但停止分裂后达到了极限。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				3</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">Splitter omitEmptyStrings()</b><br style="box-sizing: border-box;">
				返回使用给定的单字符分离器分离器。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				4</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">static Splitter on(char separator)</b><br style="box-sizing: border-box;">
				返回使用给定的单字符分离器分离器。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				5</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">static Splitter on(CharMatcher separatorMatcher)</b><br style="box-sizing: border-box;">
				返回一个分离器的匹配考虑由给定CharMatcher是一个分隔任何单个字符。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				6</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">static Splitter on(Pattern separatorPattern)</b><br style="box-sizing: border-box;">
				返回分离器的考虑任何序列匹配模式是一个分隔符。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				7</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">static Splitter on(String separator)</b><br style="box-sizing: border-box;">
				返回使用给定的固定的字符串作为分隔符分离器。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				8</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">static Splitter onPattern(String separatorPattern)</b><br style="box-sizing: border-box;">
				返回分离器的考虑任何序列匹配一个给定模式(正则表达式)是一个分隔符。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				9</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">Iterable&lt;String&gt; split(CharSequence sequence)</b><br style="box-sizing: border-box;">
				分割成序列串组件并使其可通过迭代器，其可以被懒惰地评估计算。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				10</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">List&lt;String&gt; splitToList(CharSequence sequence)</b><br style="box-sizing: border-box;">
				拆分序列化为字符串组成部分，并将其返回为不可变列表。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				11</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">Splitter trimResults()</b><br style="box-sizing: border-box;">
				返回分离器的行为等同于该分离器，但会自动删除开头和结尾的空白，从每个返回子;相当于trimResults(CharMatcher.WHITESPACE).</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				12</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">Splitter trimResults(CharMatcher trimmer)</b><br style="box-sizing: border-box;">
				返回分离器的行为等同于该分离器，但会删除所有开头或结尾的字符匹配每一个给定的CharMatcher返回字符串。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				13</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">Splitter.MapSplitter withKeyValueSeparator(char separator)</b><br style="box-sizing: border-box;">
				返回MapSplitter这样会将在此基础上分离器的条目，并分割成入口键和值使用指定的分隔符。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				14</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">Splitter.MapSplitter withKeyValueSeparator(Splitter keyValueSplitter)</b><br style="box-sizing: border-box;">
				返回MapSplitter这样会将在此基础上分离器的条目，并分割成条目使用指定的键值分离器键和值。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				15</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">Splitter.MapSplitter withKeyValueSeparator(String separator)</b><br style="box-sizing: border-box;">
				返回MapSplitter这样会将在此基础上分离器的条目，并分割成入口键和值使用指定的分隔符。</td>
		</tr>
	</tbody>
</table>	
 * 
 * @author hoojo
 * @createDate 2017年10月30日 下午3:59:51
 * @file SplitterTest.java
 * @package com.cnblogs.hoojo.string
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class SplitterTest extends BasedTest {
	
	@SuppressWarnings("static-access")
	@Test
	public void testAPI() {
		String str = "1,2,3,, 4   ,5,";
		
		// 以 "," 进行分割
		print(Splitter.on(",").split(str)); //[1, 2, 3, ,  4   , 5, ]
		
		// 对空字符串进行过滤
		print(Splitter.on(",").omitEmptyStrings().split(str)); //[1, 2, 3,  4   , 5]
		
		// 只切割3块，后面的将不被切割
		print(Splitter.on(",").limit(3).split("1,2,3,4,5")); //[1, 2, 3,4,5]
		
		// 过滤掉空格字符串
		print(Splitter.on(",").omitEmptyStrings().trimResults().split(str)); //[1, 2, 3, 4, 5]
		
		// 过滤指定字符串
		print(Splitter.on(",").trimResults(CharMatcher.is(' ')).split(str)); //[1, 2, 3, , 4, 5, ]
		// 过滤分割字符串前后的字符
		print(Splitter.on(",").trimResults(CharMatcher.is(';')).split(";abc,;e;,f;,g,1,2")); //[abc, e, f, g, 1, 2]
		// 过滤小写字符串
		print(Splitter.on(",").trimResults(CharMatcher.javaLowerCase()).split(";abc,;e;,f;,g,1,2")); //[;, ;e;, ;, , 1, 2]
		
		// 将字符串切割成map对象
		print(Splitter.on(",").withKeyValueSeparator("-").split("name-jack,age-22,email-abc@g.cn")); //{name=jack, age=22, email=abc@g.cn}
		
		// 按固定长度分割字符串
		print(Splitter.on(",").fixedLength(2).split("1234566779abc")); //[12, 34, 56, 67, 79, ab, c]
		// 使用正则切割
		print(Splitter.on(",").onPattern("[a-z]").split("1a2c34d56e67f79abc")); //[1, 2, 34, 56, 67, 79, , , ]
		
		// 多种方式分割
		print(Splitter.on(",").on(";").split(";abc,;e;,f;,g,1,2")); //[, abc,, e, ,f, ,g,1,2]
	}
}

