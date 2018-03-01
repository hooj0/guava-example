package com.cnblogs.hoojo.io;

import org.junit.Test;

/**
 * <b>function:</b> 字符流、字节流测试
 * 

<table cellspacing="0" cellpadding="0" border="1">
<tbody>
<tr>
<td width="312"><b>ByteStreams</b></td>
<td width="307"><b>CharStreams</b></td>
</tr>
<tr>
<td width="312"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/io/ByteStreams.html#toByteArray(java.io.InputStream)"><tt>byte[] toByteArray(InputStream)</tt></a></td>
<td width="307"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/io/CharStreams.html#toString(java.lang.Readable)"><tt>String toString(Readable)</tt></a></td>
</tr>
<tr>
<td width="312">N/A</td>
<td width="307"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/CharStreams.html#readLines(java.lang.Readable)"><tt>List&lt;String&gt; readLines(Readable)</tt></a></td>
</tr>
<tr>
<td width="312"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/io/ByteStreams.html#copy(java.io.InputStream, java.io.OutputStream)"><tt>long copy(InputStream, OutputStream)</tt></a></td>
<td width="307"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/io/CharStreams.html#copy(java.lang.Readable, java.lang.Appendable)"><tt>long copy(Readable, Appendable)</tt></a></td>
</tr>
<tr>
<td width="312"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/io/ByteStreams.html#readFully(java.io.InputStream, byte[])"><tt>void readFully(InputStream, byte[])</tt></a></td>
<td width="307">N/A</td>
</tr>
<tr>
<td width="312"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/ByteStreams.html#skipFully(java.io.InputStream, long)"><tt>void skipFully(InputStream, long)</tt></a></td>
<td width="307"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/CharStreams.html#skipFully(java.io.Reader, long)"><tt>void skipFully(Reader, long)</tt></a></td>
</tr>
<tr>
<td width="312"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/ByteStreams.html#nullOutputStream()"><tt>OutputStream nullOutputStream()</tt></a></td>
<td width="307"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/CharStreams.html#nullWriter()"><tt>Writer nullWriter()</tt></a></td>
</tr>
</tbody>
</table>

 * @author hoojo
 * @createDate 2018年2月28日 下午6:22:37
 * @file StreamsTest.java
 * @package com.cnblogs.hoojo.io
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class StreamsTest {

	@Test
	public void testByteStreams() {
		
	}
	
	@Test
	public void testCharStreams() {
		
	}
}
