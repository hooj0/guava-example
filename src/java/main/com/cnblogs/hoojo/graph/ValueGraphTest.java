package com.cnblogs.hoojo.graph;

import com.google.common.graph.ElementOrder;
import com.google.common.graph.Graph;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import org.junit.Test;

import java.util.Set;

/**
 * value graph test example
 * 
 * 特性：
 * a) 顶点必须唯一,边可以不唯一; 
 * b) 支持有向和无向边; 
 * c) 边的定义支持权值指定; 
 * d) 不支持并行边.
 * 
 * @author hoojo
 * @createDate 2019年1月15日 下午5:52:56
 * @file ValueGraphTest.java
 * @package com.cnblogs.hoojo.graph
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class ValueGraphTest extends AbstractGraphTests {

	@Test
	public void testDefined() {
		// 有向图
		MutableValueGraph<Integer, Double> weightedGraph = ValueGraphBuilder.directed().build();
		// 添加节点
		weightedGraph.addNode(1);
		out(weightedGraph); // isDirected: true, allowsSelfLoops: false, nodes: [1], edges: {}

		// 添加边
		weightedGraph.putEdgeValue(2, 3, 1.5);  // 如果尚未存在，还会添加节点2和3
		out(weightedGraph); // isDirected: true, allowsSelfLoops: false, nodes: [1, 2, 3], edges: {<2 -> 3>=1.5}

		weightedGraph.putEdgeValue(3, 5, 1.5);  // 边值（如Map值）不必是唯一的
		out(weightedGraph); // isDirected: true, allowsSelfLoops: false, nodes: [1, 2, 3, 5], edges: {<2 -> 3>=1.5, <3 -> 5>=1.5}
		
		weightedGraph.putEdgeValue(2, 3, 2.0);  // 将（2,3）的值更新为2.0
		out(weightedGraph); // isDirected: true, allowsSelfLoops: false, nodes: [1, 2, 3, 5], edges: {<2 -> 3>=2.0, <3 -> 5>=1.5}
	}
	
	@Test
	public void testAPI() {
		// 使用对应构建类ValueGraphBuilder来构建ValueGraph实例：
		// 节点类型为Integer,边类型为String
		int NODE_COUNT = 100;
		
		MutableValueGraph<Integer, String> graph = ValueGraphBuilder.directed() //有向
		    .allowsSelfLoops(true) //允许自环
		    .expectedNodeCount(NODE_COUNT) //期望节点数
		    .nodeOrder(ElementOrder.<Integer>insertion()) //节点顺序
		    .build();

		out("initlized graph: " + graph); // initlized graph: isDirected: true, allowsSelfLoops: true, nodes: [], edges: {}

		Integer N1 = 1;
		Integer N2 = 2;
		Integer N3 = 3;
		Integer N4 = 4;
		String E11 = "1-1";
		String E12 = "1-2";
		String E13 = "1-3";
		String E21 = "2-1";
		String E31 = "3-1";
		String E34 = "3-4";
		String E44 = "4-4";
		
		graph.putEdgeValue(N3, N1, E31);
		graph.putEdgeValue(N3, N4, E34);
		graph.putEdgeValue(N4, N4, E44);
		graph.putEdgeValue(N1, N1, E11);
		graph.putEdgeValue(N1, N2, E12);
		graph.putEdgeValue(N2, N1, E21);
		graph.putEdgeValue(N1, N3, E13);

		out("put edges after graph: " + graph); // put edges after graph: isDirected: true, allowsSelfLoops: true, nodes: [3, 1, 4, 2], edges: {<3 -> 4>=3-4, <3 -> 1>=3-1, <1 -> 1>=1-1, <1 -> 2>=1-2, <1 -> 3>=1-3, <4 -> 4>=4-4, <2 -> 1>=2-1}


		//返回图中所有的节点(顺序依赖nodeOrder)
		Set<Integer> nodes = graph.nodes(); 
		out("graph nodes count:" + nodes.size() + ", nodes value:" + format(nodes)); // graph nodes count:4, nodes value:3,1,4,2,

		// 获取连接边：
		String edge = graph.edgeValueOrDefault(N1, N2, "@null");
		out("graph node " + N1 + " & " + N2 + " edge: " + edge); // graph node 1 & 2 edge: 1-2

		// asGraph()转换为Graph视图
		// asGraph()实际上是重新new了一个AbstractGraph，只是它的接口实现是调用了Graph本身的接口，因此如果修改asGraph()返回的视图的数据，其变化也会反映在Graph本身上，反之亦然。
		Graph<Integer> graph5 = graph.asGraph();
		out("asGraph:" + graph5); // asGraph:isDirected: true, allowsSelfLoops: true, nodes: [3, 1, 4, 2], edges: [<3 -> 4>, <3 -> 1>, <1 -> 1>, <1 -> 2>, <1 -> 3>, <4 -> 4>, <2 -> 1>]
	}
}
