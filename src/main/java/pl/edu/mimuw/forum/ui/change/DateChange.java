package pl.edu.mimuw.forum.ui.change;

import java.util.Date;

import pl.edu.mimuw.forum.ui.models.TaskViewModel;

public class DateChange extends Change {
	private Date oldDateProperty;
	private Date newDateProperty;
	
	private TaskViewModel changedNode;
	
	public DateChange(TaskViewModel nodeToRegister, Date oldValue, Date newValue) {
		super();
		changedNode = nodeToRegister;
		oldDateProperty = oldValue;
		newDateProperty = newValue;
	}
	
	public void retrieveOldNode() {
		setProperties(changedNode, true);
	}
	
	public void retrieveNewNode() {
		setProperties(changedNode, false);
	}
	
	public void setProperties(TaskViewModel nodeToSet, boolean oldIfTrue) {
		 if (oldIfTrue) nodeToSet.getDueDate().set(oldDateProperty);
		 else nodeToSet.getDueDate().set(newDateProperty);
	}
}
