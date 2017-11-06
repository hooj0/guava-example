package com.cnblogs.hoojo.string;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Predicate;

/**
 * <b>function:</b> 字符匹配器
 * @author hoojo
 * @createDate 2017年10月30日 下午4:42:24
 * @file CharMatcherTest.java
 * @package com.cnblogs.hoojo.string
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class CharMatcherTest {

	private static void print(Object text) {
		System.out.println(text);
	}
	
	/**
	<table class="src" style="box-sizing: border-box; border-collapse: collapse; border-spacing: 0px;  border-color: rgb(214, 214, 214); width: 603.333374023438px; vertical-align: top; margin-top: 8px; margin-bottom: 8px; color: rgb(49, 49, 49);    font-size: 14.4444446563721px; line-height: 22px; background-color: rgb(247, 247, 247);">
		<tbody style="box-sizing: border-box;">
			<tr style="box-sizing: border-box;">
				<th style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px; width: 38.8888893127441px; background: rgb(238, 238, 238);">
					S.N.</th>
				<th style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px; background: rgb(238, 238, 238);">
					字段及说明</th>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					1</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">static CharMatcher ANY</b><br style="box-sizing: border-box;">
					匹配任意字符。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					2</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">static CharMatcher ASCII</b><br style="box-sizing: border-box;">
					确定字符是否为ASCII码，这意味着它的代码点低于128。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					3</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">static CharMatcher BREAKING_WHITESPACE</b><br style="box-sizing: border-box;">
					确定一个字符是否是一个破空白（即，一个空格可以解释为格式目的词之间休息）。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					4</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">static CharMatcher DIGIT</b><br style="box-sizing: border-box;">
					确定一个字符是否是根据Unicode数字。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					5</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">static CharMatcher INVISIBLE</b><br style="box-sizing: border-box;">
					确定一个字符是否是看不见的;也就是说，如果它的Unicode类是任何SPACE_SEPARATOR，LINE_SEPARATOR，PARAGRAPH_SEPARATOR，控制，FORMAT，SURROGATE和PRIVATE_USE根据ICU4J。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					6</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">static CharMatcher JAVA_DIGIT</b><br style="box-sizing: border-box;">
					确定一个字符是否是按照Java的定义一个数字。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					7</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">static CharMatcher JAVA_ISO_CONTROL</b><br style="box-sizing: border-box;">
					确定一个字符是否是所指定的Character.isISOControl(char)ISO控制字符。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					8</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">static CharMatcher JAVA_LETTER</b><br style="box-sizing: border-box;">
					确定一个字符是否是按照Java的定义的字母。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					9</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">static CharMatcher JAVA_LETTER_OR_DIGIT</b><br style="box-sizing: border-box;">
					确定一个字符是否是按照Java的定义，一个字母或数字。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					10</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">static CharMatcher JAVA_LOWER_CASE</b><br style="box-sizing: border-box;">
					确定一个字符是否是按照Java定义的<span style="font-size: 14.4444446563721px;">小写。</span></td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					11</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">static CharMatcher JAVA_UPPER_CASE</b><br style="box-sizing: border-box;">
					确定一个字符是否是按照Java定义的<span style="font-size: 14.4444446563721px;">大写。</span></td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					12</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">static CharMatcher NONE</b><br style="box-sizing: border-box;">
					匹配任何字符。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					13</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">static CharMatcher SINGLE_WIDTH</b><br style="box-sizing: border-box;">
					确定一个字符是否是单宽度（不是双倍宽度）。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					14</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">static CharMatcher WHITESPACE</b><br style="box-sizing: border-box;">
					决定根据最新的Unicode标准是否字符是空白，如图所示这里。</td>
			</tr>
		</tbody>
	</table>
	 */
	@Test
	public void testMatcherAPI() throws UnsupportedEncodingException {
		
		// 删除iso字符串
		print(CharMatcher.javaIsoControl().removeFrom("'/u4E2D', '/u56FD'"));
		
		// 删除数字
		print(CharMatcher.digit().removeFrom("abc234def56")); // abcdef
		
		// 删除任意字符
		print(CharMatcher.any().removeFrom("abc234def56哈哈")); // ""
		
		// 删除匹配到任意字符串
		print(CharMatcher.anyOf("aeiou").removeFrom("abc23QUB4def5XYZ6")); // bc23QUB4df5XYZ6
				
		// ascii 值 小于等于 128 都匹配
		print(CharMatcher.ascii().removeFrom("abc234def56哈哈‡›²釦")); //哈哈‡›²釦
		
		// 删除空格
		print(CharMatcher.breakingWhitespace().removeFrom("  thinking in java,  oh,  my god")); //thinkinginjava,oh,mygod
		
		// 自定义匹配条件
		print(CharMatcher.forPredicate(new Predicate<Character>() {
			@Override
			public boolean apply(Character input) {
				if (input > 'a' && input < 'e') {
					return false;
				}
				return true;
			}
		}).removeFrom("abcddezxy")); //bcdd
		
		// 在指定区间
		print(CharMatcher.inRange('a', 'g').removeFrom("abcddezxy")); // zxy
		
		// 删除匹配 指定区间的字符串
		print(CharMatcher.inRange('1', '9').removeFrom("abc23QUB4def5XYZ6")); // abcQUBdefXYZ
		
		// 删除特殊的空白字符
		print(CharMatcher.invisible().removeFrom("abc\r234zx%2F%2C\13ef5\n6")); // abc234zx%2F%2Cef56
		
		// 删除匹配到的单个字符
		print(CharMatcher.is('/').removeFrom("abc/efg/234")); // abcefg234
		
		// 匹配不等于指定字符的
		print(CharMatcher.isNot('a').removeFrom("abddbac")); // aa
		
		// 匹配数字字符
		print(CharMatcher.javaDigit().removeFrom("a5b2d1d3b9a0c")); // abddbac
		
		// 匹配全部英文字符，包含大小写
		print(CharMatcher.javaLetter().removeFrom("a5b2d1d3b9a0cabc23QUB4def5XYZ6")); // 52139023456

		// 匹配大小写英文字母和数字
		print(CharMatcher.javaLetterOrDigit().removeFrom("<#a5b2d1d@3b9a0cabc$23QUB4d%ef5XYZ6>")); // <#@$%>
		
		// 匹配小写字母
		print(CharMatcher.javaLowerCase().removeFrom("abc23QUB4def5XYZ6")); // 23QUB45XYZ6
		
		// 匹配大写字母
		print(CharMatcher.javaUpperCase().removeFrom("abc23QUB4def5XYZ6")); // abc234def56
		
		// 不匹配任何字符
		print(CharMatcher.none().removeFrom(" a bc2 3QU ")); //  a bc2 3QU
		
		// 只匹配给出的字符以外的字符
		print(CharMatcher.noneOf("Xda3e").removeFrom("abc23QUB4def5XYZ6")); // a3deX
		
		// 匹配一个字符是否是单宽度（2个字节）；注意：目前单宽度字符匹配列表还不够完整
		print(CharMatcher.singleWidth().removeFrom("abc123#@!欧米伽■□")); // 欧米伽
		
		// 空格字符
		print(CharMatcher.whitespace().removeFrom(" a bc2 3QU ")); // abc23QU
	}
	
	
	/**
	 * 多个匹配条件链接进行匹配：
	 * or: 或者，只要满足一个就匹配成功
	 * and: 并且，两个条件都需要满足，才能匹配成功
	 * negate: 取反的意思，满足前置条件后取反向的部分
	 */
	@Test
	public void testJoinAPI() {
		/**----------------------- or -----------------------*/
		
		// 只保留数字和小写字母
		print(CharMatcher.javaDigit().or(CharMatcher.javaLowerCase()).retainFrom("abc23QUB4def5XYZ6")); // abc234def56
		// 删除 指定数字区间 或者 指定字符串区间
		print(CharMatcher.inRange('1', '9').or(CharMatcher.inRange('a', 'z')).removeFrom("abc23QUB4def5XYZ6")); //QUBXYZ
		
		/**----------------------- and -----------------------*/
		// 英文字母、并且是小写的，删掉
		print("and:" + CharMatcher.javaLetter().and(CharMatcher.javaLowerCase()).removeFrom("abc23QUB4def5XYZ6")); // 23QUB45XYZ6
		
		/**----------------------- negate -----------------------*/
		// 只保留数字，negate 取反的意思
		print(CharMatcher.javaDigit().negate().removeFrom("abc23QUB4def5XYZ6")); // 23456
		// 只保留小写字符串
		print(CharMatcher.javaLowerCase().negate().removeFrom("abc23QUB4def5XYZ6")); // abcdef
		// 只保留小写字符串和数字
		print(CharMatcher.javaLowerCase().or(CharMatcher.javaDigit()).negate().removeFrom("abc23QUB4def5XYZ6")); //abc234def56
		// 删除大写字符串和数字
		print(CharMatcher.javaLowerCase().negate().or(CharMatcher.javaDigit()).removeFrom("abc23QUB4def5XYZ6")); // abcdef
	}
	
	/**
	<table class="src" style="box-sizing: border-box; border-collapse: collapse; border-spacing: 0px;  border-color: rgb(214, 214, 214); width: 603.333374023438px; vertical-align: top; margin-top: 8px; margin-bottom: 8px; color: rgb(49, 49, 49);    font-size: 14.4444446563721px; line-height: 22px; background-color: rgb(247, 247, 247);">
		<tbody style="box-sizing: border-box;">
			<tr style="box-sizing: border-box;">
				<th style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px; width: 38.8888893127441px; background: rgb(238, 238, 238);">
					S.N.</th>
				<th style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px; background: rgb(238, 238, 238);">
					方法 &amp; 描述</th>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					1</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">CharMatcher and(CharMatcher other)</b><br style="box-sizing: border-box;">
					返回一个匹配器，匹配两种匹配器和其他任何字符。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					2</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">static CharMatcher anyOf(CharSequence sequence)</b><br style="box-sizing: border-box;">
					返回一个字符匹配匹配任何字符出现在给定的字符序列。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					3</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">boolean apply(Character character)</b><br style="box-sizing: border-box;">
					不推荐使用。只有提供满足谓词接口;用匹配（字符）代替。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					4</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">String collapseFrom(CharSequence sequence, char replacement)</b><br style="box-sizing: border-box;">
					返回输入字符序列的字符串拷贝，每个组连续的字符匹配此匹配由单一的替换字符替换。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					5</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">int countIn(CharSequence sequence)</b><br style="box-sizing: border-box;">
					返回一个字符序列中发现匹配的字符的数目。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					6</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">static CharMatcher forPredicate(Predicate&lt;? super Character&gt; predicate)</b><br style="box-sizing: border-box;">
					返回与相同的行为给定的基于字符的谓词匹配，但运行在原始的字符，而不是实例。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					7</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">int indexIn(CharSequence sequence)</b><br style="box-sizing: border-box;">
					返回第一个匹配字符的索引中的一个字符序列，或-1，如果没有匹配的字符存在。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					8</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">int indexIn(CharSequence sequence, int start)</b><br style="box-sizing: border-box;">
					返回第一个匹配字符的索引中的一个字符序列，从给定位置开始，或-1，如果没有字符的位置之后匹配。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					9</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">static CharMatcher inRange(char startInclusive, char endInclusive)</b><br style="box-sizing: border-box;">
					返回一个字符匹配匹配给定范围内的任何字符（两个端点也包括在内）。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					10</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">static CharMatcher is(char match)</b><br style="box-sizing: border-box;">
					返回一个字符匹配匹配只有一个指定的字符。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					11</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">static CharMatcher isNot(char match)</b><br style="box-sizing: border-box;">
					返回一个字符匹配匹配除了指定的任何字符。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					12</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">int lastIndexIn(CharSequence sequence)</b><br style="box-sizing: border-box;">
					返回最后一个匹配字符的索引中的字符序列，或-1，如果没有匹配的字符存在。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					13</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">abstract boolean matches(char c)</b><br style="box-sizing: border-box;">
					确定给定字符一个true或false值。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					14</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">boolean matchesAllOf(CharSequence sequence)</b><br style="box-sizing: border-box;">
					确定给定字符一个true或false值。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					15</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">boolean matchesAnyOf(CharSequence sequence)</b><br style="box-sizing: border-box;">
					返回true如果字符序列包含至少一个匹配的字符。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					16</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">boolean matchesNoneOf(CharSequence sequence)</b><br style="box-sizing: border-box;">
					返回true，如果一个字符序列中没有匹配的字符。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					17</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">CharMatcher negate()</b><br style="box-sizing: border-box;">
					返回一个匹配器，不受此匹配匹配任何字符。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					18</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">static CharMatcher noneOf(CharSequence sequence)</b><br style="box-sizing: border-box;">
					返回一个字符匹配器匹配不存在于给定的字符序列的任何字符。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					19</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">CharMatcher or(CharMatcher other)</b><br style="box-sizing: border-box;">
					返回一个匹配器，匹配任何匹配或其他任何字符。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					20</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">CharMatcher precomputed()</b><br style="box-sizing: border-box;">
					返回一个字符匹配功能上等同于这一个，但它可能会快于原来的查询;您的里程可能会有所不同。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					21</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">String removeFrom(CharSequence sequence)</b><br style="box-sizing: border-box;">
					返回包含的字符序列的所有非匹配的字符，为了一个字符串。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					22</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">String replaceFrom(CharSequence sequence, char replacement)</b><br style="box-sizing: border-box;">
					返回输入字符序列的字符串副本，其中每个字符匹配该匹配器由一个给定的替换字符替换。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					23</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">String replaceFrom(CharSequence sequence, CharSequence replacement)</b><br style="box-sizing: border-box;">
					返回输入字符序列的字符串副本，其中每个字符匹配该匹配器由一个给定的替换序列替换。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					24</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">String retainFrom(CharSequence sequence)</b><br style="box-sizing: border-box;">
					返回包含的字符序列的所有字符匹配，为了一个字符串。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					25</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">String toString()</b><br style="box-sizing: border-box;">
					返回此CharMatcher，如CharMatcher.or（WHITESPACE，JAVA_DIGIT）的字符串表示。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					26</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">String trimAndCollapseFrom(CharSequence sequence, char replacement)</b><br style="box-sizing: border-box;">
					折叠匹配字符完全一样collapseFrom一组如collapseFrom(java.lang.CharSequence, char) 做的一样，不同之处在于，无需更换一组被移除的匹配字符在开始或该序列的结束。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					27</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">String trimFrom(CharSequence sequence)</b><br style="box-sizing: border-box;">
					返回输入字符序列省略了所有匹配器从一开始，并从该串的末尾匹配字符的字符串。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					28</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">String trimLeadingFrom(CharSequence sequence)</b><br style="box-sizing: border-box;">
					返回输入字符序列，它省略了所有这些匹配的字符串开始处匹配字符的字符串。</td>
			</tr>
			<tr style="box-sizing: border-box;">
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					29</td>
				<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
					<b style="box-sizing: border-box;">String trimTrailingFrom(CharSequence sequence)</b><br style="box-sizing: border-box;">
					返回输入字符序列，它省略了所有这些匹配的字符串的结尾匹配字符的字符串。</td>
			</tr>
		</tbody>
	</table>
	 */
	@Test
	public void testActionAPI() {
		String text = "abc234def56";
		// 删除动作
		print(CharMatcher.digit().removeFrom(text)); // abcdef
		
		// 替换动作
		print(CharMatcher.digit().collapseFrom(text, '*')); // abc*def*
		
		// 匹配到的数量次数
		print(CharMatcher.digit().countIn(text)); // 5
		
		// 首次匹配到的索引下标
		print(CharMatcher.digit().indexIn(text)); // 3
		
		// 从指定位置开始匹配，查找匹配的索引下标
		print(CharMatcher.digit().indexIn(text, 6)); // 9
		
		// 最后一次出现的索引下标
		print(CharMatcher.digit().lastIndexIn(text)); // 10
		
		// 是否全部匹配，eg：是否全部为数字
		print(CharMatcher.digit().matchesAllOf(text)); // false
		print(CharMatcher.digit().matchesAllOf("1246464")); // true
		
		// 是否包含，eg：是否包含数字
		print(CharMatcher.digit().matchesAnyOf(text)); // true
		print(CharMatcher.digit().matchesAnyOf("abcABC")); // false
		
		// 是否不包含，是否不包含数字
		print(CharMatcher.digit().matchesNoneOf(text)); // false
		print(CharMatcher.digit().matchesNoneOf("abcABC")); // true
		print(CharMatcher.digit().matchesNoneOf("1234234234")); // false
		
		// 将匹配到的字符替换成字符
		print(CharMatcher.digit().replaceFrom(text, '_')); // abc___def__
		// 将匹配到的字符替换成字符串
		print(CharMatcher.digit().replaceFrom(text, "^_^")); // abc^_^^_^^_^def^_^^_^
		
		// 将匹配到的字符全部保留，剔除不匹配的字符；和removeFrom相反
		print(CharMatcher.digit().retainFrom(text)); // 23456
		
		// 将两端匹配到的替换为空（删掉），剩余中间的部分替换为指定字符
		print(CharMatcher.digit().trimAndCollapseFrom("1111ab c2 34 de  f566666", '*')); // ab c* * de  f
		print(CharMatcher.whitespace().trimAndCollapseFrom(" ab c2 34 de  f566  ", '-')); // ab-c2-34-de-f566
		
		// 将两端匹配到的字符删除掉
		print(CharMatcher.digit().trimFrom("ab13234234ab")); // ab13234234ab  
		print(CharMatcher.digit().trimFrom("1323AABB4234")); // AABB
		print(CharMatcher.inRange('a', 'z').trimFrom("ab13234234ab")); // 13234234
		
		// 将左边匹配到的字符串删除掉
		print(CharMatcher.digit().trimLeadingFrom("1323AABB4234")); // AABB4234
		
		// 将右边匹配到的字符串删除掉
		print(CharMatcher.digit().trimTrailingFrom("1323AABB4234")); // 1323AABB
	}
}
