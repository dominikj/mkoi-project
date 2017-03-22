package pl.mkoi.project.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.mkoi.project.facades.CryptoFacade;
import pl.mkoi.project.keys.RsaKey;
import pl.mkoi.project.keys.RsaKeyPair;

import java.math.BigInteger;


@Component
public class RsaCryptoService {

  private final CryptoFacade cryptoUtils;
  private static final String MIN_EXPONENT = "65537";

  @Autowired
  public RsaCryptoService(CryptoFacade cryptoUtils) {
    this.cryptoUtils = cryptoUtils;
  }

  /**
   * Generates public and private key pair.
   * 
   * @param bitLength key length in bits
   * @return pair of keys
   */
  public RsaKeyPair generateKeys(int bitLength) {
    BigInteger primeP = cryptoUtils.getPrimeNumber(bitLength / 2);
    BigInteger primeQ = cryptoUtils.getPrimeNumber(bitLength / 2);
    BigInteger phi = eulerFunction(primeP, primeQ);

    // Gets coprime number from 65537 < e < phi(p*q)
    // Gets randomly prime number which may be coprime to phi, thus we need to do primality test. If
    // number is not coprime, increments it and tests again
    BigInteger floor = new BigInteger(MIN_EXPONENT);
    BigInteger primeE = cryptoUtils.getPrimeNumber(floor.bitCount());
    while (phi.gcd(primeE).compareTo(BigInteger.ONE) > 0 && primeE.compareTo(phi) < 0) {
      primeE = primeE.add(BigInteger.ONE);
    }

    BigInteger factorN = primeP.multiply(primeQ);

    RsaKey publicKey = new RsaKey();

    // Public key is (n,e)
    publicKey.setModulus(factorN);
    publicKey.setExponent(primeE);

    // Solves d*e = 1*(mod phi)
    BigInteger factorD = primeE.modInverse(phi);

    RsaKey privateKey = new RsaKey();
    // Private key is (n,d)
    privateKey.setModulus(factorN);
    privateKey.setExponent(factorD);

    RsaKeyPair keyPair = new RsaKeyPair();
    keyPair.setPublicKey(publicKey);
    keyPair.setPrivateKey(privateKey);

    return keyPair;
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

  public byte[] encrypt(byte[] message, RsaKey key) {
    return (new BigInteger(message)).modPow(key.getExponent(), key.getModulus()).toByteArray();
  }

  public byte[] decrypt(byte[] message, RsaKey key) {
    return (new BigInteger(message)).modPow(key.getExponent(), key.getModulus()).toByteArray();
  }


}
