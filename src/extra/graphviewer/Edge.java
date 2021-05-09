package extra.graphviewer;

public class Edge {
	Node _src;
	Node _trgt;
	
	public Edge(Node src, Node trgt) {
		if ( src == null ) 
			throw new IllegalArgumentException("Source node is null!");

		if ( trgt == null ) 
			throw new IllegalArgumentException("Target node is null!");

		_src = src;
		_trgt = trgt;
	}
	
	public Node getSrc() {
		return _src;
	}
	
	public Node getTrgt() {
		return _trgt;
	}
	
	@Override
	public String toString() {
		return _src.getId() + "->" + _trgt.getId();
	}
}
