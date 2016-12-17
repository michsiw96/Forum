package pl.edu.mimuw.forum.ui.controllers;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import pl.edu.mimuw.forum.exceptions.ApplicationException;
import pl.edu.mimuw.forum.ui.bindings.MainPaneBindings;
import pl.edu.mimuw.forum.ui.change.AddChange;
import pl.edu.mimuw.forum.ui.change.DeleteChange;
import pl.edu.mimuw.forum.ui.change.ChangesHistory;
import pl.edu.mimuw.forum.ui.helpers.DialogHelper;
import pl.edu.mimuw.forum.ui.helpers.NodeViewModelHolder;
import pl.edu.mimuw.forum.ui.models.CommentViewModel;
import pl.edu.mimuw.forum.ui.models.NodeViewModel;
import pl.edu.mimuw.forum.ui.tree.ForumTreeItem;
import pl.edu.mimuw.forum.ui.tree.TreeLabel;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Kontroler glownego widoku reprezentujacego forum.
 * Widok sklada sie z drzewa zawierajacego wszystkie wezly forum oraz
 * panelu z polami do edycji wybranego wezla.
 * @author konraddurnoga
 */
public class MainPaneController implements Initializable {

	/**
	 * Korzen drzewa-modelu forum.
	 */
	private NodeViewModel document;

	/**
	 * Wiazania stosowane do komunikacji z {@link pl.edu.mimuw.forum.ui.controller.ApplicationController }.
	 */
	private MainPaneBindings bindings;

	/**
	 * Widok drzewa forum (wyswietlany w lewym panelu).
	 */
	@FXML
	private TreeView<NodeViewModel> treePane;

	/**
	 * Kontroler panelu wyswietlajacego pola do edycji wybranego wezla w drzewie.
	 */
	@FXML
	private DetailsPaneController detailsController;
	
	private ChangesHistory undoRedoBuffer;
	private NodeViewModelHolder wasViewChanged;

	public MainPaneController() {
		bindings = new MainPaneBindings();
		undoRedoBuffer = new ChangesHistory();
		wasViewChanged = new NodeViewModelHolder();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		BooleanBinding nodeSelectedBinding = Bindings.isNotNull(treePane.getSelectionModel().selectedItemProperty());
		bindings.nodeAdditionAvailableProperty().bind(nodeSelectedBinding);
		bindings.nodeRemovaleAvailableProperty()
				.bind(nodeSelectedBinding.and(
						Bindings.createBooleanBinding(() -> getCurrentTreeItem().orElse(null) != treePane.getRoot(),
								treePane.rootProperty(), nodeSelectedBinding)));
		
		bindings.undoAvailableProperty().set(false);	
		bindings.redoAvailableProperty().set(false);
		
		undoRedoBuffer.canUndo().addListener((observable, oldValue, newValue) -> {
			bindings.undoAvailableProperty().set(newValue);
			if (!newValue) {
				bindings.hasChangesProperty().set(false);
			}
			else {
				bindings.hasChangesProperty().set(true);
			}
		});
		
		undoRedoBuffer.canRedo().addListener((observable, oldValue, newValue) -> {
			bindings.redoAvailableProperty().set(newValue);
		});
	}

	public MainPaneBindings getPaneBindings() {
		return bindings;
	}

	/**
	 * Otwiera plik z zapisem forum i tworzy reprezentacje graficzna wezlow forum.
	 * @param file
	 * @return
	 * @throws ApplicationException
	 */
	private void setUndoRedoBufferForTreeNode(NodeViewModel nodeModel) {
		if (nodeModel == null) {
			return;
		}
		else {
			nodeModel.setUndoRedoBuffer(undoRedoBuffer);
			for (NodeViewModel node : nodeModel.getChildren()) {
				setUndoRedoBufferForTreeNode(node);
			}
		}
	}
	
