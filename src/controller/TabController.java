package controller;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.StatusBar;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

public final class TabController implements Initializable {

    @FXML
    public TextArea textArea_Logs;
    @FXML
    private TextField textField_Dir;
    @FXML
    private TextField textField_Folder;
    @FXML
    private TextField textField_URL;


    @FXML
    private Button btn_Start;

    @FXML
    private CheckBox checkBox_DownloadSub;
    @FXML
    private CheckBox checkBox_SaveDescription;
    @FXML
    private CheckBox checkBox_NoOverwrites;
    @FXML
    private CheckBox checkBox_UseProxy;
    @FXML
    private CheckBox checkBox_UseFFmpeg;
    @FXML
    private CheckBox checkBox_NoMerge;
    @FXML
    private CheckBox checkBox_Convert;
    @FXML
    private CheckBox checkBox_SaveURL;
    @FXML
    private CheckBox checkBox_Thumbnail;

    @FXML
    private RadioButton radioButton_Single;
    @FXML
    private RadioButton radioButton_Playlist;
    @FXML
    private RadioButton radioButton_720P_MP4;
    @FXML
    private RadioButton radioButton_1080P_MP4;
    @FXML
    private RadioButton radioButton_HighestQuality;
    @FXML
    private RadioButton radioButton_Custom;

    @FXML
    private Label lbl_Dir;
    @FXML
    private Label lbl_Folder;
    @FXML
    private Label lbl_URL;

    @FXML
    public StatusBar statusBar_Main;

    private static final String True = "true";

    private String url; // url of a video or a playlist
    private String folder; // save stuff that youtube-dl download
    private String dir; // user defined directory

    private Properties props; // used to save user defined data to a xml file
    private File config; // xml file used to save props
    private File customCommands;

    private Task<Void> youtube_dl;

    public TabController() {
        props = new Properties();
        config = new File("."+ File.separator + "config.xml");
        customCommands = new File("." + File.separator + "customCommands.txt");
    }

    public void setDir(ActionEvent event) {
        var stage = (Stage) ((Control) event.getSource()).getScene().getWindow();
        var chooser = new DirectoryChooser();
        chooser.setTitle("Select Directory where youtube-dl saves file to");
        if (textField_Dir.getText() != null
                && !textField_Dir.getText().equals("")
                && Files.isDirectory(Paths.get(textField_Dir.getText()))) {

            chooser.setInitialDirectory(new File(textField_Dir.getText()));
        }
        File selectedDirectory = chooser.showDialog(stage);
        if (selectedDirectory != null)
            textField_Dir.setText(selectedDirectory.getAbsolutePath());
    }

    public void setFolder(ActionEvent event) {

        var stage = (Stage) ((Control) event.getSource()).getScene().getWindow();
        var chooser = new DirectoryChooser();
        chooser.setTitle("Select Folder where youtube-dl saves file to");
        if (textField_Dir.getText() != null
                && !textField_Dir.getText().equals("")
                && Files.isDirectory(Paths.get(textField_Dir.getText()))) {
            chooser.setInitialDirectory(new File(textField_Dir.getText()));
        }
        File selectedDirectory = chooser.showDialog(stage);
        if (selectedDirectory != null)
            textField_Folder.setText(selectedDirectory.getName());
    }

