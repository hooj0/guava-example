package com.cnblogs.hoojo.primitives;

import java.util.Arrays;

import org.junit.Test;

import com.google.common.primitives.Booleans;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Chars;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.common.primitives.Shorts;
import com.google.common.primitives.SignedBytes;
import com.google.common.primitives.UnsignedBytes;
import com.google.common.primitives.UnsignedInteger;
import com.google.common.primitives.UnsignedInts;
import com.google.common.primitives.UnsignedLong;
import com.google.common.primitives.UnsignedLongs;

/**
 * 原生类型测试
 * 
List<Wrapper> asList(prim… backingArray) 	把数组转为相应包装类的List 	Arrays.asList 	符号无关*
prim[] toArray(Collection<Wrapper> collection) 	把集合拷贝为数组，和collection.toArray()一样线程安全 	Collection.toArray() 	符号无关
prim[] concat(prim[]… arrays) 	串联多个原生类型数组 	Iterables.concat 	符号无关
boolean contains(prim[] array, prim target) 	判断原生类型数组是否包含给定值 	Collection.contains 	符号无关
int indexOf(prim[] array, prim target) 	给定值在数组中首次出现处的索引，若不包含此值返回-1 	List.indexOf 	符号无关
int lastIndexOf(prim[] array, prim target) 	给定值在数组最后出现的索引，若不包含此值返回-1 	List.lastIndexOf 	符号无关
prim min(prim… array) 	数组中最小的值 	Collections.min 	符号相关*
prim max(prim… array) 	数组中最大的值 	Collections.max 	符号相关
String join(String separator, prim… array) 	把数组用给定分隔符连接为字符串 	Joiner.on(separator).join 	符号相关
Comparator<prim[]>   lexicographicalComparator() 	按字典序比较原生类型数组的Comparator 	Ordering.natural().lexicographical() 	符号相关
 * 
 * @author hoojo
 * @createDate 2018年2月27日 上午11:35:25
 * @file PrimitivesTest.java
 * @package com.cnblogs.hoojo.primitives
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class PrimitivesTest {

	@Test
	public void testByte() {
		
		out(Bytes.asList("china!".getBytes())); // [99, 104, 105, 110, 97, 33]
		out(Bytes.concat(new byte[] { 12, 22}, new byte[] { 33, 44 })); // 12, 22, 33, 44,
		
		byte b = 3;
		out(Bytes.contains(new byte[] { 1, 2 },  b)); // false
		out(Bytes.contains(new byte[] { 1, 2, 3 },  b)); // true
		
		// 返回一个新数组，新数组包含原始数组，并且长度不小于minLength，且长度等于minLength + paddingLength。
		out(Bytes.ensureCapacity(new byte[] { 1, 2 }, 3, 1)); // 1, 2, 0, 0, 
		out(Bytes.ensureCapacity(new byte[] { 1, 2 }, 3, 3)); // 1, 2, 0, 0, 0, 0,
		out(Bytes.ensureCapacity(new byte[] { 1, 2 }, 0, 0)); // 1, 2, 
		out(Bytes.ensureCapacity(new byte[] { 1, 2 }, 0, 1)); // 1, 2,
		out(Bytes.ensureCapacity(new byte[] { 1, 2 }, 3, 0)); // 1, 2, 0, 
		
		out(Bytes.toArray(Arrays.asList(1, 2, 3))); // 1, 2, 3, 
		
		out(Bytes.hashCode((byte) 'a')); // 97
		out(Bytes.indexOf(new byte[] { 1, 2 }, (byte) 'a')); // -1
		out(Bytes.indexOf(new byte[] { 22, 97, 2 }, (byte) 'a')); // 1
		// 查找一个字节数组在源数组中是否存在，按顺序排列的数组元素才能匹配
		out(Bytes.indexOf(new byte[] { 22, 97, 2 }, new byte[]{ 97, 2 })); // 1
		out(Bytes.indexOf(new byte[] { 22, 97, 2 }, new byte[]{ 97, 22 })); // -1
		
		// 最后出现的索引位置
		out(Bytes.lastIndexOf(new byte[] { 22, 2, 97, 2 }, (byte) 2)); // -1
		
		/////////////////////////////////////////////////////////////////////////////////
		// 检查byte是否溢出
		out(SignedBytes.checkedCast(97)); // 97
		out(SignedBytes.compare((byte) 'a', (byte) 'b')); //--1
		out(SignedBytes.join(";", "china".getBytes())); // 99;104;105;110;97
		out(SignedBytes.lexicographicalComparator());
		out(SignedBytes.max("china".getBytes())); // 110
		out(SignedBytes.min("china".getBytes())); // 97
		
		// 返回合法的字节值，无论超过最大值还是小于最小值
		out(SignedBytes.saturatedCast(9789));
		
		///////////////////////////////////////////////////////////////////////////////
		out(UnsignedBytes.parseUnsignedByte("97")); // 97
		// 以给定基数返回由字符串表示的无符号字节值。
		out(UnsignedBytes.parseUnsignedByte("97", 10)); // 97
	}
	
	@Test
	public void testInt() {
		// int 转byte
		out(Ints.toByteArray(99)); // 0, 0, 0, 99,
		out(Ints.asList(1, 3, 6)); // [1, 3, 6]
		
		out(Ints.concat(new int[] { 1, 2 }, new int[] { 9, 10 })); //1, 2, 9, 10,
		
		// 返回合法区间值
		out(Ints.constrainToRange(8, 2, 7)); // 7
		out(Ints.constrainToRange(5, 2, 7)); // 5
		out(Ints.constrainToRange(1, 2, 7)); // 2
		// byte 转 int
		out(Ints.fromByteArray(new byte[] { 0, 0, 0, 99, })); // 99
		
		out(Ints.join(",", 2, 4, 8)); // 2,4,8
		
		out(Ints.stringConverter());
		out(Ints.tryParse("99")); // 99
		out(Ints.tryParse("22", 3)); // 8
		out(Ints.tryParse("22", 5)); // 12
		
		out("//////////////////////////////////////////////////////////////////////////");
		// 32 bit 
		out(UnsignedInteger.fromIntBits(16)); // 16
		out(UnsignedInteger.fromIntBits(0x77700333)); // 2003829555
		out(UnsignedInteger.valueOf(88L)); // 88
		out(UnsignedInteger.valueOf(0x77700333)); // 2003829555
		
		out("//////////////////////////////////////////////////////////////////////////");
		// 接收十进制、十六进制、八进制
		out(UnsignedInts.decode("0x77700333")); // 2003829555
		// 整除 取整数值
		out(UnsignedInts.divide(22, 2)); // 11
		out(UnsignedInts.divide(22, 3)); // 7
		
		// 整除取余数
		out(UnsignedInts.remainder(22, 4)); // 2
		out(UnsignedInts.remainder(22, 3)); // 1
		
		out(UnsignedInts.toLong(0x44556677)); // 1146447479
		// 转换进制
		out(UnsignedInts.toString(0x44556677)); // 1146447479
		out(UnsignedInts.toString(0x44556677, 10)); // 1146447479
		out(UnsignedInts.toString(0x44556677, 16)); // 44556677
		out(UnsignedInts.toString(0x44556677, 8)); // 10425263167
		out(UnsignedInts.toString(0x44556677, 2)); // 1000100010101010110011001110111

		out(UnsignedInts.toString(1146447479, 16)); // 44556677
	}
	
	@Test
	public void testLong() {
		// long 转byte
		out(Longs.toByteArray(99L)); // 0, 0, 0, 0, 0, 0, 0, 99, 
		out(Longs.asList(1, 3, 6)); // [1, 3, 6]
		
		out(Longs.concat(new long[] { 1, 2 }, new long[] { 9, 10 })); //1, 2, 9, 10,
		
		// 返回合法区间值
		out(Longs.constrainToRange(8, 2, 7)); // 7
		out(Longs.constrainToRange(5, 2, 7)); // 5
		out(Longs.constrainToRange(1, 2, 7)); // 2
		// byte 转 long
		out(Longs.fromByteArray(new byte[] { 0, 0, 0, 0, 0, 0, 0, 99 })); // 99
		
		out(Longs.join(",", 2, 4, 8)); // 2,4,8
		
		out(Longs.stringConverter());
		out(Longs.tryParse("1")); // 99
		out(Longs.tryParse("+99", 3)); // 8
		out(Longs.tryParse("-99", 5)); // 12
		
		out("//////////////////////////////////////////////////////////////////////////");
		// 32 bit 
		out(UnsignedLong.fromLongBits(1146447479)); // 16
		out(UnsignedLong.valueOf(1146447479)); // 16
		
		out("//////////////////////////////////////////////////////////////////////////");
		// 接收十进制、十六进制、八进制
		out(UnsignedLongs.decode("0x77700333")); // 2003829555
		// 整除 取整数值
		out(UnsignedLongs.divide(22, 2)); // 11
		out(UnsignedLongs.divide(22, 3)); // 7
		
		// 整除取余数
		out(UnsignedLongs.remainder(22, 4)); // 2
		out(UnsignedLongs.remainder(22, 3)); // 1
		
		// 转换进制
		out(UnsignedLongs.toString(0x44556677)); // 1146447479
		out(UnsignedLongs.toString(0x44556677, 10)); // 1146447479
		out(UnsignedLongs.toString(0x44556677, 16)); // 44556677
		out(UnsignedLongs.toString(0x44556677, 8)); // 10425263167
		out(UnsignedLongs.toString(0x44556677, 2)); // 1000100010101010110011001110111

		out(UnsignedInts.toString(1146447479, 16)); // 44556677
	}
	
	@Test
	public void testFloats() {
		out(Floats.toArray(Arrays.asList(1, 3, 5))); // 1.0, 3.0, 5.0, 
		out(Floats.asList(1, 3, 6)); // [1.0, 3.0, 6.0]
		
		out(Floats.concat(new float[] { 1, 2 }, new float[] { 9, 10 })); //1.0, 2.0, 9.0, 10.0, 
		
		out(Floats.compare(3.0f, 2.0f)); // 1
		out(Floats.contains(new float[] { 1, 2 }, 3)); // false
		out(Floats.isFinite(2.0f)); // true
		
		out(Floats.max(new float[] { 3, 2, 4, 5 })); // 5.0
		out(Floats.min(new float[] { 3, 2, 4, 5 })); // 2.0
		
		// 返回合法区间值
		out(Floats.constrainToRange(8, 2, 7)); // 7.0
		out(Floats.constrainToRange(5, 2, 7)); // 5.0
		out(Floats.constrainToRange(1, 2, 7)); // 2.0
		
		// 返回一个新数组，新数组包含原始数组，并且长度不小于minLength，且长度等于minLength + paddingLength。
		out(Floats.ensureCapacity(new float[] { 1, 2 }, 3, 1)); // 1.0, 2.0, 0.0, 0.0, 
		out(Floats.ensureCapacity(new float[] { 1, 2 }, 3, 3)); // 1.0, 2.0, 0.0, 0.0, 0.0, 0.0,
		out(Floats.ensureCapacity(new float[] { 1, 2 }, 0, 0)); // 1.0, 2.0, 
		out(Floats.ensureCapacity(new float[] { 1, 2 }, 0, 1)); // 1.0, 2.0, 
		out(Floats.ensureCapacity(new float[] { 1, 2 }, 3, 0)); // 1.0, 2.0, 0.0,
		
		out(Floats.join("|", new float[] { 3, 2, 4, 5 }));	// 3.0|2.0|4.0|5.0
		out(Floats.lastIndexOf(new float[] { 3, 2, 4, 5, 2 }, 2));	// 4
		
		out(Floats.tryParse("2.3f"));  // 2.3
		out(Floats.tryParse("2.32f")); // 2.32
	}
	
	@Test
	public void testShorts() {
		out(Shorts.toArray(Arrays.asList(1, 3, 5))); // 1, 3, 5, 
		out(Shorts.toByteArray((short) 21)); // 0, 21, 
		out(Shorts.asList((short)2, (short)3, (short)5)); // [2, 3, 5]
		
		// 强制转换 long，并检查是否在合法范围
		out(Shorts.checkedCast(5L)); // 5
		out(Shorts.saturatedCast(255)); // 255
		
		out(Shorts.concat(new short[] { 1, 2 }, new short[] { 9, 10 })); //1, 2, 9, 10, 
		
		out(Shorts.compare((short) 3, (short) 2)); // 1
		out(Shorts.contains(new short[] { 1, 2 }, (short) 3)); // false
		
		out(Shorts.max(new short[] { 3, 2, 4, 5 })); // 5
		out(Shorts.min(new short[] { 3, 2, 4, 5 })); // 2
		
		out(Shorts.lastIndexOf(new short[] { 3, 2, 4, 5, 2 }, (short)2));	// 4
		
		// 返回合法区间值
		out(Shorts.constrainToRange((short)8, (short)2, (short)7)); // 7
		out(Shorts.constrainToRange((short)5, (short)2, (short)7)); // 5
		out(Shorts.constrainToRange((short)1, (short)2, (short)7)); // 2
		
		// 返回一个新数组，新数组包含原始数组，并且长度不小于minLength，且长度等于minLength + paddingLength。
		out(Shorts.ensureCapacity(new short[] { 1, 2 }, 3, 1)); // 1, 2, 0, 0, 
		out(Shorts.ensureCapacity(new short[] { 1, 2 }, 3, 3)); // 1, 2, 0, 0, 0, 0, 
		out(Shorts.ensureCapacity(new short[] { 1, 2 }, 0, 0)); // 1, 2, 
		out(Shorts.ensureCapacity(new short[] { 1, 2 }, 0, 1)); // 1, 2,  
		out(Shorts.ensureCapacity(new short[] { 1, 2 }, 3, 0)); // 1, 2, 0, 
		
		out(Shorts.join("|", new short[] { 3, 2, 4, 5 }));	// 3|2|4|5
	}
	
	@Test
	public void testBooleans() {
		out(Booleans.asList(false, false, true, false)); // [false, false, true, false]
		out(Booleans.compare(false, true)); // -1
		
		out(Booleans.countTrue(true, false, false)); // 1
		out(Booleans.trueFirst());
		out(Booleans.falseFirst());
	}
	
	@Test
	public void test1() {
		
		// 是否在合法区间
		out(Doubles.isFinite(-0xfff0000000000000L)); // true
		
		out(Chars.BYTES);
		
		// true 的数量
		out(Booleans.countTrue(true, false, true)); // 2
		out(Booleans.trueFirst());
	}
	
	private void out(Object obj) {
		System.out.println(obj);
	}
	
	private void out(byte[] obj) {
		for (Object o : obj) {
			System.out.print(o + ", ");
		}
		System.out.println();
	}
	
	private void out(int[] obj) {
		for (Object o : obj) {
			System.out.print(o + ", ");
		}
		System.out.println();
	}
	
	private void out(long[] obj) {
		for (Object o : obj) {
			System.out.print(o + ", ");
		}
		System.out.println();
	}
	
	private void out(float[] obj) {
		for (Object o : obj) {
			System.out.print(o + ", ");
		}
		System.out.println();
	}
	
	private void out(short[] obj) {
		for (Object o : obj) {
			System.out.print(o + ", ");
		}
		System.out.println();
	}
}
