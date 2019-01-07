package com.cnblogs.hoojo.io;

import java.io.File;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

/**
 * 文件映射为内存映像文件
 * http://download.oracle.com/technetwork/java/javase/6/docs/zh/api/java/nio/class-use/ByteBuffer.html
 * https://www.cnblogs.com/chenpi/p/6475510.html
 * 
 * FileChannel提供了map方法来把文件映射为内存映像文件：MappedByteBuffer map(int mode,long position,long size); 
	  可以把文件的从position开始到size大小的区域映射为内存映像文件，
	
	模式： 
	 	mode: 可访问该内存映像文件的方式：READ_ONLY,READ_WRITE,PRIVATE.                     
		a. READ_ONLY,（只读）： 试图修改得到的缓冲区将导致抛出 ReadOnlyBufferException.(MapMode.READ_ONLY)
      	b. READ_WRITE（读/写）： 对得到的缓冲区的更改最终将传播到文件；该更改对映射到同一文件的其他程序不一定是可见的。 (MapMode.READ_WRITE)
        c. PRIVATE（专用）： 可读可写,但是修改的内容不会写入文件,只是buffer自身的改变，这种能力称之为”copy on write”
       					对得到的缓冲区的更改不会传播到文件，并且该更改对映射到同一文件的其他程序也不是可见的；相反，会创建缓冲区已修改部分的专用副本。 (MapMode.PRIVATE)
     
         方法：   
        a. fore(); 缓冲区是READ_WRITE模式下，此方法对缓冲区内容的修改强行写入文件
		b. load(); 将缓冲区的内容载入内存，并返回该缓冲区的引用
		c. isLoaded(); 如果缓冲区的内容在物理内存中，则返回真，否则返回假
		
	特征：
		a. 读取快
		b. 写入快
		c. 随时随地写入
		

	position：当前读取的位置 
	mark：为某一读过的位置做标记，便于有时候回退到该位置 
	capacity：初始化时候的容量 
	limit：读写的上限，limit <= capacity 
	flip() 方法写完数据需要开始读的时候，将position复位到0，并将limit设为当前position
	clear() 为下一次写入准备
	flip()  为下一次读取准备
		
 * @author hoojo
 * @createDate 2018年3月1日 下午4:38:34
 * @file MappedByteBufferTest.java
 * @package com.cnblogs.hoojo.io
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class MappedByteBufferTest {

	File file;
	
	@Before
	public void init() {
		file = new File("c://a.txt");
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testReadFile() {
		MappedByteBuffer buff;
		try {
			buff = Files.map(file, MapMode.READ_WRITE);
			out(buff);

			/*
			 * position：当前读取的位置 
			 * mark：为某一读过的位置做标记，便于有时候回退到该位置 
			 * capacity：初始化时候的容量 
			 * limit：读写的上限，limit <= capacity
			 */
			out("初始化读取文件--------------------");
			out("limit: " + buff.limit());
			out("capacity: " + buff.capacity());
			out("position: " + buff.position());

			// 缓冲区的内容是否在物理内存中
			out(buff.isLoaded());
			// out(buff.load().isLoaded());

			// 读取buffer内容，将buffer 转换编码，改变position
			String result = Charsets.UTF_8.decode(buff).toString();
			// out(result);
			out("读取输出文件内容--------------------");
			out("limit: " + buff.limit());
			out("capacity: " + buff.capacity());
			out("position: " + buff.position());

			// get(index) 按指定位置读取内容，不改变position
			out(buff.get(0));
			out("按索引位置读取--------------------");
			out("limit: " + buff.limit());
			out("capacity: " + buff.capacity());
			out("position: " + buff.position());

			buff.clear(); // clear会改变limit=capacity
			out(buff.get());
			out("clear 方法后读取一个字节--------------------");
			out("limit: " + buff.limit());
			out("capacity: " + buff.capacity());
			out("position: " + buff.position());

			// flip()方法写完数据需要开始读的时候，将position复位到0，并将limit设为当前position
			buff.flip();
			out("flip后读取内容--------------------");
			out("limit: " + buff.limit());
			out("capacity: " + buff.capacity());
			out("position: " + buff.position());

			buff.clear();
			while (buff.hasRemaining()) {
				System.out.print((char) buff.get());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testReadBigFile() {
		MappedByteBuffer buff;
		try {
			buff = Files.map(file, MapMode.READ_ONLY);
			out(buff);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testWriteFile() {
		MappedByteBuffer buff;
		try {
			// 扩充1024字节空间，写入数据
			buff = Files.map(file, MapMode.READ_WRITE, file.length() + 1024);

			// 将buffer 转换编码
			String result = Charsets.UTF_8.decode(buff).toString();
			out(result);

			// 写内容到内存副本
			byte[] bytes = ("\n当前时间：" + System.currentTimeMillis()).getBytes();
			int position = buff.limit() - bytes.length;
			// 移动指针位置
			buff.position(position);
			// 写入文件内容
			buff.put(bytes);
			// 强行写入文件
			buff.force();

			buff.flip();

			out(buff.limit());
			out(buff.capacity());
			result = Charsets.UTF_8.decode(buff).toString();
			out(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testWirte() {
		MappedByteBuffer buff;
		try {
			File file = new File("c://write.txt");
			if (!file.exists()) {
				file.createNewFile();
			} else {
				file.delete();
			}
			// 设置好映射区间
			buff = Files.map(file, MapMode.READ_WRITE, 1024);

			out("capacity: " + buff.capacity());
			out("limit: " + buff.limit());
			out("position: " + buff.position());

			// 将buffer 转换编码
			String result = Charsets.UTF_8.decode(buff).toString();
			out(result);

			out("capacity: " + buff.capacity());
			out("limit: " + buff.limit());
			out("position: " + buff.position());

			buff.clear(); // 转换为写入模式
			// buff.position(0);
			out("capacity: " + buff.capacity());
			out("limit: " + buff.limit());
			out("position: " + buff.position());

			// 写入一个byte
			buff.put((byte) 'b');
			// 写入一个byte数组
			buff.put("china".getBytes());
			// 写入字符
			buff.putChar('a');
			buff.putDouble(3.22D);
			buff.putFloat(1.2F);
			buff.putInt(12);
			buff.putLong(System.currentTimeMillis());
			buff.putShort(Short.MAX_VALUE);

			int i = buff.position() + 5;
			buff.position(i + 5);
			buff.put("abcdefg".getBytes(), 0, "abcdefg".getBytes().length);
			buff.putChar(i + 10, 'a');
			buff.putDouble(i + 10, 3.22D);
			buff.putFloat(i + 10, 1.2F);
			buff.putInt(i + 10);
			buff.putLong(i + 10, System.currentTimeMillis());
			buff.putShort(i + 100, Short.MAX_VALUE);

			buff.force();

			buff = Files.map(file, MapMode.READ_ONLY);
			result = Charsets.UTF_8.decode(buff).toString();
			out(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCopyFile() {

		try {
			MappedByteBuffer fromBuff = Files.map(file, MapMode.READ_ONLY);

			File temp = new File("c://temp.txt");
			if (!temp.exists()) {
				temp.createNewFile();
			}

			MappedByteBuffer toBuff = Files.map(temp, MapMode.READ_WRITE, file.length());

			// 写入文件内容
			toBuff.put(fromBuff);
			// 强行写入文件
			toBuff.force();

			toBuff.flip();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testBuff() {
		// 创建Buffer
		CharBuffer buffer = CharBuffer.allocate(8);
		out("capacity: " + buffer.capacity());
		out("limit: " + buffer.limit());
		out("position: " + buffer.position());

		// 添加元素
		buffer.put("a");
		buffer.put("b");
		buffer.put("c");

		out("加入元素后的位置， position：" + buffer.position());

		buffer.flip();
		out("执行flip方法后， position：" + buffer.position());
		out("执行flip方法后， limit：" + buffer.limit());

		// 取得第一个元素
		out("第一个元素（position=0）：" + buffer.get());
		out("获取第一个元素（position=0）：" + buffer.position());

		// 调用clear
		out("执行clear后的，limit：" + buffer.limit());
		out("执行clear后的，position：" + buffer.position());
		out("执行clear后的，缓冲区内容没有清除：" + buffer.get(2));
		out("执行clear后的，limit：" + buffer.limit());
		out("执行clear后的，position：" + buffer.position());
	}
	
	private void out(Object o) {
		System.out.println(o);
	}
}
