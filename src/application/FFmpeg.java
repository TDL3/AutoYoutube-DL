package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.SwingWorker;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class FFmpeg extends SwingWorker<Void, Void> {
	private File file;
	private File[] list;
	private String outDir;
	private TextArea textArea_Logs;
	private Button btn_start;
	private boolean autoClose;
	
	public FFmpeg(String outDir, TextArea textArea_Logs, boolean autoClose, Button btn_start) {
		file = new File(outDir);
		list = file.listFiles();
		this.outDir = outDir;
		this.autoClose = autoClose;
		this.textArea_Logs = textArea_Logs;
		this.btn_start = btn_start;
	}

	@Override 
	protected Void doInBackground() throws Exception {
        if(list != null)
        for (File f : list)
        {       	
        	if (f.getName().endsWith((".vtt"))) {
                try {
                	synchronized (this) {
                		textArea_Logs.appendText("[FFmpeg] Starting FFmpeg \n");
                		Runtime rt = Runtime.getRuntime();
                		Process ffmpeg = rt.exec(
                				"ffmpeg.exe -n -i " + "\"" + outDir + File.separator + f.getName() + "\" " + "\"" + outDir + File.separator + f.getName().substring(0, f.getName().length() - 4) + ".srt" + "\"",
                				null,
                				new File("/")
                				);       
                		BufferedReader input = new BufferedReader(new InputStreamReader(ffmpeg.getInputStream()));
                		String line=null;
                		while((line=input.readLine()) != null) {
                			textArea_Logs.appendText(line +"\n");
                		}
                		input.close();
                		int exitVal = ffmpeg.waitFor();
                		textArea_Logs.appendText("[FFmpeg] Exited with: " + exitVal + "\n");
                		btn_start.setDisable(false);
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
	
	@Override
	protected void done() {
		if (autoClose) System.exit(1);
	}
}
