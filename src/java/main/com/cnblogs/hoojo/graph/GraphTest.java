package com.cnblogs.hoojo.graph;

import java.util.Set;

import org.junit.Test;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.google.common.graph.Traverser;

/**
 * 图的定义 graph defined
 * Guava库的目录common.graph包含的模块是一个描述实体(entity)以及实体之间的关系的图数据结构模型库。
 * 
 * 例如：网页与超链接、科学家与他们写的论文、机场与其航线、人与其家族等。Guava-Graph模块的目的是提供一种通用以及可扩展的语言来描述类似上述的举例。
 * 
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
public class GraphTest extends BasedTest {

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
	public void test2() {
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

		//深度优先遍历
		Iterable<String> dfs = Traverser.forGraph(graph).depthFirstPostOrder("G");
		for (String value : dfs) {
		    out("value: " + value);
		}
	}
	
	// 格式化节点函数：
	private String format(Set<?> collections) {
	    StringBuilder builder = new StringBuilder();
	    for (Object value : collections) {
	        builder.append(value);
	        builder.append(",");
	    }
	    return builder.toString();
	}

	@Test
	public void test1() {
		int NODE_COUNT = 1000;
		
		MutableGraph<Integer> graph = GraphBuilder.directed() // 指定为有向图
			    .nodeOrder(ElementOrder.<Integer>insertion()) // 节点按插入顺序输出(还可以取值无序unordered()、节点类型的自然顺序natural())
			    .expectedNodeCount(NODE_COUNT) //预期节点数
			    .allowsSelfLoops(true) //允许自环
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
		graph.addNode(N4);

		//返回图中所有的节点(顺序依赖nodeOrder)
		Set<Integer> nodes = graph.nodes(); 
		// 按节点的插入先后顺序输出
		out("graph1 nodes count:" + nodes.size() + ", nodes value:" + format(nodes)); // graph1 nodes count:4, nodes value:2,3,1,4,

		//返回图中所有的边集合
		Set<EndpointPair<Integer>> edges = graph.edges();
		out("graph1 edge count:" + edges.size() + ", edges value:" + format(edges)); // graph1 edge count:4, edges value:<2 -> 2>,<2 -> 3>,<1 -> 2>,<1 -> 3>,

	}
}
