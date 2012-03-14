package implimentations;

import interfaces.IMessage;
import interfaces.IMessageHandler;
import interfaces.IReceiver;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.security.InvalidParameterException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;

public class Receiver implements IReceiver {
	
	private ArrayList<IReceiver> peerList;
	private ArrayList<String> peerHostNames;
	private ArrayList<Integer> cache;
	private IMessageHandler messageHandler;
	
	public Receiver(IMessageHandler mh) {
		if (mh == null)
			throw new InvalidParameterException("Message Handler cannot be null.");
		
		peerList = new ArrayList<IReceiver>();
		peerHostNames = new ArrayList<String>();
		cache = new ArrayList<Integer>();
		messageHandler = mh;
	}

	@Override
	public void propagate(IMessage message) throws RemoteException {
		try {
			// Check if message is in cache and has already been propogated before.
			if (cache.contains(message.getIdentifier())) {
				System.out.println("Duplicate message received.");
				return;
			}
			
			// Message is new, add it to the cache
			cache.add(message.getIdentifier());
			
			// Make sure cache size is limited
			while (cache.size() > 100)
				cache.remove(0);
			
			// Propagate message to all connected hosts
			String rhost = RemoteServer.getClientHost();
			for (int i = 0; i < peerList.size(); i++) {
				// Except the host that sent this to us.
				String hostname = peerHostNames.get(i);
				if (rhost.equals(hostname)) continue;
				Thread t = new Thread(new Propogate(peerList.get(i), message), hostname);
				t.start();
			}
			
			// Decrypt the message - it might be ours!
			System.out.println("Just received a message; Decrypting.");
			messageHandler.decrypt(message);
		} catch (Exception e) {
			System.out.println("Decryption Failed: " + e.getMessage());
		}
	}
	
	public boolean connectTo(String host) {
		try {
			Registry registry = LocateRegistry.getRegistry(host);
			IReceiver node = (IReceiver) registry.lookup("Receiver");
			peerList.add(node);
			peerHostNames.add(host);
			System.out.println("Node at " + host + " now part of peer list.");
			node.connect();
			return true;
		} catch (Exception e) {
			System.out.println("Failed to connect to node at " + host);
			return false;
		}
	}
	
	public boolean connectTo(ArrayList<String> peers) {
		boolean allPassed = true;
		for (String peer : peers) {
			if (!connectTo(peer)) 
				allPassed = false;
		}
		
		return allPassed;
	}

	public void addNode(IReceiver stub) {
		if (stub == null) return;
		peerList.add(stub);
	}

	@Override
	public boolean connect() throws RemoteException {
		try {
			String client = RemoteServer.getClientHost();
			if (peerHostNames.contains(client)) return false;
			Registry registry = LocateRegistry.getRegistry(client);
			IReceiver node = (IReceiver) registry.lookup("Receiver");
			peerList.add(node);
			peerHostNames.add(client);
			System.out.println("Node at " + client + " now part of peer list.");
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
}
