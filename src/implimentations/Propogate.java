package implimentations;

import java.rmi.RemoteException;

import interfaces.IReceiver;

public class Propogate implements Runnable {
	IReceiver server;
	SimpleMessage message;
	
	public Propogate(IReceiver rec, SimpleMessage msg) {
		server = rec;
		message = msg;
	}
	
	@Override
	public void run() {
		if (server == null || message == null) return;
		try {
			server.propagate(message);
			System.out.println("Progagated Message.");
		} catch (RemoteException e) {
			System.out.println("Failed to send message to node.");
		}
	}

}
