package com.cnblogs.hoojo.functional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Ordering;

/**
 * <b>function:</b> 函数式编程
 * @author hoojo
 * @createDate 2017年11月9日 下午5:50:01
 * @file FunctionalTest.java
 * @package com.cnblogs.hoojo.functional
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class FunctionalTest {

	@Test
	public void testFun() {
		Function<Character, Integer> fun = new Function<Character, Integer>() {
			@Override
			public Integer apply(Character input) {
				System.out.println("fun-Character:" + input);
				return (int) input.charValue();
			}
		};
		
		Function<Number, Integer> after = new Function<Number, Integer>() {
			@Override
			public Integer apply(Number input) {
				System.out.println("andThen-Number:" + input);
				return input.intValue();
			}
		};
		
		Function<Number, Character> before = new Function<Number, Character>() {
			@Override
			public Character apply(Number input) {
				System.out.println("compose-Number:" + input);
				return (char) input.intValue();
			}
		};
		
		System.out.println("fun result:" + fun.apply('a')); // fun result:97
		System.out.println();
		
		System.out.println("andThen-result:" + fun.andThen(after).apply('A'));
		System.out.println();
		
		System.out.println("compose-result:" + fun.compose(before).apply(65));
		
		System.out.println("-------------------------");
		System.out.println(fun.andThen(after).compose(before).apply(122));
	}
	
	@Test
	public void testPredicate() {
		Predicate<String> foo = new Predicate<String>() {
			@Override
			public boolean apply(String input) {
				return input.contains("k");
			}
		};
		
		Predicate<String> bar = new Predicate<String>() {
			@Override
			public boolean apply(String input) {
				return input.contains("a");
			}
		};
		
		System.out.println(foo.apply("jdk")); // true
		System.out.println(foo.apply("api")); // false
		
		System.out.println(foo.test("jdk")); // true
		System.out.println(foo.test("api")); // false
		
		// contains a && k
		System.out.println(foo.and(bar).test("api")); // false
		// contains a && k
		System.out.println(foo.and(bar).test("api jdk")); // true
		
		// not foo.test() = !foo.test()
		System.out.println(foo.negate().test("api")); // true
		System.out.println(foo.negate().test("jdk")); // false
		
		// ||
		System.out.println(foo.or(bar).test("api")); // true
		System.out.println(foo.or(bar).test("jdk")); // true
		System.out.println(foo.or(bar).test("open")); // false
		System.out.println(foo.or(bar).test("open jkd api")); // true
		
		// !(foo && bar || foo)
		System.out.println(foo.and(bar).or(foo).negate().test("abcd"));
	}
	
	@Test
	public void testSupplier() {
		
		Supplier<String> currentTimeMillis = new Supplier<String>() {
			@Override
			public String get() {
				return String.valueOf(System.currentTimeMillis());
			}
		};
		
		System.out.println(currentTimeMillis.get());
	}
	
	@Test
	public void testFunctions() {
		
		// 组合
		Function<String, Character> comp = Functions.compose(new Function<Number, Character>() {
			@Override
			public Character apply(Number input) {
				Character data = (char) (input.intValue() + 1);
				System.out.println("Number->Character:" + input + "->" + data);
				return data;
			}
		}, new Function<String, Integer>() {
			@Override
			public Integer apply(String input) {
				int data = input.codePointAt(0) + 1;
				System.out.println("String->Integer:" + input + "->" + data);
				return data;
			}
		});
		
		// String->Integer:A->66
		// Number->Character:66->C
		// C
		System.out.println(comp.apply("A"));
		System.out.println(comp.apply("a"));
		
		// 常量
		Function<Object, Integer> cost = Functions.constant(12);
		System.out.println(cost.apply(2)); // 12
		
		Map<Object, Object> map = ImmutableMap.of("name", "jack", 65, 'A');
		// map
		System.out.println(Functions.forMap(map).apply(65)); // A
		System.out.println(Functions.forMap(map, "NotFound").apply("age")); // NotFound
		
		// default fun
		System.out.println(Functions.identity().apply(3)); // 3
		// Object -> String
		System.out.println(Functions.toStringFunction().apply(67) + 1); // 671
		
		Function<String, Boolean> bool = Functions.forPredicate(new Predicate<String>() {
			@Override
			public boolean apply(String input) {
				return input.contains("k");
			}
		});
		
		System.out.println(bool.apply("jdk")); // true
		System.out.println(bool.apply("api")); // false
		
		// factory
		System.out.println(Functions.forSupplier(new Supplier<String>() {
			@Override
			public String get() {
				return "hi, guava examples!";
			}
		}).apply("go"));
	}
	
	@Test
	public void testPredicates() {
		
		Predicate<String> foo = new Predicate<String>() {
			@Override
			public boolean apply(String input) {
				return input.contains("k");
			}
		};
		
		Predicate<String> bar = (input) -> {
			return input.contains("a");
		};
		
		// const false
		System.out.println(Predicates.alwaysFalse().test(null)); // false
		// const true
		System.out.println(Predicates.alwaysTrue().apply(null)); // true
		
		// and &&
		System.out.println(Predicates.and(foo, bar).apply("jdk api")); // true
		System.out.println(Predicates.and(foo, bar).apply("guava api")); // false
		
		// foo && bar && anonymous 
		System.out.println(Predicates.and(foo, bar, (input) -> {
			return input.contains("g");
		}).test("jdk8 guava api")); // true
		
		System.out.println(Predicates.and(foo, bar, (input) -> {
			return input.contains("g");
		}).test("my guava api")); // false
		
		// or ||
		System.out.println(Predicates.or(bar, foo).test("open")); // false
		System.out.println(Predicates.or(bar, foo).test("jdk")); // true
		System.out.println(Predicates.or(bar, foo, (input) -> { return input.contains("g"); }).test("guava")); // true
		
		// assignable class
		System.out.println(Predicates.subtypeOf(Number.class).apply(String.class)); // false
		System.out.println(Predicates.subtypeOf(Number.class).apply(Integer.class)); // true
		System.out.println(Predicates.subtypeOf(Number.class).apply(Long.class)); // true
		System.out.println(Predicates.subtypeOf(Number.class).apply(Number.class)); // true
		System.out.println(Predicates.subtypeOf(Number.class).apply(Object.class)); // false
		
		System.out.println(Predicates.compose(foo, (Character input) -> {
			return input + "";
		}).test('z')); // false
		
		System.out.println(Predicates.compose(foo, (Character input) -> {
			return input.toString();
		}).test('k')); // true
		
		// contains
		Predicate<CharSequence> regex = Predicates.contains(Pattern.compile("[a-z][0-9]"));
		System.out.println(regex.test("jdk9")); // true
		System.out.println(regex.test("KFC")); // false
		
		// contains
		System.out.println(Predicates.containsPattern("[0-9]").test("abc")); // false
		System.out.println(Predicates.containsPattern("[0-9]").test("guava23")); // true
		
		// equal
		System.out.println(Predicates.equalTo("A").test("Z")); // false
		System.out.println(Predicates.equalTo("A").test("A")); // true
		
		// in
		System.out.println(Predicates.in(Arrays.asList(1, 3, 5)).test(2)); // false
		System.out.println(Predicates.in(Arrays.asList(1, 3, 5)).test(3)); // true
		
		// instanceOf
		System.out.println(Predicates.instanceOf(Number.class).test("33")); // false
		System.out.println(Predicates.instanceOf(Number.class).test(22)); // true
		
		// is null
		System.out.println(Predicates.isNull().test(null)); // true
		System.out.println(Predicates.isNull().test("")); // false
		System.out.println(Predicates.isNull().test(2)); // false
		
		// not !
		System.out.println(Predicates.not((input) -> {
			return input.toString().contains("u");
		}).test("guava")); // false

		System.out.println(Predicates.not((input) -> {
			return input.toString().contains("u");
		}).test("jdk")); // true
		
		// not null
		System.out.println(Predicates.notNull().test(null)); // false
		System.out.println(Predicates.notNull().test("")); // true
	}
	
	@Test
	public void testFunctionalExample() {
		
		String result = CharMatcher.forPredicate(new Predicate<Character>() {
			@Override
			public boolean apply(Character input) {
				if (input == 'a') {
					return true;
				}
				return false;
			}
		}).replaceFrom("abcdef", "#");
		System.out.println(result); // #bcdef
		
		Ordering<Integer> ordering = Ordering.<String>natural().onResultOf(new Function<Integer, String>() {
			@Override
			public String apply(Integer input) {
				return "" + input + new Random(System.currentTimeMillis()).nextLong();
			}
		});
		
		List<Integer> list = Arrays.asList(1, 5, 2, 0);
		System.out.println(ordering.sortedCopy(list)); // [0, 1, 2, 5]
		
		System.out.println(Equivalence.identity().onResultOf(new Function<Integer, String>() {
			@Override
			public String apply(Integer input) {
				return "cast-" + input;
			}
		}).equivalent(1, 2)); // false
		
		System.out.println(Equivalence.identity().onResultOf(new Function<Integer, String>() {
			@Override
			public String apply(Integer input) {
				return "cast-" + input;
			}
		}).equivalent(1, 2)); // false
		
		Equivalence.equals().equivalent('a', 'b');
	}
}
