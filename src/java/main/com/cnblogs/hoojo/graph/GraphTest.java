package com.cnblogs.hoojo.graph;

import java.util.Set;

import org.junit.Test;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

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
	}
	
	public void test1() {
		/*
		MutableGraph<Integer> graph = GraphBuilder.undirected().build();

		MutableValueGraph<City, Distance> roads = ValueGraphBuilder.directed().build();

		MutableNetwork<Webpage, Link> webSnapshot = NetworkBuilder.directed()
		    .allowsParallelEdges(true)
		    .nodeOrder(ElementOrder.natural())
		    .expectedNodeCount(100000)
		    .expectedEdgeCount(1000000)
		    .build();
		    */
	}
}
