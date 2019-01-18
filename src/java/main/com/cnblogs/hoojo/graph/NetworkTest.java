package com.cnblogs.hoojo.graph;

import java.util.Collection;
import java.util.Set;

import org.junit.Test;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;

/**
 * network graph test example
 * Network中允许并行边（即两个节点间可以有多条同向边，如：节点A和节点B可以有两条同向边：A->B: a-b-1，a-b-2），
 * 这就导致了前面介绍的使用节点作为Map的key的数据结构GraphConnections的逻辑走不下去了，因为节点不唯一了。
 * 使得设计者重新设计了另一套使用边为Key的机制来实现节点的连接关系。
 * 
 * 特性：
 * 	a) 边必须唯一（因为两个相同顶点间可以同时存在多条边）; 
 * 	b) 支持有向和无向边; 
 * 	c) 边的定义支持权值指定; 
 * 	d) 支持并行边.
 * 
 * @author hoojo
 * @createDate 2019年1月15日 下午5:53:20
 * @file NetworkTest.java
 * @package com.cnblogs.hoojo.graph
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class NetworkTest extends BasedTest {

	@Test
	public void testDefined() {
		// 构建有向图网络
		MutableNetwork<Integer, String> network = NetworkBuilder.directed().build();
		// 添加节点
		network.addNode(1);
		out(network); // isDirected: true, allowsParallelEdges: false, allowsSelfLoops: false, nodes: [1], edges: {}

		network.addEdge(2, 3, "2->3");  // 如果尚未存在，还会添加节点2和3
		out(network); // isDirected: true, allowsParallelEdges: false, allowsSelfLoops: false, nodes: [1, 2, 3], edges: {2->3=<2 -> 3>}

		// 获取后继相邻节点
		Set<Integer> successorsOfTwo = network.successors(2);  // returns {3}
		out(successorsOfTwo); // [3]
		
		// 获取当前节点的前趋相邻节点
		out(network.predecessors(3)); // [2]
		
		// 获取边edge的顶点
		out(network.incidentEdges(3));
		
		// 获取nodeU 2 和nodeV 3的直连边
		out(network.edgeConnecting(2, 3)); // Optional[2->3]
		
		// 获取node的出度边
		Set<String> outEdgesOfTwo = network.outEdges(2);   // returns {"2->3"}
		out(outEdgesOfTwo); // [2->3]

		//network.addEdge(2, 3, "2->3 too");  // 异常; 网络不允许平行边缘
		//out(network); 

		network.addEdge(2, 3, "2->3");  // 没有效果;这个边已经存在，并按此顺序连接这些节点
		out(network); // isDirected: true, allowsParallelEdges: false, allowsSelfLoops: false, nodes: [1, 2, 3], edges: {2->3=<2 -> 3>}
	
		// 判断边是否存在
		//Set<String> inEdgesOfFour = network.inEdges(4); // 异常; 节点不在图表中
		//out(inEdgesOfFour);
		
		// 返回一个Graph视图
		out(network.asGraph()); // isDirected: true, allowsSelfLoops: false, nodes: [1, 2, 3], edges: [<2 -> 3>]
	}
	
	@Test
	public void testAPI() {
		// Set<E> inEdges(N node); //node的入度边集合（不同于predecessors()，它返回的入度节点非边）
		// Set<E> outEdges(N node); //node的出度边集合（不同于successors()，它返回的是出度节点非边）
		// EndpointPair<N> incidentNodes(E edge); //边对应的两个端点
		// Set<E> adjacentEdges(E edge); //边的邻接边
		// Set<E> edgesConnecting(N nodeU, N nodeV); //两个节点的连接边集


		int NODE_COUNT = 100;
		int EDGE_COUNT = 200;
		
		// 使用对应构建类NetworkBuilder来构建Network实例：
		MutableNetwork<Integer, String> network1 = NetworkBuilder.directed() //有向网
		    .allowsParallelEdges(true) //允许并行边
		    .allowsSelfLoops(true) //允许自环
		    .nodeOrder(ElementOrder.<Integer>insertion()) //节点顺序
		    .edgeOrder(ElementOrder.<String>insertion()) //边顺序
		    .expectedNodeCount(NODE_COUNT) //期望节点数
		    .expectedEdgeCount(EDGE_COUNT) //期望边数
		    .build();

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
		String E11_A = "1-1-a";
		String E12_A = "1-2-1";
		String E12_B = "1-2-b";
		
		network1.addEdge(N1, N3, E13);
		network1.addEdge(N3, N1, E31);
		network1.addEdge(N3, N4, E34);
		network1.addEdge(N4, N4, E44);
		network1.addEdge(N1, N1, E11);
		network1.addEdge(N1, N1, E11_A );
		network1.addEdge(N1, N2, E12);
		network1.addEdge(N1, N2, E12_A);
		network1.addEdge(N1, N2, E12_B);
		network1.addEdge(N2, N1, E21);

		out("add edges after network1: " + network1); // add edges after network1: isDirected: true, allowsParallelEdges: true, allowsSelfLoops: true, nodes: [1, 3, 4, 2], edges: {1-3=<1 -> 3>, 3-1=<3 -> 1>, 3-4=<3 -> 4>, 4-4=<4 -> 4>, 1-1=<1 -> 1>, 1-1-a=<1 -> 1>, 1-2=<1 -> 2>, 1-2-1=<1 -> 2>, 1-2-b=<1 -> 2>, 2-1=<2 -> 1>}

		// 获取边的邻接边(即边对应两个端点的邻接边集合)
		Set<String> adjacentEdges = network1.adjacentEdges(E12_A);
		out("network1 edge: " + E12_A + ", adjacentEdges: " + format(adjacentEdges)); // network1 edge: 1-2-1, adjacentEdges: 1-1,1-1-a,2-1,3-1,1-2-b,1-2,1-3,

		// 获取两个顶点之间的边集(network中由于顶点间存在平行边，因此两个顶点之间存在多条边)：
		Set<String> networkEdges = network1.edgesConnecting(N1, N2);
		out("network1 node " + N1 + " & " + N2 + " edges: " + format(networkEdges)); // network1 node 1 & 2 edges: 1-2-b,1-2-1,1-2,

		// 返回两个顶点之间的一条边（如果该两个顶点间存在多条边则会抛出异常）：
		String edge = network1.edgeConnectingOrNull(N1, N3);//如果是N1和N2则会抛异常
		out("network1 node " + N1 + " & " + N3 + " edge: " + edge); // network1 node 1 & 3 edge: 1-3

		// 获取节点的邻接边(所有包含该节点的边集合)
		Set<String> incidentEdges = network1.incidentEdges(N1);
		out("network1 node " + N1 + " incidents: " + format(incidentEdges)); // network1 node 1 incidents: 1-1,1-1-a,2-1,3-1,1-2-b,1-2-1,1-2,1-3,

		// 获取边的邻接点(边对应的两个顶点)
		EndpointPair<Integer> incidentNodes =  network1.incidentNodes(E12_A);
		out("network1 edge " + E12_A + " incidentNodes: " + incidentNodes); // network1 edge 1-2-1 incidentNodes: <1 -> 2>
	}
	
	private String format(Collection<?> collections) {
	    StringBuilder builder = new StringBuilder();
	    
	    for (Object value : collections) {
	        builder.append(value);
	        builder.append(",");
	    }
	    
	    return builder.toString();
	}
}
