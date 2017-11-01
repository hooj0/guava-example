package com.cnblogs.hoojo.checked;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkPositionIndex;
import static com.google.common.base.Preconditions.checkPositionIndexes;
import static com.google.common.base.Preconditions.checkState;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

/**
<table width="631" cellspacing="0" cellpadding="0" border="1">
<tbody>
<tr>
<td width="199"><b>方法声明（不包括额外参数）</b><b></b></td>
<td width="222" valign="top"><b>描述</b><b></b></td>
<td width="210" valign="top"><b>检查失败时抛出的异常</b><b></b></td>
</tr>
<tr>
<td width="199"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/base/Preconditions.html#checkArgument(boolean)"><tt>checkArgument(boolean)</tt></a></td>
<td width="222">检查boolean是否为true，用来检查传递给方法的参数。</td>
<td width="210">IllegalArgumentException</td>
</tr>
<tr>
<td width="199"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/base/Preconditions.html#checkNotNull(T)"><tt>checkNotNull(T)</tt></a></td>
<td width="222">检查value是否为null，该方法直接返回value，因此可以内嵌使用checkNotNull<tt>。</tt></td>
<td width="210">NullPointerException</td>
</tr>
<tr>
<td width="199"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/base/Preconditions.html#checkState(boolean)"><tt>checkState(boolean)</tt></a></td>
<td width="222">用来检查对象的某些状态。</td>
<td width="210">IllegalStateException</td>
</tr>
<tr>
<td width="199"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/base/Preconditions.html#checkElementIndex(int, int)"><tt>checkElementIndex(int index, int size)</tt></a></td>
<td width="222">检查index作为索引值对某个列表、字符串或数组是否有效。index&gt;=0 &amp;&amp; index&lt;size *</td>
<td width="210">IndexOutOfBoundsException</td>
</tr>
<tr>
<td width="199"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/base/Preconditions.html#checkPositionIndex(int, int)"><tt>checkPositionIndex(int index, int size)</tt></a></td>
<td width="222">检查index作为位置值对某个列表、字符串或数组是否有效。index&gt;=0 &amp;&amp; index&lt;=size *</td>
<td width="210">IndexOutOfBoundsException</td>
</tr>
<tr>
<td width="199"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/base/Preconditions.html#checkPositionIndexes(int, int, int)"><tt>checkPositionIndexes(int start, int end, int size)</tt></a></td>
<td width="222">检查[start, end]表示的位置范围对某个列表、字符串或数组是否有效*</td>
<td width="210">IndexOutOfBoundsException</td>
</tr>
</tbody>
</table>	
 * @author hoojo
 * @createDate 2017年10月10日 下午4:13:30
 * @file PreconditionsTest.java
 * @package com.cnblogs.hoojo.checked
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class PreconditionsTest {

	@Test
	public void testCheckArgs() {
		
		try {
			// 检查参数，不满足条件就抛出默认异常
			checkArgument(3 == 2);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			// 检查参数，不满足条件就抛出指定异常
			checkArgument(3 == 2, "条件不匹配");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			// 检查参数，不满足条件就抛出指定带参数异常
			checkArgument(3 == 2, "条件不匹配 %s == %s", 3, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCheckNull() {
		
		Integer a = null;
		
		try {
			// 检查对象是否为空，为空就发出默认异常信息
			checkNotNull(a);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			// 检查对象是否为空，为空就发出指定异常信息
			a = checkNotNull(a, "数据为空");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			// 检查对象是否为空，为空就发出指定带参数异常信息
			checkNotNull(a, "数据：%s 为空", a);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCheckState() {
		
		Integer a = null;
		
		// 状态检查，多用于枚举、数据状态的校验
		try {
			checkState(a != null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			checkState(a != null, "对象为空");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			checkState(a != null, "数据：%s 为空", a);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCheckIndex() {
		
		List<Integer> list = Lists.newArrayList(1, 3, 2, 3, 4); 

		// 0~size-1
		// 元素索引开始位置 0，结束位置是长度  (size - 1)
		try {
			// 索引越界抛出异常
			checkElementIndex(5, list.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			// 索引越界抛出异常
			checkElementIndex(15, list.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			// 索引越界抛出指定信息异常
			checkElementIndex(15, list.size(), "索引越界");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCheckPosition() {
		
		// 0~size
		// 元素索引开始位置 0，结束位置是长度  (size)
		try {
			checkPositionIndex(3, 3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			checkPositionIndex(3, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			checkPositionIndex(-15, 8, "下标越界");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCheckPositions() {
		// 判断区间是否在索引范围内
		try {
			checkPositionIndexes(2, 5, 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			checkPositionIndexes(-2, 5, 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			checkPositionIndexes(2, 50, 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
