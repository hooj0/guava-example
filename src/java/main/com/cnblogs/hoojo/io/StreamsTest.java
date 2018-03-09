package com.cnblogs.hoojo.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.List;
import java.util.function.Consumer;

import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteProcessor;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

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

	@SuppressWarnings("resource")
	@Test
	public void testByteStreams() throws IOException {
		File file = new File("c://tmp.txt");
		Files.touch(file);

		InputStream from = new FileInputStream("c://a.txt");
		OutputStream to = new FileOutputStream(file);

		// 通过文件流拷贝文件
		out(ByteStreams.copy(from, to)); // 21980
		to.close();
		from.close();

		ReadableByteChannel from2 = new FileInputStream("c://a.txt").getChannel();
		WritableByteChannel to2 = new FileOutputStream(file).getChannel();
		// 通过文件管道拷贝文件
		out(ByteStreams.copy(from2, to2)); // 21980

		to2.close();
		from2.close();

		// 从给定的InputStream中读取和丢弃数据，直到到达流的末尾。 返回读取的总字节数。 不关闭流。
		out(ByteStreams.exhaust(new FileInputStream("c://a.txt"))); // 21980

		// 读取前100个字节
		InputStream in = ByteStreams.limit(new FileInputStream("c://a.txt"), 100);
		byte[] buff = new byte[100];
		in.read(buff);
		out(new String(buff));

		// 字节转换为ByteArrayDataInput
		byte[] bytes = ByteStreams.toByteArray(new FileInputStream("c://a.txt"));
		ByteArrayDataInput badi = ByteStreams.newDataInput(bytes);
		out(badi.readLine());
		out(badi.readUTF());

		// ByteArrayInputStream 转换为ByteArrayDataInput
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		badi = ByteStreams.newDataInput(bais);
		out(badi.readLine());
		out(badi.readUTF());

		// 从指定位置开始读取内容
		badi = ByteStreams.newDataInput(bytes, 1000);
		out(badi.readLine());
		out(badi.readUTF());

		// 构建字节数组输出接口
		ByteArrayDataOutput bado = ByteStreams.newDataOutput();
		bado.writeUTF("这是一只中华鲟");
		out(new String(bado.toByteArray()));

		// 构建字节数组输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write("这是一只中华鲟".getBytes());
		bado = ByteStreams.newDataOutput(baos);
		out(new String(bado.toByteArray()));

		// 构建字节数组输出接口
		bado = ByteStreams.newDataOutput(10);
		bado.writeUTF("这是一只中华鲟");
		out(new String(bado.toByteArray()));

		// 构建输出流
		OutputStream os = ByteStreams.nullOutputStream();
		os.write("这是一只中华鲟".getBytes());

		// 从输入流里面读取指定字节数据，调用者自己关闭流
		in = new FileInputStream("c://a.txt");
		byte[] b = new byte[1024];
		out(ByteStreams.read(in, b, 0, 10));
		out(new String(b));

		String result = ByteStreams.readBytes(in, new ByteProcessor<String>() {
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
		out(result);

		// 读取输入流数据到字节数组，调用者自己关闭流
		in = new FileInputStream("c://a.txt");
		b = new byte[15];
		ByteStreams.readFully(in, b);
		out(new String(b));

		in = new FileInputStream("c://a.txt");
		// 跳过指定字节数
		ByteStreams.skipFully(in, 512);
		b = new byte[15];
		ByteStreams.readFully(in, b);
		out(new String(b));
	}

	@Test
	public void testCharStreams() throws IOException {
		// 构建字符输入流
		StringBuffer sb = new StringBuffer("输入流");
		Writer writer = CharStreams.asWriter(sb);
		writer.append("append new line");

		out(writer);

		writer = CharStreams.asWriter(new StringWriter());

		// 读取文件内容到指定输出流
		StringWriter to = new StringWriter();
		Readable from = new FileReader("c://d.txt");
		CharStreams.copy(from, to);
		out(to);

		// 读取并丢弃给定Readable中的数据，直到达到流的末尾。 返回读取的字符总数。 不关闭流。
		from = new FileReader("c://d.txt");
		out(CharStreams.exhaust(from)); // 16453

		// 构建空输入流
		writer = CharStreams.nullWriter();
		writer.write("空输入流");
		out(writer);

		// 读取文件内容到集合
		from = new FileReader("c://d.txt");
		List<String> list = CharStreams.readLines(from);
		list.forEach(new Consumer<String>() {
			@Override
			public void accept(String t) {
				System.out.println(t);
			}
		});

		out("------------------------------");
		from = new FileReader("c://d.txt");
		list = CharStreams.readLines(from, new LineProcessor<List<String>>() {
			final List<String> result = Lists.newArrayList();

			@Override
			public boolean processLine(String line) {
				result.add(line);
				return true;
			}

			@Override
			public List<String> getResult() {
				return result;
			}
		});

		list.forEach(new Consumer<String>() {
			@Override
			public void accept(String t) {
				System.out.println(t);
			}
		});

		out("------------------------------");
		Reader reader = new FileReader("c://d.txt");
		CharStreams.skipFully(reader, 200); // 跳过指定数量字节

		list = CharStreams.readLines(reader);
		list.forEach(new Consumer<String>() {
			@Override
			public void accept(String t) {
				System.out.println(t);
			}
		});

		out("------------------------------");
		from = new FileReader("c://d.txt");
		out(CharStreams.toString(from));
	}

	private void out(Object o) {
		System.out.println(o);
	}
}
