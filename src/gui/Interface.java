package gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import handlers.FriendsHandler;
import implimentations.Receiver;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListModel;

import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;

public class Interface extends JFrame {
	private static final long serialVersionUID = 1L;
	private JButton btnSend;
	private JList lstMessages, lstFriends;
	private JTextField txtMessage;
	private static Receiver server;
	private ArrayList<String> messages;
	private static String alias;
	
	private class SendListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// Send Message //
			String message = txtMessage.getText();
			int friendid = lstFriends.getSelectedIndex();
			if (friendid == -1)
				return;
			server.sendMessage(alias, FriendsHandler.get(friendid), message);
		}
	}
	
	public static void main(String[] args) {	
		final String[] args2 = args;
		FriendsHandler.loadFriends();
		
		alias = JOptionPane.showInputDialog(null, "Please enter alias.");
		System.out.println("DEBUG: Welcome, " + alias);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Interface inst = new Interface(args2);
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public Interface(String[] args) {
		super("Decentralised Messaging - " + alias);
		messages = new ArrayList<String>();
		server = Receiver.startServer(this, args);
		initGUI();
	}
	
	private void initGUI() {
		try {
			// Set Layout //
			GroupLayout layout = new GroupLayout((JComponent)getContentPane());
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
			getContentPane().setLayout(layout);
			
			// Set close operation //
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

			// Add list of messages to user //
			ListModel lstMessagesModel = new DefaultComboBoxModel(new String[] { "From Alice: Hi", "From Eve: Bye" });
			lstMessages = new JList();
			lstMessages.setModel(lstMessagesModel);
			
			// Add friend identities //
			String[] friends = new String[FriendsHandler.getFriends().size()];
			for (int i = 0; i < friends.length; i++) {
				friends[i] = FriendsHandler.get(i).toString();
			}
			ListModel lstFriendsModel = new DefaultComboBoxModel(friends);
			lstFriends = new JList();
			lstFriends.setModel(lstFriendsModel);
			
			// Add text field for sending a message //
			txtMessage = new JTextField();
			
			// Add 'send' button to send message //
			btnSend = new JButton();
			btnSend.setText("Send");
			btnSend.addActionListener(new SendListener());

			layout.setHorizontalGroup(layout.createParallelGroup()
					.addGroup(layout.createSequentialGroup()
							.addComponent(lstMessages, 500, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(lstFriends, 200, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					)
					.addGroup(layout.createSequentialGroup()
							.addComponent(txtMessage, 50, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnSend, 50, GroupLayout.DEFAULT_SIZE, 200)
					)
			);
			
			layout.setVerticalGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup()
							.addComponent(lstMessages, 500, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(lstFriends, 500, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
							.addComponent(txtMessage, 50, GroupLayout.DEFAULT_SIZE, 200)
							.addComponent(btnSend, 50, GroupLayout.DEFAULT_SIZE, 50)
					)
			);
			
			pack();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		    //add your error handling code here
			e.printStackTrace();
		}
	}

	public void newMessage(String message) {
		messages.add(message);
		
		// Add new message to UI //
		String[] messageModel = new String[messages.size()];
		for (int i = 0; i < messageModel.length; i++) {
			messageModel[i] = messages.get(i);
		}
		lstMessages.setModel(new DefaultComboBoxModel(messageModel));
	}

}
