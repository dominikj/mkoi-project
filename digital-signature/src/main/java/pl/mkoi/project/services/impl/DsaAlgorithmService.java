package pl.mkoi.project.services.impl;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import pl.mkoi.project.facades.CryptoFacade;
import pl.mkoi.project.keys.DsaKeyPair;
import pl.mkoi.project.keys.KeyPair;
import pl.mkoi.project.services.SignatureAlgorithmService;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.SecureRandom;

@Component("DsaAlgorithmService")
@PropertySource("configuration-default.properties")
public class DsaAlgorithmService implements SignatureAlgorithmService {

  private final CryptoFacade cryptoUtils;
  private static final Logger LOGGER = LoggerFactory.getLogger(RsapssAlgorithmService.class);
  /**
   * length (number of bits) of primeP.
   */
  @Value("${dsa.prime.n}")
  private int primeN;
  /**
   * length (number of bits) of primeQ.
   */
  @Value("${dsa.prime.l}")
  private int primeL;
  /**
   * prime P.
   */
  @Value("${dsa.prime.p}")
  private BigInteger primeP;
  /**
   * prime Q.
   */
  @Value("${dsa.prime.q}")
  private BigInteger primeQ;
  /**
   * generator G.
   */
  @Value("${dsa.generator.g}")
  private BigInteger generatorG;

  @Autowired
  public DsaAlgorithmService(CryptoFacade cryptoUtils) {
    this.cryptoUtils = cryptoUtils;

  }

  @Override
  public byte[] signFile(byte[] file, KeyPair keys) {


    KeyPair keypair = keys;


    LOGGER.info("Priv key :{}", keypair.getPrivateKey().toString());

    Signature signature = countSignatureDsa(primeP, primeQ, generatorG,
        (BigInteger) keypair.getPrivateKey(), hash(file));

    return cryptoUtils.serializeAndCodeByte64(signature.toString());
  }

  @Override
  public KeyPair genarateKeys(int keySize) {

    return generateKeysDsa(generatorG, primeP, primeQ);
  }

  @Override
  public boolean verifySign(byte[] file, byte[] sign, KeyPair keys)
      throws ClassNotFoundException, IOException {

    BigInteger[] signature = readSignString(sign);
    return this.verifySignature(signature[0], signature[1], (BigInteger) keys.getPublicKey(),
        primeP, primeQ, generatorG, file);
  }

  /**
   * reads S and R from signatureString.
   * 
   * @param sign signature in bytes array
   * @return array of two BigInteger: S and R
   * @throws IOException error during open stream
   * @throws ClassNotFoundException error during deserialization
   */
  private BigInteger[] readSignString(byte[] sign) throws ClassNotFoundException, IOException {
    String signature = cryptoUtils.decodeBase64AndDeserialize(sign);
    String[] sandr = signature.split("NumberS");
    sandr = (sandr[1]).split("NumberR");
    BigInteger[] sandrB = new BigInteger[2];
    sandrB[0] = new BigInteger(sandr[0]);
    sandrB[1] = new BigInteger(sandr[1]);
    return sandrB;
  }

  /**
   * Generates KeyPair.
   * 
   * @return KeyPair
   */
  private KeyPair generateKeysDsa(BigInteger generatorG, BigInteger primeP, BigInteger primeQ) {

    SecureRandom rand = new SecureRandom();
    BigInteger ctmp = new BigInteger(primeN + 64, rand);

    BigInteger privateX = (ctmp.mod(primeQ.subtract(BigInteger.ONE))).add(BigInteger.ONE);
    BigInteger publicY = generatorG.modPow(privateX, primeP);

    return new DsaKeyPair(privateX, publicY);

  }

  /**
   * Counts DSA signature.
   * 
   * @param primeP first number prime
   * @param primeQ second number prime
   * @param privateKey private secret key
   * @param hash hash of message to signed
   * @return signature
   * 
   */
  private Signature countSignatureDsa(BigInteger primeP, BigInteger primeQ, BigInteger generatorG,
      BigInteger privateKey, BigInteger hash) {

    SecureRandom rand = new SecureRandom();
    BigInteger secretNumberK = new BigInteger(primeQ.bitLength(), rand);

    BigInteger numberR = generatorG.modPow(secretNumberK, primeP);
    numberR = numberR.mod(primeQ);
    BigInteger numberS = ((secretNumberK.modInverse(primeQ))
        .multiply(hash.add(privateKey.multiply(numberR)).mod(primeQ))).mod(primeQ);

    return new Signature(numberS, numberR);
  }

  /**
   * Creates hash of message and converts the result to BigInteger.
   * 
   * @param message message to hash
   * @return hash in BigInteger
   */
  private BigInteger hash(byte[] message) {
    byte[] hash = cryptoUtils.hash(message);
    BigInteger hashBi = new BigInteger(hash);
    LOGGER.info("Hash in Integer :{}", hashBi.toString());
    return hashBi;
  }

  /**
   * verifies signature.
   * 
   * @param signatureS S
   * @param signatureR R
   * @param publicKey publicKey
   * @param primeP P
   * @param primeQ Q
   * @param generatorG G
   * @param file file
   * @return verification
   */
  private boolean verifySignature(BigInteger signatureS, BigInteger signatureR,
      BigInteger publicKey, BigInteger primeP, BigInteger primeQ, BigInteger generatorG,
      byte[] file) {

    if (signatureR.compareTo(primeQ) >= 0) {
      return false;
    }
    if (signatureS.compareTo(primeQ) >= 0) {
      return false;
    }

    BigInteger hash = this.hash(file);

    BigInteger wtmp = signatureS.modInverse(primeQ);
    BigInteger u1 = (hash.multiply(wtmp)).mod(primeQ);
    BigInteger u2 = (signatureR.multiply(wtmp)).mod(primeQ);
    u1 = generatorG.modPow(u1, primeP);
    u2 = publicKey.modPow(u2, primeP);
    BigInteger vvs = (((u1.multiply(u2))).mod(primeP)).mod(primeQ);

    if (vvs.equals(signatureR)) {
      return true;
    }

    return false;

  }

  private static class Signature {
    public BigInteger signatureS;
    public BigInteger signatureR;

    public Signature(BigInteger signatureS, BigInteger signatureR) {
      this.signatureS = signatureS;
      this.signatureR = signatureR;
    }

    @Override
    public String toString() {
      String string = "NumberS";
      string += signatureS.toString();
      string += "NumberR";
      string += signatureR.toString();

      return string;
    }

  }

}
