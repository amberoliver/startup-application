import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JCheckBox;

/*
 * User interface to automatically start the run.sh script
 * 
 *  Author: Amber Oliver and Alejandro Muniz-Samalot
 */
public class GUI{

	//GUI Attributes
	private JFrame frame;
	private JPanel panel;
	
	//Flags
	private boolean fullMap = false;
	private boolean onlineMode = false;
	private boolean researchMode = false;
	
	
	public GUI(){
		
		//Instantiate GUI attributes
		frame = new JFrame();
		panel = new JPanel();
		
		//Create frame layout start gui
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("JACart Starter Application GUI");
		frame.pack();
		frame.setVisible(true);
		
		//Set panel attributes
		panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
		panel.setLayout(new GridLayout(0, 1));
		
		//Instantiate GUI buttons/labels
		JLabel mapSizeLabel = new JLabel("Choose map size");
		JComboBox mapSizeDropDown = new JComboBox();
		mapSizeDropDown.setModel(new DefaultComboBoxModel(new String[] {"Small Map", "Full Map"}));
		JButton startCartButton = new JButton("Run Cart");
		JLabel fullMapWarning = new JLabel("WARNING full map takes longer to load!");
		JCheckBox onlineBox = new JCheckBox("Online - Connects cart to the cloud. Requires stable network connection.");
		JCheckBox researchBox = new JCheckBox("Research - Psychology research (disables features).");
		JButton killButton = new JButton("Kill Terminal Processes");
		
		//Add GUI features
		panel.add(mapSizeLabel);
		panel.add(mapSizeDropDown); 
		panel.add(fullMapWarning).setVisible(false);; 
		panel.add(onlineBox);
		panel.add(researchBox);
		panel.add(startCartButton);
		panel.add(killButton).setEnabled(false);
		
		//Set map size flags with an action listener
		mapSizeDropDown.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String s = (String) mapSizeDropDown.getSelectedItem();
				
				switch(s) {
				case "Full Map":
						fullMap = true;
						fullMapWarning.setVisible(true);
						break;
				case "Small Map":
						fullMap = false;
						fullMapWarning.setVisible(false);
						break;
				}
			}
		});
		
		//Action listener for online mode check box 
		onlineBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				onlineMode = true;
			} 
		});
		
		//Action listener for research mode check box
		researchBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				researchMode = true;
			}
		});
		
		//Start the cart with an action listener for the start button
		startCartButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				// Disable features & enable kill button
				mapSizeDropDown.setEnabled(false);
				onlineBox.setEnabled(false);
				researchBox.setEnabled(false);
				startCartButton.setEnabled(false);
				killButton.setEnabled(true);
				
				String runArgs = "";
				
				if(fullMap || onlineMode || researchMode) {
					runArgs = "-";
				}
				
				//add flags
				if(fullMap) {
					runArgs += "f";
				}
				
				if(onlineMode) {
					runArgs += "o";
				}
				
				if(researchMode) {
					runArgs += "r";
				}
				
				System.out.println("sh -c \"cd ~; cd /home/jacart/catkin_ws/src/ai-navigation; ./run.sh " + runArgs + "\" ");
				String[] cmdArgs = new String[] {"gnome-terminal", "-e", "sh -c \"cd ~; cd /home/jacart/catkin_ws/src/ai-navigation; ./run.sh " + runArgs + "\" "};
				
				//Run the terminal command 
				try {
					Process proc = new ProcessBuilder(cmdArgs).start();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
			}
		});
		
		// Action listener for the kill button
		killButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				String[] killLocalCmd = new String[] {"gnome-terminal", "--tab", "-e", "sh -c \"ps axf | grep local-server | grep -v grep | awk '{print \\\"kill -15 \\\" $1}' | sh\""};
				String[] killCartUICmd = new String[] {"gnome-terminal", "--tab", "-e", "sh -c \"ps axf | grep cart-ui-offline | grep -v grep | awk '{print \\\"kill -15 \\\" $1}' | sh\""};
				String[] killMultiCamCmd = new String[] {"gnome-terminal", "--tab", "-e", "sh -c \"ps axf | grep jacart_multi_cam | grep -v grep | awk '{print \\\"kill -15 \\\" $1}' | sh\""};
				String[] killCartControlCmd = new String[] {"gnome-terminal", "--tab", "-e", "sh -c \"ps axf | grep cart_control | grep -v grep | awk '{print \\\"kill -15 \\\" $1}' | sh\""};
				String[] killSTTCmd = new String[] {"gnome-terminal", "--tab", "-e", "sh -c \"ps axf | grep stt | grep -v grep | awk '{print \\\"kill -15 \\\" $1}' | sh\""};
				String[] killTTSCmd = new String[] {"gnome-terminal", "--tab", "-e", "sh -c \"ps axf | grep tts | grep -v grep | awk '{print \\\"kill -15 \\\" $1}' | sh\""};
//				cmdArgs[3] += "; exec bash\"";
				//Run the terminal command 
				try {
					System.out.println(killLocalCmd[2]);
					Process proc = new ProcessBuilder(killLocalCmd).start();
					proc = new ProcessBuilder(killCartUICmd).start();
					proc = new ProcessBuilder(killMultiCamCmd).start();
					proc = new ProcessBuilder(killCartControlCmd).start();
					proc = new ProcessBuilder(killSTTCmd).start();
					proc = new ProcessBuilder(killTTSCmd).start();
					
					// Disable kill & enable all other button
					mapSizeDropDown.setEnabled(true);
					onlineBox.setEnabled(true);
					researchBox.setEnabled(true);
					startCartButton.setEnabled(true);
					killButton.setEnabled(false);
					System.out.println("Kill Command sent");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		frame.pack();
	}

	public static void main(String [] args) {
		new GUI();
	}
}
