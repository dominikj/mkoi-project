package pl.mkoi.project.facades;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;


@Component
public class CryptoFacade {

  /**
   * Returns prime number with 1 - 2**-100 probability.
   * 
   * @param bitLength - size of number
   * @return returns prime number
   */
  public BigInteger getPrimeNumber(int bitLength) {
    return BigInteger.probablePrime(bitLength, new SecureRandom());
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
  public BigInteger eulerFunction(BigInteger primeP, BigInteger primeQ) {
    return primeP.subtract(BigInteger.ONE).multiply(primeQ.subtract(BigInteger.ONE));
  }

  /**
   * Makes hash SHA2-256
   * 
   * @param message message to hash
   * @return 32B hash of message
   */
  public byte[] hash(byte[] message) {
    MessageDigest md;
    try {
      md = MessageDigest.getInstance("SHA-256");
      md.update(message);
      return md.digest();
    } catch (Exception e) {

      e.printStackTrace();
      byte[] x = null;
      return x;
    }

  }

  /**
   * converts byte array to hexadecimal string
   * 
   * @param array byte array
   * @return hexadecimal array
   */
  public String byteArrayToString(byte[] array) {

    StringBuffer result = new StringBuffer();
    for (byte b : array) {
      result.append(String.format("%02X", b));

    }
    return result.toString();

  }


  /**
   * Generates public and private key pair.
   * 
   * @param publicKey public key
   * @param privateKey private key
   * @param bitLength keys length in bits
   */
  public void generateKeys(Key publicKey, Key privateKey, int bitLength) {
    BigInteger primeP = getPrimeNumber(bitLength / 2);
    BigInteger primeQ = getPrimeNumber(bitLength / 2);
    BigInteger phi = eulerFunction(primeP, primeQ);

    // Gets coprime number from 0 < e < phi(p*q)
    // Gets randomly prime number which may be coprime to phi, thus we need to do primality test. If
    // number is not coprime, increments it and tests again
    BigInteger primeE = getPrimeNumber(bitLength);
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
}
