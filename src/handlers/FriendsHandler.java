package handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;

public class FriendsHandler {
	private static final String keydir = System.getProperty("user.home") + File.separator + ".p2pkeys" + File.separator;
	private static ArrayList<Friend> friends = new ArrayList<Friend>();
	private static ArrayList<Friend> newFriends = new ArrayList<Friend>();
	
	public static class Friend {
		public String name;
		public RSAPublicKey pub;
		
		private Friend(String n, String m, String e) throws NoSuchAlgorithmException, InvalidKeySpecException {
			name = n;
			BigInteger mod = new BigInteger(m);
			BigInteger exp = new BigInteger(e);
			
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(mod, exp);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			pub = (RSAPublicKey) fact.generatePublic(keySpec);
		}
		
		@Override
		public String toString() {
			return name + "[" + pub.toString().substring(0, 5) + "...]";
		}
	}
	
	private FriendsHandler() {
		// No Instances of this
	}
	
	public static void loadFriends() {
		File f = new File(keydir + "friends.keys");
		if (!f.exists()) {
			return;
		}
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(f));
			while (true) {
				String name = in.readLine();
				if (name == null) break;
				
				String modula = in.readLine();
				if (modula == null) break;
				
				String exponent = in.readLine();
				if (exponent == null) break;
				
				try {
					friends.add(new Friend(name, modula, exponent));
				} catch (Exception e) {
					System.err.println("DEBUG: Could not create key for " + name);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addFriend(String name, String mod, String exp) {
		try {
			Friend f = new Friend(name, mod, exp);
			friends.add(f);
			newFriends.add(f);
		} catch (Exception e) {
			System.err.println("DEBUG: Could not add friend for " + name);
			return;
		}
	}
	
	public static void save() {
		// TODO: Save newFriends and append to friends.keys //
		System.err.println("DEBUG: Not Implemented Yet.");
	}
	
	public static int size() {
		return friends.size();
	}
	
	public static Friend get(int index) {
		return friends.get(index);
	}

	public static ArrayList<Friend> getFriends() {
		return friends;
	}
}
