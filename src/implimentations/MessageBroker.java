package implimentations;

import interfaces.IMessage;

import java.security.InvalidKeyException;
import java.security.interfaces.RSAPublicKey;
import java.util.LinkedHashMap;

import javax.crypto.Cipher;

public class MessageBroker {
	private MessageBroker() {
		// nothing.
	}
	
	public static IMessage assembleMessage(RSAPublicKey key, String to, String from, String message) {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("Protocol", "Text-Message");
		map.put("To", to);
		map.put("From", from);
		map.put("From-RSA-Mod", key.getModulus().toString());
		map.put("From-RSA-Exp", key.getPublicExponent().toString());
		map.put("Message", message);
		return Message.createNew(key, Message.AsymmetricEncryptionType.RSA, map);
	}
}
