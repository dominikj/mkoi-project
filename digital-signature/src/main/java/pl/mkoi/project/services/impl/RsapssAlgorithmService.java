package pl.mkoi.project.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.mkoi.project.facades.CryptoFacade;
import pl.mkoi.project.facades.Key;
import pl.mkoi.project.services.SignatureAlgorithmService;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;


@Component("RsapssAlgorithmService")
public class RsapssAlgorithmService implements SignatureAlgorithmService {

  private final CryptoFacade cryptoUtils;
  private static final Logger LOGGER = LoggerFactory.getLogger(RsapssAlgorithmService.class);
  private static final int BITS_OF_EXPONENT = 6;

  @Autowired
  public RsapssAlgorithmService(CryptoFacade cryptoUtils) {
    this.cryptoUtils = cryptoUtils;
  }

  @Override
  public String signFile(byte[] file) {
    Key pub = new Key();
    Key priv = new Key();

    generateKeys(pub, priv, 1024);
    LOGGER.info("Pub key :{} {}", pub.getExponent(), pub.getModulus());
    LOGGER.info("Priv key :{} {}", priv.getExponent(), priv.getModulus());
    // test
    byte[] message = {34, 54, 45, 65, 76, 87, 98, 43};
    byte[] enc = encrypt(message, priv);
    byte[] dec = decrypt(enc, pub);
    LOGGER.info("Test: {}", dec);

    return null;
  }

  /**
   * Generates public and private key pair.
   * 
   * @param publicKey public key
   * @param privateKey private key
   * @param bitLength keys length in bits
   */
  private void generateKeys(Key publicKey, Key privateKey, int bitLength) {
    BigInteger primeP = cryptoUtils.getPrimeNumber(bitLength / 2);
    BigInteger primeQ = cryptoUtils.getPrimeNumber(bitLength / 2);
    BigInteger phi = eulerFunction(primeP, primeQ);

    // Gets coprime number from 0 < e < phi(p*q)
    // Gets randomly prime number which may be coprime to phi, thus we need to do primality test. If
    // number is not coprime, increments it and tests again
    BigInteger primeE = cryptoUtils.getPrimeNumber(BITS_OF_EXPONENT);
    while (phi.gcd(primeE).compareTo(BigInteger.ONE) > 0 && primeE.compareTo(phi) < 0) {
      primeE = primeE.add(BigInteger.ONE);
    }

    BigInteger factorN = primeP.multiply(primeQ);

    // Public key is (n,e)
    publicKey.setModulus(factorN);
    publicKey.setExponent(primeE);

    // Solves d*e = 1*(mod phi)
    BigInteger factorD = primeE.modInverse(phi);
    // Private key is (n,d)
    privateKey.setModulus(factorN);
    privateKey.setExponent(factorD);
  }

  /**
   * Counts number of coprime numbers to p*q product in special case, when p, q are primes. In range
   * 0 < k < p*q there are p−1 distinct multiples of q and q−1 distinct multiples of p, and a bit of
   * thought shows that these two sets cannot overlap, as any positive number that was a multiple of
   * both p and q would have to be at least as large as p*q. So, in 0 < k < p*q range are (p*q-1)
   * number and (q-1) + (p -1) are not coprimes, thus phi(p*q) = (p*q-1) - (q-1) - (p-1) =
   * (q-1)*(p-1)
   * 
   * @param p first prime number
   * @param q second prime number
   * @return number of relatively prime numbers
   */
  private BigInteger eulerFunction(BigInteger primeP, BigInteger primeQ) {
    return primeP.subtract(BigInteger.ONE).multiply(primeQ.subtract(BigInteger.ONE));
  }

  public byte[] encrypt(byte[] message, Key key) {
    return (new BigInteger(message)).modPow(key.getExponent(), key.getModulus()).toByteArray();
  }

  public byte[] decrypt(byte[] message, Key key) {
    return (new BigInteger(message)).modPow(key.getExponent(), key.getModulus()).toByteArray();
  }


}
