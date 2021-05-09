package extra.graphviewer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Graph {

	private List<Node> _nodes;
	private List<Edge> _edges;
	private Map<String, Node> _nodeById;

	public Graph() {
		_nodes = new ArrayList<>();
		_edges = new ArrayList<>();
		_nodeById = new HashMap<>();
	}

	public Graph(JSONObject jo) {
		_nodes = new ArrayList<>();
		_edges = new ArrayList<>();
		_nodeById = new HashMap<>();

		for (Object n : jo.getJSONArray("nodes")) {
			JSONObject node = (JSONObject) n;
			addNode(new Node(node.getString("id"), node.getInt("x"), node.getInt("y")));
		}

		for (Object e : jo.getJSONArray("edges")) {
			JSONObject edge = (JSONObject) e;
			addEdge(new Edge(_nodeById.get(edge.getString("src")), _nodeById.get(edge.getString("trgt"))));
		}

	}

	public void addNode(Node n) {
		if (_nodeById.get(n.getId()) == null) {
			_nodeById.put(n.getId(), n);
			_nodes.add(n);
		} else {
			throw new IllegalArgumentException("A node with id '" + n.getId() + "' alredy exists!");
		}
	}

	public void addEdge(Edge e) {
		_edges.add(e);
	}

	public List<Node> getNodes() {
		return _nodes;
	}

	public List<Edge> getEdges() {
		return _edges;
	}

	@Override
	public String toString() {
		return "Nodes: " + _nodes + System.lineSeparator() + "Edges: " + _edges;
	}
}
