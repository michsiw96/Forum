package pl.edu.mimuw.forum.ui.controllers;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import pl.edu.mimuw.forum.ui.models.CommentViewModel;
import pl.edu.mimuw.forum.ui.models.TaskViewModel;
import pl.edu.mimuw.forum.ui.models.SurveyViewModel;
import pl.edu.mimuw.forum.ui.models.SuggestionViewModel;
import pl.edu.mimuw.forum.ui.models.NodeViewModel;

public class DialogController implements Initializable {
	@FXML
	private ToggleGroup group;
	
	@FXML
	private TextField userField;
	
	@FXML
	private TextArea commentField;
	
	@FXML
	private Dialog<NodeViewModel> dialog;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		
		dialog.setResultConverter(buttonType -> returnResult(buttonType));
	}
	
	private NodeViewModel returnResult(ButtonType buttonType) {
		RadioButton selectedNode = (RadioButton) group.getSelectedToggle();
		
		if (buttonType == ButtonType.OK) {
			switch (selectedNode.getText()) {
				case "Comment":
					return new CommentViewModel(userField.getText(), commentField.getText());
				case "Task":
					return new TaskViewModel(userField.getText(), commentField.getText(), new Date());
				case "Survey":
					return new SurveyViewModel(userField.getText(), commentField.getText());
				case "Suggestion":
					return new SuggestionViewModel(userField.getText(), commentField.getText(), "");
			}
		}
		else {
			return null;
		}
		return null;
	}
}
