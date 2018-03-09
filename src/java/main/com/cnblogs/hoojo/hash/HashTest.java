package com.cnblogs.hoojo.hash;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.common.hash.PrimitiveSink;

/**
 * hash 散列算法
 * @author hoojo
 * @createDate 2018年3月9日 下午3:21:30
 * @file HashTest.java
 * @package com.cnblogs.hoojo.hash
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class HashTest {

	ImmutableMap<String, Object> person = ImmutableMap.<String, Object>of("age", 22, "qi", "1");

	Funnel<ImmutableMap<String, Object>> personFunnel = new Funnel<ImmutableMap<String, Object>>() {
		private static final long serialVersionUID = 1L;

		@Override
		public void funnel(ImmutableMap<String, Object> from, PrimitiveSink into) {
			into.putInt(Integer.parseInt(from.get("age").toString()));
			into.putString(from.get("qi").toString(), Charsets.UTF_8);
		}
	};
	
	@Test
	public void testHash() {
		
		long id = System.currentTimeMillis();
		CharSequence name = "hoojo";
		
		HashFunction hf = Hashing.sha256();
		
		
		HashCode hc = hf.newHasher()
				        .putLong(id )
				        .putString(name, Charsets.UTF_8)
				        .putObject(person, personFunnel)
				        .hash();
		
		out(hc.bits()); // 256

		out(hc.toString()); // b2341ae66825e13fa75152fbb579e72518cf1e6d6c76a510156b943c5c353725
		out(hc.asBytes());
		out(hc.asInt()); // -434490190
		out(hc.hashCode()); // -434490190
		
		out(hc.padToLong()); // 4603001426616661170
	}
	
	@Test
	public void testHashFunction() {
		HashFunction hf = Hashing.sha256();
		
		out(hf.bits()); // 256
		out(hf.hashBytes(new byte[] { 2, 3 })); // ee9040f65c341855e070ff438eb0ea9d5b831b2a2c270fb7ef592d750408e3b3
		
		out(hf.hashInt(10)); // 075de2b906dbd7066da008cab735bee896370154603579a50122f9b88545bd45
		out(hf.hashLong(System.currentTimeMillis())); // 21218d3c1f68e487717657b5380beb341d2c65eec3e1219624ad2dece657baf2
		out(hf.hashString("haha", Charsets.UTF_8)); // 090b235e9eb8f197f2dd927937222c570396d971222d9009a9189e2b6cc0a2c1
		out(hf.hashObject(person, personFunnel)); // b033ec96e288856ebcc6c67d4bdaa2909bc36c6140a47746258e34e8ed8c5a4a
		out(hf.hashUnencodedChars("hash")); // 6e48ddb9093c31fcd83853d73602a521ed598e66d89f2539711cf7bc7445957d
	}
	
	@Test
	public void testHasher() {
		Hasher hasher = Hashing.sha256().newHasher();
		
		hasher.putBoolean(Boolean.FALSE);
		hasher.putByte((byte) 22);
		hasher.putChar('k');
		hasher.putDouble(2.22D);
		hasher.putFloat(1.2F);
		hasher.putInt(20);
		hasher.putLong(77);
		hasher.putObject(person, personFunnel);
		hasher.putString("haha", Charsets.UTF_8);
		hasher.putShort(new Short("128"));
		hasher.putUnencodedChars("hash");
		
		out(hasher.hash()); // 834474c9fd8d0f03c554109458dc25268576361222ae61c8578f63e7cdcccd20
	}
	
	@Test
	public void testHashCode() {
		HashCode code = Hashing.sha256().newHasher().hash();
		
		out(code.asLong()); // 1449310910991872227
		
		out(HashCode.fromBytes(new byte[] { 2, 3 })); // 0203
		out(HashCode.fromInt(32)); // 20000000
		out(HashCode.fromLong(System.currentTimeMillis())); // df37ce1862010000
		//out(HashCode.fromString("h"));
		
		// BloomFilter 可以快速判断一个对象是否在集合中存在，误判率为1%
		BloomFilter<ImmutableMap<String, Object>> friends = BloomFilter.create(personFunnel, 500, 0.01);
		friends.put(person);
		friends.put(person);
		friends.put(person);
		
		// 很久以后
		if (friends.mightContain(person)) {
		    //person不是朋友还运行到这里的概率为1%
		    //在这儿，我们可以在做进一步精确检查的同时触发一些异步加载
		}
		
		out(friends.approximateElementCount()); // 1
		out(friends.expectedFpp()); // 1.4028060120101606E-20
		out(friends.mightContain(person)); // true
		
	}
	
	private void out(Object o) {
		System.out.println(o);
	}
}
