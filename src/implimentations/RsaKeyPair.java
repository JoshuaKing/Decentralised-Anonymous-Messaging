package implimentations;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RsaKeyPair {
	RSAPublicKey pub;
	RSAPrivateKey priv;
	
	private RsaKeyPair(RSAPublicKey pub, RSAPrivateKey priv) {
		this.pub = pub;
		this.priv = priv;
	}
	
	public static RsaKeyPair setPair(RSAPublicKey pub, RSAPrivateKey priv) {
		return new RsaKeyPair(pub, priv);
	}
	
	public static RsaKeyPair createPair(KeyPairGenerator kpg) {
		KeyPair kp = kpg.genKeyPair();
		RSAPrivateKey priv = (RSAPrivateKey) kp.getPrivate();
		RSAPublicKey pub = (RSAPublicKey) kp.getPublic();
		
		return new RsaKeyPair(pub, priv);
	}
	
	public RSAPrivateKey getPrivate() {
		return priv;
	}
	
	public RSAPublicKey getPublic() {
		return pub;
	}
	
}
