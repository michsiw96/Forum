package pl.edu.mimuw.forum.ui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.beans.property.ObjectProperty;
import pl.edu.mimuw.forum.ui.helpers.DateTimePicker;
import pl.edu.mimuw.forum.ui.models.TaskViewModel;

public class TaskPaneController extends BasePaneController {

	private TaskViewModel model;
	
	@FXML
	private DateTimePicker dateTimeField;
	
	public void setModel(TaskViewModel model) {
		if (this.model != null) {
			dateTimeField.dateTimeValueProperty().unbindBidirectional(this.model.getDueDate());
		}
		
		this.model = model;
		
		if (this.model != null) {
			dateTimeField.dateTimeValueProperty().bindBidirectional(this.model.getDueDate());
		}
		
		setHasModel(this.model != null);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
	}
	
	public ObjectProperty<Date> dateProperty() {
		return dateTimeField.dateTimeValueProperty();
	}
}
