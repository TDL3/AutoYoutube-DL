package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController implements Initializable {
	
	@FXML private MenuItem menuItem_NewTab;
	@FXML private MenuItem menuItem_CloseTab;
	@FXML private MenuItem menuItem_Exit;
	@FXML private MenuItem menuItem_About;
	@FXML private MenuItem menuItem_LoadConfig;
	@FXML private MenuItem menuItem_SaveConfig;
	@FXML private TabPane tabPane_Main;
   
	public MainController() {}
	
	@FXML
	private void createNewTab() throws IOException {
		var tab = new Tab("Youtube-DL");
		Parent root = FXMLLoader.load(getClass().getResource("/form/NewTab.fxml"));
		tab.setContent(root);

		tab.setOnCloseRequest(e -> {
			//e.consume();
			if (tabPane_Main.getTabs().size() == 1) Platform.exit();
			});
		tabPane_Main.getTabs().add(tab);
		tabPane_Main.getSelectionModel().select(tab);
		
	}
	
	@FXML
	private void closeTab (ActionEvent event) {	
		if (tabPane_Main.getTabs().size() == 1) {
			Platform.exit();
		} else {
			tabPane_Main.getTabs().remove(tabPane_Main.getSelectionModel().getSelectedItem());
		}
	}
	
	@FXML
	private void exitByUser(ActionEvent event) {
		Platform.exit();
	}
	
	@FXML
	private void loadConfig() {
		//TODO
	}
	
	@FXML
	private void saveConfig() {
		//TODO
	}
	
	@FXML
	private void about(ActionEvent event) throws IOException {
		var about = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/form/About.fxml"));
		about.initModality(Modality.APPLICATION_MODAL);
		about.setTitle("About AutoYoutube-DL");
		about.getIcons().add(new Image("/thirdparty/images/about.png"));
		about.setScene(new Scene(root, 400, 250));
		about.setResizable(false);
		about.showAndWait();
	}

	@FXML
	private void customArguments() throws IOException {
		var customCommends = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/form/CustomCommands.fxml"));
		customCommends.initModality(Modality.APPLICATION_MODAL);
		customCommends.setTitle("Custom Commands");
		customCommends.getIcons().add(new Image("/thirdparty/images/icon.png"));
		customCommends.setScene(new Scene(root, 400, 500));
		customCommends.setResizable(false);
		customCommends.showAndWait();


	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
			try {
				createNewTab();
			} catch (IOException e) {
				//TODO Create a alertBox to alert user
			}
	}
}