	public Node open(File file) throws ApplicationException {
		if (file != null) {
			try {
			    XStream xstream = new XStream(new DomDriver("Unicode"));
			    
			    Reader rdr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			    ObjectInputStream in = xstream.createObjectInputStream(rdr);
			    
			    xstream.addImplicitCollection(pl.edu.mimuw.forum.data.Node.class, "children");
			    document = ((pl.edu.mimuw.forum.data.Node) in.readObject()).getModel();
			    setUndoRedoBufferForTreeNode(document);
			    
			    bindings.hasChangesProperty().set(false);
			}
			catch (FileNotFoundException e) {
				throw new ApplicationException("File not found.");
			}
			catch (IOException e) {
				throw new ApplicationException("Input/output error.");
			}
			catch (Exception e) {
				throw new ApplicationException("Wrong XML file.");
			}
		} else {
			document = new CommentViewModel("Welcome to a new forum", "Admin");
			document.setUndoRedoBuffer(undoRedoBuffer);
			
			bindings.hasChangesProperty().set(true);
		}

		/** Dzieki temu kontroler aplikacji bedzie mogl wyswietlic nazwe pliku jako tytul zakladki.
		 * Obsluga znajduje sie w {@link pl.edu.mimuw.forum.ui.controller.ApplicationController#createTab }
		 * 				document = Dummy.Create().getModel();
		 */
		getPaneBindings().fileProperty().set(file);
		
		return openInView(document);
	}

