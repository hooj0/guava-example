package com.cnblogs.hoojo.graph.example;

import org.junit.Test;

import com.cnblogs.hoojo.graph.AbstractGraphTests;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

/**
 * 求最短路径算法：Dijkstra（迪杰斯特拉）算法
 * https://www.jianshu.com/p/92e46d990d17
 * 
 * 算法思想是按路径长度递增的次序一步一步并入来求取，是贪心算法的一个应用，
 * 用来解决单源点到其余顶点的最短路径问题。
 * @author hoojo
 * @createDate 2019年1月22日 下午3:35:53
 * @file DijkstraValueGraphTest.java
 * @package com.cnblogs.hoojo.graph.example
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class DijkstraValueGraphTest extends AbstractGraphTests {

	@Test
	public void test() {
		MutableValueGraph<String, Integer> graph = ValueGraphBuilder.directed()
		        .nodeOrder(ElementOrder.insertion())
		        .expectedNodeCount(10)
		        .build();
		
		String V0 = "v0";
		String V1 = "v1";
		String V2 = "v2";
		String V3 = "v3";
		String V4 = "v4";
		String V5 = "v5";
		
		graph.putEdgeValue(V0, V2, 10);
		graph.putEdgeValue(V0, V4, 30);
		graph.putEdgeValue(V0, V5, 100);
		graph.putEdgeValue(V1, V2, 5);
		graph.putEdgeValue(V2, V3, 50);
		graph.putEdgeValue(V3, V5, 10);
		graph.putEdgeValue(V4, V3, 20);
		graph.putEdgeValue(V4, V5, 60);
		
		out(graph); // isDirected: true, allowsSelfLoops: false, nodes: [v0, v2, v4, v5, v1, v3], edges: {<v0 -> v2>=10, <v0 -> v4>=30, <v0 -> v5>=100, <v2 -> v3>=50, <v4 -> v3>=20, <v4 -> v5>=60, <v1 -> v2>=5, <v3 -> v5>=10}

	}
	
}
