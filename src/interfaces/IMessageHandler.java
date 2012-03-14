package interfaces;

import java.security.interfaces.RSAPrivateKey;

public interface IMessageHandler {
	public void setPrivateKey(RSAPrivateKey key);
	public void decrypt(IMessage message);
}
