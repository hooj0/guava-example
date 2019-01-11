package com.cnblogs.hoojo.graph;

import com.cnblogs.hoojo.BasedTest;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.google.common.graph.MutableNetwork;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.NetworkBuilder;
import com.google.common.graph.ValueGraphBuilder;

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
public class GraphDefinitionsTest extends BasedTest {

	public void testDefined() {
		MutableGraph<Integer> graph = GraphBuilder.undirected().build();

		MutableValueGraph<City, Distance> roads = ValueGraphBuilder.directed().build();

		MutableNetwork<Webpage, Link> webSnapshot = NetworkBuilder.directed()
		    .allowsParallelEdges(true)
		    .nodeOrder(ElementOrder.natural())
		    .expectedNodeCount(100000)
		    .expectedEdgeCount(1000000)
		    .build();

	}
}
