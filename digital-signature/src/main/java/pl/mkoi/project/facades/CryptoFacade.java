package pl.mkoi.project.facades;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.SecureRandom;

@Component
public class CryptoFacade {
  /**
   * 
   * @param bitLength - size of number
   * @param certainty - a measure of the uncertainty that the caller is willing to tolerate: if the
   *        call returns true the probability that this BigInteger is prime exceeds (1 -
   *        1/2certainty). The execution time of this method is proportional to the value of this
   *        parameter.
   * @return returns prime number
   */
  public BigInteger getPrimeNumber(int bitLength, int certainty) {
    return new BigInteger(bitLength, certainty, new SecureRandom());
  }

}
