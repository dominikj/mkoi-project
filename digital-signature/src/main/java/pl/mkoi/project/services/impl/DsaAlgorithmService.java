package pl.mkoi.project.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.mkoi.project.facades.CryptoFacade;
import pl.mkoi.project.services.SignatureAlgorithmService;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;

@Component("DsaAlgorithmService")
public class DsaAlgorithmService implements SignatureAlgorithmService {

  private final CryptoFacade cryptoUtils;
  private static final Logger LOGGER = LoggerFactory.getLogger(RsapssAlgorithmService.class);
  /**
   * length (number of bits) of primeP.
   */
  private static final int N = 2048;
  /**
   * length (number of bits) of primeQ.
   */
  private static final int L = 256;

  @Autowired
  public DsaAlgorithmService(CryptoFacade cryptoUtils) {
    this.cryptoUtils = cryptoUtils;

  }

  @Override
  public String signFile(byte[] file) {

    KeyPair keyPair = generateKeysDsa();
    DSAPrivateKey privateKey = (DSAPrivateKey) keyPair.getPrivate();
    DSAPublicKey publicKey = (DSAPublicKey) keyPair.getPublic();

    LOGGER.info("Pub key :{}", publicKey.getY().toString());
    LOGGER.info("Priv key :{}", privateKey.getX().toString());

    byte[] signatureB = countSignatureDsa(cryptoUtils.getPrimeNumber(N),
        cryptoUtils.getPrimeNumber(L), privateKey.getX(), hash(file));
    String signature = cryptoUtils.byteArrayToString(signatureB);

    return signature;
  }

  /**
   * Generates KeyPair.
   * 
   * @return KeyPair
   */
  public KeyPair generateKeysDsa() {

    KeyPairGenerator keyGen;
    try {
      keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
      SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
      keyGen.initialize(1024, random);
      KeyPair pair = keyGen.generateKeyPair();
      return pair;
    } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;


  }

  /**
   * Generates a generator G which is needed in the DSA algorithm.
   * 
   * @param p first prime number
   * @param q second prime number
   * @return generator G
   */
  private BigInteger generateG(BigInteger numberP, BigInteger numberQ) {
    BigInteger numberE = (numberP.subtract(BigInteger.ONE)).divide(numberQ);
    BigInteger numberG = BigInteger.ONE;
    SecureRandom rand = new SecureRandom();
    while (numberG.equals(BigInteger.ONE)) {
      BigInteger numberH = new BigInteger(numberP.subtract(BigInteger.ONE).bitLength(), rand);
      numberG = numberH.modPow(numberE, numberP);
    }
    return numberG;
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
  private byte[] countSignatureDsa(BigInteger primeP, BigInteger primeQ, BigInteger privateKey,
      BigInteger hash) {

    SecureRandom rand = new SecureRandom();
    BigInteger secretNumberK = new BigInteger(primeQ.bitLength(), rand);
    BigInteger invertedK1 = secretNumberK.modInverse(primeQ);

    BigInteger generatorG = generateG(primeP, primeQ);
    BigInteger numberR = generatorG.modPow(secretNumberK, primeP);
    numberR = numberR.mod(primeQ);
    BigInteger numberS = (invertedK1.multiply(hash.add(privateKey.multiply(numberR)))).mod(primeQ);

    byte[] signature = numberS.toByteArray();

    return signature;
  }

  /**
   * Creats hash of message and converts the result to BigInteger.
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


}
