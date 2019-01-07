package com.cnblogs.hoojo.io;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.List;
import java.util.function.Consumer;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;
import com.google.common.io.Resources;

/**
 * 远程和本地文件资源读取
 * @author hoojo
 * @createDate 2018年3月8日 下午4:58:08
 * @file ResourcesTest.java
 * @package com.cnblogs.hoojo.io
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ResourcesTest {

	@Test
	public void test1() throws Exception {
		URL url = new URL("http://www.baidu.com/");
		
		// 读取远程URL到字节源
		ByteSource source = Resources.asByteSource(url);
		
		System.out.println(source.size());
		System.out.println(new String(source.read()));
		System.out.println(source.asCharSource(Charsets.UTF_8).read());
		
		// 读远程url到字符源
		System.out.println(Resources.asCharSource(url, Charsets.UTF_8).read());
		
		// 复制远程url内容到指定输出流
		ByteArrayOutputStream to = new ByteArrayOutputStream();
		Resources.copy(new URL("https://sina.com.cn"), to);
		System.out.println(to.toString());
		
		// 读取classpath资源
		System.out.println(Resources.getResource("com/cnblogs/hoojo/io/ResourcesTest.class"));
		
		System.out.println(Resources.getResource(getClass(), "ResourcesTest.class"));
		
		
		// 读取内容到集合
		List<String> list = Resources.readLines(url, Charsets.UTF_8);
		list.forEach(new Consumer<String>() {
			@Override
			public void accept(String t) {
				System.out.println(t);
			}
		});
		
		// 读取远程url到字节数组
		System.out.println(new String(Resources.toByteArray(url)));
		
		// 直接将远程url转换string
		System.out.println(Resources.toString(url, Charsets.UTF_8));
	}
}
