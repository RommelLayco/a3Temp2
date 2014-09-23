package VAMIX;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class replace extends SwingWorker<Void, Integer>{
	
	private String openPathInputV;
	private String openPathInputA;
	private String savePathOutput;

	//Store file locations
	public replace(String openPathInputV, String openPathInputA, String savePathOutput){
		this.openPathInputV = openPathInputV;
		this.openPathInputA = openPathInputA;
		this.savePathOutput = savePathOutput;
	}
	
	public void replaceAudio() throws IOException{
		String cmd="avconv -i "+this.openPathInputV+" -i "+this.openPathInputA+" -c:v copy -c:a copy -map 1:0 -map 0:0 "+this.savePathOutput;
		ProcessBuilder builder2 = new ProcessBuilder("/bin/bash", "-c", cmd);
		builder2.start();
	}
	
	protected Void doInBackground() throws Exception {
	
		this.replaceAudio();
		return null;
		
	}
	
	protected void done(){
		JFrame messageFrame = new JFrame();
		JOptionPane.showMessageDialog(messageFrame, "Finished replacing","VAMIX", JOptionPane.INFORMATION_MESSAGE);
		
	}

}
