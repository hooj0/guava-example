package com.cnblogs.hoojo.exception;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.base.Throwables;
import org.junit.Test;

import java.io.PrintStream;
import java.util.List;

/**
 * Throwables 异常类处理
 * <table cellspacing="0" cellpadding="0" border="1">
<tbody>
<tr>
<td width="228"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/base/Throwables.html#propagate(java.lang.Throwable)">RuntimeException &nbsp; propagate(Throwable)</a></td>
<td width="391">如果Throwable是Error或RuntimeException，直接抛出；否则把Throwable包装成RuntimeException抛出。返回类型是RuntimeException，所以你可以像上面说的那样写成<tt>throw Throwables.propagate(t)</tt>，Java编译器会意识到这行代码保证抛出异常。</td>
</tr>
<tr>
<td width="228"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/base/Throwables.html#propagateIfInstanceOf(java.lang.Throwable,%20java.lang.Class)">void propagateIfInstanceOf( Throwable, Class&lt;X extends &nbsp; Exception&gt;) throws X</a></td>
<td width="391">Throwable类型为X才抛出</td>
</tr>
<tr>
<td width="228"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/base/Throwables.html#propagateIfPossible(java.lang.Throwable)">void propagateIfPossible( Throwable)</a></td>
<td width="391">Throwable类型为Error或RuntimeException才抛出</td>
</tr>
<tr>
<td width="228"><a href="http://goo.gl/pgDJC">void &nbsp; propagateIfPossible( Throwable, Class&lt;X extends Throwable&gt;) throws X</a></td>
<td width="391">Throwable类型为X, Error或RuntimeException才抛出</td>
</tr>
</tbody>
</table>

<table cellspacing="0" cellpadding="0" border="1">
<tbody>
<tr>
<td width="350"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/base/Throwables.html#getRootCause(java.lang.Throwable)">Throwable &nbsp; getRootCause(Throwable)</a></td>
</tr>
<tr>
<td width="350"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/base/Throwables.html#getCausalChain(java.lang.Throwable)">List&lt;Throwable&gt; &nbsp; getCausalChain(Throwable)</a></td>
</tr>
<tr>
<td width="350"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/base/Throwables.html#getStackTraceAsString(java.lang.Throwable)">String &nbsp; getStackTraceAsString(Throwable)</a></td>
</tr>
</tbody>
</table>

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
				<b style="box-sizing: border-box;">static List&lt;Throwable&gt; getCausalChain(Throwable throwable)</b><br style="box-sizing: border-box;">
				获取一个Throwable的原因链的列表。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				2</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">static Throwable getRootCause(Throwable throwable)</b><br style="box-sizing: border-box;">
				返回抛出的最里面的原因。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				3</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">static String getStackTraceAsString(Throwable throwable)</b><br style="box-sizing: border-box;">
				返回包含toString()的结果字符串，随后完整抛出，递归的堆栈跟踪。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				4</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">static RuntimeException propagate(Throwable throwable)</b><br style="box-sizing: border-box;">
				传播抛出原样如果RuntimeException或Error是一个实例，否则作为最后的报告，把它包装在一个RuntimeException，然后传播。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				5</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">static &lt;X extends Throwable&gt; void propagateIfInstanceOf(Throwable throwable, Class&lt;X&gt; declaredType)</b><br style="box-sizing: border-box;">
				传播抛出对象完全按原样，当且仅当它是declaredType的一个实例。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				6</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">static void propagateIfPossible(Throwable throwable)</b><br style="box-sizing: border-box;">
				传播抛出对象完全按原样，当且仅当它是RuntimeException或Error的一个实例。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				7</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">static &lt;X extends Throwable&gt; void propagateIfPossible(Throwable throwable, Class&lt;X&gt; declaredType)</b><br style="box-sizing: border-box;">
				传播抛出对象完全按原样，当且仅当它是RuntimeException，错误或的declaredType的一个实例。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				8</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">static &lt;X1 extends Throwable,X2 extends Throwable&gt;void propagateIfPossible(Throwable throwable, Class&lt;X1&gt; declaredType1, Class&lt;X2&gt; declaredType2)</b><br style="box-sizing: border-box;">
				传播抛出对象完全按原样，当且仅当它是RuntimeException，Error，declaredType1或declaredType2的一个实例。</td>
		</tr>
	</tbody>
</table>
 * @author hoojo
 * @createDate 2017年10月11日 下午5:09:39
 * @file ThrowablesTest.java
 * @package com.cnblogs.hoojo.exception
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class ThrowablesTest extends BasedTest {

	// 异常链（异常信息值栈）
	@Test
	public void testCausalChain() {
		
		try {
			nullPointException();
		} catch (Exception e) {
			
			PrintStream writer = new PrintStream(System.out);
			// 异常信息值栈
			List<Throwable> list = Throwables.getCausalChain(e);
			for (Throwable th : list) {
				out("输出：" + th.getMessage());
				
				th.printStackTrace(writer);
			}
		}
	}
	
	// 最底层的异常信息，异常源头
	@Test
	public void testRootCause() {
		
		try {
			nullPointException();
		} catch (Exception e) {
			PrintStream writer = new PrintStream(System.out);

			// 根异常
			Throwables.getRootCause(e).printStackTrace(writer);
			
			// 将异常转换为字符串
			out(Throwables.getStackTraceAsString(e));
		}
	}
	
	@Test
	public void testPropagate() throws Exception {
		
		try {
			propagate(0);
		} catch (Exception e) {
			Throwables.propagateIfPossible(e, NullPointerException.class);
		}
	}
	
	@Test
	public void testPropagate2() throws Exception {
		
		try {
			propagate(1);
		} catch (Exception e) {
			// 如果是对应异常实例就抛出
			Throwables.throwIfInstanceOf(e, Error.class);
			//Throwables.throwIfUnchecked(e);
		}
	}
	
	@Test
	public void testPropagate3() {
		
		try {
			propagate(2);
		} catch (Exception e) {
			// 如果是Error或RuntimeException 就抛出
			Throwables.throwIfUnchecked(e);
		}
	}
	
	@SuppressWarnings("null")
	private int nullPointException() throws Exception {
		try {
			Integer a = null;
			return a + 1; 
		} catch (Exception e) {
			throw new RuntimeException("空指针异常", e);
		}
	}
	
	@SuppressWarnings("null")
	private int propagate(int type) throws Exception {
		try {
			if (type == 0) {
				Integer a = null;
				return a + 1; 
			} else if (type == 1) {
				throw new IndexOutOfBoundsException("custom exception!");
			} else {
				throw new Error("system error");
			}
		} catch (Exception e) {
			throw e;
		}
	}
}
