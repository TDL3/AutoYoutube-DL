package com.tdl3.sources;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import javax.swing.JTextArea;
import javax.swing.SwingWorker;

public class Worker extends SwingWorker<Integer, String> {
	
	private String url, newdir, currentPath;
	private JTextArea textArea_Output;
	
	public Worker(String url, String newdir, String currentPath, JTextArea textArea_Output) {
		this.url = url;
		this.newdir = newdir;
		this.currentPath = currentPath;
		this.textArea_Output = textArea_Output;
	}

    
    protected Integer doInBackground() throws Exception
    {
    	textArea_Output.setText("Processing, Please Wait" + "\n");
    	try {
            Runtime rt = Runtime.getRuntime();
            Process youtube_dl = rt.exec(
             	currentPath + File.separator + "youtube-dl.exe --proxy http://127.0.0.1:1080/ --newline --write-thumbnail --write-auto-sub --write-sub --sub-format vtt -o " + newdir + " --ffmpeg-location " + currentPath + " -f bestvideo[ext=mp4]+bestaudio[ext=m4a]/bestvideo+bestaudio " + url
             	);
            BufferedReader input = new BufferedReader(new InputStreamReader(youtube_dl.getInputStream()));

            String line=null;
            while((line=input.readLine()) != null) {
                //System.out.println(line);
                textArea_Output.append(line +"\n");
            }
            input.close();
            int exitVal = youtube_dl.waitFor();
            textArea_Output.append("operation completed whith exit code " + exitVal + "\n");

 		} catch(Exception e) {
 			textArea_Output.append("ERROR_YOUTUBEDL_START_FAIL");
 		}
        return 1;
    }

    protected void done()
    {
        try
        {
            //JOptionPane.showMessageDialog(f, get());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    

   //add setters for label and businessDelegate    
}

