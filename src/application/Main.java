package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/form/Main.fxml"));
        primaryStage.setTitle("AutoYoutube-DL 2.3.3");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/third_party/images/icon.png")));
        primaryStage.setScene(new Scene(root, 500, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) { launch(args);
    }
}