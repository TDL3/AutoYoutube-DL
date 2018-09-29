package controller;

import application.AlertDialog;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class MainController implements Initializable {

    @FXML
    private TabPane tabPane_Main;

    public MainController() {
    }

    public void createNewTab() throws IOException {
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

    public void closeTab() {
        if (tabPane_Main.getTabs().size() == 1) {
            Platform.exit();
        } else {
            tabPane_Main.getTabs().remove(tabPane_Main.getSelectionModel().getSelectedItem());
        }
    }

    public void exitByUser() {
        Platform.exit();
    }

    public void loadConfig() {
        Stage stage = (Stage) tabPane_Main.getScene().getWindow();
        var chooser = new FileChooser();
        chooser.setTitle("Select a configuration file");
        File selectedFile = chooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                Files.copy(selectedFile.toPath(),
                        Paths.get(System.getProperty("user.dir"), File.separator, "config.xml"),
                        REPLACE_EXISTING);
                new AlertDialog().informationDialog("Notice",
                        "Successfully loaded",
                        "Please restart the app to apply changes");
            } catch (IOException e) {
                new AlertDialog().exceptionDialog("IOException Occurred",
                        "Failed to load configuration file",
                        "unknown error",
                        e);
            }
        }
    }

   public void saveConfig() {
        Stage stage = (Stage) tabPane_Main.getScene().getWindow();
        var chooser = new FileChooser();
        chooser.setTitle("Select where you want save the configuration file");
        File selectedFile = chooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                Files.copy(Paths.get(".", File.separator, "config.xml"),
                        selectedFile.toPath(),
                        REPLACE_EXISTING);
                new AlertDialog().informationDialog("Notice",
                        "Successfully saved",
                        null);

            } catch (IOException e) {
                new AlertDialog().exceptionDialog("IOException Occurred",
                        "Failed to save configuration file",
                        "unknown error",
                        e);
            }
        }
    }

    public void about() throws IOException {
        var about = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/form/About.fxml"));
        about.initModality(Modality.APPLICATION_MODAL);
        about.setTitle("About AutoYoutube-DL");
        about.getIcons().add(new Image("/third_party/images/about.png"));
        about.setScene(new Scene(root, 400, 250));
        about.setResizable(false);
        about.showAndWait();
    }

    public void customArguments() throws IOException {
        var customCommends = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/form/CustomCommands.fxml"));
        customCommends.initModality(Modality.APPLICATION_MODAL);
        customCommends.setTitle("Custom Commands");
        customCommends.getIcons().add(new Image("/third_party/images/icon.png"));
        customCommends.setScene(new Scene(root, 400, 500));
        customCommends.setResizable(false);
        customCommends.showAndWait();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            createNewTab();
        } catch (IOException e) {
            new AlertDialog().exceptionDialog("IOException Occurred",
                    "An Exception has been thrown",
                    "Could not load fxml, probably caused by corrupted config.xml, try delete it and restart the app",
                     e);
        }
    }
}
