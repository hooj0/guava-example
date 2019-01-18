package com.cnblogs.hoojo.graph;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.MutableValueGraph;
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
public class ValueGraphTest extends BasedTest {

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
		
		MutableValueGraph<Integer, String> graph1 = ValueGraphBuilder.directed() //有向
		    .allowsSelfLoops(true) //允许自环
		    .expectedNodeCount(NODE_COUNT) //期望节点数
		    .nodeOrder(ElementOrder.<Integer>insertion()) //节点顺序
		    .build();

		out("initlized graph: " + graph1); // initlized graph: isDirected: true, allowsSelfLoops: true, nodes: [], edges: {}

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
		
		graph1.putEdgeValue(N3, N1, E31);
		graph1.putEdgeValue(N3, N4, E34);
		graph1.putEdgeValue(N4, N4, E44);
		graph1.putEdgeValue(N1, N1, E11);
		graph1.putEdgeValue(N1, N2, E12);
		graph1.putEdgeValue(N2, N1, E21);
		graph1.putEdgeValue(N1, N3, E13);

		out("put edges after graph1: " + graph1); // put edges after graph1: isDirected: true, allowsSelfLoops: true, nodes: [3, 1, 4, 2], edges: {<3 -> 4>=3-4, <3 -> 1>=3-1, <1 -> 1>=1-1, <1 -> 2>=1-2, <1 -> 3>=1-3, <4 -> 4>=4-4, <2 -> 1>=2-1}


		//返回图中所有的节点(顺序依赖nodeOrder)
		Set<Integer> nodes = graph1.nodes(); 
		out("graph1 nodes count:" + nodes.size() + ", nodes value:" + format(nodes)); // graph1 nodes count:4, nodes value:3,1,4,2,

	}
	
	private String format(Collection<?> collections) {
	    StringBuilder builder = new StringBuilder();
	    for (Object value : collections) {
	        builder.append(value);
	        builder.append(",");
	    }
	    return builder.toString();
	}
	
	private String format(Iterable<?> iterable) {
	    StringBuilder builder = new StringBuilder();
	    Iterator<?> iter = iterable.iterator();
	    while (iter.hasNext()) {
	    	 builder.append(iter.next());
		        builder.append(",");
	    }
	    return builder.toString();
	}
}
