package pl.mkoi.project.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.mkoi.project.facades.CryptoFacade;
import pl.mkoi.project.services.SignatureAlgorithmService;

import java.math.BigInteger;
import java.security.SecureRandom;

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
  private BigInteger primeP;
  private BigInteger primeQ;
  private BigInteger generatorG;

  @Autowired
  public DsaAlgorithmService(CryptoFacade cryptoUtils) {
    this.cryptoUtils = cryptoUtils;
  
  }

  @Override
  public String signFile(byte[] file) {

    generateParameters();
    KeyPair keypair = this.generateKeysDsa(primeP, primeQ, generatorG);

    LOGGER.info("Pub key :{}", keypair.publicKey.toString());
    LOGGER.info("Priv key :{}", keypair.privateKey.toString());

    Signature signature =
        countSignatureDsa(primeP, primeQ, generatorG, keypair.privateKey, hash(file));


    return signature.toString();
  }

  /**
   * Generates KeyPair.
   * 
   * @return KeyPair
   */
  public KeyPair generateKeysDsa(BigInteger generatorG, BigInteger primeP, BigInteger primeQ) {

    SecureRandom rand = new SecureRandom();
    BigInteger ctmp = new BigInteger(N + 64, rand);

    BigInteger privateX = (ctmp.mod(primeQ.subtract(BigInteger.ONE))).add(BigInteger.ONE);
    BigInteger publicY = generatorG.modPow(privateX, primeP);

    return new KeyPair(privateX, publicY);


  }

  /**
   * Generates parameters for algorithms DSA.
   */
  private void generateParameters() {
    primeQ = cryptoUtils.getPrimeNumber(N);
    primeP = generateP(primeQ, L);
    generatorG = this.generateG(primeP, primeQ);
  }

  /**
   * Generates primeP that (primeP-1)%primeQ = 0.
   * 
   * @param primeQ primeQ
   * @param L length of primeP (in number of bits)
   * @return primeP
   */
  private BigInteger generateP(BigInteger primeQ, int length) {

    SecureRandom rand = new SecureRandom();
    BigInteger p1;
    BigInteger p2;
    do {
      p1 = BigInteger.probablePrime(length, rand);
      p2 = p1.subtract(BigInteger.ONE);
      p1 = p1.subtract(p2.remainder(primeQ));
    } while (!p1.isProbablePrime(20));
    return p1;
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

      if (numberH.compareTo(numberP) >= 0) {
        numberG = BigInteger.ONE;
      }
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
  public boolean verifySignature(BigInteger signatureS, BigInteger signatureR, BigInteger publicKey,
      BigInteger primeP, BigInteger primeQ, BigInteger generatorG, byte[] file) {

   
 

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


  private static class KeyPair {
    public BigInteger privateKey;
    public BigInteger publicKey;

    public KeyPair(BigInteger privateKey, BigInteger publicKey) {
      this.privateKey = privateKey;
      this.publicKey = publicKey;
    }

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
