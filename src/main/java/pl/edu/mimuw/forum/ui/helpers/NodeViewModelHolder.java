package pl.edu.mimuw.forum.ui.helpers;

import pl.edu.mimuw.forum.ui.models.NodeViewModel;

public class NodeViewModelHolder {
	private NodeViewModel nodeHolded;
	
	public NodeViewModelHolder() {
		
	}
	
	public NodeViewModelHolder(NodeViewModel nodeToRegister) {
		this.nodeHolded = nodeToRegister;
	}
	
	public void setNode(NodeViewModel node) {
		this.nodeHolded = node;
	}
	
	public NodeViewModel getNode() {
		return nodeHolded;
	}
}
