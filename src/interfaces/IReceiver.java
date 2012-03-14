package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.crypto.SealedObject;

public interface IReceiver extends Remote {
	public void propagate(IMessage message) throws RemoteException;
	public boolean connect() throws RemoteException;
}
