package com.tdl3.sources;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.prefs.Preferences;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultCaret;

public class MainFrame extends JFrame{

	private static final long serialVersionUID = -881158862942563089L;
	
	private JTextField textField_Dir;
	private JTextField textField_NewFolder;
	private JTextField textField_URL;
	
	private JButton btnSelectFolder = new JButton("Selet Folder");
	private JButton btnStart = new JButton("Start");
	private JButton btnOpenOutput = new JButton("Output");
	
	private JLabel lblWorkDirectory = new JLabel("Work Dir");
	private JLabel lblNewFolderName = new JLabel("Folder Name");
	private JLabel lblVideoUrl = new JLabel("Video URL");
	private JLabel lblOutput = new JLabel("Log");
	
	private JCheckBox chckbxConvertVtt = new JCheckBox("Convert VTT to SRT");

	protected JTextArea textArea_Output = new JTextArea(10,15);
	
	private JScrollPane scrollBar = new JScrollPane(textArea_Output, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	private final JPanel panel = new JPanel();

	private String url, folder, dir,currentPath;
	private Preferences prefs;
	//private Worker worker;
	
	public MainFrame() {
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/com/tdl3/thiredparty/images/download.png")));
		setTitle("AutoYutube-DL Beta 1.0");
		setVisible(true);
		setSize(800,400);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		prefs = Preferences.userRoot().node(this.getClass().getName());
		
		initComponents();
		initEvents();
		
		
		try {
			currentPath = new java.io.File( "." ).getCanonicalPath();
		} catch (IOException e) {
			textArea_Output.append("UNKNOWN_ERROR_CURRENT_PATH_UNKNOWN" +"\n");
		}

	}
	
	void initComponents() {
		//Initialize Components
		btnSelectFolder.setBackground(new Color(192, 192, 192));
		btnSelectFolder.setFont(new Font("Segoe UI", Font.BOLD, 12));
		
		textField_Dir = new JTextField();
		textField_Dir.setColumns(10);
		textField_Dir.setText(prefs.get("DIR", null));
		
		textField_NewFolder = new JTextField();
		textField_NewFolder.setColumns(10);
		textField_NewFolder.setText(prefs.get("FOLDER", null));
		
		textField_URL = new JTextField();
		textField_URL.setColumns(10);
		textField_URL.setText(prefs.get("URL", null));
		
		
		lblWorkDirectory.setHorizontalAlignment(SwingConstants.RIGHT);
		lblWorkDirectory.setFont(new Font("Segoe UI", Font.BOLD, 12));
				
		lblNewFolderName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewFolderName.setFont(new Font("Segoe UI", Font.BOLD, 12));
		
		lblVideoUrl.setHorizontalAlignment(SwingConstants.RIGHT);
		lblVideoUrl.setFont(new Font("Segoe UI", Font.BOLD, 12));
		
		lblOutput.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOutput.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnStart.setBackground(new Color(192, 192, 192));
		
		btnStart.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnOpenOutput.setBackground(new Color(192, 192, 192));
		
		btnOpenOutput.setFont(new Font("Segoe UI", Font.BOLD, 12));
		textArea_Output.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		
		textArea_Output.setLineWrap(true);
		textArea_Output.setBounds(94, 182, 577, 175);
		textArea_Output.setEditable(true);
		DefaultCaret caret = (DefaultCaret)textArea_Output.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scrollBar.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		
		scrollBar.setPreferredSize(new Dimension(577, 175));
		
		panel.add(scrollBar);
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblWorkDirectory, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(textField_Dir, GroupLayout.PREFERRED_SIZE, 577, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(btnSelectFolder, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(12)
							.addComponent(lblNewFolderName, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(textField_NewFolder, GroupLayout.PREFERRED_SIZE, 577, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(12)
							.addComponent(lblVideoUrl, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(textField_URL, GroupLayout.PREFERRED_SIZE, 577, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(96)
							.addComponent(chckbxConvertVtt, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblOutput, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 577, GroupLayout.PREFERRED_SIZE)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(9)
									.addComponent(btnStart, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE))
								.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
									.addComponent(btnOpenOutput, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)))))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(23)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(lblWorkDirectory, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(1)
							.addComponent(textField_Dir, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnSelectFolder, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
							.addGap(17)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(5)
									.addComponent(lblNewFolderName, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE))
								.addComponent(textField_NewFolder, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(5)
									.addComponent(lblVideoUrl, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE))
								.addComponent(textField_URL, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
							.addGap(14)
							.addComponent(chckbxConvertVtt)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(panel, GroupLayout.PREFERRED_SIZE, 186, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblOutput)))
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnStart, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
							.addGap(119)
							.addComponent(btnOpenOutput, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
							.addGap(9)))
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);
	}

