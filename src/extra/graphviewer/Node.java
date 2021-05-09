package extra.graphviewer;

public class Node {
	String _id;

	int _x;
	int _y;

	public Node(String id) {
		this(id, 0, 0);
	}

	public Node(String id, int x, int y) {
		_id = id;
		_x = x;
		_y = y;
	}

	public String getId() {
		return _id;
	}

	public int getX() {
		return _x;
	}

	public int getY() {
		return _y;
	}

	public void setX(int x) {
		this._x = x;
	}

	public void setY(int y) {
		this._y = y;
	}
	
	@Override
	public String toString() {
		return _id;
	}

}
