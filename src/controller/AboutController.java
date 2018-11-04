package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Hyperlink;

import java.awt.*;
import java.net.URL;

public class AboutController {

    public AboutController() {
    }

    public void openLink(ActionEvent event) {
        Hyperlink link = (Hyperlink) event.getSource();
        try {
            Desktop.getDesktop().browse(new URL(link.getText()).toURI());
        } catch (Exception e) { } //intended
    }
}
