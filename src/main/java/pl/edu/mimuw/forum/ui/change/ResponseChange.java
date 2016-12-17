package pl.edu.mimuw.forum.ui.change;

import pl.edu.mimuw.forum.ui.models.SuggestionViewModel;

public class ResponseChange extends Change {
	private String oldResponseProperty;
	private String newResponseProperty;
	
	private SuggestionViewModel changedNode;
	
	public ResponseChange(SuggestionViewModel nodeToRegister, String oldValue, String newValue) {
		super();
		changedNode = nodeToRegister;
		oldResponseProperty = oldValue;
		newResponseProperty = newValue;
	}
	
	public void retrieveOldNode() {
		setProperties(changedNode, true);
	}
	
	public void retrieveNewNode() {
		setProperties(changedNode, false);
	}
	
	public void setProperties(SuggestionViewModel nodeToSet, boolean oldIfTrue) {
		 if (oldIfTrue) nodeToSet.getResponse().set(oldResponseProperty);
		 else nodeToSet.getResponse().set(newResponseProperty);
	}
}
