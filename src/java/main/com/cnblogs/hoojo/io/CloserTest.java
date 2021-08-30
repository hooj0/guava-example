package com.cnblogs.hoojo.io;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.io.Closer;
import org.junit.Test;

import java.io.*;

/**
 * file & stream close resouces
 * Closeable 完成后正确关闭资源对于确保关闭套接字连接，不泄漏文件描述符等以及确保正确的程序行为非常重要。
 * 
 * @author hoojo
 * @createDate 2018年12月26日 下午5:56:31
 * @file CloserTest.java
 * @package com.cnblogs.hoojo.io
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class CloserTest extends BasedTest {

	@Test
	public void test1() throws IOException {
		// Closer是在14.0版本中添加到Guava的类。
		// 它的目的是作为一个穷人的try-with-resources块，用于必须在JDK6下编译和运行的代码。
		Closer closer = Closer.create();
		try {
			File file = new File("");
			InputStream is = closer.register(new FileInputStream(file));
			OutputStream os = closer.register(new FileOutputStream(file));
			
			is.read(); 
			os.write(0);
		} catch (Exception e) {
			closer.rethrow(e);
		} finally {
			closer.close();	// 关闭释放资源
		}
	}
}
