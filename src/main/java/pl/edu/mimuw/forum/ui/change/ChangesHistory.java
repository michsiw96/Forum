package pl.edu.mimuw.forum.ui.change;

import java.util.Stack;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import pl.edu.mimuw.forum.exceptions.ApplicationException;

public class ChangesHistory {
	private Stack<Change> undoStack = new Stack<Change>();
	private Stack<Change> redoStack = new Stack<Change>();
	
	private boolean hasListenerGotBlankChange;
	
	private BooleanProperty canUndo;
	private BooleanProperty canRedo;
	
	public ChangesHistory() {
		hasListenerGotBlankChange = false;
		canUndo = new SimpleBooleanProperty(false);
		canRedo = new SimpleBooleanProperty(false);
	}
	
	public void registerChange(Change change) {
		if (hasListenerGotBlankChange) {
			hasListenerGotBlankChange = false;
			return;
		}
		if (!canUndo.get()) {
			canUndo.set(true);
		}
		while (!redoStack.empty()) {
			redoStack.pop();
		}
		if (canRedo.get()) {
			canRedo.set(false);
		}
		undoStack.push(change);
	}
	
	public void undo() throws ApplicationException {
		if (undoStack.empty()) {
			throw new ApplicationException("Undo not possible.");
		}
		else {
			hasListenerGotBlankChange = true;
			undoStack.peek().retrieveOldNode();
			redoStack.push(undoStack.pop());
			System.out.println("undo done");
			if (!canRedo.get()) {
				canRedo.set(true);
			}
			if (undoStack.empty() && canUndo.get()) {
				canUndo.set(false);
			}
		}
	}
	
	public void redo() throws ApplicationException {
		if (redoStack.empty()) {
			throw new ApplicationException("Redo not possible.");
		}
		else {
			hasListenerGotBlankChange = true;
			redoStack.peek().retrieveNewNode();
			undoStack.push(redoStack.pop());
			System.out.println("redo done");
			if (!canUndo.get()) {
				canUndo.set(true);
			}
			if (redoStack.empty() && canRedo.get()) {
				canRedo.set(false);
			}
		}
	}
	
	public void clear() {
		while (!undoStack.empty()) {
			undoStack.pop();
		}
		if (canUndo.get()) {
			canUndo.set(false);
		}
		while (!redoStack.empty()) {
			redoStack.pop();
		}
		if (canRedo.get()) {
			canRedo.set(false);
		}
	}
	
	public BooleanProperty canUndo() {
		return canUndo;
	}
	
	public BooleanProperty canRedo() {
		return canRedo;
	}
}
