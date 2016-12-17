package pl.edu.mimuw.forum.ui.change;

import pl.edu.mimuw.forum.ui.models.NodeViewModel;

public class AuthorChange extends Change {
	private String oldAuthorProperty;
	private String newAuthorProperty;
	
	private NodeViewModel changedNode;
	
	public AuthorChange(NodeViewModel nodeToRegister, String oldValue, String newValue) {
		super();
		changedNode = nodeToRegister;
		oldAuthorProperty = oldValue;
		newAuthorProperty = newValue;
	}
	
	public void retrieveOldNode() {
		setProperties(changedNode, true);
	}
	
	public void retrieveNewNode() {
		setProperties(changedNode, false);
	}
	
	public void setProperties(NodeViewModel nodeToSet, boolean oldIfTrue) {
		if (oldIfTrue) nodeToSet.getAuthor().set(oldAuthorProperty);
		else nodeToSet.getAuthor().set(newAuthorProperty);
	}
	
	
}
