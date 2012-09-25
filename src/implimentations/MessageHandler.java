package implimentations;

import interfaces.IMessage;

import java.security.interfaces.RSAPrivateKey;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class MessageHandler {
	RSAPrivateKey priv;
	
	public MessageHandler(RSAPrivateKey key) {
		priv = key;
	}
	
	public void setPrivateKey(RSAPrivateKey key) {
		priv = key;
	}
	
	public boolean decrypt(IMessage message) {
		LinkedHashMap<String, String> map;
		try {
			map = message.getMap(priv);
			System.out.println("DEBUG: Decryption process was successful.");
		} catch (Exception e) {
			System.out.println("DEBUG: Decryption failed: " + e.getMessage());
			return false;
		}
		
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		
		System.out.println("DEBUG: Raw Message:");
		while (it.hasNext()) {
			Entry<String, String> e = it.next();
			System.out.println("\t" + e.getKey() + " = " + e.getValue());
		}
		
		return true;
	}
}
