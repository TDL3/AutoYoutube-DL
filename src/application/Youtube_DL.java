package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.SwingWorker;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class Youtube_DL extends SwingWorker<Void, Void> {
	
	private String outDir;  //Directory to the output folder
	private String config;      //arguments to configure youtube-DL
	public TextArea textArea_Logs;
	public Button btn_start;
	private boolean convert;
	private boolean autoClose;
	private FFmpeg ffmpeg;
	
	public Youtube_DL(Button btn_start, String config, TextArea textArea_Logs, boolean convert, boolean autoClose, String outDir) {
		this.btn_start = btn_start;
		this.config = config;
		this.outDir = outDir;
		this.convert = convert;
		this.autoClose = autoClose;
		this.textArea_Logs = textArea_Logs;
		
	}

	@Override 
	protected Void doInBackground() throws Exception {
		textArea_Logs.setText("[Youtube-DL] Starting Youtube-dl \n");
    	try {
    		synchronized (this) {
    			Runtime rt = Runtime.getRuntime();
            Process youtube_dl = rt.exec(config);
            BufferedReader input = new BufferedReader(new InputStreamReader(youtube_dl.getInputStream()));
            String line=null;
            
            while((line = input.readLine()) != null) {
            		textArea_Logs.appendText(line +"\n");	
            }
            input.close();
            int exitVal = youtube_dl.waitFor();
            btn_start.setDisable(false);
            textArea_Logs.appendText("[Youtube-DL]  Exited with:" + exitVal + "\n");
            if (convert) {
         	  	ffmpeg = new FFmpeg(
                			outDir, 
                			textArea_Logs,
                			autoClose,
                			btn_start);
         	  	ffmpeg.execute();
            }
    	}      
 		} catch(IOException e) {
 			textArea_Logs.appendText("[FATAL] ERROR_YOUTUBEDL_START_FAIL \n");
 		} catch(InterruptedException e) {
 			if(FFmpeg.class.isInstance(ffmpeg)) ffmpeg.cancel(true);
 			textArea_Logs.appendText("[Youtube-DL] INTERRUPTED_BY_USER \n");
 		}
		return null;	
	}
	
	@Override 
	protected void done() {
		 
   	   if (autoClose) {
   	        System.exit(1);
  	   } else {
           btn_start.setDisable(false);
 	   }
    }
}