	/**
	 * Zapisuje aktualny stan forum do pliku.
	 * @throws ApplicationException
	 */
	public void save() throws ApplicationException {
		/**
		 * Obiekt pliku do ktorego mamy zapisac drzewo znajduje sie w getPaneBindings().fileProperty().get()
		 */
		if (document != null) {
			try {
				PrintWriter pw = new PrintWriter(getPaneBindings().fileProperty().get(), "UTF-8");
			    XStream xstream = new XStream(new DomDriver("Unicode"));
			    
			    xstream.addImplicitCollection(pl.edu.mimuw.forum.data.Node.class, "children");
			    
			    ObjectOutputStream out = xstream.createObjectOutputStream(pw, "Forum");
			    out.writeObject(document.toNode());
			    
			    undoRedoBuffer.clear();
			    
			    out.close();
			} catch (FileNotFoundException e) {
				throw new ApplicationException("File not found.");
			} catch (IOException e) {
				throw new ApplicationException("Wrong XML file.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		bindings.hasChangesProperty().set(false);
	}
	
	/**
	 * Cofa ostatnio wykonana operacje na forum.
	 * @throws ApplicationException
	 */
	public void undo() throws ApplicationException {
		undoRedoBuffer.undo();
	}

	/**
	 * Ponawia ostatnia cofnieta operacje na forum.
	 * @throws ApplicationException
	 */
	public void redo() throws ApplicationException {
		undoRedoBuffer.redo();
	}

	/**
	 * Podaje nowy wezel jako ostatnie dziecko aktualnie wybranego wezla.
	 * @param node
	 * @throws ApplicationException
	 */
	public void addNode(NodeViewModel node) throws ApplicationException {
		getCurrentNode().ifPresent(currentlySelected -> {
			currentlySelected.getChildren().add(node);		// Zmieniamy jedynie model, widok (TreeView) jest aktualizowany z poziomu
															// funkcji nasluchujacej na zmiany w modelu (zob. metode createViewNode ponizej)
		});
		node.setUndoRedoBuffer(undoRedoBuffer);
	}

	/**
	 * Usuwa aktualnie wybrany wezel.
	 */
	public void deleteNode() {
		getCurrentTreeItem().ifPresent(currentlySelectedItem -> {
			TreeItem<NodeViewModel> parent = currentlySelectedItem.getParent();

			NodeViewModel parentModel;
			NodeViewModel currentModel = currentlySelectedItem.getValue();
			if (parent == null) {
				return; // Blokujemy usuniecie korzenia - TreeView bez korzenia jest niewygodne w obsludze
			} else {
				parentModel = parent.getValue();
				parentModel.getChildren().remove(currentModel); // Zmieniamy jedynie model, widok (TreeView) jest aktualizowany z poziomu
																// funkcji nasluchujacej na zmiany w modelu (zob. metode createViewNode ponizej)
			}

		});
	}

	private Node openInView(NodeViewModel document) throws ApplicationException {
		Node view = loadFXML();

		treePane.setCellFactory(tv -> {
			try {
				//Do reprezentacji graficznej wezla uzywamy niestandardowej klasy wyswietlajacej 2 etykiety
				return new TreeLabel();
			} catch (ApplicationException e) {
				DialogHelper.ShowError("Error creating a tree cell.", e);
				return null;
			}
		});

		ForumTreeItem root = createViewNode(document);
		treePane.setRoot(root);

		for (NodeViewModel w : document.getChildren()) {
			addToTree(w, root);
		}

		expandAll(root);

		treePane.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> onItemSelected(oldValue, newValue));

		return view;
	}
	
	private Node loadFXML() throws ApplicationException {
		FXMLLoader loader = new FXMLLoader();
		loader.setController(this);
		loader.setLocation(getClass().getResource("/fxml/main_pane.fxml"));

		try {
			return loader.load();
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
	}
	
	private Optional<? extends NodeViewModel> getCurrentNode() {
		return getCurrentTreeItem().<NodeViewModel> map(TreeItem::getValue);
	}

	private Optional<TreeItem<NodeViewModel>> getCurrentTreeItem() {
		return Optional.ofNullable(treePane.getSelectionModel().getSelectedItem());
	}

	private void addToTree(NodeViewModel node, ForumTreeItem parentViewNode, int position) {
		ForumTreeItem viewNode = createViewNode(node);

		List<TreeItem<NodeViewModel>> siblings = parentViewNode.getChildren();
		siblings.add(position == -1 ? siblings.size() : position, viewNode);

		node.getChildren().forEach(child -> addToTree(child, viewNode));
	}

	private void addToTree(NodeViewModel node, ForumTreeItem parentViewNode) {
		addToTree(node, parentViewNode, -1);
	}

	private void removeFromTree(ForumTreeItem viewNode) {
		viewNode.removeChildListener();
		TreeItem<NodeViewModel> parent = viewNode.getParent();
		if (parent != null) {
			viewNode.getParent().getChildren().remove(viewNode);
		} else {
			treePane.setRoot(null);
		}
	}

	private ForumTreeItem createViewNode(NodeViewModel node) {
		ForumTreeItem viewNode = new ForumTreeItem(node);
		viewNode.setChildListener(change -> {	// wywolywanem, gdy w modelu dla tego wezla zmieni sie zawartosc kolekcji dzieci
			while (change.next()) {
				if (change.wasAdded()) {
					int i = change.getFrom();
					for (NodeViewModel child : change.getAddedSubList()) {
						undoRedoBuffer.registerChange(new AddChange(((ForumTreeItem) viewNode).getValue(), child, i));
						addToTree(child, viewNode, i);	// uwzgledniamy nowy wezel modelu w widoku
						i++;
					}
				}

				if (change.wasRemoved()) {
					for (int i = change.getFrom(); i <= change.getTo(); ++i) {
						undoRedoBuffer.registerChange(new DeleteChange(((ForumTreeItem) viewNode).getValue(), ((ForumTreeItem) viewNode.getChildren().get(i)).getValue(), i));
						removeFromTree((ForumTreeItem) viewNode.getChildren().get(i)); // usuwamy wezel modelu z widoku
					}
				}
			}
		});

		return viewNode;
	}

	private void expandAll(TreeItem<NodeViewModel> item) {
		item.setExpanded(true);
		item.getChildren().forEach(this::expandAll);
	}

	private void onItemSelected(TreeItem<NodeViewModel> oldItem, TreeItem<NodeViewModel> newItem) {
		wasViewChanged.setNode(newItem.getValue());
		detailsController.setModel(newItem != null ? newItem.getValue() : null);
	}

}
