package pl.edu.mimuw.forum.ui.change;

import pl.edu.mimuw.forum.ui.models.SuggestionViewModel;

public class ResponseAcceptChange extends Change {
	private boolean oldIsResponseAccepted;
	private boolean newIsResponseAccepted;
	
	private SuggestionViewModel changedNode;
	
	public ResponseAcceptChange(SuggestionViewModel nodeToRegister, Boolean oldValue, Boolean newValue) {
		super();
		changedNode = nodeToRegister;
		oldIsResponseAccepted = oldValue;
		newIsResponseAccepted = newValue;
	}
	
	public void retrieveOldNode() {
		setProperties(changedNode, true);
	}
	
	public void retrieveNewNode() {
		setProperties(changedNode, false);
	}
	
	public void setProperties(SuggestionViewModel nodeToSet, boolean oldIfTrue) {
		if (oldIfTrue) nodeToSet.getIsResponseAccepted().set(oldIsResponseAccepted);
		else nodeToSet.getIsResponseAccepted().set(newIsResponseAccepted);
	}
}
