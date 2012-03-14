package implimentations;

import interfaces.IMessage;
import interfaces.IReceiver;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Tester {
	private static final int PORT = 1099;
	private static String peerListUrl = "http://vierware.com/test/peerlist.txt";
	
	public static ArrayList<String> getPeerList(String url) {
		ArrayList<String> peers = new ArrayList<String>();
		URLConnection conn;
		try {
			conn = (new URL(url)).openConnection();

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			
			while ((inputLine = in.readLine()) != null) 
				peers.add(inputLine);
			in.close();
		} catch (Exception e) {
			return peers;
		}
		
		return peers;
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		// Get the RSA Keys for messaging
		RsaKeyPair rkp = RsaKeyHandler.getKeys();
		Ui ui = new Ui();
		ui.setVisible(true);
		
		try {
			System.out.println("Creating Server.");
			Receiver server = new Receiver(new MessageHandler(rkp.getPrivate()));
			
			// Create a stub of the server
			IReceiver stub = (IReceiver) UnicastRemoteObject.exportObject(server, PORT);
			
			// Get the RMI registry
			Registry registry = null;
			try {
				registry = LocateRegistry.createRegistry(PORT);
			} catch (RemoteException e) {
				registry = LocateRegistry.getRegistry();
			}
			
			// Host the stub on the registry, on the specified port
			registry.bind("Receiver", stub);
			System.out.println("Server Bound.");
			
			// Testing purposes only:
			System.out.println("Generating Local Client.");
			System.setSecurityManager(new RMISecurityManager());
			server.connectTo(getPeerList(peerListUrl));	// Get list of newline-seperated IP's of other peers
			
			System.out.println("Propagating Message.");
			IMessage m = MessageBroker.newTextMessage(rkp.getPublic(), "You", "Me", "Hello, World!");
			stub.propagate(m);
			
			
			
			System.out.println("Finished Test.");
			
			// Exit cleanly - don't let the stub continually use the port
			System.exit(0);		// Will also kill all threads (like propagating threads)
		} catch (Exception e) {
			e.printStackTrace();
		}				
	}
}
