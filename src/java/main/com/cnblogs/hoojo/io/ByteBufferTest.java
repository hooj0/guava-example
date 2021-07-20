package com.cnblogs.hoojo.io;

import com.cnblogs.hoojo.BasedTest;
import org.junit.Test;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

/**
 * nio byte buffer
 * 
 * 1、capacicty：作为一个内存块，Buffer有一个固定的大小值，也叫“capacity”
 * 	你只能往里写capacity个byte、long，char等类型。一旦Buffer满了，需要将其清空（通过读数据或者清除数据）才能继续写数据往里写数据。
 * 	一般操作只需用调用clear() 或 rewind()
 * 
 * 2、position：当你写数据到Buffer中时，position表示当前的位置
 * 	初始的position值为0，当一个byte、long等数据写到Buffer后， position会向后移动到下一个可插入数据的Buffer单元。因此写入数据会改变position的值。
 * 	position最大可为capacity – 1，当读取数据时，也是从某个特定位置读。
 * 
 * 	当将Buffer从写模式切换到读模式，position会被重置为0。
 * 	当从Buffer的position处读取数据时，position向后移动到下一个可读的位置。
 * 
 * 3、limit：表示可以读写多少数据量
 * 	在写模式下，Buffer的limit表示你最多能往Buffer里写多少数据。写模式下，limit等于Buffer的capacity。
 * 	当切换Buffer到读模式时，limit表示你最多能读到多少数据。因此，当切换Buffer到读模式时，limit会被设置成写模式下的position值。
 * 	换句话说，你能读到之前写入的所有数据（limit被设置成已写数据的数量，这个值在写模式下就是position） 
 * 
 * 4、flip：将Buffer从写模式切换到读模式。
 * 	调用flip()方法会将position设回0，并将limit设置成之前position的值。
 * 	换句话说，position现在用于标记读的位置，limit表示之前写进了多少个byte、char等 (现在能读取多少个byte、char等)。
 * 
 * 5、rewind：将position设回0，所以你可以重读Buffer中的所有数据
 * 	limit保持不变，仍然表示能从Buffer中读取多少个元素（byte、char等）。
 * 	一旦读完Buffer中的数据，需要让Buffer准备好再次被写入。可以通过clear()或compact()方法来完成。 
 * 
 * 6、clear：切换到写入模式
 * 	如果调用的是clear()方法，position将被设回0，limit被设置成 capacity的值
 * 	换句话说，Buffer被清空了。Buffer中的数据并未清除，只是这些标记告诉我们可以从哪里开始往Buffer里写数据。
 * 	如果Buffer中有一些未读的数据，调用clear()方法，数据将“被遗忘”，意味着不再有任何标记会告诉你哪些数据被读过，哪些还没有。
 * 
 * 7、compact：将所有未读的数据拷贝到Buffer起始处，并覆盖掉从0到copy_buff.length - 1 的数据。
 * 	如果Buffer中仍有未读的数据，且后续还需要这些数据，但是此时想要先写些数据，那么使用compact()方法。
 * 	将所有未读的数据拷贝到Buffer起始处，并覆盖掉从0到copy_buff.length - 1 的数据。
 * 	然后将position设到copy_buff.length的值。limit属性依然像clear()方法一样，设置成capacity。
 * 	现在Buffer准备好写数据了，但是不会覆盖掉刚才复制copy到缓冲区最前端的未读数据。
 * 
 * 
 * @author hoojo
 * @createDate 2018年3月5日 下午3:15:48
 * @file ByteBufferTest.java
 * @package com.cnblogs.hoojo.io
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class ByteBufferTest extends BasedTest {

	@Test
	public void test1() {

		ByteBuffer buffer = ByteBuffer.allocate(5);
		out("------初始化数据------");
		out("初始化：" + buffer.position()); // 0
		out("初始化：" + buffer.limit()); // 5
		out("初始化：" + buffer.capacity()); // 5

		out("------put数据------");
		buffer.put((byte) 1);

		// put数据后，会导致position向后移动
		out("放入1个字节：" + buffer.position()); // 1
		out("放入1个字节：" + buffer.limit());  // 5
		out("放入1个字节：" + buffer.capacity()); // 5
		
		out(buffer); // 1, 

		out("------flip 读取模式 数据------");
		buffer.flip();

		out("flip之后：" + buffer.position()); // 0
		out("flip之后：" + buffer.limit()); // 1 表示只能读取一个数据
		out("flip之后：" + buffer.capacity()); // 5

		out("------get数据------");
		// get数据后，会导致position向后移动
		buffer.get();

		out("拿出一个字节：" + buffer.position()); // 1
		out("拿出一个字节：" + buffer.limit()); // 1 
		out("拿出一个字节：" + buffer.capacity()); // 5

		//buffer.get(); // java.nio.BufferUnderflowException
		
		out(buffer); // 1, 
		
		out("------flip 读取模式 数据------");
		buffer.flip();

		out("flip之后：" + buffer.position()); // 0
		out("flip之后：" + buffer.limit()); // 1 表示只能读取一个数据
		out("flip之后：" + buffer.capacity()); // 5
		
		out("------put数据------");
		// 并且flip会覆盖掉之前写入的数据
		buffer.put((byte) 2);
		// 由于调用flip 没有改变limit，导致写入数据java.nio.BufferOverflowException
		//buffer.put((byte) 2);
		
		out("放入1个字节：" + buffer.position()); // 1
		out("放入1个字节：" + buffer.limit());  // 1
		out("放入1个字节：" + buffer.capacity()); // 5
		
		out(buffer); // 2, 
		
		out("------clear 写入模式 数据------");
		buffer.clear();

		out("clear之后：" + buffer.position()); // 0
		out("clear之后：" + buffer.limit()); // 5 表示只能读取5个数据
		out("clear之后：" + buffer.capacity()); // 5
		
		// clear后， 可以重新写入数据；写入的数据会覆盖掉之前的旧数据
		buffer.put((byte) 2);
		buffer.put((byte) 3);
		
		out("------flip 读取模式 数据------");
		buffer.flip();

		out("flip之后：" + buffer.position()); // 0
		out("flip之后：" + buffer.limit()); // 2
		out("flip之后：" + buffer.capacity()); // 5
		
		out(buffer); // 2, 3, 
		
		out("------rewind 重读取模式 数据------");
		buffer.rewind(); // 重读所有数据，和flip不同的是，rewind只改变position和mark，不改变limit

		out("rewind之后：" + buffer.position()); // 0
		out("rewind之后：" + buffer.limit()); // 2
		out("rewind之后：" + buffer.capacity()); // 5
		
		out(buffer); // 2, 3, 
		
		out("------------");
		// 继续在上一次put数据后，继续写入数据
		buffer.position(buffer.limit());
		buffer.limit(buffer.capacity());
		
		buffer.put((byte) -1);
		buffer.put((byte) -2);
		
		out("position：" + buffer.position()); // 4
		out("limit：" + buffer.limit()); // 5
		out("capacity：" + buffer.capacity()); // 5
		
		out("------flip 读取模式 数据------");
		buffer.flip();
		out("flip之后：" + buffer.position()); // 0
		out("flip之后：" + buffer.limit()); // 4
		out("flip之后：" + buffer.capacity()); // 5
		
		out(buffer); // 2, 3, -1, -2, 
		
		buffer.hasRemaining();
	}
	
	@Test
	public void test2() {
		String str = "JavaNioByteBuffer";
		
		ByteBuffer buff = ByteBuffer.wrap(str.getBytes());
		
		out("position: " + buff.position() + "\t limit: " + buff.limit()); // position: 0	 limit: 17
		// 读取两个字节
		buff.get();
		buff.get();
		
		out("position: " + buff.position() + "\t limit: " + buff.limit()); // position: 2	 limit: 17
		out("char: " + (char) buff.get(buff.position()) + "\t limit: " + buff.limit()); // char: v	 limit: 17
		
		buff.mark();
		out("position: " + buff.position() + "\t limit: " + buff.limit()); // position: 2	 limit: 17
		
		buff.flip();
		out("position: " + buff.position() + "\t limit: " + buff.limit()); // position: 0	 limit: 2

		buff.compact();
		out("position: " + buff.position() + "\t limit: " + buff.limit()); // position: 2	 limit: 17

		buff.clear();
		out("position: " + buff.position() + "\t limit: " + buff.limit()); // position: 0	 limit: 17
	}

	@Test
	public void test3() {
		ByteBuffer buffer = ByteBuffer.allocate(16);
		byte[] bytes = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
		
		out("--------初始化ByteBuffer--------");
		out("capacity:" + buffer.capacity()); // 16
		out("limit:" + buffer.limit()); // 16
		out("position:" + buffer.position()); // 0
		
		buffer.put(bytes);
		out("put byte[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15} into buffer.");
		
		out("--------写入16个字节数据--------");
		out("limit:" + buffer.limit()); // 16
		out("position:" + buffer.position()); // 16
		
		buffer.flip();// 数据由写转为读取
		
		out("--------执行flip，转为读取--------");
		out("limit:" + buffer.limit());  // 16
		// flip后 position会被置为 0
		out("position:" + buffer.position()); // 0
		
		byte[] dst = new byte[10];
		// 读取10个字节
		buffer.get(dst, 0, dst.length);
		out(String.format("byte[]:%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", dst[0], dst[1], dst[2], dst[3], dst[4], dst[5], dst[6], dst[7], dst[8], dst[9]));
		
		out("flip-读取完10个字节的数据后:");
		out("limit:" + buffer.limit()); // 16
		// 读取10个字节，position也移动到了10
		out("position:" + buffer.position()); // 10
		
		//倒回这个缓冲区。 该位置设置为零，标记被丢弃。
		buffer.rewind(); // rewind 会把position置为0，mark置为-1
		
		out("--------执行rewind，重新读取数据--------");
		out("limit:" + buffer.limit()); // 16
		out("position:" + buffer.position()); // 0
		
		dst = new byte[10];
		// 读取10个字节
		buffer.get(dst, 0, dst.length);
		out(String.format("byte[]:%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", dst[0], dst[1], dst[2], dst[3], dst[4], dst[5], dst[6], dst[7], dst[8], dst[9]));
		
		out("rewind-读取完10个字节的数据后:");
		out("limit:" + buffer.limit()); // 16
		out("position:" + buffer.position()); // 10
		
		out("--------在当前位置做标记mark--------");
		buffer.mark(); // 打标记
		// 读取一个字节
		out(buffer.get()); // 10
		out(buffer.get()); // 11
		out(buffer.get()); // 12
		
		out("读取3个字节后position:" + buffer.position()); // 13
		//buffer.rewind();
		buffer.reset(); // rewind会清除掉mark的位置，故抛出异常
		
		// mark后会标记之前的位置，reset后会将position置为mark时的位置
		out("执行reset后position的位置:" + buffer.position()); // 10
		//buffer.clear();
		out(buffer.get(3)); // 3
		// 从mark的地方开始取得数据
		out(buffer.get()); // 10
		
		buffer.reset();
		out("执行reset后position的位置:" + buffer.position()); // 10
		out(buffer); // 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, [10, 11, 12, 13, 14, 15,]
		
		// mark标记后调用compact，会压缩数据。将mark后的数据复制到最前面覆盖掉源数据，position会移动到复制元素的 长度 + 1
		buffer.compact();
		out(buffer); // 10, 11, 12, 13, 14, 15, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,

		out("--------执行compact后--------");
		out("limit:" + buffer.capacity()); // 16
		out("limit:" + buffer.limit()); // 16
		// position会移动的复制元素的 长度 + 1
		out("position:" + buffer.position()); // 6
		out(buffer.get(3)); // 13
		// 获取当前position位置的字节
		out(buffer.get()); // 6
		
		out("取出10个字节后，执行完compact后ByteBuffer第一个字节:" + buffer.get(0));
		
		out("------清除缓冲区，重新添加数据------");
		buffer.clear();
		out("limit:" + buffer.capacity()); // 16
		out("limit:" + buffer.limit()); // 16
		// position会移动的复制元素的 长度 + 1
		out("position:" + buffer.position()); // 6
		
		// 清除缓冲区不会清除数据，只会改变position、limit、mark
		out(buffer); // 10, 11, 12, 13, 14, 15, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 

		buffer.put("abcdefg".getBytes());
		// 再次put会覆盖掉数据，其他没有覆盖的数据还继续保留
		out(buffer); // 97, 98, 99, 100, 101, 102, 103, 7, 8, 9, 10, 11, 12, 13, 14, 15,  
	}
	
	@Test
	public void testCompact() {
		ByteBuffer buffer = ByteBuffer.allocate(16);
		byte[] bytes = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
		
		out("--------初始化ByteBuffer--------");
		out("capacity:" + buffer.capacity()); // 16
		out("limit:" + buffer.limit()); // 16
		out("position:" + buffer.position()); // 0
		
		out("--------添加数据到ByteBuffer--------");
		buffer.put(bytes);
		out("capacity:" + buffer.capacity()); // 16
		out("limit:" + buffer.limit()); // 16
		out("position:" + buffer.position()); // 16
		
		out(buffer); // 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
		
		out("--------切换到读取模式--------");
		// 切换到读取模式
		buffer.flip();
		out("capacity:" + buffer.capacity()); // 16
		out("limit:" + buffer.limit()); // 16
		out("position:" + buffer.position()); // 0
		
		// 读取5个byte [0, 1, 2, 3, 4]
		buffer.get();
		buffer.get();
		buffer.get();
		buffer.get();
		buffer.get(); 
		
		out("capacity:" + buffer.capacity()); // 16
		out("limit:" + buffer.limit()); // 16
		out("position:" + buffer.position()); // 5
		
		out("--------复制未读数据模式--------");
		// 复制未读数据到缓冲区最前端
		buffer.compact();
		out("capacity:" + buffer.capacity()); // 16
		out("limit:" + buffer.limit()); // 16
		out("position:" + buffer.position()); // 11
		
		out(buffer); // 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, <11, 12, 13, 14, 15,>
		
		// 复制未读数据到缓冲区最前端，由于未读数据过长，把本读到position=5后的未读数据都覆盖掉，只剩下缓冲区末端的数据
		out(buffer.get()); // 11
		out(buffer.get()); // 12
		
		// 继续写入数据，不会覆盖掉刚才拷贝到最前端的数据
		buffer.put((byte) -1);
		buffer.put((byte) -2);
		buffer.put((byte) -3);
		
		out(buffer); // 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 11, 12, -1, -2, -3,
	}
	
	/** 
     * 初始化缓存空间 
     */  
	@Test
	public void initByteBuffer() {
		ByteBuffer byteBuffer = ByteBuffer.allocate(32);
		out("===============init status============");
		out("position:" + byteBuffer.position()); // 0
		out("limit:" + byteBuffer.limit()); // 32
		out("capacity:" + byteBuffer.capacity()); // 32
	}
     
	/**
	 * 测试Byte，占用一个字节
	 */
	@Test
	public void testByte() {
		ByteBuffer buffer = ByteBuffer.allocate(32);

		out("===============put byte============");
		
		// 字节
		byte b = 102;
		buffer.put(b);// ByteBuffer
		buffer.get(0);// byte
		
		out("position:" + buffer.position()); // 1
		out("limit:" + buffer.limit()); // 32
		out("capacity:" + buffer.capacity()); // 32
		out("======get byte:" + buffer.get(0)); // 102
	}
	
	/**
	 * 测试Char，占用2个字节
	 */
	@Test
	public void testChar() {
		ByteBuffer buffer = ByteBuffer.allocate(32);

		out("===============put char============");
		
		buffer.put((byte) 103);
		// 字符
		char aChar = 'a';
		buffer.putChar(aChar); // 1 char=2 byte
		
		out("position:" + buffer.position()); // 3，1 char=2 byte
		out("limit:" + buffer.limit()); // 32
		out("capacity:" + buffer.capacity()); // 32
		
		out("======get Char:" + buffer.getChar(1));
	}
    
	/**
	 * 标记位置，以便reset，返回这个标记位置
	 */
	@Test
	public void testMarkReset() {
		ByteBuffer buffer = ByteBuffer.allocate(32);

		buffer.put((byte) 103);
		buffer.putChar('a');

		// 标记位置
		buffer.mark(); // position=3
		
		out("===============mark============");
		out("position:" + buffer.position()); // 3
		out("limit:" + buffer.limit()); // 32
		out("capacity:" + buffer.capacity()); // 32
		
		buffer.get();
		buffer.get();
		
		out("===============get============");
		out("position:" + buffer.position()); // 5
		out("limit:" + buffer.limit()); // 32
		out("capacity:" + buffer.capacity()); // 32
		
		out("===============reset============");
		// position=3
		buffer.reset(); // position 回到之前的位置
		out("position:" + buffer.position()); // 3
		out("limit:" + buffer.limit()); // 32
		out("capacity:" + buffer.capacity()); // 32
		
		out(buffer); // 103, 0, 97, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		
		// position=3 // 从标记位置put一个int值，原来的内容被覆盖掉
		buffer.put((byte) -1);
		buffer.put((byte) -2);

		out(buffer); // 103, 0, 97, -1, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
		
		out("position:" + buffer.position()); // 5
		out("limit:" + buffer.limit()); // 32
		out("capacity:" + buffer.capacity()); // 32
		
		testClear();
	}
     
	/**
	 * 测试int，占用4个字节
	 */
	@Test
	public void testInt() {
		ByteBuffer buffer = ByteBuffer.allocate(32);

		buffer.put((byte) 103); // 1 byte
		buffer.putChar('a'); // 2 byte
		
		out("===============put int============");
		// int
		int int4 = 4;
		buffer.putInt(int4); // 4 byte
		
		out("position:" + buffer.position()); // 7 = 1b + 2b + 4b
		out("limit:" + buffer.limit()); // 32
		out("capacity:" + buffer.capacity()); // 32
		
		// 这里为什么从第三个字节开始读取，因为前面一个字节和一个字符总共三个字节
		out("======get int:" + buffer.getInt(3));
	}

	/**
	 * 测试float，占用4个字节
	 */
	@Test
	public void testFloat() {
		ByteBuffer buffer = ByteBuffer.allocate(32);

		buffer.put((byte) 103); // 1 byte
		buffer.putChar('a'); // 2 byte
		buffer.putInt(4); // 4 byte

		out("===============put float============");
		// float
		float float5 = 10;
		buffer.putFloat(float5); // 4 byte
		
		out("position:" + buffer.position()); // 11 = 1b + 2b + 4b + 4b
		out("limit:" + buffer.limit());
		out("capacity:" + buffer.capacity());
		
		// 这里为什么从第7个字节开始读取，因为前面一个字节和一个字符，一个int总共7个字节
		out("======get float:" + buffer.getFloat(7));
	}
     
	/**
	 * 测试double，占用8个字节
	 */
	@Test
	public void testDouble() {
		ByteBuffer buffer = ByteBuffer.allocate(32);

		buffer.put((byte) 103); // 1 byte
		buffer.putChar('a'); // 2 byte
		buffer.putInt(4); // 4 byte
		buffer.putFloat(10); // 4 byte
		
		out("===============put double============");
		// double
		double double6 = 20.0;
		buffer.putDouble(double6);
		
		out("position:" + buffer.position()); // 19 = 1b + 2b + 4b + 4b + 8b
		out("limit:" + buffer.limit()); // 32
		out("capacity:" + buffer.capacity()); // 32
		
		// 这里为什么从第11个字节开始读取，因为前面一个字节和一个字符，一个int,一个float总共11个字节
		out("======get double:" + buffer.getDouble(11));
	}

	/**
	 * 测试Long，占用8个字节
	 */
	@Test
	public void testLong() {
		ByteBuffer buffer = ByteBuffer.allocate(32);

		buffer.put((byte) 103); // 1 byte
		buffer.putChar('a'); // 2 byte
		buffer.putInt(4); // 4 byte
		buffer.putFloat(10); // 4 byte
		buffer.putDouble(20.0); // 8 byte

		out("===============put long============");
		// long
		long long7 = (long) 30.0; 
		buffer.putLong(long7); // 8 byte
		
		out("position:" + buffer.position()); // 27 = 1b + 2b + 4b + 4b + 8b + 8b
		out("limit:" + buffer.limit());
		out("capacity:" + buffer.capacity());

		// 这里为什么从第19个字节开始读取，因为前面一个字节和一个字符，一个int,一个float，一个double总共19个字节
		out("======get long:" + buffer.getLong(19));
	}
	
	/**
	 * 测试字节缓冲的剩余空间函数
	 */
	@Test
	public void testRemaining() {
		ByteBuffer buffer = ByteBuffer.allocate(32);

		buffer.put((byte) 103); // 1 byte
		buffer.putChar('a'); // 2 byte
		buffer.putInt(4); // 4 byte
		buffer.putFloat(10); // 4 byte
		buffer.putDouble(20.0); // 8 byte

		//32 - 19 = 32 - (1b + 2b + 4b + 4b + 8b)
		out("======buffer 剩余空间大小:" + buffer.remaining()); // 13
	}
     
	/**
	 * 测试添加元素字节长度，大于剩余空间时的情况
	 */
	@Test
	public void testOverFlow() {
		ByteBuffer buffer = ByteBuffer.allocate(32);
		
		buffer.put((byte) 103); // 1 byte
		buffer.putChar('a'); // 2 byte
		buffer.putInt(4); // 4 byte
		buffer.putFloat(10); // 4 byte
		buffer.putDouble(20.0); // 8 byte
		buffer.putLong(22L); // 8 byte

		/*
		 * Exception in thread "main" java.nio.BufferOverflowException at java.nio.Buffer.nextPutIndex(Buffer.java:519)
		 * at java.nio.HeapByteBuffer.putLong(HeapByteBuffer.java:417) at socket.TestByteBuffer.main(TestByteBuffer.java:60) 
		 * 超出空间，则抛出BufferOverflowException异常
		 */
		try {
			buffer.putLong((long) 30.0);
		} catch (BufferOverflowException e) {
			e.printStackTrace();
		}
	}
     
	/**
	 * clear重置position，mark，limit位置，原始缓存区内容并不清掉
	 */
	public void testClear() {
		ByteBuffer buffer = ByteBuffer.allocate(32);

		out("===============clear============");
		// clear重置position，mark，limit位置，原始缓存区内容并不清掉
		buffer.clear();
		
		out("position:" + buffer.position()); // 0
		out("limit:" + buffer.limit()); // 32
		out("capacity:" + buffer.capacity()); // 32
		
		out("======get int  after clear:" + buffer.getInt(3)); // 0
	}
	
	@Test
	public void testIndex() {
		ByteBuffer buffer = ByteBuffer.allocate(32);
		
		buffer.put("IamSuperMan".getBytes());
		
		buffer.putChar(0, 'a');
		out(buffer.getChar(0));
		
		buffer.putDouble(1, 2.2d);
		out(buffer.getDouble(1));
		
		buffer.putFloat(2, 1.1F);
		out(buffer.getFloat(2));
		
		buffer.putInt(3, 3);
		out(buffer.getInt(3));
		
		buffer.putLong(4, 99999L);
		out(buffer.getLong(4));
		
		Short s = 5;
		buffer.putShort(5, s);
		out(buffer.getShort(5));
		
		out(buffer);
	}
}
