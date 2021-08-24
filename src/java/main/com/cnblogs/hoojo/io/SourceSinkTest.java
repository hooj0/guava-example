package com.cnblogs.hoojo.io;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.hash.Hashing;
import com.google.common.io.*;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * 源 与 汇 测试

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
@SuppressWarnings("ALL")
public class SourceSinkTest extends BasedTest {

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
	
	@Test
	public void testByteSink() throws MalformedURLException, IOException {
		// 创建“字节” 汇
		ByteSink sink = Files.asByteSink(file, FileWriteMode.APPEND);
		out(sink);
		
		// 转为“字符”汇
		CharSink charSink = sink.asCharSink(Charsets.UTF_8);
		out(charSink);
		
		// 打开bufferedStream 写入文件数据
		try {
			OutputStream os = sink.openBufferedStream();
			
			os.write(("\ntimed: " + System.currentTimeMillis()).getBytes());
			
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 打开一个新的OutputStream  写入文件数据，调用者需要关闭流
		try {
			OutputStream os = sink.openStream();
			os.write(("\n 时间: " + System.currentTimeMillis()).getBytes());
			
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 直接用字节汇写入字节到文件
		try {
			sink.write(("\n当前时间: 20180307").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 将输入流的文件内容，写入到sink的源文件
		// input的内容写入到 sink
		try {
			sink = Files.asByteSink(new File("c://sink.txt"), FileWriteMode.APPEND);
			
			InputStream input = new FileInputStream(file); // 可以不关闭文件流
			sink.writeFrom(input); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Copy the data from a URL to a file
		Resources.asByteSource(new URL("http://www.baidu.com/")).copyTo(Files.asByteSink(new File("c://url.txt")));
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testByteSource() throws IOException {
		// 创建"字节"源
		ByteSource source = Files.asByteSource(file);
		out(source);
		
		// 转换为"字符源"
		CharSource charSource = source.asCharSource(Charsets.UTF_8);
		out(charSource);
		
		// 内容比较
		try {
			out(source.contentEquals(Files.asByteSource(file))); // true
			
			out(source.contentEquals(Files.asByteSource(new File("c://c.txt")))); // false
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 将源的数据拷贝到汇；将file文件内容拷贝到s.txt
		try {
			Files.touch(new File("c://source.txt"));
			
			source.copyTo(Files.asByteSink(new File("c://source.txt"), FileWriteMode.APPEND));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 将源文件内容拷贝到输出流
		try {
			Files.touch(new File("c://source2.txt"));
			OutputStream output = new FileOutputStream("c://source2.txt");
			source.copyTo(output);
			
			output.flush();
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// hashCode
		try {
			out(source.hash(Hashing.sha256()).toString());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// hashCode 
		out(source.hashCode()); // 1023487453
		
		try {
			// 判断是否为空
			out(source.isEmpty()); // false
			out(Files.asByteSource(new File("c://empty.txt")).isEmpty()); // true
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 打开带缓冲输入流，读取内容；需要调用者关闭流
		try {
			InputStream is = source.openBufferedStream();
			byte[] buffer = new byte[1024];
			is.read(buffer);
			
			out(new String(buffer));
			
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 打开输入流，读取内容；需要调用者关闭流
		try {
			InputStream is = source.openStream();
			
			byte[] buffer = new byte[1024];
			is.read(buffer);
			
			out(new String(buffer));
			
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 直接读取源的文件数据到byte
		try {
			byte[] bytes = source.read();
			
			out(new String(bytes));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 读取文件内容
		try {
			source = Files.asByteSource(file);
			
			String str = source.read(new ByteProcessor<String>() {
				List<String> result = Lists.newArrayList();
				@Override
				public boolean processBytes(byte[] buf, int off, int len) throws IOException {
					out("----------------------------");
					out(off + "#" + len + "#" + buf.length);
					out("----------------------------");
					
					result.add(new String(buf));
					
					if (len == buf.length) {
						return true; // 为true继续处理，false为停止
					}
					return false; // finish
				}

				@Override
				public String getResult() {
					return Joiner.on("").join(result);
				}
			});
			
			out(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			// 文件长度
			out(source.size()); // 21109
			
			out(source.sizeIfKnown().get()); // 21109
			
			// 读取一段字节内容，可以知道开始的偏移量和长度
			out(new String(source.slice(0, 100).read()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 连接多个“源”
		ByteSource.concat(source, source);
		// 将字节包装成“源”
		ByteSource.wrap("pink".getBytes());
		
		//Count distinct word occurrences in a file
		Multiset<String> wordOccurrences = HashMultiset.create(Splitter.on(CharMatcher.whitespace())
				.trimResults()
				.omitEmptyStrings()
				.split(Files.asCharSource(file, Charsets.UTF_8).read()));
	}
	
	@Test
	public void testCharSink() {
		// 创建字符 汇
		CharSink sink = Files.asCharSink(file, Charsets.UTF_8, FileWriteMode.APPEND);
		out(sink);
		
		// 打开字符写入流直接写入字符
		try {
			Writer writer = sink.openBufferedStream();
			
			writer.append("\r\n这是系统时间戳：" + System.currentTimeMillis());
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 打开字符写入流直接写入字符
		try {
			Writer writer = sink.openStream();
			
			writer.append("\r\n这是系统时间戳2：" + System.currentTimeMillis());
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 直接写入字符串
		try {
			sink.write("\r\n写入一段字符串");
			
			StringReader reader = new StringReader("\r\n又写入一段字符串"); 
			// 写入内容来自于reader
			out(sink.writeFrom(reader)); //10；

			Iterable<? extends CharSequence> lines = Arrays.asList("\r\n多行写入", "\r\n多行写入2");
			sink.writeLines(lines);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCharSource2() throws Exception {
		// 创建字符 源
		CharSource source = Files.asCharSource(file, Charsets.UTF_8);
		
		// 拷贝 文件到字符流
		StringWriter writer = new StringWriter();
		out(source.copyTo(writer)); //20171
		out(writer);
		
		out("-------------------------");
		Writer appendable = CharStreams.nullWriter();
		source.copyTo(appendable);
		new BufferedWriter(appendable);
		out("-------------------------");
	}
	
	@Test
	public void testCharSource() throws Exception {
		// 创建字符 源
		CharSource source = Files.asCharSource(file, Charsets.UTF_8);
		out(source);
		
		// 转换字符源
		ByteSource byteSource = source.asByteSource(Charsets.UTF_8);
		out(byteSource);
		
		// 拷贝 文件到字符流
		StringWriter writer = new StringWriter();
		out(source.copyTo(writer)); //20171
		out(writer);
		
		out("-------------------------");
		out("-------------------------");
		out("-------------------------");
		Writer appendable = CharStreams.nullWriter();
		source.copyTo(appendable);
		out(appendable);
		out("-------------------------");
		out("-------------------------");
		out("-------------------------");
		
		
		// 拷贝文件内容到字符汇对应的文件中
		CharSink sink = Files.asCharSink(new File("c://char.txt"), Charsets.UTF_8, FileWriteMode.APPEND);
		source.copyTo(sink);
		
		// 循环行内容
		source.forEachLine(new Consumer<String>() {
			@Override
			public void accept(String t) {
				out(t);
			}
		});
		
		out(source.hashCode()); // 1744347043
		// 判断是否为空文件
		out(source.isEmpty()); // false
		// 获取源字符长度
		out(source.length()); // 20171
		out(source.lengthIfKnown().isPresent()); // 20171
		out(file.length()); // 21913
		
		Stream<String> lines = source.lines();
		lines.spliterator().forEachRemaining(new Consumer<String>() {
			@Override
			public void accept(String t) {
				out(t);
			}
		});
		
		// 缓冲读取器
		BufferedReader reader = source.openBufferedStream();
		out(reader.readLine());
		
		// 读取器
		Reader read = source.openStream();

		// 读取字符方式
		char[] cbuf = new char[1024];
		read.read(cbuf);
		out(new String(cbuf));
		
		// 读取字符缓冲区方式
		CharBuffer buffer = CharBuffer.allocate(1024);
		read.read(buffer);
		out(new String(buffer.array()));
		
		// 直接读取所有内容
		out(source.read());
		
		// 读取首行
		out(source.readFirstLine());
		
		// 读取多行，返回集合
		out(source.readLines().size()); // 85
		
		List<String> lines2 = Lists.newArrayList();
		String result = source.readLines(new LineProcessor<String>() {
			@Override
			public boolean processLine(String line) throws IOException {
				lines2.add(line);
				out(line);
				return line != null;
			}

			@Override
			public String getResult() {
				return Joiner.on("").join(lines2);
			}
		});
		out(result);
		
		// 连接多个字符源
		CharSource.concat(source, source);
		// 创建一个空的源
		CharSource.empty();
		
		// 包装字符源
		CharSource.wrap("");
	}
}
