package com.cnblogs.hoojo.io;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;

/**
 * <b>function:</b> 源 与 汇 测试

<table cellspacing="0" cellpadding="0" border="1">
<tbody>
<tr>
<td width="138"><b>&nbsp;</b></td>
<td width="240"><b>字节</b><b></b></td>
<td width="240"><b>字符</b><b></b></td>
</tr>
<tr>
<td width="138"><b>读</b><b></b></td>
<td width="240"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/io/ByteSource.html"><tt>ByteSource</tt></a></td>
<td width="240"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/io/CharSource.html"><tt>CharSource</tt></a></td>
</tr>
<tr>
<td width="138"><b>写</b><b></b></td>
<td width="240"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/io/ByteSink.html"><tt>ByteSink</tt></a></td>
<td width="240"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/io/CharSink.html"><tt>CharSink</tt></a></td>
</tr>
</tbody>
</table>
<br/>
创建源与汇
<table cellspacing="0" cellpadding="0" border="1">
<tbody>
<tr>
<td width="276"><b>字节</b><b></b></td>
<td width="342"><b>字符</b><b></b></td>
</tr>
<tr>
<td width="276"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/Files.html#asByteSource(java.io.File)"><tt>Files.asByteSource(File)</tt></a></td>
<td width="342"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/Files.html#asCharSource(java.io.File, java.nio.charset.Charset)"><tt>Files.asCharSource(File, Charset)</tt></a></td>
</tr>
<tr>
<td width="276"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/Files.html#asByteSink(java.io.File, com.google.common.io.FileWriteMode...)"><tt>Files.asByteSink(File, FileWriteMode...)</tt></a></td>
<td width="342"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/Files.html#asCharSink(java.io.File, java.nio.charset.Charset, com.google.common.io.FileWriteMode...)"><tt>Files.asCharSink(File, Charset, FileWriteMode...)</tt></a></td>
</tr>
<tr>
<td width="276"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/Resources.html#asByteSource(java.net.URL)"><tt>Resources.asByteSource(URL)</tt></a></td>
<td width="342"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/Resources.html#asCharSource(java.net.URL, java.nio.charset.Charset)"><tt>Resources.asCharSource(URL, Charset)</tt></a></td>
</tr>
<tr>
<td width="276"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/ByteSource.html#wrap(byte[])"><tt>ByteSource.wrap(byte[])</tt></a></td>
<td width="342"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/CharSource.html#wrap(java.lang.CharSequence)"><tt>CharSource.wrap(CharSequence)</tt></a></td>
</tr>
<tr>
<td width="276"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/ByteSource.html#concat(com.google.common.io.ByteSource...)"><tt>ByteSource.concat(ByteSource...)</tt></a></td>
<td width="342"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/CharSource.html#concat(com.google.common.io.CharSource...)"><tt>CharSource.concat(CharSource...)</tt></a></td>
</tr>
<tr>
<td width="276"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/ByteSource.html#slice(long, long)"><tt>ByteSource.slice(long, long)</tt></a></td>
<td width="342">N/A</td>
</tr>
<tr>
<td width="276">N/A</td>
<td width="342"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/ByteSource.html#asCharSource(java.nio.charset.Charset)"><tt>ByteSource.asCharSource(Charset)</tt></a></td>
</tr>
<tr>
<td width="276">N/A</td>
<td width="342"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/ByteSink.html#asCharSink(java.nio.charset.Charset)"><tt>ByteSink.asCharSink(Charset)</tt></a></td>
</tr>
</tbody>
</table>
<br/>
源操作
<table cellspacing="0" cellpadding="0" border="1">
<tbody>
<tr>
<td width="294"><b>字节源</b><b></b></td>
<td width="324"><b>字符源</b><b></b></td>
</tr>
<tr>
<td width="294"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/ByteSource.html#read()"><tt>byte[] &nbsp; read()</tt></a></td>
<td width="324"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/CharSource.html#read()"><tt>String &nbsp; read()</tt></a></td>
</tr>
<tr>
<td width="294">N/A</td>
<td width="324"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/CharSource.html#readLines()"><tt>ImmutableList&lt;String&gt; &nbsp; readLines()</tt></a></td>
</tr>
<tr>
<td width="294">N/A</td>
<td width="324"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/CharSource.html#readFirstLine()"><tt>String &nbsp; readFirstLine()</tt></a></td>
</tr>
<tr>
<td width="294"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/ByteSource.html#copyTo(com.google.common.io.ByteSink)"><tt>long &nbsp; copyTo(ByteSink)</tt></a></td>
<td width="324"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/CharSource.html#copyTo(com.google.common.io.CharSink)"><tt>long &nbsp; copyTo(CharSink)</tt></a></td>
</tr>
<tr>
<td width="294"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/ByteSource.html#copyTo(java.io.OutputStream)"><tt>long &nbsp; copyTo(OutputStream)</tt></a></td>
<td width="324"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/CharSource.html#copyTo(java.lang.Appendable)"><tt>long &nbsp; copyTo(Appendable)</tt> &nbsp; </a></td>
</tr>
<tr>
<td width="294"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/ByteSource.html#size()"><tt>long &nbsp; size()</tt></a> (in bytes)</td>
<td width="324">N/A</td>
</tr>
<tr>
<td width="294"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/ByteSource.html#isEmpty()"><tt>boolean &nbsp; isEmpty()</tt></a></td>
<td width="324"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/CharSource.html#isEmpty()"><tt>boolean &nbsp; isEmpty()</tt></a></td>
</tr>
<tr>
<td width="294"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/ByteSource.html#contentEquals(com.google.common.io.ByteSource)"><tt>boolean &nbsp; contentEquals(ByteSource)</tt></a></td>
<td width="324">N/A</td>
</tr>
<tr>
<td width="294"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/ByteSource.html#hash(com.google.common.hash.HashFunction)"><tt>HashCode &nbsp; hash(HashFunction)</tt></a></td>
<td width="324">N/A</td>
</tr>
</tbody>
</table>

<br/>
汇操作
<table style="width: 624px; height: 123px;" cellspacing="0" cellpadding="0" border="1">
<tbody>
<tr>
<td width="234"><b>字节汇</b><b></b></td>
<td width="384"><b>字符汇</b><b></b></td>
</tr>
<tr>
<td width="234"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/ByteSink.html#write(byte[])"><tt>void write(byte[])</tt></a></td>
<td width="384"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/CharSink.html#write(java.lang.CharSequence)"><tt>void write(CharSequence)</tt></a></td>
</tr>
<tr>
<td width="234"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/ByteSink.html#writeFrom(java.io.InputStream)"><tt>long writeFrom(InputStream)</tt></a></td>
<td width="384"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/CharSink.html#writeFrom(java.lang.Readable)"><tt>long writeFrom(Readable)</tt></a></td>
</tr>
<tr>
<td width="234">N/A</td>
<td width="384"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/CharSink.html#writeLines(java.lang.Iterable)"><tt>void writeLines(Iterable&lt;? extends CharSequence&gt;)</tt></a></td>
</tr>
<tr>
<td width="234">N/A</td>
<td width="384"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/CharSink.html#writeLines(java.lang.Iterable, java.lang.String)"><tt>void writeLines(Iterable&lt;? extends CharSequence&gt;, String)</tt></a></td>
</tr>
</tbody>
</table>

 * @author hoojo
 * @createDate 2018年2月28日 下午6:27:09
 * @file SourceSinkTest.java
 * @package com.cnblogs.hoojo.io
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class SourceSinkTest {

	File file;
	
	@Before
	public void init() {
		file = new File("c://a.txt");
	}
	
	@Test
	public void testCreate() {
		// 创建字节 汇
		Files.asByteSink(file, FileWriteMode.APPEND);
		// 创建字节 源
		Files.asByteSource(file);
		
		// 创建字符 汇
		Files.asCharSink(file, Charsets.UTF_8, FileWriteMode.APPEND);
		// 创建字符 源
		Files.asCharSource(file, Charsets.UTF_8);
	}
	
	private void out(Object o) {
		System.out.println(o);
	}
}
