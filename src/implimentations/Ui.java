package implimentations;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Ui extends JFrame {
	public Ui() {
		setTitle("Secure P2P Network");
		setSize(1000, 700);
		setUndecorated(true);
		setBackground(new Color(50, 50, 90, 200 ));
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
}
