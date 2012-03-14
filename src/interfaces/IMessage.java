package interfaces;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.LinkedHashMap;

public interface IMessage extends Serializable {
	public int getIdentifier();
	public LinkedHashMap<String, String> getMap(PrivateKey priv) throws Exception;
}
