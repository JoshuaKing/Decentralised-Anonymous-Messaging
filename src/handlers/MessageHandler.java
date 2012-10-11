package handlers;

import implimentations.SimpleMessage;

import java.security.interfaces.RSAPrivateKey;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class MessageHandler {
	RSAPrivateKey priv;
	
	public MessageHandler(RSAPrivateKey key) {
		priv = key;
	}
	
	public LinkedHashMap<String, String> decrypt(SimpleMessage message) {
		LinkedHashMap<String, String> map = message.getMap(priv);
		if (map == null) {
			System.err.println("DEBUG: Decryption Failed.");
			return null;
		}
		
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		
		System.out.println("DEBUG: Raw Message:");
		while (it.hasNext()) {
			Entry<String, String> e = it.next();
			System.out.println("\t" + e.getKey() + " = " + e.getValue());
		}
		
		return map;
	}
}
