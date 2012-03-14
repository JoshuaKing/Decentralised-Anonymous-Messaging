package gui;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.ListModel;

import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.SwingUtilities;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class NewJFrame extends javax.swing.JFrame {
	private JButton btnSend;
	private JList lstIdentities;
	private JList lstMessages;
	private JTextField txtMessage;

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				NewJFrame inst = new NewJFrame();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public NewJFrame() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			GroupLayout thisLayout = new GroupLayout((JComponent)getContentPane());
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
				ListModel lstIdentitiesModel = 
						new DefaultComboBoxModel(
								new String[] { "Item One", "Item Two" });
				lstIdentities = new JList();
				lstIdentities.setModel(lstIdentitiesModel);
			}
			{
				ListModel lstMessagesModel = 
						new DefaultComboBoxModel(
								new String[] { "Item One", "Item Two" });
				lstMessages = new JList();
				lstMessages.setModel(lstMessagesModel);
			}
			{
				txtMessage = new JTextField();
			}
			{
				btnSend = new JButton();
				btnSend.setText("Send");
			}
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
				.addGroup(thisLayout.createParallelGroup()
				    .addComponent(lstIdentities, GroupLayout.Alignment.LEADING, 0, 340, Short.MAX_VALUE)
				    .addGroup(thisLayout.createSequentialGroup()
				        .addComponent(lstMessages, GroupLayout.PREFERRED_SIZE, 340, GroupLayout.PREFERRED_SIZE)
				        .addGap(0, 0, Short.MAX_VALUE)))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(btnSend, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)
				    .addComponent(txtMessage, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)));
			thisLayout.setHorizontalGroup(thisLayout.createParallelGroup()
				.addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				    .addComponent(lstMessages, 0, 602, Short.MAX_VALUE)
				    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				    .addComponent(lstIdentities, 0, 231, Short.MAX_VALUE))
				.addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				    .addComponent(txtMessage, 0, 689, Short.MAX_VALUE)
				    .addComponent(btnSend, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)));
			pack();
			this.setSize(855, 443);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}

}
