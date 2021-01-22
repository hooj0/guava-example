package com.cnblogs.hoojo.collections._new;

import com.cnblogs.hoojo.BasedTest;
import org.junit.Test;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.RowSortedTable;
import com.google.common.collect.Tables;
import com.google.common.collect.TreeBasedTable;

/**
 * table 集合
HashBasedTable：本质上用HashMap<R, HashMap<C, V>>实现；
TreeBasedTable：本质上用TreeMap<R, TreeMap<C,V>>实现；
ImmutableTable：本质上用ImmutableMap<R, ImmutableMap<C, V>>实现；注：ImmutableTable对稀疏或密集的数据集都有优化。
ArrayTable：要求在构造时就指定行和列的大小，本质上由一个二维数组实现，以提升访问速度和密集Table的内存利用率。

<table class="src" style="box-sizing: border-box; border-collapse: collapse; border-spacing: 0px;  border-color: rgb(214, 214, 214); width: 603.333374023438px; vertical-align: top; margin-top: 8px; margin-bottom: 8px; color: rgb(49, 49, 49);    font-size: 14.4444446563721px; line-height: 22px; background-color: rgb(247, 247, 247);">
	<tbody style="box-sizing: border-box;">
		<tr style="box-sizing: border-box;">
			<th style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px; width: 38.8888893127441px; background: rgb(238, 238, 238);">
				S.N.</th>
			<th style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px; background: rgb(238, 238, 238);">
				方法 &amp; 描述</th>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				1</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">Set&lt;Table.Cell&lt;R,C,V&gt;&gt; cellSet()</b><br style="box-sizing: border-box;">
				返回集合中的所有行键/列键/值三元组。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				2</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">void clear()</b><br style="box-sizing: border-box;">
				从表中删除所有映射。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				3</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">Map&lt;R,V&gt; column(C columnKey)</b><br style="box-sizing: border-box;">
				返回在给定列键的所有映射的视图。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				4</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">Set&lt;C&gt; columnKeySet()</b><br style="box-sizing: border-box;">
				返回一组具有表中的一个或多个值的列键。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				5</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">Map&lt;C,Map&lt;R,V&gt;&gt; columnMap()</b><br style="box-sizing: border-box;">
				返回关联的每一列键与行键对应的映射值的视图。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				6</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">boolean contains(Object rowKey, Object columnKey)</b><br style="box-sizing: border-box;">
				返回true，如果表中包含与指定的行和列键的映射。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				7</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">boolean containsColumn(Object columnKey)</b><br style="box-sizing: border-box;">
				返回true，如果表中包含与指定列的映射。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				8</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">boolean containsRow(Object rowKey)</b><br style="box-sizing: border-box;">
				返回true，如果表中包含与指定的行键的映射关系。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				9</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">boolean containsValue(Object value)</b><br style="box-sizing: border-box;">
				返回true，如果表中包含具有指定值的映射。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				10</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">boolean equals(Object obj)</b><br style="box-sizing: border-box;">
				比较指定对象与此表是否相等。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				11</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">V get(Object rowKey, Object columnKey)</b><br style="box-sizing: border-box;">
				返回对应于给定的行和列键，如果没有这样的映射存在值，返回null。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				12</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">int hashCode()</b><br style="box-sizing: border-box;">
				返回此表中的哈希码。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				13</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">boolean isEmpty()</b><br style="box-sizing: border-box;">
				返回true，如果表中没有映射。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				14</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">V put(R rowKey, C columnKey, V value)</b><br style="box-sizing: border-box;">
				关联指定值与指定键。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				15</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">void putAll(Table&lt;? extends R,? extends C,? extends V&gt; table)</b><br style="box-sizing: border-box;">
				复制从指定的表中的所有映射到这个表。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				16</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">V remove(Object rowKey, Object columnKey)</b><br style="box-sizing: border-box;">
				如果有的话，使用给定键相关联删除的映射<span style="font-size: 14.4444446563721px;">。</span></td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				17</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">Map&lt;C,V&gt; row(R rowKey)</b><br style="box-sizing: border-box;">
				返回包含给定行键的所有映射的视图。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				18</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">Set&lt;R&gt; rowKeySet()</b><br style="box-sizing: border-box;">
				返回一组行键具有在表中的一个或多个值。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				19</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">Map&lt;R,Map&lt;C,V&gt;&gt; rowMap()</b><br style="box-sizing: border-box;">
				返回关联的每一行按键与键列对应的映射值的视图。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				20</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">int size()</b><br style="box-sizing: border-box;">
				返回行键/列键/表中的值映射关系的数量。</td>
		</tr>
		<tr style="box-sizing: border-box;">
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				21</td>
			<td style="box-sizing: border-box; border-collapse: collapse;  border-color: rgb(214, 214, 214); padding: 5px;">
				<b style="box-sizing: border-box;">Collection&lt;V&gt; values()</b><br style="box-sizing: border-box;">
				返回所有值，其中可能包含重复的集合。</td>
		</tr>
	</tbody>
</table>

 * @author hoojo
 * @createDate 2017年10月16日 上午11:32:12
 * @file TableCollectionTest.java
 * @package com.cnblogs.hoojo.collections
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class TableCollectionTest extends BasedTest {

	@Test
	public void testApi() {
		// R, C, V
		HashBasedTable<String, String, Integer> table = HashBasedTable.create();
		
		table.put("china", "guangzhou", 18900);
		table.put("china", "beijin", 56000);
		table.put("china", "shanghai", 55000);
		table.put("china", "wuhan", 15000);
		
		table.put("America", "New York", 11111);
		table.put("America", "Los Angeles", 20025);
		table.put("America", "San Francisco", 400025);
		
		//{china={guangzhou=18900, beijin=56000, shanghai=55000, wuhan=15000}, America={New York=11111, Los Angeles=20025, San Francisco=400025}}
		out(table);
		
		// Map<String, Map<String, Integer>>
		out(table.rowMap().get("china")); // {guangzhou=18900, beijin=56000, shanghai=55000, wuhan=15000}
		out(table.rowKeySet()); //[china, America]
		out(table.cellSet()); // [(china,guangzhou)=18900, (china,beijin)=56000, (china,shanghai)=55000, (china,wuhan)=15000, (America,New York)=11111, (America,Los Angeles)=20025, (America,San Francisco)=400025]
		
		out(table.contains("china", "beijin")); // true
		out(table.containsColumn("wuhan")); // true
		out(table.containsValue(55000)); // true
		
		out(table.size()); // 7
		out(table.column("beijin")); // {china=56000}
		out(table.columnKeySet()); // [guangzhou, beijin, shanghai, wuhan, New York, Los Angeles, San Francisco]
		out(table.columnMap()); // {guangzhou={china=18900}, beijin={china=56000}, shanghai={china=55000}, wuhan={china=15000}, New York={America=11111}, Los Angeles={America=20025}, San Francisco={America=400025}}
		
		out(table.row("china")); // {guangzhou=18900, beijin=56000, shanghai=55000, wuhan=15000}
	}
	
	@Test
	public void testTable() {
		TreeBasedTable<String, String, Integer> tree = TreeBasedTable.create();
		tree.put("china", "guangzhou", 18900);
		tree.put("china", "beijin", 56000);
		tree.put("china", "shanghai", 55000);
		tree.put("china", "wuhan", 15000);
		
		tree.put("America", "New York", 11111);
		tree.put("America", "Los Angeles", 20025);
		tree.put("America", "San Francisco", 400025);
		
		// SortedMap<String, Map<String, Integer>> 
		out(tree.rowMap()); // {America={Los Angeles=20025, New York=11111, San Francisco=400025}, china={beijin=56000, guangzhou=18900, shanghai=55000, wuhan=15000}}
		
		ImmutableTable<String, String, Integer> immu = ImmutableTable.<String, String, Integer>builder().put("china", "guangzhou", 18900)
		.put("china", "beijin", 56000)
		.put("china", "shanghai", 55000)
		.put("china", "wuhan", 15000)
		
		.put("America", "New York", 11111)
		.put("America", "Los Angeles", 20025)
		.put("America", "San Francisco", 400025).build();
		
		// ImmutableMap<String, Map<String, Integer>>
		out(immu.rowMap()); // {china={guangzhou=18900, beijin=56000, shanghai=55000, wuhan=15000}, America={New York=11111, Los Angeles=20025, San Francisco=400025}}
		
		RowSortedTable<String, String, Integer> row = Tables.unmodifiableRowSortedTable(tree);
		out(row); // {America={Los Angeles=20025, New York=11111, San Francisco=400025}, china={beijin=56000, guangzhou=18900, shanghai=55000, wuhan=15000}}
	}
}