    @FXML
    private void startClicked() {

        url = textField_URL.getText();
        dir = textField_Dir.getText();
        folder = textField_Folder.getText();

        if (checkBox_Convert.isSelected())
            props.setProperty("Convert", "true");
        else
            props.setProperty("Convert", "false");
        if (checkBox_NoMerge.isSelected())
            props.setProperty("NoMerge", "true");
        else
            props.setProperty("NoMerge", "false");
        if (checkBox_UseProxy.isSelected())
            props.setProperty("UseProxy", "true");
        else
            props.setProperty("UseProxy", "false");
        if (checkBox_SaveURL.isSelected())
            props.setProperty("SaveURL", "true");
        else
            props.setProperty("SaveURL", "false");
        if (radioButton_Playlist.isSelected())
            props.setProperty("Playlist", "true");
        else
            props.setProperty("Playlist", "false");
        if (checkBox_UseFFmpeg.isSelected())
            props.setProperty("UseFFmpeg", "true");
        else
            props.setProperty("UseFFmpeg", "false");
        if (checkBox_Thumbnail.isSelected())
            props.setProperty("Thumbnail", "true");
        else
            props.setProperty("Thumbnail", "false");
        if (radioButton_720P_MP4.isSelected())
            props.setProperty("720P_MP4", "true");
        else {
            props.setProperty("720P_MP4", "false");
            if (radioButton_1080P_MP4.isSelected())
                props.setProperty("1080P_MP4", "true");
            else {
                props.setProperty("1080P_MP4", "false");
                if (radioButton_HighestQuality.isSelected())
                    props.setProperty("HighestQuality", "true");
                else {
                    props.setProperty("HighestQuality", "false");
                    if (radioButton_Custom.isSelected())
                        props.setProperty("Custom", "true");
                    else
                        props.setProperty("Custom", "false");
                }

            }

        }
        if (checkBox_DownloadSub.isSelected())
            props.setProperty("DownloadSub", "true");
        else
            props.setProperty("DownloadSub", "false");
        if (checkBox_NoOverwrites.isSelected())
            props.setProperty("NoOverwrites", "true");
        else
            props.setProperty("NoOverwrites", "false");
        if (checkBox_SaveDescription.isSelected())
            props.setProperty("SaveDescription", "true");
        else
            props.setProperty("SaveDescription", "false");

        setConfig();
        // validate textFields' data format are correct
        if (validateInput(dir, folder, url)) {
            btn_Start.setDisable(true);
            props.setProperty("Url", url);
            props.setProperty("Dir", dir);
            props.setProperty("Folder", folder);
            setConfig();

            //create user specified folder
            var newFolder = new File(dir + File.separator + folder);
            if (!newFolder.exists())
                newFolder.mkdir();
            if (checkBox_SaveURL.isSelected()) {
                //save url from textField to URL.txt
                var URL = new File(dir + File.separator + folder + File.separator + "URL.txt");
                if (!URL.exists())
                    try {
                        URL.createNewFile();
                        var w = new FileWriter(URL);
                        w.write(url);
                        w.close();
                    } catch (IOException e) {
                        textArea_Logs.appendText("[AutoYoutube-DL] Failed to save URL\n");
                    }
            }
            // if condition above is matched use Task to start youtube-dl
            startYoutube_DL();
        }
    }

    private void startYoutube_DL() {
        var messageQueue = new LinkedBlockingQueue<String>();
        textArea_Logs.clear();
        youtube_dl = new Task<>() {
            @Override
            public Void call() {
                Platform.runLater(() -> new MessageConsumer(messageQueue, textArea_Logs).start());
                try {
                    messageQueue.put("[Youtube-DL] Starting Youtube-dl \n");
                    synchronized (this) {
                        var rt = Runtime.getRuntime();
                        Process youtube_dl = rt.exec(youtube_dl_Parameters(), null, new File("/"));
                        var input = new BufferedReader(new InputStreamReader(youtube_dl.getInputStream()));
                        updateMessage("[Youtube-DL] Youtube-dl is running");
                        String line;
                        while ((line = input.readLine()) != null && !Thread.currentThread().isInterrupted()) {
                            if (line.contains("%")) {
                                //set progress bar
                                var percent = Double.parseDouble(line.substring(line.indexOf("%") - 4, line.indexOf("%")));
                                updateProgress(percent, 100.0);
                            }
                            messageQueue.put(line);
                        }
                        input.close();
                        int exitVal = youtube_dl.waitFor();
                        btn_Start.setDisable(false);
                        updateMessage("[Youtube-DL] Finished | Exist code:" + exitVal);
                        messageQueue.put("[Youtube-DL] Finished | Exist code:" + exitVal + "\n");

                        if (checkBox_Convert.isSelected()) {
                            startFFmpeg();
                        } else {
                            btn_Start.setDisable(false);
                        }
                    }
                } catch (IOException e) {
                    updateMessage("[FATAL] ERROR_YOUTUBEDL_START_FAIL");
                    //textArea_Logs.appendText("[FATAL] ERROR_YOUTUBEDL_START_FAIL \n");
                } catch (InterruptedException e) {
                    //if(Task.class.isInstance(ffmpeg)) ffmpeg.cancel();
                    updateMessage("[Youtube-DL] CANCELED_BY_USER");
                    //textArea_Logs.appendText("[Youtube-DL] INTERRUPTED_BY_USER \n");
                }
                return null;
            }
        };
        statusBar_Main.textProperty().bind(youtube_dl.messageProperty());
        statusBar_Main.progressProperty().bind(youtube_dl.progressProperty());
        new Thread(youtube_dl).start();
    }

