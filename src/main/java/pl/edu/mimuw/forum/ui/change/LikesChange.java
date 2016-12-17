package pl.edu.mimuw.forum.ui.change;

import pl.edu.mimuw.forum.ui.models.SurveyViewModel;

public class LikesChange extends Change {
	private Integer oldLikesProperty;
	private Integer newLikesProperty;
	
	private SurveyViewModel changedNode;
	
	public LikesChange(SurveyViewModel nodeToRegister, Number oldValue, Number newValue) {
		super();
		changedNode = nodeToRegister;
		oldLikesProperty = oldValue.intValue();
		newLikesProperty = newValue.intValue();
	}
	
	public void retrieveOldNode() {
		setProperties(changedNode, true);
	}
	
	public void retrieveNewNode() {
		setProperties(changedNode, false);
	}
	
	public void setProperties(SurveyViewModel nodeToSet, boolean oldIfTrue) {
		if (oldIfTrue) nodeToSet.getLikes().set(oldLikesProperty);
		else nodeToSet.getLikes().set(newLikesProperty);
	}
}
