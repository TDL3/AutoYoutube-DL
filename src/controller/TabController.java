package controller;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

import org.controlsfx.control.StatusBar;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public final class TabController implements Initializable {

	@FXML
	public TextArea textArea_Status;
	@FXML
	public TextArea textArea_Logs;
	@FXML
	private TextField textField_Dir;
	@FXML
	private TextField textField_Folder;
	@FXML
	private TextField textField_URL;

	@FXML
	private Button btn_SelectDir;
	@FXML
	private Button btn_Cancel;
	@FXML
	private Button btn_Start;
	@FXML
	private Button btn_openOutPut;
	@FXML
	private Button btn_SelectFolder;

	@FXML
	private CheckBox checkBox_DownloadSub;
	@FXML
	private CheckBox checkBox_SaveDiscription;
	@FXML
	private CheckBox checkBox_NoOverwrites;
	@FXML
	private CheckBox checkBox_1080P;
	@FXML
	private CheckBox checkBox_UseProxy;
	@FXML
	private CheckBox checkBox_Single;
	@FXML
	private CheckBox checkBox_List;
	@FXML
	private CheckBox checkBox_UseFFmpeg;
	@FXML
	private CheckBox checkBox_Merge;
	@FXML
	private CheckBox checkBox_Convert;
	@FXML
	private CheckBox checkBox_SaveURL;
	@FXML
	private CheckBox checkBox_AutoClose;
	@FXML
	private CheckBox checkBox_Thumnail;

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
	private String dir; // user defiend directory
	private String currentPath; // directory where program is located

	private Properties props; // used to save user defined data to a xml file

	private File URL; // txt file used to save url
	private File newFolder; // create directory that user defined to save youtube-dl downloads
	private File config; // xml file used to save props

	// Reference of Youtube_DL class
	// private Youtube_DL youtubedl;
	//private Thread youtube_dl = null;
	// private Thread ffmpeg = null;
	private Task<Void> ffmpeg;
	private Task<Void> youtube_dl;
	
	
	public TabController() {
		currentPath = System.getProperty("user.dir");
		props = new Properties();
		config = new File(currentPath + File.separator + "config.xml");
	}

	@FXML
	private void setDir(ActionEvent event) {
		Stage stage = Stage.class.cast(Control.class.cast(event.getSource()).getScene().getWindow());
		DirectoryChooser chooser = new DirectoryChooser();
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

	@FXML
	private void setFolder(ActionEvent event) throws IOException {

		Stage stage = Stage.class.cast(Control.class.cast(event.getSource()).getScene().getWindow());
		DirectoryChooser chooser = new DirectoryChooser();
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
	private void startYoutube_DL(ActionEvent event) {

		url = textField_URL.getText();
		dir = textField_Dir.getText();
		folder = textField_Folder.getText();

		if (checkBox_Convert.isSelected())
			props.setProperty("Convert", "true");
		else
			props.setProperty("Convert", "false");
		if (checkBox_Merge.isSelected())
			props.setProperty("Merge", "true");
		else
			props.setProperty("Merge", "false");
		if (checkBox_UseProxy.isSelected())
			props.setProperty("UseProxy", "true");
		else
			props.setProperty("UseProxy", "false");
		if (checkBox_SaveURL.isSelected())
			props.setProperty("SaveURL", "true");
		else
			props.setProperty("SaveURL", "false");
		if (checkBox_AutoClose.isSelected())
			props.setProperty("AutoClose", "true");
		else
			props.setProperty("AutoClose", "false");
		if (checkBox_Single.isSelected())
			props.setProperty("Single", "true");
		else
			props.setProperty("Single", "false");
		if (checkBox_UseFFmpeg.isSelected())
			props.setProperty("UseFFmpeg", "true");
		else
			props.setProperty("UseFFmpeg", "false");
		if (checkBox_Thumnail.isSelected())
			props.setProperty("Thumnail", "true");
		else
			props.setProperty("Thumnail", "false");
		if (checkBox_1080P.isSelected())
			props.setProperty("1080P", "true");
		else
			props.setProperty("1080P", "false");
		if (checkBox_DownloadSub.isSelected())
			props.setProperty("DownloadSub", "true");
		else
			props.setProperty("DownloadSub", "false");
		if (checkBox_NoOverwrites.isSelected())
			props.setProperty("NoOverwrites", "true");
		else
			props.setProperty("NoOverwrites", "false");
		if (checkBox_SaveDiscription.isSelected())
			props.setProperty("SaveDiscription", "true");
		else
			props.setProperty("SaveDiscription", "false");

		setConfig();
		// validate textFields' data format are correct
		if (validateInput(dir, folder, url)) {
			statusBar_Main.setText("");
			btn_Start.setDisable(true);
			props.setProperty("Url", url);
			props.setProperty("Dir", dir);
			props.setProperty("Folder", folder);
			setConfig();

			//create user specified folder
			newFolder = new File(dir + File.separator + folder);
			if (!newFolder.exists())
				newFolder.mkdir();
			if (checkBox_SaveURL.isSelected()) {
			//save url from textField to URL.txt
				URL = new File(dir + File.separator + folder + File.separator + "URL.txt");
				if (!URL.exists())
					try {
						URL.createNewFile();
						Writer w = new FileWriter(URL);
						w.write(url);
						w.close();
					} catch (IOException e) {
						textArea_Logs.appendText("[AutoYoutube-DL] Failed to save URL\n");
					}
			}
			// if condition above is matched use Task to start youtube-dl
			var messageQueue = new LinkedBlockingQueue<String>();
			youtube_dl = new Task<Void>() {
				@Override
				public Void call() throws Exception {
					// final int numMessages = 300 ;
					Platform.runLater(() -> new MessageConsumer(messageQueue, textArea_Logs).start());
					textArea_Logs.setText("[Youtube-DL] Starting Youtube-dl \n");
					try {
						synchronized (this) {
							Runtime rt = Runtime.getRuntime();
							Process youtube_dl = rt.exec(youtube_dl_Parameters(), null, new File("/"));
							BufferedReader input = new BufferedReader(new InputStreamReader(youtube_dl.getInputStream()));
							String line = null;

							while ((line = input.readLine()) != null && !Thread.currentThread().isInterrupted()) {
								messageQueue.put(line);
							}
							input.close();
							int exitVal = youtube_dl.waitFor();
							btn_Start.setDisable(false);
							textArea_Logs.appendText("[Youtube-DL]  Exited with:" + exitVal + "\n");
							if (checkBox_Convert.isSelected()) {
								ffmpeg = new Task<Void>() {
									@Override
									public Void call() throws Exception {
										var list = new File(dir + File.separator + folder).listFiles();
										Platform.runLater(() -> new MessageConsumer(messageQueue, textArea_Logs).start());
										if (list != null)
											for (File f : list) {
												if (f.getName().endsWith((".vtt"))) {
													try {
														synchronized (this) {
															textArea_Logs.appendText("[FFmpeg] Starting FFmpeg \n");
															Runtime rt = Runtime.getRuntime();
															Process ffmpeg = rt.exec("ffmpeg.exe -n -i " + "\"" + dir
																	+ File.separator + folder + File.separator
																	+ f.getName() + "\" " + "\"" + dir + File.separator
																	+ folder + File.separator
																	+ f.getName().substring(0, f.getName().length() - 4)
																	+ ".srt" + "\"", null, new File("/"));
															BufferedReader input = new BufferedReader(
																	new InputStreamReader(ffmpeg.getInputStream()));
															String line = null;
															while ((line = input.readLine()) != null) {
																textArea_Logs.appendText(line + "\n");
															}
															input.close();
															int exitVal = ffmpeg.waitFor();
															textArea_Logs.appendText(
																	"[FFmpeg] Exited with: " + exitVal + "\n");
															btn_Start.setDisable(false);
														}

													} catch (IOException e) {
														textArea_Logs.appendText("[FATAL] FAILED_TO_START_FFMPEG \n");
													} catch (InterruptedException e) {
														textArea_Logs.appendText("[AutoYoutube-DL] INTERRUPTED_BY_USER \n");
													}
												}
											}
										return null;
									}
								};
								new Thread(ffmpeg).start();
							} else if (checkBox_AutoClose.isSelected()) {
								System.exit(1);
							} else {
								btn_Start.setDisable(false);
							}
						}
					} catch (IOException e) {
						textArea_Logs.appendText("[FATAL] ERROR_YOUTUBEDL_START_FAIL \n");
					} catch (InterruptedException e) {
						 if(Task.class.isInstance(ffmpeg)) ffmpeg.cancel();
						textArea_Logs.appendText("[Youtube-DL] INTERRUPTED_BY_USER \n");
					}
					return null;
				}
			};
			new Thread(youtube_dl).start();
//			 youtubedl = new Youtube_DL(
//					btn_Start,
//					youtube_dlParameters(), 
//					textArea_Logs,
//					checkBox_Convert.isSelected(),
//					checkBox_AutoClose.isSelected(),
//					dir + File.separator + folder);
//				youtubedl.start();
		}
	}

	@FXML
	private void interrruptYoutube_DL() {
		if (Task.class.isInstance(youtube_dl)) {
			youtube_dl.cancel();
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
			checkBox_Merge.setDisable(false);
			checkBox_Convert.setDisable(false);
		} else {
			checkBox_Merge.setDisable(true);
			checkBox_Convert.setDisable(true);
		}
	}

	@FXML
	private void downloedMode(ActionEvent event) {
		if (event.getSource().equals(checkBox_Single)) {
			if (checkBox_Single.isSelected()) {
				checkBox_List.setSelected(false);
			} else {
				checkBox_Single.setSelected(false);
			}
		} else {
			if (checkBox_List.isSelected()) {
				checkBox_Single.setSelected(false);
			} else {
				checkBox_List.setSelected(false);
			}
		}
	}

	private String youtube_dl_Parameters() {
		String youtubedlConfig = "youtube-dl.exe ";
		// String youtubedlConfig = currentPath + File.separator + "youtube-dl.exe";
		if (checkBox_DownloadSub.isSelected())
			youtubedlConfig += " --write-auto-sub --write-sub --sub-format vtt";
		if (checkBox_SaveDiscription.isSelected())
			youtubedlConfig += " --write-description";
		if (checkBox_NoOverwrites.isSelected())
			youtubedlConfig += "  --no-overwrites";
		if (checkBox_1080P.isSelected())
			youtubedlConfig += " -f bestvideo[ext=mp4]+bestaudio[ext=m4a]/bestvideo+bestaudio";
		if (checkBox_UseProxy.isSelected())
			youtubedlConfig += " --proxy http://127.0.0.1:1080/";
		if (checkBox_Single.isSelected())
			youtubedlConfig += " --no-playlist";
		if (checkBox_List.isSelected())
			youtubedlConfig += " --yes-playlist";
		if (checkBox_SaveDiscription.isSelected())
			youtubedlConfig += " --write-description";
		if (checkBox_Merge.isSelected())
			youtubedlConfig += " --ffmpeg-location " + "\"" + currentPath + "\"";
		if (checkBox_Merge.isSelected())
			youtubedlConfig += " --write-thumbnail";
		youtubedlConfig += " -o \"" + dir + File.separator + folder + "\\%(title)s.%(ext)s\"";
		youtubedlConfig += " " + url + "";
		return youtubedlConfig;
	}

	private boolean validateInput(String dir, String folder, String url) {

		/// \\ : * ? \" < > |
		// ^(\w+\.?)*\w+$ Folder name regex
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
		if (folder == null || folder.equals("")
				|| !Pattern.compile(
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
		URL u = null;
		try {
			u = new URL(url);
		} catch (MalformedURLException e) {
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
		try {
			u.toURI();
		} catch (URISyntaxException e) {
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
				if (props.getProperty("SaveDiscription").equals(True))
					checkBox_SaveDiscription.setSelected(true);
				if (props.getProperty("1080P").equals(True))
					checkBox_1080P.setSelected(true);
				if (props.getProperty("NoOverwrites").equals(True))
					checkBox_NoOverwrites.setSelected(true);
				if (props.getProperty("AutoClose").equals(True))
					checkBox_AutoClose.setSelected(true);
				if (props.getProperty("Thumnail").equals(True))
					checkBox_Thumnail.setSelected(true);
				if (props.getProperty("Single").equals(True)) {
					checkBox_Single.setSelected(true);
					checkBox_List.setSelected(false);
				} else {
					checkBox_Single.setSelected(false);
					checkBox_List.setSelected(true);
				}
				if (props.getProperty("UseFFmpeg").equals(True)) {
					checkBox_UseFFmpeg.setSelected(true);
					if (props.getProperty("Convert").equals(True))
						checkBox_Convert.setSelected(true);
					if (props.getProperty("Merge").equals(True))
						checkBox_Merge.setSelected(true);
				}
				textField_URL.setText(props.getProperty("Url"));
				textField_Folder.setText(props.getProperty("Folder"));
				textField_Dir.setText(props.getProperty("Dir"));
			} catch (IOException e) {
				textArea_Logs.appendText("[AutoYoutube-DL] Faild to read configration file\n");
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						textArea_Logs.appendText("[AutoYoutube-DL] Unkown Error occured during read configration file\n");
					}
				}
			}
		} else {
			try {
				config.createNewFile();
			} catch (IOException e) {
				textArea_Logs.appendText("[AutoYoutube-DL] Failed to create config.xml, your settings will not be saved\n");
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						textArea_Logs.appendText("[AutoYoutube-DL] Unkown Error occured during read configration file\n");
					}
				}
			}
		}
	}

	private void setConfig() {
		OutputStream output = null;
		try {
			config.createNewFile();
			output = new FileOutputStream(config);
			props.storeToXML(output, null, "UTF-8");
		} catch (IOException e) {
			textArea_Logs.appendText("[AutoYoutube-DL] Faild to Save configration file\n");
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					textArea_Logs.appendText("[AutoYoutube-DL] Unkown Error occured during save configration file\n");
				}
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		getConfig();

		if (!checkBox_UseFFmpeg.isSelected()) {
			checkBox_Merge.setDisable(true);
			checkBox_Convert.setDisable(true);
		}
		url = textField_URL.getText();
		dir = textField_Dir.getText();
		folder = textField_Folder.getText();
		textArea_Logs.textProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				textArea_Logs.setScrollTop(Double.MAX_VALUE); // this will scroll to the bottom
				// use Double.MIN_VALUE to scroll to the top
			}
		});
		statusBar_Main.setText("Ready");
	}
}
