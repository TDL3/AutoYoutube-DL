package com.tdl3.sources;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import javax.swing.JTextArea;

public class FFmpeg extends Thread{
	private File file;
	private File[] list;
	private String newdir;
	private JTextArea textArea_Output;
	
	public FFmpeg(String newdir,JTextArea textArea_Output) {
		file = new File(newdir);
		list = file.listFiles();
		this.newdir = newdir;
		this.textArea_Output = textArea_Output;
	}

	public void run() {
        if(list!=null)
        for (File fil : list)
        {       	
            if(fil.getName().contains(".vtt")) {
                try {
                	
                	Runtime rt = Runtime.getRuntime();
                    Process ffmpeg = rt.exec(
                    		"ffmpeg.exe -i " + "\"" + newdir + File.separator + fil.getName() + "\" " + "\"" + newdir + File.separator + fil.getName() + ".srt" + "\"",
                    		null,
                    		new File("/")
                    		);
                    
                    BufferedReader input = new BufferedReader(new InputStreamReader(ffmpeg.getInputStream()));

                    String line=null;
                    while((line=input.readLine()) != null) {
                        textArea_Output.append(line +"\n");
                    }
                    input.close();
                    int exitVal = ffmpeg.waitFor();
                    textArea_Output.append("Convertion finished with exit code " + exitVal + "\n");
				} catch (Exception e) {
					textArea_Output.append("ERROR_FFMPEG_START_FAIL");
					}
            }
        }                               
	}
}
