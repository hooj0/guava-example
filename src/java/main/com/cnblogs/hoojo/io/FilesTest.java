package com.cnblogs.hoojo.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.primitives.Bytes;

/**
 * <b>function:</b> 文件操作
 * @author hoojo
 * @createDate 2018年2月28日 下午6:06:53
 * @file FilesTest.java
 * @package com.cnblogs.hoojo.io
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 * 
 * <table style="width: 624px; height: 93px;" cellspacing="0" cellpadding="0" border="1">
<tbody>
<tr>
<td width="264"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/io/Files.html#createParentDirs(java.io.File)"><tt>createParentDirs(File)</tt></a></td>
<td width="354">必要时为文件创建父目录</td>
</tr>
<tr>
<td width="264"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/io/Files.html#getFileExtension(java.lang.String)"><tt>getFileExtension(String)</tt></a></td>
<td width="354">返回给定路径所表示文件的扩展名</td>
</tr>
<tr>
<td width="264"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/Files.html#getNameWithoutExtension(java.lang.String)"><tt>getNameWithoutExtension(String)</tt></a></td>
<td width="354">返回去除了扩展名的文件名</td>
</tr>
<tr>
<td width="264"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/io/Files.html#simplifyPath(java.lang.String)"><tt>simplifyPath(String)</tt></a></td>
<td width="354">规范文件路径，并不总是与文件系统一致，请仔细测试</td>
</tr>
<tr>
<td width="264"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/io/Files.html#fileTreeTraverser()"><tt>fileTreeTraverser()</tt></a></td>
<td width="354">返回TreeTraverser用于遍历文件树</td>
</tr>
</tbody>
</table>
 */
public class FilesTest {

	File file;
	
	@Before
	public void init() {
		file = new File("c://a.txt");
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testCopyFiles() {
		
		try {
			// 复制文件 -> 文件
			Files.copy(file, new File("c://b.txt"));
			
			// 复制文件 -> 文件流
			OutputStream os = new FileOutputStream("c://c.txt");
			Files.copy(file, os);
			os.flush();
			os.close();
			
			// 复制文件
			Files.copy(file, Charsets.UTF_8, new PrintStream("c://d.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testReadFiles() {
		// 读取文件到buffer
		MappedByteBuffer buff;
		try {
			buff = Files.map(file);
			out(buff);
			
			buff = Files.map(file, MapMode.READ_ONLY);
			out(buff);
			
			buff = Files.map(file, MapMode.READ_ONLY, 200);
			out(buff);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		try {
			BufferedReader reader = Files.newReader(file, Charsets.UTF_8);
			out(reader);
			
			BufferedWriter writer = Files.newWriter(file, Charsets.UTF_8);
			out(writer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFiles() {
		
		try {
			// 创建父目录
			Files.createParentDirs(new File("c://a//b//c.txt"));
			
			// 创建临时目录
			Files.createTempDir();
			
			// 如果给定的文件存在，不是目录，并且包含相同的字节，则返回true。
			out(Files.equal(file, new File("c://c.txt"))); // true
			
			out(Files.fileTreeTraverser());
			
			// 获取后缀名
			out(Files.getFileExtension(file.getAbsolutePath())); // txt
			
			// 获取文件名不包含后缀名
			out(Files.getNameWithoutExtension(new File("c://offline_FtnInfo.txt").getAbsolutePath())); //offline_FtnInfo a
			
			out(Files.isDirectory().test(new File("c://a//b"))); // true
			out(Files.isDirectory().test(file)); // false

			out(Files.isFile().test(new File("c://a//b"))); // false
			out(Files.isFile().test(file)); // true
			
			// 移动文件
			if (new File("c://b.txt").exists()) {
				Files.move(new File("c://b.txt"), new File("c://a//tmp.txt"));
			}
			
			// 读取文件内容到list
			out(Files.readLines(file, Charsets.UTF_8).size());
			// 返回合法的文件路径
			out(Files.simplifyPath("../a/b/../c.exe")); // ../a/c.exe
			
			// 转换成byte
			out(Bytes.asList(Files.toByteArray(file)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void out(Object o) {
		System.out.println(o);
	}
}
