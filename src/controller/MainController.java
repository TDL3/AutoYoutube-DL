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
	private Stage aboutStage;
   
	public MainController() {
		aboutStage = new Stage();
	}
	
	@FXML
	private void createNewTab() throws IOException {
		Tab tab = new Tab("Youtube-DL");
		Parent root = FXMLLoader.load(getClass().getResource("/form/NewTab.fxml"));
		tab.setContent(root);
		tabPane_Main.getTabs().add(tab);
		tabPane_Main.getSelectionModel().select(tab);
		
	}
	
	@FXML
	private void closeTab (ActionEvent event) {	
		tabPane_Main.getTabs().remove(tabPane_Main.getSelectionModel().getSelectedItem());
	}
	
	@FXML
	private void exitByUser(ActionEvent event) {
		Platform.exit();
	}
	
	@FXML
	private void loadConfig() {
		
	}
	
	@FXML
	private void saveConfig() {
		
	}
	
	@FXML
	private void about(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/form/About.fxml"));
		aboutStage.initModality(Modality.APPLICATION_MODAL);
		aboutStage.setTitle("About AutoYoutube-DL");
		aboutStage.getIcons().add(new Image("/thirdparty/images/about.png"));
		aboutStage.setScene(new Scene(root, 400, 250));
		aboutStage.setResizable(false);
		aboutStage.showAndWait();
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
