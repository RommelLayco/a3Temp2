package VAMIX;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class overlay extends SwingWorker<Void, Integer>{
	
	private String openPathInputV;
	private String openPathInputA;
	private String savePathOutput;

	//Store file locations
	public overlay(String openPathInputV, String openPathInputA, String savePathOutput){
		this.openPathInputV = openPathInputV;
		this.openPathInputA = openPathInputA;
		this.savePathOutput = savePathOutput;
	}
	
	public void overlayAV() throws IOException{
		String cmd="avconv -i "+this.openPathInputV+" -i "+this.openPathInputA+" -filter_complex amix=inputs=2:duration=first:dropout_transition=2 -acodec aac -strict experimental "+this.savePathOutput;
		ProcessBuilder builder2 = new ProcessBuilder("/bin/bash", "-c", cmd);
		builder2.start();
	}
	
	protected Void doInBackground() throws Exception {
	
		this.overlayAV();
		return null;
		
	}
	
	protected void done(){
		JFrame messageFrame = new JFrame();
		JOptionPane.showMessageDialog(messageFrame, "Finished overlaying","VAMIX", JOptionPane.INFORMATION_MESSAGE);
		
	}

}