    private void startFFmpeg() {
        var messageQueue = new LinkedBlockingQueue<String>();
        var ffmpeg = new Task<>() {
            @Override
            public Void call() {
                var list = new File(dir + File.separator + folder).listFiles();
                Platform.runLater(() -> new MessageConsumer(messageQueue, textArea_Logs).start());
                if (list != null)
                    for (var f : list) {
                        if (f.getName().endsWith((".vtt"))) {
                            try {
                                //updateMessage("[FFmpeg] Starting FFmpeg");
                                    textArea_Logs.appendText("[FFmpeg] Converting SRT formatted subtitle(s) to VTT format\n");
                                    var rt = Runtime.getRuntime();
                                    Process ffmpeg = rt.exec("ffmpeg.exe -n -i " + "\"" + dir
                                            + File.separator + folder + File.separator
                                            + f.getName() + "\" " + "\"" + dir + File.separator
                                            + folder + File.separator
                                            + f.getName().substring(0, f.getName().length() - 4)
                                            + ".srt" + "\"", null, new File("/"));
                                    //updateMessage("[FFmpeg] FFmpeg is running");
                                    var input = new BufferedReader(new InputStreamReader(ffmpeg.getInputStream()));
                                    String line;
                                    while ((line = input.readLine()) != null) {
                                        messageQueue.put(line);
                                    }
                                    input.close();
                                    var exitVal = ffmpeg.waitFor();
                                    textArea_Logs.appendText("[FFmpeg] Finished | Exist code: " + exitVal + "\n");
                                    //updateMessage("[FFmpeg] Finished | Exist code: " + exitVal);
                                    btn_Start.setDisable(false);
                            } catch (IOException e) {
                                //updateMessage("[FATAL] FAILED_TO_START_FFMPEG");
                                textArea_Logs.appendText("[FATAL] FAILED_TO_START_FFMPEG \n");
                            } catch (InterruptedException e) {
                                //updateMessage("[AutoYoutube-DL] CANCELED_BY_USER");
                                textArea_Logs.appendText("[AutoYoutube-DL] INTERRUPTED_BY_USER \n");
                            }
                        }
                    }
                return null;
            }
        };
        new Thread(ffmpeg).start();
    }

    @FXML
    private void interruptYoutube_DL() {
        if (youtube_dl != null) {
            youtube_dl.cancel();
            textArea_Logs.appendText("[AutoYoutube-DL] Download aborted");
            youtube_dl = null;
            btn_Start.setDisable(false);
        }
    }

    @FXML
    private void openOutputFolder() throws IOException {
        if (!textField_Dir.getText().equals("")) {
            Desktop.getDesktop().open(new File(dir + File.separator + folder));
        }
    }

    @FXML
    private void useFFmpegClicked() {

        if (checkBox_UseFFmpeg.isSelected()) {
            checkBox_NoMerge.setDisable(false);
            checkBox_Convert.setDisable(false);
        } else {
            checkBox_NoMerge.setDisable(true);
            checkBox_Convert.setDisable(true);
        }
    }

