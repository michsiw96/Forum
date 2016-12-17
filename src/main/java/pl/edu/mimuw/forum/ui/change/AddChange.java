package pl.edu.mimuw.forum.ui.change;

import pl.edu.mimuw.forum.ui.models.NodeViewModel;

public class AddChange extends Change {
	private NodeViewModel parentNode;
	private NodeViewModel sonNode;
	
	int position;
	
	public AddChange(NodeViewModel parent, NodeViewModel son, int position) {
		this.parentNode = parent;
		this.sonNode = son;
		this.position = position;
	}
	
	public void retrieveOldNode() {
		parentNode.getChildren().remove(sonNode);
	}
	
	public void retrieveNewNode() {
		parentNode.getChildren().add(position, sonNode);
	}
}
