package com.cnblogs.hoojo.functional;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.base.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Ordering;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * 函数式编程
 * @author hoojo
 * @createDate 2017年11月9日 下午5:50:01
 * @file FunctionalTest.java
 * @package com.cnblogs.hoojo.functional
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class FunctionalTest extends BasedTest {

	@Test
	public void testFun() {
		Function<Character, Integer> fun = new Function<Character, Integer>() {
			@Override
			public Integer apply(Character input) {
				out("fun-Character:" + input);
				return (int) input.charValue();
			}
		};
		
		Function<Number, Integer> after = new Function<Number, Integer>() {
			@Override
			public Integer apply(Number input) {
				out("andThen-Number:" + input);
				return input.intValue();
			}
		};
		
		Function<Number, Character> before = new Function<Number, Character>() {
			@Override
			public Character apply(Number input) {
				out("compose-Number:" + input);
				return (char) input.intValue();
			}
		};
		
		out("fun result:" + fun.apply('a')); // fun result:97
		out();
		
		out("andThen-result:" + fun.andThen(after).apply('A'));
		out();
		
		out("compose-result:" + fun.compose(before).apply(65));
		
		out("-------------------------");
		out(fun.andThen(after).compose(before).apply(122));
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
		
		out(foo.apply("jdk")); // true
		out(foo.apply("api")); // false
		
		out(foo.test("jdk")); // true
		out(foo.test("api")); // false
		
		// contains a && k
		out(foo.and(bar).test("api")); // false
		// contains a && k
		out(foo.and(bar).test("api jdk")); // true
		
		// not foo.test() = !foo.test()
		out(foo.negate().test("api")); // true
		out(foo.negate().test("jdk")); // false
		
		// ||
		out(foo.or(bar).test("api")); // true
		out(foo.or(bar).test("jdk")); // true
		out(foo.or(bar).test("open")); // false
		out(foo.or(bar).test("open jkd api")); // true
		
		// !(foo && bar || foo)
		out(foo.and(bar).or(foo).negate().test("abcd"));
	}
	
	@Test
	public void testSupplier() {
		
		Supplier<String> currentTimeMillis = new Supplier<String>() {
			@Override
			public String get() {
				return String.valueOf(System.currentTimeMillis());
			}
		};
		
		out(currentTimeMillis.get());
	}
	
	@Test
	public void testFunctions() {
		
		// 组合
		Function<String, Character> comp = Functions.compose(new Function<Number, Character>() {
			@Override
			public Character apply(Number input) {
				Character data = (char) (input.intValue() + 1);
				out("Number->Character:" + input + "->" + data);
				return data;
			}
		}, new Function<String, Integer>() {
			@Override
			public Integer apply(String input) {
				int data = input.codePointAt(0) + 1;
				out("String->Integer:" + input + "->" + data);
				return data;
			}
		});
		
		// String->Integer:A->66
		// Number->Character:66->C
		// C
		out(comp.apply("A"));
		out(comp.apply("a"));
		
		// 常量
		Function<Object, Integer> cost = Functions.constant(12);
		out(cost.apply(2)); // 12
		
		Map<Object, Object> map = ImmutableMap.of("name", "jack", 65, 'A');
		// map
		out(Functions.forMap(map).apply(65)); // A
		out(Functions.forMap(map, "NotFound").apply("age")); // NotFound
		
		// default fun
		out(Functions.identity().apply(3)); // 3
		// Object -> String
		out(Functions.toStringFunction().apply(67) + 1); // 671
		
		Function<String, Boolean> bool = Functions.forPredicate(new Predicate<String>() {
			@Override
			public boolean apply(String input) {
				return input.contains("k");
			}
		});
		
		out(bool.apply("jdk")); // true
		out(bool.apply("api")); // false
		
		// factory
		out(Functions.forSupplier(new Supplier<String>() {
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
		out(Predicates.alwaysFalse().test(null)); // false
		// const true
		out(Predicates.alwaysTrue().apply(null)); // true
		
		// and &&
		out(Predicates.and(foo, bar).apply("jdk api")); // true
		out(Predicates.and(foo, bar).apply("guava api")); // false
		
		// foo && bar && anonymous 
		out(Predicates.and(foo, bar, (input) -> {
			return input.contains("g");
		}).test("jdk8 guava api")); // true
		
		out(Predicates.and(foo, bar, (input) -> {
			return input.contains("g");
		}).test("my guava api")); // false
		
		// or ||
		out(Predicates.or(bar, foo).test("open")); // false
		out(Predicates.or(bar, foo).test("jdk")); // true
		out(Predicates.or(bar, foo, (input) -> { return input.contains("g"); }).test("guava")); // true
		
		// assignable class
		out(Predicates.subtypeOf(Number.class).apply(String.class)); // false
		out(Predicates.subtypeOf(Number.class).apply(Integer.class)); // true
		out(Predicates.subtypeOf(Number.class).apply(Long.class)); // true
		out(Predicates.subtypeOf(Number.class).apply(Number.class)); // true
		out(Predicates.subtypeOf(Number.class).apply(Object.class)); // false
		
		out(Predicates.compose(foo, (Character input) -> {
			return input + "";
		}).test('z')); // false
		
		out(Predicates.compose(foo, (Character input) -> {
			return input.toString();
		}).test('k')); // true
		
		// contains
		Predicate<CharSequence> regex = Predicates.contains(Pattern.compile("[a-z][0-9]"));
		out(regex.test("jdk9")); // true
		out(regex.test("KFC")); // false
		
		// contains
		out(Predicates.containsPattern("[0-9]").test("abc")); // false
		out(Predicates.containsPattern("[0-9]").test("guava23")); // true
		
		// equal
		out(Predicates.equalTo("A").test("Z")); // false
		out(Predicates.equalTo("A").test("A")); // true
		
		// in
		out(Predicates.in(Arrays.asList(1, 3, 5)).test(2)); // false
		out(Predicates.in(Arrays.asList(1, 3, 5)).test(3)); // true
		
		// instanceOf
		out(Predicates.instanceOf(Number.class).test("33")); // false
		out(Predicates.instanceOf(Number.class).test(22)); // true
		
		// is null
		out(Predicates.isNull().test(null)); // true
		out(Predicates.isNull().test("")); // false
		out(Predicates.isNull().test(2)); // false
		
		// not !
		out(Predicates.not((input) -> {
			return input.toString().contains("u");
		}).test("guava")); // false

		out(Predicates.not((input) -> {
			return input.toString().contains("u");
		}).test("jdk")); // true
		
		// not null
		out(Predicates.notNull().test(null)); // false
		out(Predicates.notNull().test("")); // true
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
		out(result); // #bcdef
		
		Ordering<Integer> ordering = Ordering.<String>natural().onResultOf(new Function<Integer, String>() {
			@Override
			public String apply(Integer input) {
				return "" + input + new Random(System.currentTimeMillis()).nextLong();
			}
		});
		
		List<Integer> list = Arrays.asList(1, 5, 2, 0);
		out(ordering.sortedCopy(list)); // [0, 1, 2, 5]
		
		out(Equivalence.identity().onResultOf(new Function<Integer, String>() {
			@Override
			public String apply(Integer input) {
				return "cast-" + input;
			}
		}).equivalent(1, 2)); // false
		
		out(Equivalence.identity().onResultOf(new Function<Integer, String>() {
			@Override
			public String apply(Integer input) {
				return "cast-" + input;
			}
		}).equivalent(1, 2)); // false
		
		Equivalence.equals().equivalent('a', 'b');
	}

	@Test
	public void testConvert() {
		Converter<String, Long> stringLongConverter = new Converter<String, Long>() {
			@Override
			protected Long doForward(String object) {
				return Long.valueOf(object);
			}

			@Override
			protected String doBackward(Long object) {
				return String.valueOf(object);
			}

			@Override
			public String toString() {
				return "string2long";
			}
		};

		Long LONG_VAL = 12345L;
		String STR_VAL = "12345";

		ImmutableList<String> STRINGS = ImmutableList.of("123", "456");
		ImmutableList<Long> LONGS = ImmutableList.of(123L, 456L);

		out(stringLongConverter.convert(STR_VAL)); // 12345
		out(stringLongConverter.reverse().convert(LONG_VAL)); // 12345

		stringLongConverter.convertAll(STRINGS).forEach(System.out::print); // 123456
		out();

		Converter<StringWrapper, String> wrapperStringConverter = new Converter<StringWrapper, String>() {
			@Override
			protected String doForward(StringWrapper object) {
				return object.value;
			}

			@Override
			protected StringWrapper doBackward(String object) {
				return new StringWrapper(object);
			}

			@Override
			public String toString() {
				return "StringWrapper";
			}
		};

		Converter<StringWrapper, Long> converter = wrapperStringConverter.andThen(stringLongConverter);
		out(converter.convert(new StringWrapper(STR_VAL))); // 12345
		out(converter.reverse().convert(LONG_VAL).value); // 12345

		Converter<String, String> converterIdentity = Converter.identity();
		out(converterIdentity.convert(STR_VAL)); // 12345
		out(converterIdentity.reverse().convert(STR_VAL)); // 12345
	}

	private static class StringWrapper {
		private final String value;

		public StringWrapper(String value) {
			this.value = value;
		}
	}
}