    private String youtube_dl_Parameters() {
        var youtubedlConfig = new StringBuilder("youtube-dl.exe");
        try {
            customCommands.createNewFile();
            var input = new BufferedReader(new FileReader(customCommands));
            String line;
            while ((line = input.readLine()) != null) youtubedlConfig.append(line);
            input.close();
        } catch (IOException e) {
            textArea_Logs.appendText("[AutoYoutube-DL] customCommands.txt can not be found, automatically created one");
        }
        if (checkBox_DownloadSub.isSelected())
            youtubedlConfig.append(" --write-auto-sub --write-sub --sub-format vtt");
        if (checkBox_SaveDescription.isSelected())
            youtubedlConfig.append(" --write-description");
        if (checkBox_NoOverwrites.isSelected())
            youtubedlConfig.append(" --no-overwrites");
        if (checkBox_UseProxy.isSelected())
            youtubedlConfig.append(" --proxy http://127.0.0.1:1080/");
        if (radioButton_Single.isSelected())
            youtubedlConfig.append(" --no-playlist");
        else
            youtubedlConfig.append(" --yes-playlist");
        if (checkBox_SaveDescription.isSelected())
            youtubedlConfig.append(" --write-description");
        if (!checkBox_NoMerge.isSelected() && !radioButton_Custom.isSelected()) {
            if (radioButton_720P_MP4.isSelected()) {
                youtubedlConfig.append(" -f bestvideo[height<=720][ext=mp4]+bestaudio[ext=m4a]/best[height<=720]");
            } else if (radioButton_1080P_MP4.isSelected()) {
                youtubedlConfig.append(" -f bestvideo[height<=1080][ext=mp4]+bestaudio[ext=m4a]/bestvideo+bestaudio");
            } else {
                youtubedlConfig.append(" -f bestvideo+bestaudio");
            }
            youtubedlConfig.append(" --ffmpeg-location " + "\"").append(System.getProperty("user.dir")).append("\"");

        } else if (checkBox_NoMerge.isSelected() && !radioButton_Custom.isSelected()){
            if (radioButton_720P_MP4.isSelected()) {
                youtubedlConfig.append(" -f bestvideo[height<=720][ext=mp4],bestaudio[ext=m4a]/best[height<=720]");
            } else if (radioButton_1080P_MP4.isSelected()) {
                youtubedlConfig.append(" -f bestvideo[height<=1080][ext=mp4],bestaudio[ext=m4a]/bestvideo,bestaudio");
            } else {
                youtubedlConfig.append(" -f bestvideo,bestaudio");
            }
        }
        if (checkBox_Thumbnail.isSelected())
            youtubedlConfig.append(" --write-thumbnail");
        youtubedlConfig.append(" -o \"").append(dir).append(File.separator).append(folder).append("\\%(title)s.%(ext)s\"");
        youtubedlConfig.append(" ").append(url);
        System.out.println(youtubedlConfig.toString());
        return youtubedlConfig.toString();
    }

