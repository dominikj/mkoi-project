package pl.mkoi.project.facades;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


@Component
public class CryptoFacade {
  private static final Logger LOGGER = LoggerFactory.getLogger(CryptoFacade.class);

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
   * Makes hash SHA2-256.
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
      // Please not use generalised errors
    } catch (NoSuchAlgorithmException e) {
      // Please use logger for logging information instead of print()
      LOGGER.error("Hash function does not exits", e);
      return new byte[0];
    }

  }

  /**
   * converts byte array to hexadecimal string.
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
}
