package pl.edu.mimuw.forum.ui.change;

import pl.edu.mimuw.forum.ui.models.NodeViewModel;

public class ContentChange extends Change {
	private String oldContentProperty;
	private String newContentProperty;
	
	private NodeViewModel changedNode;
	
	public ContentChange(NodeViewModel nodeToRegister, String oldValue, String newValue) {
		super();
		changedNode = nodeToRegister;
		oldContentProperty = oldValue;
		newContentProperty = newValue;
	}
	
	public void retrieveOldNode() {
		setProperties(changedNode, true);
	}
	
	public void retrieveNewNode() {
		setProperties(changedNode, false);
	}
	
	public void setProperties(NodeViewModel nodeToSet, boolean oldIfTrue) {
		if (oldIfTrue) nodeToSet.getContent().set(oldContentProperty);
		else nodeToSet.getContent().set(newContentProperty);
	}
	
	
}
