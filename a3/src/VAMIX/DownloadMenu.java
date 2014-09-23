package VAMIX;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;

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

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class DownloadMenu extends SwingWorker<Void, Integer> {

	private static boolean downloadPressed = false;
	private static boolean cancelPressed = false;

	private JFrame downloadFrame;
	private JPanel contentPane;

	private JTextField URL_field;
	private JButton downloadButton;
	private JButton continueButton;
	private JButton cancelButton;
	private JProgressBar progress;

	//Store variable need to download in swingworker
	private static String URL;
	private static String saveLocation;
	private static Process process2;



	/**
	 * Create the frame.
	 */
	public DownloadMenu() {
		//create frame
		downloadFrame = new JFrame("Download");
		downloadFrame.getContentPane().setBackground(Color.BLACK);
		downloadFrame.setBounds(100, 100, 450, 150);
		downloadFrame.setVisible(true);

		createUI();

	}

	private void createUI(){
		createControls();
		createLayout();
	}

	private void createControls(){
		URL_field = new JTextField(40);

		downloadButton = new JButton("Download");
		downloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				URL = URL_field.getText();
				System.out.println(URL);
				if(URL.equals("")){
					JOptionPane.showMessageDialog(null, "No URL typed in","VAMIX", JOptionPane.INFORMATION_MESSAGE);
				} else {
					
					saveLocation = fileSaver.savePath();

					//check if open source
					int value = JOptionPane.showConfirmDialog(null, "Is this file open source?", "VAMIX", JOptionPane.YES_NO_OPTION);


					if (value == JOptionPane.YES_OPTION){
						downloadPressed = true;
						execute();
					} else {
						JOptionPane.showMessageDialog(null, "Sorry not allowed to Download","VAMIX", JOptionPane.INFORMATION_MESSAGE);
					}

				}
			}
		});

		continueButton = new JButton("Continue");
		continueButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(cancelPressed){
					execute();		
				} else {
					JOptionPane.showMessageDialog(null, "No previous task cancelled","VAMIX", JOptionPane.INFORMATION_MESSAGE);
				}

			}
		});

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel(true);
			}
		});


		progress = new JProgressBar();
		progress.setStringPainted(true);

	}

	private void createLayout(){
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(4, 4, 4, 4));
		contentPane.setLayout(new GridLayout(3,1));

		JPanel textPanel = new JPanel();
		textPanel.setBackground(Color.BLACK);
		textPanel.add(URL_field);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.BLACK);
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(downloadButton);
		buttonPanel.add(continueButton);
		buttonPanel.add(cancelButton);

		JPanel progressPanel = new JPanel();
		progressPanel.setBackground(Color.BLACK);
		progressPanel.add(progress);

		//add to frame
		contentPane.add(textPanel);
		contentPane.add(buttonPanel);
		contentPane.add(progressPanel);
		downloadFrame.getContentPane().add(contentPane);
	}







	protected Void doInBackground() throws Exception {
		String cmd="wget --progress=dot -c " + URL +" -O " + saveLocation;


		ProcessBuilder builder2 = new ProcessBuilder("/bin/bash", "-c", cmd);
		process2 = builder2.start();
		InputStream stdout = process2.getErrorStream();
		BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
		builder2.redirectErrorStream(true);
		String line = null;
		while ((line = stdoutBuffered.readLine()) != null ) {

			if(isCancelled()){
				process2.destroy();
				cancelPressed = true;
			} else {

				String[] splitedLine = line.split("\\s+");

				if(splitedLine.length == 10){
					String valuePercent = splitedLine[7];
					String [] valueArray = valuePercent.split("%");
					String value = valueArray[0];
					int v = Integer.parseInt(value);
					publish(v);
				}
			}


		};
		return null;

	}

	@Override
	protected void process(final List<Integer> chunks) {
		// Updates the progressBar
		int position = chunks.size();
		int value = chunks.get(position-1);
		progress.setValue(value);

	}

	protected void done(){
		if(isCancelled()){
			if(!downloadPressed){
				JOptionPane.showMessageDialog(null, "Nothing to cancel","VAMIX", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Download Cancelled","VAMIX", JOptionPane.INFORMATION_MESSAGE);
				downloadFrame.dispose();
			}
		} else {
			progress.setValue(100);
			JFrame messageFrame = new JFrame();
			JOptionPane.showMessageDialog(messageFrame, "Downloading Finished","VAMIX", JOptionPane.INFORMATION_MESSAGE);
			//reset to 0
			progress.setValue(0);
			process2.destroy();
			downloadFrame.dispose();
		}

		new DownloadMenu();
	}




}
