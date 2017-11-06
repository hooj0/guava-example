package com.cnblogs.hoojo.collections._new;

import org.junit.Test;

import com.google.common.collect.EnumBiMap;
import com.google.common.collect.EnumHashBiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;

/**
 * <b>function:</b> 双向Map

<table cellspacing="0" cellpadding="0" border="1">
<tbody>
<tr>
<td width="170"><b>键</b><b>&ndash;</b><b>值实现</b><b></b></td>
<td width="180" valign="top"><b>值</b><b>&ndash;</b><b>键实现</b><b></b></td>
<td width="265"><b>对应的</b><b>BiMap</b><b>实现</b><b></b></td>
</tr>
<tr>
<td width="170">HashMap</td>
<td width="180">HashMap</td>
<td width="265"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/HashBiMap.html">HashBiMap</a><b></b></td>
</tr>
<tr>
<td width="170">ImmutableMap</td>
<td width="180">ImmutableMap</td>
<td width="265"><a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/ImmutableBiMap.html">ImmutableBiMap</a><b></b></td>
</tr>
<tr>
<td width="170">EnumMap</td>
<td width="180">EnumMap</td>
<td width="265"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/EnumBiMap.html">EnumBiMap</a></td>
</tr>
<tr>
<td width="170">EnumMap</td>
<td width="180">HashMap</td>
<td width="265"><a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/collect/EnumHashBiMap.html">EnumHashBiMap</a></td>
</tr>
</tbody>
</table>

 * @author hoojo
 * @createDate 2017年10月16日 上午10:34:14
 * @file BiMapCollectionTest.java
 * @package com.cnblogs.hoojo.collections
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class BiMapCollectionTest {

	@Test
	public void testAPI() {
		
		HashBiMap<String, Integer> hash = HashBiMap.<String, Integer>create();
		
		hash.put("a", 3);
		hash.put("b", 4);
		
		System.out.println(hash); // {a=3, b=4}
		System.out.println(hash.inverse()); // {3=a, 4=b}
		System.out.println(hash.inverse().get(3)); // a
		
		hash.put("b", 55);
		System.out.println(hash); // {a=3, b=55}
		
		hash.forcePut("b", 23); // {a=3, b=23}
		System.out.println(hash);
		
		System.out.println(hash.containsKey("a")); // true
		System.out.println(hash.containsValue(4)); // false
		System.out.println(hash.entrySet()); // [a=3, b=23]
		System.out.println(hash.get("b")); // 23
		System.out.println(hash.getOrDefault("c", 11)); // 11
		
		System.out.println(hash.putIfAbsent("99", 100)); // null
		System.out.println(hash.putIfAbsent("99", 5)); // 100
		System.out.println(hash); // {a=3, b=23, 99=100}
		
	}
	
	@Test
	public void testBiMap() {
		ImmutableBiMap<String, Integer> immu = ImmutableBiMap.of("z", 22, "b", 33);
		System.out.println(immu); // {z=22, b=33}
		System.out.println(immu.inverse()); // {22=z, 33=b}
		
	 	EnumBiMap<KeyEnum, ValEnum> enums = EnumBiMap.create(KeyEnum.class, ValEnum.class);
	 	enums.put(KeyEnum.A, ValEnum._1);
	 	//enums.put(KeyEnum.B, ValEnum._1); // exception，相同的值已经存在，不能直接用put方法，改用  forcePut
	 	enums.forcePut(KeyEnum.B, ValEnum._1);
	 	
	 	System.out.println(enums); // {B=_1}
	 	System.out.println(enums.inverse()); // {_1=B}
	 	
	 	EnumHashBiMap<KeyEnum, String> hash = EnumHashBiMap.create(KeyEnum.class);
	 	hash.put(KeyEnum.A, "china");
	 	hash.put(KeyEnum.B, "jap");
	 	
	 	System.out.println(hash); // {A=china, B=jap}
	}
	
	enum KeyEnum {
		A, B;
	}
	
	enum ValEnum {
		_1, _2;
	}
}
