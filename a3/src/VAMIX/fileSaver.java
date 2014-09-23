package VAMIX;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import uk.co.caprica.vlcj.filter.swing.SwingFileFilterFactory;

public class fileSaver extends JFileChooser {

	//Chose save destination
	 public static String savePath(){


		JFileChooser chooser = new JFileChooser(){	

			@Override
			public void approveSelection(){
				File f = getSelectedFile();
				if(f.exists() && getDialogType() == SAVE_DIALOG){
					int result = JOptionPane.showConfirmDialog(this,"The file exists, overwrite?","Existing file",JOptionPane.YES_NO_CANCEL_OPTION);
					switch(result){
					case JOptionPane.YES_OPTION:
						super.approveSelection();
						return;
					case JOptionPane.NO_OPTION:
						return;
					case JOptionPane.CLOSED_OPTION:
						return;
					case JOptionPane.CANCEL_OPTION:
						cancelSelection();
						return;
					}
				}
				super.approveSelection();
			}        
		};


		//filter to only audio and media files
		chooser.addChoosableFileFilter(SwingFileFilterFactory.newVideoFileFilter());
		chooser.addChoosableFileFilter(SwingFileFilterFactory.newAudioFileFilter());
		chooser.addChoosableFileFilter(SwingFileFilterFactory.newPlayListFileFilter());
		FileFilter defaultFilter = SwingFileFilterFactory.newMediaFileFilter();
		chooser.addChoosableFileFilter(defaultFilter);
		chooser.setFileFilter(defaultFilter);

		//get file
		chooser.showSaveDialog(null);
		File f = chooser.getSelectedFile();
		String path = f.getAbsolutePath();

		return path;
	}
	 
		public static String openVideoOnly() {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Specify a file to open");

			//filter to only video files
			chooser.setFileFilter(SwingFileFilterFactory.newVideoFileFilter());
			chooser.setAcceptAllFileFilterUsed(false);

			//get file
			chooser.showOpenDialog(null);
			File f = chooser.getSelectedFile();
			String path = f.getAbsolutePath();
			return path;
		}
}
