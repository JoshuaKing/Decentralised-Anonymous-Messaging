package implimentations;

import interfaces.IMessage;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;

import tools.Hex;

public class SimpleMessage implements IMessage {
	private static final long serialVersionUID = -5746917610976873331L;
	private SealedObject contents, secretKey;
	private final int id;
	public enum AsymmetricEncryptionType { RSA };
	
	private SimpleMessage(PublicKey key, AsymmetricEncryptionType aet, LinkedHashMap<String, String> message) throws UnsupportedOperationException, InvalidParameterException {
		id = (new Random()).nextInt();
		
		if (message == null) throw new InvalidParameterException("Message cannot be null.");
		
		message.put("Hash-Type", "SHA-512");
		
		try {
			MessageDigest mdb = MessageDigest.getInstance("SHA-512");
			message.put("Hash", Hex.asHex(mdb.digest(getNonHash(message).getBytes())));
		} catch (NoSuchAlgorithmException e) {
			throw new UnsupportedOperationException("Cannot obtain SHA 512 hash.");
		}
		
		try {
			KeyGenerator generator = KeyGenerator.getInstance("AES");
			generator.init(128);
			SecretKey secret = generator.generateKey();
			Cipher c = Cipher.getInstance("AES/CTR/PKCS5Padding");
			c.init(Cipher.ENCRYPT_MODE, secret);
			contents = new SealedObject(message, c);
			
			if (aet.equals(AsymmetricEncryptionType.RSA))
				c = Cipher.getInstance("RSA");
			else
				throw new UnsupportedOperationException("Unsupported Assymentric Encryption Type.");
			
			c.init(Cipher.ENCRYPT_MODE, key);
			secretKey = new SealedObject(secret, c);
			System.out.println("Message Securely Encrypted.");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getNonHash(LinkedHashMap<String, String> message) {
		String str = "";
		Iterator<Entry<String, String>> it = message.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> e = it.next();
			if (e.getKey().equalsIgnoreCase("hash")) continue;
			
			if (str.length() > 0) str += "&";
			str += e.getKey() + "=" + e.getValue();
		}
		
		return str;
	}
	
	public static IMessage createMessage(RSAPublicKey key, String to, String from, String message) {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("Protocol", "Text-Message");
		map.put("To", to);
		map.put("From", from);
		map.put("From-RSA-Mod", key.getModulus().toString());
		map.put("From-RSA-Exp", key.getPublicExponent().toString());
		map.put("Message", message);
		return new SimpleMessage(key, SimpleMessage.AsymmetricEncryptionType.RSA, map);
	}

	@Override
	public LinkedHashMap<String, String> getMap(PrivateKey key) throws Exception {
		// Decrypt secret key
		SecretKey secret = (SecretKey) secretKey.getObject(key);
		
		// Decrypt contents with secret key
		LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) contents.getObject(secret);
		
		// Validate the hash - Check it has not been tampered with.
		if (validateHash(map))
			return map;
		
		System.out.println("Message has been tampered with!");
		return new LinkedHashMap<String, String>();
	}

	private boolean validateHash(LinkedHashMap<String, String> message) {
		try {
			MessageDigest mdb;
			if (message.get("Hash-Type") != null)
				mdb = MessageDigest.getInstance(message.get("Hash-Type"));
			else
				mdb = MessageDigest.getInstance("SHA-512");
			
			String test = Hex.asHex(mdb.digest(getNonHash(message).getBytes()));
			return test.equals(message.get("Hash"));
		} catch (NoSuchAlgorithmException e) {
			return false;
		}
	}

	@Override
	public int getIdentifier() {
		return id;
	}

}
