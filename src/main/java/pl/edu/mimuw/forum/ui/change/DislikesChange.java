package pl.edu.mimuw.forum.ui.change;

import pl.edu.mimuw.forum.ui.models.SurveyViewModel;

public class DislikesChange extends Change {
	private Integer oldDislikesProperty;
	private Integer newDislikesProperty;
	
	private SurveyViewModel changedNode;
	
	public DislikesChange(SurveyViewModel nodeToRegister, Number oldValue, Number newValue) {
		super();
		changedNode = nodeToRegister;
		oldDislikesProperty = oldValue.intValue();
		newDislikesProperty = newValue.intValue();
	}
	
	public void retrieveOldNode() {
		setProperties(changedNode, true);
	}
	
	public void retrieveNewNode() {
		setProperties(changedNode, false);
	}
	
	public void setProperties(SurveyViewModel nodeToSet, boolean oldIfTrue) {
		if (oldIfTrue) nodeToSet.getLikes().set(oldDislikesProperty);
		else nodeToSet.getLikes().set(newDislikesProperty);
	}
}
