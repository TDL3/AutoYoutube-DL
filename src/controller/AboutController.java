package controller;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;

public class AboutController {

	public AboutController() {}
	
	@FXML
	private void openLink(ActionEvent event) throws IOException, URISyntaxException {
		Hyperlink link;
		link = Hyperlink.class.cast(event.getSource());
		Desktop.getDesktop().browse(new URL(link.getText()).toURI());
	}
}
