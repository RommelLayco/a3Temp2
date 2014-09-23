package VAMIX;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.filter.swing.SwingFileFilterFactory;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.DefaultFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

public class MAINFRAME {

	private JFrame mainFrame;

	private final Canvas videoSurface;
	private final MediaPlayerFactory mediaPlayerFactory;
	private final JPanel controlPanel;

	private EmbeddedMediaPlayer mediaPlayer;

	private JMenuBar menuBar;
	private JMenu FileMenu;
	private JMenuItem open;
	private JMenu Edit;
	private JMenu Tools;
	private JMenuItem extract;
	private JMenuItem downloadMenu;
	private JMenuItem saveAsMenu;
	private JMenuItem replace;

	private static boolean CAN_SAVE = false;
	private JMenuItem textEditor;

	public static void main(final String[] args) {


		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MAINFRAME();
			}
		});
	}

	private MAINFRAME() {
		mainFrame = new JFrame("VAMIX");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		mainFrame.setBounds(100, 100, 500, 400);
		FullScreenStrategy fullScreenStrategy = new DefaultFullScreenStrategy(
				mainFrame);

		// create factory
		mediaPlayerFactory = new MediaPlayerFactory();
		mediaPlayerFactory.setUserAgent("VAMIX");

		videoSurface = new Canvas();

		videoSurface.setBackground(Color.black);
		videoSurface.setSize(800, 600);

		mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(fullScreenStrategy);
		mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(videoSurface));
		mediaPlayer.setPlaySubItems(true);

		// control functions
		controlPanel = new MediaControl(mediaPlayer);

		// create layout
		mainFrame.getContentPane().setLayout(new BorderLayout());
		mainFrame.setBackground(Color.black);

		mainFrame.getContentPane().add(videoSurface, BorderLayout.CENTER);
		mainFrame.getContentPane().add(controlPanel, BorderLayout.SOUTH);



		// ----------------add menu -------------------------------

		menuBar = new JMenuBar();
		mainFrame.setJMenuBar(menuBar);

		FileMenu = new JMenu("File");
		menuBar.add(FileMenu);

		open = new JMenuItem("Open");
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String p = open();
				mediaPlayer.playMedia(p);
			}
		});
		FileMenu.add(open);

		saveAsMenu = new JMenuItem("Save As");
		saveAsMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		saveAsMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		FileMenu.add(saveAsMenu);

		Edit = new JMenu("Edit");
		menuBar.add(Edit);

		Tools = new JMenu("Tools");
		Edit.add(Tools);

		extract = new JMenuItem("extract");
		extract.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame messageFrame = new JFrame();
				JOptionPane.showMessageDialog(messageFrame, "Open file to extract","VAMIX", JOptionPane.INFORMATION_MESSAGE);

				//Store file locations
				String videoPath = fileSaver.openVideoOnly();

				//save audio file
				JOptionPane.showMessageDialog(messageFrame, "Save extraction to a location","VAMIX", JOptionPane.INFORMATION_MESSAGE);
				String savePath = fileSaver.savePath();
				savePath = savePath + ".mp3";

				//save video file with no sound
				JOptionPane.showMessageDialog(messageFrame, "Save video with no sound to a location","VAMIX", JOptionPane.INFORMATION_MESSAGE);
				String videoSave = fileSaver.savePath();
				videoSave = videoSave + ".mp4";


				//check if file has sound
				Extract fileLocations = new Extract(videoPath,savePath, videoSave);
				//				int c = fileLocations.checkAudio();
				//				System.out.println(c);
				System.out.println(savePath);

				if(!(videoPath==null) && !(savePath==null) && !(videoSave == null)){
					fileLocations.execute();
				} else {
					//display pop up menu error
				}


			}
		});
		extract.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		Tools.add(extract);


		downloadMenu = new JMenuItem("Download");
		downloadMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new DownloadMenu();
			}
		});
		downloadMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));
		Edit.add(downloadMenu);

		replace = new JMenuItem("replace");
		replace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame messageFrame = new JFrame();
				JOptionPane.showMessageDialog(messageFrame, "Open video file to replace","VAMIX", JOptionPane.INFORMATION_MESSAGE);

				//Store file locations
				String videoPathInput = open();

				JOptionPane.showMessageDialog(messageFrame, "Open audio file to cover","VAMIX", JOptionPane.INFORMATION_MESSAGE);

				String audeoPathInput = open();

				JOptionPane.showMessageDialog(messageFrame, "Save extraction to a location","VAMIX", JOptionPane.INFORMATION_MESSAGE);
				String newFileSavePath = fileSaver.savePath();
				newFileSavePath =newFileSavePath+ ".mp4"; //add .mp4 to file name


				if(!(videoPathInput==null) && !(newFileSavePath==null) && !(audeoPathInput==null)){
					replace replacingLocations = new replace(videoPathInput,audeoPathInput,newFileSavePath);
					replacingLocations.execute();
				} else {
					//display pop up menu error
				}




			}
		});
		replace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		Tools.add(replace);

		textEditor = new JMenuItem("Text Editor");
		textEditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Choose file to add text","VAMIX", JOptionPane.INFORMATION_MESSAGE);
				String openFile = open();
				if(open != null){
					new Text(openFile);
				} else {
					JOptionPane.showMessageDialog(null, "Did not choose a file","VAMIX", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		textEditor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
		Tools.add(textEditor);

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