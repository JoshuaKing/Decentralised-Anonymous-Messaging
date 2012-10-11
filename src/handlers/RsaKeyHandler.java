package handlers;

import implimentations.RsaKeyPair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class RsaKeyHandler {
	private static final String keydir = System.getProperty("user.home") + File.separator + ".p2pkeys" + File.separator;
	
	private RsaKeyHandler() {
		// nothing.
	}
	
	private static boolean keyFilesExist() {
		File f = new File(keydir);
		if (!f.exists() || !f.isDirectory())
			return false;
		
		File f1, f2;
		f1 = new File(keydir + "public.key");
		f2 = new File(keydir + "private.key");
		
		if (!f1.exists() || !f1.isFile() || !f1.canRead())
			return false;
		
		if (!f2.exists() || !f2.isFile() || !f2.canRead())
			return false;
		
		return true;
	}
	
	private static boolean saveToFile(String path, BigInteger mod, BigInteger exp) {
		try {
			ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(path));
			oout.writeObject(mod);
			oout.writeObject(exp);
			oout.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private static boolean writeKeys(RsaKeyPair rpk) {
		(new File(keydir)).mkdir();
		
		try {
			KeyFactory fact = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec pub = fact.getKeySpec(rpk.getPublic(), RSAPublicKeySpec.class);
			RSAPrivateKeySpec priv = fact.getKeySpec(rpk.getPrivate(), RSAPrivateKeySpec.class);
			
			if (!saveToFile(keydir + "public.key", pub.getModulus(), pub.getPublicExponent()))
				return false;
			if (!saveToFile(keydir + "private.key", priv.getModulus(), priv.getPrivateExponent()))
				return false;
			
			System.out.println("Written RSA keys to file.");
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static RSAPublicKey readPublicFromFile(String path, KeyFactory fact) {
		try {
			ObjectInputStream oin = new ObjectInputStream(new FileInputStream(path));
			BigInteger mod = (BigInteger) oin.readObject();
			BigInteger exp = (BigInteger) oin.readObject();
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(mod, exp);
			oin.close();
			return (RSAPublicKey) fact.generatePublic(keySpec);
		} catch (Exception e) {
			return null;
		}
	}
	
	private static RSAPrivateKey readPrivateFromFile(String path, KeyFactory fact) {
		try {
			ObjectInputStream oin = new ObjectInputStream(new FileInputStream(path));
			BigInteger mod = (BigInteger) oin.readObject();
			BigInteger exp = (BigInteger) oin.readObject();
			RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(mod, exp);
			oin.close();
			return (RSAPrivateKey) fact.generatePrivate(keySpec);
		} catch (Exception e) {
			return null;
		}
	}
	
	private static RsaKeyPair readKeys() {
		try {
			KeyFactory fact = KeyFactory.getInstance("RSA");
			
			RSAPublicKey pub = readPublicFromFile(keydir + "public.key", fact);
			RSAPrivateKey priv = readPrivateFromFile(keydir + "private.key", fact);
			RsaKeyPair rkp = RsaKeyPair.setPair(pub, priv);
			
			return rkp;
		} catch (Exception e) {
			return null;
		}
	}
	
	private static RsaKeyPair generateRsaKeys() throws UnsupportedOperationException {
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			throw new UnsupportedOperationException("Could not generate RSA Keys.");
		}
		kpg.initialize(2048);
		
		RsaKeyPair rpk = RsaKeyPair.createPair(kpg);
		System.out.println("Key Pair Generated.");
		writeKeys(rpk);
		
		return rpk;
	}
	
	public static RSAPublicKey createRsaPublicKey(BigInteger mod, BigInteger exp) throws UnsupportedOperationException {
		KeyFactory fact;
		try {
			fact = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(mod, exp);
			return (RSAPublicKey) fact.generatePublic(keySpec);
		} catch (Exception e) {
			throw new UnsupportedOperationException("Could not generate RSA Keys.");
		}
	}
	
	public static RsaKeyPair getKeys() {
		if (!keyFilesExist())
			return generateRsaKeys();
		
		RsaKeyPair p = readKeys();
		if (p == null)
			return generateRsaKeys();
		
		return p;
	}
}
