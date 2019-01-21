package com.cnblogs.hoojo.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.Graph;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.Traverser;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;

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
	
	/**
	 * AOE网对应研究实际问题是工程的工期问题：
	 * （1）完成一项工程至少需要多少时间？
	 * （2）哪些活动是影响整个工程进度的关键？
	 * @createDate 2019年1月21日 下午5:56:49
	 */
	@Test
	public void testAOE() {
		MutableValueGraph<String, Integer> graph = ValueGraphBuilder.directed()
			    .nodeOrder(ElementOrder.insertion())
			    .expectedNodeCount(10)
			    .build();

		String V1 = "v1";
		String V2 = "v2";
		String V3 = "v3";
		String V4 = "v4";
		String V5 = "v5";
		String V6 = "v6";
		String V7 = "v7";
		String V8 = "v8";
		String V9 = "v9";
		
		graph.putEdgeValue(V1, V2, 6);
		graph.putEdgeValue(V1, V3, 4);
		graph.putEdgeValue(V1, V4, 5);
		graph.putEdgeValue(V2, V5, 1);
		graph.putEdgeValue(V3, V5, 1);
		graph.putEdgeValue(V4, V6, 2);
		graph.putEdgeValue(V5, V7, 9);
		
		graph.putEdgeValue(V5, V8, 7);
		graph.putEdgeValue(V6, V8, 4);
		graph.putEdgeValue(V7, V9, 2);
		graph.putEdgeValue(V8, V9, 4);
		out("graph: " + graph); // graph: isDirected: true, allowsSelfLoops: false, nodes: [v1, v2, v3, v4, v5, v6, v7, v8, v9], edges: {<v1 -> v2>=6, <v1 -> v3>=4, <v1 -> v4>=5, <v2 -> v5>=1, <v3 -> v5>=1, <v4 -> v6>=2, <v5 -> v7>=9, <v5 -> v8>=7, <v6 -> v8>=4, <v7 -> v9>=2, <v8 -> v9>=4}

		/**
		 * 利用Traverser接口将graph进行拓扑排序topologically，此处返回的逆拓扑排序
		 */
		String startNode = "v1";
		Iterable<String> topologicallys = Traverser.forGraph(graph).depthFirstPostOrder(startNode);

		// 倒序
		out("topologically: " + format(topologicallys)); // topologically: v9,v7,v8,v5,v2,v3,v6,v4,v1,
		
		// 递推求得ve(j)值：
		Map<String, Integer> ves = getVeValues(graph, topologicallys);
		out("ves: " + format(ves)); // ves: {v6:7, v7:16, v8:14, v9:18, v1:0, v2:6, v3:4, v4:5, v5:7, }

	}
	
	/**
	 * ve(j) = Max{ve(i) + dut(<i,j>) }; <i,j>属于T，j=1,2...,n-1
	 * @param graph
	 * @param topologicallys
	 * @return
	 */
	private static Map<String, Integer> getVeValues(ValueGraph<String, Integer> graph, 	Iterable<String> topologicallys) {
	    List<String> reverses = Lists.newArrayList(topologicallys.iterator());
	    Collections.reverse(reverses); // 将逆拓扑排序反向
	    
	    Map<String, Integer> ves = new HashMap<>(); // 结果集
	    // 从前往后遍历
	    for (String node : reverses) {
	        ves.put(node, 0); // 每个节点的ve值初始为0

	        // 获取node的前趋列表
	        Set<String> predecessors = graph.predecessors(node); 
	        int maxValue = 0;
	        
	        // 找前趋节点+当前活动耗时最大的值为当前节点的ve值
	        for (String predecessor : predecessors) {
	            maxValue = Math.max(ves.get(predecessor) + graph.edgeValueOrDefault(predecessor, node, 0), maxValue);
	        }
	        ves.put(node, maxValue);
	    }
	    return ves;
	}

}
