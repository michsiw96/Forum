package pl.edu.mimuw.forum.ui.change;

import pl.edu.mimuw.forum.ui.models.NodeViewModel;

public class DeleteChange extends Change {	
	private NodeViewModel parentNode;
	private NodeViewModel sonNode;
	
	int position;
	
	public DeleteChange(NodeViewModel parent, NodeViewModel son, int position) {
		this.parentNode = parent;
		this.sonNode = son;
		this.position = position;
	}
	
	public void retrieveOldNode() {
		parentNode.getChildren().add(position, sonNode);
	}
	
	public void retrieveNewNode() {
		parentNode.getChildren().remove(sonNode);
	}
}