    private boolean validateInput(String dir, String folder, String url) {

        statusBar_Main.textProperty().unbind();
        statusBar_Main.progressProperty().unbind();

        if (dir == null || dir.equals("") || !Files.isDirectory(Paths.get(dir))) {
            lbl_Dir.setTextFill(Color.web("#f44b42"));
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.2), lbl_Dir);
            fadeTransition.setFromValue(0.5);
            fadeTransition.setToValue(1);
            fadeTransition.setCycleCount(1);
            fadeTransition.setAutoReverse(true);
            fadeTransition.play();
            statusBar_Main.setText("INVALID DIRECTORY");
            return false;
        } else {
            lbl_Dir.setTextFill(Color.web("#000000"));
            statusBar_Main.setText("");
        }
		/*
		   "# Match a valid Windows filename (unspecified file system).                   \n"
			+ "^                                                        # Anchor to start of string.        \n"
			+ "(?!                                              # Assert filename is not: CON, PRN, \n"
			+ "  (?:                                 # AUX, NUL, COM1, COM2, COM3, COM4, \n"
			+ "    CON|PRN|AUX|NUL|      # COM5, COM6, COM7, COM8, COM9,     \n"
			+ "    COM[1-9]|LPT[1-9]                 # LPT1, LPT2, LPT3, LPT4, LPT5,     \n"
			+ "  )                                              # LPT6, LPT7, LPT8, and LPT9...      \n"
			+ "  (?:\\.[^.]*)?                                   # followed by optional extension     \n"
			+ "  $                                                      # and end of string                  \n"
			+ ")                                                # End negative lookahead assertion. \n"
			+ "[^<>:\"/\\\\|?*\\x00-\\x1F]*              # Zero or more valid filename chars.\n"
			+ "[^<>:\"/\\\\|?*\\x00-\\x1F\\ .]              # Last char is not a space or dot.  \n"
			+ "$                                                        # Anchor to end of string.           ",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.COMMENTS).matcher(folder).matches()
		*/
        if (folder == null
                || folder.equals("")
                || !Pattern.compile(
                "^(?!(?:CON|PRN|AUX|NUL|COM[1-9]|LPT[1-9])(?:\\.[^.]*)?$)[^<>:\"/\\\\|?*\\x00-\\x1F]*[^<>:\"/\\\\|?*\\x00-\\x1F\\ .]$",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.COMMENTS).matcher(folder).matches()) {
            lbl_Folder.setTextFill(Color.web("#f44b42"));
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.2), lbl_Folder);
            fadeTransition.setFromValue(0.5);
            fadeTransition.setToValue(1);
            fadeTransition.setCycleCount(1);
            fadeTransition.setAutoReverse(true);
            fadeTransition.play();
            statusBar_Main.setText("INVALID FOLDER NAME");
            return false;
        } else {
            lbl_Folder.setTextFill(Color.web("#000000"));
            statusBar_Main.setText("");
        }
        // use URL class to validate url
        try {
            URL u = new URL(url);
            u.toURI();
        } catch (Exception e) {
            lbl_URL.setTextFill(Color.web("#f44b42"));
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.2), lbl_URL);
            fadeTransition.setFromValue(0.5);
            fadeTransition.setToValue(1);
            fadeTransition.setCycleCount(1);
            fadeTransition.setAutoReverse(true);
            fadeTransition.play();
            statusBar_Main.setText("INVALID URL");
            return false;
        }

        lbl_URL.setTextFill(Color.web("#000000"));
        statusBar_Main.setText("");

        return true;
    }

    private void getConfig() {
        InputStream input = null;
        if (config.exists()) {
            try {
                input = new FileInputStream(config);
                props.loadFromXML(input);
                if (props.getProperty("UseProxy").equals(True))
                    checkBox_UseProxy.setSelected(true);
                if (props.getProperty("SaveURL").equals(True))
                    checkBox_SaveURL.setSelected(true);
                if (props.getProperty("DownloadSub").equals(True))
                    checkBox_DownloadSub.setSelected(true);
                if (props.getProperty("SaveDescription").equals(True))
                    checkBox_SaveDescription.setSelected(true);
                if (props.getProperty("720P_MP4").equals(True))
                    radioButton_720P_MP4.setSelected(true);
                else if (props.getProperty("1080P_MP4").equals(True))
                    radioButton_1080P_MP4.setSelected(true);
                else if (props.getProperty("HighestQuality").equals(True))
                    radioButton_HighestQuality.setSelected(true);
                else if (props.getProperty("Custom").equals(True))
                    radioButton_Custom.setSelected(true);
                if (props.getProperty("NoOverwrites").equals(True))
                    checkBox_NoOverwrites.setSelected(true);

                if (props.getProperty("Thumbnail").equals(True))
                    checkBox_Thumbnail.setSelected(true);
                if (props.getProperty("Playlist").equals(True))
                    radioButton_Playlist.setSelected(true);
                else radioButton_Single.setSelected(true);

                if (props.getProperty("UseFFmpeg").equals(True)) {
                    checkBox_UseFFmpeg.setSelected(true);
                    if (props.getProperty("Convert").equals(True))
                        checkBox_Convert.setSelected(true);
                    if (props.getProperty("NoMerge").equals(True))
                        checkBox_NoMerge.setSelected(true);
                }
                textField_URL.setText(props.getProperty("Url"));
                textField_Folder.setText(props.getProperty("Folder"));
                textField_Dir.setText(props.getProperty("Dir"));
            } catch (IOException e) {
                textArea_Logs.appendText("[AutoYoutube-DL] Failed to read configuration file\n");
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        textArea_Logs.appendText("[AutoYoutube-DL] Unknown Error occurred during read configuration file\n");
                    }
                }
            }
        } else {
            try {
                if (config.createNewFile()) {
                    textArea_Logs.appendText("[AutoYoutube-DL] Failed to find an existing configuration file, automatically created a new one\n");
                }
            } catch (IOException e) {
                textArea_Logs.appendText("[AutoYoutube-DL] Failed to create config.xml, your settings will not be saved\n");
            }
        }
    }

    private void setConfig() {
        OutputStream output = null;
        try {
            if (config.createNewFile()) {
                textArea_Logs.appendText("[AutoYoutube-DL] Failed to find an existing configuration file, automatically created a new one\n");
            }
            output = new FileOutputStream(config);
            props.storeToXML(output, null, StandardCharsets.UTF_8);
        } catch (IOException e) {
            textArea_Logs.appendText("[AutoYoutube-DL] Failed to Save configuration file\n");
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    textArea_Logs.appendText("[AutoYoutube-DL] Unknown Error occurred during save configuration file\n");
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getConfig();
        if (!checkBox_UseFFmpeg.isSelected()) {
            checkBox_NoMerge.setDisable(true);
            checkBox_Convert.setDisable(true);
        }
        url = textField_URL.getText();
        dir = textField_Dir.getText();
        folder = textField_Folder.getText();
        textArea_Logs.textProperty().addListener(e -> textArea_Logs.setScrollTop(Double.MAX_VALUE));
        statusBar_Main.setText("Ready");
    }
}
