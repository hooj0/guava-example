package com.cnblogs.hoojo.graph;

import com.google.common.graph.*;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * graph test example
 * Guava库的目录common.graph包含的模块是一个描述实体(entity)以及实体之间的关系的图数据结构模型库。
 * 
 * 特性：
 * 	a) 顶点唯一; 
 * 	b) 支持有向边和无向边; 
 * 	c) 边只能通过两个顶点隐式定义; 
 * 	d) 不支持并行边。
 * 
 * 例如：网页与超链接、科学家与他们写的论文、机场与其航线、人与其家族等。Guava-Graph模块的目的是提供一种通用以及可扩展的语言来描述类似上述的举例。
 * https://www.jianshu.com/nb/20139843
 * 
 * @author hoojo
 * @createDate 2019年1月7日 下午6:02:41
 * @file GraphDefinitionsTest.java
 * @package com.cnblogs.hoojo.graph
 * @project guava-example
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class GraphTest extends AbstractGraphTests {

	@Test
	public void testDirected() {
		// 有向图
		MutableGraph<Integer> graph = GraphBuilder.directed().build();
		
		// 添加节点
		graph.addNode(1);
		// 添加边
		graph.putEdge(2, 3);  // 如果尚未存在，还会添加节点2和3
		
		out(graph); // isDirected: true, allowsSelfLoops: false, nodes: [1, 2, 3], edges: [<2 -> 3>]

		// 后继节点
		Set<Integer> successorsOfTwo = graph.successors(2); // returns {3}
		out(successorsOfTwo); // [3]

		graph.putEdge(2, 3);  // 没有效果;图不支持平行边
		out(graph); // isDirected: true, allowsSelfLoops: false, nodes: [1, 2, 3], edges: [<2 -> 3>]
		
		// 获取node的邻接点
		out(graph.adjacentNodes(2)); // [3]
		// 获取node的入度
		out(graph.inDegree(3)); // 1
	}
	
	@Test
	public void testBuilder() {
		MutableGraph<String> graph = GraphBuilder.undirected().build();
		
		//将节点加入有向图中
	    graph.addNode("A");
	    graph.addNode("I");
	    graph.addNode("C");
	    graph.addNode("B");
	    graph.addNode("G");
	    graph.addNode("D");
	    graph.addNode("H");
	    graph.addNode("E");
	    graph.addNode("F");
	    graph.addNode("J");
	    graph.addNode("K");

	    //根据示例图加入有向边（终点-起点）
	    graph.putEdge("A", "B");
	    graph.putEdge("B", "G");
	    graph.putEdge("C", "B");
	    graph.putEdge("C", "H");
	    graph.putEdge("D", "A");
	    graph.putEdge("D", "C");
	    graph.putEdge("E", "D");
	    graph.putEdge("E", "J");
	    graph.putEdge("E", "K");
	    graph.putEdge("E", "F");
	    graph.putEdge("F", "C");
	    graph.putEdge("F", "I");
	    graph.putEdge("H", "G");
	    graph.putEdge("I", "H");
	    graph.putEdge("J", "D");
	    graph.putEdge("K", "F");

	
		// 输出有向图的大小（节点个数）
		final int size = graph.nodes().size();
		out("size: " + size); // size: 11
		
		// 输出节点‘E’的入度边
		Set<String> incomingEdges = graph.predecessors("E"); // ‘E’的前趋
		out("node E, incomming: " + format(incomingEdges)); // node E, incomming: D,J,F,K,
		
		// 输出节点'C'的出度边
		Set<String> outgoingEdges = graph.successors("C"); // ‘C’的后继
		out("node C, outgoings: " + format(outgoingEdges)); // node C, outgoings: H,D,B,F,

		// 深度优先遍历
		Iterable<String> dfs = Traverser.forGraph(graph).depthFirstPostOrder("G");
		for (String value : dfs) {
		    out("value: " + value);
		}
	}
	
	@Test
	public void testAPI() {
		int NODE_COUNT = 1000;
		
		MutableGraph<Integer> graph = GraphBuilder.directed() // 指定为有向图
			    .nodeOrder(ElementOrder.<Integer>insertion()) // 节点按插入顺序输出(还可以取值无序unordered()、节点类型的自然顺序natural())
			    .expectedNodeCount(NODE_COUNT) // 预期节点数
			    .allowsSelfLoops(true) // 允许自环
			    .build();

		out(graph); // isDirected: true, allowsSelfLoops: true, nodes: [], edges: []

		Integer N1 = 1;
		Integer N2 = 2;
		Integer N3 = 3;
		Integer N4 = 4;
		
		//插入边(默认会将节点加入graph中)
		graph.putEdge(N2, N3);
		graph.putEdge(N1, N3);
		graph.putEdge(N1, N2);
		graph.putEdge(N2, N2);
		
		// 添加节点
		graph.addNode(N4);

		//返回图中所有的节点(顺序依赖nodeOrder)
		Set<Integer> nodes = graph.nodes(); 
		// 按节点的插入先后顺序输出
		out("graph nodes count:" + nodes.size() + ", nodes value:" + format(nodes)); // graph nodes count:4, nodes value:2,3,1,4,
		
		// unordered() 无序：节点的输出顺序
		// nodes value:{3,4,1,2}

		// natural() 自然顺序：节点输出顺序
		// nodes value:{1,2,3,4}

		//返回图中所有的边集合
		Set<EndpointPair<Integer>> edges = graph.edges();
		out("graph edge count:" + edges.size() + ", edges value:" + format(edges)); // graph edge count:4, edges value:<2 -> 2>,<2 -> 3>,<1 -> 2>,<1 -> 3>,
		
		// 获取节点的前趋列表：
		Set<Integer> predecessors = graph.predecessors(N2); // 获取N2的前趋
		// 对于允许自环的图allowsSelfLoops(true)中，一条自环边在有向图中既是前趋也是后继，既是入度也是出度。
		out("graph node:" + N2 + " predecessors:" + format(predecessors)); // graph node:2 predecessors:1,2,

		// 获取节点的后继列表
		graph.putEdge(N2, N4); //图上面示例图中红色边所示，动态增加了一条边
		out(graph); // isDirected: true, allowsSelfLoops: true, nodes: [2, 3, 1, 4], edges: [<2 -> 4>, <2 -> 2>, <2 -> 3>, <1 -> 2>, <1 -> 3>]
		
		Set<Integer> successors = graph.successors(N2); // 获取N2的后继
		out("add edge of (" + N2 + "->" + N4 + ") after graph node:" 
				+ N2 + " successors:" + format(successors)); // add edge of (2->4) after graph node:2 successors:4,2,3,

		// 获取节点的邻接点列表(包括前趋和后继)：
		Set<Integer> adjacents = graph.adjacentNodes(N2); // 获取N2的邻接点
		out("graph node: " + N2 + ", adjacents: " + format(adjacents)); // graph node: 2, adjacents: 4,1,2,3,

		// 获取节点的度(入度和出度)：
		out("graph node: " + N2 + ", degree: " + graph.degree(N2)+ ", indegree: " + graph.inDegree(N2) + ", outdegree: " + graph.outDegree(N2)); //N2的度、入度、出度
		// 自环既是入度也是出度
		// graph node: 2, degree: 5, indegree: 2, outdegree: 3

		// 判断顶点连通性(是否有直连边)：
		final boolean connecting23 = graph.hasEdgeConnecting(N2, N3); // N2&N3 是否连通
		final boolean connecting14 = graph.hasEdgeConnecting(N1, N4); // N1&N4 是否连通
		out("graph node " + N2 + " & " + N3 + " connecting: " + connecting23 + ", node " + N1 + " & " + N4 + " connecting: " + connecting14); // graph node 2 & 3 connecting: true, node 1 & 4 connecting: false //N1&N4之间无边

		// 返回此图中的边，其端点包含节点
		out("incident edges: " + graph.incidentEdges(N2)); // [<1 -> 2>, <2 -> 2>, <2 -> 4>, <2 -> 3>]
		// 是否是有向图
		out("is directed：" + graph.isDirected()); // true
		
		// 转换成不可变graph(Immutable**类型)
		ImmutableGraph<Integer> immutableGraph = ImmutableGraph.copyOf(graph);
		nodes = immutableGraph.nodes(); //返回图中所有的节点(顺序依赖nodeOrder)
		out("immutable graph nodes count:" + nodes.size() + ", nodes value:" + format(nodes)); // immutable graph nodes count:4, nodes value:2,3,1,4, //同被拷贝图顺序

		// 判断是否存在环(第一个顶点和最后一个顶点相同的路径称为环)
		final boolean cycle = Graphs.hasCycle(graph);
		out("graph has cycle: " + cycle); // graph has cycle: true // 因为N2节点存在一条自环，如果去掉则不存在环
		
		// 获取仅包含指定节点的生成子图：
		Set<Integer> subNodes = new HashSet<>();
		subNodes.add(N1);
		subNodes.add(N2); //获取只包含N1和N2的生成子图
		MutableGraph<Integer> subgraph = Graphs.inducedSubgraph(graph, subNodes);
		out("subgraph: " + subgraph); // subgraph: isDirected: true, allowsSelfLoops: true, nodes: [1, 2], edges: [<1 -> 2>, <2 -> 2>]

		// 获取节点的可到达列表(获取能访问到的节点结合，不单指直连边)：
		Set<Integer> reachNodes = Graphs.reachableNodes(graph, N2); // N2的可达列表
		// 这里是通过从起始点N开始进行BFS遍历的结果。
		out("graph node: " + N2 + ", reachNodes: " + format(reachNodes)); // graph node: 2, reachNodes: 2,4,3, //N2不存在能访问到N1的边

		// 获取图的传递闭包(如果节点A的可达列表reachableNodes(A)包含节点B，
		// 则在节点A和节点B之间增加一条直连边)，具体参考有向图的传递闭包概念。
		Graph<Integer> graph2 = Graphs.transitiveClosure(graph);
		out("transitiveClosure graph2: " + graph2); // transitiveClosure graph2: isDirected: true, allowsSelfLoops: true, nodes: [2, 4, 3, 1], edges: [<2 -> 4>, <2 -> 2>, <2 -> 3>, <4 -> 4>, <3 -> 3>, <1 -> 4>, <1 -> 1>, <1 -> 2>, <1 -> 3>]

		// 获取有向图的的反转图：
		Graph<Integer> graph3 = Graphs.transpose(graph);
		out("transpose graph3: " + graph3); // transpose graph3: isDirected: true, allowsSelfLoops: true, nodes: [2, 3, 1, 4], edges: [<2 -> 1>, <2 -> 2>, <3 -> 1>, <3 -> 2>, <4 -> 2>]
		
		// 图的遍历
		// -----------------------------------------------------------------------
		// 深度优先-后序
		Iterable<Integer> dfs = Traverser.forGraph(graph).depthFirstPostOrder(N1); 
		out("dfs traverser: " + format(dfs)); // dfs traverser: 4,3,2,1,

		// 深度优先-前序
		Iterable<Integer> dfsPre =Traverser.forGraph(graph).depthFirstPreOrder(N1); 
		out("dfs pre traverser: " + format(dfsPre)); // dfs pre traverser: 1,2,4,3,

		// 广度优先
		Iterable<Integer> bfs =Traverser.forGraph(graph).breadthFirst(N1);
		out("bfs traverser: " + format(bfs)); // bfs traverser: 1,2,3,4,

		// 删除节点(对应的连接边也将删除)
		// 删除节点，或者删除边时，需要将对应的连接关系也要删除，因此又涉及到了关系结构GraphConnections，此处也重点分析一下：
		graph.removeNode(N2); //删除一个节点N2
		edges = graph.edges();
		out("graph remove node of (" + N2 +  ") after graph edge count:" 
				+ edges.size() + ", edges value:" + format(edges)); // graph remove node of (2) after graph edge count:1, edges value:<1 -> 3>,

		// 构建类Builder的from()接口只能复制其属性值，而并不会复制相应的节点和边：
		// build of from()仅仅复制其属性，节点和边不会复制过来
		MutableGraph<Integer> graph4 = GraphBuilder.from(graph3).build(); 
		Set<EndpointPair<Integer>> edges4 = graph4.edges();
		out("graph4 edge count:" + edges4.size() + ", edges value:" + format(edges4));
	}
}
