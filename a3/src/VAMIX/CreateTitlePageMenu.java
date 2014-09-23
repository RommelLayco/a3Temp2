package VAMIX;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import uk.co.caprica.vlcj.filter.swing.SwingFileFilterFactory;

public class CreateTitlePageMenu extends SwingWorker<Void, Integer> {
	private static boolean createPressed = false;

	private JFrame createTFrame;
	private JPanel contentPane;

	private JTextField titleContent_field;
	private JButton createButton;
	private JButton openButton;
	private JProgressBar loading;

	//Store variable need to download in swingworker
	private static String titleContent;
	private static String openPathInputV;
	private static String saveLocation;
	private static String timeLength = "10";
	private static Process process2;



	/**
	 * Create the frame.
	 */
	public CreateTitlePageMenu() {
		//create frame
		createTFrame = new JFrame("Create a Title Page");
		createTFrame.getContentPane().setBackground(Color.BLACK);
		createTFrame.setBounds(100, 100, 450, 150);
		createTFrame.setVisible(true);

		createUI();

	}

	private void createUI(){
		createControls();
		createLayout();
	}

	private void createControls(){
		titleContent_field = new JTextField(40);

		createButton = new JButton("Create");
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("--------------------");
				
				titleContent = titleContent_field.getText();
				
				if(titleContent.equals("")){
					JOptionPane.showMessageDialog(null, "No title content typed in","VAMIX", JOptionPane.INFORMATION_MESSAGE);
				} else {
					
					saveLocation = fileSaver.savePath();
					saveLocation = saveLocation+".mp4";

						createPressed = true;
						loading.setIndeterminate(true);
						execute();
					 

				}
			}
		});
		openButton = new JButton("Open");
		openButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				openPathInputV = open();
				
			}
		});

		loading = new JProgressBar();
		loading.setValue(0);

	}

	private void createLayout(){
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(4, 4, 4, 4));
		contentPane.setLayout(new GridLayout(3,1));

		JPanel textPanel = new JPanel();
		textPanel.setBackground(Color.BLACK);
		textPanel.add(titleContent_field);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.BLACK);
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(createButton);
		buttonPanel.add(openButton);
		
		JPanel loadingPanel = new JPanel();
		loadingPanel.add(loading);

		//add to frame
		contentPane.add(textPanel);
		contentPane.add(buttonPanel);
		contentPane.add(loading);
		createTFrame.getContentPane().add(contentPane);
	}


	public void createNewPage() throws IOException, InterruptedException{
		String cmd="avconv -y -filter_complex \"color=0x000000:320x240:24 [lead]; [lead] drawtext=fontfile='/usr/share/fonts/truetype/droid/DroidSans.ttf':text='"+titleContent+"':x=(main_w-text_w)/2:y=20:fontsize=36:fontcolor=0xFFFF00\" -t "+timeLength+" title.mp4";
		ProcessBuilder builder2 = new ProcessBuilder("/bin/bash", "-c", cmd);
		Process p = builder2.start();
		p.waitFor();
		JOptionPane.showMessageDialog(null, "Finished step 1 of 4","VAMIX", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void createTsfile1() throws IOException, InterruptedException{
		String cmd="avconv -y -ss 0 -i "+openPathInputV+" -t 60 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y a.ts";
		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
		Process p = builder.start();
		p.waitFor();
		JOptionPane.showMessageDialog(null, "Finished step 2 of 4","VAMIX", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void createTsfile2() throws IOException, InterruptedException{
		String cmd="avconv -y -ss 0 -i title.mp4 -t 60 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y b.ts";
		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
		Process p = builder.start();
		p.waitFor();
		JOptionPane.showMessageDialog(null, "Finished step 3 of 4","VAMIX", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void combineTogether() throws IOException, InterruptedException{
		String cmd="avconv -y -i concat:\"b.ts|a.ts\" -c copy -bsf:a aac_adtstoasc -y "+saveLocation;
		System.out.println(saveLocation);
		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
		Process p = builder.start();
		p.waitFor();
		JOptionPane.showMessageDialog(null, "Finished step 4 of 4","VAMIX", JOptionPane.INFORMATION_MESSAGE);
	
	} 
	
	public void removeFiles() throws IOException, InterruptedException{
		String cmd="rm a.ts b.ts title.mp4";
		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
		Process p = builder.start();
		p.waitFor();
	} 




	protected Void doInBackground() throws Exception {
		    this.createUI();
			this.createNewPage();
			this.createTsfile1();
			this.createTsfile2();
			this.combineTogether();
			this.removeFiles();
			return null;
				
	}

	@Override


	protected void done(){
		JFrame messageFrame = new JFrame();
		JOptionPane.showMessageDialog(messageFrame, "Title page creation complete","VAMIX", JOptionPane.INFORMATION_MESSAGE);
		
		loading.setIndeterminate(false);
		loading.setValue(0);
		createTFrame.dispose();
	}


	public String open() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Specify a file to open");

		//filter to only audio and media files
		chooser.addChoosableFileFilter(SwingFileFilterFactory.newVideoFileFilter());
		chooser.addChoosableFileFilter(SwingFileFilterFactory.newAudioFileFilter());
		chooser.addChoosableFileFilter(SwingFileFilterFactory.newPlayListFileFilter());
		FileFilter defaultFilter = SwingFileFilterFactory.newMediaFileFilter();
		chooser.addChoosableFileFilter(defaultFilter);
		chooser.setFileFilter(defaultFilter);

		//get file
		chooser.showOpenDialog(null);
		File f = chooser.getSelectedFile();
		String path = f.getAbsolutePath();
		return path;
	}



}
