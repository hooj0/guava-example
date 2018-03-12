package com.cnblogs.hoojo.math;

import java.math.BigInteger;
import java.math.RoundingMode;

import org.junit.Test;

import com.google.common.math.BigIntegerMath;
import com.google.common.math.DoubleMath;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;

/**
 * int math
 * 
 * <h3>有溢出检查的运算</h3>
 * <table cellspacing="0" cellpadding="0" border="1">
<tbody>
<tr>
<td width="228"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/IntMath.html#checkedAdd(int, int)"><tt>IntMath.checkedAdd</tt></a></td>
<td width="204"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/LongMath.html#checkedAdd(long, long)"><tt>LongMath.checkedAdd</tt></a></td>
</tr>
<tr>
<td width="228"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/IntMath.html#checkedSubtract(int, int)"><tt>IntMath.checkedSubtract</tt></a></td>
<td width="204"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/LongMath.html#checkedSubtract(long, long)"><tt>LongMath.checkedSubtract</tt></a></td>
</tr>
<tr>
<td width="228"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/IntMath.html#checkedMultiply(int, int)"><tt>IntMath.checkedMultiply</tt></a></td>
<td width="204"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/LongMath.html#checkedMultiply(long, long)"><tt>LongMath.checkedMultiply</tt></a></td>
</tr>
<tr>
<td width="228"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/IntMath.html#checkedPow(int, int)"><tt>IntMath.checkedPow</tt></a></td>
<td width="204"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/LongMath.html#checkedPow(long, long)"><tt>LongMath.checkedPow</tt></a></td>
</tr>
</tbody>
</table>
 * 

<p>java.math.RoundingMode枚举值作为舍入的模式：</p>
<ul>
<li>DOWN：向零方向舍入（去尾法）</li>
<li>UP：远离零方向舍入</li>
<li>FLOOR：向负无限大方向舍入</li>
<li>CEILING：向正无限大方向舍入</li>
<li>UNNECESSARY：不需要舍入，如果用此模式进行舍入，应直接抛出ArithmeticException</li>
<li>HALF_UP：向最近的整数舍入，其中x.5远离零方向舍入</li>
<li>HALF_DOWN：向最近的整数舍入，其中x.5向零方向舍入</li>
<li>HALF_EVEN：向最近的整数舍入，其中x.5向相邻的偶数舍入</li>
</ul>

<table cellspacing="0" cellpadding="0" border="1">
<tbody>
<tr>
<td width="105"><b>运算</b><b></b></td>
<td width="141"><b>IntMath</b><b></b></td>
<td width="162"><b>LongMath</b><b></b></td>
<td width="211"><b>BigIntegerMath</b><b></b></td>
</tr>
<tr>
<td width="105">除法</td>
<td width="141"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/IntMath.html#divide(int, int, java.math.RoundingMode)"><tt>divide(int, int, RoundingMode)</tt></a></td>
<td width="162"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/LongMath.html#divide(long, long, java.math.RoundingMode)"><tt>divide(long, long, RoundingMode)</tt></a></td>
<td width="211"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/BigIntegerMath.html#divide(java.math.BigInteger, java.math.BigInteger, java.math.RoundingMode)"><tt>divide(BigInteger, BigInteger, RoundingMode)</tt></a></td>
</tr>
<tr>
<td width="105">2为底的对数</td>
<td width="141"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/IntMath.html#log2(int, java.math.RoundingMode)"><tt>log2(int, RoundingMode)</tt></a></td>
<td width="162"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/LongMath.html#log2(long, java.math.RoundingMode)"><tt>log2(long, RoundingMode)</tt></a></td>
<td width="211"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/BigIntegerMath.html#log2(java.math.BigInteger, java.math.RoundingMode)"><tt>log2(BigInteger, RoundingMode)</tt></a></td>
</tr>
<tr>
<td width="105">10为底的对数</td>
<td width="141"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/IntMath.html#log10(int, java.math.RoundingMode)"><tt>log10(int, RoundingMode)</tt></a></td>
<td width="162"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/LongMath.html#log10(long, java.math.RoundingMode)"><tt>log10(long, RoundingMode)</tt></a></td>
<td width="211"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/BigIntegerMath.html#log10(java.math.BigInteger, java.math.RoundingMode)"><tt>log10(BigInteger, RoundingMode)</tt></a></td>
</tr>
<tr>
<td width="105">平方根</td>
<td width="141"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/IntMath.html#sqrt(int, java.math.RoundingMode)"><tt>sqrt(int, RoundingMode)</tt></a></td>
<td width="162"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/LongMath.html#sqrt(long, java.math.RoundingMode)"><tt>sqrt(long, RoundingMode)</tt></a></td>
<td width="211"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/BigIntegerMath.html#sqrt(java.math.BigInteger, java.math.RoundingMode)"><tt>sqrt(BigInteger, RoundingMode)</tt></a></td>
</tr>
</tbody>
</table>

<table width="624" cellspacing="0" cellpadding="0" border="1">
<tbody>
<tr>
<td width="96"><b>运算</b><b></b></td>
<td width="156"><b>IntMath</b><b></b></td>
<td width="156"><b>LongMath</b><b></b></td>
<td width="216"><b>BigIntegerMath</b><b>*</b></td>
</tr>
<tr>
<td width="96">最大公约数</td>
<td width="156"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/IntMath.html#gcd(int, int)"><tt>gcd(int, int)</tt></a></td>
<td width="156"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/LongMath.html#gcd(long, long)"><tt>gcd(long, long)</tt></a></td>
<td width="216"><a href="http://docs.oracle.com/javase/6/docs/api/java/math/BigInteger.html#gcd(java.math.BigInteger)"><tt>BigInteger.gcd(BigInteger)</tt></a></td>
</tr>
<tr>
<td width="96">取模</td>
<td width="156"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/IntMath.html#mod(int, int)"><tt>mod(int, int)</tt></a></td>
<td width="156"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/LongMath.html#mod(long, long)"><tt>mod(long, long)</tt></a></td>
<td width="216"><a href="http://docs.oracle.com/javase/6/docs/api/java/math/BigInteger.html#mod(java.math.BigInteger)"><tt>BigInteger.mod(BigInteger)</tt></a></td>
</tr>
<tr>
<td width="96">取幂</td>
<td width="156"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/IntMath.html#pow(int, int)"><tt>pow(int, int)</tt></a></td>
<td width="156"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/LongMath.html#pow(long, int)"><tt>pow(long, int)</tt></a></td>
<td width="216"><a href="http://docs.oracle.com/javase/6/docs/api/java/math/BigInteger.html#pow(int)"><tt>BigInteger.pow(int)</tt></a></td>
</tr>
<tr>
<td width="96">是否2的幂</td>
<td width="156"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/IntMath.html#isPowerOfTwo(int)"><tt>isPowerOfTwo(int)</tt></a></td>
<td width="156"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/LongMath.html#isPowerOfTwo(long)"><tt>isPowerOfTwo(long)</tt></a></td>
<td width="216"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/BigIntegerMath.html#isPowerOfTwo(java.math.BigInteger)"><tt>isPowerOfTwo(BigInteger)</tt></a></td>
</tr>
<tr>
<td width="96">阶乘*</td>
<td width="156"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/IntMath.html#factorial(int)"><tt>factorial(int)</tt></a></td>
<td width="156"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/LongMath.html#factorial(int)"><tt>factorial(int)</tt></a></td>
<td width="216"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/BigIntegerMath.html#factorial(int)"><tt>factorial(int)</tt></a></td>
</tr>
<tr>
<td width="96">二项式系数*</td>
<td width="156"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/IntMath.html#binomial(int, int)"><tt>binomial(int, int)</tt></a></td>
<td width="156"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/LongMath.html#binomial(int, int)"><tt>binomial(int, int)</tt></a></td>
<td width="216"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/BigIntegerMath.html#binomial(int, int)"><tt>binomial(int, int)</tt></a></td>
</tr>
</tbody>
</table>

<table width="612" cellspacing="0" cellpadding="0" border="1">
<tbody>
<tr>
<td width="318"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/DoubleMath.html#isMathematicalInteger(double)"><tt>isMathematicalInteger(double)</tt></a></td>
<td width="294">判断该浮点数是不是一个整数</td>
</tr>
<tr>
<td width="318"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/DoubleMath.html#roundToInt(double, java.math.RoundingMode)"><tt>roundToInt(double, RoundingMode)</tt></a></td>
<td width="294">舍入为int；对无限小数、溢出抛出异常</td>
</tr>
<tr>
<td width="318"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/DoubleMath.html#roundToLong(double, java.math.RoundingMode)"><tt>roundToLong(double, RoundingMode)</tt></a></td>
<td width="294">舍入为long；对无限小数、溢出抛出异常</td>
</tr>
<tr>
<td width="318"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/DoubleMath.html#roundToBigInteger(double, java.math.RoundingMode)"><tt>roundToBigInteger(double, RoundingMode)</tt></a></td>
<td width="294">舍入为BigInteger；对无限小数抛出异常</td>
</tr>
<tr>
<td width="318"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/math/DoubleMath.html#log2(double, java.math.RoundingMode)"><tt>log2(double, RoundingMode)</tt></a></td>
<td width="294">2的浮点对数，并且舍入为int，比JDK的Math.log(double) 更快</td>
</tr>
</tbody>
</table>

 * 
 * @author hoojo
 * @createDate 2018年3月12日 下午3:50:03
 * @file IntMathTest.java
 * @package com.cnblogs.hoojo.math
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class MathTest {

	@Test
	public void testCheck() {
		// 检查运算，防止数据溢出
		// 加法
		out(IntMath.checkedAdd(2, 12)); // 14
		// 乘法
		out(IntMath.checkedMultiply(2, 12)); // 24
		// 幂运算
		out(IntMath.checkedPow(2, 3)); // 8
		out(IntMath.checkedPow(2, 4)); // 16
		// 减法
		out(IntMath.checkedSubtract(12, 2)); // 10
	}
	
	@Test
	public void testRoundingMode() {
		out(31 / 3.0); // 10.333333333333334
		out(20 / 3.0); // 6.666666666666667

	    // DOWN：向零方向舍入（去尾法）；直接去掉小数位
		out(IntMath.divide(31, 3, RoundingMode.DOWN)); // 10
		
		// UP：远离零方向舍入；带有小数位且小数位不为0都直接进一
		out(IntMath.divide(31, 3, RoundingMode.UP)); // 11
		
		// FLOOR：向负无限大方向舍入；正数忽略小数位，附数小数位会减一
		out(IntMath.divide(31, 3, RoundingMode.FLOOR)); // 10
		out(IntMath.divide(-31, 3, RoundingMode.FLOOR)); // -11
		
		// CEILING：向正无限大方向舍入；正数有小数位就进一位，负数的小数忽略小数位
		out(IntMath.divide(31, 3, RoundingMode.CEILING)); // 11
		out(IntMath.divide(-31, 3, RoundingMode.CEILING)); // -10
		
		// UNNECESSARY：不需要舍入，如果用此模式进行舍入，应直接抛出ArithmeticException
		//out(IntMath.divide(31, 3, RoundingMode.UNNECESSARY));
		
		// HALF_UP：向最近的整数舍入，其中x.5远离零方向舍入；四舍五入
		out(IntMath.divide(31, 3, RoundingMode.HALF_UP)); // 10
		
		// HALF_DOWN：向最近的整数舍入，其中x.5向零方向舍入；五舍六入
		out(IntMath.divide(31, 3, RoundingMode.HALF_DOWN)); // 10
		out(IntMath.divide(31, 2, RoundingMode.HALF_DOWN)); // 15
		
		// HALF_EVEN：向最近的整数舍入，其中x.5向相邻的偶数舍入；
		out(IntMath.divide(31, 3, RoundingMode.HALF_EVEN)); // 10.333333333333334 //10
		out(IntMath.divide(31, 2, RoundingMode.HALF_EVEN)); // 15.5 // 16
		out(IntMath.divide(33, 5, RoundingMode.HALF_EVEN)); // 6.6 // 7
	}
	
	@Test
	public void testMath() {
		// 除法
		out(IntMath.divide(10, 3, RoundingMode.HALF_UP)); // 3
		// 2为底的对数
		out(IntMath.log2(2, RoundingMode.HALF_UP)); // 1
		// 10为底的对数
		out(IntMath.log10(22, RoundingMode.HALF_UP)); // 1
		// 平方根
		out(IntMath.sqrt(2, RoundingMode.HALF_UP)); // 1
		
		// 最大公约数
		out(IntMath.gcd(12, 2)); // 2
		// 取模
		out(IntMath.mod(14, 5)); // 4
		// 取幂
		out(IntMath.pow(2, 5)); // 32
		// 是否2的幂
		out(IntMath.isPowerOfTwo(32)); // true
		out(IntMath.isPowerOfTwo(31)); // false
		// 阶乘*  n - 1 * n
		out(IntMath.factorial(4)); // 1 * 2 * 3 * 4
		// 二项式系数*
		out(IntMath.binomial(6, 3));
	}
	
	@Test
	public void testDoubleMath() {
		// 判断该浮点数是不是一个整数
		out(DoubleMath.isMathematicalInteger(3.0)); // true
		out(DoubleMath.isMathematicalInteger(3.02)); // false
		
		// 舍入为int；对无限小数、溢出抛出异常
		out(DoubleMath.roundToInt(3.5, RoundingMode.HALF_EVEN)); // 4
		out(DoubleMath.roundToInt(3.4, RoundingMode.HALF_EVEN)); // 3
		out(DoubleMath.roundToInt(3.6, RoundingMode.HALF_EVEN)); // 4
		out(DoubleMath.roundToInt(4.6, RoundingMode.HALF_EVEN)); // 5
		
		// 舍入为long；对无限小数、溢出抛出异常
		out(DoubleMath.roundToLong(2.59, RoundingMode.HALF_EVEN)); // 3
		out(DoubleMath.roundToLong(2.49, RoundingMode.HALF_EVEN)); // 2
		
		// 舍入为BigInteger；对无限小数抛出异常
		out(DoubleMath.roundToBigInteger(4.6, RoundingMode.HALF_EVEN)); // 5
		
		// 2的浮点对数，并且舍入为int，比JDK的Math.log(double) 更快
		out(DoubleMath.log2(2.33, RoundingMode.HALF_EVEN)); // 1
		
		// 该方法是比较两个double类型的数是否在一定范围内相等，
		// 即a和b的差的绝对值是否小于tolerance，如果小于，返回true，否则返回false。
		out("---------fuzzyEquals----------");
		out(DoubleMath.fuzzyEquals(2.0, 3.0, 0.5)); // 2.0 - 3.0 < 0.5 ? false
		out(DoubleMath.fuzzyEquals(3.0, 3.0, 2)); // 3.0 - 3.0 < 2 ? true
		out(DoubleMath.fuzzyEquals(2.0, 3.0, 0)); // 2.0 - 3.0 < 1 ? false

		// 该方法是比较两个double类型的数，如果在一定范围内相等，就返回0，如果a > b，就返回1， 
		// 如果a < b就返回-1，否则返回Booleans.compare(Double.isNaN(a),Double.isNaN(b));
		// 这是针对所有的NaN的值。
		/*
			if (fuzzyEquals(a, b, tolerance)) {
		      return 0;
		    } else if (a < b) {
		      return -1;
		    } else if (a > b) {
		      return 1;
		    }
		 */
		out("---------fuzzyCompare----------");
		out(DoubleMath.fuzzyCompare(2.1, 2.1, 5.0)); // 2.1 - 2.1 < 5.0 ? true -> 0 
		out(DoubleMath.fuzzyCompare(2.1, 3.2, 2.5)); // 2.1 - 3.2 < 2.5 ? true -> 0
		out(DoubleMath.fuzzyCompare(5.2, 3.2, 0)); // 5.2 - 3.2 < 0 ? false -> 5.2 > 3.2 = 1
		out(DoubleMath.fuzzyCompare(1.1, 3.2, 1.5)); // 1.1 - 3.2 < 1.5 ? false -> 1.1 < 3.2 = -1
	}
	
	@Test
	public void testLongMath() {
		out(LongMath.ceilingPowerOfTwo(22)); // 32
		
		out(LongMath.floorPowerOfTwo(22)); // 16
		// 加法
		out(LongMath.saturatedAdd(10, 20)); // 30
		// 乘法
		out(LongMath.saturatedMultiply(10, 20)); // 200
		// 幂运算
		out(LongMath.saturatedPow(2, 3)); // 8
		// 减法
		out(LongMath.saturatedSubtract(10, 20)); // -10
	}
	
	@Test
	public void testBigIntegerMath() {
		out(BigIntegerMath.ceilingPowerOfTwo(new BigInteger("20")));
	}
	
	private void out(Object o) {
		System.out.println(o);
	}
}