	void initEvents() {
		btnSelectFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser c = new JFileChooser();
		        c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
		        c.showSaveDialog(null);
				c.setCurrentDirectory(null);
				if (c.getSelectedFile() != null) {
					File f =c.getSelectedFile();
					textField_Dir.setText(f.getAbsolutePath());
					dir = f.getAbsolutePath();
					
				}
				
			}
		});
		
		
		btnStart.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				url = textField_URL.getText();
				dir = textField_Dir.getText();
				folder = textField_NewFolder.getText();
				
				if(!url.equals("")) prefs.put("URL", url);
				if(!dir.equals("")) prefs.put("DIR", dir);
				if(!folder.equals("")) prefs.put("FOLDER", folder);
				if(url.equals("") || dir.equals("") || currentPath.equals("")) {
					textArea_Output.setText("Invalid Input Detected\n");
				} else {
					
					new Worker(url, "\"" + dir + File.separator + folder + "\\%(title)s.%(ext)s\"", currentPath, textArea_Output).execute();
					//startClicked(url, "\"" + dir + File.separator + folder + "\\%(title)s.%(ext)s\"", currentPath);		
				}
				if(chckbxConvertVtt.isSelected()) convertVttToSrt(dir + File.separator + folder, currentPath);
			}
		});
		
		btnOpenOutput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!textField_Dir.getText().equals("")) {
					try {
						Desktop.getDesktop().open(new File(dir));
					} catch (IOException e1) {}
				}
				
			}
		});
	}
	
	/*
	 * Old method to crate process for youtube-dl which
	 * causes MainFrame freezes when youtube-dl is executing
	 * 
	private void startClicked(String url, String newdir, String currentPath) {
		
		textArea_Output.setText("Processing, Please Wait" + "\n");
		try {
           Runtime rt = Runtime.getRuntime();
           Process youtube_dl = rt.exec(
            	currentPath + File.separator + "youtube-dl.exe --proxy http://127.0.0.1:1080/ --write-thumbnail --write-auto-sub --write-sub --sub-format vtt -o " + newdir + " --ffmpeg-location " + currentPath + " -f bestvideo[ext=mp4]+bestaudio[ext=m4a]/bestvideo+bestaudio " + url
            	);
           BufferedReader input = new BufferedReader(new InputStreamReader(youtube_dl.getInputStream()));

           String line=null;
           while((line=input.readLine()) != null) {
               System.out.println(line);
               textArea_Output.append(line +"\n");
           }
           input.close();
           int exitVal = youtube_dl.waitFor();
           textArea_Output.append("Exited with code " + exitVal + "\n");

		} catch(Exception e) {
			textArea_Output.append("ERROR_YOUTUBEDL_START_FAIL");
		}
		

	}
	*/
	private void convertVttToSrt(String newdir, String currentPath) {
		File file = new File(newdir);
		File[] list = file.listFiles();
        if(list!=null)
        for (File fil : list)
        {       	
            if(fil.getName().contains(".vtt")) {
                try {
                	Runtime rt = Runtime.getRuntime();
                    Process ffmpeg = rt.exec(
                    		currentPath + File.separator + "ffmpeg.exe -i " + "\"" + newdir + File.separator + fil.getName() + "\" " + "\"" + newdir + File.separator + fil.getName() + ".srt" + "\""
                    		);
                    
                    BufferedReader input = new BufferedReader(new InputStreamReader(ffmpeg.getInputStream()));

                    String line=null;
                    while((line=input.readLine()) != null) {
                        //System.out.println(line);
                        textArea_Output.append(line +"\n");
                    }
                    input.close();
                    int exitVal = ffmpeg.waitFor();
                    textArea_Output.append("Convertion finished with exit code" + exitVal + "\n");
				} catch (Exception e) {
					textArea_Output.append("ERROR_FFMPEG_START_FAIL");
					}
                System.out.println(fil.getName());
            }
        }                                                                                                                                                         
	}
}
