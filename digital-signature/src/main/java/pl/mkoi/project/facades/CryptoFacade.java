package pl.mkoi.project.facades;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.SecureRandom;

@Component
public class CryptoFacade {

  /**
   * Returns prime number with 1 - 2**-100 probability.
   * @param bitLength - size of number
   * @return returns prime number
   */
  private BigInteger getPrimeNumber(int bitLength) {
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
   * Generates a generator G which is needed in the DSA algorithm.
   * 
   * @param p first prime number
   * @param q second prime number
   * @return generator G
   */
  public BigInteger generateG(BigInteger p, BigInteger q) {
    BigInteger e = (p.subtract(BigInteger.ONE)).divide(q);
    BigInteger g = BigInteger.ONE;
    //FIXME: please use getPrimeNumber() method - better probability
    SecureRandom rand = new SecureRandom();
    BigInteger h = new BigInteger(p.subtract(BigInteger.ONE).bitLength(), rand);
    while (!g.equals(BigInteger.ONE)) {
      g = h.modPow(e, p);
    }
    return g;
  }

  /**
   * Counts DSA signature
   * 
   * @param primeP first number prime
   * @param primeQ second number prime
   * @param privateKey private secret key
   * @param hash hash of message to signed
   * @return signature
   * 
   */
  @SuppressFBWarnings("UPM_UNCALLED_PRIVATE_METHOD")
  private byte[] countSignatureDSA(BigInteger primeP, BigInteger primeQ, BigInteger privateKey,
      BigInteger hash) {
    SecureRandom rand = new SecureRandom();
    //FIXME: please use getPrimeNumber() method - better probability
    BigInteger secretNumberK = new BigInteger(primeQ.bitLength(), rand);
    BigInteger invertedK_1 = secretNumberK.modInverse(primeQ);

    BigInteger generatorG = generateG(primeP, primeQ);
    BigInteger r = generatorG.modPow(secretNumberK, primeP);
    r = r.mod(primeQ);
    BigInteger s = (invertedK_1.multiply(hash.add(privateKey.multiply(r)))).mod(primeQ);

    byte[] Signature = s.toByteArray();

    return Signature;
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
