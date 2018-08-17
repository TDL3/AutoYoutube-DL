package controller;

import javafx.fxml.FXML;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class CustomArgumentsController implements Initializable {
    @FXML
    private TextArea textArea_customYoutube_DL_Arguments;

    private File customCommands = new File(System.getProperty("user.dir") + File.separator + "customCommands.txt");

    public CustomArgumentsController() {}

    @FXML
    private void saveCustomYoutube_DL_Arguments() {
        try {
            var output = new BufferedWriter(new FileWriter(customCommands));
            output.write(textArea_customYoutube_DL_Arguments.getText());
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(!customCommands.exists()) {
            try {
                customCommands.createNewFile();
            } catch (IOException e) {
                textArea_customYoutube_DL_Arguments.setText("[FATAL] customCommands.txt does not exists and can not be created, your custom arguments will not be applied");
            }
        } else {
            try {
                var input = new BufferedReader(new FileReader(customCommands));
                var buffer = new char[(int)customCommands.length()];
                input.read(buffer);
                input.close();
                textArea_customYoutube_DL_Arguments.appendText(new String(buffer));
            } catch (Exception e) {
                textArea_customYoutube_DL_Arguments.setText("[FATAL] customCommands.txt appears to be not readable");
            }
        }
    }
}
